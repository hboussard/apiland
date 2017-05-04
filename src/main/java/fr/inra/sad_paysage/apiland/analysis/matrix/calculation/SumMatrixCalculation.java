package fr.inra.sad_paysage.apiland.analysis.matrix.calculation;

import fr.inra.sad_paysage.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.matrix.Matrix;

public class SumMatrixCalculation extends Pixel2PixelMatrixCalculation {
	
	public SumMatrixCalculation(Matrix... m){
		super(m);
	}
	
	@Override
	public void doInit() {	}
	
	@Override
	protected double treatPixel(Pixel p) {
		double sum = 0;
		for(Matrix m : wholeMatrix()){
			sum += m.get(p);
		}
		return sum;
	}

	@Override
	public void doClose() {	}


}

