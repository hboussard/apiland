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

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import fr.inra.sad_paysage.apiland.core.space.impl.GeometryImpl;

public final class RasterComposite extends Raster {

	/**version number */
	private static final long serialVersionUID = 1L;

	private Set<Raster> rasters;
	
	public RasterComposite(){
		this.rasters = new HashSet<Raster>();
		smooth = true;
	}
	
	public RasterComposite(Set<Raster> rasters){
		this.rasters = new HashSet<Raster>();
		this.rasters.addAll(rasters);
	}
	
	public void addSimplePixelComposite(PixelComposite pc){
		rasters.add(pc);
	}
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append('{');
		sb.append('\n');
		int index = 0;
		for(Raster p : rasters){
			sb.append(++index);
			sb.append(" : ");
			sb.append(p.size());
			sb.append('\n');
		}
		sb.append('}');
		return sb.toString();
	}
	
	public Set<Raster> getRasters() {
		return rasters;
	}

	public boolean isSmooth() {
		return smooth;
	}
	
	private Set<Raster> lissage(Set<Raster> rs){
		for(Raster r1 : rs){
			for(Raster r2 : rs){
				if(r1.touches(r2)){
					rs.remove(r1);
					rs.remove(r2);
					rs.add(r1.addGeometryImpl(r2));
					return lissage(rs);
				}	
			}
		}
		return rs;
	}
	
	public Raster smooth() {
		if(!smooth){
			if(rasters.size() > 0){
				rasters = lissage(rasters);
				if(rasters.size() == 1){
					return rasters.iterator().next();
				}
			}
			smooth = true;
		}
		return this;
	}
	
	public RasterComposite clone(){
		RasterComposite clone = (RasterComposite)super.clone();
		clone.rasters = new HashSet<Raster>();
		for(Raster r : this.rasters){
			clone.rasters.add(r.clone());
		}
		return clone;
	}
	
	protected void add(Raster r){
		rasters.add(r);
		smooth = false;
	}
	
	public void remove(Raster r){
		rasters.remove(r);
		smooth = false;
	}
	
	protected void addAll(Collection<Raster> c){
		rasters.addAll(c);
		smooth = false;
	}
	
	protected void removeAll(Collection<Raster> c){
		rasters.removeAll(c);
		smooth = false;
	}
	
	@Override
	public Raster addGeometryImpl(GeometryImpl impl){
		try{
			return ((Raster)impl).addRasterComposite(this);
		}catch(Exception ex){
			return this;
		}
	}
	
	@Override
	protected Raster addPixel(Pixel impl) {
		if(rasters.size() == 0){
			return impl;
		}
		if(containsPixel(impl)){
			return this;
		}
		Iterator<Raster> ite = rasters.iterator();
		Raster r;
		while(ite.hasNext()){
			r = ite.next();
			if(r.touchesPixelWithoutContainsTest(impl)){
				ite.remove();
				rasters.add(r.addGeometryImpl(impl));
				smooth = false;
				return this;
			}
		}
		add(impl);
		return this;
	}

	@Override
	protected Raster addPixelComposite(PixelComposite impl) {
		if(rasters.size() == 0){
			
			return impl;
		}
		if(impl.count() == 0){
			return clone().smooth();
		}
		/*
		System.out.println("a2");
		if(intersects(impl)){
			System.out.println("ajout3");
			if(contains(impl)){
				return clone().smooth();
			}
			if(within(impl)){
				return impl.clone().smooth();
			}
			RasterComposite rc = clone();
			rc.add(impl);
			return rc.smooth();
		}
		System.out.println("a3");
		if(touches(impl)){
			System.out.println("ajout4");
			RasterComposite rc = clone();
			rc.add(impl);
			return rc.smooth();
		}
		*/
		RasterComposite rc = new RasterComposite();
		rc.add(this);
		rc.add(impl);
		//return rc.smooth();
		return rc;
	}

	@Override
	protected Raster addRasterComposite(RasterComposite impl) {
		if(rasters.size() == 0){
			return impl.clone().smooth();
		}
		if(impl.count() == 0){
			return clone().smooth();
		}
		if(intersects(impl)){
			if(contains(impl)){
				return clone().smooth();
			}
			if(within(impl)){
				return impl.clone().smooth();
			}
			RasterComposite rc = clone();
			rc.add(impl);
			return rc.smooth();
		}
		if(touches(impl)){
			RasterComposite rc = clone();
			rc.add(impl);
			return rc.smooth();
		}
		RasterComposite rc = new RasterComposite();
		rc.add(this);
		rc.add(impl);
		return rc.smooth();
	}	

	@Override
	public Iterator<Pixel> iterator() {
		return new PixelIterator(rasters.iterator());
	}
	
	@Override
	public Iterator<Pixel> getBoundaries(){
		return new BoundaryIterator(this);
	}
	
	@Override
	public Iterator<Pixel> getMargins(){
		return new MarginIterator(this);
	}
	
	@Override
	public int count(){
		if(!smooth){
			Raster r = this.smooth();
			return r.count();
		}
		int count = 0;
		for(Raster r : rasters){
			count += r.count();
		}
		return count;
	}
	
	@Override
	public int size(){
		return rasters.size();
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
			return ((Raster)impl).equalsRasterComposite(this);
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
	public boolean contains(GeometryImpl impl){
		try{
			return ((Raster)impl).withinRasterComposite(this);
		}catch(Exception ex){
			return false;
		}
	}
	
	@Override
	protected boolean containsPixel(Pixel impl) {
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
		for(Pixel p : impl){
			if(!containsPixel(p)){
				return false;
			}
		}
		return true;
	}
	
	@Override
	public boolean crosses(GeometryImpl impl) {
		try{
			return ((Raster)impl).crossesRasterComposite(this);
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
	public boolean touches(GeometryImpl impl){
		try{
			return ((Raster)impl).touchesRasterComposite(this);
		}catch(Exception ex){
			return false;
		}
	}
	
	@Override
	protected boolean touchesPixel(Pixel impl) {
		boolean touche = false;
		for(Raster r : rasters){
			if(r.containsPixel(impl)){
				return false;
			}
			if(r.touchesPixel(impl)){
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
		boolean response = false;
		for(Pixel p1 : this){
			for(Pixel p2 : impl){
				if(p1.equalsPixel(p2)){
					return false;
				}
				if(p1.touchesPixel(p2)){
					response = true;
				}
			}
		}
		return response;
		/*
		if(disjointPixelComposite(impl)){
			for(Pixel p : this){
				if(impl.touchesPixel(p)){
					return true;
				}
			}
		}
		return false;
		*/
	}

	@Override
	protected boolean touchesRasterComposite(RasterComposite impl) {
		if(disjointRasterComposite(impl)){
			for(Pixel p : this){
				if(impl.touchesPixel(p)){
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean within(GeometryImpl impl){
		try{
			return ((Raster)impl).containsRasterComposite(this);
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
		for(Pixel p : this){
			if(!impl.containsPixel(p)){
				return false;
			}
		}
		return true;
	}
	
	@Override
	public boolean intersects(GeometryImpl impl){
		try{
			return ((Raster)impl).intersectsRasterComposite(this);
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
		for(Pixel p : this){
			if(impl.containsPixel(p)){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean disjoint(GeometryImpl impl){
		try{
			return ((Raster)impl).disjointRasterComposite(this);
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
		for(Pixel p : this){
			if(impl.containsPixel(p)){
				return false;
			}
		}
		return true;
	}
	
	@Override
	public boolean overlaps(GeometryImpl impl){
		try{
			return ((Raster)impl).overlapsRasterComposite(this);
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
		// TODO Auto-generated method stub
		return false;
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
		return rasters.iterator().next().getOne();
	}
	
	
}
