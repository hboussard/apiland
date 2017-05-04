package fr.inra.sad_paysage.apiland.analysis.vector.metric;

import java.util.Set;

import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

import fr.inra.sad_paysage.apiland.analysis.VariableManager;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.Raster;
import fr.inra.sad_paysage.apiland.core.time.Instant;

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

