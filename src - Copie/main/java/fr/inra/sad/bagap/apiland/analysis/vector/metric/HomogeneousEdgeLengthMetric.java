package fr.inra.sad.bagap.apiland.analysis.vector.metric;

import java.util.HashSet;
import java.util.Set;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

import fr.inra.sad.bagap.apiland.analysis.VariableManager;
import fr.inra.sad.bagap.apiland.core.composition.Composition;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.core.time.Instant;

public class HomogeneousEdgeLengthMetric extends VectorMetric {
	
	private String attribute;
	
	public HomogeneousEdgeLengthMetric(String attribute) {
		super(VariableManager.get("E_Homo"));
		this.attribute = attribute;
	}

	@Override
	public void doCalculate(Point point, Set<Polygon> polygons, Instant t) {
		
		Geometry i;
		value = 0.0;
		Object o1, o2;
		Set<Polygon> ever = new HashSet<Polygon>();
		for(Polygon p1 : polygons){
			ever.add(p1);
			o1 = ((Composition) p1.getUserData()).getAttribute(attribute).getValue(t);
			for(Polygon p2 : polygons){
				o2 = ((Composition) p2.getUserData()).getAttribute(attribute).getValue(t);
				if(!ever.contains(p2) && o1.equals(o2)){
					if(p1.intersects(p2)){
						i = p1.intersection(p2);
						value += i.getLength();
					}
				}
			}
		}
		
		if(Double.isNaN(value)){
			value = Raster.getNoDataValue();
		}
	} 
	
}
