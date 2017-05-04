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

import fr.inra.sad_paysage.apiland.core.time.delay.Delay;

/**
 * modeling class of an interval of time
 */
public class Interval extends Time {

	/**
	 * version number
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * the begin
	 */
	private Instant start;

	/**
	 * the end
	 */
	private Instant end;
	
	/**
	 * the length
	 */
	private long length = -1;
	
	/**
	 * constructor
	 * @param start instant
	 */
	public Interval(Instant start) {
		if(start instanceof TimeNulle){
			this.start = start;
			this.end = start;
			this.length = 0l;
		}else{
			this.start = start;
			this.end = Timer.future();
			this.length = this.end.date.getTime() - this.start.date.getTime();
		}
	}

	public Interval(int d, int m, int y) {
		this(Instant.get(d, m, y));
	}
	
	/**
	 * constructor
	 * @param start start instant
	 * @param end end instant
	 */
	public Interval(Instant start, Instant end) {
		if(start.isAfter(end)){
			this.start = end;
			this.end = end;
		}else{
			this.start = start;
			this.end = end;
		}
		this.length = this.end.date.getTime() - this.start.date.getTime();
	}
		
	public Interval(int sd, int sm, int sy, int ed, int em, int ey){
		this(Instant.get(sd, sm, sy),Instant.get(ed,em,ey));
	}
	
	/**
	 * constructor
	 * @param year the year
	 */
	public Interval(int year){
		this.start = new Instant(1,1,year);
		this.end = Timer.future();
		this.length = this.end.date.getTime() - this.start.date.getTime();
	}

	/**
	 * constructor
	 * @param year1 the year
	 * @param year2 the year
	 */
	public Interval(int year1, int year2){
		if(year1 <= year2){
			this.start = new Instant(1,1,year1);
			this.end = new Instant(31,12,year2);
		}else{
			this.start = new Instant(1,1,year2);
			this.end = new Instant(31,12,year1);
		}
		this.length = this.end.date.getTime() - this.start.date.getTime();
	}
	
	/**
	 * constructor
	 * @param start the start instant
	 * @param delay the delay
	 */
	public Interval(Instant start, Delay delay){
		this.start = start;
		//this.end = Instant.getPreviousDay(delay.getNext(start));
		this.end = delay.next(start);
		this.length = this.end.date.getTime() - this.start.date.getTime();
	}
	
//	@Override
//	protected List<Time> getTimes(){
//		List<Time> l = new ArrayList<Time>();
//		l.add(this);
//		return l;
//	}
	
	@Override
	public String toString(){
		return "["+start+"-"+end+"[";
	}
	
	@Override
	public Interval clone(){
		Interval clone = (Interval)super.clone();
		clone.start = (Instant)this.start.clone();
		clone.end = (Instant)this.end.clone();
		return clone;
	}
	
	protected Time killTime(Instant t){
		if(!end.isBefore(t)
				&& start.isBefore(t)){
			return new Interval(start,t);
		}else if(start.equals(t)){
			return new TimeNulle();
		}
		return this;
	}

	public boolean isActive(Instant t) {
		return start.equals(t) || (start.isBefore(t) && end.isAfter(t));
	}

	@Override
	public boolean isAfter(Time other) {
		return end.isAfter(other.end());
	}

	@Override
	public boolean isBefore(Time other) {
		return start.isBefore(other.start());
	}
	
	@Override
	public Instant start() {
		return this.start;
	}

	@Override
	public Instant end() {
		return this.end;
	}
	
	@Override
	public long getLength() {
		return length;
	}

	@Override
	public void setStart(Instant t) throws TimeException {
		if(end.isBefore(t)){
			throw new TimeException("start must be before the end");
		}
		start = t;
		this.length = this.end.date.getTime() - this.start.date.getTime();
	}
	
	@Override
	public void setEnd(Instant t) throws TimeException{
		if(start.isAfter(t)){
			throw new TimeException("start must be before the end");
		}
		end = t;
		this.length = this.end.date.getTime() - this.start.date.getTime();
	}
	
	@Override
	public boolean equals(Time other) {
		return other.equalsInterval(this);
		//return length == other.getLength() && start.equals(other.getStart()) && end.equals(other.getEnd()) ;
	}
	
	@Override
	public boolean equalsInstant(Instant t){
		return t.date.equals(start.date) && t.date.equals(end.date);
	}
	
	@Override
	protected boolean equalsInterval(Interval t){
		return length == t.length && start.equals(t.start) && end.equals(t.end) ;
	}
	
	@Override
	protected <T extends Time> boolean equalsComplexTime(ComplexTime<T> t){
		return this.equals(t);
	}
	
	@Override
	public boolean contains(Time other) {
		return this.isActive(other.start()) && 
			(this.isActive(other.end()) || this.end.equals(other.end()));
	}
	
	@Override
	protected boolean containsInterval(Interval t){
		return this.contains(t);
	}
	
	@Override
	public <T extends Time> boolean containsComplexTime(ComplexTime<T> t){
		return this.contains(t);
	}
	
	@Override
	public boolean within(Time other) {
		return other.containsInterval(this);
	}
	
	@Override
	protected boolean withinInstant(Instant t){
		return false;
	}
	
	@Override
	protected <T extends Time> boolean withinComplexTime(ComplexTime<T> t){
		return this.within(t);
	}
	
