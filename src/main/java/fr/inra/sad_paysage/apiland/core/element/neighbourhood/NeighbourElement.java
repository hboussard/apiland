package fr.inra.sad_paysage.apiland.core.element.neighbourhood;

import java.util.Set;

import fr.inra.sad_paysage.apiland.core.element.DynamicElement;
import fr.inra.sad_paysage.apiland.core.time.Instant;

public interface NeighbourElement {

///// gestion du voisinnage
	
	Set<? extends Neighbourhood> getNeighbourhoods();

	void addNeighbourhood(Neighbourhood neighbourhood);
	
	void displayNeighbourhood(Instant t);
	
	Set<DynamicElement> getNeighbours(Instant t);
	
	Neighbourhood getNeighbourhood(DynamicElement element);
	
	int getNeighbourhoodCount();
	
	double perimeter(Instant t);
	
	double getNeighbourhoodPerimeter(Instant t);
	
	boolean isClosed(Instant t);
	
	double getUnclosedPerimeter(Instant t);
	
}
