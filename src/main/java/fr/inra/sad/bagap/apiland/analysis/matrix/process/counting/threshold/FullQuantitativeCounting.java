package fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.threshold;

import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;

import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.Counting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.CountingDecorator;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;

public class FullQuantitativeCounting extends CountingDecorator {

	private final int size;
	
	private double min;
	
	private double max;
	
	// structure de données pour les valeurs quantitatives
	private double[][] datas;
	
	public FullQuantitativeCounting(Counting decorate, int size) {
		super(decorate);
		this.size = size;
	}
	
	@Override
	public double getMaximum() {
		if(max != -1*Double.MAX_VALUE){
			return max;
		}
		return Raster.getNoDataValue();
	}

	@Override
	public double getMinimum() {
		if(min != Double.MAX_VALUE){
			return min;
		}
		return Raster.getNoDataValue();
	}
	
	/*@Override
	public double[][] datas(){
		throw new UnsupportedOperationException();
	}*/
	
	@Override
	public double getTruncatedAverage(double threshold){
		double value = 0.0;
		int count = 0;
		double v;
		double v2 = 0.0;
		for(int j=0; j<size; j++){
			for(int i=0; i<size; i++){
				v = datas[j][i];
				if(v != Raster.getNoDataValue()){
					count++;
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

	@Override
	protected void doInit() {
		min = Double.MAX_VALUE;
		max = -1*Double.MAX_VALUE;
		datas = new double[size][size];
		for(int j=0; j<size; j++){
			for(int i=0; i<size; i++){
				datas[j][i] = Raster.getNoDataValue();
			}
		}
	}
	
	@Override
	protected void doDown(int d, int place){
		double[] tnewFill = new double[size];
		Arrays.fill(tnewFill, Raster.getNoDataValue());
		datas = Arrays.copyOfRange(datas, 1, datas.length);
		datas = ArrayUtils.addAll(datas, tnewFill);
	}

	@Override
	protected void doAdd(double value, int x, int y, int filter, double ch, double cv) {
		doAddValue(value, x, y);
	}
	
	@Override
	protected void doAddValue(double value, int x, int y) {
		if(value != Raster.getNoDataValue()){
			min = Math.min(min, value);
			max = Math.max(max, value);
			datas[y][x] = value;
		}
	}
	
	@Override
	public void doRemoveValue(double value, int x, int y) {
		if(value != Raster.getNoDataValue()){
			datas[y][x] = Raster.getNoDataValue();
			if(min == value){
				min = Double.MAX_VALUE;
				double v;
				for(int j=0; j<size; j++){
					for(int i=0; i<size; i++){
						v = datas[j][i];
						if(v != Raster.getNoDataValue()){
							min = Math.min(min, v);
						}
					}
				}
			}
			if(max == value){
				max = -1*Double.MAX_VALUE;
				double v;
				for(int j=0; j<size; j++){
					for(int i=0; i<size; i++){
						v = datas[j][i];
						if(v != Raster.getNoDataValue()){
							max = Math.max(max, v);
						}
					}
				}
			}
		}
	}
	
	@Override
	protected void doDelete() {
		datas = null;
	}

}
