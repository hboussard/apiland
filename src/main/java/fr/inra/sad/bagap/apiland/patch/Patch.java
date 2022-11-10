package fr.inra.sad.bagap.apiland.patch;

import java.util.Set;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;

public interface Patch {

	int getValue();
	
	boolean touches(Pixel p);
	
	boolean contains(Pixel p);
	
	void add(Pixel p);
	
	void addAll(Patch pa);
	
	Pixel pixel();
	
	Set<Pixel> pixels();
	
	void remove(Pixel p);
	
	int size();
	
	double getArea();
	
	void display();
	
	void upPixels();
	
	boolean equals(Patch pi);
	
	Envelope getEnvelope();
	
}
