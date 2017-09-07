package fr.inra.sad.bagap.agriconnect.model.metric;

import java.util.Set;

import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

import fr.inra.sad.bagap.apiland.analysis.VariableManager;
import fr.inra.sad.bagap.apiland.analysis.vector.metric.VectorMetric;
import fr.inra.sad.bagap.apiland.capfarm.model.Cover;
import fr.inra.sad.bagap.apiland.capfarm.model.CoverGroup;
import fr.inra.sad.bagap.apiland.core.composition.Composition;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.core.time.Instant;

public class ConnectivityMetric extends VectorMetric {

	private Object code;
	
	public ConnectivityMetric(Object code) {
		super(VariableManager.get("C_"+code));
		this.code = code;
	} 

	@Override
	public void doCalculate(Point point, Set<Polygon> polygons, Instant t) {
		value = 0.0;
		int nb = 0;
		if(code instanceof CoverGroup){
			for(Polygon p : polygons){
				if(((Composition) p.getUserData()).getAttribute("cover").isActive(t) && 
						((CoverGroup) code).contains((Cover) ((Composition) p.getUserData()).getAttribute("cover").getValue(t))){
					//System.out.println("vecteur : distance (en km) = "+(point.distance(p)/1000.0));
					//System.out.println("vecteur : aire (en hectare) = "+(p.getArea()/10000.0));
					nb++;
					value += Math.exp(-(point.distance(p)/1000.0)) * (p.getArea()/10000.0);
				}
			}
			value /= nb;
		}else{
			for(Polygon p : polygons){
				if(((Composition) p.getUserData()).getAttribute("cover").isActive(t)
						&& ((Composition) p.getUserData()).getAttribute("cover").getValue(t).equals(code)){
					nb++;
					value += Math.exp(-(point.distance(p)/1000.0)) * (p.getArea()/10000.0);
				}
			}
			value /= nb;
		}
		
		if(Double.isNaN(value)){
			value = Raster.getNoDataValue();
		}
		
		//System.out.println(t.year()+" connectivity = "+value);
	}

}
