package fr.inra.sad.bagap.apiland.analysis.vector.metric;

import java.util.Set;

import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

import fr.inra.sad.bagap.apiland.analysis.Variable;
import fr.inra.sad.bagap.apiland.analysis.process.metric.Metric;
import fr.inra.sad.bagap.apiland.analysis.vector.process.VectorProcess;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.core.time.Instant;

public abstract class VectorMetric extends Metric {
	
	public VectorMetric(Variable v){
		super(v);
	}
	
	/**
	 * to calculate the metric on a specific process
	 * @param wp the specific process
	 */
	public final void calculate(VectorProcess p, String pref, Point point, Set<Polygon> polygons, Instant t){
		value = Raster.getNoDataValue();
		doCalculate(point, polygons, t);
		notifyObservers(p, pref);
	}
	
	protected abstract void doCalculate(Point point, Set<Polygon> polygons, Instant t);
	
	public final void unCalculate(VectorProcess p, String pref){
		value = Raster.getNoDataValue();
		notifyObservers(p, pref);
	}
	
	public class Count{
		private double value = 0;
		public void add(double v){value += v;};
		public double get(){return value;};
	}
	
}
