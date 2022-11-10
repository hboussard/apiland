package fr.inra.sad.bagap.apiland.core.time.period;

public abstract class Moment implements Periodic{

	private static final long serialVersionUID = 1L;
	
	@Override
	public Moment getPeriod() {
		return this;
	}
	
	public abstract Date getBefore(boolean bissextile, int delay);
	
	public abstract Date getAfter(boolean bissextile, int delay);
	
	public abstract Date start();
	
	public abstract Date end();
	
}
