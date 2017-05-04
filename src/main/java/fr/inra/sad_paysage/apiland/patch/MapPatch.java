package fr.inra.sad_paysage.apiland.patch;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;

import fr.inra.sad_paysage.apiland.core.space.impl.raster.Pixel;

public class MapPatch implements Patch {

	private int value;
	
	private Map<Integer, Set<Integer>> pixels;
	
	public MapPatch(int v){
		this.value = v;
		pixels = new TreeMap<Integer, Set<Integer>>();
	}
	
	@Override
	public int getValue(){
		return value;
	}
	
	@Override
	public boolean touches(Pixel p){
		if(pixels.containsKey(p.y()-1)){
			for(Integer x : pixels.get(p.y()-1)){
				//if(new Pixel(x, p.y()-1).touches(p)){
				if(new Pixel(x, p.y()-1).rootTouches(p)){
					return true;
				}
			}
		}
		if(pixels.containsKey(p.y())){
			for(Integer x : pixels.get(p.y())){
				//if(new Pixel(x, p.y()).touches(p)){
				if(new Pixel(x, p.y()).rootTouches(p)){
					return true;
				}
			}
		}/*
		if(pixels.containsKey(p.y()+1)){
			for(Integer x : pixels.get(p.y()+1)){
				//if(new Pixel(x, p.y()+1).touches(p)){
				if(new Pixel(x, p.y()+1).rootTouches(p)){
					return true;
				}
			}
		}*/
		return false;
	}
	
	@Override
	public boolean contains(Pixel p) {
		if(pixels.containsKey(p.y())){
			for(Integer x : pixels.get(p.y())){
				if(x == p.x()){
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public void add(Pixel p){
		if(!pixels.containsKey(p.y())){
			pixels.put(p.y(), new TreeSet<Integer>());
		}
		pixels.get(p.y()).add(p.x());
	}
	
	private void addAll(int y, Set<Integer> xs){
		if(!pixels.containsKey(y) || pixels.get(y).size() == 0){
			pixels.put(y, xs);
		}else{
			pixels.get(y).addAll(xs);
		}
	}
	
	@Override
	public void addAll(Patch pa){
		for(Entry<Integer, Set<Integer>> e : ((MapPatch) pa).pixels.entrySet()){
			addAll(e.getKey(), e.getValue());
		}
	}
	
	@Override
	public Set<Pixel> pixels(){
		Set<Pixel> px = new TreeSet<Pixel>();
		for(int y : pixels.keySet()){
			for(int x : pixels.get(y)){
				px.add(new Pixel(x, y));
			}
		}
		return px;
	}

	@Override
	public void remove(Pixel p) {
		pixels.get(p.y()).remove(p.x());
		if(pixels.get(p.y()).size() == 0){
			pixels.remove(p.y());
		}
	}

	@Override
	public int size() {
		int size = 0;
		for(Set<Integer> p : pixels.values()){
			size += p.size();
		}
		return size;
	}

	@Override
	public double getArea(){
		return size() * Pixel.getCellSize() * Pixel.getCellSize();
	}

	@Override
	public void display() {
		for(Pixel p : pixels()){
			System.out.print(p+", ");
		}
		System.out.println();
	}

	@Override
	public void upPixels() {
		Map<Integer, Set<Integer>> pixels2 = new TreeMap<Integer, Set<Integer>>();
		for(Entry<Integer, Set<Integer>> e : pixels.entrySet()){
			if(e.getKey() > 0){
				pixels2.put(e.getKey()-1, e.getValue());
			}
		}
		pixels = pixels2;
	}

	@Override
	public Pixel pixel() {
		int y = pixels.keySet().iterator().next();
		int x = pixels.get(y).iterator().next();
		return new Pixel(x, y);
	}
	
	@Override
	public boolean equals(Patch pi) {
		int y1 = pixels.keySet().iterator().next();
		int y2 = ((MapPatch) pi).pixels.keySet().iterator().next();
		if(y1 != y2){
			return false;
		}
		int x1 = pixels.values().iterator().next().iterator().next();
		int x2 = ((MapPatch) pi).pixels.values().iterator().next().iterator().next();
		if(x1 != x2){
			return false;
		}
		//System.out.println(y1+" "+x1+" - "+y2+" "+x2);
		return true;
	}
	
	@Override
	public Envelope getEnvelope(){
		int minx = Integer.MAX_VALUE;
		int maxx = Integer.MIN_VALUE;
		int miny = Integer.MAX_VALUE;
		int maxy = Integer.MIN_VALUE;
		for(int y : pixels.keySet()){
			miny = Math.min(miny, y);
			maxy = Math.max(maxy, y);
			for(int x : pixels.get(y)){
				minx = Math.min(minx, x);
				maxx = Math.max(maxx, x);
			}
		}
		return new Envelope(minx, maxx, miny, maxy);
	}
}

