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

import fr.inra.sad_paysage.apiland.core.space.impl.GeometryImpl;

public class RasterNull extends Raster{

	private static final long serialVersionUID = 1L;


	@Override
	public int count() {
		return 0;
	}
	
	@Override
	public Iterator<Pixel> getBoundaries() {
		return new IteratorPixelNull();
	}

	@Override
	public Iterator<Pixel> getMargins() {
		return new IteratorPixelNull();
	}
	
	@Override
	public Raster smooth() {
		return this;
	}

	
	@Override
	public double getArea() {
		return 0;
	}

	@Override
	public double getLength() {
		return 0;
	}

	@Override
	public boolean isSmooth() {
		return true;
	}


	public Iterator<Pixel> iterator() {
		return new IteratorPixelNull();
	}

	@Override
	public int size() {
		return 0;
	}
	
	@Override
	public boolean equals(GeometryImpl impl){
		return false;
	}
	
	@Override
	protected boolean equalsPixel(Pixel impl) {
		return false;
	}

	@Override
	protected boolean equalsPixelComposite(PixelComposite impl) {
		return false;
	}

	@Override
	protected boolean equalsRasterComposite(RasterComposite impl) {
		return false;
	}

	@Override
	public boolean contains(GeometryImpl impl){
		return false;
	}

	@Override
	protected boolean containsPixel(Pixel impl) {
		return false;
	}

	@Override
	protected boolean containsPixelComposite(PixelComposite impl) {
		return false;
	}

	@Override
	protected boolean containsRasterComposite(RasterComposite impl) {
		return false;
	}

	@Override
	public boolean crosses(GeometryImpl impl) {
		return false;
	}

	@Override
	protected boolean crossesPixel(Pixel impl) {
		return false;
	}

	@Override
	protected boolean crossesPixelComposite(PixelComposite impl) {
		return false;
	}

	@Override
	protected boolean crossesRasterComposite(RasterComposite impl) {
		return false;
	}

	@Override
	public boolean touches(GeometryImpl impl){
		return false;
	}
	
	@Override
	protected boolean touchesPixel(Pixel impl) {
		return false;
	}

	@Override
	protected boolean touchesPixelComposite(PixelComposite impl) {
		return false;
	}

	@Override
	protected boolean touchesRasterComposite(RasterComposite impl) {
		return false;
	}
	
	@Override
	protected boolean touchesPixelWithoutContainsTest(Pixel impl) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean within(GeometryImpl impl){
		return false;
	}

	@Override
	protected boolean withinPixel(Pixel impl) {
		return false;
	}

	@Override
	protected boolean withinPixelComposite(PixelComposite impl) {
		return false;
	}

	@Override
	protected boolean withinRasterComposite(RasterComposite impl) {
		return false;
	}
	
	@Override
	public boolean intersects(GeometryImpl impl){
		return false;
	}
	
	@Override
	protected boolean intersectsPixel(Pixel impl) {
		return false;
	}

	@Override
	protected boolean intersectsPixelComposite(PixelComposite impl) {
		return false;
	}

	@Override
	protected boolean intersectsRasterComposite(RasterComposite impl) {
		return false;
	}
	
	@Override
	public boolean disjoint(GeometryImpl impl){
		return true;
	}

	@Override
	protected boolean disjointPixel(Pixel impl) {
		return true;
	}

	@Override
	protected boolean disjointPixelComposite(PixelComposite impl) {
		return true;
	}

	@Override
	protected boolean disjointRasterComposite(RasterComposite impl) {
		return true;
	}
	
	@Override
	public boolean overlaps(GeometryImpl impl){
		return false;
	}
	
	@Override
	protected boolean overlapsPixel(Pixel impl) {
		return false;
	}

	@Override
	protected boolean overlapsPixelComposite(PixelComposite impl) {
		return false;
	}

	@Override
	protected boolean overlapsRasterComposite(RasterComposite impl) {
		return false;
	}

	@Override
	public Raster addGeometryImpl(GeometryImpl impl){
		try{
			return (Raster)impl;
		}catch(Exception ex){
			return this;
		}
	}
	
	@Override
	protected Raster addPixel(Pixel impl) {
		return impl;
	}

	@Override
	protected Raster addPixelComposite(PixelComposite impl) {
		return impl;
	}

	@Override
	protected Raster addRasterComposite(RasterComposite impl) {
		return impl;
	}

	@Override
	public double minX() {
		throw new UnsupportedOperationException();
	}

	@Override
	public double maxX() {
		throw new UnsupportedOperationException();
	}

	@Override
	public double minY() {
		throw new UnsupportedOperationException();
	}

	@Override
	public double maxY() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Pixel getOne() {
		throw new UnsupportedOperationException();
	}

}
