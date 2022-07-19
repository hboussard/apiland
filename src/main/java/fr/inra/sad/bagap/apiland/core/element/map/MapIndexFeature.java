package fr.inra.sad.bagap.apiland.core.element.map;

import fr.inra.sad.bagap.apiland.core.element.DefaultDynamicFeature;
import fr.inra.sad.bagap.apiland.core.element.DynamicElement;
import fr.inra.sad.bagap.apiland.core.time.Instant;

public class MapIndexFeature extends DefaultDynamicFeature {

	private static final long serialVersionUID = 1L;

	private DynamicElement element1, element2;
	
	public MapIndexFeature(MapIndexFeatureType type, DynamicElement e1, DynamicElement e2) {
		super(type);
		this.element1 = e1;
		this.element2 = e2;
	}
	
	@Override
	public MapIndexFeature clone(){
		MapIndexFeature clone = (MapIndexFeature)super.clone();
		clone.element1 = this.element1;
		clone.element2 = this.element2;
		return clone;
	}
	
	public void display(Instant t){
		System.out.println(element1+" <--> "+element2);
	}

	public DynamicElement getOtherElement(DynamicElement e){
		if(element1.equals(e)){
			return element2;
		}
		if(element2.equals(e)){
			return element1;
		}
		throw new IllegalArgumentException();
	}
	
	public DynamicElement getElement1(){
		return element1;
	}
	
	public DynamicElement getElement2(){
		return element2;
	}
	
	
}
