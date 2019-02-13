package fr.inra.sad.bagap.apiland.analysis.matrix.pixel.combination;

import java.util.HashSet;
import java.util.Set;
import org.antlr.v4.runtime.misc.NotNull;
import fr.inra.sad.bagap.apiland.analysis.matrix.pixel.combination.CombinationParser.NameContext;
import fr.inra.sad.bagap.apiland.analysis.matrix.pixel.combination.expression.CombinationExpression;

public abstract class CombinationExpressionListener extends CombinationBaseListener {

	protected Set<String> localNames;
	
	protected CombinationExpression expression;
	
	public CombinationExpressionListener(){
		localNames = new HashSet<String>();
	}
	
	@Override 
	public void enterName(@NotNull NameContext ctx) {
		System.out.println("enterName "+ctx.getText());
		localNames.add(ctx.getText());
	}
	
	/*
	private CombinationExpressionListener parent;
	
	private Set<String> localNames;
	
	private CombinationExpression expression;
	
	private Boolean isConditionalTerm;
	
	private String boolExpression, boolOp;
	
	private List<CombinationExpressionListener> combinationListeners;
	
	public CombinationExpressionListener(CombinationExpressionListener parent){
		this.parent = parent;
		localNames = new HashSet<String>();
		isConditionalTerm = false;
	}
	
	public CombinationExpression getCombinationExpression(){
		return expression;
	}
	
	@Override 
	public void enterConditional(@NotNull ConditionalContext ctx) { 
		System.out.println("enterConditional "+ctx.getText());
		isConditionalTerm = true;
	}
	
	@Override 
	public void exitConditional(@NotNull ConditionalContext ctx) { 
		System.out.println("exitConditional "+ctx.getText());
		isConditionalTerm = false;
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
		localNames.clear();
	}
	
	@Override 
	public void enterBoolop(@NotNull CombinationParser.BoolopContext ctx) {
		System.out.println("enterBoolop "+ctx.getText());
		boolOp = ctx.getText();	
	}
	
	@Override 
	public void enterLogicalop(@NotNull CombinationParser.LogicalopContext ctx) { 
		System.out.println("enterLogicalop "+ctx.getText());
	}
	
	@Override 
	public void enterBloc(@NotNull CombinationParser.BlocContext ctx) {
		System.out.println("enterBloc "+ctx.getText());
		expression = new SimpleCombinationExpression(ctx.getText(), localNames.toArray(new String[localNames.size()]));
		localNames.clear();
	}
	
	@Override 
	public void enterName(@NotNull CombinationParser.NameContext ctx) {
		System.out.println("enterName "+ctx.getText());
		localNames.add(ctx.getText());
	}
	
	@Override 
	public void enterLeftoperation(@NotNull CombinationParser.LeftoperationContext ctx) { 
		System.out.println("enterLeftoperation "+ctx.getText());
		boolExpression = ctx.getText();
	}
	
	@Override 
	public void enterRightoperation(@NotNull CombinationParser.RightoperationContext ctx) {
		System.out.println("enterRightoperation "+ctx.getText());
		boolExpression = boolExpression+"-"+ctx.getText();
	}
	*/

}
