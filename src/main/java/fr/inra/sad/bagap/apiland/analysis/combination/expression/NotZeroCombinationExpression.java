package fr.inra.sad.bagap.apiland.analysis.combination.expression;

public class NotZeroCombinationExpression extends SimpleBooleanCombinationExpression {
		
	public NotZeroCombinationExpression(String exp, String... names){
		super(exp, names);
	}

	@Override
	public boolean evaluation() {
		if(getExpression().evaluate() != 0){
			return true;
		}
		return false;
	}

}