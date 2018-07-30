package fr.inra.sad.bagap.apiland.analysis.matrix.window.shape.distance;

public class DistanceFunction {

	public DistanceFunction(String function){
		
	}
	
	public double interprete(double distance){
		return 1;
	}
	
	/**
	 * to get the diameter of the window shape of the function according to the cellsize 
	 * note : the result must be odd 
	 * @param cellsize : the raster cellsize
	 * @return the diameter of the window shape of the function according to the cellsize 
	 */
	public int getDiameter(double cellsize){
		return 11;
	}
	
}
