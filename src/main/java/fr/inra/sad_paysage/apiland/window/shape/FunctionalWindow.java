package fr.inra.sad_paysage.apiland.window.shape;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Comparator;
import java.util.Iterator;

import fr.inra.sad_paysage.apiland.analysis.matrix.process.WindowMatrixProcess;
import fr.inra.sad_paysage.apiland.analysis.process.ProcessObserver;
import fr.inra.sad_paysage.apiland.analysis.process.ProcessState;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.Raster;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.matrix.ArrayMatrixFactory;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.matrix.Matrix;
import fr.inra.sad_paysage.apiland.analysis.process.Process;

public abstract class FunctionalWindow extends WindowShape implements ProcessObserver {
	
	protected double dMax;
	
	protected Matrix matrix;
	
	private Map<Pixel, LocateFunctionalWindow> locations;
	
	private LocateFunctionalWindow location;
	
	private Pixel locate;
	//private Pixel initPixel;
	
	private TreeSet<Pixel> waits;
	//private LinkedList<Pixel> waits;
	//private Comparator<Pixel> waitsComparator;
	
	//private Set<Pixel> ever;
	//private Matrix ever;
	
	//private Map<Pixel, Double> rcm;
	private Matrix rcm;
	
	private boolean isInit;
	
	private List<Pixel> addDownList;
	
	private List<Pixel> removeDownList;
	
	private List<Pixel> addVerticalDownList;
	
	private List<Pixel> removeVerticalDownList;
	
	private List<Pixel> addHorizontalDownList;
	
	private List<Pixel> removeHorizontalDownList;
	
	private int displacement;
	//private Map<String, Integer> filters;
	
	//private int index;
	
	//private Map<Pixel, Map<Pixel, Double>> angles;
	
	//private static final double angleMin = 0;
	//private static final double angleMin = 0.35;
	//private static final double angleMin = 0.79;
	//private static final double angleMin = 2.35;
	//private static final double angleMin = 2.355;
	//private static final double angleMin = Math.PI;
	
	private Pixel[][] pixels;
	
	public FunctionalWindow(Matrix m, double d){
		this(m, d, -1);
	}
	
	public FunctionalWindow(Matrix m, double d, int displacement){
		matrix = m;
		dMax = d;
		this.displacement = displacement;
		locations = new TreeMap<Pixel, LocateFunctionalWindow>();
		
		//rcm = new HashMap<Pixel, Double>();
		//rcm = ArrayMatrixFactory.get().create(m);
		rcm = ArrayMatrixFactory.get().create(diameter(), diameter(), m.cellsize(), 0, 0, 0, 0,Raster.getNoDataValue());
		
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
		
		pixels = new Pixel[diameter()+2][diameter()+2];
		for(int y=-1; y<diameter()+1; y++){
			for(int x=-1; x<diameter()+1; x++){
				pixels[y+1][x+1] = new Pixel(x, y);
			}
		}
		
		//initPixel = pixel(diameter()/2, diameter()/2);
		/*angles = new HashMap<Pixel, Map<Pixel, Double>>();
		double a, b;
		Pixel p, pc;
		for(int y=0; y<diameter(); y++){
			for(int x=0; x<diameter(); x++){
				pc = pixel(x, y);
				angles.put(pc, new HashMap<Pixel, Double>());
				
				if(pc.equals(initPixel)){
					p = pixel(pc.x(), pc.y()-1); // nord 
					angles.get(pc).put(p, Math.PI);
					p = pixel(pc.x()+1, pc.y()); // est
					angles.get(pc).put(p, Math.PI);
					p = pixel(pc.x(), pc.y()+1); // sud
					angles.get(pc).put(p, Math.PI);
					p = pixel(pc.x()-1, pc.y()); // ouest
					angles.get(pc).put(p, Math.PI);
					p = pixel(pc.x()+1, pc.y()-1); // nord est
					angles.get(pc).put(p, Math.PI);
					p = pixel(pc.x()+1, pc.y()+1); // sud est
					angles.get(pc).put(p, Math.PI);
					p = pixel(pc.x()-1, pc.y()+1); // sud ouest
					angles.get(pc).put(p, Math.PI);
					p = pixel(pc.x()-1, pc.y()-1); // nord ouest
					angles.get(pc).put(p, Math.PI);
					continue;
				}
				
				b = Math.sqrt(Math.pow(pc.x()-initPixel.x(), 2)+Math.pow(pc.y()-initPixel.y(), 2));
				
				a = 1;
				p = pixel(pc.x(), pc.y()-1); // nord 
				setAngle(pc, p, a, b);
				
				p = pixel(pc.x()+1, pc.y()); // est
				setAngle(pc, p, a, b);
				
				p = pixel(pc.x(), pc.y()+1); // sud
				setAngle(pc, p, a, b);
				
				p = pixel(pc.x()-1, pc.y()); // ouest
				setAngle(pc, p, a, b);
				
				a = Math.sqrt(2);
				p = pixel(pc.x()+1, pc.y()-1); // nord est
				setAngle(pc, p, a, b);
				
				p = pixel(pc.x()+1, pc.y()+1); // sud est
				setAngle(pc, p, a, b);
				
				p = pixel(pc.x()-1, pc.y()+1); // sud ouest
				setAngle(pc, p, a, b);
				
				p = pixel(pc.x()-1, pc.y()-1); // nord ouest
				setAngle(pc, p, a, b);
			}
		}*/
	}
	
