package fr.inra.sad.bagap.apiland.analysis.matrix.window.shape;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

import fr.inra.sad.bagap.apiland.analysis.matrix.window.shape.distance.DistanceFunction;
import fr.inra.sad.bagap.apiland.core.space.CoordinateManager;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.PixelWithID;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;

public class CircleWindow extends WindowShape{

	private final int diameter;
	
	//private final double rayon;
	
	private final int[] filter;
	
	private int theoriticalSize;
	
	private List<Pixel> addDownList;
	
	private List<Pixel> removeDownList;
	
	private List<Pixel> addVerticalDownList;
	
	private List<Pixel> removeVerticalDownList;
	
	private List<Pixel> addHorizontalDownList;
	
	private List<Pixel> removeHorizontalDownList;
	
	// structure de donn�es pour la fonction de distance
	// ou structure de donn�es pour les distances par pixel
	// ou d�l�gation � la forme de fen�tre
	private double[][] distance;
	
	private double[][] weighted;
	
	private double[][] weightedH;
	
	private double[][] weightedV;
	
	/*
	public CircleWindow(double rayon){
		this.rayon = rayon;
		this.diameter = rayon + (1.0/2);
		
		double rayon = new Double(diameter)/2 - (1.0/2);
		
		filter = new int[diameter*diameter];
		init();
		//display();
	}*/
	
	public CircleWindow(int d){
		this.diameter = d;
		filter = new int[diameter*diameter];
		init();
		//display();
	}
	
	public CircleWindow(int d, DistanceFunction function){
		super(function);
		this.diameter = d;
		filter = new int[diameter*diameter];
		init();
		//display();
	}
	
	@Override
	public double[][] weighted(){
		if(weighted == null){
			double rayon = new Double(diameter)/2 - (1.0/2);
			initWeighted(rayon);
		}
		return weighted;
	}
	
	@Override
	public double[][] weightedH(){
		if(weightedH == null){
			if(weighted == null){
				double rayon = new Double(diameter)/2 - (1.0/2);
				initWeighted(rayon);
			}
			initWeightedCouple();
		}
		return weightedH;
	}
	
	@Override
	public double[][] weightedV(){
		if(weightedV == null){
			if(weighted == null){
				double rayon = new Double(diameter)/2 - (1.0/2);
				initWeighted(rayon);
			}
			initWeightedCouple();
		}
		return weightedV;
	}
	
