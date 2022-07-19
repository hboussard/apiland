package fr.inra.sad.bagap.apiland.core.element.decorator;

import fr.inra.sad.bagap.apiland.core.element.DynamicElement;

public interface DynamicElementDecorator<E extends DynamicElement> extends DynamicElement {

	E getDecorateElement();
	
}
