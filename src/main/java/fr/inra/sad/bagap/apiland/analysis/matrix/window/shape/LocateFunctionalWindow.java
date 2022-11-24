package fr.inra.sad.bagap.apiland.analysis.matrix.window.shape;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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

public class LocateFunctionalWindow extends WindowShape {

	private int[] filter;
	
	private int width;
	
	//private int theoricalSize;
	
	private double[][] distance;
	
	private double[][] weighted;
	
	private double[][] weightedH;
	
	private double[][] weightedV;
	
	public LocateFunctionalWindow(int[] f, /*int theoricalSize,*/ DistanceFunction function){
		super(function);
		//System.out.println("création de la forme fonctionnelle en "+pixel);
		this.filter = f;
		//this.theoricalSize = theoricalSize;
		width = new Double(Math.sqrt(filter.length)).intValue();
		//display();
		/*if(function != null){
			initWeighted();
		}*/
	}
	
	@Override
	public double[][] weighted(){
		if(weighted == null){
			initWeighted();
		}
		return weighted;
	}
	
	@Override
	public double[][] weightedH(){
		if(weightedH == null){
			if(weighted == null){
				initWeighted();
			}
			initWeightedCouple();
		}
		return weightedH;
	}
	
	@Override
	public double[][] weightedV(){
		if(weightedV == null){
			if(weighted == null){
				initWeighted();
			}
			initWeightedCouple();
		}
		return weightedV;
	}
	
	protected void initWeighted() {
		
		distance = new double[width][width];
		double rayon = width / 2;
		
		WKTReader wkt = new WKTReader();
		Point center;
		try {
			center = (Point) wkt.read("POINT (" + (rayon+(1.0/2)) + " " + (rayon+(1.0/2)) + ")");
			Point p;
			//theoricalSize = 0;
			int j=0, i=0;
			double d;
			for (double y=0.5; y<width; y++) {
				for (double x=0.5; x<width; x++) {
					p = (Point) wkt.read("POINT (" + x + " " + y + ")");
					d = center.distance(p);
					if(d > rayon){
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
		
		//DistanceFunction function = new DistanceFunction("", rayon*Raster.getCellSize());
		weighted = new double[width][width];
		for(int j=0; j<width; j++){
			for(int i=0; i<width; i++){
				weighted[j][i] = getDistanceFunction().interprete(distance[j][i]);
			}
		}
	}
	
	protected void initWeightedCouple() {
		weightedH = new double[width][width-1];
		for(int j=0; j<width; j++){
			for(int i=0; i<width-1; i++){
				weightedH[j][i] = getDistanceFunction().interprete((distance[j][i] + distance[j][i+1]) / 2);
			}
		}
		
		weightedV = new double[width-1][width];
		for(int j=0; j<width-1; j++){
			for(int i=0; i<width; i++){
				weightedV[j][i] = getDistanceFunction().interprete((distance[j][i] + distance[j+1][i]) / 2);
			}
		}
	}
	
	public String getStringFilter(){
		StringBuffer sb = new StringBuffer();
		for(int f : filter){
			sb.append(f);
		}
		return sb.toString();
	}
	
	protected int[] filters(){
		return filter;
	}
	
	@Override
	public int filter(int wx, int wy){
		return filter[wy * width + wx];
	}
	
	@Override
	public int theoreticalSize(){
		//return theoricalSize;
		throw new UnsupportedOperationException();
	}
	
	public void display(){
		int index=0;
		for(int f : filter){
			System.out.print(f+" ");
			index++;
			if(index%width == 0){
				System.out.println();
			}
		}
		System.out.println();
	}
	
	@Override
	public void export(Pixel p, Matrix m, String path){
		try {
			String name = new File(m.getFile()).getName().replace(".asc", "");
			
			String file = "";
			if(p instanceof PixelWithID){
				file = path+name+"_functional_"+width+"_"+((PixelWithID) p).getId()+".asc";
			}else{
				file = path+name+"_functional_"+width+"_"+CoordinateManager.getProjectedX(m, p.x())+"-"+CoordinateManager.getProjectedY(m, p.y())+".asc";
			}
			
			//System.out.print(locate.x()+" "+CoordinateManager.getProjectedX(m, locate.x()));
			//System.out.println(", "+locate.y()+" "+CoordinateManager.getProjectedY(m, locate.y()));
			//System.out.println(theoricalSize);
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
			
			double delta = width/2.0;
			double X = CoordinateManager.getProjectedX(m, p.x());
			double nX = X - delta * Raster.getCellSize();
			
			double Y = CoordinateManager.getProjectedY(m, p.y());
			double nY = Y - delta * Raster.getCellSize();
			
			out.write("ncols ");
			out.write(width+"");
			out.newLine();
			out.write("nrows ");
			out.write(width+"");
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
			out.write("NODATA_value  ");
			out.write(Raster.getNoDataValue()+"");
			out.newLine();
			
			int index = 0;
			for(int j=p.y()-width/2; j<p.y()+1+width/2; j++){
				for(int i=p.x()-width/2; i<p.x()+1+width/2; i++){
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
				if(index%width == 0){
					out.newLine();
				}
			}*/
			
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int width() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int height() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean equals(Object other){
	if(other instanceof LocateFunctionalWindow){
		LocateFunctionalWindow l = (LocateFunctionalWindow) other;
		for(int s=0; s<filter.length; s++){
			if(filter[s] != l.filter[s]){
				return false;
			}
		}
		return true;
	}
	return false;
	}
	
	@Override
	public int hashCode(){
		int h = 0;
		for(int s=0; s<filter.length; s++){
			h += filter[s];
		}
		return h;
	}
	
	@Override
	public List<Pixel> removeDownList() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public List<Pixel> addDownList() {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Pixel> removeHorizontalDownList() {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Pixel> addHorizontalDownList() {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Pixel> removeVerticalDownList() {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Pixel> addVerticalDownList() {
		throw new UnsupportedOperationException();
	}


	@Override
	public void infos() {
		
	}
	
}
