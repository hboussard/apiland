package fr.inra.sad_paysage.apiland.core.element.decorator;

import fr.inra.sad_paysage.apiland.core.element.DynamicElement;

public interface DynamicElementDecorator<E extends DynamicElement> extends DynamicElement {

	E getDecorateElement();
	
}
