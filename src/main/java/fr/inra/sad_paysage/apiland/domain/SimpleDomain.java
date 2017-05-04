package fr.inra.sad_paysage.apiland.domain;

public abstract class SimpleDomain<D, E> implements Domain<D, E>{
	
	private D value;
	
	protected Domain<D, E> inverse;
	
	public SimpleDomain(D value){
		this.value = value;
	}
	
	public SimpleDomain(D value, Domain<D, E> inverse){
		this.value = value;
		this.inverse = inverse;
	}
	
	@Override
	public int size() {
		throw new UnsupportedOperationException();
	}
	
	public D value(){
		return value;
	}

	@Override
	public D minimum(){
		throw new UnsupportedOperationException();
	}
	
	@Override
	public D maximum(){
		throw new UnsupportedOperationException();
	}

}
