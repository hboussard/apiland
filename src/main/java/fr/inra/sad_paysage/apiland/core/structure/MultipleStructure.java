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
package fr.inra.sad_paysage.apiland.core.structure;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import fr.inra.sad_paysage.apiland.core.change.Changeable;
import fr.inra.sad_paysage.apiland.core.change.ChangeableObject;
import fr.inra.sad_paysage.apiland.core.change.ChangeableObserver;
import fr.inra.sad_paysage.apiland.core.time.Instant;
import fr.inra.sad_paysage.apiland.core.time.TimeException;

public class MultipleStructure extends Structure implements Iterable<Representation<?>>{

	private static final long serialVersionUID = 1L;

	private Map<String,Representation<?>> representations;
	
	private ChangeableObject changeable;
	
	public MultipleStructure(){
		representations = new HashMap<String,Representation<?>>();
		changeable = new ChangeableObject();
	}
	
	public MultipleStructure clone(){
		MultipleStructure clone = (MultipleStructure)super.clone();
		clone.changeable = new ChangeableObject();
		if(representations != null){
			clone.representations = new HashMap<String,Representation<?>>();
			for(Entry<String,Representation<?>> s : this.representations.entrySet()){	
				clone.representations.put(s.getKey(), s.getValue().clone());
			}
		}
		return clone;
	}

	@Override
	public Iterator<Representation<?>> iterator() {
		return representations.values().iterator();
	}
	
	@Override
	public void addRepresentation(Representation<?> r) {
		if(representations.size() == 0){
			representations.put("the_geom", r);
		}
		representations.put(r.getName(), r);
		r.addObserver(this);
	}
	
	@Override
	public void removeRepresentation(String name){
		representations.remove(representations.get(name));
	}

	@Override
	public Representation<?> getDefaultRepresentation() {
		return representations.get("the_geom");
	} 
	
	@Override
	public Representation getRepresentation(String name) {
		if(representations.containsKey(name)){
			return representations.get(name);
		}
		throw new IllegalArgumentException("representation '"+name+"' does not exist");
	}
	
	@Override
	public void kill(Instant t) throws TimeException {
		for(Representation<?> r : this){
			r.kill(t);
		}
	}
	
	public void displayTuples(){
		for(Representation<?> r : this){
			r.display();
		}
	}

	@Override
	public void addObserver(ChangeableObserver o) {
		changeable.addObserver(o);
		for(Representation<?> r : this){
			r.addObserver(o);
		}
	}

	@Override
	public void removeObserver(ChangeableObserver o) {
		changeable.removeObserver(o);
		for(Representation<?> r : this){
			r.removeObserver(o);
		}
	}

	@Override
	public void notifyObservers(Instant t, Changeable c, Object o) {
		changeable.notifyObservers(t,c,o);
	}

	@Override
	public boolean hasRepresentation(String name) {
		return representations.containsKey(name);
	}

	@Override
	public Instant getLastChange(){
		Instant t = null;
		Instant n;
		for(Representation<?> r : this){
			n = r.getLastChange();
			if(t == null || (n != null && n.isAfter(t))){
				t = n;
			}
		}
		return t;
	}

	@Override
	public double minX() {
		return getDefaultRepresentation().minX();
	}

	@Override
	public double maxX() {
		return getDefaultRepresentation().maxX();
	}

	@Override
	public double minY() {
		return getDefaultRepresentation().minY();
	}

	@Override
	public double maxY() {
		return getDefaultRepresentation().maxY();
	}
	
	@Override
	public void delete() {
		super.delete();
		for(Representation<?> r : representations.values()){
			r.delete();
		}
		representations.clear();
		representations = null;
		
		changeable.delete();
		changeable = null;
	}


}
