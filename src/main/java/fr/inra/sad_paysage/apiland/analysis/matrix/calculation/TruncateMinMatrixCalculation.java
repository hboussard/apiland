package fr.inra.sad_paysage.apiland.analysis.matrix.calculation;

import fr.inra.sad_paysage.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.matrix.Matrix;

public class TruncateMinMatrixCalculation extends Pixel2PixelMatrixCalculation {

	private double min;
	
	public TruncateMinMatrixCalculation(double min, Matrix m){
		super(m);
		this.min = min;
	}
	
	@Override
	public void doInit() {}
	
	@Override
	protected double treatPixel(Pixel p) {
		if(matrix().get(p) > min){
			return 1;
		}
		return 0;
	}

	@Override
	public void doClose() {}

}
