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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import fr.inra.sad.bagap.apiland.core.change.Changeable;
import fr.inra.sad.bagap.apiland.core.change.ChangeableObserver;
import fr.inra.sad.bagap.apiland.core.dynamic.Dynamic;
import fr.inra.sad.bagap.apiland.core.time.Instant;
import fr.inra.sad.bagap.apiland.core.time.Interval;
import fr.inra.sad.bagap.apiland.core.time.Time;
import fr.inra.sad.bagap.apiland.core.time.TimeException;
import fr.inra.sad.bagap.apiland.core.time.delay.Delay;

/**
 * modeling class of dynamic attribute
 * @author H. BOUSSARD
 */
public class DynamicAttribute<O extends Serializable> extends Attribute<O> implements Iterable<TemporalValue<O>>{

	/**
	 * version number
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * the dynamic
	 */
	private Dynamic<TemporalValue<O>> dynamic;
	
	/**
	 * constructor
	 * @param name the attribute name
	 */
	public DynamicAttribute(DynamicAttributeType type){
		super(type);
		dynamic = new Dynamic<TemporalValue<O>>();
	}
	
	public DynamicAttribute(String name, Class<? extends Time> temporal, Class<O> binding){
		this(new DynamicAttributeType(name,temporal,binding));
	}
	
