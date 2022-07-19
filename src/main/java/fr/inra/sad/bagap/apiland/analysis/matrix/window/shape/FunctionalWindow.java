package fr.inra.sad.bagap.apiland.analysis.matrix.window.shape;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import fr.inra.sad.bagap.apiland.analysis.matrix.process.WindowMatrixProcess;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.shape.distance.DistanceFunction;
import fr.inra.sad.bagap.apiland.analysis.process.Process;
import fr.inra.sad.bagap.apiland.analysis.process.ProcessObserver;
import fr.inra.sad.bagap.apiland.analysis.process.ProcessState;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.ArrayMatrixFactory;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;

import java.util.TreeMap;
import java.util.TreeSet;

import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

import java.util.Comparator;
import java.util.Iterator;

public abstract class FunctionalWindow extends WindowShape implements ProcessObserver {
	
	protected double dMax;
	
	protected Matrix matrix;
	
	private Map<Pixel, LocateFunctionalWindow> locations;
	
	private LocateFunctionalWindow location;
	
	private Pixel locate;
	
	private TreeSet<Pixel> waits;
	//private LinkedList<Pixel> waits;
	//private Comparator<Pixel> waitsComparator;
	
	//private Set<Pixel> ever;
	//private Matrix ever;
	
	private Matrix rcm;
	
	private boolean isInit;
	
	private List<Pixel> addDownList;
	
	private List<Pixel> removeDownList;
	
	private List<Pixel> addVerticalDownList;
	
	private List<Pixel> removeVerticalDownList;
	
	private List<Pixel> addHorizontalDownList;
	
	private List<Pixel> removeHorizontalDownList;
	
	private int displacement;
	
	private Pixel[][] pixels;
	
	private double fmin;
	
	private int theoreticalSize;
	
	public FunctionalWindow(Matrix m, double d, double min, DistanceFunction function){
		this(m, d, -1, min, function);
	}
	
	public FunctionalWindow(Matrix m, double d, double min){
		this(m, d, -1, min);
	}
	
	public FunctionalWindow(Matrix m, double d, int displacement, double min){
		this(m, d, displacement, min, null);
	}
	
	public FunctionalWindow(Matrix m, double d, int displacement, double min, DistanceFunction function){
		super(function);
		matrix = m;
		dMax = d;
		fmin = min;
		//System.out.println("min "+min);
		this.displacement = displacement;
		locations = new TreeMap<Pixel, LocateFunctionalWindow>();
		
		rcm = ArrayMatrixFactory.get().create(diameter(), diameter(), m.cellsize(), 0, 0, 0, 0, Raster.getNoDataValue());
		
		waits = new TreeSet<Pixel>(new Comparator<Pixel>(){
			@Override
			public int compare(Pixel p1, Pixel p2){
				if(FunctionalWindow.this.rcm.get(p1) > FunctionalWindow.this.rcm.get(p2)){
					return 1;
				}
				return -1;
			}
		});
		
		//waits = new LinkedList<Pixel>();
		/*
		waitsComparator = new Comparator<Pixel>(){
			@Override
			public int compare(Pixel p1, Pixel p2) {
				if(FunctionalWindow.this.rcm.get(p1) > FunctionalWindow.this.rcm.get(p2)){
					return 1;
				}else if(FunctionalWindow.this.rcm.get(p1) == FunctionalWindow.this.rcm.get(p2)){
					return 0;
				}
				return -1;
			}
		};
		*/
		//ever = new HashSet<Pixel>();
		//ever = ArrayMatrixFactory.get().create(m);
		//ever = ArrayMatrixFactory.get().create(diameter(), diameter(), m.cellsize(), 0, 0, 0, 0,Raster.getNoDataValue());
		
		isInit = false;
		addDownList = new LinkedList<Pixel>();
		removeDownList = new LinkedList<Pixel>();
		addHorizontalDownList = new LinkedList<Pixel>();
		removeHorizontalDownList = new LinkedList<Pixel>();
		addVerticalDownList = new LinkedList<Pixel>();
		removeVerticalDownList = new LinkedList<Pixel>();
		
		//filters = new TreeMap<String, Integer>();
		
		//System.out.println(dMax+" "+diameter()+" "+(diameter()+2));
		/*
		pixels = new Pixel[diameter()+2][diameter()+2];
		//pixels = new Pixel[diameter()][diameter()];
		for(int y=-1; y<diameter()+1; y++){
			for(int x=-1; x<diameter()+1; x++){
				pixels[y+1][x+1] = new Pixel(x, y);
			}
		}
		*/
		pixels = new Pixel[diameter()][diameter()];
		for(int y=0; y<diameter(); y++){
			for(int x=0; x<diameter(); x++){
				pixels[y][x] = new Pixel(x, y);
			}
		}
	}
	
