package fr.inra.sad_paysage.apiland.core.util;

import fr.inra.sad_paysage.apiland.core.space.impl.raster.Raster;

public class Couple3 {

	public static boolean isHomogeneous(double c){
		double min = Math.floor(c);
		return c == get(min, min);
	}
	
	public static double get(int i, int j){
		if(i == 0 || j == 0){
			return 0;
		}
		if(i == Raster.getNoDataValue() || j == Raster.getNoDataValue()){
			return Raster.getNoDataValue();
		}
		int min = Math.min(i, j);
		int max = Math.max(i, j);
		double coeff = 1;
		for(int c=0; c<Math.floor(Math.log10(max))+1; c++){
			coeff *= 0.1;
		}
		coeff = Math.round(coeff*100)/100.0;
		return min+(max*coeff);
	}
	
	public static double get(double i, double j){
		if(i == 0 || j == 0){
			return 0.0;
		}
		if(i == Raster.getNoDataValue() || j == Raster.getNoDataValue()){
			return Raster.getNoDataValue();
		}
		double min = Math.min(i, j);
		double max = Math.max(i, j);
		double coeff = 1;
		for(int c=0; c<Math.floor(Math.log10(max))+1; c++){
			coeff *= 0.1;
		}
		coeff = Math.round(coeff*100)/100.0;
		return min+(max*coeff);
	}
	
	public static int getOne(double c){
		return new Double(Math.floor(c)).intValue();
	}
	
}
