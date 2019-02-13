package fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.threshold;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.Counting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.CountingDecorator;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.Counting.Count;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;

public class ValueCounting extends CountingDecorator {
	
	/** the count of values */
	private Map<Integer, Count> countV;
	
	public ValueCounting(Counting decorate) {
		super(decorate);
	}
	
	@Override
	public void doInit() {
		countV = new TreeMap<Integer, Count>();
	}
	
	@Override
	public void doAdd(double value, int x, int y, int filter, double ch, double cv) {
		//System.out.println("doAdd (ValueCounting)");
		doAddValue(value, x, y);
	}
	
	@Override
	protected void doAddValue(double value, int x, int y) {
		//System.out.println("doAddValue (ValueCounting)");
		if(value != Raster.getNoDataValue() && (value != 0)){
			//System.out.println("doAddValue "+value+" "+x+" "+y);
			if(!countV.containsKey((int) value)){
				countV.put((int) value, new Count());
			}
			countV.get((int) value).add();
		}
	}
	
	@Override
	protected void doRemoveValue(double value, int x, int y) {
		if(value != Raster.getNoDataValue() && (value != 0)){
			//System.out.println("doRemoveValue "+value+" "+x+" "+y);
			countV.get((int) value).minus();
			if(countV.get((int) value).get() == 0){
				countV.remove((int) value);
			}
		}
	}
	
	@Override
	public void doDelete() {
		countV.clear();
		countV = null;
	}
	
	@Override
	public Set<Integer> values(){
		return countV.keySet();
	}
	
	@Override
	public double countValue(int v){
		if(countV.containsKey(v)){
			return countV.get(v).get();
		}
		return 0;
	}

	@Override
	public double countClass() {
		return countV.size();
	}
	
}
