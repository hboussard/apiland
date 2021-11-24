package fr.inra.sad.bagap.apiland.analysis.matrix.window;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import fr.inra.sad.bagap.apiland.analysis.matrix.process.WindowMatrixProcess;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.WindowMatrixProcessType;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.type.Window;
import fr.inra.sad.bagap.apiland.analysis.process.Process;
import fr.inra.sad.bagap.apiland.analysis.process.ProcessState;
import fr.inra.sad.bagap.apiland.cluster.Cluster;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.type.MultipleWindow;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.type.SimpleWindow;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.shape.FilteredRectangularWindow;

public class ClusteredWindowMatrixAnalysis extends WindowMatrixAnalysis {

	private Set<Cluster> clusters;
	
	private Map<Integer, WindowMatrixProcess> processes;
	
	public ClusteredWindowMatrixAnalysis(Matrix m, Window w, WindowMatrixProcessType pType, Set<Cluster> clusters) {
		super(m, w, pType);
		this.clusters = clusters; 
	}

	@Override
	protected void doInit() {
		// do nothing 
	}

	@Override
	protected void doRun() {
		
		processes = new HashMap<Integer, WindowMatrixProcess>();
		for(Cluster c : clusters){
			Window window = null;
			for(Window w : ((MultipleWindow) window()).windows()){
				if(((FilteredRectangularWindow) ((SimpleWindow) w).shape()).getValue() == c.getValue()){
					window = w;
				}
			}
			//System.out.println(window.getClass());
			//System.out.println(c.getCorner());
			window.locate(c.getCorner());
			processes.put(c.getValue(), processType().create(window, c.getCorner())); 
			//System.out.println(window.pixel());
		}
		
		for(int yt=0; yt<matrix().numYTiles(); yt++){
			for(int xt=0; xt<matrix().numXTiles(); xt++){
				distributeValues(xt, yt);	
			}
		}
	}
	
	@Override
	protected void doClose() {
		// do nothing 
	}

	private void distributeValues(int xt, int yt) {
		//System.out.println("distribution des valeurs de la tuile "+xt+" "+yt);
		double v;
		Pixel p;
		//boolean active, ok;
		int total = matrix().width()*matrix().height();
		for(int y=yt*matrix().tileHeight(); y<(yt+1)*matrix().tileHeight() && y<matrix().height(); y++) {
			System.out.println(y);
			for(int x=xt*matrix().tileWidth(); x<(xt+1)*matrix().tileWidth() && x<matrix().width(); x++){
				
				//p = PixelManager.get(x, y);
				//System.out.println("distribution du pixel "+x+" "+y);
				
				v = matrix().get(x, y);
				processType().setValue(x, y, v, null);
				
				for(WindowMatrixProcess wmp : processes.values()){
					wmp.add(x, y, v);
				}
				
				updateProgression(total);
			}
		}
	}

	@Override
	public void notify(Process p, ProcessState s) {
		if(p instanceof WindowMatrixProcess && p.processType().equals(processType())){
			switch(s){
			case READY : notifyProcessReady((WindowMatrixProcess) p); break;
			case DONE : notifyProcessDone((WindowMatrixProcess) p); break;
			}
		}
	}
	
	private void notifyProcessReady(WindowMatrixProcess p) {
		//System.out.println("calculate "+p);
		p.calculateMetrics();
	}

	private void notifyProcessDone(WindowMatrixProcess p) {
		p.delete();
	}

	
}
