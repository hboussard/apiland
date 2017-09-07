package fr.inra.sad.bagap.apiland.capfarm.model.territory;

import fr.inra.sad.bagap.apiland.core.element.DefaultDynamicLayer;
import fr.inra.sad.bagap.apiland.core.element.type.DynamicLayerType;

public class AgriculturalArea extends DefaultDynamicLayer<FarmTerritory> implements Area {

	private static final long serialVersionUID = 1L;

	public AgriculturalArea(DynamicLayerType type) {
		super(type);
	}	

	@Override
	public void display(){
		System.out.println("trame agricole : nombre d'exploitations = "+size());
		for(FarmTerritory ft : this){
			ft.display();
		}
	}
}

