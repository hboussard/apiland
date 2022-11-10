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
package fr.inra.sad.bagap.apiland.core.element.map;

import java.util.Collection;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import fr.inra.sad.bagap.apiland.core.change.Changeable;
import fr.inra.sad.bagap.apiland.core.change.ChangeableObserver;
import fr.inra.sad.bagap.apiland.core.element.DynamicElement;
import fr.inra.sad.bagap.apiland.core.element.DynamicLayer;
import fr.inra.sad.bagap.apiland.core.time.Instant;
import fr.inra.sad.bagap.apiland.core.time.Time;
import fr.inra.sad.bagap.apiland.core.time.TimeException;

/**
 * modeling class of a dynamic map
 * @author H.BOUSSARD
 */
public class DefaultDynamicMap implements DynamicMap {

	private static final long serialVersionUID = 1L;
	
	/** the structure of dynamic layers */
	private HashMap<String,DynamicLayer<? extends DynamicElement>> map;
	
	public DefaultDynamicMap(){
		this.map = new HashMap<String,DynamicLayer<? extends DynamicElement>>();
	}
	
	@Override
	public DefaultDynamicMap clone(){
		DefaultDynamicMap clone;
		try {
			clone = (DefaultDynamicMap)super.clone();
			clone.map = new HashMap<String,DynamicLayer<? extends DynamicElement>>();
			for(Entry<String,DynamicLayer<? extends DynamicElement>> e : map.entrySet()){
				System.out.println("clonage de "+e.getKey());
				clone.map.put(e.getKey(), e.getValue().clone());
			}
			return clone;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public void addObserver(ChangeableObserver o) {
		for(DynamicLayer<?> e : values()){
			e.addObserver(o);
		}
	}

	@Override
	public void removeObserver(ChangeableObserver o) {
		for(DynamicLayer<?> e : values()){
			e.removeObserver(o);
		}
	}

	@Override
	public void notifyObservers(Instant t, Changeable c, Object o) {
		for(DynamicLayer<?> e : values()){
			e.notifyObservers(t,c,o);
		}
	}

	@Override
	public Iterator<DynamicLayer<? extends DynamicElement>> iterator() {
		return values().iterator();
	}

	@Override
	public Time getTime() {
		Time t = null;
		for(DynamicLayer<?> e : values()){
			t = Time.add(t,e.getTime());
		}
		return t;
	}

	@Override
	public boolean isActive(Instant t) {
		for(DynamicLayer<?> e : values()){
			if(e.isActive(t)){
				return true;
			}
		}
		return false;
	}

	@Override
	public void kill(Instant t) throws TimeException {
		for(DynamicLayer<?> e : values()){
			e.kill(t);
		}
	}

	@Override
	public void setTime(Time t) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Instant getLastChange() {
		Instant last = null;
		Instant t;
		for(DynamicLayer<?> e : values()){
			if(last == null){
				last = e.getLastChange();
			}else{
				t = e.getLastChange();
				if(t.isAfter(last)){
					last = t;
				}
			}
		}
		return last;
	}

	@Override
	public void clear() {
		map.clear();
	}

	@Override
	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	@Override
	public Set<java.util.Map.Entry<String, DynamicLayer<? extends DynamicElement>>> entrySet() {
		return map.entrySet();
	}

	@Override
	public DynamicLayer<?> get(Object key) {
		return map.get(key);
	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	@Override
	public Set<String> keySet() {
		return map.keySet();
	}

	@Override
	public DynamicLayer<?> put(String key, DynamicLayer<?> value) {
		return map.put(key, value);
	}

	@Override
	public void putAll(Map<? extends String, ? extends DynamicLayer<? extends DynamicElement>> m) {
		map.putAll(m);
	}

	@Override
	public DynamicLayer<?> remove(Object key) {
		return map.remove(key);
	}

	@Override
	public int size() {
		return map.size();
	}

	@Override
	public Collection<DynamicLayer<? extends DynamicElement>> values() {
		return map.values();
	}
	
	@Override
	public void delete() {
		for(DynamicLayer<? extends DynamicElement> dl : map.values()){
			dl.delete();
		}
		map.clear();
		map = null;
	}
	

}
