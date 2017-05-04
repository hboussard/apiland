package fr.inra.sad_paysage.apiland.analysis.vector.metric;

import java.util.Set;

import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

import fr.inra.sad_paysage.apiland.analysis.VariableManager;
import fr.inra.sad_paysage.apiland.core.composition.Composition;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.Raster;
import fr.inra.sad_paysage.apiland.core.time.Instant;

public class ConnectivityMetricOld extends VectorMetric {

	private String attribute;
	
	private Object code;
	
	public ConnectivityMetricOld(String attribute, Object code) {
		super(VariableManager.get("C_"+code));
		this.attribute = attribute;
		this.code = code;
	} 

	@Override
	public void doCalculate(Point point, Set<Polygon> polygons, Instant t) {
		value = 0.0;
		for(Polygon p : polygons){
			if(((Composition) p.getUserData()).getAttribute(attribute).isActive(t)
					&& ((Composition) p.getUserData()).getAttribute(attribute).getValue(t).equals(code)){
				value += Math.exp(-(point.distance(p)/1000.0)) * p.getArea();
			}
		}
		
		if(Double.isNaN(value)){
			value = Raster.getNoDataValue();
		}
		
		//System.out.println(t.year()+" connectivity = "+value);
	}

}
