package fr.inra.sad.bagap.apiland.cluster;

import java.util.HashSet;
import java.util.Set;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;

public class Cluster {
	
	private int value;

	private int[] filter;
	
	private int width;
	
	private int height;
	
	private Pixel corner;
	
	private int minx, maxx, miny, maxy;
	
	private Set<Pixel> pixels;
	
	private int theoriticalSize;
	
	private boolean isInit;
	
	public Cluster(int value){
		this.value = value;
		minx = Integer.MAX_VALUE;
		maxx = -1;
		miny = Integer.MAX_VALUE;
		maxy = -1;
		pixels = new HashSet<Pixel>();
		isInit = false;
	}
	
	public Cluster(int value, int width, int height, int minx, int miny, int size, int[] filter) {
		this.value = value;
		this.width = width;
		this.height = height;
		this.corner = new Pixel(minx, miny);
		this.theoriticalSize = size;
		this.filter = filter;
		isInit = true;
	}

	public int getValue(){
		return value;
	}
	
	public int width(){
		if(!isInit){
			init();
		}
		return width;
	}
	
	public int height(){
		if(!isInit){
			init();
		}
		return height;
	}
	
	public Pixel getCorner(){
		if(!isInit){
			init();
		}
		return corner;
	}
	
	public int theoriticalSize(){
		if(!isInit){
			init();
		}
		return theoriticalSize;
	}
	
	public int minx(){
		if(!isInit){
			init();
		}
		return minx;
	}
	
	public int miny(){
		if(!isInit){
			init();
		}
		return miny;
	}
	
	public void setPixel(Pixel p){
		pixels.add(p);
		theoriticalSize = pixels.size();
		minx = Math.min(minx, p.x());
		maxx = Math.max(maxx, p.x());
		miny = Math.min(miny, p.y());
		maxy = Math.max(maxy, p.y());
	}
	
	private void init(){
		if(!isInit){
			corner = new Pixel(minx, miny);
			width = maxx - minx + 1;
			height = maxy - miny + 1;
			filter = new int[width * height];
			
			int index = 0;
			for(int y=miny; y<miny+height; y++) {
				for(int x=minx; x<minx+width; x++) {
					Pixel p = new Pixel(x, y);
					if(pixels.contains(p)){
						filter[index++] = 1;
					}else{
						filter[index++] = 0;
					}
				}
			}
			
			pixels.clear();
			pixels = null;
			
			// 2eme boucle : affecter les valeurs specifiques 1, 2, 3 et 4
			index = 0;
			// 1ere ligne
			for(int x=0; x<width; x++) {
				if(x == 0){
					if(filter[index] != 0){
						filter[index] = 4;
					}
				}else{
					if(filter[index] != 0){
						if(filter[index-1] != 0){
							filter[index] = 2;
						}else{
							filter[index] = 4;
						}
					}
				}
				index++;
			}
			for(int y=1; y<height; y++) {
				for(int x=0; x<width; x++) {
					if(x == 0){
						if(filter[index] != 0){
							if(filter[index-width] != 0){
								filter[index] = 3;
							}else{
								filter[index] = 4;
							}
						}
					}else{
						if(filter[index] != 0){
							if(filter[index-1] == 0){
								if(filter[index-width] == 0){
									filter[index] = 4;
								}else{
									filter[index] = 3;
								}
							}else{
								if(filter[index-width] == 0){
									filter[index] = 2;
								}
							}
						}
					}
					index++;
				}
			}	
				
				
			isInit = true;
		}
	}
	
	public int filter(int wx, int wy){
		if(!isInit){
			init();
		}
		//System.out.println(this.value+" "+filter.length+" "+width()+" "+wx+" "+wy+" "+theoriticalSize);
		return filter[wy * width() + wx];
	}
	
}
