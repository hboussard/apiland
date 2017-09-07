package fr.inra.sad_paysage.apiland.analysis.matrix.window;

import fr.inra.sad_paysage.apiland.window.Window;
import fr.inra.sad_paysage.apiland.analysis.process.Process;
import fr.inra.sad_paysage.apiland.analysis.matrix.process.WindowMatrixProcess;
import fr.inra.sad_paysage.apiland.analysis.matrix.process.WindowMatrixProcessType;
import fr.inra.sad_paysage.apiland.analysis.process.ProcessState;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.Raster;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.matrix.Matrix;

public class MapWindowMatrixAnalysis extends WindowMatrixAnalysis implements VolatileWindowAnalysis{
	
	private WindowMatrixProcess process;
	
	private Matrix matrixFilter;
	
	private Matrix matrixUnFilter;
	
	public MapWindowMatrixAnalysis(Matrix m, Window w, WindowMatrixProcessType pType, Matrix matrixFilter, Matrix matrixUnFilter) {
		super(m, w, pType);
		this.matrixFilter = matrixFilter;
		this.matrixUnFilter = matrixUnFilter;
	}

	@Override
	protected void doInit() {
		// do nothing 
	}

	@Override
	protected void doRun() {
		
		process = processType().create(window(), new Pixel(0, 0));
		
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
			for(int x=xt*matrix().tileWidth(); x<(xt+1)*matrix().tileWidth() && x<matrix().width(); x++){
				
				//p = PixelManager.get(x, y);
				//System.out.println("distribution du pixel "+p);
				
				v = matrix().get(x, y);
				processType().setValue(x, y, v, null);
				
				if((matrixFilter != null && matrixFilter.get(x, y) == 0) || 
						matrixUnFilter != null && matrixUnFilter.get(x, y) != 0){
					// pas de traitement
					//System.out.println("rien en "+x+" "+y);
					process.add(x, y, Raster.getNoDataValue());
				}else{
					//System.out.println("process en "+x+" "+y);
					process.add(x, y, v);
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
		p.calculateMetrics();
	}

	private void notifyProcessDone(WindowMatrixProcess p) {
		p.delete();
	}

	@Override
	public Pixel next(Pixel pixel) {
		return pixel;
	}
	
}
