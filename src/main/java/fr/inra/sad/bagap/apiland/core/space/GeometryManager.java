package fr.inra.sad.bagap.apiland.core.space;

import fr.inra.sad.bagap.apiland.core.space.impl.GeometryImpl;

public class GeometryManager {

	public static GeometryImpl add(GeometryImpl g1, GeometryImpl g2){
		if(g1 == null || g2 == null){
			throw new IllegalArgumentException(g1+" or "+g2);
		}
		if(!g1.getType().equals(g2.getType())){
			throw new IllegalArgumentException(g1.getType()+" or "+g2.getType());
		}
		return g1.addGeometryImpl(g2);
	}
	
}
