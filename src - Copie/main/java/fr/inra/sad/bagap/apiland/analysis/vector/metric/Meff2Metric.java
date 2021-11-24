package fr.inra.sad.bagap.apiland.analysis.vector.metric;

import java.util.Set;

import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

import fr.inra.sad.bagap.apiland.analysis.VariableManager;
import fr.inra.sad.bagap.apiland.core.composition.Composition;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.core.time.Instant;

public class Meff2Metric extends VectorMetric {
	
	public Meff2Metric() {
		super(VariableManager.get("Meff2"));
	} 

	@Override
	public void doCalculate(Point point, Set<Polygon> polygons, Instant t) {
		value = 0.0;
		
		double surf_ter = 0.0;
		for(Polygon p : polygons){
			surf_ter += p.getArea();
		}
		
		for(Polygon p : polygons){
			double surf_nat = p.getArea();
			double s_natter = (Double) ((Composition) p.getUserData()).getAttribute("area").getValue(t);
			double prod_surf = s_natter * surf_nat;
			
			value += (surf_nat/surf_ter)*prod_surf;
		}
		
		value /= surf_ter;
		
		if(Double.isNaN(value)){
			value = Raster.getNoDataValue();
		}else{
			System.out.println(value);
		}
		
	}

}