	@Override
	public DynamicAttribute<O> clone() {
		DynamicAttribute<O> clone = (DynamicAttribute<O>)super.clone(); 
		clone.dynamic = this.dynamic.clone();
		return clone;
	}
	
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append(getName());
		/*
		sb.append(' ');
		sb.append("temporal ");
		sb.append(getTemporalBinding());
		sb.append(" and value ");
		sb.append(getBinding());
		*/
		return sb.toString();
	}
	
	@Override
	public DynamicAttributeType getType(){
		return (DynamicAttributeType)super.getType();
	}
	
	public Class<? extends Time> getTemporalBinding(){
		return getType().getTemporalBinding();
	}
	
	@Override
	public void addTemporal(TemporalValue<O> t) {
		dynamic.addTemporal(t);
	}
	
	@Override
	public void removeTemporal(TemporalValue<O> t) {
		dynamic.removeTemporal(t);
	}

	@Override
	public Time getTime() {
		return dynamic.getTime();
	}

	@Override
	public boolean isActive(Instant t) {
		return dynamic.isActive(t);
	}

	@Override
	public void kill(Instant t) throws TimeException {
		dynamic.kill(t);
	}

	@Override
	public void setTime(Time t) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<TemporalValue<O>> iterator() {
		return dynamic.iterator();
	}
	
	@Override
	public void replaceValue(Time t, Serializable o){
		if(getBinding().equals(o.getClass())){
			dynamic.getActive(t.start()).setValue((O) o);
			notifyObservers(t.start(),this, o);
			return;
		}
		throw new IllegalArgumentException("attribute '"+getName()+"' expected "+getBinding()+" and not "+o.getClass());
	}

	@Override
	public void setValue(TemporalValue<?> tv) {
		if(getTemporalBinding().equals(tv.getTime().getClass())){
			dynamic.addTemporal((TemporalValue<O>)tv);
			notifyObservers(tv.getTime().start(),this, tv.getValue());
			return;
		}else if(getTemporalBinding().equals(Interval.class)
				&& tv.getTime().getClass().equals(Instant.class)){
			if(dynamic.getLast() != null){
				try{
					dynamic.getLast().kill((Instant)tv.getTime());
				}catch(TimeException e){
					e.printStackTrace();
				}	
			}
			tv.setTime(new Interval((Instant)tv.getTime()));
			dynamic.addTemporal((TemporalValue<O>)tv);
			notifyObservers(tv.getTime().start(),this, tv.getValue());
			return;
		}
		throw new IllegalArgumentException("attribute '"+getName()+"' expected "+getTemporalBinding()+" and not "+tv.getTime().getClass());
	}
	
	@Override
	public void setValue(Time t, Serializable o){
		if(getBinding().equals(o.getClass())){
			if(getTemporalBinding().equals(t.getClass())){
				dynamic.addTemporal(new TemporalValue<O>(t,(O)o));
				notifyObservers(t.start(),this, o);
				return;
			}else if(getTemporalBinding().equals(Interval.class)
					&& t.getClass().equals(Instant.class)){
				if(dynamic.getLast() != null){
					try{
						dynamic.getLast().kill((Instant)t);
					}catch(TimeException e){
						e.printStackTrace();
					}	
				}
				dynamic.addTemporal(new TemporalValue<O>(new Interval((Instant)t),(O)o));
				notifyObservers(t.start(),this, o);
				return;
			}
			throw new IllegalArgumentException("attribute '"+getName()+"' expected "+getTemporalBinding()+" and not "+t.getClass());
		}
		throw new IllegalArgumentException("attribute '"+getName()+"' expected "+getBinding()+" and not "+o.getClass());
		
	}
	
	/**
	 * to get the value at a precise instant
	 * @param t the instant
	 * @return the value
	 */
	@Override
	public O getValue(Instant t){
		Iterator<TemporalValue<O>> ite = dynamic.iteratorInverse();
		TemporalValue<O> tv;
		while(ite.hasNext()){
			tv = ite.next();
			if(tv.isActive(t)){
				return tv.getValue();
			}
		}
		return null;
	}
	
	/**
	 * to get the list of value
	 * @return the list of values
	 */
	public List<O> values(){
		List<O> l = new ArrayList<O>();
		for(TemporalValue<O> tv : this){
			l.add(tv.getValue());
		}
		return l;
	}
	
	/**
	 * to get the list of value active during a given interval
	 * @param t the interval
	 * @return the list of values
	 */
	public List<O> values(Interval t){
		List<O> l = new ArrayList<O>();
		for(TemporalValue<O> tv : this){
			if(tv.getTime().intersects(t)){
				l.add(tv.getValue());
			}
		}
		return l;
	}
	
	@Override
	public boolean hasValue(Serializable value, Time t) {
		for(TemporalValue<O> tv : this){
			if(tv.getTime().intersects(t) && tv.getValue().equals(value)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * to get the active time of a given value
	 * @param o the value
	 * @return the active time
	 */
	@Override
	public Time getTimeForValue(Serializable o){
		Time t = null;
		for(TemporalValue<O> tv : this){
			if(tv.getValue().equals(o)){
				t = Time.add(t,tv.getTime());
			}
		}
		return t;
	}
	
	public int getCountOfLastValue(Serializable o){
		int count = 0;
		for(int i=dynamic.size()-1; i>=0; i--){
			if(dynamic.get(i).getValue().equals(o)){
				count++;
			}else{
				break;
			}
		}
		return count;
	}

	@Override
	public TemporalValue<O> getActive(Instant t) {
		return dynamic.getActive(t);
	}

	@Override
	public void addObserver(ChangeableObserver o) {
		dynamic.addObserver(o);
	}

	@Override
	public void removeObserver(ChangeableObserver o) {
		dynamic.removeObserver(o);	
	}

	@Override
	public void notifyObservers(Instant t, Changeable c, Object o) {
		dynamic.notifyObservers(t,c,o);
	}

	@Override
	public void display() {
		for(TemporalValue<O> tv : this){
			System.out.print(tv+" ");
		}
		System.out.println();
	}

	public Dynamic<TemporalValue<O>> getDynamics(){
		return dynamic;
	}

	@Override
	public TemporalValue<O> get(int index) {
		return dynamic.get(index);
	}

	@Override
	public int size() {
		return dynamic.size();
	}

	@Override
	public int getIndex(Instant t) {
		return dynamic.getIndex(t);
	}

	@Override
	public TemporalValue<O> getFirst() {
		return dynamic.getFirst();
	}
	
	@Override
	public TemporalValue<O> getLast() {
		return dynamic.getLast();
	}

	@Override
	public Instant getLastChange() {
		return dynamic.getLastChange();
	}
	
//	public List<O> getValues(Delay delay);

	@Override
	public void delete(){
		super.delete();
		dynamic.delete();
		dynamic = null;
	}

	public boolean hasOnlyForValue(Instant start, Instant end, Set<O> value){
		List<O> values = values(new Interval(start, end));
		for(O v : values){
			if(!value.contains(v)){
				return false;
			}
		}
		return true;
	}

	@Override
	public void clear() {
		dynamic.clear();
		//dynamic = new Dynamic<TemporalValue<O>>();
	}

	@Override
	public Instant getLastOccurence(Serializable o) {
		Instant t = null;
		for(TemporalValue<O> tv : this){
			if(tv.getValue().equals(o)){
				t = tv.getTime().end();
			}
		}
		return t;
	}

	
	@Override
	public O[] split(Delay d) {
		List<O> l = new ArrayList<O>();
		Instant end = getTime().end();
		if(end.equals(Instant.FUTUR)){
			end = getLast().getTime().start();
		}
		for(Instant t = getTime().start(); t.isBefore(end) || t.equals(end); t = d.next(t)){
			l.add(getValue(t));
		}
		return (O[]) l.toArray(new Serializable[l.size()]);
	}

}
