package fr.inra.sad.bagap.apiland.analysis.tab;

import java.util.Map;
import java.util.Map.Entry;

import fr.inra.sad.bagap.apiland.domain.Domain;

public class ClassificationPixel2PixelTabCalculation extends Pixel2PixelTabCalculation {

	private int noDataValue;
	
	private Map<Domain<Float, Float>, Integer> domains;
	
	public ClassificationPixel2PixelTabCalculation(float[] outTab, float[] inTab, int noDataValue, Map<Domain<Float, Float>, Integer> domains) {
		super(outTab, inTab);
		this.noDataValue = noDataValue;
		this.domains = domains;
	}

	@Override
	protected float doTreat(float[] values) {
		float v = values[0];
		if(v != noDataValue){
			for(Entry<Domain<Float, Float>, Integer> e : domains.entrySet()) {
				if(e.getKey().accept(v)){
					return e.getValue();
				}
			}
		}
		return noDataValue;
	}

}
