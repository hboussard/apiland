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
package fr.inra.sad.bagap.apiland.simul.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import fr.inra.sad.bagap.apiland.core.element.DynamicElement;
import fr.inra.sad.bagap.apiland.core.time.Instant;
import fr.inra.sad.bagap.apiland.core.time.delay.Delay;
import fr.inra.sad.bagap.apiland.simul.Simulator;

public class CompositeModel<M extends Model> extends Model implements Collection<M> {

	private static final long serialVersionUID = 1L;

	private Map<String, M> models;
	
	private List<String> order;
	
	protected Delay delay;
	
	public CompositeModel(String name, Simulator simulator) {
		super(name, simulator);
		init(simulator.manager().delay());
	}
	
	public CompositeModel(String name, Instant start, Simulator simulator) {
		super(name, start, simulator);
		init(simulator.manager().delay());
	}
	
	public CompositeModel(String name, Delay delay, Simulator simulator) {
		super(name, simulator);
		init(delay);
	}
	
	public CompositeModel(String name, Instant start, Delay delay, Simulator simulator) {
		super(name, start, simulator);
		init(delay);
	}
	
	public CompositeModel(String name, Instant start, Delay delay, Simulator simulator, DynamicElement element) {
		super(name, start, simulator, element);
		init(delay);
	}
	
	private void init(Delay delay){
		models = new HashMap<String, M>();
		order = new ArrayList<String>();
		this.delay = delay;
	}
	
	public void display(){
		System.out.println("model listing");
		for(M m : this){
			System.out.println(m);
		}
	}
	
	@Override
	public void initCurrent(Instant t){
		setCurrent(t);
		for(M m : this){
			m.initCurrent(t);
		}
	}
	
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append(getName());
		sb.append('\n');
		for(M m : this){
			sb.append('	');
			sb.append(m);
			sb.append('\n');
		}
		return sb.toString();
	}
	
	@Override
	public int size(){
		return models.size();
	}
	
	@Override
	public boolean add(M model){
		order.add(model.getName());
		models.put(model.getName(), model);
		model.setParent(this);
		return true;
	}
	
	public void add(int index, M model){
		order.add(index, model.getName());
		models.put(model.getName(), model);
	}
	
	public M get(int index){
		return models.get(order.get(index));
	}
	
	public M get(String name){
		return models.get(name);
	}
	
	@Override
	public boolean deepContains(String name){
		if(getName().equalsIgnoreCase(name)){
			return true;
		}
		for(M m : this){
			if(m.deepContains(name)){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public Model deepGet(String name){
		if(getName().equalsIgnoreCase(name)){
			return this;
		}
		for(M m : this){
			if(m.deepContains(name)){
				return m.deepGet(name);
			}
		}
		throw new IllegalArgumentException();
	}
	
	@Override
	public boolean run(Instant t) {
		boolean ok = true;
		while(!current().isAfter(t) && !manager().isCancelled()){
			//System.out.println("lancement de "+getName()+" at "+current());
			for(M m : this){
				//System.out.println(getName()+" lance "+m.getName()+" at "+current());
				//manager().display(m.getName()+" at "+current());
				if(!m.run(current())){
					ok = false;
				}
			}
			if(ok){
				setCurrent(delay.next(current()));
			}else{
				simulation().abort(current(), "");
				break;
			}
		}
		return ok;
	}
	
	@Override
	public void delete(){
		super.delete();
		delay = null;
		for(Model m : models.values()){
			m.delete();
		}
		models.clear();
		models = null;
	}

	@Override
	public Iterator<M> iterator() {
		return new ModelIterator(order, models);
	}

	@Override
	public boolean addAll(Collection<? extends M> all) {
		for(M m : all){
			models.put(m.getName(), m);
		}
		return true;
	}

	public boolean contains(String name) {
		return models.containsKey(name);
	}
	
	@Override
	public boolean contains(Object o) {
		return models.values().contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> all) {
		return models.values().containsAll(all);
	}

	@Override
	public boolean isEmpty() {
		return models.isEmpty();
	}

	@Override
	public Object[] toArray() {
		return models.values().toArray();
	}

	@Override
	public <T> T[] toArray(T[] array) {
		return models.values().toArray(array);
	}
	
	@Override
	public void clear() {
		throw new IllegalArgumentException();
	}

	@Override
	public boolean remove(Object o) {
		throw new IllegalArgumentException();
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throw new IllegalArgumentException();
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new IllegalArgumentException();
	}

}
