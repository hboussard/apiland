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

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import fr.inra.sad.bagap.apiland.core.composition.AttributeType;
import fr.inra.sad.bagap.apiland.core.composition.DynamicAttributeType;
import fr.inra.sad.bagap.apiland.core.element.DynamicElement;
import fr.inra.sad.bagap.apiland.core.space.GeometryType;
import fr.inra.sad.bagap.apiland.core.space.impl.GeometryImplType;
import fr.inra.sad.bagap.apiland.core.structure.DynamicRepresentationType;
import fr.inra.sad.bagap.apiland.core.structure.RepresentationType;
import fr.inra.sad.bagap.apiland.core.time.Time;

public class DynamicElementTypeFactory {
	
	public static DynamicElementType createDynamicElementType(String name, List<AttributeType> attributeTypes, 
			List<RepresentationType> representationTypes, Class<? extends DynamicElementType> binding,
			Class<? extends DynamicElement> elementBinding){
		DynamicElementType type = null;
		try{
			type = binding.getConstructor(String.class, Class.class).newInstance(name, elementBinding);
			type.setAttributeTypes(attributeTypes);
			type.setRepresentationTypes(representationTypes);
		}catch (InstantiationException e) {
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
		return type;
	}
	
	public static AttributeType createAttributeType(String name, Class<? extends Time> temporal, Class<?> binding){
		if(temporal == null){
			return new AttributeType(name,binding);
		}
		return new DynamicAttributeType(name,temporal,binding);
	}

	public static RepresentationType createRepresentationType(String name, Class<?extends Time> temporal, Class<? extends GeometryType> spatial, GeometryImplType geometryType){
		if(temporal == null){
			return new RepresentationType(name,spatial,geometryType);
		}
		return new DynamicRepresentationType(name,temporal,spatial,geometryType);
	}

}