	protected void initTheoriticalSize() {
		int width = width();
		double rayon = new Double(width)/2 - (1.0/2);
				
		WKTReader wkt = new WKTReader();
		try {
			Point center = (Point) wkt.read("POINT (" + (rayon+(1.0/2)) + " " + (rayon+(1.0/2)) + ")");
			Point p;
			theoreticalSize = 0;
			for (double y=0.5; y<width; y++) {
				for (double x=0.5; x<width; x++) {
					p = (Point) wkt.read("POINT (" + x + " " + y + ")");
					if(center.distance(p) <= rayon){
						theoreticalSize++;
					}
				}
			}					
		} catch (ParseException e) {
			e.printStackTrace();
		}	
	}

	@Override
	public double[][] weighted(){
		return locations.get(locate).weighted();
	}
	
	@Override
	public double[][] weightedH(){
		return locations.get(locate).weightedH();
	}
	
	@Override
	public double[][] weightedV(){
		return locations.get(locate).weightedV();
	}
	
	private Pixel pixel(int x, int y){
		//System.out.println(diameter());
		//System.out.println("size "+pixels.length+" "+pixels[0].length);
		//return pixels[y+1][x+1];
		//System.out.println(x+" "+y+" "+pixels[0].length);
		return pixels[y][x];
	}
	
	public Matrix matrix(){
		return matrix;
	}
	
	@Override
	public int theoreticalSize(){
		//return location.theoricalSize();
		return theoreticalSize;
	}
	
	@Override
	public int diameter(){
		//System.out.println("diameter "+new Double((2*dMax/matrix.cellsize())+1).intValue());
		//return new Double((2*dMax/matrix.cellsize())+1).intValue();
		//return new Double((2*dMax/matrix.cellsize())).intValue();
		//if(fmin != Raster.getNoDataValue()){
		fmin = 1;
		//System.out.println(dMax+" "+matrix.cellsize()+" "+fmin);
		
			int v = new Double((2*dMax/matrix.cellsize())/fmin).intValue();
			if(v % 2 == 0){
				return new Double((2*dMax/matrix.cellsize())/fmin).intValue()+1;
			}else{
				return new Double((2*dMax/matrix.cellsize())/fmin).intValue();
			}
		/*}else{
			int v = new Double((2*dMax/matrix.cellsize())).intValue();
			if(v % 2 == 0){
				return new Double((2*dMax/matrix.cellsize())).intValue()+1;
			}else{
				return new Double((2*dMax/matrix.cellsize())).intValue();
			}
		}*/
	}
	
	@Override
	public int filter(int wx, int wy){
		return location.filter(wx, wy);
	}
	
	@Override
	public void locate(int x, int y) {
		locate = new Pixel(x, y);
		if(!locations.containsKey(locate)){
			if(displacement==-1 || y%displacement==0){
				location = diffusion(window.toWindow(locate));
			}else{
				location = locations.get(new Pixel(x, y-(y%displacement)));
			}
			locations.put(locate, location);
		}else{
			location = locations.get(locate);
		}
		if(displacement!=-1 && locations.containsKey(new Pixel(x, y-displacement))){
			locations.remove(new Pixel(x, y-displacement));
		}
	}
	
	@Override
	public void reinit() {
		isInit = false;
	}
	
