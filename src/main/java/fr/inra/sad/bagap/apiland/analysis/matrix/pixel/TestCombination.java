package fr.inra.sad.bagap.apiland.analysis.matrix.pixel;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import fr.inra.sad.bagap.apiland.analysis.matrix.pixel.CombinationParser.EvaluateContext;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.ArrayMatrixFactory;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.MatrixManager;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class TestCombination {

	public static void main(String[] args){
		script25PointsMin();
	}
	
	private static void script25PointsMin(){
		String path = "C:/Hugues/projets/woodnet/data_5m/";
		
		Matrix[] matrix = new Matrix[1];
		String[] names = new String[1];
		names[0] = "m1";
		
		for(int b=1; b<=35; b++){
			matrix[0] = ArrayMatrixFactory.get().createWithAsciiGrid(path+"z1/mosaique_5m_z1_band"+b+".asc", true);
			
			String combination = "if(m1 <= 50) {0} else {m1}";
			
			Pixel2PixelCombinationMatrixCalculation1 ppcmc = new Pixel2PixelCombinationMatrixCalculation1(combination, names, matrix);
			ppcmc.allRun();
			
			MatrixManager.exportAsciiGridAndVisualize(ppcmc.getResult(), path+"test/mosaique_5m_z1_band"+b+".asc");
		}
	}
	
	private static void script25PointsMin2(){
		String path = "C:/Hugues/projets/woodnet/data_5m/";
		
		Matrix[] matrix = new Matrix[1];
		
		for(int b=1; b<=35; b++){
			matrix[0] = ArrayMatrixFactory.get().createWithAsciiGrid(path+"z1/mosaique_5m_z1_band"+b+".asc", true);
			
			Pixel2PixelMatrixCalculation ppcmc = new Pixel2PixelMatrixCalculation(matrix){
				@Override
				protected double treatPixel(Pixel p) {
					double v = matrix().get(p);
					if(v == -1){
						return -1;
					}
					if(v <= 25){
						return 0;
					}
					return v;
				}
			};
			ppcmc.allRun();
			
			MatrixManager.exportAsciiGridAndVisualize(ppcmc.getResult(), path+"test2/mosaique_5m_z1_band"+b+".asc");
		}
	}
	
	
	private static void script(){
		String combination = "m1 / m2";
		
		CombinationParser parser = new CombinationParser(new CommonTokenStream(new CombinationLexer(new ANTLRInputStream(combination))));
		EvaluateContext ec = parser.evaluate();
		
		Map<String, Double> matrix = new HashMap<String, Double>();
		matrix.put("m1", 1.5);
		matrix.put("m2", 2.5);
		
		Pixel2PixelCombinationListener1 listener = new Pixel2PixelCombinationListener1();
		listener.setMatrix(matrix);
		ParseTreeWalker walker = new ParseTreeWalker();
		walker.walk(listener, ec);
		
		System.out.println(listener.evaluate());
		
	}
	
	private static void test(){
				
		System.out.println("start direct");
		for(int i=0; i<100000000; i++){
			int h = 2 + 2;
		}
		System.out.println("end direct");
				
		Expression e = new ExpressionBuilder("2 + m2")
				.variables("m2")
				.build()
				.setVariable("m2", 2);
		System.out.println("start expression");
		for(int i=0; i<100000000; i++){
			e.setVariable("m2", 2);
			e.evaluate();
		}
		System.out.println("end expression");
		
	}

	
}
