package fr.inra.sad.bagap.agriconnect.model.metric;

import java.util.Set;

import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

import fr.inra.sad.bagap.apiland.analysis.VariableManager;
import fr.inra.sad.bagap.apiland.analysis.vector.metric.VectorMetric;
import fr.inra.sad.bagap.apiland.capfarm.model.Cover;
import fr.inra.sad.bagap.apiland.capfarm.model.CoverGroup;
import fr.inra.sad.bagap.apiland.capfarm.model.CoverManager;
import fr.inra.sad.bagap.apiland.core.composition.Composition;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.core.time.Instant;

public class HanskiConnectivityMetric extends VectorMetric {

	private Object code;
	
	private int index = 0;
	
	public HanskiConnectivityMetric(Object code) {
		super(VariableManager.get("HC_"+code));
		this.code = code;
	} 

	@Override
	public void doCalculate(Point point, Set<Polygon> polygons, Instant t) {
		value = 0.0;
		int nb = 0;
		if(code instanceof CoverGroup){
			for(Polygon p1 : polygons){
				if(((Composition) p1.getUserData()).getAttribute("cover").isActive(t) 
						&& ((CoverGroup) code).contains((Cover) ((Composition) p1.getUserData()).getAttribute("cover").getValue(t))){
					nb++;
					for(Polygon p2 : polygons){
						if(!p1.equals(p2) 
								&& ((Composition) p2.getUserData()).getAttribute("cover").isActive(t) 
								&& ((CoverGroup) code).contains((Cover) ((Composition) p2.getUserData()).getAttribute("cover").getValue(t))){
							//System.out.println("vecteur : distance (en km) = "+(p1.distance(p2)/1000.0));
							//System.out.println("vecteur : aire (en hectare) = "+(p2.getArea()/10000.0));
							
							value += Math.exp(-(p1.distance(p2)/1000.0)) * (p2.getArea()/10000.0);
						}
					}
				}
			}
			value /= nb;
			//System.out.println("vecteur "+index++);
			System.out.println("vecteur "+(index++)+": value = "+value+" ("+nb+")");
		}else{
			for(Polygon p1 : polygons){
				if(((Composition) p1.getUserData()).getAttribute("cover").isActive(t) 
						&& ((Composition) p1.getUserData()).getAttribute("cover").getValue(t).equals(code)){
					nb++;
					for(Polygon p2 : polygons){
						if(!p1.equals(p2) 
								&& ((Composition) p2.getUserData()).getAttribute("cover").isActive(t) 
								&& ((Composition) p2.getUserData()).getAttribute("cover").getValue(t).equals(code)){
							//System.out.println("vecteur : distance (en km) = "+(p1.distance(p2)/1000.0));
							//System.out.println("vecteur : aire (en hectare) = "+(p2.getArea()/10000.0));
							
							value += Math.exp(-(p1.distance(p2)/1000.0)) * (p2.getArea()/10000.0);
						}
					}
				}
			}
			value /= nb;
			System.out.println("vecteur : value = "+value+" ("+nb+")");
		}
		
		if(Double.isNaN(value)){
			value = Raster.getNoDataValue();
		}
		
		//System.out.println(t.year()+" connectivity = "+value);
	}

}
