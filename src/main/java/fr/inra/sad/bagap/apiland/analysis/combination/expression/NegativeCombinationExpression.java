package fr.inra.sad.bagap.apiland.analysis.combination.expression;

public class NegativeCombinationExpression implements BooleanCombinationExpression {

	private BooleanCombinationExpression booleanExpression;
	
	public void setBooleanCombinationExpression(BooleanCombinationExpression expression){
		this.booleanExpression = expression;
	}
	
	@Override
	public void setValue(String name, double value){
		booleanExpression.setValue(name, value);
	}

	@Override
	public boolean evaluation() {
		return ! (booleanExpression.evaluation());
	}

	@Override
	public double evaluate() {
		throw new UnsupportedOperationException();
	}

	
	@Override
	public boolean addCombinationExpression(CombinationExpression ce) {
		if(booleanExpression == null){
			booleanExpression = (BooleanCombinationExpression) ce;
			return true;
		}else if(booleanExpression.addCombinationExpression(ce)){
			return true;
		}
		return false;
	}

	@Override
	public void init() {
		booleanExpression.init();
	}
	
}
