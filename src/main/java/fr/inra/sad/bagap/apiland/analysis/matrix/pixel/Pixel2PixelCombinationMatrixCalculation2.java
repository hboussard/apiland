package fr.inra.sad.bagap.apiland.analysis.matrix.pixel;

import java.util.HashMap;
import java.util.Map;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import fr.inra.sad.bagap.apiland.analysis.matrix.pixel.CombinationParser.EvaluateContext;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;
import net.objecthunter.exp4j.Expression;

public class Pixel2PixelCombinationMatrixCalculation2 extends Pixel2PixelMatrixCalculation {
	
	private String[] matNames;
	
	private Map<String, Double> values;
	
	private Expression expression;
	
	public Pixel2PixelCombinationMatrixCalculation2(String combination, String[] matNames, Matrix... m){
		super(m);
		this.matNames = matNames;
		values = new HashMap<String, Double>();
		CombinationParser parser = new CombinationParser(new CommonTokenStream(new CombinationLexer(new ANTLRInputStream(combination)))); 
		EvaluateContext context = parser.evaluate();
		Pixel2PixelCombinationListener2 listener = new Pixel2PixelCombinationListener2();
		new ParseTreeWalker().walk(listener, context);
		expression = listener.buildExpression();
	}
	
	@Override
	protected double treatPixel(Pixel p) {
		for(int i=0; i<matNames.length; i++){
			values.put(matNames[i], matrix(i).get(p));
		}
		expression.setVariables(values);
		return expression.evaluate();
	}

}
