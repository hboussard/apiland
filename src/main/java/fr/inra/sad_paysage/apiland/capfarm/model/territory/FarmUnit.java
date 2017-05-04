package fr.inra.sad_paysage.apiland.capfarm.model.territory;

import fr.inra.sad_paysage.apiland.core.element.DefaultDynamicFeature;
import fr.inra.sad_paysage.apiland.core.element.type.DynamicFeatureType;

public abstract class FarmUnit extends DefaultDynamicFeature {

	private static final long serialVersionUID = 1L;

	public FarmUnit(DynamicFeatureType type) {
		super(type);
	}

}
