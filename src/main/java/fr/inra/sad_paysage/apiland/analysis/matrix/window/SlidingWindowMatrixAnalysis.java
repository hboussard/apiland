package fr.inra.sad_paysage.apiland.analysis.matrix.window;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import fr.inra.sad_paysage.apiland.analysis.process.Process;
import fr.inra.sad_paysage.apiland.analysis.matrix.process.WindowMatrixProcess;
import fr.inra.sad_paysage.apiland.analysis.matrix.process.WindowMatrixProcessType;
import fr.inra.sad_paysage.apiland.analysis.metric.Metric;
import fr.inra.sad_paysage.apiland.analysis.process.ProcessState;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.Raster;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.matrix.Matrix;
import fr.inra.sad_paysage.apiland.window.Window;

public class SlidingWindowMatrixAnalysis extends WindowMatrixAnalysis implements VolatileWindowAnalysis {
	
	private final int delta;
	
	private WindowMatrixProcess[] processes;
	
	private LinkedList<WindowMatrixProcess> finishProcesses;
	
	private Set<Integer> filters;
	
	private Set<Integer> unfilters;
	
	private Matrix matrixFilter;
	
	private Matrix matrixUnFilter;
	
	public SlidingWindowMatrixAnalysis(Matrix m, Window w, WindowMatrixProcessType pType, int d, double minRate, 
			Set<Integer> filters, Set<Integer> unfilters, Matrix matrixFilter, Matrix matrixUnFilter) {
		super(m, w, pType);
		
		this.delta = d;
		Metric.setMinRate(minRate);
		
		this.filters = new HashSet<Integer>();
		this.filters.addAll(filters);
		
		this.unfilters = new HashSet<Integer>();
		this.unfilters.addAll(unfilters);
		
		this.matrixFilter = matrixFilter;
		
		this.matrixUnFilter = matrixUnFilter;
	}

	@Override
	public int delta(){
		return delta;
	}

	@Override
	public Pixel next(Pixel pixel){
		pixel.setX(pixel.x()+delta);
		if(matrix().contains(pixel)){
			return pixel;
		}
		pixel.setX(0);
		pixel.setY(pixel.y()+delta);
		if(matrix().contains(pixel)){
			return pixel;
		}
		return null;
	}

	@Override
	protected void doInit() {
		// do nothing 
	}
	
	@Override
	protected void doClose() {
		// do nothing 
	}
	
	@Override
	protected void doRun() {
		
		processes = new WindowMatrixProcess[matrix().width()];
		createProcesses();
		
		for(int yt=0; yt<matrix().numYTiles(); yt++){
			//System.out.println("Sliding Window : "+yt);
			for(int xt=0; xt<matrix().numXTiles(); xt++){
				distributeValues(xt, yt);	
			}
		}
		
		if(finishProcesses != null){
			while(finishProcesses.size() > 0){
				WindowMatrixProcess wmp = finishProcesses.removeFirst();
				//System.out.println();
				//System.out.println(wmp.pixel());
				calculate(wmp);
			}
		}
		
		window().close();
	}

	private void createProcesses(){
		for(int x=0; x<processes.length; x++){
			if(x%delta == 0){
				processes[x] = processType().create(window(), new Pixel(x, 0));
			}
		}
	}
	
	private void distributeValues(int xt, int yt) {
		double v;
		int total = matrix().width()*matrix().height();
		for(int y=yt*matrix().tileHeight(); y<(yt+1)*matrix().tileHeight() && y<matrix().height(); y++) {
			//System.out.println(y+" / "+matrix().height());
			for(int x=xt*matrix().tileWidth(); x<(xt+1)*matrix().tileWidth() && x<matrix().width(); x++){
				v = matrix().get(x, y);
				//System.out.println("distribution du pixel ("+x+", "+y+") avec la valeur "+v);
				processType().setValue(x, y, v, null);
				// recherche spatialement indexee des process potentiellement interesses
				for(int ix=Math.max(0, x-(window().width()/2)); ix<=Math.min(matrix().width()-1, x+(window().width()/2)); ix++){
					
					if(processes[ix] != null){
						//System.out.println(ix);
						processes[ix].add(x, y, v);
					}
				}
				updateProgression(total);
			}
		}
	}

	@Override
	public void notifyFromProcess(Process p, ProcessState s) {
		if(p instanceof WindowMatrixProcess && p.processType().equals(processType())){
			switch(s){
			case INIT : notifyProcessInit((WindowMatrixProcess) p); break;
			case READY : notifyProcessReady((WindowMatrixProcess) p); break;
			case DONE : notifyProcessDone((WindowMatrixProcess) p); break;
			case FINISH : notifyProcessFinish((WindowMatrixProcess) p); break;
			}
		}
	}

	private void notifyProcessInit(WindowMatrixProcess wp) {
		// do nothing
	}
	
	private void notifyProcessReady(WindowMatrixProcess wp) {
		calculate(wp);
	}

	private void notifyProcessDone(WindowMatrixProcess wp) {
		//wp.window().infos();
		//System.out.println(wp+" done");
		wp.down(delta);
	}
	
	private void notifyProcessFinish(WindowMatrixProcess wp) {
		if(finishProcesses == null){
			finishProcesses = new LinkedList<WindowMatrixProcess>();
		}
		finishProcesses.add(wp);
	}
	
	private void calculate(WindowMatrixProcess wp){
		double v = matrix().get(wp.x(), wp.y());
		boolean filter = true;
		if(unfilters.contains((int) v) || (filters.size() > 0 && !filters.contains((int) v))
				|| ((matrixFilter != null) && ((matrixFilter.get(wp.x(), wp.y()) == Raster.getNoDataValue()) || (matrixFilter.get(wp.x(), wp.y()) == 0)))
				|| ((matrixUnFilter != null) && matrixUnFilter.get(wp.x(), wp.y()) != 0)){
			filter = false;
		}
		
		if(filter){
			wp.calculateMetrics();
		}else{
			wp.unCalculateMetrics();
		}
	}

}
