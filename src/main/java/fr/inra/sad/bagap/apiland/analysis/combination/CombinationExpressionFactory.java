package fr.inra.sad.bagap.apiland.analysis.combination;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import fr.inra.sad.bagap.apiland.analysis.ascii.Pixel2PixelCombinationExpressionAsciiGridCalculation;
import fr.inra.sad.bagap.apiland.analysis.combination.CombinationParser.EvaluateContext;
import fr.inra.sad.bagap.apiland.analysis.matrix.Pixel2PixelCombinationExpressionMatrixCalculation;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.shape.distance.CombinationExpressionDistanceFunction;
import fr.inra.sad.bagap.apiland.analysis.tab.CombinationExpressionPixel2PixelTabCalculation;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;

public class CombinationExpressionFactory {

	private static ConcreteCombinationExpressionListener parse(String combination) throws RecognitionException {
		ConcreteCombinationExpressionListener listener = null;
		
		CombinationParser parser = new CombinationParser(new CommonTokenStream(new CombinationLexer(new ANTLRInputStream(combination)))); 
		EvaluateContext context = parser.evaluate();
			
		listener = new ConcreteCombinationExpressionListener();
		ParseTreeWalker walker = new ParseTreeWalker();
		walker.walk(listener, context);
		
		return listener;
	}
	
	public static Pixel2PixelCombinationExpressionMatrixCalculation createPixel2PixelMatrixCalculation(String combination, String[] names, Matrix... matrix){
		ConcreteCombinationExpressionListener listener = parse(combination);
		
		return new Pixel2PixelCombinationExpressionMatrixCalculation(listener.getCombinationExpression(), names, matrix);
		//return listener.getPixel2PixelMatrixCalculation(names, matrix);
	}
	
	public static Pixel2PixelCombinationExpressionAsciiGridCalculation createPixel2PixelAsciiGridCalculation(String combination, String[] names, String outAscii, String... inAscii){
		ConcreteCombinationExpressionListener listener = parse(combination);
		
		return new Pixel2PixelCombinationExpressionAsciiGridCalculation(listener.getCombinationExpression(), names, outAscii, inAscii);
	}
	
	public static CombinationExpressionPixel2PixelTabCalculation createPixel2PixelTabCalculation(float[] outAscii, String combination, int noDataValue, String[] names, float[]... inAscii){
		ConcreteCombinationExpressionListener listener = parse(combination);
		
		return new CombinationExpressionPixel2PixelTabCalculation(outAscii, listener.getCombinationExpression(), noDataValue, names, inAscii);
	}
	
	public static CombinationExpressionDistanceFunction createDistanceFunction(String combination, double distanceMax){
		ConcreteCombinationExpressionListener listener = parse(combination);
		
		return new CombinationExpressionDistanceFunction(listener.getCombinationExpression(), distanceMax);
		//return listener.getDistanceFunction(distanceMax);
	}
	
}
