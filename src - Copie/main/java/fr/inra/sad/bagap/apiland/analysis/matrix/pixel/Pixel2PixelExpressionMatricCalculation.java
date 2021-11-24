package fr.inra.sad.bagap.apiland.analysis.matrix.pixel;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class Pixel2PixelExpressionMatricCalculation extends Pixel2PixelMatrixCalculation {

	private Expression expression;
	
	public Pixel2PixelExpressionMatricCalculation(String exp, Matrix... m){
		super(m);
		expression = new ExpressionBuilder(exp)
				.variables("m1")
				.build();
	}
	
	@Override
	protected double treatPixel(Pixel p) {
		expression.setVariable("m1", matrix(0).get(p));
		return expression.evaluate();
	}
	
	

}
