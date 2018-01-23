package fr.inra.sad.bagap.apiland.analysis.matrix.pixel;

import java.util.HashMap;
import java.util.Map;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import fr.inra.sad.bagap.apiland.analysis.matrix.pixel.CombinationParser.EvaluateContext;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;

public class Pixel2PixelCombinationMatrixCalculation1 extends Pixel2PixelMatrixCalculation {

	private String[] matNames;
	
	private ParseTreeWalker walker;
	
	private EvaluateContext context;
	
	private Pixel2PixelCombinationListener1 listener;
	
	private Map<String, Double> values;
	
	public Pixel2PixelCombinationMatrixCalculation1(String combination, String[] matNames, Matrix... m){
		super(m);
		this.matNames = matNames;
		values = new HashMap<String, Double>();
		CombinationParser parser = new CombinationParser(new CommonTokenStream(new CombinationLexer(new ANTLRInputStream(combination)))); 
		context = parser.evaluate();
		listener = new Pixel2PixelCombinationListener1();
		walker = new ParseTreeWalker();
	}

	@Override
	protected double treatPixel(Pixel p) {
		for(int i=0; i<matNames.length; i++){
			values.put(matNames[i], matrix(i).get(p));
		}
		listener.setMatrix(values);
		walker.walk(listener, context);
		
		return listener.evaluate();
	}
	
	@Override
	protected void doClose() {
		matNames = null;
		walker = null;
		context = null;
		listener = null;
		values.clear();
		values = null;
	}

}
