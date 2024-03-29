package fr.inra.sad.bagap.agriconnect.model.metric;

import java.util.Set;

import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

import fr.inra.sad.bagap.apiland.analysis.VariableManager;
import fr.inra.sad.bagap.apiland.analysis.vector.metric.VectorMetric;
import fr.inra.sad.bagap.apiland.capfarm.model.Cover;
import fr.inra.sad.bagap.apiland.core.composition.Composition;
import fr.inra.sad.bagap.apiland.core.time.Instant;

public class IsCoverMetric extends VectorMetric {

	private String code;
	
	private Set<Cover> covers;
	
	public IsCoverMetric(String code, Set<Cover> covers) {
		super(VariableManager.get("is_"+code));
		this.code = code;
		this.covers = covers;
	} 

	@Override
	public void doCalculate(Point point, Set<Polygon> polygons, Instant t) {
		value = 0.0;
		
		for(Polygon p : polygons){
			if(p.intersects(point)){
				//System.out.println(((Composition) p.getUserData()).getAttribute("cover").getValue(t)+" --> "+code);
				if(covers.contains(((Composition) p.getUserData()).getAttribute("cover").getValue(t))){
					value = 1.0;
					return;
				}
			}
		}
	}

}
