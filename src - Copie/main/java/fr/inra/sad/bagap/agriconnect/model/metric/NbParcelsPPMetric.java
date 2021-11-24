package fr.inra.sad.bagap.agriconnect.model.metric;

import java.util.HashSet;
import java.util.Set;

import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

import fr.inra.sad.bagap.apiland.analysis.VariableManager;
import fr.inra.sad.bagap.apiland.analysis.vector.metric.VectorMetric;
import fr.inra.sad.bagap.apiland.capfarm.model.CoverUnit;
import fr.inra.sad.bagap.apiland.core.composition.Composition;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.core.time.Instant;

public class NbParcelsPPMetric extends VectorMetric {
	
	Set<String> sau;
	
	public NbParcelsPPMetric() {
		super(VariableManager.get("NB_PP"));
		sau = new HashSet<String>();
		sau.add("B");   // blé
		sau.add("BE");  // bande enherbée
		sau.add("C");   // colza
		sau.add("M");   // mais
		sau.add("O");   // orge
		sau.add("P");  // prairie
		sau.add("PP");  // prairie permanente
	} 

	@Override
	public void doCalculate(Point point, Set<Polygon> polygons, Instant t) {
		value = 0.0;
		
		for(Polygon p : polygons){
			if(((Composition) p.getUserData()).getAttribute("cover").isActive(t) 
					&& ((Composition) p.getUserData()).getAttribute("area") != null
					&& ((Double) ((Composition) p.getUserData()).getAttribute("area").getValue(t)) < 5000){
				String c = ((CoverUnit) ((Composition) p.getUserData()).getAttribute("cover").getValue(t)).getCode();
				for(String s : sau){
					if(s.equalsIgnoreCase(c)){
						value++;
						break;
					}
				}	
			}
		}
		
		if(Double.isNaN(value)){
			value = Raster.getNoDataValue();
		}
		
		//System.out.println(t.year()+" connectivity = "+value);
	}

}

