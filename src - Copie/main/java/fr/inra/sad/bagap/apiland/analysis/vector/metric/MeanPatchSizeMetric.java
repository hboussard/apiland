package fr.inra.sad.bagap.apiland.analysis.vector.metric;

import java.util.Set;

import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

import fr.inra.sad.bagap.apiland.analysis.Stats;
import fr.inra.sad.bagap.apiland.analysis.VariableManager;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.core.time.Instant;

public class MeanPatchSizeMetric extends VectorMetric {
	
	public MeanPatchSizeMetric() {
		super(VariableManager.get("MPS"));
	} 

	@Override
	public void doCalculate(Point point, Set<Polygon> polygons, Instant t) {
		
		Stats sizes = new Stats();
		for(Polygon p : polygons){
			sizes.add(p.getArea());
		}
		sizes.calculate();
		value = sizes.getAverage();
		
		if(Double.isNaN(value)){
			value = Raster.getNoDataValue();
		}
		
	}

}

