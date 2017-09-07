package fr.inra.sad.bagap.apiland.core.time.period;

import java.util.StringTokenizer;

import fr.inra.sad.bagap.apiland.core.time.Instant;
import fr.inra.sad.bagap.apiland.core.time.Interval;
import fr.inra.sad.bagap.apiland.core.time.Time;

/**
 * modeling class of a period
 * @author Hugues BOUSSARD
 */
public class Period extends Moment implements Comparable<Period> {
	
	private static final long serialVersionUID = 1L;

	private Moment start, end;
	
	protected Period(){}
	
	public Period(int startDay, Month startMonth, int endDay, Month endMonth){
		this.start = new Date(startDay, startMonth);
		this.end = new Date(endDay, endMonth);
	}
	
	public Period(Interval inter){
		this.start = new Date(inter.start());
		this.end = new Date(inter.end());
	}
	
	public Period(int startDay, int startMonth, int endDay, int endMonth){
		this.start = new Date(startDay, startMonth);
		this.end = new Date(endDay, endMonth);
	}
	
	public Period(Moment start, Moment end){
		this.start = start;
		this.end = end;
	}
	
	public Period(int startDay, int startMonth, Moment end){
		this.start = new Date(startDay, startMonth);
		this.end = end;
	}
	
	public Period(Moment start, int endDay, int endMonth){
		this.start = start;
		this.end = new Date(endDay, endMonth);
	}
	
	public Period(String period){
		StringTokenizer st = new StringTokenizer(period, "-");
		this.start = new Date(st.nextToken());
		this.end = new Date(st.nextToken());
	}
	
