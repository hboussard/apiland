package fr.inra.sad_paysage.apiland.analysis.matrix.calculation.perso;

import fr.inra.sad_paysage.apiland.analysis.matrix.calculation.Pixel2PixelMatrixCalculation;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.Raster;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.matrix.Matrix;

public class MyTruncateCalculation extends Pixel2PixelMatrixCalculation{

	private int max;
	
	public MyTruncateCalculation(Matrix m, int max){
		super(m);
		this.max = max;
	}
	
	@Override
	protected void doInit() {
		// do nothing
	}
	
	@Override
	protected double treatPixel(Pixel p) {
		double v = matrix(0).get(p);
		if(v > max){
			return Raster.getNoDataValue();
		}
		return v;
	}

	@Override
	protected void doClose() {
		// do nothing
	}

}
