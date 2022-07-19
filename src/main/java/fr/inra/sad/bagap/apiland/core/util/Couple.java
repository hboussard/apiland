package fr.inra.sad.bagap.apiland.core.util;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;

public class Couple {

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
		return min+0.001*max;
	}
	
	public static double get(double i, double j){
		if(i == 0 || j == 0){
			return 0;
		}
		if(i == Raster.getNoDataValue() || j == Raster.getNoDataValue()){
			return Raster.getNoDataValue();
		}
		double min = Math.min(i, j);
		double max = Math.max(i, j);
		return min+0.001*max;
	}
	
	public static int getOne(double c){
		if(c == 0){
			throw new IllegalArgumentException();
		}
		if(c == Raster.getNoDataValue()){
			throw new IllegalArgumentException();
		}
		return new Double(Math.floor(c)).intValue();
	}
	
	public static int getOther(double c){
		if(c == 0){
			throw new IllegalArgumentException();
		}
		if(c == Raster.getNoDataValue()){
			throw new IllegalArgumentException();
		}
		return new Double(Math.round((c - Couple.getOne(c)) * 1000)).intValue();
		//return new Double((c - getOne(c)) * 1000).intValue();
	}
	
	public static int getOther(double c, int v){
		int v1 = new Double(Math.floor(c)).intValue();
		if(v1 == v){
			return new Double(Math.round((c - Couple.getOne(c)) * 1000)).intValue();		
		}
		return v1;		
	}
	
	public static boolean contains(double c, int i){
		if(new Double(Math.floor(c)).intValue() == i){
			return true;
		}
		if(new Double(Math.round((c - Couple.getOne(c)) * 1000)).intValue() == i){
			return true;
		}
		return false;
	}
	
}
