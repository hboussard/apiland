package fr.inra.sad.bagap.apiland.analysis.combination.expression;

import java.util.ArrayList;
import java.util.List;

public abstract class ComplexLogicalCombinationExpression implements BooleanCombinationExpression {
	
	private int size;
	
	private List<BooleanCombinationExpression> booleanExpressions;
	
	public ComplexLogicalCombinationExpression(int size) {
		this.size = size;
		booleanExpressions = new ArrayList<BooleanCombinationExpression>();
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		for(BooleanCombinationExpression bce : booleanExpressions){
			sb.append(bce.toString()+" ");
		}
		return sb.toString();
	}
	
	protected List<BooleanCombinationExpression> getBooleanCombinationExpressions(){
		return booleanExpressions;
	}
	
	@Override
	public void setValue(String name, double value){
		for(BooleanCombinationExpression lce : booleanExpressions){
			lce.setValue(name, value);
		}
	}
	
	@Override
	public double evaluate() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addCombinationExpression(CombinationExpression ce) {
		for(int i=0; i<size; i++){
			//System.out.println(i+" "+size+" "+booleanExpressions.size());
			//if(booleanExpressions.get(i) == null){
			if(i == booleanExpressions.size()){
				booleanExpressions.add((BooleanCombinationExpression) ce);
				return true;
			}else if(booleanExpressions.get(i).addCombinationExpression(ce)){
				return true;
			}
		}
		return false;
	}
	

	@Override
	public void init() {
		for(BooleanCombinationExpression lce : booleanExpressions){
			lce.init();
		}
	}
	
}
