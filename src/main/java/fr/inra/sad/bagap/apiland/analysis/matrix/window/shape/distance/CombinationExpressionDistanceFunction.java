package fr.inra.sad.bagap.apiland.analysis.matrix.window.shape.distance;

import fr.inra.sad.bagap.apiland.analysis.matrix.pixel.combination.expression.CombinationExpression;

public class CombinationExpressionDistanceFunction implements DistanceFunction {

	private CombinationExpression expression;
	
	public CombinationExpressionDistanceFunction(CombinationExpression expression, double distanceMax) {
		this.expression = expression;
		this.expression.setValue("dmax", distanceMax);
	}

	@Override
	public double interprete(double distance) {
		expression.setValue("distance", distance);
		return expression.evaluate();
	}

}
