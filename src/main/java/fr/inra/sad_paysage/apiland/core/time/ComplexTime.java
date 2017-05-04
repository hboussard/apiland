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
package fr.inra.sad_paysage.apiland.core.time;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

/**
 * modeling class of a complex time
 * @author H. BOUSSARD
 */
public class ComplexTime<T extends Time> extends Time implements Iterable<T> {
	
	/**
	 * version number
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * the times include
	 */
	protected List<T> times;
	
	/**
	 * is it a smooth time ?
	 */
	private boolean smooth;
	
	/**
	 * constructor
	 */
	public ComplexTime(){
		times = new ArrayList<T>();
		smooth = false;
	}
	
	/**
	 * constructor
	 * @param times list of times
	 */
	public ComplexTime(List<T> times){
		this.times = times;
		smooth = false;
	}
	
	public ComplexTime<T> clone(){
		ComplexTime<T> clone = (ComplexTime<T>)super.clone();
		clone.times = new ArrayList<T>();
		for(T t : this){
			clone.times.add((T)t.clone());
		}
		return clone;
	}
	
	/**
	 * to add a new time in the list of times
	 * with no verification
	 * @param t time to add
	 */
	protected void add(T t){
		times.add(t);
		Collections.sort(times,new ComparatorTime());
		smooth = false;
	}
	
	public T getAt(int index){
		return times.get(index);
	}
	
	protected void set(List<T> times){
		this.times = times;
		smooth = false;
	}
	
	@Override
	public Time smooth(){
		if(!smooth){
			if(times.size() > 0){
				Collections.sort(times,new ComparatorTime());
				Time t = times.get(0);
				List<Time> l = new ArrayList<Time>();
				for(int i=1; i<times.size(); i++){
					if(times.get(i).isTimeNull()){
						// do nothing
					}else if(t.intersects(times.get(i))){
						t = t.addTime(times.get(i));
					}else{
						l.add(t);
						t = times.get(i);
					}
				}
				l.add(t);
				if(l.size() == 1){
					return l.get(0);
				}
				times = (List<T>)l;
			}
			smooth = true;
		}
		return this;
	}
	
	@Override
	protected boolean isSmooth() {
		return smooth;
	}
	
	@Override
	public String toString(){
		StringBuffer sb = null;
		for(T t : times){
			if(sb == null){
				sb = new StringBuffer();
				sb.append('{');
			}else{
				sb.append(";");
			}
			sb.append(t);
		}
		sb.append('}');
		return sb.toString();
	}

	public Iterator<T> iterator() {
		return times.iterator();
	}

	public boolean isActive(Instant t) {
		for(T time : this){
			if(time.isActive(t)){
				return true;
			}
		}
		return false;
	}

	public Time killTime(Instant t){
		ComplexTime<Time> ct = new ComplexTime<Time>();
		for(T time : this){
			if(time.contains(t)){
				ct.add(time.killTime(t));
			}
			else{
				ct.add(time);
			}
		}
		return ct.smooth();
	}
	
	@Override
	public long getLength() {
		long l = 0;
		for(T t : this){
			l += t.getLength();
		}
		return l;
	}
	
	@Override
	public Instant start() {
		if(smooth){
			return times.get(0).start();
		}
		T tBegin = times.get(0);
		for(T t : this){
			if(t.isBefore(tBegin)){
				tBegin = t;
			}
		}
		return tBegin.start();
	}

	@Override
	public Instant end() {
		T tEnd = times.get(0);
		for(T t : this){
			if(t.isAfter(tEnd)){
				tEnd = t;
			}	
		}
		return tEnd.end();
	}
	
	@Override
	public void setStart(Instant t) throws TimeException {
		for(T time : this){
			if(time.isBefore(t)){
				time.setStart(t);
			}
		}
	}

	@Override
	public void setEnd(Instant t) throws TimeException {
		for(T time : this){
			if(time.isAfter(t)){
				time.setEnd(t);
			}
		}
	}
	
	@Override
	public boolean isBefore(Time other) {
		return this.start().isBefore(other.start());
	}
	
	@Override
	public boolean isAfter(Time other) {
		return this.end().isAfter(other.end());
	}

	@Override
	public boolean equals(Time other) {
		return other.equalsComplexTime(this);
	}
	
	@Override
	public boolean equalsInstant(Instant t){
		return t.date.equals(start().date) && t.date.equals(end().date);
	}
	
	@Override
	protected boolean equalsInterval(Interval t){
		return getLength() == t.getLength() && start().equals(t.start()) && end().equals(t.end()) ;
	}
	
	@Override
	protected <TT extends Time> boolean equalsComplexTime(ComplexTime<TT> t){
		for(int i=0; i<this.times.size(); i++){
			try{
				if(!this.times.get(i).equals(t.get(i))){
					return false;
				}
			}catch(IndexOutOfBoundsException ex){
				return false;
			}
		}
		return true;
	}
	
	@Override
	public boolean contains(Time other) {
		return other.withinComplexTime(this);
	}
	
	@Override
	protected boolean containsInterval(Interval t){
		for(T time : this){
			if(time.containsInterval(t)){
				return true;
			}
		}
		return false;
	}
	
