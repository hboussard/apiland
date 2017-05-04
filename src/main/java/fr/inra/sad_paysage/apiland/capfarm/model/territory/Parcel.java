package fr.inra.sad_paysage.apiland.capfarm.model.territory;

import com.vividsolutions.jts.geom.Geometry;

import fr.inra.sad_paysage.apiland.capfarm.CAPFarm;
import fr.inra.sad_paysage.apiland.core.element.type.DynamicFeatureType;

public class Parcel extends FarmUnit {

	private static final long serialVersionUID = 1L;
	
	public Parcel(DynamicFeatureType type) {
		super(type);
	}
	
	@Override
	public String toString(){
		return getId();
	}

	public Geometry getShape(){
		return getGeometry(CAPFarm.t).get().getJTS();
	}
	
	public int getArea(){
		return (int) getShape().getArea();
	}

	public void clearAttributes() {
		getAttribute("cover").clear();
	}
	
	public int getDistance(Parcel p2){
		return (int) getShape().distance(p2.getShape());
	}
	
	public int getPGP(){
		if(getAttribute("pgp") != null){
			return (int) getAttribute("pgp").getValue(CAPFarm.t);
		}
		return -1;
	}
}
