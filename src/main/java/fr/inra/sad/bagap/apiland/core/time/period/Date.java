package fr.inra.sad.bagap.apiland.core.time.period;

import java.util.StringTokenizer;

import fr.inra.sad.bagap.apiland.core.time.Instant;
import fr.inra.sad.bagap.apiland.core.time.Time;

/**
 * modeling class of an abstract date
 * @author Hugues BOUSSARD
 */
public class Date extends Moment{
	
	private static final long serialVersionUID = 1L;
	
	private int day;
	
	private int month;
	
	protected Date(){}

	public Date(Instant t){
		this(Time.getDayOfYear(t), Time.getMonth(t));
	}
	
	public Date(int dayOfYear){
		int d = dayOfYear%365;
		if(d <= 31){
			day = d;
			month = 1;
		}else if(d <= 59){
			day = d - 31;
			month = 2;
		}else if(d <= 90){
			day = d - 59;
			month = 3;
		}else if(d <= 120){
			day = d - 90;
			month = 4;
		}else if(d <= 151){
			day = d - 120;
			month = 5;
		}else if(d <= 181){
			day = d - 151;
			month = 6;
		}else if(d <= 212){
			day = d - 181;
			month = 7;
		}else if(d <= 243){
			day = d - 212;
			month = 8;
		}else if(d <= 273){
			day = d - 243;
			month = 9;
		}else if(d <= 304){
			day = d - 273;
			month = 10;
		}else if(d <= 334){
			day = d - 304;
			month = 11;
		}else{
			day = d - 334;
			month = 12;
		}
	}
	
	public Date(int day, Month month){
		if(day <= 0 || day > month.getDayCount()){
			throw new IllegalArgumentException("wrong day number");
		}
		this.day = day;
		this.month = month.getMonthNumber();
	}
	
	public Date(int day, int month){
		if(day <= 0 || day > Month.getDayCount(month)){
			throw new IllegalArgumentException("wrong day number");
		}
		this.day = day;
		this.month = month;
	}
	
	public Date(String date){
		StringTokenizer st = new StringTokenizer(date, "/");
		this.day = new Integer(st.nextToken());
		this.month = new Integer(st.nextToken());
	}

	public Instant getInstantThisYear(int year){
		return Instant.get(day, month, year);
	}
	
	@Override
	public Date getBefore(boolean bissextile, int delay){
		return new Date(getDayOfYear(bissextile)-delay);
	}
	
	@Override
	public Date getAfter(boolean bissextile, int delay){
		return new Date(getDayOfYear(bissextile)+delay);
	}
	
	@Override
	public String toString(){
		return day+"/"+month;
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof Date){
			if(day == ((Date)o).day
					&& month == ((Date) o).month){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * to get the number of the day
	 * @return the number of the day
	 */
	public int getDay() {
		return day;
	}

	/**
	 * to get the number of the month
	 * @return the number of the month
	 */
	public int getMonth() {
		return month;
	}
	
	public boolean isActive(Instant t) {
		return day==Time.getDayOfMonth(t) && month == Time.getMonth(t);
	}
	
	/**
	 * to get the day of the year
	 * @return the number of the day in the year
	 */
	public int getDayOfYear(boolean bissextile){
		return day+Month.getCumulDay(bissextile, month);
	}

	@Override
	public Date end() {
		return this;
	}

	@Override
	public Date start() {
		return this;
	}
	
	public boolean isBefore(Date date){
		if(month < date.month){
			return true;
		}else if (month > date.month){
			return false;
		}else{
			if(day < date.day){
				return true;
			}else if(day > date.day){
				return false;
			}else{
				return false;
			}
		}
	}
	
	public boolean isAfter(Date date){
		if(month > date.month){
			return true;
		}else if (month < date.month){
			return false;
		}else{
			if(day > date.day){
				return true;
			}else if(day < date.day){
				return false;
			}else{
				return false;
			}
		}
	}



}
