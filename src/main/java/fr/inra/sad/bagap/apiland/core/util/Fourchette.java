package fr.inra.sad.bagap.apiland.core.util;

import java.io.Serializable;

public class Fourchette implements Serializable, Comparable<Double> {

	private static final long serialVersionUID = 1L;

	private double min, max;
	
	public Fourchette(double min, double max){
		this.min = min;
		this.max = max;
	}
	
	public String toString(){
		return min+" "+max;
	}
	
	public double min(){
		return min;
	}
	
	public double max(){
		return max;
	}

	@Override
	public int compareTo(Double n) {
		if(n < min){
			return 1;
		}
		if(n > max){
			return -1;
		}
		return 0;
	}
	
	public boolean contains(double n){
		return compareTo(n) == 0;
	}
	
	public double distance(double n){
		int compare = compareTo(n);
		if(compare == 1){
			return n - min; 
		}if(compare == -1){
			return n - max; 
		}
		return 0;
	}
	
	public double distanceToBeSure(double n, double dForSure){
		if(n < min){
			return n - min; 
		}
		if(n > max){
			return n - max;
		}
		if(n > max - dForSure){
			return n - (max - dForSure);
		}
		/*
		if(n < min + dForSure){
			return n - (min + dForSure);
		}
		*/
		
		/*
		if(min + dForSure < max
				&& n < min + dForSure){
			return n - (min + dForSure);
		}
		if(min + dForSure > max
				&& n < max){
			return n - max;
		}
		if(max - dForSure > min
				&& n > max - dForSure){
			return n - (max - dForSure);
		}
		if(max - dForSure < min
				&& n > min){
			return n - min;
		}*/
		return 0;
	}
	
}
