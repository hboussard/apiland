package fr.inra.sad.bagap.apiland.analysis.matrix.pixel;

import java.util.Map;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;

public class TranslateMatrixCalculation extends Pixel2PixelMatrixCalculation {
	
	private Map<Double, Double> map;
	
	public TranslateMatrixCalculation(Map<Double, Double> map, Matrix... m){
		super(m);
		this.map = map;
	}
	
	@Override
	public void doInit() {	}
	
	@Override
	protected double treatPixel(Pixel p) {
		return map.get(matrix().get(p));
	}

	@Override
	public void doClose() {	}

}

