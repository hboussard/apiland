package fr.inra.sad_paysage.apiland.core.dynamic;

import fr.inra.sad_paysage.apiland.core.space.Geometry;
import fr.inra.sad_paysage.apiland.core.space.Point;
import fr.inra.sad_paysage.apiland.core.time.Instant;
import fr.inra.sad_paysage.apiland.core.spacetime.SpatioTemporal;

public class SpatioDynamic<ST extends SpatioTemporal> extends Dynamic<ST> implements SpatioDynamical<ST> {

	private static final long serialVersionUID = 1L;
	
	@Override
	public double getArea(Instant t) {
		for(SpatioTemporal st : temporals){
			if(st.isActive(t)){
				return st.getArea();
			}
		}
		return 0;
	}

	@Override
	public Geometry getGeometry(Instant t) {
		for(SpatioTemporal st : temporals){
			if(st.isActive(t)){
				return st.getGeometry();
			}
		}
		return null;
	}

	@Override
	public double getLength(Instant t) {
		for(SpatioTemporal st : temporals){
			if(st.isActive(t)){
				return st.getLength();
			}
		}
		return 0;
	}

	@Override
	public boolean isActive(Instant t, Point g) {
		for(SpatioTemporal st : temporals){
			if(st.isActive(t,g)){
				return true;
			}
		}
		return false;
	}
	

}
