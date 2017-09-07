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
package fr.inra.sad.bagap.apiland.core.time;

import java.util.Date;
import java.util.GregorianCalendar;

import fr.inra.sad.bagap.apiland.core.time.period.Month;

/**
 * modeling class of a instant
 * @author H. BOUSSARD
 */
public class Instant extends Time {
	
	/**
	 * version number
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * the java date
	 */
	public java.util.Date date;
	
	public static final Instant FUTUR = new Future(); 
	
	public static final Instant PAST = new Past(); 
	
	public Instant(){}
	
	/**
	 * constructor
	 * @param n the number of seconds since the 1st January 1970
	 */
	public Instant(long n) {
		this.date = new java.util.Date(n);
	}
	
	public Instant(Date d){
		this.date = d;
	}

	/**
	 * constructor
	 * @param month the month number (from 1 (January) to 12 (December))
	 * @param year the year 
	 */
	public Instant(int month, int year){
		if((month <= 0) || (month > 12)){
			throw new IllegalArgumentException();
		}
		Timer.getCalendar().set(year,month-1,1);
		this.date = Timer.getCalendar().getTime();
	}
	
	/**
	 * constructor 
	 * @param day the day in the month
	 * @param month the month number (from 1 (January) to 12 (December))
	 * @param year the year 
	 */
	public Instant(int day, int month, int year){
		int d = day;
		if((month <= 0) || (month > 12)){
			throw new IllegalArgumentException("error : month = "+month);
		}
		if((day <= 0) || (day > Month.getMonth(month).getDayCount())){
			d = Month.getMonth(month).getDayCount();
			//throw new IllegalArgumentException("error : day = "+ day+" and month = "+month);
		}
		Timer.getCalendar().set(year,month-1,d);
		this.date = Timer.getCalendar().getTime();
	}
	
//	@Override
//	protected List<Time> getTimes(){
//		List<Time> l = new ArrayList<Time>();
//		l.add(this);
//		return l;
//	}
	
	public String toString(){
		Timer.getCalendar().setTime(date);
		return Timer.getCalendar().get(GregorianCalendar.DAY_OF_MONTH)+"/"+(Timer.getCalendar().get(GregorianCalendar.MONTH)+1)+"/"+new Integer(Timer.getCalendar().get(GregorianCalendar.YEAR)).toString();
	}
	
	@Override
	public Instant clone(){
		Instant clone = (Instant)super.clone();
		if(this.date != null){
			clone.date = (java.util.Date)this.date.clone();
		}
		return clone;
	}
	
	/**
	 * to get the number of seconds since the 1st January 1970
	 * @return a number of seconds
	 */
	public long get(){
		return date.getTime(); 
	}
	
	public int dayOfMonth(){
		return Time.getDayOfMonth(this);
	}
	
	public int dayOfYear(){
		return Time.getDayOfYear(this);
	}
	
	public int month(){
		return Time.getMonth(this);
	}
	
	public int year(){
		return Time.getYear(this);
	}
	
	public boolean isActive(Instant t) {
		//return this.date.equals(t.date);
		return Math.abs(this.date.getTime() - t.date.getTime()) < 100000000;
	}
	

	protected Instant killTime(Instant t){
		if(this.equals(t)){
			return new TimeNulle();
		}
		return this;
	}
	
	@Override
	public long getLength() {
		return 0;
	}
	
	@Override
	public Instant start(){
		return this;
	}
	
	@Override
	public Instant end(){
		return this;
	}
	
	@Override
	public void setStart(Instant t) throws TimeException{
		date = t.date;
	}

	@Override
	public void setEnd(Instant t) throws TimeException{
		date = t.date;
	}
	
	@Override
	public boolean isAfter(Time other) {
		return date.after(other.end().date);
	}

	@Override
	public boolean isBefore(Time other) {
		return date.before(other.start().date);
	}
	
	@Override
	public boolean equals(Time other) {
		return other.equalsInstant(this);
		//return date.equals(other.getStart().date) && date.equals(other.getEnd().date) ;
	}
	
	@Override
	protected boolean equalsInstant(Instant t){
		return this.date.equals(t.date);
	}
	
