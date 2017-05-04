package fr.inra.sad_paysage.apiland.domain;

public interface Domain<D, E> {

	int size();
	
	boolean accept(E e);
	
	Domain<D, E> inverse();
	
	D minimum();
	
	D maximum();

}
