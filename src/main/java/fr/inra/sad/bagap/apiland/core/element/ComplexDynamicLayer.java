package fr.inra.sad.bagap.apiland.core.element;

import java.util.Set;

import fr.inra.sad.bagap.apiland.core.element.type.DynamicLayerType;
import fr.inra.sad.bagap.apiland.core.space.Geometry;
import fr.inra.sad.bagap.apiland.core.space.Point;
import fr.inra.sad.bagap.apiland.core.time.Instant;
import fr.inra.sad.bagap.apiland.core.time.Time;
import fr.inra.sad.bagap.apiland.core.time.TimeException;

public class ComplexDynamicLayer<E extends DynamicElement> extends AbstractDynamicLayer<E>{

	private static final long serialVersionUID = 1L;
	
	private Time time;
	
	public ComplexDynamicLayer(DynamicLayerType type){
		super(type);
	}

	@Override
	public double getArea(Instant t) {
		return getStructure().getDefaultRepresentation().getArea(t);
	}

	@Override
	public Geometry getGeometry(Instant t) {
		return getStructure().getDefaultRepresentation().getGeometry(t);
	}

	@Override
	public double getLength(Instant t) {
		return getStructure().getDefaultRepresentation().getLength(t);
	}

	@Override
	public boolean isActive(Instant t, Point g) {
		return getStructure().getDefaultRepresentation().isActive(t);
	}

	@Override
	public Time getTime() {
		return time;
	}

	@Override
	public boolean isActive(Instant t) {
		return time.isActive(t);
	}

	@Override
	public void setTime(Time t) {
		this.time = t;
		getStructure().getDefaultRepresentation().setTime(t);
	}
	
	@Override
	public void kill(Instant t) throws TimeException{
		time = Time.kill(time,t);
		super.kill(t);
	}

	@Override
	public double maxX() {
		return getStructure().maxX();
	}

	@Override
	public double maxY() {
		return getStructure().maxY();
	}

	@Override
	public double minX() {
		return getStructure().minX();
	}

	@Override
	public double minY() {
		return getStructure().minY();
	}

	@Override
	public void delete(){
		super.delete();
		time = null;
		
	}

	@Override
	public <F extends DynamicElement> Set<F> set(Class<F> theClass) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
