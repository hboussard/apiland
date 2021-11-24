package fr.inra.sad.bagap.apiland.analysis.ascii;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;

public class OverlayPixel2PixelAsciiGridCalculation extends Pixel2PixelAsciiGridCalculation {

	public OverlayPixel2PixelAsciiGridCalculation(String outAscii, String... inAscii) {
		super(outAscii, inAscii);
	}

	@Override
	protected double doTreat(double[] values) {
		boolean zero = false;
		for(double v : values){
			if(v != 0 && v != Raster.getNoDataValue()){
				return v;
			}else if(v == 0){
				zero = true;
			}
		}
		if(zero){
			return 0;
		}
		return Raster.getNoDataValue();
	}

}
