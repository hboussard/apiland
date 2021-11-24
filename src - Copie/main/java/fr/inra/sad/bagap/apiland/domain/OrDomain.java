package fr.inra.sad.bagap.apiland.domain;

/**
 * OR = Union des 2 sous-ensembles
 * @author sad48
 *
 * @param <D>
 * @param <E>
 */
public class OrDomain<D, E> extends ComplexDomain<D, E> {
	
	public OrDomain(Domain<D, E> dA, Domain<D, E> dB) {
		super(dA, dB);
	}
	
	public OrDomain(Domain<D, E> dA, Domain<D, E> dB, Domain<D,E> inverse) {
		super(dA, dB, inverse);
	}

	@Override
	public String toString(){
		return dA()+" OR "+dB();
	}
	
	@Override
	public boolean accept(E e) {
		return dA().accept(e) || dB().accept(e);
	}
	
	@Override
	public Domain<D, E> inverse() {
		if(inverse == null){
			inverse = new AndDomain<D, E>(dA().inverse(), dB().inverse(), this);
		}
		return inverse;
	}
	
}
