package fr.inra.sad_paysage.apiland.analysis.matrix.calculation;

import fr.inra.sad_paysage.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.matrix.Matrix;

public class FuzionMatrixCalculation extends Pixel2PixelMatrixCalculation {

	public FuzionMatrixCalculation(Matrix... m){
		super(m);
	}
	
	@Override
	protected double treatPixel(Pixel p) {
		double v;
		for(Matrix  m : wholeMatrix()){
			v = m.get(p);
			if(v != -1){
				return v;
			}
		}
		return -1;
	}

	@Override
	protected void doInit() {}

	@Override
	protected void doClose() {}

}
