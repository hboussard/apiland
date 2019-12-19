package fr.inra.sad.bagap.apiland.analysis.matrix.pixel.combination.expression;

public class AndCombinationExpression extends ComplexLogicalCombinationExpression {	

	public AndCombinationExpression(int size) {
		super(size);
	}
	
	@Override
	public boolean evaluation() {
		for(BooleanCombinationExpression lce : getBooleanCombinationExpressions()){
			if(!lce.evaluation()){
				return false;
			}
		}
		return true;
	}


	
}