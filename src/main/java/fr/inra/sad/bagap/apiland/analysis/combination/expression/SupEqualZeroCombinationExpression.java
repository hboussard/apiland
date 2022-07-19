package fr.inra.sad.bagap.apiland.analysis.combination.expression;

public class SupEqualZeroCombinationExpression extends SimpleBooleanCombinationExpression {
	
	public SupEqualZeroCombinationExpression(String exp, String... names){
		super(exp, names);
	}

	@Override
	public boolean evaluation() {
		if(getExpression().evaluate() >= 0){
			return true;
		}
		return false;
	}
	

}