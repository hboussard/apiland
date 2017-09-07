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
package fr.inra.sad.bagap.apiland.core.element;

import fr.inra.sad.bagap.apiland.core.change.Changeable;
import fr.inra.sad.bagap.apiland.core.change.ChangeableObject;
import fr.inra.sad.bagap.apiland.core.change.ChangeableObserver;
import fr.inra.sad.bagap.apiland.core.composition.Attribute;
import fr.inra.sad.bagap.apiland.core.composition.Composition;
import fr.inra.sad.bagap.apiland.core.element.type.DynamicElementType;
import fr.inra.sad.bagap.apiland.core.structure.Representation;
import fr.inra.sad.bagap.apiland.core.structure.Structure;
import fr.inra.sad.bagap.apiland.core.time.Instant;
import fr.inra.sad.bagap.apiland.core.time.TimeException;

public abstract class AbstractDynamicElement implements DynamicElement{

	/**
	 * version number
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * the identifying
	 */
	private String id;

	/**
	 * composition
	 */
	private Composition composition;
	
	/**
	 * structure
	 */
	private Structure structure;
	
	/**
	 * the type
	 */
	private DynamicElementType type;
	
	/**
	 * the aggregate layer if exists
	 */
	private DynamicLayer<?> layer;
	
	/**
	 * gestion des changements
	 */
	private ChangeableObject changeable;
	
	/**
	 * constructor
	 * @param type : the element type
	 */
	public AbstractDynamicElement(DynamicElementType type){
		setType(type);
		changeable = new ChangeableObject();
		id = IdManager.get().getId();
	}
	
	/*
	@Override
	public int hashCode(){
		return id.hashCode();
	}
	
	@Override
	public boolean equals(Object other){
		if(other instanceof DynamicElement){
			return ((DynamicElement) other).getId().equalsIgnoreCase(id);
		}
		return false;
	}
	*/
	
	@Override
	public int compareTo(DynamicElement e){
		return this.id.compareTo(e.getId());
	}
	
	@Override
	public AbstractDynamicElement clone(){
		try{
			AbstractDynamicElement clone = (AbstractDynamicElement)super.clone();
			if(this.composition != null){
				clone.composition = this.composition.clone();
			}
			if(this.structure != null){
				clone.structure = this.structure.clone();
			}
			clone.type = this.type.clone();
			return clone;
		}catch(CloneNotSupportedException ex){
			ex.printStackTrace();
			return null;
		}
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}
	
	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getInheritedId(String idName){
		if(type.getIdName().equalsIgnoreCase(idName)){
			return id;
		}else if(getLayer() != null){
			return getLayer().getInheritedId(idName);
		}
		return "";
		//throw new IllegalArgumentException("id '"+idName+"' does not exist");
	}
	
	public Attribute<?> getInheritedAttribute(String attName){
		if(getType().hasAttributeType(attName)){
			return getAttribute(attName);
		}else if(getLayer() != null){
			return getLayer().getInheritedAttribute(attName);
		}
		throw new IllegalArgumentException("attribute '"+attName+"' does not exist");
	}

	@Override
	public void setLayer(DynamicLayer<?> layer) {
		this.layer = layer;
	}
	
	@Override
	public DynamicLayer<?> getLayer() {
		return layer;
	}

	public void setType(DynamicElementType type){
		this.type = type;
	}
	
	@Override
	public DynamicElementType getType() {
		return type;
	}

	@Override
	public void update(Instant t, Changeable c, Object o) {
		notifyObservers(t,this,o);
	}
	
	@Override
	public void addObserver(ChangeableObserver o) {
		changeable.addObserver(o);
		composition.addObserver(o);
		structure.addObserver(o);
	}

	@Override
	public void removeObserver(ChangeableObserver o) {
		changeable.removeObserver(o);
		composition.removeObserver(o);
		structure.removeObserver(o);
	}

	@Override
	public void notifyObservers(Instant t, Changeable c, Object o) {
		changeable.notifyObservers(t, c, o);
	}

	@Override
	public void kill(Instant t) throws TimeException{
		composition.kill(t);
		structure.kill(t);
	}

	@Override
	public boolean hasAttribute(String name) {
		return composition.hasAttribute(name);
	}
	
	@Override
	public Attribute<?> getAttribute(String name) {
		if(composition.hasAttribute(name)){
			return composition.getAttribute(name);
		}if(layer != null){
			return layer.getAttribute(name);
		}
		throw new IllegalArgumentException("attribute '"+name+"' does not exist");
	}

	@Override
	public Composition getComposition() {
		return composition;
	}
	
	@Override
	public Structure getStructure(){
		return structure;
	}

	@Override
	public boolean hasRepresentation(String name) {
		return structure.hasRepresentation(name);
	}

	@Override
	public void setComposition(Composition composition) {
		this.composition = composition;
		this.composition.addObserver(this);
	}
	
	@Override
	public void setStructure(Structure structure) {
		this.structure = structure;
	}
	
	@Override
	public Representation<?> getDefaultRepresentation() {
		return structure.getDefaultRepresentation();
	}

	@Override
	public Representation<?> getRepresentation(String name) {
		return structure.getRepresentation(name);
	}
	
	@Override
	public int count(Instant t, DynamicElementType... types) {
		for(DynamicElementType type : types){
			if(getType().equals(type)){
				if(isActive(t)){
					return 1;
				}else{
					return 0;
				}
			}
		}
		return 0;
	}
	
	@Override
	public Instant getLastChange(){
		if(getComposition() != null){
			return getComposition().getLastChange();
		}
		return null;
	}
	
	@Override
	public double maxX() {
		return structure.maxX();
	}

	@Override
	public double maxY() {
		return structure.maxY();
	}

	@Override
	public double minX() {
		return structure.minX();
	}

	@Override
	public double minY() {
		return structure.minY();
	}
	
	@Override
	public void delete(){
		id = null;
		if(composition != null){
			composition.delete();
			composition = null;
		}
		if(structure != null){
			structure.delete();
			structure = null;
		}
		layer = null;
		changeable.delete();
		changeable = null;
		type = null;
	}
	
}