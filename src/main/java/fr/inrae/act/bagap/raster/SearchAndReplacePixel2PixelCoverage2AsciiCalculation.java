package fr.inrae.act.bagap.raster;

import java.util.Map;

public class SearchAndReplacePixel2PixelCoverage2AsciiCalculation extends Pixel2PixelCoverage2AsciiCalculation {

	private Map<Float, Float> sarMap;
	
	public SearchAndReplacePixel2PixelCoverage2AsciiCalculation(String asciiOut, EnteteRaster entete, Map<Float, Float> sarMap, Coverage... coverages) {
		super(asciiOut, entete, coverages);
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
