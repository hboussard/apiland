package fr.inra.sad.bagap.apiland.analysis.vector.metric;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

import fr.inra.sad.bagap.apiland.analysis.VariableManager;
import fr.inra.sad.bagap.apiland.core.composition.Composition;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.core.time.Instant;

public class ShannonMetric extends VectorMetric {

	private Set<Object> codes;
	
	public ShannonMetric(Set<Object> codes) {
		super(VariableManager.get("SHDI"));
		this.codes = codes;
	}

	@Override
	public void doCalculate(Point point, Set<Polygon> polygons, Instant t) {
		Map<Object, Count> areas = new HashMap<Object, Count>();
		for(Object code : codes){
			areas.put(code, new Count());
		}
		
		Object code;
		double total = 0.0;
		for(Polygon p : polygons){
			total += p.getArea();
			code = (Object) ((Composition) p.getUserData()).getAttribute("cover").getValue(t);
			if(codes.contains(code)){
				areas.get(code).add(p.getArea());
			}
		}
		
		value = 0.0;
		double p;
		for(Object c : codes){
			p = areas.get(c).get() / total;
			if(p != 0){
				value += p*Math.log(p);
			}
		}
		
		if(value != 0){
			value *= -1;
		}
		
		if(Double.isNaN(value)){
			value = Raster.getNoDataValue();
		}
	}

}
