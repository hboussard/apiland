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
package fr.inra.sad_paysage.apiland.core.space.impl.raster;

import java.util.Iterator;
import fr.inra.sad_paysage.apiland.core.space.Geometry;
import fr.inra.sad_paysage.apiland.core.space.impl.GeometryImpl;
import fr.inra.sad_paysage.apiland.core.space.impl.GeometryImplType;

public abstract class Raster implements GeometryImpl, Iterable<Pixel>{

	private static final long serialVersionUID = 1L;
	
	// pixel size
	protected static double size = 1;
	
	private static int noDataValue = -1;
	
	private double userData;
	
	private GeometryImplType type;
	
	public Raster(){
		this.type = GeometryImplType.RASTER;
		userData = noDataValue;
	}
	
	public double getUserData(){
		return userData;
	}
	
	public void setUserData(double userData){
		this.userData = userData;
	}
	
	@Override
	public GeometryImplType getType(){
		return type;
	}
	
	public static void setCellSize(double s){
		size = s;
	}
	
	public static double getCellSize(){
		return size;
	}
	
	public static void setNoDataValue(int ndtv){
		noDataValue = ndtv;
	}
	
	public static int getNoDataValue(){
		return noDataValue;
	}
	
	protected boolean smooth = false;
	
	@Override
	public boolean equals(Object o){
		if(!(o instanceof Raster)){
			return false;
		}
		for(Pixel p : this){
			if(!((Raster)o).containsPixel(p)){
				return false;
			}
		}
		for(Pixel p : (Raster)o){
			if(!containsPixel(p)){
				return false;
			}
		}
		return true;
	}

	@Override
	public abstract int count();
	
	public abstract int size();
	
	@Override
	public void display(){
		System.out.println(this);
	}
	
	@Override
	public void init(Geometry g){
		//g.initRaster(this);
	}
	
	@Override
	public Raster clone(){
		Raster clone = null;
		try {
			clone = (Raster)super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return clone;
	}
	
	@Override
	public abstract Raster smooth();
	
	protected void setSmooth(boolean smooth){
		this.smooth = smooth;
	}
	
	@Override
	public com.vividsolutions.jts.geom.Geometry getJTS() {
		throw new UnsupportedOperationException();
	}

	/**
	 * to get an iteration of the boundaries pixels
	 * @return iterator of the boundaries pixels
	 */
	public abstract Iterator<Pixel> getBoundaries();
	
	/**
	 * to get one pixel of the raster
	 * @return a single pixel
	 */
	public abstract Pixel getOne();
	
	/**
	 * to get an iteration of the margins pixels
	 * @return iterator of the margins pixels
	 */
	public abstract Iterator<Pixel> getMargins();
	
	protected abstract boolean equalsPixel(Pixel impl);
	
	protected abstract boolean equalsPixelComposite(PixelComposite impl);
	
	protected abstract boolean equalsRasterComposite(RasterComposite impl);
	
	protected abstract boolean containsPixel(Pixel impl);
	
	protected abstract boolean containsPixelComposite(PixelComposite impl);
	
	protected abstract boolean containsRasterComposite(RasterComposite impl);
	
	protected abstract boolean touchesPixel(Pixel impl);
	
	protected abstract boolean touchesPixelWithoutContainsTest(Pixel impl);
	
	protected abstract boolean touchesPixelComposite(PixelComposite impl);
	
	protected abstract boolean touchesRasterComposite(RasterComposite impl);
	
	protected abstract boolean withinPixel(Pixel impl);
	
	protected abstract boolean withinPixelComposite(PixelComposite impl);
	
	protected abstract boolean withinRasterComposite(RasterComposite impl);
	
	protected abstract boolean crossesPixel(Pixel impl);
	
	protected abstract boolean crossesPixelComposite(PixelComposite impl);
	
	protected abstract boolean crossesRasterComposite(RasterComposite impl);
	
	protected abstract boolean intersectsPixel(Pixel impl);
	
	protected abstract boolean intersectsPixelComposite(PixelComposite impl);
	
	protected abstract boolean intersectsRasterComposite(RasterComposite impl);
	
	protected abstract boolean disjointPixel(Pixel impl);
	
	protected abstract boolean disjointPixelComposite(PixelComposite impl);
	
	protected abstract boolean disjointRasterComposite(RasterComposite impl);
	
	protected abstract boolean overlapsPixel(Pixel impl);
	
	protected abstract boolean overlapsPixelComposite(PixelComposite impl);
	
	protected abstract boolean overlapsRasterComposite(RasterComposite impl);

	@Override
	public abstract Raster addGeometryImpl(GeometryImpl impl);
	
	protected abstract Raster addPixel(Pixel impl);
	
	protected abstract Raster addPixelComposite(PixelComposite impl);
	
	protected abstract Raster addRasterComposite(RasterComposite impl);

	
}
