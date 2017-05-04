package fr.inra.sad_paysage.apiland.analysis.matrix.calculation;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import fr.inra.sad_paysage.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.Raster;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.matrix.Matrix;

public class DifferenceCalculation extends Pixel2PixelMatrixCalculation {

	private DecimalFormat format;
	
	public DifferenceCalculation(Matrix... m){
		super(m);
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		format = new DecimalFormat("0.00000", symbols);
	}
	
	@Override
	protected double treatPixel(Pixel p) {
		double v0 = matrix(0).get(p);
		if(v0 != Raster.getNoDataValue()){
			double v1 = matrix(1).get(p);
			if(v1 != Raster.getNoDataValue()){
				return new Double(format(v1 - v0));
			}
		}
		return 0.0;
	}

	@Override
	protected void doInit() {}

	@Override
	protected void doClose() {}
	
	protected String format(double v){
		int f = new Double(Math.floor(v)).intValue();
		if(v == f){
			return f+"";
		}
		return format.format(v);
	}
	
	

}
