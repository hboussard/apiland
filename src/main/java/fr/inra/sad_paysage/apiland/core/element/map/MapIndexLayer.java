package fr.inra.sad_paysage.apiland.core.element.map;

import fr.inra.sad_paysage.apiland.core.element.DefaultDynamicLayer;
import fr.inra.sad_paysage.apiland.core.element.DynamicLayer;
import fr.inra.sad_paysage.apiland.core.element.type.DynamicLayerType;

public class MapIndexLayer<F extends MapIndexFeature> extends DefaultDynamicLayer<F> {

	private static final long serialVersionUID = 1L;
	
	private DynamicLayer<?> layer1;
	
	private DynamicLayer<?> layer2;
	
	public MapIndexLayer(DynamicLayerType type, DynamicLayer<?> l1, DynamicLayer<?> l2) {
		super(type);
		this.layer1 = l1;
		this.layer2 = l2;
	}

	@Override
	public MapIndexLayer<F> clone(){
		MapIndexLayer<F> clone = (MapIndexLayer<F>)super.clone();
		return clone;
	}
	
}
