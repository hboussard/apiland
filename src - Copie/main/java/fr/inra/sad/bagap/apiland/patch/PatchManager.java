package fr.inra.sad.bagap.apiland.patch;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;

public class PatchManager {

	public static double distance(Patch pa1, Patch pa2){
		double min = Double.MAX_VALUE;
			
		for(Pixel p1 : pa1.pixels()){
			for(Pixel p2 : pa2.pixels()){
				min = Math.min(min, Pixel.distance(p1, p2));
			}
		}
		
		return min;
	}
	
	public static double distance(Patch pa1, Pixel p){
		double min = Double.MAX_VALUE;
		
		for(Pixel p1 : pa1.pixels()){
			min = Math.min(min, Pixel.distance(p1, p));
		}
		
		return min;
	}
	
}
