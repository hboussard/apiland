package fr.inra.sad.bagap.apiland.analysis.matrix.pixel.combination.expression;

public class SupZeroCombinationExpression extends SimpleBooleanCombinationExpression {
	
	public SupZeroCombinationExpression(String exp, String... names){
		super(exp, names);
	}

	@Override
	public boolean evaluation() {
		if(getExpression().evaluate() > 0){
			return true;
		}
		return false;
	}

}
