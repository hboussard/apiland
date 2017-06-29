package fr.inra.sad_paysage.apiland.core.structure;


import fr.inra.sad_paysage.apiland.core.change.Changeable;
import fr.inra.sad_paysage.apiland.core.composition.DiscreteAttribute;
import fr.inra.sad_paysage.apiland.core.composition.DiscreteAttributeType;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.matrix.Matrix;
import fr.inra.sad_paysage.apiland.core.time.Instant;
import fr.inra.sad_paysage.apiland.core.time.delay.YearDelay;

/**
 * modeling class of a geometric structure of a dynamic element
 * @author H.Boussard
 */
public abstract class Structure implements Structurable, Iterable<Representation<?>> {

	private static final long serialVersionUID = 1L;
	
	//private DynamicElement element;
		
	@Override
	public Structure clone() {
		try{
			Structure clone = (Structure)super.clone();
			return clone;
		}catch(CloneNotSupportedException ex){
			ex.printStackTrace();
			return null;
		}
	}
	
	@Override
	public void setStructure(Structure structure) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Structure getStructure() {
		return this;
	}
	
	/*
	public void setElement(DynamicElement element){
		this.element = element;
	}
	*/
	
	/*
	public DynamicElement getElement(){
		return element;
	}
	*/
	
	public abstract void addRepresentation(Representation<?> representation);
	
	public abstract void removeRepresentation(String representation);
	
	@Override
	public void update(Instant t, Changeable c, Object o) {
		notifyObservers(t,this,o);
	}
	
	public void delete(){}
	
	public void setMinX(double minX){
		// do nothing
	}
	
	public void setMinY(double minY){
		// do nothing
	}
	
	public void setMaxX(double maxX){
		// do nothing
	}
	
	public void setMaxY(double maxY){
		// do nothing
	}
	
}
