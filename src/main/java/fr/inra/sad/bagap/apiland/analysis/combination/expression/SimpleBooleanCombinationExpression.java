package fr.inra.sad.bagap.apiland.analysis.combination.expression;

public abstract class SimpleBooleanCombinationExpression extends SimpleCombinationExpression implements BooleanCombinationExpression {

	public SimpleBooleanCombinationExpression(String exp, String[] names) {
		super(exp, names);
	}
	
	@Override
	public double evaluate() {
		throw new UnsupportedOperationException();
	}

}
