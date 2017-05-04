package fr.inra.sad_paysage.apiland.core.util;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

public class Manager<E extends Comparable<E>> {

	private Map<E, Integer> elements;
	
	private int total;
	
	public Manager(){
		reset();
	}
	
	public void reset(){
		elements = new TreeMap<E, Integer>();
		total = 0;
	}
	
	private boolean contains(E e){
		for(E i : elements.keySet()){
			if(i.equals(e)) {
				return true;
			}
		}
		return false;
	}
	
	private void set(E e){
		elements.put(e, 0);
	}
	
	private void put(E e){
		elements.put(e, elements.get(e) + 1);
		total++;
	}
	
	public void add(E e){
		if(!contains(e)){
			set(e);
		}
		put(e);
	}
	
	public void display(){
		for(Entry<E, Integer> e : elements.entrySet()){
			System.out.println(e.getKey()+" --> "+e.getValue()+", soit "+(new Double(e.getValue())*100/total)+"%");
		}
	}
	
	public Map<E, Double> gets(){
		Map<E, Double> results = new HashMap<E, Double>();
		for(Entry<E, Integer> e : elements.entrySet()){
			results.put(e.getKey(), new Double(e.getValue())*100/total);
		}
		return results;
	}
	
	public double note(Map<E, Fourchette> ref){
		double note = 0;
		for(Entry<E, Double> e : gets().entrySet()){
			note += ref.get(e.getKey()).distance(e.getValue());
		}
		return note;
	}
	
}
