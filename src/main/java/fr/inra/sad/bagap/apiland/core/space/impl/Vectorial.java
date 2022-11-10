/*Copyright 2010 by INRA SAD-Paysage (Rennes)

Author: Hugues BOUSSARD 
email : hugues.boussard@rennes.inra.fr

This library is a JAVA toolbox made to create and manage dynamic landscapes.

This software is governed by the CeCILL-C license under French law and
abiding by the rules of distribution of free software.  You can  use,
modify and/ or redistribute the software under the terms of the CeCILL-C
license as circulated by CEA, CNRS and INRIA at the following URL
"http://www.cecill.info".

As a counterpart to the access to the source code and rights to copy,
modify and redistribute granted by the license, users are provided only
with a limited warranty  and the software's author,  the holder of the
economic rights,  and the successive licensors  have only  limited
liability.

In this respect, the user's attention is drawn to the risks associated
with loading,  using,  modifying and/or developing or reproducing the
software by the user in light of its specific status of free software,
that may mean  that it is complicated to manipulate, and that also
therefore means  that it is reserved for developers and experienced
professionals having in-depth computer knowledge. Users are therefore
encouraged to load and test the software's suitability as regards their
requirements in conditions enabling the security of their systems and/or
data to be ensured and,  more generally, to use and operate it in the
same conditions as regards security.

The fact that you are presently reading this means that you have had
knowledge of the CeCILL-C license and that you accept its terms.
*/
package fr.inra.sad.bagap.apiland.core.space.impl;

import fr.inra.sad.bagap.apiland.core.space.Geometry;

public class Vectorial implements GeometryImpl  {

	private static final long serialVersionUID = 1L;
	
	/**
	 * the JTS geometry
	 */
	private org.locationtech.jts.geom.Geometry g;
	
	/**
	 * smooth entity ?
	 */
	private boolean smooth; 
	
	private GeometryImplType type;
	
	/**
	 * constructor
	 * @param g the JTS geometry
	 */
	public Vectorial(org.locationtech.jts.geom.Geometry g){
		this.g = g;
		this.type = GeometryImplType.VECTOR;
	}
	

	@Override
	public GeometryImplType getType(){
		return type;
	}
	
	@Override
	public void init(Geometry g){
		//g.initVectorial(this);
	}
	
