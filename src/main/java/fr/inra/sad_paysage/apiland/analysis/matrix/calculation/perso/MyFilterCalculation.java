package fr.inra.sad_paysage.apiland.analysis.matrix.calculation.perso;

import fr.inra.sad_paysage.apiland.analysis.matrix.calculation.Pixel2PixelMatrixCalculation;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.Raster;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.matrix.Matrix;

public class MyFilterCalculation extends Pixel2PixelMatrixCalculation {
	
	private int min;
	
	public MyFilterCalculation(int min, Matrix... m){
		super(m);
		this.min= min;
	}
	
	@Override
	protected void doInit() {
		// do nothing
	}
	
	@Override
	protected double treatPixel(Pixel p) {
		if(matrix(1).get(p) > min){
			return matrix(0).get(p);
		}
		return Raster.getNoDataValue();
	}


	@Override
	protected void doClose() {
		// do nothing
	}

}