	private Pixel pixel(int x, int y){
		return pixels[y+1][x+1];
	}
	/*
	private void setAngle(Pixel pc, Pixel p, double a, double b){
		double c = Math.sqrt(Math.pow(p.x()-initPixel.x(), 2) + Math.pow(p.y()-initPixel.y(), 2));
		double cos = (Math.pow(a, 2) + Math.pow(b, 2) - Math.pow(c, 2)) / (2*a*b);
		if(cos < -1){
			cos = -1;
		}else if(cos > 1){
			cos = 1;
		}
		double angle = Math.acos(cos);
		//System.out.println(a+" "+b+" "+c+" "+angle);
		if(Double.isNaN(angle)){
			//System.out.println(a+" "+b+" "+c+" "+angle);
			//angles.get(pc).put(p, 0.0);
			throw new IllegalArgumentException(cos+" "+angle);
		}else{
			angles.get(pc).put(p, angle);
		}
	}*/
	
	public Matrix matrix(){
		return matrix;
	}
	
	@Override
	public int theoricalSize(){
		return location.theoricalSize();
	}
	
	@Override
	public int diameter(){
		return new Double((2*dMax/matrix.cellsize())+1).intValue();
	}
	
	@Override
	public int filter(int wx, int wy){
		return location.filter(wx, wy);
	}
	
	@Override
	public void locate(int x, int y) {
		//System.out.println(x);
		locate = new Pixel(x, y);
		if(!locations.containsKey(locate)){
			/*
			if(x == 0){
				System.out.println("diffusion en "+y);
			}
			if(location == null){
				location = diffusion(window.toWindow(locate));
			}
			if(x == 395){
				//if(x == 11745){
				System.out.println("diffusion en "+y+" faite");
			}
			*/
			/*
			if(x == 0){
				System.out.println("diffusion en "+y);
			}
			location = diffusion(window.toWindow(locate));
			if(x == 395){
			//if(x == 11745){
				System.out.println("diffusion en "+y+" faite");
			}
			*/
			
			if(displacement==-1 || y%displacement==0){
				location = diffusion(window.toWindow(locate));
				
			}else{
				location = locations.get(new Pixel(x, y-(y%displacement)));
			}
			
			
			//}
			
			//location = diffusion2(window.toWindow(locate));
			/*
			String filter = location.getStringFilter();
			if(!filters.containsKey(filter)){
				filters.put(filter, 0);
			}
			filters.put(filter, filters.get(filter) + 1);
			index++;
			*/
			
			//System.out.println("create window shape "+x+" "+y+" : "+locations.size());
			//location.display();
			
			locations.put(locate, location);
			//System.out.println("size "+locations.size()+" "+locate);
		}else{
			location = locations.get(locate);
		}
		if(displacement!=-1 && locations.containsKey(new Pixel(x, y-displacement))){
			locations.remove(new Pixel(x, y-displacement));
		}
	}
	
