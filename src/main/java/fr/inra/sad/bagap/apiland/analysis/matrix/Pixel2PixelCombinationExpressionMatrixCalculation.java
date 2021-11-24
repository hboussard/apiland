package fr.inra.sad.bagap.apiland.analysis.matrix;

import fr.inra.sad.bagap.apiland.analysis.combination.expression.CombinationExpression;
import fr.inra.sad.bagap.apiland.analysis.matrix.pixel.Pixel2PixelMatrixCalculation;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;

public class Pixel2PixelCombinationExpressionMatrixCalculation extends Pixel2PixelMatrixCalculation {
	
	private String[] names;
	
	private CombinationExpression expression;
	
	public Pixel2PixelCombinationExpressionMatrixCalculation(CombinationExpression expression, String[] names, Matrix... m){
		super(m);
		this.names = names;
		this.expression = expression;
	}

	@Override
	protected double treatPixel(Pixel p) {
		double v;
		
		expression.init();
		
		for(int i=0; i<names.length; i++){
			v = matrix(i).get(p);
			
			/*if(v == Raster.getNoDataValue()){
				return Raster.getNoDataValue();
			}*/
			expression.setValue(names[i], v);
		}
		
		return expression.evaluate();
		
	}

}
