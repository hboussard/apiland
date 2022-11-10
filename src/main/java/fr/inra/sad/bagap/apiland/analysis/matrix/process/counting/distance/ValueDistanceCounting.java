package fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.distance;

import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import org.apache.commons.lang3.ArrayUtils;

import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.Counting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.CountingDecorator;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;

public class ValueDistanceCounting extends CountingDecorator {

	private int size;
	
	// structure de données pour les valeurs
	private int[][] datas;
	
	private Map<Integer, Double> values;
	
	private Map<Integer, Double> countClass;
	
	private boolean calculated;
	
	// structure de données pour les valeurs
	//private Map<Integer, int[][]> datas;
	
	public ValueDistanceCounting(Counting decorate, int size) {
		super(decorate);
		this.size = size;
	}
	
	@Override
	protected void doInit() {
		datas = new int[size][size];
		for(int j=0; j<size; j++){
			Arrays.fill(datas[j], 0);
		}
		
		values = new TreeMap<Integer, Double>();
		
		countClass = new TreeMap<Integer, Double>();
		
		calculated = false;
		
		//datas = new TreeMap<Integer, int[][]>();
	}
	
	@Override
	protected void doDown(int d, int place){

		int[] tnewFill = new int[size];
		Arrays.fill(tnewFill, 0);
		datas = Arrays.copyOfRange(datas, 1, datas.length);
		datas = ArrayUtils.addAll(datas, tnewFill);
		
		calculated = false;
		
		/*
		for(Integer v : datas.keySet()){
			int[] tnewFill = new int[size];
			Arrays.fill(tnewFill, 0);
			datas.put(v, Arrays.copyOfRange(datas.get(v), 1, datas.get(v).length));
			datas.put(v, ArrayUtils.addAll(datas.get(v), tnewFill));
		}
		*/
	}

	@Override
	protected void doAdd(double value, int x, int y, int filter, double ch, double cv) {
		doAddValue(value, x, y);
	}
	
	@Override
	protected void doAddValue(double value, int x, int y) {
		
		if(value != Raster.getNoDataValue() && value != 0){
			datas[y][x] = (int) value;
		}
		
		/*
		if(value != Raster.getNoDataValue() && value != 0){
			//System.out.println("doAddValue "+value+" "+x+" "+y);
			
			if(!datas.containsKey((int) value)){
				datas.put((int) value, new int[size][size]);
			}
			datas.get((int) value)[y][x] = 1;
		}
		*/
	}
	
	@Override
	protected void doRemoveValue(double value, int x, int y) {
		
		if(value != Raster.getNoDataValue() && (value != 0)){
			datas[y][x] = 0;
		}
		
		/*
		if(value != Raster.getNoDataValue() && (value != 0)){
			//System.out.println("doRemoveValue "+value+" "+x+" "+(y));
			
			datas.get((int) value)[y][x] = 0;
		//	if(datas.get((int) value).size() == 0){
		//		remove ?;
		//	}
		}
		*/
	}
	
	@Override
	public void doDelete() {
		//datas.clear();
		datas = null;
		values.clear();
		values = null;
		countClass.clear();
		countClass = null;
	}
	
	private void calculate(){
		values.clear();
		countClass.clear();
		double v;
		double weight;
		for(int j=0; j<size; j++){
			for(int i=0; i<size; i++){
				v = datas[j][i];
				if(v > 0){
					if(!values.containsKey((int) v)){
						values.put((int) v, 0.0);
						//countClass.put((int) v, 0.0);
						countClass.put((int) v, Double.MIN_VALUE);
					}
					weight = process().window().weighted()[j][i];
					values.put((int) v, values.get((int) v) + weight); 
					//countClass.put((int) v, countClass.get((int) v) + 1);
					if(countClass.get((int) v) < weight){
						countClass.put((int) v, weight);
					}
				}
			}
		}
		for(Entry<Integer, Double> e : values.entrySet()){
			if(e.getValue() < 0){
				values.put(e.getKey(), 0.0);
			}
		}
		calculated = true;
	}
	
	@Override
	public Set<Integer> values(){
		if(!calculated){
			calculate();
		}
		return values.keySet();
		//return datas.keySet();
	}
	
	@Override
	public double countValue(int v){
		if(!calculated){
			calculate();
		}
		if(values.containsKey(v)){
			return values.get(v);
		}
		return 0;
		
		/*
		if(datas.containsKey(v)){
			double count = 0.0;
			for(int j=0; j<size; j++){
				for(int i=0; i<size; i++){
					if(datas.get(v)[j][i] == 1){
						count += process().window().weigted()[j][i];
					}
				}
			}
			return count;
		}
		return 0;
		*/
	}
	
	@Override
	public double countClass() {
		if(!calculated){
			calculate();
		}
		/*
		double c = 0;
		for(Integer v : countClass.keySet()){
			c += values.get(v) / countClass.get(v);
		}
		return c;
		*/
		
		double c = 0;
		for(Double v : countClass.values()){
			c += v;
		}
		return c;
		
	}

}
