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


public class Surface extends Geometry implements Surfacic {

	/**
	 * version number 
	 */
	private static final long serialVersionUID = 1L;
	
	public Surface(GeometryImpl impl) {
		super(impl);
	}
	@Override
	
	public String toString(){
		return "surface  area = "+getArea();
	}

	@Override
	public Surface clone(){
		Surface clone = (Surface)super.clone();
		return clone;
	}
	
	@Override
	public Geometry smooth() {
		return this;
	}
	
	@Override
	public Geometry addGeometry(Geometry g) {
		return g.addSurface(this);
	}

	@Override
	public Geometry addPoint(Point g) {
		if(contains(g)){
			return clone();
		}
		ComplexGeometry<Geometry> cg = new ComplexGeometry<Geometry>();
		cg.add(this);
		cg.add(g);
		return cg.smooth();
	}
	
	@Override
	public Geometry addCurve(Curve g) {
		if(intersects(g)){
			if(contains(g)){
				return clone();
			}
			return new Surface(GeometryManager.add(get(), g.get()));
		}else if(touches(g)){
			return new Surface(GeometryManager.add(get(), g.get()));
		}
		ComplexGeometry<Geometry> cg = new ComplexGeometry<Geometry>();
		cg.add(this);
		cg.add(g);
		return cg.smooth();
	}

	@Override
	public Geometry addSurface(Surface g) {
		if(intersects(g)){
			if(contains(g)){
				return clone();
			}
			if(within(g)){
				return g.clone();
			}
			return new Surface(GeometryManager.add(get(), g.get()));
		}
		MultiSurface ms = new MultiSurface();
		ms.add(this);
		ms.add(g);
		return ms.smooth();
	}

	@Override
	public <G extends Geometry> Geometry addComplexGeometry(ComplexGeometry<G> g) {
		return g.addSurface(this);
	}

}