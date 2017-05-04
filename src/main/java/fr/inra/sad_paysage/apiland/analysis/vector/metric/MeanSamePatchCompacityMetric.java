package fr.inra.sad_paysage.apiland.analysis.vector.metric;

import java.util.HashSet;
import java.util.Set;

import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

import fr.inra.sad_paysage.apiland.analysis.Stats;
import fr.inra.sad_paysage.apiland.analysis.VariableManager;
import fr.inra.sad_paysage.apiland.core.composition.Composition;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.Raster;
import fr.inra.sad_paysage.apiland.core.time.Instant;

public class MeanSamePatchCompacityMetric extends VectorMetric {
	
	private String attribute;
	
	public MeanSamePatchCompacityMetric(String attribute) {
		super(VariableManager.get("MSKG"));
		this.attribute = attribute;
	}

	@Override
	public void doCalculate(Point point, Set<Polygon> polygons, Instant t) {
		
		Set<Polygon> patches = new HashSet<Polygon>();
		patches.addAll(polygons);
		while(getPatches(patches, t));
		
		Stats stats = new Stats();
		for(Polygon p : patches){
			//System.out.println(p.getArea());
			stats.add(0.28*p.getLength()/Math.sqrt(p.getArea()));
		}
		
		stats.calculate();
		
		value = stats.getAverage();
		
		if(Double.isNaN(value)){
			value = Raster.getNoDataValue();
		}
	} 
	
	private boolean getPatches(Set<Polygon> patches, Instant t){
		boolean ok = false, thisok;
		Set<Polygon> temp = new HashSet<Polygon>();
		Set<Polygon> ever = new HashSet<Polygon>();
		Object o1, o2;
		for(Polygon p1 : patches){
			if(!ever.contains(p1)){
				thisok = false;
				ever.add(p1);
				o1 = ((Composition) p1.getUserData()).getAttribute(attribute).getValue(t);
				for(Polygon p2 : patches){
					o2 = ((Composition) p2.getUserData()).getAttribute(attribute).getValue(t);
					if(!ever.contains(p2) && o1.equals(o2) && p1.intersects(p2)){
						ok = true;
						thisok = true;
						ever.add(p2);
						Polygon p3 = (Polygon) p1.union(p2);
						p3.setUserData(p1.getUserData());
						temp.add(p3);
					}
				}
				if(!thisok){
					temp.add(p1);
				}
			}
		}
		
		if(ok){
			patches.clear();
			patches.addAll(temp);
		}
		
		return ok;
	}
	
}
