package fr.inra.sad.bagap.apiland.analysis.matrix.window.shape;

import java.util.LinkedList;
import java.util.List;

import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;

public class DonutWindow extends WindowShape{

	private final int min, max;
	
	private final int[] filter;
	
	private List<Pixel> addDownList;
	
	private List<Pixel> removeDownList;
	
	private List<Pixel> addVerticalDownList;
	
	private List<Pixel> removeVerticalDownList;
	
	private List<Pixel> addHorizontalDownList;
	
	private List<Pixel> removeHorizontalDownList;
	
	public DonutWindow(int min, int max){
		this.min = min;
		this.max = max;
		filter = new int[max*max];
		init();
		display();
	}
	
	private void init() {
		double rayon = new Double(max)/2;
		double internal = new Double(min)/2;
		WKTReader wkt = new WKTReader();
		int index;
		try {
			// 1ère boucle : écarter les valeurs en dehors du cercle
			// et écarter les valeurs au centre du cercle
			Point center = (Point) wkt.read("POINT (" + rayon + " " + rayon + ")");
			Point p;
			index = 0;
			for (double y=0.5; y<max; y++) {
				for (double x=0.5; x<max; x++) {
					p = (Point) wkt.read("POINT (" + x + " " + y + ")");
					if(center.distance(p) > rayon){
						filter[index++] = 0;
					}else if(center.distance(p) < internal){
						filter[index++] = 0;
					}else{
						filter[index++] = 1;
					}
				}
			}
		
			// 2ème boucle : affecter les valeurs spécifiques 1, 2, 3 et 4
			index = 0;
			// 1ère ligne
			for(int x=0; x<max; x++) {
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
			for(int y=1; y<max; y++) {
				for(int x=0; x<max; x++) {
					if(x == 0){
						if(filter[index] != 0){
							if(filter[index-max] != 0){
								filter[index] = 3;
							}else{
								filter[index] = 4;
							}
						}
					}else{
						if(filter[index] != 0){
							if(filter[index-1] == 0){
								if(filter[index-max] == 0){
									filter[index] = 4;
								}else{
									filter[index] = 3;
								}
							}else{
								if(filter[index-max] == 0){
									filter[index] = 2;
								}
							}
						}
					}
					index++;
				}
			}
		} catch (ParseException ex) {
			ex.printStackTrace();
		}
	}
	
	@Override
	public int width() {
		return max;
	}

	@Override
	public int height() {
		return max;
	}
	
	@Override
	public int filter(int wx, int wy){
		return filter[wy * width() + wx];
	}
	
	private Pixel pixelByIndex(int index){
		return new Pixel(index%width(), index/width());
	}

	private void initLists(){
		if(addDownList == null){
			addDownList = new LinkedList<Pixel>();
			removeDownList = new LinkedList<Pixel>();
			addHorizontalDownList = new LinkedList<Pixel>();
			removeHorizontalDownList = new LinkedList<Pixel>();
			addVerticalDownList = new LinkedList<Pixel>();
			removeVerticalDownList = new LinkedList<Pixel>();
			Pixel p;
			
			for(int i=0; i<filter.length; i++){
				if(filter[i] == 2 || filter[i] == 4){
					p = pixelByIndex(i);
					removeDownList.add(p);
					if(i < filter.length - width()
							&& filter[i+width()] != 0){
						removeVerticalDownList.add(p);
					}
				}
				if(i > width() && filter[i] == 0 && filter[i-width()] != 0){
					addDownList.add(pixelByIndex(i));
					addVerticalDownList.add(pixelByIndex(i-width()));
				}
			}
			
			for(Pixel r : removeDownList){
				if(r.x() < width()-1 && filter(r.x()+1, r.y()) != 0){
					removeHorizontalDownList.add(r);
				}
				if(r.x() > 0 && filter(r.x()-1, r.y()) != 0){
					p = new Pixel(r.x()-1, r.y());
					if(!removeDownList.contains(p)){
						removeHorizontalDownList.add(p);
					}
				}
			}
			
			for(Pixel r : addDownList){
				if(r.x() < width()-1 && filter(r.x()+1, r.y()-1) != 0){
					addHorizontalDownList.add(r);
				}
				if(r.x() > 0 && filter(r.x()-1, r.y()-1) != 0){
					p = new Pixel(r.x()-1, r.y());
					if(!addDownList.contains(p)){
						addHorizontalDownList.add(p);
					}
				}
			}
		}
	}
	
	@Override
	public List<Pixel> removeDownList() {
		initLists();
		return removeDownList;
	}
	
	@Override
	public List<Pixel> addDownList() {
		initLists();
		return addDownList;
	}

	@Override
	public List<Pixel> removeHorizontalDownList() {
		initLists();
		return removeHorizontalDownList;
	}

	@Override
	public List<Pixel> addHorizontalDownList() {
		initLists();
		return addHorizontalDownList;
	}

	@Override
	public List<Pixel> removeVerticalDownList() {
		initLists();
		return removeVerticalDownList;
	}

	@Override
	public List<Pixel> addVerticalDownList() {
		initLists();
		return addVerticalDownList;
	}
	
	public void display(){
		int index=0;
		for(int f : filter){
			System.out.print(f+" ");
			index++;
			if(index%max == 0){
				System.out.println();
			}
		}
	}

	@Override
	public void export(Pixel p, Matrix m, String path) {
		// TODO Auto-generated method stub
		
	}

	
	@Override
	public void infos() {
		// TODO Auto-generated method stub
		
	}
	
}

