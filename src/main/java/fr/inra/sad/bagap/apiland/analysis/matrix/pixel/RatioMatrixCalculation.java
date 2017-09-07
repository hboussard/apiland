package fr.inra.sad.bagap.apiland.analysis.matrix.pixel;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;

public class RatioMatrixCalculation extends Pixel2PixelMatrixCalculation {
	
	private double ratio; // ratio multiplicateur
	
	public RatioMatrixCalculation(double ratio, Matrix m){
		super(m);
		this.ratio = ratio;
	}
	
	@Override
	public void doInit() {	}
	
	@Override
	protected double treatPixel(Pixel p) {
		return matrix().get(p) * ratio;
	}

	@Override
	public void doClose() {	}


}

