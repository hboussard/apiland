package fr.inra.sad.bagap.apiland.analysis.ascii;

import fr.inra.sad.bagap.apiland.analysis.combination.expression.CombinationExpression;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;

public class Pixel2PixelCombinationExpressionAsciiGridCalculation extends Pixel2PixelAsciiGridCalculation {

	private String[] names;
	
	private CombinationExpression expression;
	
	public Pixel2PixelCombinationExpressionAsciiGridCalculation(CombinationExpression expression, String[] names, String outAscii, String[] inAscii) {
		super(outAscii, inAscii);
		this.names = names;
		this.expression = expression;
	}

	@Override
	protected double doTreat(double[] v) {
		double value;
		
		expression.init();
		
		boolean hasNodata = false;
		for(int i=0; i<names.length; i++){
			value = v[i];
			if(value == Raster.getNoDataValue()){
				hasNodata = true;
			}
			expression.setValue(names[i], value);
		}
		if(!hasNodata){
			return expression.evaluate();
		}else{
			return Raster.getNoDataValue();
		}
		
	}

}
