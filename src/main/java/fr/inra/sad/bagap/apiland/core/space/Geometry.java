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
package fr.inra.sad.bagap.apiland.core.space;

import fr.inra.sad.bagap.apiland.core.space.impl.GeometryImpl;

/**
 * abstract modeling class of a geometry
 * @author H. BOUSSARD
 */
public abstract class Geometry implements Spatial, GeometryType{

	private static final long serialVersionUID = 1L;

	/**
	 * the implementation
	 */
	protected GeometryImpl impl;
	
	protected Geometry(GeometryImpl impl){
		impl.init(this);
		this.impl = impl;
	}
	
	protected Geometry(){}
	
	@Override
	public Geometry clone() {
		try{
			Geometry clone = (Geometry)super.clone();
			clone.impl = this.impl.clone();
			return clone;
		}catch(CloneNotSupportedException ex){
			ex.printStackTrace();
			return null;
		}	
	}
	
	/**
	 * to get the active implementation
	 * @return the active implementation
	 */
	public GeometryImpl get(){
		return impl;
	}
	
	public double minX(){
		return get().minX();
	}
	
	public double maxX(){
		return get().maxX();
	}
	
	public double minY(){
		return get().minY();
	}
	
	public double maxY(){
		return get().maxY();
	}
	
	public abstract Geometry smooth();
	
	@Override
	public Geometry getGeometry(){
		return this;
	}
	
	@Override
	public void setGeometry(Geometry geom){
		throw new UnsupportedOperationException();
	}
	
	/**
	 * to get the area 
	 * @return the spatial object area 
	 */
	@Override
	public double getArea(){
		return get().getArea();
	}
	
	/**
	 * to get the length 
	 * @return the spatial object length 
	 */
	@Override
	public double getLength(){
		return get().getLength();
	}

	@Override
	public boolean isActive(Point g) {
		return get().contains(g.get());
	}
	
	/**
	 * test if both geometries are equals
	 * @param other an other geometry
	 * @return true if both geometries are equals
	 */
	public boolean equals(Geometry other) {
		return get().equals(other.get());
	}
	
	/**
	 * test if the current geometry contains 
	 * the other geometry
	 * @param other an other geometry
	 * @return true if the current geometry contains 
	 * the other geometry
	 */
	public boolean contains(Geometry other) {
		return get().contains(other.get());
	}
	
	/**
	 * test if both geometries touches together
	 * @param other an other geometry
	 * @return true if both geometries touches together
	 */
	public boolean touches(Geometry other) {
		return get().touches(other.get());
	}

	/**
	 * test if the current geometry is totally include 
	 * in an other geometry
	 * @param other an other geometry
	 * @return true if the current geometry is totally include 
	 * in an other geometry
	 */
	public boolean within(Geometry other) {
		return get().within(other.get());
	}

	/**
	 * test if both geometries crosses together
	 * @param other an other geometry
	 * @return true if both geometries crosses together
	 */
	public boolean crosses(Geometry other) {
		return get().crosses(other.get());
	}

	/**
	 * test if both geometries intersect together
	 * @param other an other geometry
	 * @return true if both geometries intersect together
	 */
	public boolean intersects(Geometry other) {
		return get().intersects(other.get());
	}

	/**
	 * test if both geometries have no spatial link
	 * @param other an other geometry
	 * @return true if both geometries have no spatial link
	 */
	public boolean disjoint(Geometry other) {
		return get().disjoint(other.get());
	}
	
	/**
	 * 
	 * @param other
	 * @return
	 */
	public boolean overlaps(Geometry other) {
		return get().overlaps(other.get());
	}
	
	/**
	 * to add a geometry
	 * @param g geometry to add
	 * @return the new geometry
	 */
	public abstract Geometry addGeometry(Geometry g);
	
	public abstract Geometry addPoint(Point g);
	
	public abstract Geometry addCurve(Curve g);
	
	public abstract Geometry addSurface(Surface g);
	
	public abstract <G extends Geometry> Geometry addComplexGeometry(ComplexGeometry<G> g);
	
	/////// statics functions of the class Geometry /////////
	public static Geometry add(Geometry g1, Geometry g2){
		if(g1 == null){
			return g2;
		}else{
			return g1.addGeometry(g2);
		}
	}
	
	//protected Vectorial vImpl;
	//protected Raster rImpl;
	//private boolean vectorial;
//	
//	public void initVectorial(Vectorial vImpl){
//		this.vImpl = vImpl;
//		this.vectorial = true;
//	}
//	
//	public void initRaster(Raster rImpl){
//		this.rImpl = rImpl;
//		this.vectorial = false;
//	}
//
//	/**
//	 * true if the active implementation is vectorial
//	 * @return true if the active implementation is vectorial
//	 */
//	public boolean isVectorial(){
//		return vectorial;
//	}
//	
//	/**
//	 * true if the active implementation is raster
//	 * @return true if the active implementation is raster
//	 */
//	public boolean isRaster(){
//		return !vectorial;
//	}
//	
//	/**
//	 * to affect the active implementation into raster
//	 */
//	public void setRaster(){
//		createRaster();
//		vectorial = false;
//	}
//	
//	/**
//	 * to affect the active implementation into vectorial
//	 */
//	public void setVectorial(){
//		createVectorial();
//		vectorial = true;
//	}
//	
//	/**
//	 * to get the raster implementation
//	 * (without changing the active implementation)
//	 * @return the raster implementation
//	 */
//	public Raster getRaster(){
//		createRaster();
//		return rImpl;
//	}
//	
//	/**
//	 * to get the vectorial implementation
//	 * (without changing the active implementation)
//	 * @return the vectorial implementation
//	 */
//	public Vectorial getVectorial(){
//		createVectorial();
//		return vImpl;
//	}
//	
//	/**
//	 * if not, create the raster implementation
//	 */
//	private void createRaster(){
//		if(rImpl == null){
//			rImpl = vImpl.getRaster();
//		}
//	}
//	
//	/**
//	 * if not, create the vectorial implementation
//	 */
//	private void createVectorial(){
//		if(vImpl == null){
//			vImpl = rImpl.getVectorial();
//		}
//	}
//	
}
