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

import java.util.Date;
import java.util.GregorianCalendar;

/**
 * abstract modeling class of a time
 * @author H. BOUSSARD
 */
public abstract class Time implements Temporal, Comparable<Time>{
	
	@Override
	public Time clone(){
		try{
			return (Time)super.clone();
		}catch(CloneNotSupportedException ex){
			ex.printStackTrace();
			return null;
		}
	}
	
	/**
	 * true if the time is smooth
	 */
	protected abstract boolean isSmooth();
	
	/**
	 * to smooth a time
	 * @return a smooth time
	 */
	public abstract Time smooth();
	
	@Override
	public Time getTime() {
		return this;
	}
	
	@Override
	public void setTime(Time t){
		throw new UnsupportedOperationException();
	}
	
	/**
	 * to get the length of time validity
	 * @return a count of seconds
	 */
	public abstract long getLength();
	
	/**
	 * to get the time begin
	 * @return the begin
	 */
	public abstract Instant start();
	
	/**
	 * to get the time end 
	 * @return the end
	 */
	public abstract Instant end();
	
	/**
	 * to affect the begin of the time
	 * @param t begin time
	 */
	public abstract void setStart(Instant t) throws TimeException;
	
	/**
	 * to affect the end of the time
	 * @param t end time
	 */
	public abstract void setEnd(Instant t) throws TimeException;
	
	
	/**
	 * test if the current time is
	 * before the other time
	 * @param other an other time
	 */
	public abstract boolean isBefore(Time other);
	
	/**
	 * test if the current time is 
	 * after the other time
	 * @param other an other time
	 */
	public abstract boolean isAfter(Time other);


