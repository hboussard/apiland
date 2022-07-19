package fr.inra.sad.bagap.apiland.patch;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;

public class PatchComposite {

	private Map<Integer, Set<Patch>> patches;
	
	public PatchComposite(){
		patches = new HashMap<Integer, Set<Patch>>();
	}
	
	public void addPixel(Pixel p, int value){
		//System.out.println(p+" value : "+value);
		if(!patches.containsKey(value)){
			patches.put(value, new HashSet<Patch>());
		}
		Set<Patch> touches = new HashSet<Patch>();
		for(Patch pa : patches.get(value)){
			if(pa.touches(p)){
				touches.add(pa);
			}
		}
		switch(touches.size()){
		case 0 : 
			//Patch pa = new Patch(value);
			Patch pa = new MapPatch(value);
			pa.add(p);
			patches.get(value).add(pa);
			break;
		case 1 :
			touches.iterator().next().add(p);
			break;
		default :
			Iterator<Patch> ite = touches.iterator();
			Patch patch = ite.next();
			patch.add(p);
			while(ite.hasNext()){
				pa = ite.next();
				patch.addAll(pa);
				patches.get(value).remove(pa);
			}
			break;
		}
	}
	
	public void removePixel(Pixel p, int value){
		for(Patch pa : patches.get(value)){
			if(pa.contains(p)){
				pa.remove(p);
				if(pa.size() == 0){
					patches.get(value).remove(pa);
					if(patches.get(value).size() == 0){
						patches.remove(value);
					}
				}
				return;
			}
		}
	}
	
	public Set<Patch> patches(){
		Set<Patch> ps = new HashSet<Patch>();
		for(Set<Patch> p : patches.values()){
			ps.addAll(p);
		}
		return ps;
	}
	
	public void display(){
		for(Integer v : patches.keySet()){
			System.out.println("valeur : "+v);
			for(Patch p : patches.get(v)){
				System.out.println(p.size());
			}
		}
	}
	
	public void info(){
		System.out.println("nombre de patchs : "+patches.size());
		for(Integer i : patches.keySet()){
			System.out.println(i);
		}
	}
	
	public int size(){
		int size = 0;
		for(Integer v : patches.keySet()){
			for(Patch p : patches.get(v)){
				size += p.size();
			}
		}
		return size;
	}
	
	public void display(int value){
		if(patches.containsKey(value)){
			for(Patch p : patches.get(value)){
				p.display();
			}
		}
	}

	public void upPixels() {
		//System.out.println("up pixels");
		for(Integer v : patches.keySet()){
			for(Patch p : patches.get(v)){
				p.upPixels();
			}
		}
	}
	
}
