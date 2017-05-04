package fr.inra.sad_paysage.agriconnect.model.metric;

import java.util.Set;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import fr.inra.sad_paysage.apiland.analysis.VariableManager;
import fr.inra.sad_paysage.apiland.analysis.vector.metric.VectorMetric;
import fr.inra.sad_paysage.apiland.core.composition.Composition;
import fr.inra.sad_paysage.apiland.core.time.Instant;
import fr.inra.sad_paysage.apiland.capfarm.model.Cover;

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
