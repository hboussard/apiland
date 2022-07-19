package fr.inra.sad.bagap.apiland.core.element.iterator;

import java.util.Iterator;
import java.util.Stack;

import fr.inra.sad.bagap.apiland.core.element.DynamicLayer;

public class ElementsIterator<DynamicElement> implements Iterator<DynamicElement>{

	private Stack<Iterator<DynamicElement>> pile = new Stack<Iterator<DynamicElement>>();
	
	public ElementsIterator(Iterator<DynamicElement> iterator){
		pile.push(iterator);
	}
	
	@Override
	public DynamicElement next() {
		if(hasNext()){
			Iterator<DynamicElement> ite = (Iterator<DynamicElement>) pile.peek();
			DynamicElement e = ite.next();
			if(e instanceof DynamicLayer){
				pile.push(((DynamicLayer) e).iterator());
			}
			return e;
		}
		return null;
	}
	
	@Override
	public boolean hasNext() {
		if(!pile.empty()){
			Iterator<DynamicElement> ite = (Iterator<DynamicElement>)pile.peek();
			if(!ite.hasNext()){
				pile.pop();
				return hasNext();
			}
			return true;
		}
		return false;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
	
}
