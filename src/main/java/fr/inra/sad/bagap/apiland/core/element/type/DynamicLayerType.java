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
package fr.inra.sad.bagap.apiland.core.element.type;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import fr.inra.sad.bagap.apiland.core.composition.AttributeType;
import fr.inra.sad.bagap.apiland.core.element.DefaultDynamicLayer;
import fr.inra.sad.bagap.apiland.core.element.DynamicElement;
import fr.inra.sad.bagap.apiland.core.element.DynamicLayer;
import fr.inra.sad.bagap.apiland.core.structure.RepresentationType;
import fr.inra.sad.bagap.apiland.core.time.Instant;

public class DynamicLayerType extends DynamicElementType implements Iterable<DynamicElementType>{

	private static final long serialVersionUID = 1L;

	private Set<DynamicElementType> elementTypes;
	
	public DynamicLayerType(){
		super("lid",DefaultDynamicLayer.class);
		elementTypes = new HashSet<DynamicElementType>();
	}
	
	public DynamicLayerType(Class<? extends DynamicLayer> binding){
		super("lid",binding);
		elementTypes = new HashSet<DynamicElementType>();
	}
	
	public DynamicLayerType(String name){
		super(name,DefaultDynamicLayer.class);
		elementTypes = new HashSet<DynamicElementType>();
	}
	
	public DynamicLayerType(String name, Class<? extends DynamicLayer> binding){
		super(name,binding);
		elementTypes = new HashSet<DynamicElementType>();
	}
	
	public DynamicLayerType(String name,
			List<AttributeType> attributeTypes,
			List<RepresentationType> representationTypes) {
		super(name,attributeTypes, representationTypes);
		elementTypes = new HashSet<DynamicElementType>();
	}
	
	public DynamicLayerType(String idName,
			Class<? extends DynamicElement> binding,
			List<AttributeType> attributeTypes, 
			List<RepresentationType> representationTypes){
		super(idName,binding,attributeTypes,representationTypes);
		elementTypes = new HashSet<DynamicElementType>();
	}

	@Override
	public void display(){
		System.out.println(this);
		for(DynamicElementType type : elementTypes){
			type.display();
		}
	}

	public void addElementType(DynamicElementType elementType) {
		elementTypes.add(elementType);
		elementType.setLayerType(this);
	}
	
	public DynamicElementType getElementType(){
		return elementTypes.iterator().next();
	}
	
	@Override
	public DynamicElementType getDeepElementType(){
		return elementTypes.iterator().next().getDeepElementType();
	}
	
	public Set<DynamicElementType> getElementTypes(){
		return elementTypes;
	}
	
	@Override
	public boolean hasElementType(Class<? extends DynamicElement> binding){
		if(isElementType(binding)){
			return true;
		}
		for(DynamicElementType t : elementTypes){
			if(t.hasElementType(binding)){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public DynamicElementType getElementType(Class<? extends DynamicElement> binding){
		if(isElementType(binding)){
			return this;
		}
		for(DynamicElementType t : elementTypes){
			if(t.hasElementType(binding)){
				return t.getElementType(binding);
			}
		}
		throw new IllegalArgumentException();
	}

	@Override
	public List<DynamicFeatureType> getFeatureTypes(){
		List<DynamicFeatureType> types = new ArrayList<DynamicFeatureType>();
		for(DynamicElementType type : elementTypes){
			types.addAll(type.getFeatureTypes());
		}
		return types;
	}

	@Override
	public Iterator<DynamicElementType> iterator() {
		return elementTypes.iterator();
	}
	
	@Override
	public boolean containsIdName(String name){
		if(idName.equalsIgnoreCase(name)){
			return true;
		}
		for(DynamicElementType type : elementTypes){
			if(type.containsIdName(name)){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean containsAttributeByName(String name){
		if(hasAttributeType(name)){
			return true;
		}else{
			AttributeType at;
			for(DynamicElementType type : elementTypes){
				if(type.containsAttributeByName(name)){
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public int getLevelBottomUp(){
		int level = 0;
		for(DynamicElementType type : elementTypes){
			level = Math.max(level, type.getLevelBottomUp());
		}
		return level + 1;
	}

	@Override
	public Instant getLastChange() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public RepresentationType getRepresentationType(String name){
		if(hasRepresentationType(name)){
			return super.getRepresentationType(name);
		}else{
			RepresentationType rt;
			for(DynamicElementType type : elementTypes){
				rt = type.getRepresentationType(name);
				if(rt != null){
					return rt;
				}
			}
		}
		return null;
	}
	
	@Override
	public AttributeType getAttributeType(String name){
		if(hasAttributeType(name)){
			return super.getAttributeType(name);
		}else{
			AttributeType at;
			for(DynamicElementType type : elementTypes){
				at = type.getAttributeType(name);
				if(at != null){
					return at;
				}
			}
		}
		return null;
	}
	
	@Override
	public boolean hasDeepCondition(Object o){
		if(!hasCondition(o)){
			for(DynamicElementType type : elementTypes){
				if(type.hasDeepCondition(o)){
					return true;
				}
			}
			return false;
		}
		return true;
	}
	
	public boolean containsAttributeType(String name){
		if(hasAttributeType(name)){
			return true;
		}
		for(DynamicElementType type : elementTypes){
			if(type.hasAttributeType(name)){
				return true;
			}
		}
		return false;
	}
	
}
