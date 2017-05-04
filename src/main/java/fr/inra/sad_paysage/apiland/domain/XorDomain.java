package fr.inra.sad_paysage.apiland.domain;

/**
 * XOR = l'un ou l'autre mais pas les 2
 * @author sad48
 *
 * @param <D>
 * @param <E>
 */
public class XorDomain<D, E> extends ComplexDomain<D, E> {
	
	public XorDomain(Domain<D, E> dA, Domain<D, E> dB) {
		super(dA, dB);
	}
	
	public XorDomain(Domain<D, E> dA, Domain<D, E> dB, Domain<D,E> inverse) {
		super(dA, dB, inverse);
	}

	@Override
	public String toString(){
		return dA()+" XOR "+dB();
	}
	
	@Override
	public boolean accept(E e) {
		return (dA().accept(e) || dB().accept(e)) && !(dA().accept(e) && dB().accept(e));
	}

	@Override
	public Domain<D, E> inverse() {
		if(inverse == null){
			inverse = new OrDomain<D, E>(new AndDomain<D, E>(dA(), dB()), new AndDomain<D, E>(dA().inverse(), dB().inverse()), this);
		}
		return inverse;
	}
	
}