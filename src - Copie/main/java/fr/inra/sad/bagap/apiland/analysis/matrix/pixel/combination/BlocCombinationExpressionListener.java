package fr.inra.sad.bagap.apiland.analysis.matrix.pixel.combination;

import org.antlr.v4.runtime.misc.NotNull;
import fr.inra.sad.bagap.apiland.analysis.matrix.pixel.combination.CombinationParser.BlocContext;
import fr.inra.sad.bagap.apiland.analysis.matrix.pixel.combination.expression.SimpleCombinationExpression;

public class BlocCombinationExpressionListener extends CombinationExpressionListener {
	
	public BlocCombinationExpressionListener(){
		super();
	}
	
	@Override 
	public void enterBloc(@NotNull BlocContext ctx) {
		System.out.println("enterBloc "+ctx.getText());
	}
	
	@Override 
	public void exitBloc(@NotNull BlocContext ctx) { 
		System.out.println("exitBloc "+ctx.getText());
		expression = new SimpleCombinationExpression(ctx.getText(), localNames.toArray(new String[localNames.size()]));
	}	
	
}
