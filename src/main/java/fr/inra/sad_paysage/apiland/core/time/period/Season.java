package fr.inra.sad_paysage.apiland.core.time.period;

public final class Season extends Period{

	private static final long serialVersionUID = 1L;
	
	public static final Season WINTER = new Season(21,12,20,3); 
	public static final Season SPRING = new Season(21,3,21,6);
	public static final Season SUMMER = new Season(22,6,22,9); 
	public static final Season AUTUMN = new Season(23,9,20,12); 
	
	//private static final Season[] seasons = new Season[]{WINTER,SPRING,SUMMER,AUTUMN};
	
	private Season(int startDay, int startMonth, int endDay, int endMonth){
		super(startDay,startMonth,endDay,endMonth);
	}
	
	
}