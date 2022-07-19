package fr.inra.sad.bagap.apiland.analysis.combination.expression;

public class ConditionCombinationExpression implements CombinationExpression {

	private BooleanCombinationExpression booleanExpression;
	
	protected CombinationExpression trueExpression;
	
	protected CombinationExpression falseExpression;
	
	public String toString(){
		return trueExpression+" "+falseExpression;
	}
	
	protected CombinationExpression getTrueExpression(){
		return trueExpression;
	}
	
	protected CombinationExpression getFalseExpression(){
		return falseExpression;
	}

	@Override
	public double evaluate() {
		if(booleanExpression.evaluation()){
			return trueExpression.evaluate();
		}
		return falseExpression.evaluate();
	}
	
	@Override
	public void setValue(String name, double value){
		booleanExpression.setValue(name, value);
		if(trueExpression != null){
			trueExpression.setValue(name, value);
		}
		if(falseExpression != null){
			falseExpression.setValue(name, value);	
		}
	}
	
	@Override
	public boolean addCombinationExpression(CombinationExpression ce) {
		if(booleanExpression == null){
			booleanExpression = (BooleanCombinationExpression) ce;
			return true;
		}else if(booleanExpression.addCombinationExpression(ce)){
			return true;
		}else if(trueExpression == null){
			this.trueExpression = ce;
			return true;
		}else if(trueExpression.addCombinationExpression(ce)){
			return true;
		}else if(falseExpression == null){
			this.falseExpression = ce;
			return true;
		}else if(falseExpression.addCombinationExpression(ce)){
			return true;
		}
		return false;
	}

	@Override
	public void init() {
		booleanExpression.init();
		if(trueExpression != null){
			trueExpression.init();
		}
		if(falseExpression != null){
			falseExpression.init();
		}
	}
	
}
