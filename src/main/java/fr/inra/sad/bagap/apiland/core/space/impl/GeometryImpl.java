package fr.inra.sad.bagap.apiland.core.space.impl;

import java.io.Serializable;

import fr.inra.sad.bagap.apiland.core.space.Geometry;

public interface GeometryImpl extends Cloneable, Serializable{

	GeometryImpl clone();
	
	GeometryImplType getType();
	
	GeometryImpl addGeometryImpl(GeometryImpl impl);
	
	double minX();

	double maxX();

	double minY();

	double maxY();

	void display();

	void init(Geometry g);

	boolean isSmooth();

	GeometryImpl smooth();

	int count();

	double getArea();

	double getLength();

	boolean equals(GeometryImpl impl);

	boolean contains(GeometryImpl impl);

	boolean touches(GeometryImpl impl);

	boolean within(GeometryImpl impl);

	boolean crosses(GeometryImpl impl);

	boolean intersects(GeometryImpl impl);

	boolean disjoint(GeometryImpl impl);

	boolean overlaps(GeometryImpl impl);
	
	org.locationtech.jts.geom.Geometry getJTS();

}