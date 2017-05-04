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
package fr.inra.sad_paysage.apiland.core.spacetime;

import fr.inra.sad_paysage.apiland.core.space.Geometry;
import fr.inra.sad_paysage.apiland.core.space.Point;
import fr.inra.sad_paysage.apiland.core.space.Spatial;
import fr.inra.sad_paysage.apiland.core.time.Instant;
import fr.inra.sad_paysage.apiland.core.time.Temporal;

/**
 * modeling interface of a spatio-temporal object
 * @author H. BOUSSARD
 */
public interface SpatioTemporal extends Temporal, Spatial {

	/**
	 * test if the spatio-temporal object is
	 * active at the given point and the 
	 * given instant
	 * @param p the given point
	 * @param i the given instant
	 * @return true if the spatio-temporal object is
	 * active at the given point and the 
	 * given instant
	 */
	boolean isActive(Instant t, Point g);
	
	/*
	double getArea(Instant t);
	
	double getLength(Instant t);
	*/
	
	/**
	 * to get the entity 
	 * @return the entity 
	 */
	//TemporalEntity<Geometry> getEntity();
	
	/**
	 * to set an entity
	 * @param e the entity
	 */
	//void setEntity(TemporalEntity<Geometry> e);
	
}
