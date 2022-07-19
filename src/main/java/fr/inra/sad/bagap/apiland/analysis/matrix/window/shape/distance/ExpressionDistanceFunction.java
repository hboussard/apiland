package fr.inra.sad.bagap.apiland.analysis.matrix.window.shape.distance;

import java.util.Set;
import java.util.TreeSet;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class ExpressionDistanceFunction implements DistanceFunction {

	private Set<String> names;
	
	//private double distanceMax;
	
	private Expression expression;
	
	public ExpressionDistanceFunction(String function, double distanceMax){
		//this.distanceMax = distanceMax;
		
		this.names = new TreeSet<String>();
		boolean hasDmax = false;
		if(function.contains("dmax") || function.contains("dMax") || function.contains("Dmax") || function.contains("DMax") || function.contains("DMAX")){
			names.add("dmax");
			hasDmax = true;	
		}
		if(function.contains("distance")){
			names.add("distance");
		}
		expression = new ExpressionBuilder(function).variables(names).build();
		if(hasDmax){
			expression.setVariable("dmax", distanceMax);
		}
	}
	
	public double interprete(double distance){
		expression.setVariable("distance", distance);
		return expression.evaluate();
		/*
		//System.out.println(distance);
		if(distance == Raster.getNoDataValue()){
			return (double) Raster.getNoDataValue();
		}
		if(distance == 0){
			return 0.0;
		}
		if(distance>0 && distance <=50){
			return 1/3.0;
		}
		if(distance>50 && distance <=100){
			return 2/3.0;
		}
		if(distance>100){
			return 1.0;
		}
		throw new IllegalArgumentException();
		/*
		if(distance == Raster.getNoDataValue()){
			return Raster.getNoDataValue();
		}
		*/
		//return 1-Math.exp(-distance/(distanceMax/4));
		//return Math.exp(-distance/(distanceMax/4));
		//return (distance/distanceMax) ;
		//return -(distance/distanceMax) + 1;
		//return -distance + distanceMax;
		//return 1;
		 
		 
	}
	
//	/**
//	 * to get the diameter of the window shape of the function according to the cellsize 
//	 * note : the result must be odd 
//	 * @param cellsize : the raster cellsize
//	 * @return the diameter of the window shape of the function according to the cellsize 
//	 */
//	public int getDiameter(double cellsize){
//		return 11;
//	}
	
}