	private void initLists(){
		if(!isInit){
			isInit = true;
			addDownList.clear();
			removeDownList.clear();
			addHorizontalDownList.clear();
			removeHorizontalDownList.clear();
			addVerticalDownList.clear();
			removeVerticalDownList.clear();
			
			int[] f1 = location.filters();
			window.locate(locate.x(), locate.y()+1);
			int[] f2 = location.filters();
			
			// gestion de la première ligne
			for(int i=0; i<width(); i++){
				if(f1[i] != 0){
					removeDownList.add(pixelByIndex(i));
				}
				if(f1[i] == 2){
					removeHorizontalDownList.add(pixelByIndex(i));
				}
			}
			
			for(int i=width(); i<f1.length; i++){
				if(f1[i] != 0 && f2[i-width()] == 0){
					removeDownList.add(pixelByIndex(i));
				}
				if(f1[i] == 0 && f2[i-width()] != 0){
					addDownList.add(pixelByIndex(i));
				}
				if(i%width() != 0 
						&& (f1[i] == 1 || f1[i] == 2)
						&& (f2[i-width()] == 0 || f2[i-width()] == 3 || f2[i-width()] == 4)){
					removeHorizontalDownList.add(pixelByIndex(i));
				}
				if(i%width() != 0
						&& (f1[i] == 0 || f1[i] == 3 || f1[i] == 4)
						&& (f2[i-width()] == 1 || f2[i-width()] == 2)){
					addHorizontalDownList.add(pixelByIndex(i));
				}
				if((f1[i] == 1 || f1[i] == 3)
						&& (f2[i-width()] == 0 || f2[i-width()] == 2 || f2[i-width()] == 4)){
					removeVerticalDownList.add(pixelByIndex(i));
				}
				if((f1[i] == 0 || f1[i] == 2 || f1[i] == 4)
						&& (f2[i-width()] == 1 || f2[i-width()] == 3)){
					addVerticalDownList.add(pixelByIndex(i));
				}
			}
			window.locate(locate.x(), locate.y()-1);
		}
	}
	
