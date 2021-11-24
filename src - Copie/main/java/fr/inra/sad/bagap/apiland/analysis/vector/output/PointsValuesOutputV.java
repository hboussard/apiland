package fr.inra.sad.bagap.apiland.analysis.vector.output;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.locationtech.jts.geom.Point;

import fr.inra.sad.bagap.apiland.analysis.process.Process;
import fr.inra.sad.bagap.apiland.analysis.process.metric.AbstractMetricOutput;
import fr.inra.sad.bagap.apiland.analysis.process.metric.Metric;
import fr.inra.sad.bagap.apiland.analysis.vector.process.VectorProcess;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;

public class PointsValuesOutputV extends AbstractMetricOutput {

	private Map<Point, Map<String, Double>> values;
	
	public PointsValuesOutputV(Set<Point> points){
		super();
		values = new HashMap<Point, Map<String, Double>>();
		for(Point p : points){
			values.put(p, new TreeMap<String, Double>());
		}
	}
	
	@Override
	public void notify(Metric m, String metric, double value, Process process){
		//System.out.println("enter : "+metric.replace(".0", "")+" "+value);
		values.get(((VectorProcess) process).point()).put(metric.replace(".0", ""), value);
	}
	
	public boolean hasValues(){
		return values.size() > 0;
	}
	
	public double get(Point p, String metric){
		//System.out.println("try : "+metric);
		if(values.containsKey(p) && values.get(p).containsKey(metric)){
			return values.get(p).get(metric);
		}
		return Raster.getNoDataValue();
	}
	
}