	@Override
	protected <TT extends Time> boolean containsComplexTime(ComplexTime<TT> t){
		return t.withinComplexTime(this);
	}
	
	@Override
	public boolean within(Time other) {
		return other.containsComplexTime(this);
	}
	
	@Override
	protected boolean withinInstant(Instant t){
		return false;
	}

	@Override
	protected <TT extends Time> boolean withinComplexTime(ComplexTime<TT> t) {
		for(T time : this){
			if(!time.withinComplexTime(t)){
				return false; 
			}
		}
		return true;
	}
	
	@Override
	public boolean intersects(Time other) {
		for(T t : this){
			if(t.intersects(other)){
				return true;
			}
		}
		return false;
	}
	
	@Override
	protected boolean intersectInterval(Interval t) {
		return this.intersects(t);
	}
	
	@Override
	public boolean touches(Time other) {
		return other.touchesComplexTime(this);
	}

	@Override
	protected boolean touchesInstant(Instant t) {
		for(T time : this){
			if(time.touchesInstant(t)){
				return true;
			}
		}
		return false;
	}

	@Override
	protected boolean touchesInterval(Interval t) {
		boolean isTouche = false;
		for(T time : this){
			if(time.intersects(t)){
				if(time.touchesInterval(t)){
					isTouche = true;
				}
				return false;
			}
		}
		return isTouche;
	}
	
	@Override
	protected <TT extends Time> boolean touchesComplexTime(ComplexTime<TT> t){
		boolean isTouche = false;
		for(T time : this){
			if(time.intersects(t)){
				if(time.touches(t)){
					isTouche = true;
				}
				return false;
			}
		}
		return isTouche;
	}
	
	@Override
	public boolean crosses(Time other) {
		return other.crossesComplexTime(this);
	}

	@Override
	protected boolean crossesInstant(Instant t) {
		return this.isActive(t) && this.getLength() != 0;
	}

	@Override
	protected boolean crossesInterval(Interval t) {
		for(T time : this){
			if(time.crossesInterval(t)){
				return true;
			}else if(time.equals(t) && this.times.size()>1){
				return true;
			}
		}
		return false;
	}
	
	@Override
	protected <TT extends Time> boolean crossesComplexTime(ComplexTime<TT> t){
		boolean equals = false;
		boolean disjoint = false;
		boolean notouches;
		for(T time : this){
			notouches = true;
			for(TT tt : t){
				if(time.crosses(tt)){
					return true;
				}else if(time.equals(tt)){
					if(this.times.size() != t.times.size()){
						return true;
					}
					equals = true;
					notouches = false;
				}
			}
			if(notouches){
				disjoint = true;
			}
		}
		if(equals && disjoint){
			return true;
		}
		return false;
	}
	
	@Override
	public boolean disjoint(Time other) {
		for(T t : this){
			if(!t.disjoint(other)){
				return false;
			}
		}
		return true;
	}

	@Override
	protected boolean disjointInterval(Interval t) {
		return this.disjoint(t);
	}	

	@Override
	public boolean overlaps(Time other) {
		return other.overlapsComplexTime(this);
	}

	@Override
	protected boolean overlapsInstant(Instant t) {
		return false;
	}

	@Override
	protected boolean overlapsInterval(Interval t) {
		return false;
	}

	@Override
	public <TT extends Time> boolean overlapsComplexTime(ComplexTime<TT> t){
		int n = 0;
		for(T time : this){
			if(time.intersects(t)){
				n++;
			}
		}
		if(n >= 2){
			return true;
		}
		n = 0;
		for(TT time : t){
			if(time.intersects(this)){
				n++;
			}
		}
		if(n >= 2){
			return true;
		}
		return false;
	}
	
	@Override
	public Time addTime(Time t) {
		return t.addComplexTime(this);
	}
	
	@Override
	protected Time addInstant(Instant t) {
		if(this.isActive(t)){
			return this.clone();
		}
		ComplexTime<T> ct = this.clone();
		ct.add((T)t);
		return ct.smooth();
	}

	@Override
	protected Time addInterval(Interval t) {
		if(this.contains(t)){	
			return this.clone();
		}
		ComplexTime<T> ct = this.clone();
		ct.add((T)t);
		return ct.smooth();
	}
	
	@Override
	protected <TT extends Time> Time addComplexTime(ComplexTime<TT> t) {
		ComplexTime<T> ct = this.clone();
		for(TT time : t){
			ct.add((T)time);
		}
		return ct.smooth();
	}

	@Override
	protected Time addMultiInstant(MultiInstant t) {
		return t.addComplexTime(this);
	}

	@Override
	protected Time addMultiInterval(MultiInterval t) {
		return t.addComplexTime(this);
	}

	@Override
	protected <T extends Time> Time deleteComplexTime(ComplexTime<T> t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Time deleteInstant(Instant t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Time deleteInterval(Interval t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Time deleteMultiInstant(MultiInstant t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Time deleteMultiInterval(MultiInterval t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Time deleteTime(Time t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean isTimeNull() {
		for(Time t : times){
			if(!t.isTimeNull()){
				return false;
			}
		}
		return true;
	}
	

	
}
