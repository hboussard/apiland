package fr.inra.sad_paysage.apiland.analysis.matrix.calculation;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import fr.inra.sad_paysage.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.Raster;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.matrix.Matrix;

public class AbsoluteDifferenceCalculation extends Pixel2PixelMatrixCalculation {

	private DecimalFormat format;
	
	public AbsoluteDifferenceCalculation(Matrix... m){
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
				return new Double(format(Math.abs(v0 - v1)));
			}
		}
		return 0.0;
	}

	@Override
	protected void doInit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void doClose() {
		// TODO Auto-generated method stub
		
	}
	
	protected String format(double v){
		int f = new Double(Math.floor(v)).intValue();
		if(v == f){
			return f+"";
		}
		return format.format(v);
	}
	
	

}