	@Override
	public int hashCode(){
		return 15;
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof Time){
			return equals((Time)o);
		}
		return false;
	}
	
	/**
	 * test if both times are equals
	 * @param other an other time
	 */
	public abstract boolean equals(Time other);
	
	protected abstract boolean equalsInstant(Instant t);
	
	protected abstract boolean equalsInterval(Interval t);
	
	protected abstract <T extends Time> boolean equalsComplexTime(ComplexTime<T> t);
	
	/**
	 * test if the current time contains 
	 * totally the other time
	 * @param other an other time
	 */
	public abstract boolean contains(Time other);
	
	protected abstract boolean containsInterval(Interval t);
	
	protected abstract <T extends Time> boolean containsComplexTime(ComplexTime<T> t);
	
	/**
	 * test if the current time is totally include 
	 * in the other time
	 * @param other an other time
	 */
	public abstract boolean within(Time other);
	
	protected abstract boolean withinInstant(Instant t);
	
	protected abstract <T extends Time> boolean withinComplexTime(ComplexTime<T> t);
	
	/**
	 * test if both times intersect
	 * @param other an other time
	 */
	public abstract boolean intersects(Time other);
	
	protected abstract boolean intersectInterval(Interval t);

	/**
	 * test if both times touches together
	 * @param other an other time
	 */
	public abstract boolean touches(Time other);
	
	protected abstract boolean touchesInstant(Instant t);
	
	protected abstract boolean touchesInterval(Interval t);
	
	protected abstract <T extends Time> boolean touchesComplexTime(ComplexTime<T> t);

	/**
	 * test if both times crosses together
	 * @param other an other time
	 */
	public abstract boolean crosses(Time other);
	
	protected abstract boolean crossesInstant(Instant t);
	
	protected abstract boolean crossesInterval(Interval t);
	
	protected abstract <T extends Time> boolean crossesComplexTime(ComplexTime<T> t);

	/**
	 * test if both times are totally disjoint
	 * @param other an other time
	 */
	public abstract boolean disjoint(Time other);
	
	protected abstract boolean disjointInterval(Interval t);
	
	/**
	 * test if both times overlaps
	 * @param other an other time
	 * @return true the times overlaps
	 */
	public abstract boolean overlaps(Time other);
	
	protected abstract boolean overlapsInstant(Instant t);
	
	protected abstract boolean overlapsInterval(Interval t);
	
	protected abstract <T extends Time> boolean overlapsComplexTime(ComplexTime<T> t);

	public abstract Time deleteTime(Time t);
	
	protected abstract Time deleteInstant(Instant t);
	
	protected abstract Time deleteInterval(Interval t);
	
	protected abstract <T extends Time> Time deleteComplexTime(ComplexTime<T> t);
	
	protected abstract Time deleteMultiInstant(MultiInstant t);
	
	protected abstract Time deleteMultiInterval(MultiInterval t);
	
	/**
	 * to add a time
	 * @param t time to add
	 * @return the new time
	 */
	public abstract Time addTime(Time t);
	
	/**
	 * to add an instant
	 * @param t instant to add
	 * @return new time
	 */
	protected abstract Time addInstant(Instant t);
	
	/**
	 * to add an interval
	 * @param t interval to add
	 * @return new time
	 */
	protected abstract Time addInterval(Interval t);
	
	/**
	 * to add a complex time
	 * @param t complex time to add
	 * @return new time
	 */
	protected abstract <T extends Time> Time addComplexTime(ComplexTime<T> t);
	
	/**
	 * to add a multi_instant
	 * @param t multi_instant to add
	 * @return new time
	 */
	protected abstract Time addMultiInstant(MultiInstant t);
	
	/**
	 * to add a multi_interval
	 * @param t multi_interval to add
	 * @return new time
	 */
	protected abstract Time addMultiInterval(MultiInterval t);
	
	
	///// statics functions of the class Time
	
	public static Time add(Time t1, Time t2){
		if(t1 == null){
			return t2;
		}if(t2 == null){
			return t1;
		}
		return t1.addTime(t2);
	}
	
	public static Time delete(Time t1, Time t2){
		if(t1 == null){
			return null;
		}else if(t2 == null){
			return t1;
		}else{
			return t1.deleteTime(t2);
		}
	}
	
	public static Interval addTimeToInterval(Interval target, Time src){
		if(target == null){
			return new Interval(src.start(),src.end());
		}else{
			if(src.start().isBefore(target.start())){
				if(src.end().isAfter(target.end())){
					return new Interval(src.start(),src.end());
				}else{
					return new Interval(src.start(),target.end());
				}
			}else{
				if(src.end().isAfter(target.end())){
					return new Interval(target.start(),src.end());
				}else{
					return target;
				}
			}
		}
	}
	
	public static int getDayOfMonth(Instant t){
		Timer.getCalendar().setTime(t.date);
		return Timer.getCalendar().get(GregorianCalendar.DAY_OF_MONTH);
	}
	
	public static int getDayOfYear(Instant t){
		Timer.getCalendar().setTime(t.date);
		return Timer.getCalendar().get(GregorianCalendar.DAY_OF_YEAR);
	}
	
	public static Instant getPreviousDay(Instant t){
		return new Instant(t.date.getTime() - 86400000);
	}
	
	public static Instant getNextDay(Instant t){
		return new Instant(t.date.getTime() + 86400000);
	}
	
	public static Instant getDayAfter(Instant t, long next){
		return new Instant(t.date.getTime() + (next * 86400000l));
	}
	
	public static Instant getDayBefore(Instant t, long before){
		return new Instant(t.date.getTime() - (before * 86400000l));
	}
	
	public static int getMonth(Instant t){
		Timer.getCalendar().setTime(t.date);
		return Timer.getCalendar().get(GregorianCalendar.MONTH)+1;
	}
	
	public static int getYear(Instant t){
		Timer.getCalendar().setTime(t.date);
		return Timer.getCalendar().get(GregorianCalendar.YEAR);
	}

	@Override
	public void kill(Instant t) throws TimeException {
		throw new UnsupportedOperationException();
		//setTime(kill(this,t));
	}
	
	public static Time kill(Time t1, Instant t2){
		if(t1 != null){
			return t1.killTime(t2);
		}
		return t2;
	}
	
	protected abstract Time killTime(Instant t);
	
	public static Instant get(int day, int month, int year){
		return TimeManager.get(day, month, year);
	}
	
	public static Instant get(int year){
		return TimeManager.get(1, 1, year);
	}
	
	public static Instant get(String ddMMyyyy){
		return TimeManager.get(ddMMyyyy);
	}
	
	public static Instant get(Date d){
		return TimeManager.get(d);
	}
	
	public static boolean isBissextile(Instant t){
		int y = Time.getYear(t);
		if(y%4 == 0){ // ann�es multiple de 4
			if(y%100 == 0){ // ann�es multiple de 100
				if(y%400 == 0){ // ann�es multiple de 400
					return true;
				}
				return false;
			}
			return true;
		}
		return false;
	}
	
	public static Interval getIntervalYear_N (Instant t, int year_n){
		assert year_n > 0;
		return new Interval(get(getDayOfMonth(t), getMonth(t), getYear(t) - year_n), t); 
	}
	
	protected abstract boolean isTimeNull();
	

	@Override
	public int compareTo(Time t) {
		if(this.isBefore(t)){
			return -1;
		}
		if(t.isBefore(this)){
			return 1;
		}
		return 0;
	}
	
}
