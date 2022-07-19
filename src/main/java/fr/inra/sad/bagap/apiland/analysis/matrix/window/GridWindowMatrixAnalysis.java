package fr.inra.sad.bagap.apiland.analysis.matrix.window;

import fr.inra.sad.bagap.apiland.analysis.matrix.process.WindowMatrixProcess;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.WindowMatrixProcessType;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.type.Window;
import fr.inra.sad.bagap.apiland.analysis.process.Process;
import fr.inra.sad.bagap.apiland.analysis.process.ProcessState;
import fr.inra.sad.bagap.apiland.analysis.process.metric.Metric;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.PixelManager;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;

public class GridWindowMatrixAnalysis extends WindowMatrixAnalysis implements VolatileWindowAnalysis {
	
	private final int gridSize;
	
	private int xp, yp;
	
	private int nextx, nexty;
	
	public GridWindowMatrixAnalysis(Matrix m, Window w, WindowMatrixProcessType pType, int d, double minRate) {
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
		nextx = 0;
		nexty = 0;
		int ypt;
		for(int yt=0; yt<matrix().numYTiles(); yt++){
			yp = nexty;
			ypt = yp;
			for(int xt=0; xt<matrix().numXTiles(); xt++){
				xp = nextx;
				yp = ypt;
				createProcesses(xt, yt);
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
				
				for(xp = x; xp<(xt+1)*matrix().tileWidth()+(window().width()/2)+1 && xp<matrix().width(); xp+=gridSize){
					xi = true;
					wp = processType().create(window(), PixelManager.get(xp, yp));
					distributeValues(wp, xp, yp);
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
	
	private void distributeValues(WindowMatrixProcess wp, int xp, int yp) {
		
		//System.out.println("distribution des valeurs de la grille "+xp+" "+yp);
		double v;
		
		int total = matrix().width()*matrix().height();
		for(int y=yp; y<yp+gridSize && y<matrix().height(); y++) {
			for(int x=xp; x<xp+gridSize && x<matrix().width(); x++){
				//System.out.println("distribution du pixel "+x+" "+y);
				v = matrix().get(x, y);
				processType().setValue(x, y, v, null);
				
				wp.add(x, y, v);
								
				//update();
				updateProgression(total);
			}
		}
		
		wp.calculateMetrics();
	}
	
	@Override
	public void notify(Process p, ProcessState s) {
		// do nothing
	}

}


