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
package fr.inra.sad.bagap.apiland.core.space.impl.raster;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import fr.inra.sad.bagap.apiland.core.space.impl.GeometryImpl;


public final class PixelComposite extends Raster implements Iterable<Pixel> {

	/**
	 * version number
	 */
	private static final long serialVersionUID = 1L;
	
	private int value;
	
	private Set<Pixel> pixels;
	
	private Set<Pixel> bounds;
	
	public PixelComposite(){
		this.pixels = new HashSet<Pixel>();
		smooth = true;
	}
	
	public PixelComposite(Set<Pixel> pixels){
		this.pixels = new HashSet<Pixel>();
		addAll(pixels);
		smooth = true;
	}
	
	public PixelComposite(Pixel pixel){
		this.pixels = new HashSet<Pixel>();
		add(pixel);
		smooth = true;
	}
	
	public PixelComposite(Pixel pixel, int value){
		this(pixel);
		this.value = value;
	}
	
	public PixelComposite(Pixel pixel, int value, Number userObject){
		this(pixel, value);
		setUserData(userObject.doubleValue());
	}
	
	public void setValue(int value){
		this.value = value;
	}
	
	public int getValue(){
		return value;
	}
	
	public void clear(){
		pixels.clear();
		smooth = true;
	}
	
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append('[');
		for(Pixel p : this){
			sb.append(p.toString()+";");
		}
		sb.append(']');
		return sb.toString();
	}
	
	@Override
	public PixelComposite clone(){
		PixelComposite clone = (PixelComposite)super.clone();
//		clone.smooth = this.smooth;
		clone.pixels = new HashSet<Pixel>();
		for(Pixel p : this.pixels){
			clone.pixels.add(p.clone());
		}
		/*
		clone.rasters = new HashSet<Raster>();
		for(Raster r : this.rasters){
			clone.rasters.add(r.clone());
		}
		*/
		return clone;
	}

	public void addSimplePixel(Pixel p){
		//rasters.add(p);
		pixels.add(p);
	}
	
	public void addAllSimplePixels(Set<Pixel> p){
		//rasters.add(p);
		pixels.addAll(p);
	}
	
	protected void add(Raster r){
		//rasters.add(r);
		for(Pixel p : r){
			pixels.add(p);
		}
		smooth = false;
	}
	
	protected void addAll(Collection<? extends Raster> c){
		//rasters.addAll(c);
		for(Raster r : c){
			for(Pixel p : r){
				pixels.add(p);
			}
		}
		smooth = false;
	}
	
	@Override
	public boolean isSmooth() {
		return smooth;
	}

	@Override
	public Raster smooth() {
		if(!smooth){
			if(pixels.size() > 0){
				if(pixels.size() == 1){
					return pixels.iterator().next().smooth();
				}else{
					Set<Pixel> pixelss = new HashSet<Pixel>();
					for(Pixel p : this){
						pixelss.add(p);
					}
					pixels = pixelss;
					if(pixels.size() == 1){
						return pixels.iterator().next();
					}
				}
			}
			smooth = true;
		}
		return this;
	}
	
	public Set<Pixel> getPixels(){
		return pixels;
	}
	
	@Override
	public Iterator<Pixel> iterator() {
		return new PixelIterator(pixels.iterator());
	}
	
	@Override
	public Iterator<Pixel> getBoundaries(){
		return new BoundaryIterator(this);
	}
	
	public Set<Pixel> getBounds(){
		if(bounds == null){
			bounds = new HashSet<Pixel>();
			Pixel p, p2;
			Iterator<Pixel> ite;
			Iterator<Pixel> iterator = iterator();
			while(iterator.hasNext()){
				p = iterator.next();
				ite = p.getMargins();
				while(ite.hasNext()){
					p2 = ite.next();
					if(!pixels.contains(p2)){
						bounds.add(p);
						break;
					}
				}
			}
		}
		return bounds;
	}
	
	@Override
	public Iterator<Pixel> getMargins(){
		return new MarginIterator(this);
	}
	
	@Override
	public int count(){
		if(!smooth){
			Raster r = smooth();
			return r.count();
		}
		return pixels.size();
	}
	
	@Override
	public int size(){
		int s = 0;
		for(Pixel p : pixels){
			s += p.size();
		}
		return s;
	}
	
	@Override
	public double getArea() {
		return count()*Math.pow(size,2);
	}
	
	@Override
	public double getLength(){
		Iterator<Pixel> ite = getBoundaries();
		double length = 0.0;
		while(ite.hasNext()){
			ite.next();
			length += size;
		}
		return length;
	}
	
	@Override
	public boolean equals(GeometryImpl impl){
		try{
			return ((Raster)impl).equalsPixelComposite(this);
		}catch(Exception ex){
			return false;
		}
	}
	
	@Override
	protected boolean equalsPixel(Pixel impl) {
		for(Pixel p : this){
			if(!p.equalsPixel(impl)){
				return false;
			}
		}
		return true;
	}
	
	@Override
	protected boolean equalsPixelComposite(PixelComposite impl) {
		if(this.getArea() == impl.getArea()){
			for(Pixel p : this){
				if(!impl.containsPixel(p)){
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	@Override
	protected boolean equalsRasterComposite(RasterComposite impl) {
		return impl.equalsPixelComposite(this);
	}
	
	@Override
	public boolean contains(GeometryImpl impl){
		try{
			return ((Raster)impl).withinPixelComposite(this);
		}catch(Exception ex){
			return false;
		}
	}
	
	@Override
	public boolean containsPixel(Pixel impl) {
		for(Pixel p : this){
			if(p.equalsPixel(impl)){
				return true;
			}
		}
		return false;
	}

	@Override
	protected boolean containsPixelComposite(PixelComposite impl) {
		for(Pixel p : impl){
			if(!containsPixel(p)){
				return false;
			}
		}
		return true;
	}
	
	@Override
	protected boolean containsRasterComposite(RasterComposite impl) {
		return impl.withinPixelComposite(this);
	}
	
	@Override
	public boolean crosses(GeometryImpl impl) {
		try{
			return ((Raster)impl).crossesPixelComposite(this);
		}catch(Exception ex){
			return false;
		}
	}
	
	@Override
	protected boolean crossesPixel(Pixel impl) {
		return false;
	}

	@Override
	protected boolean crossesPixelComposite(PixelComposite impl) {
		Iterator<Pixel> ite = this.iterator();
		if(ite.hasNext()){
			Pixel p = ite.next();
			if(impl.containsPixel(p)){
				while(ite.hasNext()){
					p = ite.next();
					if(!impl.containsPixel(p)){
						return true;
					}
				}
				return false;
			}else{
				while(ite.hasNext()){
					p = ite.next();
					if(impl.containsPixel(p)){
						return true;
					}
				}
				return false;
			}
		}
		return false;
	}
	
	@Override
	protected boolean crossesRasterComposite(RasterComposite impl) {
		return impl.crossesPixelComposite(this);
	}
	
	@Override
	public boolean touches(GeometryImpl impl){
		try{
			return ((Raster)impl).touchesPixelComposite(this);
		}catch(Exception ex){
			return false;
		}
	}
	
	@Override
	protected boolean touchesPixel(Pixel impl) {
		boolean touche = false;
		for(Pixel p : this){
			if(p.equals(impl)){
				return false;
			}
			if(p.touchesPixel(impl)){
				touche = true;
			}
		}
		return touche;
	}
	
	@Override
	protected boolean touchesPixelWithoutContainsTest(Pixel impl) {
		for(Pixel p : this){
			if(p.touchesPixel(impl)){
				return true;
			}
		}
		return false;
	}

	@Override
	protected boolean touchesPixelComposite(PixelComposite impl) {
		if(disjointPixelComposite(impl)){
			for(Pixel p : this){
				if(impl.touchesPixel(p)){
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean touchesPixelCompositeWithoutDisjointTest(PixelComposite impl) {
		for(Pixel p : this){
			if(impl.touchesPixelWithoutContainsTest(p)){
				return true;
			}
		}
		return false;
	}
	
	@Override
	protected boolean touchesRasterComposite(RasterComposite impl) {
		return impl.touchesPixelComposite(this);
	}
	
	@Override
	public boolean within(GeometryImpl impl){
		try{
			return ((Raster)impl).containsPixelComposite(this);
		}catch(Exception ex){
			return false;
		}
	}

	@Override
	protected boolean withinPixel(Pixel impl) {
		for(Pixel p : this){
			if(!impl.equalsPixel(p)){
				return false;
			}
		}
		return true;
	}

	@Override
	protected boolean withinPixelComposite(PixelComposite impl) {
		for(Pixel p : this){
			if(!impl.containsPixel(p)){
				return false;
			}
		}
		return true;
	}
	
	@Override
	protected boolean withinRasterComposite(RasterComposite impl) {
		return impl.containsPixelComposite(this);
	}	

	@Override
	public boolean intersects(GeometryImpl impl){
		try{
			return ((Raster)impl).intersectsPixelComposite(this);
		}catch(Exception ex){
			return false;
		}
	}
	
	@Override
	protected boolean intersectsPixel(Pixel impl) {
		return containsPixel(impl);
	}

	@Override
	protected boolean intersectsPixelComposite(PixelComposite impl) {
		for(Pixel p : this){
			if(impl.containsPixel(p)){
				return true;
			}
		}
		return false;
	}
	
	@Override
	protected boolean intersectsRasterComposite(RasterComposite impl) {
		return impl.intersectsPixelComposite(this);
	}

	@Override
	public boolean disjoint(GeometryImpl impl){
		try{
			return ((Raster)impl).disjointPixelComposite(this);
		}catch(Exception ex){
			return false;
		}
	}
	
	@Override
	protected boolean disjointPixel(Pixel impl) {
		return !containsPixel(impl);
	}

	@Override
	protected boolean disjointPixelComposite(PixelComposite impl) {
		for(Pixel p : this){
			if(impl.containsPixel(p)){
				return false;
			}
		}
		return true;
	}
	
	@Override
	protected boolean disjointRasterComposite(RasterComposite impl) {
		return impl.disjointPixelComposite(this);
	}

	@Override
	public boolean overlaps(GeometryImpl impl){
		try{
			return ((Raster)impl).overlapsPixelComposite(this);
		}catch(Exception ex){
			return false;
		}
	}

	@Override
	protected boolean overlapsPixel(Pixel impl) {
		for(Pixel p : this){
			if(!impl.equalsPixel(p)){
				return false;
			}
		}
		return true;
	}

	@Override
	protected boolean overlapsPixelComposite(PixelComposite impl) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	protected boolean overlapsRasterComposite(RasterComposite impl) {
		return impl.overlapsPixelComposite(this);
	}
	
	@Override
	public Raster addGeometryImpl(GeometryImpl impl){
		try{
			return ((Raster)impl).addPixelComposite(this);
		}catch(Exception ex){
			return this;
		}
	}
	
	@Override
	protected Raster addPixel(Pixel impl) {
		if(count() == 0){
			return impl;
		}
		if(containsPixel(impl)){
			return this;
		}
		if(touchesPixelWithoutContainsTest(impl)){
			add(impl);
			smooth = true;
			return this;
		}
		RasterComposite rc = new RasterComposite();
		rc.add(this);
		rc.add(impl);
		rc.setSmooth(true);
		return rc;
	}

	@Override
	protected Raster addPixelComposite(PixelComposite impl) {
		if(count() == 0){
			return impl;
		}
		if(impl.count() == 0){
			return this;
		}
		if(intersectsPixelComposite(impl)){
			if(containsPixelComposite(impl)){
				return this;
			}
			if(withinPixelComposite(impl)){
				return impl;
			}
			add(impl);
			return this.smooth();
		}
		if(touchesPixelCompositeWithoutDisjointTest(impl)){
			add(impl);
			return this.smooth();
		}
		RasterComposite rc = new RasterComposite();
		rc.add(this);
		rc.add(impl);
		rc.smooth = true;
		return rc;
	}

	@Override
	protected Raster addRasterComposite(RasterComposite impl) {
		return impl.addPixelComposite(this);
	}

	@Override
	public double minX() {
		double min = 999999999;
		for(Pixel p : this){
			min = Math.min(min, p.minX());
		}
		return min;
	}

	@Override
	public double maxX() {
		double max = -999999999;
		for(Pixel p : this){
			max = Math.max(max, p.maxX());
		}
		return max;
	}

	@Override
	public double minY() {
		double min = 999999999;
		for(Pixel p : this){
			min = Math.min(min, p.minY());
		}
		return min;
	}

	@Override
	public double maxY() {
		double max = -999999999;
		for(Pixel p : this){
			max = Math.max(max, p.maxY());
		}
		return max;
	}

	@Override
	public Pixel getOne() {
		return pixels.iterator().next();
	}

	
}
