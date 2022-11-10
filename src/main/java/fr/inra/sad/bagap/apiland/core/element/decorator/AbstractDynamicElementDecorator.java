package fr.inra.sad.bagap.apiland.core.element.decorator;

import fr.inra.sad.bagap.apiland.core.change.Changeable;
import fr.inra.sad.bagap.apiland.core.change.ChangeableObserver;
import fr.inra.sad.bagap.apiland.core.composition.Attribute;
import fr.inra.sad.bagap.apiland.core.composition.Composition;
import fr.inra.sad.bagap.apiland.core.element.AbstractDynamicElement;
import fr.inra.sad.bagap.apiland.core.element.DynamicElement;
import fr.inra.sad.bagap.apiland.core.element.type.DynamicElementType;
import fr.inra.sad.bagap.apiland.core.space.Geometry;
import fr.inra.sad.bagap.apiland.core.space.Point;
import fr.inra.sad.bagap.apiland.core.structure.Representation;
import fr.inra.sad.bagap.apiland.core.structure.Structure;
import fr.inra.sad.bagap.apiland.core.time.Instant;
import fr.inra.sad.bagap.apiland.core.time.Time;
import fr.inra.sad.bagap.apiland.core.time.TimeException;

public class AbstractDynamicElementDecorator<E extends DynamicElement> extends AbstractDynamicElement implements DynamicElementDecorator<E> {

	private static final long serialVersionUID = 1L;
	
	protected E decorate;
	
	public AbstractDynamicElementDecorator(E element, DynamicElementType type){
		super(type);
		decorate = element;
		setId(element.getId());
	}
	
	@Override
	public AbstractDynamicElementDecorator<E> clone(){
		AbstractDynamicElementDecorator<E> clone = (AbstractDynamicElementDecorator<E>)super.clone();
		clone.decorate = decorate;
		return clone;	
	}
	
	@Override
	public int count(Instant t) {
		return decorate.count(t);
	}

	@Override
	public void display() {
		decorate.display();
	}

	@Override
	public double getArea(Instant t) {
		return decorate.getArea(t);
	}

	@Override
	public Geometry getGeometry(Instant t) {
		return decorate.getGeometry(t);
	}

	@Override
	public double getLength(Instant t) {
		return decorate.getLength(t);
	}

	@Override
	public boolean isActive(Instant t, Point g) {
		return decorate.isActive(t,g);
	}

	@Override
	public boolean isActive(Instant t) {
		return decorate.isActive(t);
	}

	@Override
	public void kill(Instant t) throws TimeException {
		decorate.kill(t);
	}

	@Override
	public Representation getDefaultRepresentation() {
		return decorate.getDefaultRepresentation();
	}

	@Override
	public Representation getRepresentation(String name) {
		return decorate.getRepresentation(name);
	}

	@Override
	public Structure getStructure() {
		return decorate.getStructure();
	}

	@Override
	public void setStructure(Structure structure) {
		decorate.setStructure(structure);
	}
	
	@Override
	public boolean hasRepresentation(String name) {
		return decorate.hasRepresentation(name);
	}
	
	@Override
	public Attribute<?> getAttribute(String name) {
		return decorate.getAttribute(name);
	}

	@Override
	public Composition getComposition() {
		return decorate.getComposition();
	}

	@Override
	public void setComposition(Composition composition) {
		decorate.setComposition(composition);
	}

	@Override
	public boolean hasAttribute(String name) {
		return decorate.hasAttribute(name);
	}
	
	@Override
	public void addObserver(ChangeableObserver o) {
		decorate.addObserver(o);
	}

	@Override
	public void removeObserver(ChangeableObserver o) {
		decorate.removeObserver(o);
	}

	@Override
	public void notifyObservers(Instant t, Changeable c, Object o) {
		decorate.notifyObservers(t, c, o);
	}
	
	@Override
	public double minX(){
		return decorate.minX();
	}
	
	@Override
	public double maxX(){
		return decorate.maxX();
	}
	
	@Override
	public double minY(){
		return decorate.minY();
	}
	
	@Override
	public double maxY(){
		return decorate.maxY();
	}
	
	@Override
	public Instant getLastChange(){
		return decorate.getLastChange();
	}
	
	@Override
	public Time getTime() {
		return decorate.getTime();
	}

	@Override
	public void setTime(Time t) {
		// do nothing	
	}

	@Override
	public E getDecorateElement(){
		return decorate;
	}
	
	@Override
	public void delete(){
		super.delete();
		decorate = null;
	}

	
}
