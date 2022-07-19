package fr.inra.sad.bagap.apiland.analysis.matrix.pixel;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;

public class SearchAndReplaceMatrixCalculation extends Pixel2PixelMatrixCalculation {

	private Double[] oldValues;
	
	private Double[] newValues;
	
	public SearchAndReplaceMatrixCalculation(Matrix m, Double[] oldValues, Double[] newValues){
		super(m);
		this.oldValues = oldValues;
		this.newValues = newValues;
	}
	
	@Override
	protected double treatPixel(Pixel p) {
		double v = matrix().get(p);
		for(int io=0; io<oldValues.length; io++){
			if(oldValues[io] == v){
				return newValues[io];
			}
		}
		return v;
	}

	@Override
	public void doClose() {
		oldValues = null;
		newValues = null;
	}

}
