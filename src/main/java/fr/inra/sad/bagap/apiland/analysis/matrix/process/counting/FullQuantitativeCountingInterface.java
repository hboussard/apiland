package fr.inra.sad.bagap.apiland.analysis.matrix.process.counting;

public interface FullQuantitativeCountingInterface {

	/** @return the maximum of internal values */
	double getMaximum();
	
	/** @return the minimum of internal values */
	double getMinimum();
	
	/** @return the whole set of values */
	//double[][] datas();
	
	/** @return the mean distance index */
	double getTruncatedAverage(double threshold);
	
}
