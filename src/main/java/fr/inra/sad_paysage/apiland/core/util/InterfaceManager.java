package fr.inra.sad_paysage.apiland.core.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import com.csvreader.CsvWriter;
import com.csvreader.CsvWriter.FinalizedException;

public class InterfaceManager<E extends Comparable<E>> {

	private Map<Interface<E>, Integer> interfaces;
	
	private int total;

	private Map<Interface<E>, Double> mauvais;
	
	public InterfaceManager(){
		reset();
	}
	
	public void reset(){
		interfaces = new TreeMap<Interface<E>, Integer>();
		total = 0;
		mauvais = new HashMap<Interface<E>, Double>();
	}
	
	private boolean contains(E e1, E e2){
		for(Interface<E> i : interfaces.keySet()){
			if(i.contains(e1, e2)){
				return true;
			}
		}
		return false;
	}
	
	private void set(E e1, E e2){
		interfaces.put(new Interface<E>(e1, e2), 0);
	}
	
	private Interface<E> get(E e1, E e2){
		for(Interface<E> i : interfaces.keySet()){
			if(i.contains(e1, e2)){
				return i;
			}
		}
		throw new IllegalArgumentException();
	}
	
	private void add(Interface<E> i){
		interfaces.put(i, interfaces.get(i) + 1);
		total++;
	}
	
	public void add(E e1, E e2){
		if(!contains(e1, e2)){
			set(e1, e2);
		}
		add(get(e1, e2));
	}
	
	public void display(){
		for(Entry<Interface<E>, Integer> e : interfaces.entrySet()){
			System.out.println(e.getKey()+" --> "+e.getValue()+", soit "+(new Double(e.getValue())*100/total)+"%");
		}
	}
	
	public void export(String f){
		try{
			CsvWriter cw = new CsvWriter(f);
			cw.setDelimiter(';');
			for(Entry<Interface<E>, Integer> e : interfaces.entrySet()){
				cw.write(e.getKey()+" --> "+e.getValue()+", soit "+(new Double(e.getValue())*100/total)+"%");
				cw.endRecord();
			}
			cw.close();
		}catch(FinalizedException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public Map<Interface<E>, Double> gets(){
		Map<Interface<E>, Double> results = new HashMap<Interface<E>, Double>();
		for(Entry<Interface<E>, Integer> e : interfaces.entrySet()){
			results.put(e.getKey(), new Double(e.getValue())*100/total);
		}
		return results;
	}
	
	public double note(Map<Interface<E>, Fourchette> f){
		double note = 0;
		double d;
		mauvais.clear();
		for(Entry<Interface<E>, Double> e : gets().entrySet()){
			d = f.get(e.getKey()).distance(e.getValue());
			if(d != 0){
				note += Math.abs(d);
			}
			d = f.get(e.getKey()).distanceToBeSure(e.getValue(), 0.5);
			if(d != 0){
				mauvais.put(e.getKey(), d);
			}
		}
		return note;
	}
	
	public Map<Interface<E>, Double> getMauvais(){
		return mauvais;
	}
	
	public double getPourcentage(int v){
		return v*100.0/total;
	}
	
}
