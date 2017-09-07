package fr.inra.sad.bagap.apiland.core.structure;

import java.util.Iterator;

import fr.inra.sad.bagap.apiland.core.change.Changeable;
import fr.inra.sad.bagap.apiland.core.change.ChangeableObserver;
import fr.inra.sad.bagap.apiland.core.time.Instant;
import fr.inra.sad.bagap.apiland.core.time.TimeException;

public class NullStructure extends Structure{

	private static final long serialVersionUID = 1L;
	
	private double minX;
	
	private double minY;
	
	private double maxX;
	
	private double maxY;
	
	@Override
	public void addRepresentation(Representation<?> r) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeRepresentation(String r) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Representation getRepresentation(String name) {
		return null;
	}

	@Override
	public Representation getDefaultRepresentation() {
		return null;
	}

	@Override
	public boolean hasRepresentation(String name) {
		return false;
	}

	@Override
	public void kill(Instant t) throws TimeException {
		// do nothing
	}

	@Override
	public Instant getLastChange() {
		// TODO Auto-generated method stub
		return null;
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
	public Iterator<Representation<?>> iterator() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public double minX() {
		return minX;
	}

	@Override
	public double maxX() {
		return maxX;
	}

	@Override
	public double minY() {
		return minY;
	}

	@Override
	public double maxY() {
		return maxY;
	}
	
	@Override
	public void setMinX(double minX){
		this.minX = minX;
	}
	
	@Override
	public void setMinY(double minY){
		this.minY = minY;
	}
	
	@Override
	public void setMaxX(double maxX){
		this.maxX = maxX;
	}
	
	@Override
	public void setMaxY(double maxY){
		this.maxY = maxY;
	}


}
