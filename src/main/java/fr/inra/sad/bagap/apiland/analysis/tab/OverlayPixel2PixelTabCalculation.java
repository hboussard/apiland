package fr.inra.sad.bagap.apiland.analysis.tab;

public class OverlayPixel2PixelTabCalculation extends Pixel2PixelTabCalculation {
	
	private int noDataValue;
	
	public OverlayPixel2PixelTabCalculation(float[] outTab, int noDataValue, float[]... inTab) {
		super(outTab, inTab);
		this.noDataValue = noDataValue;
	}

	@Override
	protected float doTreat(float[] values) {
		
		boolean zero = false;
		for(float v : values){
			if(v != 0 && v != noDataValue){
				return v;
			}else if(v == 0){
				zero = true;
			}
		}
		if(zero){
			return 0;
		}
		return noDataValue;
	}

}
