package fr.inrae.act.bagap.raster;

import org.locationtech.jts.geom.Envelope;

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
	
	public void setNoDataValue(int noDataValue){
		this.noDataValue = noDataValue;
	}
	
	public static EnteteRaster getEntete(Envelope envelope, float cellsize, int noDataValue) {
		
		int ncols;
		if((envelope.getMaxX() - envelope.getMinX())%cellsize == 0){
			ncols = new Double((Math.floor((envelope.getMaxX() - envelope.getMinX()) / cellsize))).intValue();
		}else{
			ncols = new Double((Math.floor((envelope.getMaxX() - envelope.getMinX()) / cellsize)) + 1).intValue();
		}
		int nrows;
		if((envelope.getMaxY() - envelope.getMinY())%cellsize == 0){
			nrows = new Double((Math.floor((envelope.getMaxY() - envelope.getMinY()) / cellsize))).intValue();
		}else{
			nrows = new Double((Math.floor((envelope.getMaxY() - envelope.getMinY()) / cellsize)) + 1).intValue();
		}
		return new EnteteRaster(ncols, nrows, envelope.getMinX(), envelope.getMaxX(), envelope.getMinY(), envelope.getMaxY(), cellsize, noDataValue);
	}
}
