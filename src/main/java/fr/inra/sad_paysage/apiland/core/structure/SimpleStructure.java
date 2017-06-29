package fr.inra.sad_paysage.apiland.core.structure;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fr.inra.sad_paysage.apiland.core.change.Changeable;
import fr.inra.sad_paysage.apiland.core.change.ChangeableObject;
import fr.inra.sad_paysage.apiland.core.change.ChangeableObserver;
import fr.inra.sad_paysage.apiland.core.time.Instant;
import fr.inra.sad_paysage.apiland.core.time.TimeException;

public class SimpleStructure extends Structure {
	
	private static final long serialVersionUID = 1L;

	private Representation<?> representation;
	
	private ChangeableObject changeable;
	
	private double minX;
	
	private double minY;
	
	private double maxX;
	
	private double maxY;
	
	
	public SimpleStructure(){
		changeable = new ChangeableObject();
		//System.out.println("simple strucutre");
	}
	
	@Override
	public SimpleStructure clone() {
		SimpleStructure clone = (SimpleStructure)super.clone();
		clone.changeable = new ChangeableObject();
		if(this.representation != null){
			clone.representation = this.representation;
		}
		return clone;
	}
	
	@Override
	public Representation<?> getDefaultRepresentation() {
		return representation;
	}

	@Override
	public Representation<?> getRepresentation(String name) {
		if(representation.getType().getName().equalsIgnoreCase(name)){
			return representation;
		}
		return null;
	}

	@Override
	public boolean hasRepresentation(String name) {
		if(representation.getType().getName().equalsIgnoreCase(name)){
			return true;
		}
		return false;
	}

	@Override
	public void kill(Instant t) throws TimeException {
		representation.kill(t);
	}

	@Override
	public void addObserver(ChangeableObserver o) {
		changeable.addObserver(o);
		representation.addObserver(o);
	}

	@Override
	public void notifyObservers(Instant t, Changeable c, Object o) {
		changeable.notifyObservers(t,c,o);
	}

	@Override
	public void removeObserver(ChangeableObserver o) {
		changeable.removeObserver(o);
		representation.removeObserver(o);
	}

	@Override
	public void addRepresentation(Representation<?> r) {
		this.representation = r;
		this.representation.addObserver(this);
	}

	@Override
	public void removeRepresentation(String r) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Iterator<Representation<?>> iterator() {
		List<Representation<?>> l = new ArrayList<Representation<?>>();
		l.add(representation);
		return l.iterator();
	}

	@Override
	public Instant getLastChange() {
		return representation.getLastChange();
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
	/*
	
	@Override
	public double minX() {
		return representation.minX();
	}

	@Override
	public double maxX() {
		return representation.maxX();
	}

	@Override
	public double minY() {
		return representation.minY();
	}

	@Override
	public double maxY() {
		return representation.maxY();
	}
	*/
	@Override
	public void delete() {
		super.delete();
		representation.delete();
		representation = null;
		changeable.delete();
		changeable = null;
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
