package fr.inra.sad_paysage.apiland.core.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransitionMatrixBuilder<O extends Serializable> {

	private Map<O, Map<O, Double>> transitionRate;
 	
	public TransitionMatrixBuilder(){
		reset();
	}
	
	private void reset(){
		transitionRate = new HashMap<O, Map<O, Double>>();
	}
	
	public void setTransitionRate(O in, O out, double rate){
		if(!transitionRate.containsKey(in)){
			transitionRate.put(in, new HashMap<O, Double>());
		}
		transitionRate.get(in).put(out, rate);
	}
	
	private boolean isValid(){
		double sum;
		for(O in : transitionRate.keySet()){
			if(transitionRate.size() != transitionRate.get(in).size()){
				throw new IllegalArgumentException("size problem");
				//return false;
			}
			sum = 0;
			for(O out : transitionRate.get(in).keySet()){
				sum += transitionRate.get(in).get(out);
			}
			if(sum < 99 || sum > 101){
				throw new IllegalArgumentException("sum problem : not equals to '100'");
				//return false;
			}
		}
		return true;
	}
	
	public TransitionMatrix<O> build(){
		TransitionMatrix<O> tMat = null;
		if(isValid()){
			List<O> lo = new ArrayList<O>(transitionRate.size());
			double[][] rates = new double[transitionRate.size()][];
			
			for(O in : transitionRate.keySet()){
				lo.add(in);
			}
	
			for(O in : transitionRate.keySet()){
				rates[lo.indexOf(in)] = new double[transitionRate.size()];
				for(O out : transitionRate.keySet()){
					rates[lo.indexOf(in)][lo.indexOf(out)] = transitionRate.get(in).get(out);
				}
			}
			
			tMat = new TransitionMatrix<O>(lo, rates);
		}
		
		reset();
		
		return tMat;
	}
	
}
