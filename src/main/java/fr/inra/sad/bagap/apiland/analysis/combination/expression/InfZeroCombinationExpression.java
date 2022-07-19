package fr.inra.sad.bagap.apiland.analysis.combination.expression;

public class InfZeroCombinationExpression extends SimpleBooleanCombinationExpression {
	
	public InfZeroCombinationExpression(String exp, String... names){
		super(exp, names);
	}

	@Override
	public boolean evaluation() {
		if(getExpression().evaluate() < 0){
			return true;
		}
		return false;
	}
	

}