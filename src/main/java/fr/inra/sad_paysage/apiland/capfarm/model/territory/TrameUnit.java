package fr.inra.sad_paysage.apiland.capfarm.model.territory;

import fr.inra.sad_paysage.apiland.capfarm.CAPFarm;
import fr.inra.sad_paysage.apiland.core.element.DefaultDynamicFeature;
import fr.inra.sad_paysage.apiland.core.element.type.DynamicFeatureType;
import fr.inra.sad_paysage.apiland.core.time.Instant;

public class TrameUnit extends DefaultDynamicFeature {

	private static final long serialVersionUID = 1L;

	public TrameUnit(DynamicFeatureType type) {
		super(type);
	}

	public double getArea(){
		return getArea(CAPFarm.t);
	}
}
