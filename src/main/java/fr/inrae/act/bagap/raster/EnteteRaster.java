package fr.inrae.act.bagap.raster;

public class EnteteRaster {

	private int width, height;
	
	private double minx, maxx, miny, maxy;
	
	private float cellsize;
	
	private int noDataValue;
	
	public EnteteRaster(int width, int height, double minx, double maxx, double miny, double maxy, float cellsize, int noDataValue){
		this.width = width;
		this.height = height;
		this.minx = minx;
		this.maxx = maxx;
		this.miny = miny;
		this.maxy = maxy;
		this.cellsize = cellsize;
		this.noDataValue = noDataValue;
	}
	
	public int width(){
		return width;
	}
	
	public int height(){
		return height;
	}
	
	public double minx(){
		return minx;
	}
	
	public double maxx(){
		return maxx;
	}
	
	public double miny(){
		return miny;
	}
	
	public double maxy(){
		return maxy;
	}
	
	public float cellsize(){
		return cellsize;
	}
	
	public int noDataValue(){
		return noDataValue;
	}
	
}
