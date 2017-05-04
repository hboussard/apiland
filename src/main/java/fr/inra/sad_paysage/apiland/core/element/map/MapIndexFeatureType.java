package fr.inra.sad_paysage.apiland.core.element.map;

import fr.inra.sad_paysage.apiland.core.element.type.DynamicFeatureType;

public class MapIndexFeatureType extends DynamicFeatureType{

	private static final long serialVersionUID = 1L;
	
	public MapIndexFeatureType(Class<? extends MapIndexFeature> binding) {
		super("id", binding);
	}

	@Override
	public Class<? extends MapIndexFeature> getBinding(){
		return (Class<? extends MapIndexFeature>)super.getBinding();
	}

}