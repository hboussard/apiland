package fr.inra.sad_paysage.apiland.analysis.matrix.calculation;

import fr.inra.sad_paysage.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.matrix.Matrix;

public class SearchAndReplaceMatrixCalculation extends Pixel2PixelMatrixCalculation {

	private double newValue;
	
	private int[] old;
	
	public SearchAndReplaceMatrixCalculation(Matrix m, double newValue, int... old){
		super(m);
		this.newValue = newValue;
		this.old = old;
	}
	
	@Override
	public void doInit() {}
	
	@Override
	protected double treatPixel(Pixel p) {
		double v = matrix().get(p);
		for(double o : old){
			if(v == o){
				return newValue;
			}
		}
		return v;
	}

	@Override
	public void doClose() {
		old = null;
	}

}
