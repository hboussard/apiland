package fr.inra.sad.bagap.apiland.core.composition;

import java.io.Serializable;

import fr.inra.sad.bagap.apiland.core.change.Changeable;
import fr.inra.sad.bagap.apiland.core.change.ChangeableObserver;
import fr.inra.sad.bagap.apiland.core.time.Instant;
import fr.inra.sad.bagap.apiland.core.time.Time;
import fr.inra.sad.bagap.apiland.core.time.TimeException;
import fr.inra.sad.bagap.apiland.core.time.delay.Delay;

public class DiscreteAttribute<O extends Serializable> extends Attribute<O> {

	private static final long serialVersionUID = 1L;
	
	private Serializable[] values;

	public DiscreteAttribute(DiscreteAttributeType type) {
		super(type);
		values = new Serializable[type.getInternalSize()];
	}
	
	@Override
	public DiscreteAttributeType getType(){
		return (DiscreteAttributeType)super.getType();
	}
	
	@Override
	public O getValue(Instant t) {
		return (O) values[getType().getIndex(t)];
	}
	
	@Override
	public boolean hasValue(Serializable value, Time t) {
		for(int i=getType().getIndex(t.start()); i<getType().getIndex(t.end()); i++){
			if(values[i].equals(value)){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean isActive(Instant t) {
		return getType().getTime().isActive(t);
	}
	
	@Override
	public void setValue(Time t, Serializable o) {
		if(t instanceof Instant){
			values[getType().getIndex((Instant)t)] = o;
			return;
		}
		throw new IllegalArgumentException();
	}

	@Override
	public void replaceValue(Time t, Serializable o) {
		setValue(t, o);
	}
	
	@Override
	public void delete(){
		super.delete();
		for(int i=0; i<values.length; i++){
			values[i] = null;
		}
		values = null;
	}

	@Override
	public void addObserver(ChangeableObserver o) {
		// do noting
	}

	@Override
	public void removeObserver(ChangeableObserver o) {
		// do noting
	}

	@Override
	public void notifyObservers(Instant t, Changeable c, Object o) {
		// do noting
	}
	
	@Override
	public void setValue(TemporalValue<?> tv) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addTemporal(TemporalValue<O> t) {
		throw new UnsupportedOperationException();
	}

	@Override
	public TemporalValue<O> getActive(Instant t) {
		throw new UnsupportedOperationException();
	}

	@Override
	public TemporalValue<O> get(int index) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getIndex(Instant t) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int size() {
		throw new UnsupportedOperationException();
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
	public Time getTime() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setTime(Time t) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void kill(Instant t) throws TimeException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Instant getLastChange() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void display() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Time getTimeForValue(Serializable o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		values =  new Serializable[getType().getInternalSize()];
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
