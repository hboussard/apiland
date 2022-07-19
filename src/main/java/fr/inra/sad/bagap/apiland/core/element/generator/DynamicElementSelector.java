package fr.inra.sad.bagap.apiland.core.element.generator;

import java.util.Set;

import fr.inra.sad.bagap.apiland.core.element.DynamicElement;

/**
 * sélecteur de paysage, selon un système de notation
 * @author H.Boussard
 * @param <E> le type de paysage
 */
public abstract class DynamicElementSelector<E extends DynamicElement> {

	private GeneticDynamicElementGenerator<E> generator;
	
	public void setGenerator(GeneticDynamicElementGenerator<E> g){
		this.generator = g;
	}
	
	public GeneticDynamicElementGenerator<E> getGenerator(){
		return generator;
	}
	
	/**
	 * Y-a-t'il un "bon" paysage identifié ?
	 * @return oui si un "bon" paysage est identifié
	 */
	public abstract boolean hasSelection();
	
	/**
	 * récupère un "bon" paysage
	 * @return un "bon" paysage
	 */
	public abstract E getSelection();
	
	/**
	 * sélection d'un ensemble de "meilleurs" paysages en fonction
	 * d'un système de notation et parmi un ensemble prédéterminé
	 * @param set ensemble de paysage prédéterminés
	 * @param notator le système de notation
	 * @return les "meilleurs" paysages
	 */
	public abstract Set<E> selection(Set<E> set);
	
}
