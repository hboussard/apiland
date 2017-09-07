package fr.inra.sad.bagap.apiland.analysis.matrix.process.counting;

import java.util.HashMap;
import java.util.Map;

import fr.inra.sad.bagap.apiland.analysis.Stats;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;

public class ClassCounting extends CountingDecorator {

	private Map<Object, Count> counts;
	
	private Stats stats;
	
	public ClassCounting(Counting decorate) {
		super(decorate);
	}
	
	@Override
	public void doInit() {
		counts = new HashMap<Object, Count>();
	}
	
	@Override
	protected void doAdd(double value, int x, int y, int filter, double ch,	double cv) {
		if(value != Raster.getNoDataValue()){
			if(!counts.containsKey(value)){
				counts.put(value, new Count());
			}
			counts.get(value).add();
		}
	}
	
	@Override
	public void doAddValue(double value, int x, int y) {
		if(value != Raster.getNoDataValue()){
			if(!counts.containsKey(value)){
				counts.put(value, new Count());
			}
			counts.get(value).add();
		}
	}
	
	@Override
	public void doRemoveValue(double value, int x, int y) {
		if(value != Raster.getNoDataValue()){
			counts.get(value).minus();
			if(counts.get(value).get() == 0){
				counts.remove(value);
			}
		}
	}

	@Override
	public void doDelete() {
		counts.clear();
		counts = null;
		stats = null;
	}
	
	@Override
	protected void doDown() {
		stats = null;
	}
	
	@Override
	public int countClass(){
		if(counts.containsKey(0.0)){
			return counts.size()-1;
		}
		return counts.size();
	}
	
	@Override
	public double averageClass(){
		calculate();
		return stats.getAverage();
	}
	
	@Override
	public double varianceClass(){
		calculate();
		return stats.getVariance();
	}
	
	@Override
	public double standardDeviationClass(){
		calculate();
		return stats.getStandardDeviation();
	}
	
	private void calculate() {
		if(stats == null){
			stats = new Stats();
			for(Count c : counts.values()){
				stats.add(c.get());
			}
			stats.calculate();
		}
	}

	
}
