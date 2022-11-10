package fr.inra.sad.bagap.apiland.analysis.ascii;

import java.util.Map;
import java.util.Map.Entry;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.domain.Domain;

public class ClassificationPixel2PixelAsciiGridCalculation extends Pixel2PixelAsciiGridCalculation {

	private Map<Domain<Double, Double>, Integer> domains;
	
	public ClassificationPixel2PixelAsciiGridCalculation(String outAscii, String inAscii, Map<Domain<Double, Double>, Integer> domains) {
		super(outAscii, inAscii);
		this.domains = domains;
	}

	@Override
	protected double doTreat(double[] values) {
		double v = values[0];
		if(v != Raster.getNoDataValue()){
			for(Entry<Domain<Double, Double>, Integer> e : domains.entrySet()) {
				if(e.getKey().accept(v)){
					return e.getValue();
				}
			}
		}
		return Raster.getNoDataValue();
	}

}
