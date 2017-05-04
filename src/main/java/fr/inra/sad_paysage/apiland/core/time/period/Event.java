package fr.inra.sad_paysage.apiland.core.time.period;

import fr.inra.sad_paysage.apiland.core.time.Instant;

public class Event extends Moment{

	private static final long serialVersionUID = 1L;
	
	private String name;
	
	private Moment moment;
	
	protected Event(){}
	
	public Event(String name, Moment moment){
		this.name = name;
		this.moment = moment;
	}
	
	public Event(String name, Moment moment, int variation){
		this.name = name;
		if(variation <= 0){
			this.moment = moment;
		}else{
			this.moment = new Period(moment.getBefore(false,variation),moment.getAfter(false,variation));
		}
	}
	

	@Override
	public boolean isActive(Instant t) {
		return moment.isActive(t);
	}

	public Date getBefore(boolean bissextile, int delay){
		return new Date(start().getDayOfYear(bissextile)-delay);
	}
	
	public Date getAfter(boolean bissextile, int delay){
		return new Date(end().getDayOfYear(bissextile)+delay);
	}

	@Override
	public Date end() {
		return moment.end();
	}

	@Override
	public Date start() {
		return moment.start();
	}

}
