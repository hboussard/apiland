package fr.inra.sad.bagap.apiland.analysis.vector.metric;

import java.util.Set;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

import fr.inra.sad.bagap.apiland.analysis.Stats;
import fr.inra.sad.bagap.apiland.analysis.VariableManager;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.core.time.Instant;

public class MeanCompacityMetric extends VectorMetric {
	
	public MeanCompacityMetric() {
		super(VariableManager.get("MKG"));
	}

	@Override
	public void doCalculate(Point point, Set<Polygon> polygons, Instant t) {
		
		Stats stats = new Stats();
		for(Polygon p : polygons){
			stats.add(0.28*p.getLength()/Math.sqrt(p.getArea()));
		}
		
		stats.calculate();
		
		value = stats.getAverage();
		
		if(Double.isNaN(value)){
			value = Raster.getNoDataValue();
		}
	} 
	
}
