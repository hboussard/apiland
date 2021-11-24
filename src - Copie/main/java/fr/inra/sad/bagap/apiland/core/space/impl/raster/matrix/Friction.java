package fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.csvreader.CsvReader;
import com.csvreader.CsvReader.CatastrophicException;
import com.csvreader.CsvReader.FinalizedException;

public class Friction {

	private Map<Double, Double> r;
	
	private double min;
	
	public Friction(String f){
		r = new HashMap<Double, Double>();
		min = Integer.MAX_VALUE;
		read(f);
	}
	
	private void read(String f){
		try{
			CsvReader cr = new CsvReader(f,';');
			cr.readHeaders();
			while(cr.readRecord()){
				r.put(new Double(cr.get("code")), new Double(cr.get("friction")));
				min = Math.min(min, new Double(cr.get("friction")));
			}
			cr.close();
		}catch(FinalizedException ex){
			ex.printStackTrace();
		}catch(CatastrophicException ex){
			ex.printStackTrace();
		}catch(IOException ex){
			ex.printStackTrace();
		}
	}
	
	public double min(){
		return min;
	}
	
	public double get(double v){
		//System.out.println(v);
		return r.get(v);
	}
	
	public Map<Double, Double> getMap(){
		return r;
	}
	
	public void display(){
		for(Entry<Double, Double> e : r.entrySet()){
			System.out.println(e.getKey()+" --> "+e.getValue());
		}
	}
	
}
