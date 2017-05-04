package fr.inra.sad_paysage.apiland.core.time;

public class TimeNulle extends Instant{

	private static final long serialVersionUID = 1L;

	public String toString(){
		return "no-time"; 
	}

	@Override
	protected <T extends Time> Time addComplexTime(ComplexTime<T> t) {
		return t;
	}

	@Override
	protected Time addInstant(Instant t) {
		return t;
	}

	@Override
	protected Time addInterval(Interval t) {
		return t;
	}

	@Override
	protected Time addMultiInstant(MultiInstant t) {
		return t;
	}

	@Override
	protected Time addMultiInterval(MultiInterval t) {
		return t;
	}

	@Override
	public Time addTime(Time t) {
		return t;
	}

	@Override
	public boolean contains(Time other) {
		return false;
	}

	@Override
	protected <T extends Time> boolean containsComplexTime(ComplexTime<T> t) {
		return false;
	}

	@Override
	protected boolean containsInterval(Interval t) {
		return false;
	}

	@Override
	public boolean crosses(Time other) {
		return false;
	}

	@Override
	protected <T extends Time> boolean crossesComplexTime(ComplexTime<T> t) {
		return false;
	}

	@Override
	protected boolean crossesInstant(Instant t) {
		return false;
	}

	@Override
	protected boolean crossesInterval(Interval t) {
		return false;
	}

	@Override
	protected <T extends Time> Time deleteComplexTime(ComplexTime<T> t) {
		return this;
	}

	@Override
	protected Time deleteInstant(Instant t) {
		return this;
	}

	@Override
	protected Time deleteInterval(Interval t) {
		return this;
	}

	@Override
	protected Time deleteMultiInstant(MultiInstant t) {
		return this;
	}

	@Override
	protected Time deleteMultiInterval(MultiInterval t) {
		return this;
	}

	@Override
	public Time deleteTime(Time t) {
		return this;
	}

	@Override
	public boolean disjoint(Time other) {
		return true;
	}

	@Override
	protected boolean disjointInterval(Interval t) {
		return true;
	}

	@Override
	public boolean equals(Time other) {
		return false;
	}

	@Override
	protected <T extends Time> boolean equalsComplexTime(ComplexTime<T> t) {
		return false;
	}

	@Override
	public Instant start() {
		return this;
	}

	@Override
	public Instant end() {
		return this;
	}

	@Override
	public long getLength() {
		return 0;
	}

	@Override
	protected boolean intersectInterval(Interval t) {
		return false;
	}

	@Override
	public boolean intersects(Time other) {
		return false;
	}

	@Override
	public boolean isAfter(Time other) {
		return false;
	}

	@Override
	public boolean isBefore(Time other) {
		return false;
	}

	@Override
	protected boolean isSmooth() {
		return true;
	}

	@Override
	protected Instant killTime(Instant t) {
		return this;
	}

	@Override
	public boolean overlaps(Time other) {
		return false;
	}

	@Override
	public void setStart(Instant t) throws TimeException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setEnd(Instant t) throws TimeException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Time smooth() {
		return this;
	}

	@Override
	public boolean touches(Time other) {
		return false;
	}

	@Override
	protected <T extends Time> boolean touchesComplexTime(ComplexTime<T> t) {
		return false;
	}

	@Override
	protected boolean touchesInstant(Instant t) {
		return false;
	}

	@Override
	protected boolean touchesInterval(Interval t) {
		return false;
	}

	@Override
	public boolean within(Time other) {
		return false;
	}

	@Override
	protected <T extends Time> boolean withinComplexTime(ComplexTime<T> t) {
		return false;
	}

	@Override
	protected boolean withinInstant(Instant t) {
		return false;
	}

	@Override
	public boolean isActive(Instant t) {
		return false;
	}
	
	@Override
	protected boolean isTimeNull() {
		return true;
	}

}
