package fr.inra.sad_paysage.apiland.analysis.matrix.counting;

import fr.inra.sad_paysage.apiland.core.space.impl.raster.Raster;

public class QuantitativeCounting21 extends CountingDecorator {

	/** the sum */
	private double sum;
	
	/** the square sum */
	private double squaresum;
	
	/** the variance */
	private double variance;
	
	/** variance is calculated ? */
	private boolean hasVariance;
	
	/** the average */
	private double average;
	
	/** average is calculated */
	private boolean hasAverage;
	
	/** the standard deviation */
	private double stddeviation;
	
	/** standard deviation is calculated ? */
	private boolean hasStddeviation;
	
	/** the standard error */
	private double stderror;
	
	/** standard error is calculated ? */
	private boolean hasStderror;
	
	/** the variation coefficient */
	private double variationCoefficient;
	
	/** variation coefficient is calculated */
	private boolean hasVariationCoefficient;
	
	/** the internal size */
	private int nb;
	
	/** the number of positive values */
	private int nbPos;
	
	/** the number of negative values */
	private int nbNeg;
	
	/** the maximum value */
	private double max;
	
	/** the minimum value */
	private double min;
	
	public QuantitativeCounting2(Counting decorate) {
		super(decorate);
	}
	
	@Override
	public double getVariance(){
		if(size() == 0){
			return Raster.getNoDataValue();
		}
		if(!hasVariance){
			variance = (getSquareSum() - (getAverage() * getAverage())) / size();
			hasVariance = true;
		}
		return variance;
	}
	
	@Override
	public double getAverage(){
		if(size() == 0){
			return Raster.getNoDataValue();
		}
		if(!hasAverage){
			average = getSum() / size();
			hasAverage = true;
		}
		return average;
	}
	
	@Override
	public double getSum(){
		if(size() == 0){
			return Raster.getNoDataValue();
		}
		return sum;
	}
	
	@Override
	public double getSquareSum(){
		if(size() == 0){
			return Raster.getNoDataValue();
		}
		return squaresum;
	}
	
	@Override
	public double getStandardDeviation(){
		if(size() == 0){
			return Raster.getNoDataValue();
		}
		if(!hasStddeviation){
			stddeviation = Math.sqrt(getVariance());
			hasStddeviation = true;
		}
		return stddeviation;
	}
	
	@Override
	public double getMaximum(){
		if(size() == 0){
			return Raster.getNoDataValue();
		}
		return max;
	}
	
	@Override
	public double getMinimum(){
		if(size() == 0){
			return Raster.getNoDataValue();
		}
		return min;
	}
	
	@Override
	public double getStandardError(){
		if(size() == 0){
			return Raster.getNoDataValue();
		}
		if(!hasStderror){
			stderror = getStandardDeviation() / Math.sqrt(size());
			hasStderror = true;
		}
		return stderror;
	}
	
	@Override
	public int countPositives(){
		if(size() == 0){
			return Raster.getNoDataValue();
		}
		return nbPos;
	}
	
	@Override
	public int countNegatives(){
		if(size() == 0){
			return Raster.getNoDataValue();
		}
		return nbNeg;
	}
	
	@Override
	public int size(){
		return nb;
	}

	@Override
	public double getVariationCoefficient() {
		if(size() == 0){
			return Raster.getNoDataValue();
		}
		if(!hasVariationCoefficient){
			variationCoefficient = getStandardDeviation()/getAverage()*100.0;
		}
		return variationCoefficient;
	}

	@Override
	public void doInit(){
		hasAverage = false;
		hasVariance = false;
		hasStddeviation = false;
		hasStderror = false;
		hasVariationCoefficient = false;
		max = Double.MIN_VALUE;
		min = Double.MAX_VALUE;
		nb = 0;
		sum = 0;
		squaresum = 0;
		nbPos = 0;
		nbNeg = 0;
	}
		
	/**
	 * to add a value
	 * @param v the value to add
	 */
	@Override
	protected void doAdd(double value, int x, int y, int filter, double ch,	double cv) {
		if(value != Raster.getNoDataValue()){
			if(value > 0){
				nbPos++;
			}else if(value < 0){
				nbNeg++;
			}
			nb++;
			sum += value;
			squaresum += (value * value); 
			max = Math.max(max, value);
			min = Math.min(min, value);
		}
	}
	
	@Override
	public void doRemoveValue(double value, int x, int y) {
		if(value != Raster.getNoDataValue()){
			if(value > 0){
				nbPos--;
			}else if(value < 0){
				nbNeg--;
			}
			nb--;
			sum -= value;
			squaresum -= (value * value); 
		}
	}

	@Override
	public void doDelete() {
		// do nothing
	}
	
}
