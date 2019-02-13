package fr.inra.sad.bagap.apiland.analysis.matrix.pixel.combination.expression;

import java.util.Set;
import java.util.TreeSet;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class SimpleCombinationExpression implements CombinationExpression {
	
	private Set<String> names;
	
	private Expression expression;
	
	private boolean hasnodata;
	
	public SimpleCombinationExpression(String exp, String... names){
		this.names = new TreeSet<String>();
		for(String name : names){
			this.names.add(name);
		}
		hasnodata = false;
		expression = new ExpressionBuilder(exp).variables(names).build();
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		for(String n : names){
			sb.append(n+" ");
		}
		return sb.toString();
	}
	
	protected Expression getExpression(){
		return expression;
	}

	@Override
	public void setValue(String name, double value){
		if(names.contains(name)){
			if(value == Raster.getNoDataValue()){
				hasnodata = true;
			}
			expression.setVariable(name, value);
		}
	}
	
	@Override
	public double evaluate() {
		try{
			if(!hasnodata){
				return expression.evaluate();
			}else{
				hasnodata = false;
				return Raster.getNoDataValue();
			}
			
		}catch(ArithmeticException e){
			return Raster.getNoDataValue();
		}
	}

	@Override
	public boolean addCombinationExpression(CombinationExpression ce) {
		return false;
	}

	@Override
	public void init() {
		hasnodata = false;
	}
	
}
