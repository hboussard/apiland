package fr.inra.sad.bagap.apiland.analysis.combination;

import org.antlr.v4.runtime.misc.NotNull;

import fr.inra.sad.bagap.apiland.analysis.combination.CombinationParser.BlocContext;
import fr.inra.sad.bagap.apiland.analysis.combination.CombinationParser.BooltermContext;
import fr.inra.sad.bagap.apiland.analysis.combination.CombinationParser.ConditionContext;
import fr.inra.sad.bagap.apiland.analysis.combination.CombinationParser.NameContext;
import fr.inra.sad.bagap.apiland.analysis.combination.expression.CombinationExpression;

public class ConcreteCombinationListenerOld extends CombinationBaseListener {

	private CombinationExpressionListener listener;
	
	private CombinationExpression expression;
	
	public ConcreteCombinationListenerOld(){}
	
	private void buildExpression(CombinationExpression expression){
		if(this.expression == null){
			this.expression = expression;
		}else{
			//...
		}
	}
	
	public CombinationExpression getCombinationExpression(){
		return expression;
	}
	
	@Override 
	public void enterBloc(@NotNull BlocContext ctx) {
		listener = new BlocCombinationExpressionListener();
		listener.enterBloc(ctx);
	}
	
	@Override 
	public void exitBloc(@NotNull BlocContext ctx) { 
		listener.exitBloc(ctx);
		buildExpression(listener.expression);
		listener = null;
	}
	
	@Override 
	public void enterName(@NotNull NameContext ctx) {
		listener.enterName(ctx);
	}
	
	@Override 
	public void enterCondition(@NotNull ConditionContext ctx) {
		listener = new ConditionCombinationExpressionListener();
		listener.enterCondition(ctx);
	}
	
	@Override 
	public void exitCondition(@NotNull ConditionContext ctx) {
		listener.exitCondition(ctx);
		buildExpression(listener.expression);
		listener = null;
	}
	
	@Override 
	public void enterBoolterm(@NotNull BooltermContext ctx) {
		listener.enterBoolterm(ctx);		
	}
	
	@Override 
	public void exitBoolterm(@NotNull CombinationParser.BooltermContext ctx) {
		listener.exitBoolterm(ctx);
	}
	
	@Override 
	public void enterBoolop(@NotNull CombinationParser.BoolopContext ctx) {
		listener.enterBoolop(ctx);
	}
	
	@Override 
	public void exitLeftoperation(@NotNull CombinationParser.LeftoperationContext ctx) { 
		listener.exitLeftoperation(ctx);
	}
	
	@Override 
	public void exitRightoperation(@NotNull CombinationParser.RightoperationContext ctx) {
		listener.exitRightoperation(ctx);
	}
	
}
