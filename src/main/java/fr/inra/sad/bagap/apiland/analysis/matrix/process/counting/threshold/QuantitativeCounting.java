package fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.threshold;

import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.Counting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.CountingDecorator;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;

/**
 * modeling class of an non-spatial quantitative process
 * @author H.Boussard
 */
public class QuantitativeCounting extends CountingDecorator {

	/** les valeurs triées */
	//private List<Double> values;
	
	private int nb;
	
	private int nbPos;
	
	private int nbNeg;
	
	private double sum;
	
	private double squaresum;
	
	public QuantitativeCounting(Counting decorate) {
		super(decorate);
	}
	
	@Override
	public double getSum(){
		if(size() == 0){
			return Raster.getNoDataValue();
		}
		if(sum < 0.000001 && sum > -0.000001){
			sum = 0;
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
	public double countPositives(){
		if(size() == 0){
			return Raster.getNoDataValue();
		}
		return nbPos;
	}
	
	@Override
	public double countNegatives(){
		if(size() == 0){
			return Raster.getNoDataValue();
		}
		return nbNeg;
	}
	
	@Override
	public double size(){
		return nb;
	}
	
	@Override
	public double getVariance(){
		if(size() == 0){
			return Raster.getNoDataValue();
		}
		return (getSquareSum() / size()) - (getAverage() * getAverage());
	}
	
	@Override
	public double getAverage(){
		if(size() == 0){
			return Raster.getNoDataValue();
		}
		return getSum() / size();
	}
	
	@Override
	public double getStandardDeviation(){
		if(size() == 0){
			return Raster.getNoDataValue();
		}
		return Math.sqrt(getVariance());
	}
	
	@Override
	public double getStandardError(){
		if(size() == 0){
			return Raster.getNoDataValue();
		}
		return getStandardDeviation() / Math.sqrt(size());
	}

	@Override
	public double getVariationCoefficient() {
		if(size() == 0){
			return Raster.getNoDataValue();
		}
		double a = getAverage();
		if(a == 0 || a == -0){
			return Raster.getNoDataValue();
		}
		double st = getStandardDeviation();
		if(Double.isNaN(st)){
			return Raster.getNoDataValue();
		}
		if(st == 0){
			return 0;
		}
		return 100.0 * st / a;
	}
	
	@Override
	public void doInit(){
		//values = new ArrayList<Double>();
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
	public void doAdd(double value, int x, int y, int filter, double ch, double cv) {
		doAddValue(value, x, y);
	}
	
	@Override
	protected void doAddValue(double value, int x, int y) {
		
		if(value != Raster.getNoDataValue()){
			if(value > 0){
				nbPos++;
			}else if(value < 0){
				//System.out.println(value);
				nbNeg++;
			}
			nb++;
			sum += value;
			squaresum += (value * value);
			//values.add(value);
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
			
			//values.remove(value);
		}
	}

	@Override
	public void doDelete() {
		/*
		values.clear();
		values = null;
		*/
	}
	
}
