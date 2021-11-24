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

import fr.inra.sad.bagap.apiland.core.change.Changeable;
import fr.inra.sad.bagap.apiland.core.change.ChangeableObserver;
import fr.inra.sad.bagap.apiland.core.composition.Attribute;
import fr.inra.sad.bagap.apiland.core.composition.Composable;
import fr.inra.sad.bagap.apiland.core.element.type.DynamicElementType;
import fr.inra.sad.bagap.apiland.core.space.Geometry;
import fr.inra.sad.bagap.apiland.core.space.Point;
import fr.inra.sad.bagap.apiland.core.structure.Structurable;
import fr.inra.sad.bagap.apiland.core.time.Instant;
import fr.inra.sad.bagap.apiland.core.time.Temporal;

/**
 * modeling interface of a dynamic element
 * @author H. BOUSSARD
 */
public interface DynamicElement	extends Cloneable, Comparable<DynamicElement>, Temporal, Changeable, ChangeableObserver, Structurable, Composable {
	
	/**
	 * to get the identifying
	 * @return the identifying
	 */
	String getId();
	
	String getInheritedId(String idName);
	
	/**
	 * to set the identifying
	 * @param id the identifying
	 */
	void setId(String id);

	/**
	 * to get the area of the dynamic element
	 * at the given instant
	 * @param t the given instant
	 * @return the area
	 */
	double getArea(Instant t);
	
	/**
	 * to get the length of the dynamic element
	 * at the given instant
	 * @param t the given instant
	 * @return the length
	 */
	double getLength(Instant t);
	
	/**
	 * to get the geometry of the dynamic element
	 * at the given instant
	 * @param t the given instant
	 * @return the geometry
	 */
	Geometry getGeometry(Instant t);
	
	/**
	 * to know if the dynamic element is active
	 * at the given instant and the given point
	 * @param t the given instant
	 * @param g the given point
	 * @return true if active
	 */
	boolean isActive(Instant t, Point g);
	
	/**
	 * to get the number of active element 
	 * at the given instant  
	 * @param t the given instant
	 * @return the number
	 */
	int count(Instant t);
	
	int count(Instant t, DynamicElementType... types);
	
	void setType(DynamicElementType type);
	
	/**
	 * to get the element type
	 * @return the element type
	 */
	DynamicElementType getType();
	
	/**
	 * to get the aggregate layer
	 * if exists 
	 * @return the aggregate layer
	 */
	DynamicLayer<?> getLayer();

	/**
	 * to set the aggregate layer
	 * @param layer
	 */
	void setLayer(DynamicLayer<?> layer);
	
	/**
	 * to clone the dyanmic element
	 * @return
	 */
	@Override
	DynamicElement clone();
	
	/**
	 * to display the dynamic element
	 */
	void display();
	
	/**
	 * to get a inherited attribute
	 * @param attName the attribute name
	 * @return the attribute
	 */
	Attribute getInheritedAttribute(String attName);
	
	/**
	 * to delete properly the element
	 */
	void delete();

	
}
