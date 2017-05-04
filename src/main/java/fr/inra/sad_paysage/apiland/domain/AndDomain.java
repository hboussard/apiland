package fr.inra.sad_paysage.apiland.domain;

/**
 * AND = Intersection de 2 sous-ensembles
 * @author sad48
 *
 * @param <D>
 * @param <E>
 */
public class AndDomain<D, E> extends ComplexDomain<D, E> {
	
	public AndDomain(Domain<D, E> dA, Domain<D, E> dB) {
		super(dA, dB);
	}
	
	public AndDomain(Domain<D, E> dA, Domain<D, E> dB, Domain<D,E> inverse) {
		super(dA, dB, inverse);
	}

	@Override
	public String toString(){
		return dA()+" AND "+dB();
	}
	
	@Override
	public boolean accept(E e) {
		return dA().accept(e) && dB().accept(e);
	}
	
	@Override
	public Domain<D, E> inverse() {
		if(inverse == null){
			inverse = new OrDomain<D, E>(dA().inverse(), dB().inverse(), this);
		}
		return inverse;
	}
	
}
