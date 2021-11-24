// Generated from Combination.g4 by ANTLR 4.4
package fr.inra.sad.bagap.apiland.analysis.combination;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link CombinationParser}.
 */
public interface CombinationListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link CombinationParser#boolop}.
	 * @param ctx the parse tree
	 */
	void enterBoolop(@NotNull CombinationParser.BoolopContext ctx);
	/**
	 * Exit a parse tree produced by {@link CombinationParser#boolop}.
	 * @param ctx the parse tree
	 */
	void exitBoolop(@NotNull CombinationParser.BoolopContext ctx);
	/**
	 * Enter a parse tree produced by {@link CombinationParser#conditional}.
	 * @param ctx the parse tree
	 */
	void enterConditional(@NotNull CombinationParser.ConditionalContext ctx);
	/**
	 * Exit a parse tree produced by {@link CombinationParser#conditional}.
	 * @param ctx the parse tree
	 */
	void exitConditional(@NotNull CombinationParser.ConditionalContext ctx);
	/**
	 * Enter a parse tree produced by {@link CombinationParser#orgenericboolterm}.
	 * @param ctx the parse tree
	 */
	void enterOrgenericboolterm(@NotNull CombinationParser.OrgenericbooltermContext ctx);
	/**
	 * Exit a parse tree produced by {@link CombinationParser#orgenericboolterm}.
	 * @param ctx the parse tree
	 */
	void exitOrgenericboolterm(@NotNull CombinationParser.OrgenericbooltermContext ctx);
	/**
	 * Enter a parse tree produced by {@link CombinationParser#mathop}.
	 * @param ctx the parse tree
	 */
	void enterMathop(@NotNull CombinationParser.MathopContext ctx);
	/**
	 * Exit a parse tree produced by {@link CombinationParser#mathop}.
	 * @param ctx the parse tree
	 */
	void exitMathop(@NotNull CombinationParser.MathopContext ctx);
	/**
	 * Enter a parse tree produced by {@link CombinationParser#andgenericboolterm}.
	 * @param ctx the parse tree
	 */
	void enterAndgenericboolterm(@NotNull CombinationParser.AndgenericbooltermContext ctx);
	/**
	 * Exit a parse tree produced by {@link CombinationParser#andgenericboolterm}.
	 * @param ctx the parse tree
	 */
	void exitAndgenericboolterm(@NotNull CombinationParser.AndgenericbooltermContext ctx);
	/**
	 * Enter a parse tree produced by {@link CombinationParser#booltermwithcoma}.
	 * @param ctx the parse tree
	 */
	void enterBooltermwithcoma(@NotNull CombinationParser.BooltermwithcomaContext ctx);
	/**
	 * Exit a parse tree produced by {@link CombinationParser#booltermwithcoma}.
	 * @param ctx the parse tree
	 */
	void exitBooltermwithcoma(@NotNull CombinationParser.BooltermwithcomaContext ctx);
	/**
	 * Enter a parse tree produced by {@link CombinationParser#boolterm}.
	 * @param ctx the parse tree
	 */
	void enterBoolterm(@NotNull CombinationParser.BooltermContext ctx);
	/**
	 * Exit a parse tree produced by {@link CombinationParser#boolterm}.
	 * @param ctx the parse tree
	 */
	void exitBoolterm(@NotNull CombinationParser.BooltermContext ctx);
	/**
	 * Enter a parse tree produced by {@link CombinationParser#rightoperation}.
	 * @param ctx the parse tree
	 */
	void enterRightoperation(@NotNull CombinationParser.RightoperationContext ctx);
	/**
	 * Exit a parse tree produced by {@link CombinationParser#rightoperation}.
	 * @param ctx the parse tree
	 */
	void exitRightoperation(@NotNull CombinationParser.RightoperationContext ctx);
	/**
	 * Enter a parse tree produced by {@link CombinationParser#bloc}.
	 * @param ctx the parse tree
	 */
	void enterBloc(@NotNull CombinationParser.BlocContext ctx);
	/**
	 * Exit a parse tree produced by {@link CombinationParser#bloc}.
	 * @param ctx the parse tree
	 */
	void exitBloc(@NotNull CombinationParser.BlocContext ctx);
	/**
	 * Enter a parse tree produced by {@link CombinationParser#function1param}.
	 * @param ctx the parse tree
	 */
	void enterFunction1param(@NotNull CombinationParser.Function1paramContext ctx);
	/**
	 * Exit a parse tree produced by {@link CombinationParser#function1param}.
	 * @param ctx the parse tree
	 */
	void exitFunction1param(@NotNull CombinationParser.Function1paramContext ctx);
	/**
	 * Enter a parse tree produced by {@link CombinationParser#condition}.
	 * @param ctx the parse tree
	 */
	void enterCondition(@NotNull CombinationParser.ConditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link CombinationParser#condition}.
	 * @param ctx the parse tree
	 */
	void exitCondition(@NotNull CombinationParser.ConditionContext ctx);
	/**
	 * Enter a parse tree produced by {@link CombinationParser#combinationwithcoma}.
	 * @param ctx the parse tree
	 */
	void enterCombinationwithcoma(@NotNull CombinationParser.CombinationwithcomaContext ctx);
	/**
	 * Exit a parse tree produced by {@link CombinationParser#combinationwithcoma}.
	 * @param ctx the parse tree
	 */
	void exitCombinationwithcoma(@NotNull CombinationParser.CombinationwithcomaContext ctx);
	/**
	 * Enter a parse tree produced by {@link CombinationParser#termminus}.
	 * @param ctx the parse tree
	 */
	void enterTermminus(@NotNull CombinationParser.TermminusContext ctx);
	/**
	 * Exit a parse tree produced by {@link CombinationParser#termminus}.
	 * @param ctx the parse tree
	 */
	void exitTermminus(@NotNull CombinationParser.TermminusContext ctx);
	/**
	 * Enter a parse tree produced by {@link CombinationParser#termwithcoma}.
	 * @param ctx the parse tree
	 */
	void enterTermwithcoma(@NotNull CombinationParser.TermwithcomaContext ctx);
	/**
	 * Exit a parse tree produced by {@link CombinationParser#termwithcoma}.
	 * @param ctx the parse tree
	 */
	void exitTermwithcoma(@NotNull CombinationParser.TermwithcomaContext ctx);
	/**
	 * Enter a parse tree produced by {@link CombinationParser#function}.
	 * @param ctx the parse tree
	 */
	void enterFunction(@NotNull CombinationParser.FunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link CombinationParser#function}.
	 * @param ctx the parse tree
	 */
	void exitFunction(@NotNull CombinationParser.FunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link CombinationParser#booltermnegation}.
	 * @param ctx the parse tree
	 */
	void enterBooltermnegation(@NotNull CombinationParser.BooltermnegationContext ctx);
	/**
	 * Exit a parse tree produced by {@link CombinationParser#booltermnegation}.
	 * @param ctx the parse tree
	 */
	void exitBooltermnegation(@NotNull CombinationParser.BooltermnegationContext ctx);
	/**
	 * Enter a parse tree produced by {@link CombinationParser#name}.
	 * @param ctx the parse tree
	 */
	void enterName(@NotNull CombinationParser.NameContext ctx);
	/**
	 * Exit a parse tree produced by {@link CombinationParser#name}.
	 * @param ctx the parse tree
	 */
	void exitName(@NotNull CombinationParser.NameContext ctx);
	/**
	 * Enter a parse tree produced by {@link CombinationParser#function2params}.
	 * @param ctx the parse tree
	 */
	void enterFunction2params(@NotNull CombinationParser.Function2paramsContext ctx);
	/**
	 * Exit a parse tree produced by {@link CombinationParser#function2params}.
	 * @param ctx the parse tree
	 */
	void exitFunction2params(@NotNull CombinationParser.Function2paramsContext ctx);
	/**
	 * Enter a parse tree produced by {@link CombinationParser#term}.
	 * @param ctx the parse tree
	 */
	void enterTerm(@NotNull CombinationParser.TermContext ctx);
	/**
	 * Exit a parse tree produced by {@link CombinationParser#term}.
	 * @param ctx the parse tree
	 */
	void exitTerm(@NotNull CombinationParser.TermContext ctx);
	/**
	 * Enter a parse tree produced by {@link CombinationParser#evaluate}.
	 * @param ctx the parse tree
	 */
	void enterEvaluate(@NotNull CombinationParser.EvaluateContext ctx);
	/**
	 * Exit a parse tree produced by {@link CombinationParser#evaluate}.
	 * @param ctx the parse tree
	 */
	void exitEvaluate(@NotNull CombinationParser.EvaluateContext ctx);
	/**
	 * Enter a parse tree produced by {@link CombinationParser#operation}.
	 * @param ctx the parse tree
	 */
	void enterOperation(@NotNull CombinationParser.OperationContext ctx);
	/**
	 * Exit a parse tree produced by {@link CombinationParser#operation}.
	 * @param ctx the parse tree
	 */
	void exitOperation(@NotNull CombinationParser.OperationContext ctx);
	/**
	 * Enter a parse tree produced by {@link CombinationParser#leftoperation}.
	 * @param ctx the parse tree
	 */
	void enterLeftoperation(@NotNull CombinationParser.LeftoperationContext ctx);
	/**
	 * Exit a parse tree produced by {@link CombinationParser#leftoperation}.
	 * @param ctx the parse tree
	 */
	void exitLeftoperation(@NotNull CombinationParser.LeftoperationContext ctx);
	/**
	 * Enter a parse tree produced by {@link CombinationParser#combination}.
	 * @param ctx the parse tree
	 */
	void enterCombination(@NotNull CombinationParser.CombinationContext ctx);
	/**
	 * Exit a parse tree produced by {@link CombinationParser#combination}.
	 * @param ctx the parse tree
	 */
	void exitCombination(@NotNull CombinationParser.CombinationContext ctx);
	/**
	 * Enter a parse tree produced by {@link CombinationParser#genericboolterm}.
	 * @param ctx the parse tree
	 */
	void enterGenericboolterm(@NotNull CombinationParser.GenericbooltermContext ctx);
	/**
	 * Exit a parse tree produced by {@link CombinationParser#genericboolterm}.
	 * @param ctx the parse tree
	 */
	void exitGenericboolterm(@NotNull CombinationParser.GenericbooltermContext ctx);
}