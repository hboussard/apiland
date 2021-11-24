package fr.inra.sad.bagap.apiland.analysis.vector.window;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;

import fr.inra.sad.bagap.apiland.analysis.vector.metric.VectorMetric;
import fr.inra.sad.bagap.apiland.analysis.vector.process.VectorProcessType;
import fr.inra.sad.bagap.apiland.analysis.window.WindowAnalysisObserver;
import fr.inra.sad.bagap.apiland.analysis.window.WindowAnalysisType;
import fr.inra.sad.bagap.apiland.core.element.DynamicLayer;
import fr.inra.sad.bagap.apiland.core.time.Instant;

public class WindowVectorAnalysisBuilder {

	private WindowAnalysisType type;
	
	private Instant time;
	
	private double displacement;
	
	private Set<DynamicLayer<?>> layers;
	
	private VectorProcessType processType;
	
	private Set<WindowAnalysisObserver> observers;
	
	private Map<String, Set<Object>> filters;
	
	private Map<String, Set<Object>> unfilters;
	
	private Geometry geomFilter;
	
	private Set<Point> points;
	
	private Set<Double> buffers;
	
	private Set<VectorMetric> metrics;
	
	private double minRate;
	
	private double minX, maxX, minY, maxY;
	
	public WindowVectorAnalysisBuilder(WindowAnalysisType t) {
		this.type = t;
		observers = new HashSet<WindowAnalysisObserver>();
		filters = new HashMap<String, Set<Object>>();
		unfilters = new HashMap<String, Set<Object>>();
		points = new TreeSet<Point>(new Comparator<Point>(){
			@Override
			public int compare(Point p1, Point p2) {
				if(p1.getCoordinate().y < p2.getCoordinate().y){
					return 1;
				}else if(p1.getCoordinate().y > p2.getCoordinate().y){
					return -1;
				}else{
					if(p1.getCoordinate().x < p2.getCoordinate().x){
						return -1;
					}else if(p1.getCoordinate().x > p2.getCoordinate().x){
						return 1;
					}else{
						return 0;
					}
				}
			}
		});
		buffers = new TreeSet<Double>();
		metrics = new TreeSet<VectorMetric>();
		layers  = new HashSet<DynamicLayer<?>>();
		reset();
	}
	
	private void reset(){
		processType = null;
		displacement = 1;
		minRate = 0;
		minX = -1;
		maxX = -1;
		minY = -1;
		maxY = -1;
		observers.clear();
		filters.clear();
		unfilters.clear();
		geomFilter = null;
		points.clear();
		buffers.clear();
		metrics.clear();
		layers.clear();
	}
	
	public void setTime(Instant t){
		this.time = t;
	}
	
	public void addLayer(DynamicLayer<?> layer){
		this.layers.add(layer);
	}
	
	public void addPoint(Point p){
		this.points.add(p);
	}
	
	public void addPoints(Set<Point> points){
		this.points.addAll(points);
	}
	
	public void addBuffer(double b){
		this.buffers.add(b);
	}
	
	public void addBuffers(Set<Double> b){
		this.buffers.addAll(b);
	}
	
	public void addMetric(VectorMetric m){
		this.metrics.add(m);
	}
	
	public void setDisplacement(int d) {
		this.displacement = d;
	}
	
	public void setMinRate(double mr){
		this.minRate = mr;
	}
	
	public void addObserver(WindowAnalysisObserver o){
		observers.add(o);
	}
	
	public void addFilter(Geometry g){
		this.geomFilter = g;
	}
	
	public void addFilter(String att, Object val){
		if(!filters.containsKey(att)){
			filters.put(att, new HashSet<Object>());
		}
		filters.get(att).add(val);
	}
	
	public void setFilters(Map<String, Set<Object>> f){
		filters.putAll(f);
	}
	
	public void addUnfilter(String att, Object val){
		if(!unfilters.containsKey(att)){
			unfilters.put(att, new HashSet<Object>());
		}
		unfilters.get(att).add(val);
	}
	
	public void setUnfilters(Map<String, Set<Object>> f){
		unfilters.putAll(f);
	}
	
	public void setProcessType(VectorProcessType pt) {
		this.processType = pt;
	}
	
	public void setEnveloppe(double minX, double maxX, double minY, double maxY){
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
	}
	
	public void setMinX(double minX){
		this.minX = minX;
	}
	
	public void setMaxX(double maxX){
		this.maxX = maxX;
	}
	
	public void setMinY(double minY){
		this.minY = minY;
	}
	
	public void setMaxY(double maxY){
		this.maxY = maxY;
	}

	public <W extends WindowVectorAnalysis> W build(){
		W wa = null;
		
		switch (type) {
		case SLIDING : 
			wa = (W) new SlidingWindowVectorAnalysis(layers.iterator().next(), buffers, metrics, minRate, filters, unfilters, geomFilter, displacement, 
					time, processType, minX, maxX, minY, maxY);
			for(WindowAnalysisObserver o : observers){
				wa.addObserver(o); // the analysis is observed 
				
				processType.addObserver(o); // the process type is observed
				
				// metrics are observed
				for(VectorMetric wm : metrics){
					wm.addObserver(o);
				}
			}
			break;
		case SELECTED : 
			wa = (W) new SelectedWindowVectorAnalysis(layers.iterator().next(), buffers, metrics, minRate, filters, unfilters, points, time, processType);
			for(WindowAnalysisObserver o : observers){
				wa.addObserver(o); // the analysis is observed 
				
				processType.addObserver(o); // the process type is observed
				
				// metrics are observed
				for(VectorMetric wm : metrics){
					wm.addObserver(o);
				}
			} 
			
			break;
		case MAP : 
			wa = (W) new MapWindowVectorAnalysis(layers.iterator().next(), metrics, time, processType);
			for(WindowAnalysisObserver o : observers){
				wa.addObserver(o); // the analysis is observed 
				
				processType.addObserver(o); // the process type is observed
				
				// metrics are observed
				for(VectorMetric wm : metrics){
					wm.addObserver(o);
				}
			}
			break;
		case GRID : 
			throw new IllegalArgumentException();
		}
		
		//reset();
		
		return wa;
	}
}
