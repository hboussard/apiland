package fr.inra.sad.bagap.apiland.analysis.matrix.process.counting;

/**
 * modeling interface of a quantitative counting
 * @author H.Boussard
 */
public interface QuantitativeCountingInterface {

	/** @return the variance of internal values */
	double getVariance();
	
	/** @return the average of internal values */
	double getAverage();
	
	/** @return the sum of internal values */
	double getSum();
	
	/** @return the square sum of internal values */
	double getSquareSum();
	
	/** @return the standard deviation of internal values */
	double getStandardDeviation();
	
	/** @return the standard error of internal values */
	double getStandardError();
	
	/** @return the standard error of internal values */
	double getVariationCoefficient();
	
	/** @return the number of positives internal values */
	double countPositives();
	
	/** @return the number of negatives internal values */
	double countNegatives();
	
	/** @return the number of internal values */
	double size();
	
}
