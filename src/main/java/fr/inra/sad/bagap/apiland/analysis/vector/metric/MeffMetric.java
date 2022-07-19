package fr.inra.sad.bagap.apiland.analysis.vector.metric;

import java.util.Set;

import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

import fr.inra.sad.bagap.apiland.analysis.VariableManager;
import fr.inra.sad.bagap.apiland.core.composition.Composition;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.core.time.Instant;

public class MeffMetric extends VectorMetric {
	
	public MeffMetric() {
		super(VariableManager.get("Meff"));
	} 

	@Override
	public void doCalculate(Point point, Set<Polygon> polygons, Instant t) {
		value = 0.0;
		
		double surf_ter = 0.0;
		double surf_nat;
		double s_natter;
		double prod_surf;
		for(Polygon p : polygons){
			surf_nat = p.getArea();
			surf_ter += surf_nat;
			s_natter = (Double) ((Composition) p.getUserData()).getAttribute("area").getValue(t);
			prod_surf = s_natter * surf_nat;
			
			value += prod_surf;
		}
		
		value /= surf_ter;
		
		if(Double.isNaN(value)){
			value = Raster.getNoDataValue();
		}else{
			//System.out.println(point+" "+value);
		}
		
	}

}
