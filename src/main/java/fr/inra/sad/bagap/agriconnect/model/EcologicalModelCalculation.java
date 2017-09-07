package fr.inra.sad.bagap.agriconnect.model;

import fr.inra.sad.bagap.apiland.analysis.matrix.pixel.Pixel2PixelMatrixCalculation;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;

public class EcologicalModelCalculation extends Pixel2PixelMatrixCalculation {
	
	private double intercept;
	
	private int[] codeHabitats;
	
	private double[] coeffHabitats;
	
	private double[] coeffMetrics;
	
	public EcologicalModelCalculation(double intercept, double[] coeffHabitats, int[] codeHabitats, double[] coeffMetrics, Matrix... m){
		super(m);
		this.intercept = intercept;
		this.codeHabitats = codeHabitats;
		this.coeffHabitats = coeffHabitats;
		this.coeffMetrics = coeffMetrics;
	}
	
	@Override
	public void doInit() {}
	
	@Override
	protected double treatPixel(Pixel p) {
		double coeffH = 1;
		double h = matrix().get(p);
		if(codeHabitats != null){
			for(int c=0; c<codeHabitats.length; c++){
				if(codeHabitats[c] == h){
					coeffH = coeffHabitats[c];
					break;
				}
			}
		}
		
		if(coeffH != 0){
			double[] valMetrics = new double[this.coeffMetrics.length];
			for(int i=0; i<coeffMetrics.length; i++){
				valMetrics[i+1] = wholeMatrix()[i+1].get(p);
			}
			return calculate(intercept, coeffH, coeffMetrics, valMetrics);
		}else{
			return Raster.getNoDataValue();
		}
	}

	@Override
	public void doClose() {
		codeHabitats = null;
		coeffHabitats = null;
		coeffMetrics = null;
	}
	
	public static double calculate(double intercept, double coeffH, double[] coeffMetrics, double[] valMetrics) {
		
		double v = 0, vi;
		v += intercept;
		for(int i=0; i<coeffMetrics.length; i++){
			vi = valMetrics[i];
			if(vi == Raster.getNoDataValue()){
				v = 0;
				break;
			}
			v += coeffMetrics[i] * vi;
		}
		if(v <= 0){
			v = Raster.getNoDataValue();
		}else{
			v = Math.exp(v); // fonction de lien inverse de la loi binomiale négative 
		}
		
		return coeffH*v;
	}


}
