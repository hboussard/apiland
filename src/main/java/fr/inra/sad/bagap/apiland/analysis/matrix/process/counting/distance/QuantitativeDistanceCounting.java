package fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.distance;

import java.util.Arrays;
import org.apache.commons.lang3.ArrayUtils;

import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.Counting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.CountingDecorator;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;

public class QuantitativeDistanceCounting extends CountingDecorator {
	
	private final int size;
	
	// structure de données pour les valeurs quantitatives
	private double[][] datas;
	
	private boolean calculated;
	
	private double count, sum, max, min, squareSum, countPos, countNeg;
	
	public QuantitativeDistanceCounting(Counting decorate, int size) {
		super(decorate);
		this.size = size;
	}
	
	@Override
	public void doInit(){
		datas = new double[size][size];
		for(int j=0; j<size; j++){
			for(int i=0; i<size; i++){
				datas[j][i] = Raster.getNoDataValue();
			}
		}
		calculated = false;
	}
	
	@Override
	protected void doDown(int d, int place){
		double[] tnewFill = new double[size];
		Arrays.fill(tnewFill, Raster.getNoDataValue());
		datas = Arrays.copyOfRange(datas, 1, datas.length);
		datas = ArrayUtils.addAll(datas, tnewFill);
		calculated = false;
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
			datas[y][x] = value;
		}
	}
	
	@Override
	public void doRemoveValue(double value, int x, int y) {
		if(value != Raster.getNoDataValue()){
			datas[y][x] = Raster.getNoDataValue();
		}
	}

	@Override
	public void doDelete() {
		datas = null;
	}
	
	private void calculate(){
		count = 0.0;
		sum = 0.0;
		max = Double.MIN_VALUE;
		min = Double.MAX_VALUE;
		squareSum = 0.0;
		countPos = 0.0;
		countNeg = 0.0;
		double v;
		double weight;
		double produit;
		for(int j=0; j<size; j++){
			for(int i=0; i<size; i++){
				v = datas[j][i];
				if(v != Raster.getNoDataValue()){
					weight = process().window().weighted()[j][i];
					produit = weight * v;
							
					count += weight;
					sum += produit;
					max = Math.max(max, produit);
					min = Math.min(min, produit);
					squareSum += Math.pow(produit, 2);
					
					if(produit > 0){
						countPos += weight;
					}
					if(produit < 0){
						countNeg += weight;
					}
				}
			}
		}
		calculated = true;
	}
	
	@Override
	public double size(){
		if(!calculated){
			calculate();
		}
		return count;
	}
	
	@Override
	public double getSum(){
		if(!calculated){
			calculate();
		}
		return sum;
	}
	
	@Override
	public double getAverage(){
		if(!calculated){
			calculate();
		}
		if(count == 0){
			return Raster.getNoDataValue();
		}
		return sum / count;
	}
	
	@Override
	public double getMaximum() {
		if(!calculated){
			calculate();
		}
		if(max != Double.MIN_VALUE){
			return max;
		}
		return Raster.getNoDataValue();
	}
	
	@Override
	public double getMinimum() {
		if(!calculated){
			calculate();
		}
		if(min != Double.MAX_VALUE){
			return min;
		}
		return Raster.getNoDataValue();
	}
	
	@Override
	public double getSquareSum(){
		if(!calculated){
			calculate();
		}
		return squareSum;
	}
	
	@Override
	public double countPositives(){
		if(!calculated){
			calculate();
		}
		return countPos;
	}
	
	@Override
	public double countNegatives(){
		if(!calculated){
			calculate();
		}
		return countNeg;
	}
	
	@Override
	public double getVariance(){
		if(!calculated){
			calculate();
		}
		if(count == 0){
			return Raster.getNoDataValue();
		}
		return (squareSum / count) - Math.pow(sum / count, 2);
	}
	
	@Override
	public double getStandardDeviation(){
		if(!calculated){
			calculate();
		}
		if(count == 0){
			return Raster.getNoDataValue();
		}
		return Math.sqrt((squareSum / count) - Math.pow(sum / count, 2));
	}
	
	@Override
	public double getStandardError(){
		if(!calculated){
			calculate();
		}
		if(count == 0){
			return Raster.getNoDataValue();
		}
		return Math.sqrt((squareSum / count) - Math.pow(sum / count, 2)) / Math.sqrt(count);
	}
	
	@Override
	public double getVariationCoefficient() {
		if(!calculated){
			calculate();
		}
		if(count == 0){
			return Raster.getNoDataValue();
		}
		return 100.0 * Math.sqrt((squareSum / count) - Math.pow(sum / count, 2))/(sum / count);
	}
	
	@Override
	public double getTruncatedAverage(double threshold){
		double value = 0.0;
		double count = 0;
		double v;
		double v2 = 0.0;
		double weight;
		for(int j=0; j<size; j++){
			for(int i=0; i<size; i++){
				v = datas[j][i];
				if(v != Raster.getNoDataValue()){
					weight = process().window().weighted()[j][i];
					count += weight;
					v *= weight;
					if(v <= threshold){
						v2 += v/threshold;
					}else{
						v2 += 1;
					}
					
				}
			}
		}
		
		if(count == 0){
			value = 0;
		}else{
			value = v2/count;
		}
		return value;
	}
	
}