package fr.inra.sad.bagap.apiland.analysis.matrix.window;

import java.util.Map;
import java.util.TreeMap;

import fr.inra.sad.bagap.apiland.analysis.matrix.process.WindowMatrixProcess;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.WindowMatrixProcessType;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.type.Window;
import fr.inra.sad.bagap.apiland.analysis.process.Process;
import fr.inra.sad.bagap.apiland.analysis.process.ProcessState;
import fr.inra.sad.bagap.apiland.analysis.process.metric.Metric;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.PixelManager;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;

public class GridWindowMatrixAnalysisOld extends WindowMatrixAnalysis implements VolatileWindowAnalysis {
	
	private final int gridSize;
	
	private Map<Integer, Map<Integer, WindowMatrixProcess>> processes;
	
	//private Set<WindowProcess> dones;
	
	//private boolean update;
	
	private int xp, yp;
	
	private int nextx, nexty;
	
	public GridWindowMatrixAnalysisOld(Matrix m, Window w, WindowMatrixProcessType pType, int d, double minRate) {
		super(m, w, pType);
		this.gridSize = d;
		Metric.setMinRate(minRate);
	}

	@Override
	public int delta(){
		return gridSize;
	}

	@Override
	public Pixel next(Pixel pixel){
		Pixel p = PixelManager.get(pixel.x()+gridSize, pixel.y());
		if(matrix().contains(p)){
			return p;
		}
		p = PixelManager.get(0, pixel.y()+gridSize);
		if(matrix().contains(p)){
			return p;
		}
		return null;
	}

	@Override
	protected void doInit() {
		//update = false;
	}
	
	@Override
	protected void doClose() {
		// do nothing 
	}

	@Override
	protected void doRun() {
		//dones = new HashSet<WindowProcess>();
		processes = new TreeMap<Integer, Map<Integer, WindowMatrixProcess>>();
		nextx = 0;
		nexty = 0;
		int ypt;
		for(int yt=0; yt<matrix().numYTiles(); yt++){
			yp = nexty;
			ypt = yp;
			for(int xt=0; xt<matrix().numXTiles(); xt++){
				xp = nextx;
				yp = ypt;
				//System.out.println(xt+" "+yt+" "+nextx+" "+nexty);
				createProcesses(xt, yt);
				distributeValues(xt, yt);	
			}
		}
	}

	private void createProcesses(int xt, int yt){
		
		
		if(yt%gridSize == 0){
			//System.out.println("creation des processus en "+xt+" "+yt);
			WindowMatrixProcess wp;
			boolean yi = false;
			boolean xi = false;
			int x = xp;
			
			for(; yp<(yt+1)*matrix().tileHeight()+(window().height()/2)+1 && yp<matrix().height(); yp+=gridSize){
				yi = true;
				
				//System.out.println(yp);
				
				if(!processes.containsKey(yp)){
					processes.put(yp, new TreeMap<Integer, WindowMatrixProcess>());
				}
				
				for(xp = x; xp<(xt+1)*matrix().tileWidth()+(window().width()/2)+1 && xp<matrix().width(); xp+=gridSize){
					xi = true;
					
					if(!processes.get(yp).containsKey(xp)){	
						System.out.println("creation du processus en "+xp+" "+yp);
						wp = processType().create(window(), PixelManager.get(xp, yp));
						
						processes.get(yp).put(xp, wp);
					}
				}
			}
			
			if(!(xi && yi && xp >= matrix().width() && yp >= matrix().height())){
				if(xi){
					if(xp >= matrix().width()){
						nextx = 0;
					}else{
						nextx = xp;
					}
				}
				
				if(yi){
					if(yp >= matrix().height()){
						nexty = 0;
					}else{
						nexty = yp;
					}	
				}
			}else{
				nextx = 0;
				nexty = yp;
			}
		}
		
	}
	
	private void distributeValues(int xt, int yt) {
		
		if(xt%gridSize == 0){
			//System.out.println("distribution des valeurs de la tuile "+xt+" "+yt);
			double v;
			Pixel p;
			boolean ok;
			int total = matrix().width()*matrix().height();
			for(int y=yt*matrix().tileHeight(); y<(yt+1)*matrix().tileHeight() && y<matrix().height(); y++) {
				for(int x=xt*matrix().tileWidth(); x<(xt+1)*matrix().tileWidth() && x<matrix().width(); x++){
						
					v = matrix().get(x, y);
					processType().setValue(x, y, v, null);
					
					ok = false;
					
					// recherche spatialement indexee des process potentiellement interesses
					for(int iy=y-window().height()+1; !ok && iy<=y; iy++){
						if(processes.containsKey(iy)){
							for(int ix=x-window().width()+1; !ok && ix<=x; ix++){
								if(processes.get(iy).containsKey(ix)){
									processes.get(iy).get(ix).add(x, y, v);
									ok = true;
								}
							}
						}
					}
					//update();
					updateProgression(total);
				}
			}
		}
		
	}

	/*
	private void update(){
		if(update){
			update = false;
			
			if(dones.size() > 0){
				for(WindowProcess wp : dones){
					processes.get(wp.y()).remove(wp.x());
					//System.out.println("fermeture du processus ("+wp.x()+", "+wp.y()+")");
					if(processes.get(wp.y()).size() == 0){
						processes.remove(wp.y());
					}
				}
				dones.clear();
			}
		}
	}*/

	@Override
	public void notify(Process p, ProcessState s) {
		if(p instanceof WindowMatrixProcess && p.processType().equals(processType())){
			switch(s){
			case INIT : notifyProcessInit((WindowMatrixProcess)p); break;
			case READY : notifyProcessReady((WindowMatrixProcess) p); break;
			case DONE : notifyProcessDone((WindowMatrixProcess)p); break;
			}
		}
	}
	
	private void notifyProcessInit(WindowMatrixProcess p) {
		//update = true;
	}
	
	private void notifyProcessReady(WindowMatrixProcess p) {
		p.calculateMetrics();
	}

	private void notifyProcessDone(WindowMatrixProcess p) {
		//dones.add(p);
		p.delete();
		//update = true;
	}
}


