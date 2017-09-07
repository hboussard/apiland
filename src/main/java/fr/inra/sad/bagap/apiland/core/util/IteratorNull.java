package fr.inra.sad.bagap.apiland.core.util;

import java.util.Iterator;

public class IteratorNull implements Iterator {

	@Override
	public boolean hasNext() {
		return false;
	}

	@Override
	public Object next() {
		return null;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

}
