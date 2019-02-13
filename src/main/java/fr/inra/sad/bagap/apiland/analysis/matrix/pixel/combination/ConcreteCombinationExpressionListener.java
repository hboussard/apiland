package fr.inra.sad.bagap.apiland.analysis.matrix.pixel.combination;

import org.antlr.v4.runtime.misc.NotNull;
import fr.inra.sad.bagap.apiland.analysis.matrix.pixel.combination.expression.CombinationExpression;

public class ConcreteCombinationExpressionListener extends CombinationBaseListener {

	private CombinationExpressionBuilder builder;
	
	public ConcreteCombinationExpressionListener(){
		builder = new CombinationExpressionBuilder();
	}
	
	public CombinationExpression getCombinationExpression(){
		return builder.build();
	}
	
	@Override 
	public void enterBoolop(@NotNull CombinationParser.BoolopContext ctx) { builder.enterBoolop(ctx); }
	
	@Override 
	public void exitBoolop(@NotNull CombinationParser.BoolopContext ctx) { builder.exitBoolop(ctx); }
	
	@Override 
	public void enterConditional(@NotNull CombinationParser.ConditionalContext ctx) { builder.enterConditional(ctx); }
	
	@Override 
	public void exitConditional(@NotNull CombinationParser.ConditionalContext ctx) { builder.exitConditional(ctx); }
	
	@Override 
	public void enterBoolterm(@NotNull CombinationParser.BooltermContext ctx) { builder.enterBoolterm(ctx); }
	
	@Override 
	public void exitBoolterm(@NotNull CombinationParser.BooltermContext ctx) { builder.exitBoolterm(ctx); }
	
	@Override 
	public void enterRightoperation(@NotNull CombinationParser.RightoperationContext ctx) { builder.enterRightoperation(ctx); }
	
	@Override 
	public void exitRightoperation(@NotNull CombinationParser.RightoperationContext ctx) { builder.exitRightoperation(ctx); }
	
	@Override 
	public void enterBloc(@NotNull CombinationParser.BlocContext ctx) { builder.enterBloc(ctx); }
	
	@Override 
	public void exitBloc(@NotNull CombinationParser.BlocContext ctx) { builder.exitBloc(ctx); }
	
	@Override 
	public void enterCondition(@NotNull CombinationParser.ConditionContext ctx) { builder.enterCondition(ctx); }
	
	@Override 
	public void exitCondition(@NotNull CombinationParser.ConditionContext ctx) { builder.exitCondition(ctx); }
	
	@Override 
	public void enterBooltermnegation(@NotNull CombinationParser.BooltermnegationContext ctx) { builder.enterBooltermnegation(ctx); }
	
	@Override 
	public void exitBooltermnegation(@NotNull CombinationParser.BooltermnegationContext ctx) { builder.exitBooltermnegation(ctx); }
	
	public void enterBooltermwithcoma(@NotNull CombinationParser.BooltermwithcomaContext ctx) { builder.enterBooltermwithcoma(ctx); }
	
	@Override 
	public void enterName(@NotNull CombinationParser.NameContext ctx) { builder.enterName(ctx); }
	
	@Override 
	public void exitName(@NotNull CombinationParser.NameContext ctx) { builder.exitName(ctx); }
	
	@Override 
	public void enterAndgenericboolterm(@NotNull CombinationParser.AndgenericbooltermContext ctx) {  builder.enterAndgenericboolterm(ctx); }
	
	@Override 
	public void enterOrgenericboolterm(@NotNull CombinationParser.OrgenericbooltermContext ctx) {  builder.enterOrgenericboolterm(ctx); }

	@Override 
	public void enterLeftoperation(@NotNull CombinationParser.LeftoperationContext ctx) { builder.enterLeftoperation(ctx); }
	
	@Override 
	public void exitLeftoperation(@NotNull CombinationParser.LeftoperationContext ctx) { builder.exitLeftoperation(ctx); }
	
	public void enterGenericboolterm(@NotNull CombinationParser.GenericbooltermContext ctx) { builder.enterGenericboolterm(ctx); }
	
	
}
