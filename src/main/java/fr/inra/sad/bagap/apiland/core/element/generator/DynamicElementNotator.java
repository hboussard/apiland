package fr.inra.sad.bagap.apiland.core.element.generator;

import fr.inra.sad.bagap.apiland.core.element.DynamicElement;

/**
 * noteur de paysage selon une fonction fitness
 * @author H.Boussard
 * @param <E> le type de paysage
 */
public abstract class DynamicElementNotator<E extends DynamicElement> {

	private GeneticDynamicElementGenerator<E> generator;
	
	public void setGenerator(GeneticDynamicElementGenerator<E> g){
		this.generator = g;
	}
	
	public GeneticDynamicElementGenerator<E> getGenerator(){
		return generator;
	}
	
	public abstract double notation(E element);
	
	public abstract double best();
	
}