	@Override
	public boolean intersects(Time other) {
		return other.intersectInterval(this);
	}
	
	@Override
	protected boolean intersectInterval(Interval t){
		return this.isActive(t.start) || t.isActive(this.start);
	}
	
//	@Override
	public boolean touches(Time other) {
		return other.touchesInterval(this);
	}
	
	@Override
	protected boolean touchesInstant(Instant t) {
		return this.start.isActive(t) || this.end.isActive(t);
	}
	
	@Override
	protected boolean touchesInterval(Interval t) {
		return this.start.isActive(t.end) || this.end.isActive(t.start);
	}
	
	@Override
	protected <T extends Time> boolean touchesComplexTime(ComplexTime<T> t){
		return this.touches(t);
	}

	@Override
	public boolean crosses(Time other) {
		return other.crossesInterval(this);
	}

	@Override
	protected boolean crossesInstant(Instant t){
		return this.isActive(t) && this.getLength() != 0;
	}
	
	@Override
	protected boolean crossesInterval(Interval t){
		return (this.isActive(t.start) || t.isActive(this.start)) 
				&& ((!this.start.equals(t.start)) || (!this.end.equals(t.end)));
	}
	
	@Override
	protected <T extends Time> boolean crossesComplexTime(ComplexTime<T> t){
		return this.crosses(t);
	}

	@Override
	public boolean disjoint(Time other) {
		return other.disjointInterval(this);
//		if(!start.intersects(other) 
//				&& !other.getStart().intersects(this)){
//			return true;
//		}
//		return false;
	}

	@Override
	protected boolean disjointInterval(Interval t){
		return this.start.isAfter(t.end) || this.end.isBefore(t.start)
			|| (this.start.isActive(t.end) && !this.start.isActive(t.start))
			|| (this.end.isActive(t.start) && !this.end.isActive(t.end));
	}
	
	@Override
	public boolean overlaps(Time other){
		return other.overlapsInterval(this);
	}
	
	@Override
	public boolean overlapsInstant(Instant t){
		return false;
	}
	
	@Override
	public boolean overlapsInterval(Interval t){
		return this.intersectInterval(t) && !this.touchesInterval(t);
	}
	
	@Override
	public <T extends Time> boolean overlapsComplexTime(ComplexTime<T> t){
		return false;
	}
	
	@Override
	public Time addTime(Time t) {
		return t.addInterval(this);
	}
	
	@Override
	protected Time addInstant(Instant t) {
		if(this.contains(t)){
			return this.clone();
		}else if(isTimeNull()){
			return t.clone();
		}else{
			ComplexTime<Time> ct = new ComplexTime<Time>();
			ct.add(this);
			ct.add(t);
			return ct.smooth();
		}
	}
	
	@Override
	protected boolean isTimeNull() {
		return start().isTimeNull();
	}

	@Override
	protected Time addInterval(Interval t) {
		if(isTimeNull()){
			return t.clone();
		}
		if(t.isTimeNull()){
			return this.clone();
		}
		if(this.disjointInterval(t)){
			MultiInterval mi = new MultiInterval();
			mi.add(this);
			mi.add(t);
			return mi.smooth();
		}
		if(this.isBefore(t)){
			if(this.isAfter(t)){
				return this.clone();
			}
			return new Interval(this.start,t.end);
		}else{
			if(this.isAfter(t)){
				return new Interval(t.start,this.end);
			}
			return t.clone();
		}
	}

	@Override
	protected <T extends Time> Time addComplexTime(ComplexTime<T> t) {
		return t.addInterval(this);
	}

	@Override
	protected Time addMultiInstant(MultiInstant t) {
		return t.addInterval(this);
	}

	@Override
	protected Time addMultiInterval(MultiInterval t) {
		return t.addInterval(this);
	}


	/**
	 * to concat 2 intervals
	 * @param inter1 first interval
	 * @param inter2 second interval
	 * @return the concatenation of the two intervals
	 */
	public static Interval concat(Interval inter1, Interval inter2){
		Instant b;
		Instant e;
		if(inter1.isBefore(inter2)){
			b = inter1.start();
		}else{
			b = inter2.start();
		}
		if(inter1.isAfter(inter2)){
			e = inter1.end();
		}else{
			e = inter2.end();
		}
		return new Interval(b,e);
	}

	@Override
	protected boolean isSmooth() {
		return true;
	}

	@Override
	public Time smooth() {
		return this;
	}

	@Override
	protected Time deleteInstant(Instant t) {
		return this.clone();
	}

	@Override
	protected Time deleteInterval(Interval t) {
		if(this.equals(t)){
			return new TimeNulle();
		}
		if(this.intersectInterval(t)){
			if(this.containsInterval(t)){
				MultiInterval mi = new MultiInterval();
				mi.add(this);
				mi.add(t);
				return mi.smooth();
			}
			if(t.containsInterval(this)){
				return new TimeNulle();
			}
			if(this.start.isBefore(t.start())){
				return new Interval(t.end(),this.end);
			}
			return new Interval(this.start,t.start());
		}
		return this.clone();
	}

	@Override
	protected <T extends Time> Time deleteComplexTime(ComplexTime<T> t) {
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
		return t.deleteInterval(this);
	}

	public int yearInterval(){
		return end.year()-start.year();
	}

	
}
