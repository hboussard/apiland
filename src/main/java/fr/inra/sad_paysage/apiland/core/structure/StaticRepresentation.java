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
requirements in conditions enablgetSing the security of their systems and/or
data to be ensured and,  more generally, to use and operate it in the
same conditions as regards security.

The fact that you are presently reading this means that you have had
knowledge of the CeCILL-C license and that you accept its terms.
*/
package fr.inra.sad_paysage.apiland.core.structure;

import fr.inra.sad_paysage.apiland.core.change.Changeable;
import fr.inra.sad_paysage.apiland.core.change.ChangeableObserver;
import fr.inra.sad_paysage.apiland.core.composition.TemporalValue;
import fr.inra.sad_paysage.apiland.core.space.Geometry;
import fr.inra.sad_paysage.apiland.core.space.Point;
import fr.inra.sad_paysage.apiland.core.time.Instant;
import fr.inra.sad_paysage.apiland.core.time.Time;
import fr.inra.sad_paysage.apiland.core.time.TimeException;

public class StaticRepresentation<G extends Geometry> extends Representation<G> {

	private static final long serialVersionUID = 1L;
	
	private TemporalEntity<G> te;
	
	public StaticRepresentation(RepresentationType type){
		super(type);
	}
	
	@Override
	public void display() {
		// TODO Auto-generated method stub
		
	}
	
	public G getGeometry(Instant t) {
		return (G)getGeometry(t);
	}

	@Override
	public Time getTime() {
		return te.getTime();
	}

	@Override
	public boolean isActive(Instant t) {
		return te.isActive(t);
	}

	@Override
	public void kill(Instant t) throws TimeException {
		te.kill(t);
	}

	@Override
	public void setTime(Time t) {
		te.setTime(t);
	}

	public void addObserver(ChangeableObserver o) {
		// do nothing
	}

	public void removeObserver(ChangeableObserver o) {
		// do nothing
	}

	public void notifyObservers(Instant t, Changeable c, Object o) {
		// do nothing
	}

	@Override
	public boolean isActive(Instant t, Point g) {
		return te.isActive(t,g);
	}

	@Override
	public double getArea(Instant t) {
		return te.getArea();
	}

	@Override
	public double getLength(Instant t) {
		return te.getLength();
	}

	@Override
	public int size() {
		if(te != null){
			return 1;
		}
		return 0;
	}

	@Override
	public void setGeometry(Time t, G g) {
		if(te == null){
			te = new TemporalEntity(t, g);
		}else{
			te.setGeometry(g);
		}
	}

	@Override
	public void addTemporal(TemporalEntity<G> t) {
		throw new UnsupportedOperationException();
	}

	@Override
	public TemporalEntity<G> get(int index) {
		if(index == 0){
			return te;
		}
		return null;
	}

	@Override
	public TemporalEntity<G> getActive(Instant t) {
		if(te.isActive(t)){
			return te;
		}
		return null;
	}

	@Override
	public int getIndex(Instant t) {
		return 0;
	}

	@Override
	public TemporalEntity<G> getLast() {
		return te;
	}

	@Override
	public Instant getLastChange() {
		return te.getTime().start();
	}

	@Override
	public double minX() {
		return te.getGeometry().minX();
	}

	@Override
	public double maxX() {
		return te.getGeometry().maxX();
	}

	@Override
	public double minY() {
		return te.getGeometry().minY();
	}

	@Override
	public double maxY() {
		return te.getGeometry().maxY();
	}

	@Override
	public void delete(){
		super.delete();
		te.delete();
		te = null;
	}

	@Override
	public void removeTemporal(TemporalEntity<G> t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public TemporalEntity<G> getFirst() {
		// TODO Auto-generated method stub
		return null;
	}


}
