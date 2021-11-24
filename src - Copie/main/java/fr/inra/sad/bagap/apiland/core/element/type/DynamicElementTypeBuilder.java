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
import java.util.List;

import fr.inra.sad.bagap.apiland.core.composition.AttributeType;
import fr.inra.sad.bagap.apiland.core.composition.AttributeTypeBuilder;
import fr.inra.sad.bagap.apiland.core.element.DefaultDynamicFeature;
import fr.inra.sad.bagap.apiland.core.element.DynamicElement;
import fr.inra.sad.bagap.apiland.core.space.Geometry;
import fr.inra.sad.bagap.apiland.core.space.impl.GeometryImplType;
import fr.inra.sad.bagap.apiland.core.structure.RepresentationType;
import fr.inra.sad.bagap.apiland.core.structure.RepresentationTypeBuilder;
import fr.inra.sad.bagap.apiland.core.time.Time;

public class DynamicElementTypeBuilder {
	
	private Class<? extends DynamicElementType> binding;
	
	private Class<? extends DynamicElement> elementBinding;

	private String idName;
	
	private AttributeTypeBuilder attributeBuilder;
	
	private RepresentationTypeBuilder representationBuilder;
	
	private List<AttributeType> attributeTypes;
	
	private List<RepresentationType> representationTypes;
	
	public DynamicElementTypeBuilder(Class<? extends DynamicElementType> binding, Class<? extends DynamicElement> elementBinding){
		this.binding = binding;
		this.elementBinding = elementBinding;
		attributeBuilder = new AttributeTypeBuilder();
		representationBuilder = new RepresentationTypeBuilder();
		reset();
	}
	
	public DynamicElementTypeBuilder(Class<? extends DynamicElement> elementBinding){
		this(DynamicElementType.class,elementBinding);
	}
	
	public DynamicElementTypeBuilder(){
		this(DynamicFeatureType.class,DefaultDynamicFeature.class);
	}
	
	public void reset(){
		attributeTypes = new ArrayList<AttributeType>();
		representationTypes = new ArrayList<RepresentationType>();
	}
	
	public void setIdName(String idName){
		this.idName = idName;
	}
	
	public void addAttributeType(String name, Class<? extends Time> temporal, Class<?> binding){
		attributeBuilder.setName(name);
		attributeBuilder.setTemporal(temporal);
		attributeBuilder.setBinding(binding);
		attributeTypes.add(attributeBuilder.build());
	}
	
	public void addAttributeType(AttributeType attributeType){
		attributeTypes.add(attributeType);
	}
	
	public void addRepresentationType(String name, Class<? extends Time> temporal, Class<? extends Geometry> spatial, GeometryImplType geometryType){
		representationBuilder.setName(name);
		representationBuilder.setTemporal(temporal);
		representationBuilder.setSpatial(spatial);
		representationBuilder.setGeometryType(geometryType);
		representationTypes.add(representationBuilder.build());
	}
	
	public void addRepresentationType(RepresentationType representationType){
		representationTypes.add(representationType);
	}
	
	public DynamicElementType build(){
		DynamicElementType type = DynamicElementTypeFactory
		.createDynamicElementType(idName,attributeTypes, representationTypes, binding, elementBinding);
		reset();
		return type;
	}
	
	
	
}