	@Override
	public Vectorial clone(){
		Vectorial clone = null;
		try {
			clone = (Vectorial)super.clone();
			clone.type = this.type;
			clone.g = (org.locationtech.jts.geom.Geometry)this.g.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return clone;
	}
	
	@Override
	public void display() {
		// write something
	}
	
	@Override
	public org.locationtech.jts.geom.Geometry getJTS() {
		return g;
	}
	
	@Override
	public boolean isSmooth() {
		return smooth;
	}

	@Override
	public GeometryImpl smooth() {
		return this;
	}

	@Override
	public int count(){
		return g.getNumGeometries();
	}
	
	@Override
	public double getArea() {
		return g.getArea();
	}
	
	@Override
	public double getLength() {
		return g.getLength();
	}
	
	@Override
	public boolean equals(GeometryImpl impl) {
		try{
			return g.equals(((Vectorial)impl).g);
		}catch(Exception ex){
			return false;
		}
	}
	
	@Override
	public boolean contains(GeometryImpl impl) {
		try{
			return g.contains(((Vectorial)impl).g);
		}catch(Exception ex){
			return false;
		}
	}
	
	@Override
	public boolean crosses(GeometryImpl impl) {
		try{
			return g.crosses(((Vectorial)impl).g);
		}catch(Exception ex){
			return false;
		}
	}
	
	@Override
	public boolean disjoint(GeometryImpl impl) {
		try{
			return g.disjoint(((Vectorial)impl).g);
		}catch(Exception ex){
			return false;
		}
	}

	@Override
	public boolean intersects(GeometryImpl impl) {
		try{
			return g.intersects(((Vectorial)impl).g);
		}catch(Exception ex){
			return false;
		}
	}
	
	@Override
	public boolean overlaps(GeometryImpl impl) {
		try{
			return g.overlaps(((Vectorial)impl).g);
		}catch(Exception ex){
			return false;
		}
	}
	
	@Override
	public boolean touches(GeometryImpl impl) {
		try{
			return g.touches(((Vectorial)impl).g);
		}catch(Exception ex){
			return false;
		}
	}
	
	@Override
	public boolean within(GeometryImpl impl) {
		try{
			return g.within(((Vectorial)impl).g);
		}catch(Exception ex){
			return false;
		}
	}

	@Override
	public Vectorial addGeometryImpl(GeometryImpl impl) {
		try{
			return new Vectorial(g.union(((Vectorial)impl).g));
		}catch(Exception ex){
			throw new IllegalArgumentException();
		}
	}

	@Override
	public double minX() {
		return g.getEnvelopeInternal().getMinX();
	}

	@Override
	public double maxX() {
		return g.getEnvelopeInternal().getMaxX();
	}

	@Override
	public double minY() {
		return g.getEnvelopeInternal().getMinY();
	}

	@Override
	public double maxY() {
		return g.getEnvelopeInternal().getMaxY();
	}
	
//	public boolean equals(GeometryImpl impl) {
//		return impl.equalsVectorial(this);
//	}
//	
//	public boolean contains(GeometryImpl impl) {
//		return impl.withinVectorial(this);
//	}
//	
//	public boolean crosses(GeometryImpl impl) {
//		return impl.crossesVectorial(this);
//	}
//	
//	public boolean disjoint(GeometryImpl impl) {
//		return impl.disjointVectorial(this);
//	}
//
//	public boolean intersects(GeometryImpl impl) {
//		return impl.intersectsVectorial(this);
//	}
//	
//	public boolean overlaps(GeometryImpl impl) {
//		return impl.overlapsVectorial(this);
//	}
//	
//
//	public boolean touches(GeometryImpl impl) {
//		return impl.touchesVectorial(this);
//	}
//	
//	public boolean within(GeometryImpl impl) {
//		return impl.containsVectorial(this);
//	}
	
	
//	public Raster getRaster(){
//		return Vector2Raster.convert(this,1);
//	}
//
//	public Vectorial getVectorial() {
//		return this;
//	}
	
//	public boolean equalsRaster(Raster impl) {
//		return impl.equalsVectorial(this);
//	}
//	
//	public boolean equalsVectorial(Vectorial impl) {
//		return g.equals(impl.g);
//	}

//	public boolean withinRaster(Raster impl) {
//		return impl.containsVectorial(this);
//	}
//	
//	public boolean withinVectorial(Vectorial impl) {
//		return g.within(impl.g);
//	}
	
//	@Override
//	public Raster addRaster(Raster impl) {
//		throw new UnsupportedOperationException();
//	}
//
//	@Override
//	public Vectorial addVectorial(Vectorial impl) {
//		//GeometryCollection geo = this.g.getFactory().createGeometryCollection(new org.locationtech.jts.geom.Geometry[]{g,impl.g});
//		//Polygon g.union(impl.g) = (Polygon)g.union(impl.g);
//		return new Vectorial(g.union(impl.g));
//	}
	
//	public boolean containsRaster(Raster impl) {
//		return impl.withinVectorial(this);
//	}
//	
//	public boolean containsVectorial(Vectorial impl) {
//		return g.contains(impl.g);
//	}
	
//	public boolean crossesRaster(Raster impl) {
//		return impl.crossesVectorial(this);
//	}
//	
//	public boolean crossesVectorial(Vectorial impl) {
//		return g.crosses(impl.g);
//	}

//	public boolean disjointRaster(Raster impl) {
//		return impl.disjointVectorial(this);
//	}
//	
//	public boolean disjointVectorial(Vectorial impl) {
//		return g.disjoint(impl.g);
//	}
	
//	public boolean intersectsRaster(Raster impl) {
//		return impl.intersectsVectorial(this);
//	}
//	
//	public boolean intersectsVectorial(Vectorial impl) {
//		return g.intersects(impl.g);
//	}
	
//	public boolean touchesRaster(Raster impl) {
//		return impl.touchesVectorial(this);
//	}
//	
//	public boolean touchesVectorial(Vectorial impl) {
//		return g.touches(impl.g);
//	}


//	public boolean overlapsRaster(Raster impl) {
//		return impl.overlapsVectorial(this);
//	}
//	
//	public boolean overlapsVectorial(Vectorial impl) {
//		return g.overlaps(impl.g);
//	}
	
}
