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
package fr.inra.sad_paysage.apiland.core.element.manager;

import java.lang.reflect.InvocationTargetException;
import java.io.Serializable;
import fr.inra.sad_paysage.apiland.core.composition.Attribute;
import fr.inra.sad_paysage.apiland.core.composition.AttributeType;
import fr.inra.sad_paysage.apiland.core.composition.Composition;
import fr.inra.sad_paysage.apiland.core.composition.DiscreteAttribute;
import fr.inra.sad_paysage.apiland.core.composition.DiscreteAttributeType;
import fr.inra.sad_paysage.apiland.core.composition.DynamicAttribute;
import fr.inra.sad_paysage.apiland.core.composition.DynamicAttributeType;
import fr.inra.sad_paysage.apiland.core.composition.ExternalMethodAttribute;
import fr.inra.sad_paysage.apiland.core.composition.ExternalMethodAttributeType;
import fr.inra.sad_paysage.apiland.core.composition.StaticAttribute;
import fr.inra.sad_paysage.apiland.core.element.DynamicElement;
import fr.inra.sad_paysage.apiland.core.element.type.DynamicElementType;
import fr.inra.sad_paysage.apiland.core.space.Geometry;
import fr.inra.sad_paysage.apiland.core.structure.DynamicRepresentation;
import fr.inra.sad_paysage.apiland.core.structure.DynamicRepresentationType;
import fr.inra.sad_paysage.apiland.core.structure.Representation;
import fr.inra.sad_paysage.apiland.core.structure.RepresentationType;
import fr.inra.sad_paysage.apiland.core.structure.StaticRepresentation;
import fr.inra.sad_paysage.apiland.core.structure.Structure;
import fr.inra.sad_paysage.apiland.core.time.Time;

public class DynamicElementFactory {

	public static <T extends Time, O extends Serializable> Attribute<O> createAttribute(AttributeType type){
		Attribute<O> a = null;
		if(type instanceof ExternalMethodAttributeType){
			a = new ExternalMethodAttribute((ExternalMethodAttributeType)type);
		}else if(type instanceof DiscreteAttributeType){
			a = new DiscreteAttribute<O>((DiscreteAttributeType)type);
		}else if(type instanceof DynamicAttributeType){
			a = new DynamicAttribute<O>((DynamicAttributeType)type);
		}else{
			a = new StaticAttribute<O>(type);
		}
		//a.addObserver(type);
		return a;
	}
	
	public static <T extends Time, G extends Geometry> Representation<G> createRepresentation(RepresentationType type){
		Representation<G> r = null;
		if(type instanceof DynamicRepresentationType){
			r = new DynamicRepresentation((DynamicRepresentationType)type);
		}else{
			r = new StaticRepresentation(type);
		}
		//r.addObserver(type);
		return r;
	}
	
	public static <E extends DynamicElement> E createDynamicElement(Class<? extends DynamicElement> binding,
			DynamicElementType type, String id, Time time, Composition composition, Structure structure){
		E element = null;
		try {
			element = (E) binding.getConstructor(type.getClass()).newInstance(type);
			element.setId(id);
			element.setComposition(composition);
			element.setStructure(structure);
			element.setTime(time);
				
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		
		return element;
	}
	
	/*
	public static DynamicElement createDynamicElement(Class<? extends DynamicElement> binding, DynamicElementType type){
		return createDynamicElement(binding, type, new Composition(), new Structure());
	}
	*/
	

	
}
