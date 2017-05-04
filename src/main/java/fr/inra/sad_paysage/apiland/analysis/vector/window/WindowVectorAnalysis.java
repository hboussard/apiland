package fr.inra.sad_paysage.apiland.analysis.vector.window;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import fr.inra.sad_paysage.apiland.analysis.vector.VectorAnalysis;
import fr.inra.sad_paysage.apiland.analysis.vector.metric.VectorMetric;
import fr.inra.sad_paysage.apiland.analysis.vector.process.VectorProcessType;
import fr.inra.sad_paysage.apiland.core.element.DynamicLayer;
import fr.inra.sad_paysage.apiland.core.time.Instant;

public abstract class WindowVectorAnalysis extends VectorAnalysis {

	protected DynamicLayer<?> layer;
	
	protected VectorProcessType processType;
	
	protected Set<Double> buffers;
	
	protected Set<VectorMetric> metrics;
	
	protected double minRate;
	
	protected Instant t;
	
	protected Map<String, Set<Object>> filters;
	
	protected Map<String, Set<Object>> unfilters;
	
	public WindowVectorAnalysis(DynamicLayer<?> layer, Set<VectorMetric> metrics, Instant t, VectorProcessType processType) {
		super();
		this.layer = layer;
		this.metrics = metrics;
		this.t = t;
		this.processType = processType;
	}
	
	public WindowVectorAnalysis(DynamicLayer<?> layer, Set<Double> buffers, Set<VectorMetric> metrics, double minRate,
			Map<String, Set<Object>> filters, Map<String, Set<Object>> unfilters, Instant t, VectorProcessType processType) {
		super();
		this.layer = layer;
		this.buffers = buffers;
		this.metrics = metrics;
		this.minRate = minRate;
		
		if(filters.size() > 0){
			this.filters = new HashMap<String, Set<Object>>();
			this.filters.putAll(filters);
		}
		
		if(unfilters.size() > 0){
			this.unfilters = new HashMap<String, Set<Object>>();
			this.unfilters.putAll(unfilters);
		}
		
		this.t = t;
		this.processType = processType;
	}
	
	public DynamicLayer<?> layer(){
		return layer;
	}
	
	public Set<Double> buffers(){
		return buffers;
	}
	
	public Set<VectorMetric> metrics(){
		return metrics;
	}
	
	protected int getMaxBuffer(){
		Iterator<Double> ite = buffers.iterator();
		double b;
		int max = Integer.MIN_VALUE;
		while(ite.hasNext()){
			b = ite.next();
			max = Math.max(max, new Double(b).intValue());
		}
		return max;
	}
	
	public void setTime(Instant t){
		this.t = t;
	}
	
}
