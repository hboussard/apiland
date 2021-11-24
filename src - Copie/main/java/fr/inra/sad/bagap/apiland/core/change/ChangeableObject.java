package fr.inra.sad.bagap.apiland.core.change;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fr.inra.sad.bagap.apiland.core.time.Instant;

public class ChangeableObject implements Changeable, Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * list of observers
	 */
	private List<ChangeableObserver> observers;
	
	public ChangeableObject(){
		observers = new ArrayList<ChangeableObserver>();
	}
	
	@Override
	public void addObserver(ChangeableObserver o) {
		observers.add(o);
	}

	@Override
	public void removeObserver(ChangeableObserver o) {
		observers.remove(o);
	}

	@Override
	public void notifyObservers(Instant t, Changeable c, Object o) {
		for(ChangeableObserver ob : observers){
			ob.update(t,c,o);
		}
	}

	@Override
	public Instant getLastChange() {
		throw new UnsupportedOperationException();
	}
	
	public void delete() {
		observers.clear();
		observers = null;
	}

}
