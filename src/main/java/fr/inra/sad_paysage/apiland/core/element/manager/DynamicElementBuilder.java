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

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

import fr.inra.sad_paysage.apiland.core.composition.Attribute;
import fr.inra.sad_paysage.apiland.core.composition.AttributeType;
import fr.inra.sad_paysage.apiland.core.composition.Composition;
import fr.inra.sad_paysage.apiland.core.element.DynamicElement;
import fr.inra.sad_paysage.apiland.core.element.type.DynamicElementType;
import fr.inra.sad_paysage.apiland.core.space.Geometry;
import fr.inra.sad_paysage.apiland.core.structure.MultipleStructure;
import fr.inra.sad_paysage.apiland.core.structure.NullStructure;
import fr.inra.sad_paysage.apiland.core.structure.Representation;
import fr.inra.sad_paysage.apiland.core.structure.RepresentationType;
import fr.inra.sad_paysage.apiland.core.structure.SimpleStructure;
import fr.inra.sad_paysage.apiland.core.structure.Structure;
import fr.inra.sad_paysage.apiland.core.time.Time;

public class DynamicElementBuilder implements Serializable {

	private static final long serialVersionUID = 1L;

	private final DynamicElementType type;
	
	private String id; 
	
	private Time time;
	
	private Composition composition;
	
	private Structure structure;
	
	public DynamicElementBuilder(DynamicElementType type){
		this.type = type;
		reset();
	}
	
	protected Time getTime(){
		return time;
	}
	
	protected DynamicElementType getType() {
		return type;
	}

	protected Composition getComposition() {
		return composition;
	}

	protected Structure getStructure() {
		return structure;
	}

	protected String getId(){
		return id;
	}
	
	public void display(){
		for(Attribute a : composition){
			a.display();
		}
		for(Representation r : structure){
			r.display();
		}
	}
	
	protected void reset(){
		id = null;
		time = null;
		composition = new Composition();
		for(AttributeType t : type.getAttributeTypes()){
			composition.addAttribute(DynamicElementFactory.createAttribute(t));
		}
		if(type.getRepresentationTypes().size() == 1){
			structure = new SimpleStructure();
			structure.addRepresentation(DynamicElementFactory.createRepresentation(type.getRepresentationTypes().get(0)));
		}else if(type.getRepresentationTypes().size() > 1){
			structure = new MultipleStructure();
			for(RepresentationType t : type.getRepresentationTypes()){
				structure.addRepresentation(DynamicElementFactory.createRepresentation(t));
			}
		}else{
			structure = new NullStructure();
		}
	}
	
	public void setId(String id){
		this.id = id;
	}
	
	public void setTime(Time time){
		this.time = time;
	}
	
	public void setComposition(Composition composition){
		this.composition = composition;
	}
	
	public void setStructure(Structure structure){
		this.structure = structure;
	}
	
	public void setAttribute(String name, Time t, Serializable o){
		if(o != null && !(o.getClass().equals(String.class) && ((String) o).equals(""))){
			if(type.getAttributeType(name).getBinding().equals(o.getClass())){
				composition.getAttribute(name).setValue(t, o);
			}else{
				try {
					Serializable ob = (Serializable) type.getAttributeType(name).getBinding().getConstructor(o.getClass()).newInstance(o);
					composition.getAttribute(name).setValue(t, ob);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void setAttribute(Attribute attribute){
		composition.addAttribute(attribute);
	}
		
	public void setRepresentation(String name, Time t, Geometry g){
		structure.getRepresentation(name).setGeometry(t, g);
	}
	
	public void setRepresentation(String name, Time t){
		structure.getRepresentation(name).setTime(t);
	}
	
	public void setRepresentation(Representation representation){
		structure.addRepresentation(representation);
	}

	private boolean isValid(){
		if(structure != null
				&& composition != null){
			return true;
		}
		System.err.println("element parameter must not be empty !!!");
		return false;
	}
	
	public <E extends DynamicElement> E build(){
		if(isValid()){
			E element = DynamicElementFactory.createDynamicElement(getType().getBinding(),getType(),getId(),getTime(),getComposition(),getStructure()); 
			reset();
			return element;
		}else{
			throw new IllegalArgumentException("parameters are not initialized !!!");
		}
	}

}
