package fr.inra.sad_paysage.apiland.analysis.vector.metric;

import java.util.Set;

import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

import fr.inra.sad_paysage.apiland.analysis.VariableManager;
import fr.inra.sad_paysage.apiland.core.composition.Composition;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.Raster;
import fr.inra.sad_paysage.apiland.core.time.Instant;

public class SurfaceProportionMetric extends VectorMetric {

	private Object code;
	
	public SurfaceProportionMetric(Object code) {
		super(VariableManager.get("P_"+code));
		this.code = code;
	} 

	@Override
	public void doCalculate(Point point, Set<Polygon> polygons, Instant t) {
		double area = 0.0;
		double total = 0.0;
		
		for(Polygon p : polygons){
			if(((Composition) p.getUserData()).getAttribute("cover").isActive(t)){
				total += p.getArea();
				if(((Composition) p.getUserData()).getAttribute("cover").getValue(t).equals(code)){
					area += p.getArea();
				}
			}
		}
		
		value = area / total;
		
		if(Double.isNaN(value)){
			value = Raster.getNoDataValue();
		}
	}

}
