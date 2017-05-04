package fr.inra.sad_paysage.apiland.simul.operation;

import java.io.Serializable;
import java.util.Iterator;

import fr.inra.sad_paysage.apiland.core.element.DynamicElement;
import fr.inra.sad_paysage.apiland.core.element.DynamicFeature;
import fr.inra.sad_paysage.apiland.core.element.DynamicLayer;
import fr.inra.sad_paysage.apiland.core.time.Instant;
import fr.inra.sad_paysage.apiland.core.time.delay.Delay;
import fr.inra.sad_paysage.apiland.core.util.TransitionMatrix;

public class OpTransitionMatrix extends OpComposition{

	private static final long serialVersionUID = 1L;

	private TransitionMatrix<?> tMat;
	
	private Delay delay;

	public OpTransitionMatrix(OpTypeTransitionMatrix type) {
		super(type);
		this.tMat = type.getTransitionMatrix();
		this.delay = type.getDelay();
	}
	
	public OpTransitionMatrix clone(){
		OpTransitionMatrix clone = (OpTransitionMatrix) super.clone();
		clone.tMat = this.tMat;
		clone.delay = this.delay;
		return clone;
	}

	@Override
	public boolean make(Instant t, DynamicElement... e) {
		Instant previous = delay.previous(t);
		Iterator<DynamicFeature> ite = (Iterator<DynamicFeature>)((DynamicLayer<?>)e[0]).activeDeepIterator(t);
		DynamicElement de;
		Serializable last;
		while(ite.hasNext()){
			de = ite.next();
			last = de.getAttribute(getAttribute()).getValue(previous); // récupération de l'ancienne valeur
			de.getAttribute(getAttribute()).setValue(t, tMat.getTransition(last)); // affectation de la nouvelle
		}
		
		return true;
	}
	
	@Override
	public void delete(){
		super.delete();
		tMat = null;
		delay = null;
	}
	
}
