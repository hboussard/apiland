package fr.inra.sad.bagap.apiland.analysis.matrix.pixel;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;

public class DecoupeAnalysis extends Pixel2PixelMatrixCalculation {

	public DecoupeAnalysis(Matrix... m){
		super(m);
	}
	
	@Override
	protected double treatPixel(Pixel p) {
		if(matrix(1).get(p) == Raster.getNoDataValue()){
			return Raster.getNoDataValue();
		}
		return matrix(0).get(p);
	}

	@Override
	protected void doInit() {}

	@Override
	protected void doClose() {}

}
