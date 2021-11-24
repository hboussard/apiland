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
package fr.inra.sad.bagap.apiland.core.composition;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import fr.inra.sad.bagap.apiland.core.change.Changeable;
import fr.inra.sad.bagap.apiland.core.change.ChangeableObject;
import fr.inra.sad.bagap.apiland.core.change.ChangeableObserver;
import fr.inra.sad.bagap.apiland.core.time.Instant;
import fr.inra.sad.bagap.apiland.core.time.Time;
import fr.inra.sad.bagap.apiland.core.time.TimeException;

public class Composition implements Composable, Iterable<Attribute<?>>{

	private static final long serialVersionUID = 1L;

	private Map<String,Attribute<?>> attributes;
	
	private ChangeableObject changeable;
	
	public Composition(){
		attributes = new HashMap<String,Attribute<?>>();
		changeable = new ChangeableObject();
	}
	
	@Override
	public Composition clone() {
		try{
			Composition clone = (Composition)super.clone();
			clone.changeable = new ChangeableObject();
			if(attributes != null){
				clone.attributes = new HashMap<String,Attribute<?>>();
				for(Entry<String,Attribute<?>> s : this.attributes.entrySet()){
					clone.attributes.put(s.getKey(), s.getValue().clone());
				}
			}
			
			return clone;
		}catch(CloneNotSupportedException ex){
			ex.printStackTrace();
			return null;
		}
	}
	
	@Override
	public Composition getComposition(){
		return this;
	}
	
	@Override
	public void setComposition(Composition composition) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Iterator<Attribute<?>> iterator() {
		return attributes.values().iterator();
	}

	public void addAttribute(Attribute<?> d) {
		attributes.put(d.getName(), d);
		d.addObserver(this);
	}
	
	public void removeAttribute(String name){
		attributes.remove(name);
	}

	public Attribute getAttribute(String name) {
		return attributes.get(name);
	}
	
	public Time getAttributeTime(String name){
		return getAttribute(name).getTime();
	}
	
	public Object getValue(String s, Instant t){
		return attributes.get(s).getValue(t);
	}
	
	/*
	public List<? extends Object> getValues(String s){
		return attributes.get(s).getValues();
	}*/
	
	@Override
	public void kill(Instant t) throws TimeException {
		for(Attribute<?> da : this){
			da.kill(t);
		}
	}
	
	public void display(){
		for(Attribute<?> a : this){
			a.display();
		}
	}
	
	/*
	public Time getTimeForValue(String s, Class<?> o){
		return attributes.get(s).getTimeForValue(o);
	}*/

	@Override
	public void update(Instant t, Changeable c, Object o) {
		notifyObservers(t,this,o);
	}
	
	@Override
	public void addObserver(ChangeableObserver o) {
		changeable.addObserver(o);
		for(Attribute<?> a : this){
			a.addObserver(o);
		}
	}

	@Override
	public void removeObserver(ChangeableObserver o) {
		changeable.removeObserver(o);
		for(Attribute<?> a : this){
			a.removeObserver(o);
		}
	}

	@Override
	public void notifyObservers(Instant t, Changeable c, Object o) {
		changeable.notifyObservers(t,c,o);
	}

	@Override
	public boolean hasAttribute(String name) {
		return attributes.containsKey(name);
	}

	public int size(){
		return attributes.size();
	}
	
	@Override
	public Instant getLastChange(){
		Instant t = null;
		Instant n;
		for(Attribute<?> a : this){
			n = a.getLastChange();
			if(t == null || (n != null && n.isAfter(t))){
				t = n;
			}
		}
		return t;
	}

	public void delete(){
		for(Attribute<?> a : attributes.values()){
			a.delete();
		}
		attributes.clear();
		attributes = null;
		changeable.delete();
		changeable = null;
	}

}
