package fr.inra.sad.bagap.apiland.core.util;

import java.util.Map;
import java.util.TreeMap;

public class DistanceValueMatrix {

	private Map<Integer, Map<Integer, Double>> distances;
	
	public DistanceValueMatrix(){
		distances = new TreeMap<Integer, Map<Integer, Double>>();
	}
	
	public void setDistance(int v1, int v2, double distance){
		if(v1 <= v2){
			if(!distances.containsKey(v1)){
				distances.put(v1, new TreeMap<Integer, Double>());
			}
			distances.get(v1).put(v2, distance);
		}else{
			if(!distances.containsKey(v2)){
				distances.put(v2, new TreeMap<Integer, Double>());
			}
			distances.get(v2).put(v1, distance);
		}
	}
	
	public double getDistance(int v1, int v2){
		if(v1 <= v2){
			return distances.get(v1).get(v2);
		}else{
			return distances.get(v2).get(v1);
		}
	}
	
}
