package fr.inra.sad.bagap.apiland.simul.model;

import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class ModelIterator<M extends Model> implements Iterator<M> {

	private Iterator<String> iterator;
	
	private Map<String, M> models;
	
	public ModelIterator(List<String> order, Map<String, M> models){
		iterator = order.iterator();
		this.models = models;
	}
	
	@Override
	public boolean hasNext() {
		return iterator.hasNext();
	}

	@Override
	public M next() {
		return models.get(iterator.next());
	}

	@Override
	public void remove() {
		throw new IllegalArgumentException();
	}

}
