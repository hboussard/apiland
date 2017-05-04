package fr.inra.sad_paysage.apiland.analysis.matrix.calculation;

import fr.inra.sad_paysage.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.matrix.Matrix;

public class IntersectionMatrixCalculation extends Pixel2PixelMatrixCalculation {
	
	public IntersectionMatrixCalculation(Matrix... m){
		super(m);
	}
	
	@Override
	public void doInit() {	}
	
	@Override
	protected double treatPixel(Pixel p) {
		for(Matrix m : wholeMatrix()){
			if(m.get(p) == 0){
				return 0;
			}
		}
		return 1;
	}

	@Override
	public void doClose() {	}


}