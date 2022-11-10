package fr.inra.sad.bagap.apiland.analysis.matrix.process.counting;

import java.util.Set;

public interface ValueCountingInterface {

	/**
	 * to get values of the map
	 * @return values of the map
	 */
	Set<Integer> values();
	
	/**
	 * to count value of type v
	 * @param v : type of value
	 * @return the count of v
	 */
	double countValue(int v);
	
	/**
	 * to count the classes
	 * @return the count of classes
	 */
	double countClass();
	
}
