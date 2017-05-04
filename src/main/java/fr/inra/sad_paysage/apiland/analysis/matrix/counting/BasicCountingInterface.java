package fr.inra.sad_paysage.apiland.analysis.matrix.counting;

public interface BasicCountingInterface {

	int theoricalSize();
	
	/**
	 * the total count of values
	 * @return the total count of values
	 */
	int totalValues();
	
	/**
	 * the total count of valid values
	 * a valid value is different to Raster.noDataValue()
	 * @return the total count of valid values
	 */
	int validValues();
	
	int countValues();
	
}
