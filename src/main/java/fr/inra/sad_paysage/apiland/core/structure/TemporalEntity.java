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
package fr.inra.sad_paysage.apiland.core.structure;

import fr.inra.sad_paysage.apiland.core.composition.TemporalValue;
import fr.inra.sad_paysage.apiland.core.space.Geometry;
import fr.inra.sad_paysage.apiland.core.space.GeometryType;
import fr.inra.sad_paysage.apiland.core.space.Point;
import fr.inra.sad_paysage.apiland.core.spacetime.SpatioTemporal;
import fr.inra.sad_paysage.apiland.core.time.Instant;
import fr.inra.sad_paysage.apiland.core.time.Time;

/**
 * modeling class of a spatio_temporal entity
 * @author H. BOUSSARD
 */
public class TemporalEntity<G extends Geometry> extends TemporalValue<G> implements SpatioTemporal {

	private static final long serialVersionUID = 1L;
	
	public TemporalEntity(Time t, G g){
		super(t,g);
	}
	
	// spatio-temporal
	public boolean equals(TemporalEntity<G> other) {
		return getValue().equals(other.getGeometry()) 
		&& getTime().equals(other.getTime());
	}
	
	@Override
	public boolean isActive(Instant i, Point p) {
		return isActive(i) && isActive(p);
	}
	
	// spatial
	@Override
	public G getGeometry() {
		return getValue();
	}
	
	@Override
	public void setGeometry(Geometry g) {
		setValue((G)g);
	}
	// end spatial

	@Override
	public TemporalEntity<G> clone(){
		TemporalEntity<G> clone = (TemporalEntity<G>) super.clone();
		return clone;
	}

	public TemporalEntity getEntity() {
		return this;
	}
	
	public void setEntity(TemporalEntity entity) {
		throw new UnsupportedOperationException();
	}

	@Override
	public double getArea() {
		return getValue().getArea();
	}

	@Override
	public boolean isActive(Point g) {
		return getValue().isActive(g);
	}

	@Override
	public double getLength() {
		return getValue().getLength();
	}

	/*
	@Override
	public double getArea(Instant t) {
		if(isActive(t)){
			return getArea();
		}
		return 0;
	}

	@Override
	public double getLength(Instant t) {
		if(isActive(t)){
			return getValue().getLength();
		}
		return 0;
	}
	*/
	
	
}