	@Override
	public String toString(){
		return start+"-"+end;
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof Period){
			if(start.equals(((Period) o).start())
					&& end.equals(((Period) o).end())){
				return true;
			}
		}
		return false;
	}
	
	public int getDayCount(boolean bissextile){
		if(end.end().getMonth()<start.start().getMonth()){
			return 365 - (start.start().getDayOfYear(bissextile)-end.end().getDayOfYear(bissextile));
		}else{
			return end.end().getDayOfYear(bissextile)-start.start().getDayOfYear(bissextile);
		}
	}
	
	/**
	 * test if a period is before a precise instant
	 * as part of a larger period
	 * @param instant : the precise instant
	 * @param period : the larger period
	 * @return true if the period is before the instant
	 */
	public boolean isBefore(Instant instant, Period period){
		Interval iPeriod = period.getActiveInterval(instant);
		Interval iCurent = this.getActiveInterval(iPeriod);
		return iCurent.isBefore(instant);
	}
	
	/**
	 * test if a period is after a precise instant
	 * as part of a larger period 
	 * @param instant : the precise instant
	 * @param period : the larger period
	 * @return true if the period is after the instant
	 */
	public boolean isAfter(Instant instant, Period period){
		Interval iPeriod = period.getActiveInterval(instant);
		Interval iCurent = this.getActiveInterval(iPeriod);
		return iCurent.isAfter(instant);
	}
	
	/**
	 * test if if the period contains a precise instant
	 * as part of a larger period
	 * @param instant : the precise instant
	 * @param period : the larger period
	 * @return true if the period contains the instant
	 */
	public boolean contains(Instant instant, Period period){
		Interval iPeriod = period.getActiveInterval(instant);
		Interval iCurent = this.getActiveInterval(iPeriod);
		return iCurent.isActive(instant);
	}
	
	/**
	 * to get the active interval at a precise instant
	 * @param t : the precise instant
	 * @return the active interval
	 */
	public Interval getActiveInterval(Instant t){
		Interval interval = null;
		Instant iBegin = new Instant(start().getDay(),start().getMonth(),Time.getYear(t));
		Instant iEnd = new Instant(end().getDay(),end().getMonth(),Time.getYear(t));
		if(iBegin.isBefore(iEnd)){
			interval = new Interval(iBegin,iEnd);
			if(interval.isActive(t)){
				return interval;
			}
		}else{
			Instant iEnd2 = new Instant(end().getDay(),end().getMonth(),Time.getYear(t)+1);
			interval = new Interval(iBegin,iEnd2);
			if(interval.isActive(t)){
				return interval;
			}else{
				iBegin = new Instant(start().getDay(),start().getMonth(),Time.getYear(t)-1);
				interval = new Interval(iBegin,iEnd);
				if(interval.isActive(t)){
					return interval;
				}
			}
		}
		return null;
	}
	
	/**
	 * to get the active interval in a larger interval
	 * @param t : the larger interval
	 * @return the active interval
	 */
	public Interval getActiveInterval(Interval t){
		Interval aInterval = null;
		if(Time.getYear(t.start()) == Time.getYear(t.end())){
			Instant iBegin = new Instant(start().getDay(),start().getMonth(),Time.getYear(t.start()));
			Instant iEnd = new Instant(end().getDay(),end().getMonth(),Time.getYear(t.end()));
			if(iBegin.isBefore(iEnd)){
				aInterval = new Interval(iBegin,iEnd);
				if(aInterval.within(t)){
					return aInterval;
				}
			}
		}else{
			Instant iBegin = new Instant(start().getDay(),start().getMonth(),Time.getYear(t.start()));
			Instant iEnd = new Instant(end().getDay(),end().getMonth(),Time.getYear(t.start()));
			if(iBegin.isBefore(iEnd)){
				aInterval = new Interval(iBegin,iEnd);
				if(aInterval.within(t)){
					return aInterval;
				}else{
					iBegin = new Instant(start().getDay(),start().getMonth(),Time.getYear(t.end()));
					iEnd = new Instant(start().getDay(),start().getMonth(),Time.getYear(t.end()));
					aInterval = new Interval(iBegin,iEnd);
					if(aInterval.within(t)){
						return aInterval;
					}
				}
			}else{
				iEnd = new Instant(end().getDay(),end().getMonth(),Time.getYear(t.end()));
				aInterval = new Interval(iBegin,iEnd);
				if(aInterval.within(t)){
					return aInterval;
				}
			}
		}
		return null;
	}

	public Interval getActiveInterval(int year){
		if(start instanceof Date && end instanceof Date){
			if(((Date)start).isBefore((Date)end)){
				return new Interval(
						((Date)start).getDay(),
						((Date)start).getMonth(),
						year,
						((Date)end).getDay(),
						((Date)end).getMonth(),
						year);
			}else{
				return new Interval(
						((Date)start).getDay(),
						((Date)start).getMonth(),
						year,
						((Date)end).getDay(),
						((Date)end).getMonth(),
						year+1);
			}
		}
		throw new UnsupportedOperationException();
	}
	
	/**
	 * to get the beginning date
	 * @return the beginning date
	 */
	@Override
	public Date start() {
		return start.start();
	}

	/**
	 * to get the ending date
	 * @return the ending date
	 */
	@Override
	public Date end() {
		return end.end();
	}

	@Override
	public boolean isActive(Instant t) {
		if(Time.getDayOfYear(t) >= start.start().getDayOfYear(Time.isBissextile(t))){
			if(Time.getDayOfYear(t) <= start.start().getDayOfYear(Time.isBissextile(t)) + getDayCount(Time.isBissextile(t))){
				return true;
			}
		}else if(Time.getDayOfYear(t) <= end.end().getDayOfYear(Time.isBissextile(t))){
			if(Time.getDayOfYear(t) <= getDayCount(Time.isBissextile(t)) - (365 - start.start().getDayOfYear(Time.isBissextile(t)))){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public Date getBefore(boolean bissextile, int delay){
		return new Date(start().getDayOfYear(bissextile)-delay);
	}
	
	@Override
	public Date getAfter(boolean bissextile, int delay){
		return new Date(end().getDayOfYear(bissextile)+delay);
	}
	
	public static Period get(String period){
		if(Month.isMonth(period)){
			return Month.get(period);
		}else{
			StringTokenizer st = new StringTokenizer(period,"-");
			String start = st.nextToken();
			String end = st.nextToken();
			st = new StringTokenizer(start,"/");
			int sd = Integer.valueOf(st.nextToken());
			int sm = Integer.valueOf(st.nextToken());
			st = new StringTokenizer(end,"/");
			int ed = Integer.valueOf(st.nextToken());
			int em = Integer.valueOf(st.nextToken());
			return new Period(sd,sm,ed,em);
		}
	}

	@Override
	public int compareTo(Period p) {
		if(start().getDayOfYear(true) < p.start().getDayOfYear(true)){
			return -1;
		}
		if(start().getDayOfYear(true) > p.start().getDayOfYear(true)){
			return 1;
		}
		return 0;
	}

}
