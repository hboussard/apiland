package fr.inra.sad.bagap.apiland.analysis.vector.metric;

import java.util.Set;

import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

import fr.inra.sad.bagap.apiland.analysis.VariableManager;
import fr.inra.sad.bagap.apiland.core.composition.Composition;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.core.time.Instant;

public class DispersionMetric extends VectorMetric {

	private Object code;
	
	public DispersionMetric(Object code) {
		super(VariableManager.get("D_"+code));
		this.code = code;
	} 

	@Override
	public void doCalculate(Point point, Set<Polygon> polygons, Instant t) {
		value = 0.0;
		//Map<Coordinate, Double> parcels = new HashMap<Coordinate, Double>();
		double xc = 0.0;
		double yc = 0.0;
		double poids = 0.0;
		int nb = 0;
		for(Polygon p : polygons){
			if(((Composition) p.getUserData()).getAttribute("cover").isActive(t)
					&& ((Composition) p.getUserData()).getAttribute("cover").getValue(t).equals(code)){
				//parcels.put(p.getCentroid().getCoordinate(), p.getArea());
				xc += p.getCentroid().getCoordinate().x * p.getArea();
				yc += p.getCentroid().getCoordinate().y * p.getArea();
				poids += p.getArea();
				nb++;
			}
		}
		
		if(nb > 1){
			xc = xc / poids;
			yc = yc / poids;
			double x, y, d;
			for(Polygon p : polygons){
				if(((Composition) p.getUserData()).getAttribute("cover").isActive(t)
						&& ((Composition) p.getUserData()).getAttribute("cover").getValue(t).equals(code)){
			
					x = p.getCentroid().getCoordinate().x;
					y = p.getCentroid().getCoordinate().y;
					
					d = Math.sqrt(Math.pow(x-xc, 2) + Math.pow(y-yc, 2));
					
					value += Math.pow(d, 2) / (nb - 1);
				}
			}
			value = Math.sqrt(value);
		}
		
		if(Double.isNaN(value) || value == 0.0){
			value = Raster.getNoDataValue();
		}
		
		//System.out.println("valeur de dispersion agronomique du "+code+" en "+t.year()+" est de "+value);
	}

}
