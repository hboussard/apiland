package fr.inra.sad_paysage.apiland.core.element.generator;

import java.util.Set;

import fr.inra.sad_paysage.apiland.core.element.DynamicElement;

/**
 * générateur de paysage selon un algorithme génétique
 * @author H.Boussard
 * @param <E> le type de paysage à générer
 */
public abstract class GeneticDynamicElementGenerator<E extends DynamicElement> {
	
	/**
	 * noteur du paysage, selon une fonction fitness
	 */
	private DynamicElementNotator<E> notator;
	
	/**
	 * sélecteur de paysage, en fonction des notes
	 */
	private DynamicElementSelector<E> selector;
	
	/**
	 * mutateur de paysage
	 */
	private DynamicElementMutator<E> mutator;
	
	private int number;
	
	/**
	 * contruteur
	 * @param notator le noteur
	 * @param selector le selecteur
	 * @param mutator le matuteur
	 */
	public GeneticDynamicElementGenerator(
			DynamicElementNotator<E> notator,
			DynamicElementSelector<E> selector, 
			DynamicElementMutator<E> mutator,
			int number){
		this.notator = notator;
		this.notator.setGenerator(this);
		this.selector = selector;
		this.selector.setGenerator(this);
		this.mutator = mutator;
		this.mutator.setGenerator(this);
		this.number = number;
	}
	
	public DynamicElementNotator<E> getNotator() {
		return notator;
	}

	public DynamicElementSelector<E> getSelector() {
		return selector;
	}

	public DynamicElementMutator<E> getMutator() {
		return mutator;
	}

	public int getNumber(){
		return number;
	}
	
	/**
	 * algorithme génétique de génération de paysage
	 * avec utilisation d'un patron de méthode
	 * @return un paysage
	 */
	public E generation(){
		
		// génération des paysages initiaux
		Set<E> set = initGeneration();
		
		// sélection des "meilleurs" paysages via le système de notation
		set = selector.selection(set);
		
		int index = 0; // index de génération
		
		// tant qu'on n'a pas un paysage satisfaisant
		while(!selector.hasSelection()){
			
			System.out.println("boucle de génération numéro "+(++index));
			
			// mutations des paysages
			set.addAll(mutator.mutation(set));
			
			// sélection des "meilleurs" paysages via le système de notation
			set = selector.selection(set);
		}
		
		// récupération du "meilleur" paysage
		return selector.getSelection();
	}
	
	/**
	 * initialisation des premiers paysages
	 * @return les premiers paysages
	 */
	protected abstract Set<E> initGeneration();
	
}
