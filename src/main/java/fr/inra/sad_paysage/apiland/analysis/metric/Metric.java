package fr.inra.sad_paysage.apiland.analysis.metric;

import java.util.HashSet;
import java.util.Set;
import fr.inra.sad_paysage.apiland.analysis.Variable;
import fr.inra.sad_paysage.apiland.analysis.matrix.process.WindowMatrixProcess;
import fr.inra.sad_paysage.apiland.analysis.process.Process;

/**
 * modeling class of a metric
 * @author H.Boussard
 */
public abstract class Metric implements Comparable<Metric> {
	
	/**
	 * minimum rate of non missing values
	 */
	protected static double minRate = 0;
	
	/** the refereed variable */
	private Variable variable;
	
	/** the metric observers */
	private Set<MetricObserver> observers;
	
	/** the metric observers to remove*/
	//private Set<MetricObserver> removers;
	
	/** the result value */
	protected double value;
	
	/**
	 * constructor
	 * @param v the refereed variable
	 */
	public Metric(Variable v){
		//System.out.println("création de la métrique "+v);
		this.variable = v;
		observers = new HashSet<MetricObserver>();
		//removers = new HashSet<MetricObserver>();
	}
	
	@Override
	public String toString(){
		return variable.getName();
	}
	
	public static void setMinRate(double min){
		minRate= min;
	}
	
	public static double minRate(){
		return minRate;
	}
	
	/**
	 * to set the refereed variable
	 * @param v the refereed variable
	 */
	public void setVariable(Variable v){
		this.variable = v;
	}
	
	/** @return the name of the variable */
	public String getName(){
		return variable.getName();
	}
	
	/**
	 * to add an observer
	 * @param o the observer
	 */
	public void addObserver(MetricObserver o){
		observers.add(o);
	}
	
	public void removeObserver(MetricObserver o){
		//removers.add(o);
	}
	
	/**
	 * to notify the observers
	 * @param <AnalysisProcess>
	 * @param wp the calling process
	 * @param pref the prefix of the metric name
	 */
	public void notifyObservers(Process wp, String pref){
		for(MetricObserver o : observers){
			o.notify(this, pref+variable.getName(), value, wp);
		}
		/*if(removers.size() != 0){
			for(MetricObserver o : removers){
				observers.remove(o);
			}
			removers.clear();
		}*/
	}

	@Override
	public int compareTo(Metric m) {
		return this.getName().compareTo(m.getName());
	}
	
}
