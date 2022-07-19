package fr.inra.sad.bagap.apiland.analysis.tab;

import java.util.Map;

public class SearchAndReplacePixel2PixelTabCalculation extends Pixel2PixelTabCalculation {

	private Map<Float, Float> sarMap;
	
	public SearchAndReplacePixel2PixelTabCalculation(float[] outTab, float[] inTab, Map<Float, Float> sarMap) {
		super(outTab, inTab);
		this.sarMap = sarMap;
	}

	@Override
	protected float doTreat(float[] values) {
		float v = values[0];
		if(sarMap.containsKey(v)){
			return sarMap.get(v);
		}else{
			return v;
		}
	}

}
