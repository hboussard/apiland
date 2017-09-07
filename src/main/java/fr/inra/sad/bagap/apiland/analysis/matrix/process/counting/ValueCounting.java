package fr.inra.sad.bagap.apiland.analysis.matrix.process.counting;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

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
		doAddValue(value, x, y);
	}
	
	@Override
	protected void doAddValue(double value, int x, int y) {
		if(value != Raster.getNoDataValue() && (value != 0)){
			if(!countV.containsKey((int) value)){
				countV.put((int) value, new Count());
			}
			countV.get((int) value).add();
		}
	}
	
	@Override
	protected void doRemoveValue(double value, int x, int y) {
		if(value != Raster.getNoDataValue() && (value != 0)){
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
	public Collection<Count> counts(){
		return countV.values();
	}
	
	@Override
	public int countValue(int v){
		if(countV.containsKey(v)){
			return countV.get(v).get();
		}
		return 0;
	}

}
