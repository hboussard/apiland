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
package fr.inra.sad_paysage.apiland.core.composition;

import java.io.Serializable;

import fr.inra.sad_paysage.apiland.core.change.Changeable;
import fr.inra.sad_paysage.apiland.core.change.ChangeableObject;
import fr.inra.sad_paysage.apiland.core.change.ChangeableObserver;
import fr.inra.sad_paysage.apiland.core.time.Instant;
import fr.inra.sad_paysage.apiland.core.time.Time;
import fr.inra.sad_paysage.apiland.core.time.TimeException;
import fr.inra.sad_paysage.apiland.core.time.delay.Delay;

public class StaticAttribute<O extends Serializable> extends Attribute<O>{

	private static final long serialVersionUID = 1L;
	
	private TemporalValue<O> tv;
	
	public StaticAttribute(AttributeType type){
		super(type);
	}
	
	private O getValue(){
		if(tv != null){
			return tv.getValue();
		}
		return null;
	}
	
	private void setValue(O o){
		tv.setValue(o);
	}
	
	@Override
	public O getValue(Instant t) {
		return getValue();
	}
	
	@Override
	public void setValue(Time t, Serializable o) {
		if(tv == null){
			tv = new TemporalValue(t,o);
		}else{
			setValue((O)o);
		}
	}
	
	@Override
	public void replaceValue(Time t, Serializable o){
		if(tv == null){
			tv = new TemporalValue(t,o);
		}else{
			setValue((O)o);
		}
	}
	
	@Override
	public void display() {
		System.out.println(tv);
	}
	
	@Override
	public Time getTime() {
		return tv.getTime();
	}

	@Override
	public boolean isActive(Instant t) {
		if(tv == null){
			return false;
		}
		return tv.isActive(t);
	}

	@Override
	public void kill(Instant t) throws TimeException {
		tv.kill(t);
	}

	@Override
	public void setTime(Time t) {
		tv.setTime(t);
	}
	
	@Override
	public void addObserver(ChangeableObserver o) {
		// do nothing
	}

	@Override
	public void removeObserver(ChangeableObserver o) {
		// do nothing
	}

	@Override
	public void notifyObservers(Instant t, Changeable c, Object o) {
		// do nothing
	}
	
	@Override
	public void setValue(TemporalValue<?> tv) {
		this.tv = (TemporalValue<O>)tv;
	}

	@Override
	public void addTemporal(TemporalValue<O> t) {
		throw new UnsupportedOperationException();
	}

	@Override
	public TemporalValue<O> get(int index) {
		if(index == 0){
			return tv;
		}
		return null;
	}

	@Override
	public int size() {
		if(tv != null){
			return 1;
		}
		return 0;
	}

	@Override
	public TemporalValue<O> getActive(Instant t) {
		if(tv.isActive(t)){
			return tv;
		}
		return null;
	}
	
	@Override
	public int getIndex(Instant t) {
		return 0;
	}
	
	@Override
	public TemporalValue<O> getFirst(){
		return tv;
	}
	
	@Override
	public TemporalValue<O> getLast(){
		return tv;
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
	public void delete() {
		super.delete();
		tv.delete();
		tv = null;
	}

	@Override
	public boolean hasValue(Serializable value, Time t) {
		return tv.getValue().equals(value);
	}

	@Override
	public void clear() {
		this.tv = null;
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
