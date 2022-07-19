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
package fr.inra.sad.bagap.apiland.core.element;

import java.util.Set;
import java.util.TreeSet;

import fr.inra.sad.bagap.apiland.core.element.type.DynamicLayerType;
import fr.inra.sad.bagap.apiland.core.space.Geometry;
import fr.inra.sad.bagap.apiland.core.space.Point;
import fr.inra.sad.bagap.apiland.core.time.Instant;
import fr.inra.sad.bagap.apiland.core.time.Time;
import fr.inra.sad.bagap.apiland.core.time.TimeException;

public class DefaultDynamicLayer<E extends DynamicElement> extends AbstractDynamicLayer<E> {

	private static final long serialVersionUID = 1L;
	
	public DefaultDynamicLayer(DynamicLayerType type){
		super(type);
	}
	
	@Override
	public DefaultDynamicLayer<E> clone(){
		DefaultDynamicLayer<E> clone = (DefaultDynamicLayer<E>)super.clone();
		return clone;
	}
	
	@Override
	public Time getTime() {
		Time t = null;
		for(DynamicElement e : this){
			t = Time.add(t,e.getTime());
		}
		return t;
	}
	
	@Override
	public void setTime(Time t) {
		// do noting
	}

	@Override
	public boolean isActive(Instant t) {
		for(DynamicElement e : this){
			if(e.isActive(t)){
				return true;
			}
		}
		return false;
	}

	@Override
	public void kill(Instant t) throws TimeException {
		for(DynamicElement e : this){
			e.kill(t);
		}
		super.kill(t);
	}

	@Override
	public double getArea(Instant t) {
		double area = 0.0;
		for(E e : this){
			if(e.isActive(t)){
				area += e.getArea(t);
			}
		}
		return area;
	}
	
	@Override
	public double getLength(Instant t) {
		double length = 0.0;
		for(E e : this){
			if(e.isActive(t)){
				length += e.getLength(t);
			}
		}
		return length;
	}

	@Override
	public Geometry getGeometry(Instant t) {
		Geometry g = null;
		for(E e : this){
			g = Geometry.add(g,e.getGeometry(t));
		}
		return g;
	}

	@Override
	public boolean isActive(Instant t, Point g) {
		for(E e : this){
			if(e.isActive(t, g)){
				return true;
			}
		}
		return false;
	}

	@Override
	public <F extends DynamicElement> Set<F> set(Class<F> theClass) {
		Set<F> set = new TreeSet<F>();
		for(E e : this){
			if(e.getClass().equals(theClass)){
				set.add((F) e);
			}
		}
		return set;
	}

}
