package fr.inra.sad.bagap.apiland.domain;

import java.util.Set;

public class SetDomain<D> implements Domain<D, D> {

	private Set<D> set;
	
	public SetDomain(Set<D> set) {
		this.set = set;
	}

	@Override
	public int size() {
		return set.size();
	}

	@Override
	public boolean accept(D e) {
		return set.contains(e);
	}
	
	public D getSingle(){
		return set.iterator().next();
	}

	@Override
	public Domain<D, D> inverse() {
		throw new UnsupportedOperationException();
	}

	@Override
	public D minimum() {
		throw new UnsupportedOperationException();
	}

	@Override
	public D maximum() {
		throw new UnsupportedOperationException();
	}

}
