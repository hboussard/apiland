package fr.inra.sad_paysage.apiland.analysis.vector.metric;

import java.util.Set;

import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

import fr.inra.sad_paysage.apiland.analysis.VariableManager;
import fr.inra.sad_paysage.apiland.core.composition.Composition;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.Raster;
import fr.inra.sad_paysage.apiland.core.time.Instant;

public class AreaMetric extends VectorMetric {

	private Object code;
	
	public AreaMetric(Object code) {
		super(VariableManager.get("A_"+code));
		this.code = code;
	} 

	@Override
	public void doCalculate(Point point, Set<Polygon> polygons, Instant t) {
		value = 0.0;
		//System.out.println(polygons.size());
		for(Polygon p : polygons){
			if(((Composition) p.getUserData()).getAttribute("cover").isActive(t)){
				if(((Composition) p.getUserData()).getAttribute("cover").getValue(t).equals(code)){
					value += p.getArea();
				}
			}
		}
		
		if(Double.isNaN(value)){
			value = Raster.getNoDataValue();
		}
	}

}
