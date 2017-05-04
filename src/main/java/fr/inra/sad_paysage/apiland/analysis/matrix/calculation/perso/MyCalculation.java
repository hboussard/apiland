package fr.inra.sad_paysage.apiland.analysis.matrix.calculation.perso;

import fr.inra.sad_paysage.apiland.analysis.matrix.calculation.Pixel2PixelMatrixCalculation;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.Raster;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.matrix.Matrix;

public class MyCalculation extends Pixel2PixelMatrixCalculation {

	public MyCalculation(Matrix m){
		super(m);
	}
	
	@Override
	protected double treatPixel(Pixel p) {
		double v = matrix().get(p);
		if(v == Raster.getNoDataValue() || v == 0){
			return Raster.getNoDataValue();
		}
		return 2 - v;
	}

	@Override
	protected void doInit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void doClose() {
		// TODO Auto-generated method stub
		
	}
	
}