	private Pixel pixelByIndex(int index){
		return new Pixel(index%width(), index/width());
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
	public List<Pixel> removeVerticalDownList() {
		initLists();
		return removeVerticalDownList;
	}

	@Override
	public List<Pixel> addHorizontalDownList() {
		initLists();
		return addHorizontalDownList;
	}

	@Override
	public List<Pixel> addVerticalDownList() {
		initLists();
		return addVerticalDownList;
	}

	/*
	private LocateFunctionalWindow diffusion2(Pixel p){
		
		int[] filter = new int[size()];
		for(int i=0; i<size(); i++){
			filter[i] = 1;
		}
		return new LocateFunctionalWindow(filter, size()*size(), p);
	}
	*/
	
	private LocateFunctionalWindow diffusion(Pixel p){
		
		//rcm.clear();
		//rcm.put(p, 0.0);
		rcm.init(Integer.MAX_VALUE);
		rcm.put(p, 0.0);
		
		waits.clear();
		waits.add(p);
		//waits.addFirst(p);
		
		//ever.clear();
		//ever.init(Raster.getNoDataValue());
		while(!waits.isEmpty()){
			diffuse();
		}
		// 1ere boucle
		int[] filter = new int[size()];
		Pixel px;
		int index = 0;
		//int theoricalSize = 0;
		//System.out.println("largeur "+width()+" "+height());
		for(int y=0; y<height(); y++){
			for(int x=0; x<width(); x++){
				//System.out.println(x+" "+y);
				px = pixel(x, y);
				//if(rcm.containsKey(px)){
				if(rcm.get(px) != Integer.MAX_VALUE){
					filter[index] = 1;
					//theoricalSize++;
				}else{
					filter[index] = 0;
				}
				index++;
			}
		}
		
		// 2eme boucle
		index = 0;
		// 1ere ligne
		for(int x=0; x<width(); x++) {
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
		for(int y=1; y<height(); y++) {
			for(int x=0; x<width(); x++) {
				if(x == 0){
					if(filter[index] != 0){
						if(filter[index-width()] != 0){
							filter[index] = 3;
						}else{
							filter[index] = 4;
						}
					}
				}else{
					if(filter[index] != 0){
						if(filter[index-1] == 0){
							if(filter[index-width()] == 0){
								filter[index] = 4;
							}else{
								filter[index] = 3;
							}
						}else{
							if(filter[index-width()] == 0){
								filter[index] = 2;
							}
						}
					}
				}
				index++;
			}
		}
		
		return new LocateFunctionalWindow(filter, /*theoricalSize,*/ getDistanceFunction());
	}
	
	protected abstract double friction(Matrix m, Pixel p);
	
	//protected abstract Friction friction();
	
	private void diffuseRook(Pixel pc, Pixel op, double v, double f){
		double ov, of;
		if(rcm.get(op) == Integer.MAX_VALUE){
			if((of = friction(matrix, op)) != Raster.getNoDataValue()){
				ov = v + (matrix.cellsize()/2*f + matrix.cellsize()/2*of);
			//ov = 2;
				if(ov <= dMax){
					rcm.put(op, ov);
					waits.add(op);
				}
			}
		}
	}
	
	private void diffuseQueen(Pixel pc, Pixel op, double v, double f){
		double ov, of;
		if(rcm.get(op) == Integer.MAX_VALUE){
			if((of = friction(matrix, op)) != Raster.getNoDataValue()){
				ov = v + (Math.sqrt(2)*matrix.cellsize()/2*f + Math.sqrt(2)*matrix.cellsize()/2*of);
			//ov = 2;	
				if(ov <= dMax){
					rcm.put(op, ov);
					waits.add(op);
				}
			}
		}
	}
	
	private void diffuse(){
		Pixel p = waits.pollFirst();
		
		double f = friction(matrix, p);
		if(f != Raster.getNoDataValue()){
			//System.out.println(p+" "+c);
			
			double v = rcm.get(p);
			
			if(p.x() >= 0 && p.x() < pixels[0].length && (p.y()-1) >= 0 && (p.y()-1) < pixels[0].length){
				diffuseRook(p, pixel(p.x(), p.y()-1), v, f); // nord
			}
			if((p.x()+1) >= 0 && (p.x()+1) < pixels[0].length && p.y() >= 0 && p.y() < pixels[0].length){
				diffuseRook(p, pixel(p.x()+1, p.y()), v, f); // est
			}
			if(p.x() >= 0 && p.x() < pixels[0].length && (p.y()+1) >= 0 && (p.y()+1) < pixels[0].length){
				diffuseRook(p, pixel(p.x(), p.y()+1), v, f); // sud
			}
			if((p.x()-1) >= 0 && (p.x()-1) < pixels[0].length && p.y() >= 0 && p.y() < pixels[0].length){
				diffuseRook(p, pixel(p.x()-1, p.y()), v, f); // ouest
			}
			
			if((p.x()+1) >= 0 && (p.x()+1) < pixels[0].length && (p.y()-1) >= 0 && (p.y()-1) < pixels[0].length){
				diffuseQueen(p, pixel(p.x()+1, p.y()-1), v, f); // nord est
			}
			if((p.x()+1) >= 0 && (p.x()+1) < pixels[0].length && (p.y()+1) >= 0 && (p.y()+1) < pixels[0].length){
				diffuseQueen(p, pixel(p.x()+1, p.y()+1), v, f); // sud est
			}
			if((p.x()-1) >= 0 && (p.x()-1) < pixels[0].length && (p.y()+1) >= 0 && (p.y()+1) < pixels[0].length){
				diffuseQueen(p, pixel(p.x()-1, p.y()+1), v, f); // sud ouest
			}
			if((p.x()-1) >= 0 && (p.x()-1) < pixels[0].length && (p.y()-1) >= 0 && (p.y()-1) < pixels[0].length){
				diffuseQueen(p, pixel(p.x()-1, p.y()-1), v, f); // nord ouest
			}
			
			//ever.put(p, 1);
		}
	}
	
	@Override
	public void notify(Process p, ProcessState s) {
		switch(s){
		case DONE : 
			WindowMatrixProcess wp = (WindowMatrixProcess) p;
			Pixel pi = new Pixel(wp.x(), wp.y()-1);
			//System.out.println();
			Iterator<Entry<Pixel, LocateFunctionalWindow>> ite = locations.entrySet().iterator();
			while(ite.hasNext()){
				Entry<Pixel, LocateFunctionalWindow> e = ite.next();
				if(e.getKey().y() < wp.y()-1){
					ite.remove();
				}else{
					break;
				}
			}
			/*if(locations.containsKey(pi)){
				locations.remove(pi);
			}*/
			//locations.remove(new Pixel(wp.x(), wp.y()));
		}
	}

	@Override
	public int compareTo(ProcessObserver o) {
		return 1;
	}
	
	@Override
	public void display() {
		location.display();
	}

	@Override
	public void export(Pixel p, Matrix m, String path) {
		location.export(p, m, path);
	}

	public void infos(){
		System.out.println(locations.size());
	}
}
