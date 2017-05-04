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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import fr.inra.sad_paysage.apiland.core.composition.AttributeType;
import fr.inra.sad_paysage.apiland.core.element.DynamicElement;
import fr.inra.sad_paysage.apiland.core.element.DynamicLayer;
import fr.inra.sad_paysage.apiland.core.element.type.DynamicElementType;
import fr.inra.sad_paysage.apiland.core.element.type.DynamicElementTypeFactory;
import fr.inra.sad_paysage.apiland.core.element.type.DynamicLayerType;
import fr.inra.sad_paysage.apiland.core.space.Geometry;
import fr.inra.sad_paysage.apiland.core.space.GeometryType;
import fr.inra.sad_paysage.apiland.core.space.impl.GeometryImplType;
import fr.inra.sad_paysage.apiland.core.structure.MultipleStructure;
import fr.inra.sad_paysage.apiland.core.structure.NullStructure;
import fr.inra.sad_paysage.apiland.core.structure.RepresentationType;
import fr.inra.sad_paysage.apiland.core.structure.SimpleStructure;
import fr.inra.sad_paysage.apiland.core.time.Time;

public class DynamicElementManager {

	public static void save(DynamicElement element, String file) throws IOException{
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
		try{
			out.writeObject(element);
		}
		finally{
			out.flush();
			out.close();
		}
	}
	
	public static DynamicElement load(String file) throws IOException, ClassNotFoundException {
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
		DynamicElement element = (DynamicElement)in.readObject();
		in.close();
		return element;
	}
	
	public static void addAttributeToFeatures(DynamicElement element,
			String name, Class<? extends Time> temporal, Class<?> binding){
		addAttributeToFeatures(element, DynamicElementTypeFactory.createAttributeType(name,temporal,binding));
	}
	
	public static void addAttributeToFeatures(DynamicElement element, String name, Class<?> binding){
		addAttributeToFeatures(element, DynamicElementTypeFactory.createAttributeType(name,null,binding));
	}
	
	public static void addAttributeToFeatures(DynamicElement element, AttributeType type){
		if(element instanceof DynamicLayer){
			for(DynamicElement e : (DynamicLayer<DynamicElement>)element){
				addAttributeToFeatures(e,type);
			}
		}else{
			if(!element.getType().hasAttributeType(type.getName())){
				//System.out.println("ajout de l'attribut "+type.getName());
				element.getType().addAttributeType(type);
			}
			element.getComposition().addAttribute(DynamicElementFactory.createAttribute(type));
		}
	}
	
	public static boolean hasAttributeToFeatures(DynamicElement element, String name) {
		
		if(element instanceof DynamicLayer){
			for(DynamicElementType type : ((DynamicLayerType)element.getType()).getFeatureTypes()){
				//System.out.println(type.getBinding());
				//System.out.println(type.hasAttributeType(name));
			}
			for(DynamicElement e : (DynamicLayer<DynamicElement>)element){
				if(hasAttributeToFeatures(e, name)){
					return true;
				}
			}
		}else{
			if(element.getType().hasAttributeType(name)){
				return true;
			}
		}
		return false;
	}
	
	public static void removeAttribute(DynamicElement element, String name){
		if(element.getType().hasAttributeType(name)){
			element.getComposition().removeAttribute(name);
			element.getType().removeAttributeType(name);
		}else if(element instanceof DynamicLayer){
			for(DynamicElement e : (DynamicLayer<DynamicElement>)element){
				removeAttribute(e, name);
			}
		}
	}
	
	public static void removeAttributeToFeatures(DynamicElement element, String name){
		
		if(element instanceof DynamicLayer){
			for(DynamicElementType type : ((DynamicLayerType)element.getType()).getFeatureTypes()){
				type.removeAttributeType(name);
			}
			for(DynamicElement e : (DynamicLayer<DynamicElement>)element){
				removeAttributeToFeatures(e,name);
			}
		}else{
			//element.getType().removeAttributeType(name);
			element.getComposition().removeAttribute(name);
		}
	}
	
	public static void addRepresentationToFeatures(DynamicElement element,
			String name, Class<? extends Time> temporal, Class<? extends GeometryType> spatial, GeometryImplType geometryType){
		addRepresentationToFeatures(element, DynamicElementTypeFactory.createRepresentationType(name,temporal,spatial,geometryType));
	}
	
	public static void addRepresentationToFeatures(DynamicElement element, RepresentationType type){
		//System.out.println("add raster "+element);
		if(element instanceof DynamicLayer){
			for(DynamicElement e : (DynamicLayer<DynamicElement>)element){
				addRepresentationToFeatures(e,type);
			}
		}else{
			if(!element.getType().hasRepresentationType(type.getName())){
				element.getType().addRepresentationType(type);
			}
			if(element.getStructure() instanceof NullStructure){
				SimpleStructure structure = new SimpleStructure();
				element.setStructure(structure);
			}else if(element.getStructure() instanceof SimpleStructure){
				MultipleStructure structure = new MultipleStructure();
				structure.addRepresentation(element.getDefaultRepresentation());
				element.setStructure(structure);
			}
			element.getStructure().addRepresentation(DynamicElementFactory.createRepresentation(type));
		}
	}
	
	public static void removeRepresentationToFeatures(DynamicElement element, String name){
		if(element instanceof DynamicLayer){
			for(DynamicElement e : (DynamicLayer<DynamicElement>)element){
				removeRepresentationToFeatures(e,name);
			}
		}else{
			if(element.hasRepresentation(name)){
				element.getType().removeRepresentationType(name);
			}
			element.getStructure().removeRepresentation(name);
		}
	}

	
	
	
}
