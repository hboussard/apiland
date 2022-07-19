package fr.inra.sad.bagap.apiland.core.time.period;

import java.io.Serializable;

import fr.inra.sad.bagap.apiland.core.time.Instant;

public interface Periodic extends Serializable{

	/**
	 * test if the periodic object is active at a precise instant
	 * @param t the precise instant
	 */
	boolean isActive(Instant t);
	
	Moment getPeriod();
	
}
