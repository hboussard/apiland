package fr.inra.sad_paysage.apiland.analysis.matrix.calculation;

import fr.inra.sad_paysage.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.matrix.Matrix;

public class TruncateMinMaxMatrixCalculation extends Pixel2PixelMatrixCalculation {

	private double min, max;
	
	public TruncateMinMaxMatrixCalculation(Matrix m, double min, double max){
		super(m);
		this.min = min;
		this.max = max;
	}
	
	@Override
	public void doInit() {}
	
	@Override
	protected double treatPixel(Pixel p) {
		double v = matrix().get(p);
		if(v > min && v < max){
			return 1;
		}
		return 0;
	}

	@Override
	public void doClose() {}

}