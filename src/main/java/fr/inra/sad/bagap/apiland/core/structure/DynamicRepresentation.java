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
package fr.inra.sad.bagap.apiland.core.structure;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fr.inra.sad.bagap.apiland.core.change.Changeable;
import fr.inra.sad.bagap.apiland.core.change.ChangeableObserver;
import fr.inra.sad.bagap.apiland.core.dynamic.SpatioDynamic;
import fr.inra.sad.bagap.apiland.core.space.Geometry;
import fr.inra.sad.bagap.apiland.core.space.GeometryFactory;
import fr.inra.sad.bagap.apiland.core.space.GeometryManager;
import fr.inra.sad.bagap.apiland.core.space.GeometryType;
import fr.inra.sad.bagap.apiland.core.space.Point;
import fr.inra.sad.bagap.apiland.core.space.impl.GeometryImplType;
import fr.inra.sad.bagap.apiland.core.spacetime.SpatioTemporal;
import fr.inra.sad.bagap.apiland.core.time.Instant;
import fr.inra.sad.bagap.apiland.core.time.Interval;
import fr.inra.sad.bagap.apiland.core.time.Time;
import fr.inra.sad.bagap.apiland.core.time.TimeException;

public class DynamicRepresentation<G extends Geometry> extends Representation<G> implements Iterable<TemporalEntity<G>>{

	private static final long serialVersionUID = 1L;
	
	/**
	 * spatio_temporal dynamic
	 */
	private SpatioDynamic<TemporalEntity<G>> dynamic;
	
	/**
	 * constructor
	 */
	public DynamicRepresentation(DynamicRepresentationType type){
		super(type);
		dynamic = new SpatioDynamic<TemporalEntity<G>>();
	}
	
