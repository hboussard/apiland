package fr.inra.sad_paysage.apiland.core.util;

import java.io.Serializable;
import java.util.List;

public class TransitionMatrix<O extends Serializable> implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<O> objects;
	
	private double[][] matrix;
	
	public TransitionMatrix(List<O> o, double[][] m){
		this.objects = o;
		this.matrix = m;
	}
	
	public double getTransitionRate(O in, O out){
		return matrix[getPosition(in)][getPosition(out)];
	}
	
	private int getPosition(Serializable o){
		return objects.indexOf(o);
	}
	
	public void display(){
		for(double[] dd : matrix){
			for(double d : dd){
				System.out.print(d+" ");
			}
			System.out.println();
		}
	}
	
	public O getTransition(Serializable in){
		double r = Math.random();
		double t = 0;
		int index = 0;
		for(double d : matrix[getPosition(in)]){
			t += d/100;
			if(t >= r){
				return objects.get(index);
			}
			index++;
		}
		return objects.get(objects.size()-1);
	}
	
}
