/*Copyright 2010 by INRA SAD-Paysage (Rennes)

Author: Hugues BOUSSARD 
email : hugues.boussard@rennes.inra.fr

This library is a JAVA toolbox made to create and manage dynamic landscapes.

This software is governed by the CeCILL-C license under French law and
abiding by the rules of distribution of free software.  You can  use,
modify and/ or redistribute the software under the terms of the CeCILL-C
license as circulated by CEA, CNRS and INRIA at the following URL
"http://www.cecill.info".

As a counterpart to the access to the source code and rights to copy,
modify and redistribute granted by the license, users are provided only
with a limited warranty  and the software's author,  the holder of the
economic rights,  and the successive licensors  have only  limited
liability.

In this respect, the user's attention is drawn to the risks associated
with loading,  using,  modifying and/or developing or reproducing the
software by the user in light of its specific status of free software,
that may mean  that it is complicated to manipulate, and that also
therefore means  that it is reserved for developers and experienced
professionals having in-depth computer knowledge. Users are therefore
encouraged to load and test the software's suitability as regards their
requirements in conditions enabling the security of their systems and/or
data to be ensured and,  more generally, to use and operate it in the
same conditions as regards security.

The fact that you are presently reading this means that you have had
knowledge of the CeCILL-C license and that you accept its terms.
*/
package fr.inra.sad_paysage.apiland.core.element.decorator;

import fr.inra.sad_paysage.apiland.core.change.Changeable;
import fr.inra.sad_paysage.apiland.core.change.ChangeableObject;
import fr.inra.sad_paysage.apiland.core.change.ChangeableObserver;
import fr.inra.sad_paysage.apiland.core.composition.Attribute;
import fr.inra.sad_paysage.apiland.core.composition.Composition;
import fr.inra.sad_paysage.apiland.core.element.DynamicElement;
import fr.inra.sad_paysage.apiland.core.element.type.DynamicElementType;
import fr.inra.sad_paysage.apiland.core.structure.Structure;
import fr.inra.sad_paysage.apiland.core.time.Instant;
import fr.inra.sad_paysage.apiland.core.time.Time;

public abstract class CompositionDecorator<E extends DynamicElement> extends AbstractDynamicElementDecorator<E> {

	private static final long serialVersionUID = 1L;
	
	protected Composition composition;
	
	private ChangeableObject changeable;
	
	private Time time;

	public CompositionDecorator(E element, DynamicElementType type){
		super(element, type);
		this.composition = new Composition();
		changeable = new ChangeableObject();
	}
	
	@Override
	public CompositionDecorator<E> clone(){
		CompositionDecorator<E> clone = (CompositionDecorator<E>)super.clone();
		clone.changeable = new ChangeableObject();
		clone.composition = composition.clone();
		clone.decorate = decorate;
		clone.time = time;
		return clone;	
	}

	@Override
	public void setStructure(Structure structure) {
		throw new UnsupportedOperationException("set no structure to the composition decorator");
	}
	
	@Override
	public void setComposition(Composition composition) {
		this.composition = composition;
	}
	
	@Override
	public Attribute<?> getAttribute(String name) {
		if(decorate.hasAttribute(name)){
			return decorate.getAttribute(name);
		}
		if(composition.hasAttribute(name)){
			return composition.getAttribute(name);
		}
		return getComposition().getAttribute(name);
	}

	@Override
	public boolean hasAttribute(String name) {
		if(decorate.hasAttribute(name)){
			return true;
		}
		if(composition.hasAttribute(name)){
			return true;
		}
		return getComposition().hasAttribute(name);
	}
	
	@Override
	public void addObserver(ChangeableObserver o) {
		changeable.addObserver(o);
		decorate.addObserver(o);
		getComposition().addObserver(o);
		getStructure().addObserver(o);
	}

	@Override
	public void removeObserver(ChangeableObserver o) {
		changeable.removeObserver(o);
		getComposition().removeObserver(o);
		getStructure().removeObserver(o);
	}

	@Override
	public void notifyObservers(Instant t, Changeable c, Object o) {
		changeable.notifyObservers(t, c, o);
	}
	
	@Override
	public Instant getLastChange(){		
		Instant t = decorate.getLastChange();
		Instant n = getComposition().getLastChange();
		if(t == null || (n != null && n.isAfter(t))){
			t = n;
		}
		return t;
	}
	
	@Override
	public void delete(){
		super.delete();
		composition.delete();
		composition = null;
		changeable.delete();
		changeable = null;
		time = null;
	}
	
	
}
