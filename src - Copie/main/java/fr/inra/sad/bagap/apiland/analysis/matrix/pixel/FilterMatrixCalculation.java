package fr.inra.sad.bagap.apiland.analysis.matrix.pixel;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;

public class FilterMatrixCalculation extends Pixel2PixelMatrixCalculation {
	
	public FilterMatrixCalculation(Matrix... m){
		super(m);
	}
	
	@Override
	protected double treatPixel(Pixel p) {
		double v0 = matrix(1).get(p);
		if(v0 != Raster.getNoDataValue() && v0 != 0){
			return matrix(0).get(p);
		}
		return 0.0;
	}

	@Override
	protected void doInit() {}

	@Override
	protected void doClose() {}
	

}