	protected void initWeighted(double rayon1) {
		
		double rayon2 = new Double(diameter)/2;
		
		distance = new double[diameter][diameter];
		
		WKTReader wkt = new WKTReader();
		Point center;
		try {
			center = (Point) wkt.read("POINT (" + (rayon1+(1.0/2)) + " " + (rayon1+(1.0/2)) + ")");
			Point p;
			//theoricalSize = 0;
			int j=0, i=0;
			double d;
			for (double y=0.5; y<diameter; y++) {
				for (double x=0.5; x<diameter; x++) {
					p = (Point) wkt.read("POINT (" + x + " " + y + ")");
					d = center.distance(p);
					if(d > rayon1){
						distance[j][i] = Raster.getNoDataValue();
					}else{
						distance[j][i] = d * Raster.getCellSize();
					}
					i++;
				}
				j++;
				i = 0;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		//System.out.println(rayon1+" "+Raster.getCellSize()+" "+(rayon1*Raster.getCellSize()));
		//DistanceFunction function = new DistanceFunction("", rayon1*Raster.getCellSize());
		weighted = new double[diameter][diameter];
		for(int j=0; j<diameter; j++){
			for(int i=0; i<diameter; i++){
				weighted[j][i] = getDistanceFunction().interprete(distance[j][i]);
			}
		}
	}
	
	protected void initWeightedCouple() {
		weightedH = new double[diameter][diameter-1];
		for(int j=0; j<diameter; j++){
			for(int i=0; i<diameter-1; i++){
				weightedH[j][i] = getDistanceFunction().interprete((distance[j][i] + distance[j][i+1]) / 2);
			}
		}
		
		weightedV = new double[diameter-1][diameter];
		for(int j=0; j<diameter-1; j++){
			for(int i=0; i<diameter; i++){
				weightedV[j][i] = getDistanceFunction().interprete((distance[j][i] + distance[j+1][i]) / 2);
			}
		}
	}
	
	@Override
	public int theoreticalSize(){
		return theoriticalSize;
	}
	
	protected void init() {
		
		//double rayon = new Double(diameter)/2;
		double rayon = new Double(diameter)/2 - (1.0/2);
		
		//System.out.println(rayon);
		WKTReader wkt = new WKTReader();
		int index;
		try {
			// 1ere boucle : ecarter les valeurs en dehors du cercle
			Point center = (Point) wkt.read("POINT (" + (rayon+(1.0/2)) + " " + (rayon+(1.0/2)) + ")");
			//Point center = (Point) wkt.read("POINT (" + rayon + " " + rayon + ")");
			Point p;
			index = 0;
			theoriticalSize = 0;
			for (double y=0.5; y<diameter; y++) {
				for (double x=0.5; x<diameter; x++) {
					p = (Point) wkt.read("POINT (" + x + " " + y + ")");
					if(center.distance(p) > rayon){
						filter[index++] = 0;
					}else{
						filter[index++] = 1;
						theoriticalSize++;
					}
				}
			}
			
			// 2eme boucle : affecter les valeurs specifiques 1, 2, 3 et 4
			index = 0;
			// 1ère ligne
			for(int x=0; x<diameter; x++) {
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
			for(int y=1; y<diameter; y++) {
				for(int x=0; x<diameter; x++) {
					if(x == 0){
						if(filter[index] != 0){
							if(filter[index-diameter] != 0){
								filter[index] = 3;
							}else{
								filter[index] = 4;
							}
						}
					}else{
						if(filter[index] != 0){
							if(filter[index-1] == 0){
								if(filter[index-diameter] == 0){
									filter[index] = 4;
								}else{
									filter[index] = 3;
								}
							}else{
								if(filter[index-diameter] == 0){
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
		return diameter;
	}

	@Override
	public int height() {
		return diameter;
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
			
			for(int i=0; i<filter.length; i++){
				if(filter[i] == 2 || filter[i] == 4){
					removeDownList.add(pixelByIndex(i));
				}
				if(i >= width() && filter[i] == 0 && filter[i-width()] != 0){
					addDownList.add(pixelByIndex(i));
				}
				if(i < width() && filter[i] == 2){
					removeHorizontalDownList.add(pixelByIndex(i));
				}
				if(i >= width() && i%width() != 0 
						&& (filter[i] == 1 || filter[i] == 2)
						&& (filter[i-width()] == 0 || filter[i-width()] == 3 || filter[i-width()] == 4)){
					removeHorizontalDownList.add(pixelByIndex(i));
				}
				if(i >= width() && i%width() != 0
						&& (filter[i] == 0 || filter[i] == 3 || filter[i] == 4)
						&& (filter[i-width()] == 1 || filter[i-width()] == 2)){
					addHorizontalDownList.add(pixelByIndex(i));
				}
				if(i >= width()
						&& (filter[i] == 1 || filter[i] == 3)
						&& (filter[i-width()] == 0 || filter[i-width()] == 2 || filter[i-width()] == 4)){
					removeVerticalDownList.add(pixelByIndex(i));
				}
				if(i >= width()
						&& (filter[i] == 0 || filter[i] == 2 || filter[i] == 4)
						&& (filter[i-width()] == 1 || filter[i-width()] == 3)){
					addVerticalDownList.add(pixelByIndex(i));
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

	@Override
	public void display(){
		int index=0;
		System.out.println();
		for(int f : filter){
			System.out.print(f+" ");
			index++;
			if(index%diameter == 0){
				System.out.println();
			}
		}
		if(weighted != null){
			System.out.println();
			for(int j=0; j<diameter; j++){
				for(int i=0; i<diameter; i++){
					System.out.print(distance[j][i]+" ");
				}
				System.out.println();
			}
			System.out.println();
			for(int j=0; j<diameter; j++){
				for(int i=0; i<diameter; i++){
					System.out.print(weighted[j][i]+" ");
				}
				System.out.println();
			}
		}
	}
	
	@Override
	public void export(Pixel p, Matrix m, String path) {
		try {
			String name = new File(m.getFile()).getName().replace(".asc", "");
			String file = "";
			if(p instanceof PixelWithID){
				file = path+name+"_circle_"+diameter+"_"+((PixelWithID) p).getId()+".asc";
			}else{
				file = path+name+"_circle_"+diameter+"_"+CoordinateManager.getProjectedX(m, p.x())+"-"+CoordinateManager.getProjectedY(m, p.y())+".asc";
			}
			
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
			
			double delta = diameter/2.0;
			double X = CoordinateManager.getProjectedX(m, p.x());
			double nX = X - delta * Raster.getCellSize();
				
			double Y = CoordinateManager.getProjectedY(m, p.y());
			double nY = Y - delta * Raster.getCellSize();
				
			out.write("ncols ");
			out.write(diameter+"");
			out.newLine();
			out.write("nrows ");
			out.write(diameter+"");
			out.newLine();
			out.write("xllcorner ");
			out.write(nX+"");
			out.newLine();
			out.write("yllcorner ");
			out.write(nY+"");
			out.newLine();
			out.write("cellsize ");
			out.write(Raster.getCellSize()+"");
			out.newLine();
			out.write("NODATA_value ");
			out.write(Raster.getNoDataValue()+"");
			out.newLine();
			
			int index = 0;
			for(int j=p.y()-diameter/2; j<p.y()+1+diameter/2; j++){
				for(int i=p.x()-diameter/2; i<p.x()+1+diameter/2; i++){
					if(filter[index++] == 0){
						out.write("-1 ");
					}else{
						out.write(m.get(i, j)+" ");
					}
				}
				out.newLine();
			}
			
			/*
			int index=0;
			for(int f : filter){
				if(f == 0){
					out.write("-1 ");
				}else{
					out.write("1 ");
				}
				
				index++;
				if(index%diameter == 0){
					out.newLine();
				}
			}*/
			
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	@Override
	public void infos() {
		// do nothing
	}

	
	/*
	private final int[] filter = {
			0,0,0,0,0,4,2,2,2,2,0,0,0,0,0,
			0,0,0,4,2,1,1,1,1,1,2,2,0,0,0,
			0,0,4,1,1,1,1,1,1,1,1,1,2,0,0,
			0,4,1,1,1,1,1,1,1,1,1,1,1,2,0,
			0,3,1,1,1,1,1,1,1,1,1,1,1,1,0,
			4,1,1,1,1,1,1,1,1,1,1,1,1,1,2,
			3,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
			3,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
			3,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
			3,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
			0,3,1,1,1,1,1,1,1,1,1,1,1,1,0,
			0,3,1,1,1,1,1,1,1,1,1,1,1,1,0,
			0,0,3,1,1,1,1,1,1,1,1,1,1,0,0,
			0,0,0,3,1,1,1,1,1,1,1,1,0,0,0,
			0,0,0,0,0,3,1,1,1,1,0,0,0,0,0,
	};
	*/
	/*
	private final int[] filter = {
			0,0,0,0,0,0,0,0,0,0,4,0,0,0,0,0,0,0,0,0,0,
			0,0,0,0,0,0,0,0,0,4,1,2,0,0,0,0,0,0,0,0,0,
			0,0,0,0,0,0,0,0,4,1,1,1,2,0,0,0,0,0,0,0,0,
			0,0,0,0,0,0,0,4,1,1,1,1,1,2,0,0,0,0,0,0,0,
			0,0,0,0,0,0,4,1,1,1,1,1,1,1,2,0,0,0,0,0,0,
			0,0,0,0,0,4,1,1,1,1,1,1,1,1,1,2,0,0,0,0,0,
			0,0,0,0,4,1,1,1,1,1,1,1,1,1,1,1,2,0,0,0,0,
			0,0,0,4,1,1,1,1,1,1,1,1,1,1,1,1,1,2,0,0,0,
			0,0,4,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,2,0,0,
			0,4,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,2,0,
			4,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,2,
			0,3,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,2,0,
			0,0,3,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,2,0,0,
			0,0,0,3,1,1,1,1,1,1,1,1,1,1,1,1,1,2,0,0,0,
			0,0,0,0,3,1,1,1,1,1,1,1,1,1,1,1,2,0,0,0,0,
			0,0,0,0,0,3,1,1,1,1,1,1,1,1,1,2,0,0,0,0,0,
			0,0,0,0,0,0,3,1,1,1,1,1,1,1,2,0,0,0,0,0,0,
			0,0,0,0,0,0,0,3,1,1,1,1,1,2,0,0,0,0,0,0,0,
			0,0,0,0,0,0,0,0,3,1,1,1,2,0,0,0,0,0,0,0,0,
			0,0,0,0,0,0,0,0,0,3,1,2,0,0,0,0,0,0,0,0,0,
			0,0,0,0,0,0,0,0,0,0,3,0,0,0,0,0,0,0,0,0,0,
	};
	*/

}
