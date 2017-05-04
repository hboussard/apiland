package fr.inra.sad_paysage.apiland.analysis.process;

import java.util.Set;
import java.util.TreeSet;
import fr.inra.sad_paysage.apiland.analysis.metric.Metric;

public abstract class ProcessType<M extends Metric> {

	/** observers of the whole process of this type */
	private Set<ProcessObserver> observers;
	
	/** metrics to be calculated */
	private Set<M> metrics;
	
	/** constructor */
	public ProcessType(){
		observers = new TreeSet<ProcessObserver>();
		metrics = new TreeSet<M>();
	}
	
	/**
	 * to add a set of metrics
	 * @param m new metrics
	 */
	protected void addMetrics(Set<M> m){
		metrics.addAll(m);
	}
	
	/**
	 * to add a single metric
	 * @param wm new metric
	 */
	public void addMetric(M wm){
		metrics.add(wm);
	}
	
	/** @return the metrics */
	public Set<M> metrics(){
		return metrics;
	}
	
	/** @return a specific metric */
	public Metric getMetric(String variable){
		for(M m : metrics){
			if(m.getName().equalsIgnoreCase(variable)){
				return m;
			}
		}
		throw new IllegalArgumentException();
	}
	
	/**
	 * to add an process observer 
	 * @param wpo new process observer
	 */
	public void addObserver(ProcessObserver wpo){
		observers.add(wpo);
	}
	
	/** @return the list of observers */
	public Set<ProcessObserver> observers(){
		return observers;
	}
	
	/** to delete an observer */
	public void clearObservers(){
		observers.clear();
	}
	
}