	@Override
	public void close() {
		//System.out.println("nombre de passages : "+index);
		/*
		System.out.println(filters.size()+"/"+index);
		Stats s = new Stats();
		for(int v : filters.values()){
			s.add(v);
		}
		s.calculate();
		System.out.println(s.getMinimum()+" "+s.getMaximum()+" "+s.getAverage()+" "+s.getStandardDeviation());
		*/
		/*
		try {
			CsvWriter cw = new CsvWriter("c://Hugues/temp/schemes-11.csv");
			cw.setDelimiter(';');
			cw.write("id");
			cw.write("scheme");
			cw.write("count");
			cw.endRecord();
			int id = 0;
			for(Entry<String, Integer> e : filters.entrySet()){
				//if(e.getValue() == s.getMaximum()){
				//	System.out.println(e.getKey());
				//}
				cw.write((++id)+"");
				cw.write(e.getKey());
				cw.write(e.getValue()+"");
				cw.endRecord();
			}
			cw.close();
		} catch (FinalizedException | IOException e1) {
			e1.printStackTrace();
		}*/
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
		int theoricalSize = 0;
		for(int y=0; y<height(); y++){
			for(int x=0; x<width(); x++){
				px = pixel(x, y);
				//if(rcm.containsKey(px)){
				if(rcm.get(px) != Integer.MAX_VALUE){
					filter[index] = 1;
					theoricalSize++;
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
		
		return new LocateFunctionalWindow(filter, theoricalSize);
	}
	
	protected abstract double friction(Matrix m, Pixel p);
	/*
	private void diffuseRook(Pixel pc, Pixel op, double v, double f){
		double angle = angles.get(pc).get(op);
		double ov, of;
		if(angle >= angleMin 
				&& ever.get(op) == Raster.getNoDataValue()
				&& (of = friction(matrix, op)) != Raster.getNoDataValue()){
			ov = v + (matrix.cellsize()/2*f + matrix.cellsize()/2*of);
			if(ov <= dMax && ov < rcm.get(op)){
				rcm.put(op, ov);
				waits.add(op);
			}
		}
	}
	
	private void diffuseQueen(Pixel pc, Pixel op, double v, double f){
		double angle = angles.get(pc).get(op);
		double ov, of;
		if(angle >= angleMin 
				&& ever.get(op) == Raster.getNoDataValue()
				&& (of = friction(matrix, op)) != Raster.getNoDataValue()){
			ov = v + (Math.sqrt(2)*matrix.cellsize()/2*f + Math.sqrt(2)*matrix.cellsize()/2*of);
			if(ov <= dMax && ov < rcm.get(op)){
				rcm.put(op, ov);
				waits.add(op);
			}
		}
	}*/
	
	private void diffuseRookFalse(Pixel pc, Pixel op, double v, double f){
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
	
	private void diffuseQueenFalse(Pixel pc, Pixel op, double v, double f){
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
			/*
			diffuseRook(p, pixel(p.x(), p.y()-1), v, f); // nord
			diffuseRook(p, pixel(p.x()+1, p.y()), v, f); // est
			diffuseRook(p, pixel(p.x(), p.y()+1), v, f); // sud
			diffuseRook(p, pixel(p.x()-1, p.y()), v, f); // ouest
			
			diffuseQueen(p, pixel(p.x()+1, p.y()-1), v, f); // nord est
			diffuseQueen(p, pixel(p.x()+1, p.y()+1), v, f); // sud est
			diffuseQueen(p, pixel(p.x()-1, p.y()+1), v, f); // sud ouest
			diffuseQueen(p, pixel(p.x()-1, p.y()-1), v, f); // nord ouest
			*/
		// test
		//double f = 1;
		//double v = 1;
		// fin test
		
		
			diffuseRookFalse(p, pixel(p.x(), p.y()-1), v, f); // nord
			diffuseRookFalse(p, pixel(p.x()+1, p.y()), v, f); // est
			diffuseRookFalse(p, pixel(p.x(), p.y()+1), v, f); // sud
			diffuseRookFalse(p, pixel(p.x()-1, p.y()), v, f); // ouest
			
			diffuseQueenFalse(p, pixel(p.x()+1, p.y()-1), v, f); // nord est
			diffuseQueenFalse(p, pixel(p.x()+1, p.y()+1), v, f); // sud est
			diffuseQueenFalse(p, pixel(p.x()-1, p.y()+1), v, f); // sud ouest
			diffuseQueenFalse(p, pixel(p.x()-1, p.y()-1), v, f); // nord ouest
			
			
			//ever.put(p, 1);
		}
	}
	
	@Override
	public void notifyFromProcess(Process p, ProcessState s) {
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
	public void export(int x, int y, Matrix m, String path) {
		location.export(x, y, m, path);
	}

	public void infos(){
		System.out.println(locations.size());
	}
}
