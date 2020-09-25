package fr.inra.sad.bagap.apiland.capfarm.model.territory;

import java.util.HashSet;
import java.util.Set;

import org.locationtech.jts.geom.Polygon;

import fr.inra.sad.bagap.apiland.capfarm.CAPFarm;
import fr.inra.sad.bagap.apiland.core.element.type.DynamicFeatureType;

public class Facility extends FarmUnit {

	private static final long serialVersionUID = 1L;
	
	private Set<String> facilities;
	
	public Facility(DynamicFeatureType type){
		super(type);
		facilities = new HashSet<String>();
	}
	
	public Polygon getShape(){
		return (Polygon) getGeometry(CAPFarm.t).get().getJTS();
	}
	
	public void addFacility(String m){
		facilities.add(m);
	}
	
	public Set<String> facilities(){
		return facilities;
	}
	
	public boolean containsFacility(String m){
		return facilities.contains(m);
	}

	public double getArea(){
		return getArea(CAPFarm.t);
	}
}
