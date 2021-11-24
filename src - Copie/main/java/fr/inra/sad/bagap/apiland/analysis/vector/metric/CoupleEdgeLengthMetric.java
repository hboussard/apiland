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

public class CoupleEdgeLengthMetric extends VectorMetric {

	private String attribute;
	
	private Object code1, code2;
	
	public CoupleEdgeLengthMetric(String attribute, Object code1, Object code2) {
		super(VariableManager.get("E_"+code1+"-"+code2));
		this.attribute = attribute;
		this.code1 = code1;
		this.code2 = code2;
	}

	@Override
	public void doCalculate(Point point, Set<Polygon> polygons, Instant t) {		
		Geometry i;
		value = 0.0;
		
		Set<Polygon> ever = new HashSet<Polygon>();
		for(Polygon p1 : polygons){
			if(((Composition) p1.getUserData()).getAttribute(attribute).isActive(t) 
					&& ((Composition) p1.getUserData()).getAttribute(attribute).getValue(t).equals(code1)){
				ever.add(p1);
				for(Polygon p2 : polygons){
					if(((Composition) p2.getUserData()).getAttribute(attribute).isActive(t)
							&& ((Composition) p2.getUserData()).getAttribute(attribute).getValue(t).equals(code2)
							&& !ever.contains(p2)){
						if(p1.intersects(p2)){
							i = p1.intersection(p2);
							value += i.getLength();
						}
					}
				}
			}
		}
		
		if(Double.isNaN(value)){
			value = Raster.getNoDataValue();
		}
		
		//System.out.println(t.year()+" edgelength = "+value);
	} 
	
}
