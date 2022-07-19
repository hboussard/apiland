package fr.inra.sad.bagap.apiland.analysis.matrix;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;

public class AreaAnalysis extends MatrixAnalysis {

	double area;
	
	public AreaAnalysis(Matrix m){
		super(m);
	}
	
	@Override
	protected void doInit() {
		area = 0;
	}

	@Override
	protected void doRun() {
		for(Pixel p : matrix()){
			if(matrix().get(p) == 1){
				area += Math.pow(Raster.getCellSize(), 2);
			}
		}
		setResult(area);
	}

	@Override
	protected void doClose() {
		// do nothing
	}

}
