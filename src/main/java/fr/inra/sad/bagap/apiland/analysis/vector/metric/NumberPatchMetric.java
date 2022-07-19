package fr.inra.sad.bagap.apiland.analysis.vector.metric;

import java.util.Set;

import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

import fr.inra.sad.bagap.apiland.analysis.VariableManager;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.core.time.Instant;

public class NumberPatchMetric extends VectorMetric {
	
	public NumberPatchMetric() {
		super(VariableManager.get("NP"));
	} 

	@Override
	public void doCalculate(Point point, Set<Polygon> polygons, Instant t) {
		value = polygons.size();
		
		if(Double.isNaN(value)){
			value = Raster.getNoDataValue();
		}
	}

}

