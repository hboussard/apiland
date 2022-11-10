package fr.inra.sad.bagap.apiland.analysis.matrix.process.counting;

import java.util.Set;

/**
 * modeling interface of a qualitative counting
 * @author H.Boussard
 */
public interface QualitativeCounting {
	
	/**
	 * the total count of values different to Raster.noDataValue() and 0
	 * @return the total count of values
	 */
	int countValues();
	
	/**
	 * the count of values according to a specific value
	 * @param v the specific value
	 * @return the count of values
	 */
	int countValue(int v);	
	
	/**
	 * the present values
	 * @return the present values
	 */
	Set<Integer> values();
	
	/**
	 * the total count of couples
	 * @return the total count of couples
	 */
	int totalCouples();
	
	/**
	 * the total count of valid couples
	 * a valid couple is different to Raster.noDataValue()
	 * @return the total count of valid couples
	 */
	int validCouples();
	
	/**
	 * the total count of couples different to Raster.noDataValue() and 0
	 * @return the total count of couples
	 */
	int countCouples();
	
	/**
	 * the total count of homogeneous couples
	 * @return the total count of homogeneous couples
	 */
	int homogeneousCouples();
	
	/**
	 * the toal count of unhomogeneous couples
	 * @return the total count of unhomogeneous couples
	 */
	int unhomogeneousCouples();
	
	/**
	 * the count of couples according to a specific couple
	 * @param c the specific couple
	 * @return the count of couples
	 */
	int countCouple(double c);
	
	/**
	 * the present couples
	 * @return the present couples
	 */
	Set<Double> couples();
	
}
