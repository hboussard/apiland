package fr.inra.sad.bagap.apiland.analysis.matrix.pixel;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;

public class TruncateMaxMatrixCalculation extends Pixel2PixelMatrixCalculation {

	private double max;
	
	public TruncateMaxMatrixCalculation(double max, Matrix m){
		super(m);
		this.max = max;
	}
	
	@Override
	public void doInit() {}
	
	@Override
	protected double treatPixel(Pixel p) {
		double v = matrix().get(p);
		if(v < max){
			return 1;
		}
		return 0;
	}

	@Override
	public void doClose() {}

}
