package fr.inra.sad.bagap.apiland.core.element.generator;

import java.util.Set;

import fr.inra.sad.bagap.apiland.core.element.DynamicElement;

/**
 * le mutateur de paysages
 * @author H.Boussard
 * @param <E> le type de paysage
 */
public abstract class DynamicElementMutator<E extends DynamicElement> {

	private GeneticDynamicElementGenerator<E> generator;
	
	public void setGenerator(GeneticDynamicElementGenerator<E> g){
		this.generator = g;
	}
	
	public GeneticDynamicElementGenerator<E> getGenerator(){
		return generator;
	}
	
	/**
	 * génère un paysage muté en fonction d'un ou plusieurs paysages en entrée
	 * @param elements un ou plusieurs paysages
	 * @return un paysage muté
	 */
	protected abstract E singleMutation(E... elements);
	
	/**
	 * se charge de la mutation d'un ensemble de paysages
	 * @param set l'ensemble de paysages à faire muter
	 * @return les paysages mutés
	 */
	public abstract Set<E> mutation(Set<E> set); 
	
}
