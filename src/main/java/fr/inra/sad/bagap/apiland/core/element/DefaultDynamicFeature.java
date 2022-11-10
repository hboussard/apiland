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

import fr.inra.sad.bagap.apiland.core.element.type.DynamicFeatureType;
import fr.inra.sad.bagap.apiland.core.space.Geometry;
import fr.inra.sad.bagap.apiland.core.space.Point;
import fr.inra.sad.bagap.apiland.core.time.Instant;
import fr.inra.sad.bagap.apiland.core.time.Time;
import fr.inra.sad.bagap.apiland.core.time.TimeException;
import fr.inra.sad.bagap.apiland.core.time.TimeNulle;

/**
 * modeling class of the default dynamic feature implementation
 * @author H. BOUSSARD
 */
public class DefaultDynamicFeature extends AbstractDynamicElement implements DynamicFeature{

	/**
	 * version number
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * time
	 */
	private Time time;
	
	/**
	 * constructor
	 */
	public DefaultDynamicFeature(DynamicFeatureType type){
		super(type);
	}
	
	@Override
	public DynamicFeatureType getType(){
		return (DynamicFeatureType) super.getType();
	}
	
	@Override
	public DefaultDynamicFeature clone(){
		DefaultDynamicFeature clone = (DefaultDynamicFeature)super.clone();
		return clone;
	}
	
	@Override
	public Time getTime() {
		return time;
	}
	
	@Override
	public void setTime(Time t) {
		this.time = t;
		//getStructure().getDefaultRepresentation().setTime(t);
	}
	
	@Override
	public void kill(Instant t) throws TimeException{
		time = Time.kill(time,t);
		super.kill(t);
	}
	
	@Override
	public int count(Instant t) {
		if(isActive(t)){
			return 1;
		}
		return 0;
	}

	@Override
	public void display() {
		System.out.println("default dynamic feature : "+getId());
	}

	@Override
	public double getArea(Instant t) {
		return getStructure().getDefaultRepresentation().getArea(t);
	}

	@Override
	public Geometry getGeometry(Instant t) {
		return getStructure().getDefaultRepresentation().getGeometry(t);
	}

	@Override
	public double getLength(Instant t) {
		return getStructure().getDefaultRepresentation().getLength(t);
	}

	@Override
	public boolean isActive(Instant t, Point g) {
		return getStructure().getDefaultRepresentation().isActive(t);
	}

	@Override
	public boolean isActive(Instant t) {
		return time.isActive(t);
	}
	
	@Override
	public Instant getLastChange(){
		Instant t = super.getLastChange();
		Instant n = getStructure().getLastChange(); 
		if(t == null || t instanceof TimeNulle || (n != null && n.isAfter(t))){
			t = n;
		}
		return t;
	}

	@Override
	public void delete(){
		super.delete();
		time = null;
	}
	
}
