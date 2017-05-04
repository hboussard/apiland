package fr.inra.sad_paysage.apiland.capfarm.model.territory;

import fr.inra.sad_paysage.apiland.core.element.DefaultDynamicLayer;
import fr.inra.sad_paysage.apiland.core.element.type.DynamicLayerType;

public class Territory extends DefaultDynamicLayer<Area> {

	private static final long serialVersionUID = 1L;

	public Territory(DynamicLayerType type) {
		super(type);
	}	

	@Override
	public void display(){
		for(Area a : this){
			a.display();
		}
		System.out.println();
	}
	
	
}