	public DynamicRepresentation(String name, Class<? extends Time> temporal, Class<? extends GeometryType> spatial, GeometryImplType geometryType){
		this(new DynamicRepresentationType(name,temporal,spatial,geometryType));
	}
	
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append(getName());
		sb.append(' ');
		sb.append("temporal ");
		sb.append(getTemporalBinding());
		sb.append(" and spatial ");
		sb.append(getSpatialBinding());
		return sb.toString();
	}
	
	@Override
	public DynamicRepresentationType getType(){
		return (DynamicRepresentationType)super.getType();
	}
	
	public Class<? extends Time> getTemporalBinding(){
		return getType().getTemporalBinding();
	}
	
	@Override
	public Class<? extends GeometryType> getSpatialBinding(){
		return getType().getSpatialBinding();
	}
	
	@Override
	public Time getTime() {
		return dynamic.getTime();
	}

	@Override
	public boolean isActive(Instant t) {
		return dynamic.isActive(t);
	}

	@Override
	public void kill(Instant t) throws TimeException{
		dynamic.kill(t);
	}
	
	@Override
	public void setTime(Time t){
		dynamic.setTime(t);
	}
	
	@Override
	public void setGeometry(Time t, G g) {
		
		//if(g.getClass() instanceof getSpatialBinding())){
			if(getTemporalBinding().equals(t.getClass())){
				
				dynamic.addTemporal(new TemporalEntity<G>(t,g));
				notifyObservers(t.start(),this, g);
				return;
			}else if(getTemporalBinding().equals(Interval.class)){
				if(dynamic.getLast() == null){
					dynamic.addTemporal(new TemporalEntity<G>(new Interval((Instant)t),g));
				} else if(dynamic.getLast().getTime().start().equals(t)){
					Geometry geo = GeometryFactory.create(GeometryManager.add(dynamic.getActive(t.start()).getGeometry().get(), g.get()));
					dynamic.getActive(t.start()).setGeometry(geo);
				} else{
					try{
						dynamic.getLast().kill((Instant)t);
						dynamic.addTemporal(new TemporalEntity<G>(new Interval((Instant)t),g));
					}catch(TimeException e){
						e.printStackTrace();
					}
				}
				notifyObservers(t.start(),this, g);
				return;
			}
			throw new IllegalArgumentException("representation '"+getName()+"' expected "+getTemporalBinding()+" and not "+t.getClass());
			
		//}
		//throw new IllegalArgumentException("representation '"+getName()+"' expected "+getSpatialBinding()+" and not "+g.getClass());
		
	}
	
	@Override
	public G getGeometry(Instant t){
		for(SpatioTemporal st : this){
			if(st.isActive(t)){
				return (G)st.getGeometry();
			}
		}
		return null;
	}
	
	@Override
	public Iterator<TemporalEntity<G>> iterator() {
		return dynamic.iterator();
	}
	
	@Override
	public boolean isActive(Instant i, Point p) {
		for(SpatioTemporal st : this){
			if(st.isActive(i,p)){
				return true;
			}
		}
		return false;
	}

	@Override
	public double getArea(Instant t) {
		return dynamic.getActive(t).getGeometry().getArea();
	}
	
	@Override
	public double getLength(Instant t) {
		return dynamic.getActive(t).getGeometry().getLength();
	}

	/**
	 * to get the active time of a given value
	 * @param o the value
	 * @return the active time
	 */
	public Time getTimeForGeometry(Class<? extends Geometry> g){
		Time t = null;
		for(TemporalEntity<G> e : this){
			if(e.getGeometry().equals(g)){
				t = Time.add(t,e.getTime());
			}
		}
		return t;
	}
	
	/**
	 * to get the list of geometries
	 * @return the list of geometries
	 */
	public List<Geometry> getGeometries(){
		List<Geometry> l = new ArrayList<Geometry>();
		for(SpatioTemporal st : this){
			l.add(st.getGeometry());
		}
		return l;
	}
	
	/**
	 * to get the list of active geometries
	 * during the given interval
	 * @param t 
	 * @return a list of geometries
	 */
	public List<Geometry> getGeometries(Interval t){
		List<Geometry> l = new ArrayList<Geometry>();
		for(SpatioTemporal st : this){
			if(st.getTime().intersects(t)){
				l.add(st.getGeometry());
			}
		}
		return l;
	}
	
	/**
	 * to get the active time for a given geometry
	 * @param g the geometry
	 * @return the time
	 */
	public Time getTimeForGeometry(Geometry g){
		Time t = null;
		for(SpatioTemporal st : this){
			if(st.getGeometry().equals(g)){
				t = Time.add(t,st.getTime());
			}
		}
		return t;
	}

	@Override
	public void addObserver(ChangeableObserver o) {
		dynamic.addObserver(o);
	}

	@Override
	public void removeObserver(ChangeableObserver o) {
		dynamic.removeObserver(o);
	}

	@Override
	public void notifyObservers(Instant t, Changeable c, Object o) {
		dynamic.notifyObservers(t,c,o);
	}
	
	@Override
	public int size() {
		return dynamic.size();
	}

	@Override
	public void display() {
		// TODO Auto-generated method stub
	}

	@Override
	public void addTemporal(TemporalEntity<G> t) {
		dynamic.addTemporal(t);
	}

	@Override
	public TemporalEntity<G> get(int index) {
		return dynamic.get(index);
	}

	@Override
	public TemporalEntity<G> getActive(Instant t) {
		return dynamic.getActive(t);
	}

	@Override
	public int getIndex(Instant t) {
		return dynamic.getIndex(t);
	}

	@Override
	public TemporalEntity<G> getLast() {
		return dynamic.getLast();
	}
	
	@Override
	public Instant getLastChange() {
		return dynamic.getLastChange();
	}

	@Override
	public double minX() {
		double min = 999999999;
		for(TemporalEntity<G> e : this){
			min = Math.min(min, e.getGeometry().minX());
		}
		return min;
	}

	@Override
	public double maxX() {
		double max = -999999999;
		for(TemporalEntity<G> e : this){
			max = Math.max(max, e.getGeometry().maxX());
		}
		return max;
	}

	@Override
	public double minY() {
		double min = 999999999;
		for(TemporalEntity<G> e : this){
			min = Math.min(min, e.getGeometry().minY());
		}
		return min;
	}

	@Override
	public double maxY() {
		double max = -999999999;
		for(TemporalEntity<G> e : this){
			max = Math.max(max, e.getGeometry().maxY());
		}
		return max;
	}
	
	@Override
	public void delete() {
		super.delete();
		dynamic.delete();
		dynamic = null;
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