	@Override
	protected boolean equalsInterval(Interval t){
		return date.equals(t.start().date) && date.equals(t.end().date);
	}
	
	@Override
	public int hashCode(){
		return date.hashCode();
	}
	
	@Override
	protected <T extends Time> boolean equalsComplexTime(ComplexTime<T> t){
		return this.equals(t);
	}
	
	@Override
	public boolean contains(Time other) {
		return other.withinInstant(this);
	}
	
	@Override
	protected boolean containsInterval(Interval t){
		return this.contains(t);
	}
	
	@Override
	protected <T extends Time> boolean containsComplexTime(ComplexTime<T> t){
		return this.contains(t);
	}
	
	@Override
	public boolean within(Time other) {
		return other.isActive(this);
	}
	
	@Override
	protected boolean withinInstant(Instant t){
		return this.isActive(t);
	}
	
	@Override
	protected <T extends Time> boolean withinComplexTime(ComplexTime<T> t){
		return this.within(t);
	}
	
	@Override
	public boolean intersects(Time other) {
		return other.isActive(this);
	}
	
	protected boolean intersectInterval(Interval t){
		return this.intersects(t);
	}
	
	@Override
	public boolean touches(Time other) {
		return other.touchesInstant(this);
	}
	
	@Override
	protected boolean touchesInstant(Instant t) {
		return this.isActive(t);
	}
	
	@Override
	protected boolean touchesInterval(Interval t) {
		return this.touches(t);
	}
	
	@Override
	protected <T extends Time> boolean touchesComplexTime(ComplexTime<T> t){
		return this.touches(t);
	}

	@Override
	public boolean crosses(Time other) {
		return other.crossesInstant(this);
	}
	
	@Override
	protected boolean crossesInstant(Instant t){
		return false;
	}
	
	@Override
	protected boolean crossesInterval(Interval t){
		return this.crosses(t);
	}
	
	@Override
	protected <T extends Time> boolean crossesComplexTime(ComplexTime<T> t){
		return this.crosses(t);
	}

	@Override
	public boolean disjoint(Time other) {
		return !other.isActive(this);
	}

	@Override
	protected boolean disjointInterval(Interval t){
		return this.disjoint(t);
	}
	
	@Override
	public boolean overlaps(Time other){
		return other.overlapsInstant(this);
	}
	
	@Override
	public boolean overlapsInstant(Instant t){
		return this.isActive(t);
	}
	
	@Override
	public boolean overlapsInterval(Interval t){
		return false;
	}
	
	@Override
	public <T extends Time> boolean overlapsComplexTime(ComplexTime<T> t){
		return false;
	}
	
	public Time deleteTime(Time t){
		return t.deleteInstant(this);
	}
	
	protected Time deleteInstant(Instant t){
		if(this.equals(t)){
			return new TimeNulle();
		}
		return this.clone();
	}
	
	protected Time deleteInterval(Interval t){
		return t.deleteInstant(this);
	}
	
	protected <T extends Time> Time deleteComplexTime(ComplexTime<T> t){
		return t.deleteInstant(this);
	}
	
	protected Time deleteMultiInstant(MultiInstant t){
		return t.deleteInstant(this);
	}
	
	protected Time deleteMultiInterval(MultiInterval t){
		return t.deleteInstant(this);
	}
	
	@Override
	public Time addTime(Time t) {
		return t.addInstant(this);
	}
	
	@Override
	protected Time addInstant(Instant t) {
		if(this.equals(t)){
			return this.clone();
		}
		MultiInstant mi = new MultiInstant();
		mi.add(this);
		mi.add(t);
		return mi.smooth();
	}
	
	@Override
	protected Time addInterval(Interval t) {
		return t.addInstant(this);
	}
	
	@Override
	protected <T extends Time> Time addComplexTime(ComplexTime<T> t) {
		return t.addInstant(this);
	}
	
	@Override
	protected Time addMultiInstant(MultiInstant t) {
		return t.addInstant(this);
	}

	@Override
	protected Time addMultiInterval(MultiInterval t) {
		return t.addInstant(this);
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
	protected boolean isTimeNull() {
		return false;
	}

}
