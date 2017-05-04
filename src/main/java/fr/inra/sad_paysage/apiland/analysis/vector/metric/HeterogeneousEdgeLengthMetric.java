package fr.inra.sad_paysage.apiland.analysis.vector.metric;

import java.util.HashSet;
import java.util.Set;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

import fr.inra.sad_paysage.apiland.analysis.VariableManager;
import fr.inra.sad_paysage.apiland.core.composition.Composition;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.Raster;
import fr.inra.sad_paysage.apiland.core.time.Instant;

public class HeterogeneousEdgeLengthMetric extends VectorMetric {
	
	private String attribute;
	
	public HeterogeneousEdgeLengthMetric(String attribute) {
		super(VariableManager.get("E_Hete"));
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
				if(!ever.contains(p2) && !o1.equals(o2)){
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
