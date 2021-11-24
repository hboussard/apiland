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
import java.lang.reflect.InvocationTargetException;

import fr.inra.sad.bagap.apiland.core.change.Changeable;
import fr.inra.sad.bagap.apiland.core.change.ChangeableObject;
import fr.inra.sad.bagap.apiland.core.change.ChangeableObserver;
import fr.inra.sad.bagap.apiland.core.time.Instant;
import fr.inra.sad.bagap.apiland.core.time.Time;
import fr.inra.sad.bagap.apiland.core.time.TimeException;
import fr.inra.sad.bagap.apiland.core.time.delay.Delay;

public class ExternalMethodAttribute<O extends Serializable> extends Attribute<O>{

	private static final long serialVersionUID = 1L;
	
	public Object object;
	
	private Object[] arg;
	
	/**
	 * list of observers
	 */
	private ChangeableObject changeable;
	
	public ExternalMethodAttribute(String name, Class<? extends Time> temporal, Class<?> binding){
		this(new ExternalMethodAttributeType(name,temporal,binding));
	}
	
	public ExternalMethodAttribute(ExternalMethodAttributeType type) {
		super(type);
		changeable = new ChangeableObject();
	}
	
	public String toString(){
		return getName();
	}
	
	public ExternalMethodAttributeType getType(){
		return (ExternalMethodAttributeType)super.getType();
	}
	
	@Override
	public void display() {
		throw new UnsupportedOperationException();
	}
	
	public void setObject(Object object){
		this.object = object;
	}
	
	public void setParameter(Object... arg){
		this.arg = arg;
	}
	
	public Object getParameter(int index) {
		return arg[index];
	}
	
	public int getParameterCount(){
		if(arg == null){
			return 0;
		}
		return arg.length;
	}

	@Override
	public O getValue(Instant t) {
		try {
			Object[] args = new Object[1+getParameterCount()];
			args[0] = t;
			for(int i=1; i<=getParameterCount(); i++){
				args[i] = getParameter(i-1);
			}
			return (O)getType().getMethod().invoke(object,args);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			//e.printStackTrace();
			return null;
		}
		throw new IllegalArgumentException();
	}
	
	@Override
	public void replaceValue(Time t, Serializable o){
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void setValue(TemporalValue<?> tv) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setValue(Time t, Serializable o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Time getTime() {
		return null;
	}

	@Override
	public boolean isActive(Instant t) {
		return true;
	}

	@Override
	public void kill(Instant t) throws TimeException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setTime(Time t) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addObserver(ChangeableObserver o) {
		changeable.addObserver(o);
	}

	@Override
	public void removeObserver(ChangeableObserver o) {
		changeable.addObserver(o);
	}

	/*
	public void notifyObservers(Instant t) {
		// do nothing
	}*/

	@Override
	public void notifyObservers(Instant t, Changeable c, Object o) {
		changeable.notifyObservers(t, c, o);
	}

	@Override
	public void addTemporal(TemporalValue<O> t) {
		throw new UnsupportedOperationException();
	}

	@Override
	public TemporalValue<O> get(int index) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int size() {
		throw new UnsupportedOperationException();
	}

	@Override
	public TemporalValue<O> getActive(Instant t) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public int getIndex(Instant t) {
		return 0;
	}

	@Override
	public TemporalValue<O> getFirst() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public TemporalValue<O> getLast() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Instant getLastChange() {
		return null;
	}
	
	@Override
	public Time getTimeForValue(Serializable o) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean hasValue(Serializable value, Time t) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void delete() {
		super.delete();
		object = null;
		arg = null;
		changeable.delete();
		changeable = null;
	}

	@Override
	public void clear() {
		// do nothing
	}

	@Override
	public void removeTemporal(TemporalValue<O> t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Instant getLastOccurence(Serializable o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public O[] split(Delay d) {
		// TODO Auto-generated method stub
		return null;
	}


}
