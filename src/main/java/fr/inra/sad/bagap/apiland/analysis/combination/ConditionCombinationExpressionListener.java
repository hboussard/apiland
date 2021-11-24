package fr.inra.sad.bagap.apiland.analysis.combination;

import org.antlr.v4.runtime.misc.NotNull;

import fr.inra.sad.bagap.apiland.analysis.combination.CombinationParser.BooltermContext;
import fr.inra.sad.bagap.apiland.analysis.combination.CombinationParser.ConditionContext;
import fr.inra.sad.bagap.apiland.analysis.combination.expression.EqualZeroCombinationExpression;
import fr.inra.sad.bagap.apiland.analysis.combination.expression.InfEqualZeroCombinationExpression;
import fr.inra.sad.bagap.apiland.analysis.combination.expression.InfZeroCombinationExpression;
import fr.inra.sad.bagap.apiland.analysis.combination.expression.NotZeroCombinationExpression;
import fr.inra.sad.bagap.apiland.analysis.combination.expression.SupEqualZeroCombinationExpression;
import fr.inra.sad.bagap.apiland.analysis.combination.expression.SupZeroCombinationExpression;

public class ConditionCombinationExpressionListener extends CombinationExpressionListener {
	
	private String boolExpression, boolOp;
	
	public ConditionCombinationExpressionListener(){
		super();
	}

	@Override 
	public void enterCondition(@NotNull ConditionContext ctx) {
		System.out.println("enterCondition "+ctx.getText());	
	}
	
	@Override 
	public void enterBoolterm(@NotNull BooltermContext ctx) {
		System.out.println("enterBoolTerm "+ctx.getText());	
	}
	
	@Override 
	public void exitBoolterm(@NotNull CombinationParser.BooltermContext ctx) {
		System.out.println("exitBoolterm "+ctx.getText());
		switch(boolOp){
		case ">" : expression = new SupZeroCombinationExpression(boolExpression, localNames.toArray(new String[localNames.size()])); break;
		case "<" : expression = new InfZeroCombinationExpression(boolExpression, localNames.toArray(new String[localNames.size()])); break;
		case ">=" : expression = new SupEqualZeroCombinationExpression(boolExpression, localNames.toArray(new String[localNames.size()])); break;
		case "<=" : expression = new InfEqualZeroCombinationExpression(boolExpression, localNames.toArray(new String[localNames.size()])); break;
		case "==" : expression = new EqualZeroCombinationExpression(boolExpression, localNames.toArray(new String[localNames.size()])); break;
		case "!=" : expression = new NotZeroCombinationExpression(boolExpression, localNames.toArray(new String[localNames.size()])); break;
		default : throw new IllegalArgumentException (boolOp);
		}
	}
	
	@Override 
	public void enterBoolop(@NotNull CombinationParser.BoolopContext ctx) {
		System.out.println("enterBoolop "+ctx.getText());
		boolOp = ctx.getText();	
	}
	
	@Override 
	public void exitLeftoperation(@NotNull CombinationParser.LeftoperationContext ctx) { 
		System.out.println("exitLeftoperation "+ctx.getText());
		boolExpression = ctx.getText();
	}
	
	@Override 
	public void exitRightoperation(@NotNull CombinationParser.RightoperationContext ctx) {
		System.out.println("exitRightoperation "+ctx.getText());
		boolExpression = boolExpression+"-"+ctx.getText();
	}

}
