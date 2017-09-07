package fr.inra.sad.bagap.apiland.analysis.vector.metric;

import java.util.HashSet;
import java.util.Set;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

import fr.inra.sad.bagap.apiland.analysis.VariableManager;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.core.time.Instant;

public class TotalEdgeLengthMetric extends VectorMetric {
	
	public TotalEdgeLengthMetric() {
		super(VariableManager.get("TE"));
	}

	@Override
	public void doCalculate(Point point, Set<Polygon> polygons, Instant t) {
		Set<Polygon> buffers = new HashSet<Polygon>();
		Polygon buffer;
		for(Polygon p : polygons){
			buffer = (Polygon) p.buffer(0.5);
			buffer.setUserData(p.getUserData());
			buffers.add(buffer); 
		}
		
		Geometry i;
		value = 0.0;
		
		Set<Polygon> ever = new HashSet<Polygon>();
		for(Polygon p1 : buffers){
			ever.add(p1);
			for(Polygon p2 : buffers){
				if(!ever.contains(p2)){
					if(p1.intersects(p2)){
						i = p1.intersection(p2);
						if(i instanceof Polygon || i instanceof MultiPolygon){
							value += i.getLength()/2;
						}else{
							System.out.println(i.getClass());
						}
					}
				}
			}
		}
		
		if(Double.isNaN(value)){
			value = Raster.getNoDataValue();
		}
	} 
	
}
