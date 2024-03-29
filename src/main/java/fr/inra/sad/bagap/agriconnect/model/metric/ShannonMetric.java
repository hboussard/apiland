package fr.inra.sad.bagap.agriconnect.model.metric;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

import fr.inra.sad.bagap.apiland.analysis.VariableManager;
import fr.inra.sad.bagap.apiland.analysis.vector.metric.VectorMetric;
import fr.inra.sad.bagap.apiland.capfarm.model.Cover;
import fr.inra.sad.bagap.apiland.capfarm.model.CoverGroup;
import fr.inra.sad.bagap.apiland.core.composition.Composition;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.core.time.Instant;

public class ShannonMetric extends VectorMetric {

	private Set<CoverGroup> codes;
	
	public ShannonMetric(Set<CoverGroup> codes) {
		super(VariableManager.get("SHDI"));
		this.codes = codes;
	}

	@Override
	public void doCalculate(Point point, Set<Polygon> polygons, Instant t) {
		Map<Cover, Count> areas = new HashMap<Cover, Count>();
		for(CoverGroup code : codes){
			areas.put(code, new Count());
		}
		
		Object code;
		double total = 0.0;
		for(Polygon p : polygons){
			total += p.getArea();
			code = (Cover) ((Composition) p.getUserData()).getAttribute("cover").getValue(t);
			for(CoverGroup group : codes){
				if(group.contains((Cover) code)){
					areas.get(group).add(p.getArea());
					break;
				}
			}/*
			if(codes.contains(code)){
				areas.get(code).add(p.getArea());
			}*/
		}
		
		value = 0.0;
		double p;
		for(Cover c : codes){
			//System.out.println(c+" "+areas.get(c).get());
			p = areas.get(c).get() / total;
			if(p != 0){
				value += p*Math.log(p);
			}
		}
		
		if(value != 0.0){
			value *= -1.0;
		}
		
		if(Double.isNaN(value)){
			value = Raster.getNoDataValue();
		}
		//System.out.println("shdi = "+value);
	}

}
