package fr.inra.sad.bagap.apiland.core.element.neighbourhood;

import java.util.HashSet;
import java.util.Set;

import fr.inra.sad.bagap.apiland.core.element.DynamicElement;
import fr.inra.sad.bagap.apiland.core.element.decorator.AbstractDynamicElementDecorator;
import fr.inra.sad.bagap.apiland.core.element.type.DynamicElementType;
import fr.inra.sad.bagap.apiland.core.time.Instant;

public abstract class NeighbourFeature<E extends DynamicElement> extends AbstractDynamicElementDecorator<E> implements NeighbourElement{

	private static final long serialVersionUID = 1L;

	private Set<Neighbourhood> neighbourhoods;
	
	public NeighbourFeature(E element, DynamicElementType type) {
		super(element, type);
		neighbourhoods = new HashSet<Neighbourhood>();
	}

	@Override
	public NeighbourFeature<E> clone(){
		NeighbourFeature<E> clone = (NeighbourFeature<E>)super.clone();
		clone.neighbourhoods = new HashSet<Neighbourhood>();
		for(Neighbourhood n : this.neighbourhoods){
			clone.addNeighbourhood(n.clone());
		}
		return clone;
	}
	
	@Override
	public Set<? extends Neighbourhood> getNeighbourhoods(){
		return neighbourhoods;
	}

	@Override
	public void addNeighbourhood(Neighbourhood neighbourhood){
		neighbourhood.setNeighbour(this);
		neighbourhoods.add(neighbourhood);
	}

	@Override
	public void displayNeighbourhood(Instant t){
		for(Neighbourhood n : getNeighbourhoods()){
			if(n.isActive(t)){
				System.out.println(n.getNeighbour(this)+" : "+n.length(t));
			}
		}
	}
	
	@Override
	public Set<DynamicElement> getNeighbours(Instant t){
		Set<DynamicElement> neighbours = new HashSet<DynamicElement>();
		for(Neighbourhood v : getNeighbourhoods()){
			if(v.isActive(t)){
				neighbours.add(v.getNeighbour(this));
			}
		}
		return neighbours;
	}
	
	@Override
	public Neighbourhood getNeighbourhood(DynamicElement element){
		for(Neighbourhood n : getNeighbourhoods()){
			if(n.getNeighbour(this).equals(element)){
				return n;
			}
		}
		throw new IllegalArgumentException();
	}
	
	@Override
	public int getNeighbourhoodCount(){
		return neighbourhoods.size();
	}
	
	@Override
	public double perimeter(Instant t){
		return getLength(t);
	}
	
	@Override
	public double getNeighbourhoodPerimeter(Instant t){
		double perimeter = 0;
		for(Neighbourhood v : getNeighbourhoods()){
			perimeter += v.getLength(t);
		}
		return perimeter;
	}
	
	@Override
	public boolean isClosed(Instant t){
		return perimeter(t) - getNeighbourhoodPerimeter(t) == 0;
	}
	
	@Override
	public double getUnclosedPerimeter(Instant t){
		return perimeter(t) - getNeighbourhoodPerimeter(t);
	}
	
	
}
