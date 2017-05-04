package fr.inra.sad_paysage.apiland.core.time.period;

import java.io.Serializable;

import fr.inra.sad_paysage.apiland.core.time.Instant;

public interface Periodic extends Serializable{

	/**
	 * test if the periodic object is active at a precise instant
	 * @param t the precise instant
	 */
	boolean isActive(Instant t);
	
	Moment getPeriod();
	
}
