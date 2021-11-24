package fr.inra.sad.bagap.apiland.analysis.ascii;

import java.util.Map;

public class SearchAndReplacePixel2PixelAsciiGridCalculation extends Pixel2PixelAsciiGridCalculation {

	private Map<Double, Double> sarMap;
	
	public SearchAndReplacePixel2PixelAsciiGridCalculation(String outAscii, Map<Double, Double> sarMap, String inAscii) {
		super(outAscii, inAscii);
		this.sarMap = sarMap;
	}

	@Override
	protected double doTreat(double[] values) {
		double v = values[0];
		if(sarMap.containsKey(v)){
			return sarMap.get(v);
		}else{
			return v;
		}
	}

}
