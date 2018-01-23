package fr.inra.sad.bagap.apiland.analysis.matrix.pixel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.antlr.v4.runtime.misc.NotNull;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class Pixel2PixelCombinationListener1 extends CombinationBaseListener {
	
	private Map<String, Double> matrix;
	
	private double evaluation;
	
	private List<String> variables;
	
	private double operationValue, leftOperationValue, rightOperationValue;
	
	private String boolop, logicalop;
	
	private boolean condition, active, logic;
	
	private Map<String, Expression> expressions;
	
	public Pixel2PixelCombinationListener1(){
		variables = new ArrayList<String>();
		expressions = new HashMap<String, Expression>();
	}
	
	@Override
	public void enterEvaluate(@NotNull CombinationParser.EvaluateContext ctx) { 
		active = true;
		logic = false;
	}
	
	public void setMatrix(Map<String, Double> matrix){
		this.matrix = matrix;
	}
	
	public double evaluate(){
		return evaluation;
	}
	
	@Override
	public void exitBoolterm(@NotNull CombinationParser.BooltermContext ctx) { 
		logic = false;
	}
	
	@Override
	public void enterBoolop(@NotNull CombinationParser.BoolopContext ctx) { 
		boolop = ctx.getText();
	}
	
	@Override
	public void exitLeftoperation(@NotNull CombinationParser.LeftoperationContext ctx) { 
		leftOperationValue = operationValue;
	}
	
	@Override
	public void exitRightoperation(@NotNull CombinationParser.RightoperationContext ctx) { 
		rightOperationValue = operationValue;
		
		boolean localcondition = false;
		switch (boolop){
		case "==" : 
			if(leftOperationValue == rightOperationValue){
				localcondition = true;
			}else{
				localcondition = false;
			}
			break;
		case "!=" :
			if(leftOperationValue != rightOperationValue){
				localcondition = true;
			}else{
				localcondition = false;
			}
			break;
		case ">" :
			if(leftOperationValue > rightOperationValue){
				localcondition = true;
			}else{
				localcondition = false;
			}
			break;
		case ">=" :
			if(leftOperationValue >= rightOperationValue){
				localcondition = true;
			}else{
				localcondition = false;
			}
			break;
		case "<" :
			if(leftOperationValue < rightOperationValue){
				localcondition = true;
			}else{
				localcondition = false;
			}
			break;
		case "<=" :
			if(leftOperationValue <= rightOperationValue){
				localcondition = true;
			}else{
				localcondition = false;
			}
			break;
		}
		
		if(logic){
			switch (logicalop){
			case "&" :
				condition = condition & localcondition;
				break;
			case "&&" :
				condition = condition & localcondition;
				break;
			case "AND" :
				condition = condition & localcondition;
				break;
			case "|" :
				condition = condition | localcondition;
				break;
			case "||" :
				condition = condition | localcondition;
				break;
			case "OR" :
				condition = condition | localcondition;
				break;
			}
		}else{
			condition = localcondition;
		}
	}
	
	@Override
	public void enterLogicalop(@NotNull CombinationParser.LogicalopContext ctx) { 
		logicalop = ctx.getText();
		logic = true;
	}
	
	@Override
	public void enterOperation(@NotNull CombinationParser.OperationContext ctx) { 
		variables.clear();
	}
	
	@Override
	public void exitOperation(@NotNull CombinationParser.OperationContext ctx) { 
		if(active){
			Expression e = null;
			//if(!expressions.containsKey(ctx.getText())){
				System.out.println("pass "+ctx.getText());
				expressions.put(ctx.getText(), new ExpressionBuilder(ctx.getText())
						.variables(variables.toArray(new String[variables.size()]))
						.build());
			//}
			e = expressions.get(ctx.getText());
			
			for(String v : variables){
				e.setVariable(v, matrix.get(v));
			}
			
			operationValue = e.evaluate();
			evaluation = operationValue;
		}
		
	}
	
	@Override 
	public void enterMatrix(@NotNull CombinationParser.MatrixContext ctx) { 
		variables.add(ctx.getText());
	}	
	
	@Override
	public void enterIfcombination(@NotNull CombinationParser.IfcombinationContext ctx) { 
		if(condition){
			active = true;
		}else{
			active = false;
		}
	}
	
	@Override
	public void enterElsecombination(@NotNull CombinationParser.ElsecombinationContext ctx) { 
		if(condition){
			active = false;
		}else{
			active = true;
		}
	}
	
}
