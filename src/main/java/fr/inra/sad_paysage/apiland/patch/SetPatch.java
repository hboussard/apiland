package fr.inra.sad_paysage.apiland.patch;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import fr.inra.sad_paysage.apiland.core.space.impl.raster.Pixel;

public class SetPatch implements Patch {

	private int value;
	
	private Set<Pixel> pixels;
	
	public SetPatch(int v){
		this.value = v;
		pixels = new TreeSet<Pixel>();
	}
	
	@Override
	public int getValue(){
		return value;
	}
	
	@Override
	public boolean touches(Pixel p){
		for(Pixel px : pixels){
			if(px.touches(p)){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean contains(Pixel p) {
		for(Pixel px : pixels){
			if(px.equals(p)){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void add(Pixel p){
		pixels.add(p);
	}
	
	@Override
	public void addAll(Patch pa){
		pixels.addAll(pa.pixels());
	}
	
	@Override
	public Set<Pixel> pixels(){
		return pixels;
	}

	@Override
	public void remove(Pixel p) {
		pixels.remove(p);
	}

	@Override
	public int size() {
		return pixels.size();
	}

	@Override
	public double getArea(){
		return size() * Pixel.getCellSize();
	}

	@Override
	public void display() {
		for(Pixel p : pixels){
			System.out.print(p+", ");
		}
		System.out.println();
	}

	@Override
	public void upPixels() {
		Iterator<Pixel> ite = pixels.iterator();
		Pixel p;
		while(ite.hasNext()){
			p = ite.next();
			if(p.y() == 0){
				ite.remove();
			}else{
				p.setY(p.y()-1);
			}
		}
	}

	@Override
	public Pixel pixel() {
		return pixels.iterator().next();
	}

	@Override
	public boolean equals(Patch pi) {
		return pixels.iterator().next().equals(pi.pixels().iterator().next());
	}


	
}
