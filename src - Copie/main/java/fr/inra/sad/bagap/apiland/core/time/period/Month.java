package fr.inra.sad.bagap.apiland.core.time.period;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

public final class Month extends Period {

	private static final long serialVersionUID = 1L;
	
	private static final int[] dayCounts = new int[]{31,28,31,30,31,30,31,31,30,31,30,31};
	private static final int[] cumulDays = new int[]{0,31,59,90,120,151,181,212,243,273,304,334};
	
	public static final Month JANUARY = new Month(1,31,1,0,"january"); 
	public static final Month FEBRUARY = new Month(1,28,2,31,"february");
	public static final Month MARCH = new Month(1,31,3,59,"march"); 
	public static final Month APRIL = new Month(1,30,4,90,"april"); 
	public static final Month MAY = new Month(1,31,5,120,"may"); 
	public static final Month JUNE = new Month(1,30,6,151,"june");
	public static final Month JULY = new Month(1,31,7,181,"july"); 
	public static final Month AUGUST = new Month(1,31,8,212,"august");
	public static final Month SEPTEMBER = new Month(1,30,9,243,"september"); 
	public static final Month OCTOBER = new Month(1,31,10,273,"october"); 
	public static final Month NOVEMBER = new Month(1,30,11,304,"november"); 
	public static final Month DECEMBER = new Month(1,31,12,334,"december");
	
	private static final Month[] months = new Month[]{JANUARY,FEBRUARY,MARCH,APRIL,MAY,
		JUNE,JULY,AUGUST,SEPTEMBER,OCTOBER,NOVEMBER,DECEMBER};
	
	private final String name;
	
	private final int monthNumber;
	
	private final int dayCount;
	
	private final int cumulDay;
	
	private Month(int startDay, int endDay,	int monthNumber, int cumulDay, String name){
		super(startDay,monthNumber,endDay,monthNumber);
		this.monthNumber = monthNumber;
		this.dayCount = endDay;
		this.cumulDay = cumulDay;
		this.name = name;
	}
	
	public String toString(){
		return name;
	}
	
	public static Set<Period> values(){
		return new TreeSet<Period>(Arrays.asList(months));
	}
	
	public int getMonthNumber() {
		return monthNumber;
	}
	
	public int getDayCount() {
		return dayCount;
	}

	public int getCumulDay() {
		return cumulDay;
	}
	
	public static Month getMonth(int number){
		if(number <= 0 || number > 12){
			throw new IllegalArgumentException("wrong month number");
		}
		return months[number-1];
	}

	public static int getDayCount(int number) {
		if(number <= 0 || number > 12){
			throw new IllegalArgumentException("wrong month number");
		}
		return dayCounts[number-1];
	}
	
	public static int getCumulDay(boolean bissextile, int number) {
		if(number <= 0 || number > 12){
			throw new IllegalArgumentException("wrong month number");
		}
		if(bissextile && number > 2){
			return cumulDays[number-1] + 1;
		}
		return cumulDays[number-1];
	}

	public static boolean isMonth(String period) {
		for(Month m : months){
			if(m.toString().equalsIgnoreCase(period)){
				return true;
			}
		}
		return false;
	}
	
	public static Month get(String period){
		for(Month m : months){
			if(m.toString().equalsIgnoreCase(period)){
				return m;
			}
		}
		return null;
	}
	
}
