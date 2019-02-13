package fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.distance;

import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;

import fr.inra.sad.bagap.apiland.analysis.matrix.process.SimpleWindowMatrixProcess;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.Counting;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;

public class BasicDistanceCounting extends Counting {

	private int size;
	
	/** the total count of values 1 if Raster.getNoDataValue, 2 if 0 and 3 if value */
	private int[][] values;
	
	private double total, valid, count;
	
	private boolean calculated;
	
	public BasicDistanceCounting(int size){
		this.size = size;
	}
	
	@Override
	public void delete() {
		super.delete();
		values = null;
	}
	
	@Override
	public void init() {
		values = new int[size][size];
		for(int j=0; j<size; j++){
			Arrays.fill(values[j], 0);
		}
		
		calculated = false;
	}
	
	@Override
	public SimpleWindowMatrixProcess process(){
		return (SimpleWindowMatrixProcess) super.process();
	}
	
	@Override
	public void add(double value, int x, int y, int filter, double ch, double cv) {
		addValue(value, x, y);
	}
	
	@Override
	public void addValue(double value, int x, int y) {
		if(value != Raster.getNoDataValue()){
			if(value != 0){
				values[y][x] = 3;
			}else{
				values[y][x] = 2;
			}
		}else{
			values[y][x] = 1;
		}
	}
	
	@Override
	public void removeValue(double value, int x, int y) {
		values[y][x] = 0;
	}
	
	@Override
	public void addCouple(double couple, int x1, int y1, int x2, int y2) {
		// do nothing
	}

	@Override
	public void removeCouple(double couple, int x1, int y1, int x2, int y2) {
		// do nothing
	}
	
	@Override
	public void down(int d, int place) {
		int outx, outy;
		// on enleve les valeurs qui ne sont plus actuelles
		//System.out.println("enleve les valeurs "+window().removeDownList());
		//System.out.println("enleve les valeurs");
		for(Pixel p : process().window().removeDownList()){
			outx = process().window().outXWindow(p.x());
			outy = process().window().outYWindow(p.y());
			if(outy >= 0 && outy < process().processType().matrix().height()
					&& outx >= 0 && outx < process().processType().matrix().width()){
				//System.out.println("remove "+p+" "+process().values()[p.y()][p.x()]);
				process().counting().removeValue(process().values()[p.y()][p.x()], p.x(), p.y());
			}
		}
		// on ajoute les valeurs qui sont devenues actuelles
		//System.out.println("ajoute les valeurs "+window().addDownList());
		//System.out.println("ajoute les valeurs ");
		for(Pixel p : process().window().addDownList()){
			outx = process().window().outXWindow(p.x());
			outy = process().window().outYWindow(p.y());
			if(outy >= 0 && outy < process().processType().matrix().height()
					&& outx >= 0 && outx < process().processType().matrix().width()){
				//System.out.println("add "+p+" "+process().values()[p.y()][p.x()]);
				//process().counting().addValue(process().values()[p.y()][p.x()], p.x(), p.y());
				if(p.y() < process().window().diameter() - d + place){
					process().counting().addValue(process().values()[p.y()][p.x()], p.x(), p.y());
				}
			}
		}
		
		int[] tnewFill = new int[size];
		Arrays.fill(tnewFill, 0);
		values = Arrays.copyOfRange(values, 1, values.length);
		values = ArrayUtils.addAll(values, tnewFill);
		
		calculated = false;
	}
	
	@Override
	public int theoreticalSize(){
		return process().window().theoreticalSize();
	}
	
	private void calculate(){
		total = 0;
		valid = 0;
		count = 0;
		double v;
		double weight;
		for(int j=0; j<size; j++){
			for(int i=0; i<size; i++){
				v = values[j][i];
				weight = process().window().weighted()[j][i];
				if(weight > 0){
					if(v > 0){
						total += weight;
						if(v > 1){
							valid += weight;
							if(v == 3){
								count += weight;
							}
						}
					}
				}
			}
		}
		calculated = true;
	}
	
	@Override
	public double totalValues(){
		if(!calculated){
			calculate();
		}
		return total;
	}
	
	@Override
	public double validValues(){
		if(!calculated){
			calculate();
		}
		return valid;
	}
	
	@Override
	public double countValues(){
		if(!calculated){
			calculate();
		}
		return count;
	}
	
}
