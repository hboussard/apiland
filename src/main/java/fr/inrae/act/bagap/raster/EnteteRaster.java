package fr.inrae.act.bagap.raster;

import java.awt.Rectangle;
import java.util.Set;

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
	
	@Override
	public String toString(){
		return width+" "+height+" "+minx+" "+maxx+" "+miny+" "+maxy+" "+noDataValue;
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
	
	public Envelope getEnvelope(){
		return new Envelope(minx, maxx, miny, maxy);
	}
	
	public static EnteteRaster getEntete(EnteteRaster refEntete, Rectangle roi){
		double minX = refEntete.minx + (roi.x * refEntete.cellsize);
		double maxX = minX + (roi.width * refEntete.cellsize);
		double maxY = refEntete.maxy - (roi.y * refEntete.cellsize);
		double minY = maxY - (roi.height * refEntete.cellsize);
		return new EnteteRaster(roi.width, roi.height, minX, maxX, minY, maxY, refEntete.cellsize, refEntete.noDataValue);
	}
	
	public static EnteteRaster getEntete(Envelope envelope, float cellsize, int noDataValue) {
		int ncols = new Double(Math.round(envelope.getMaxX() - envelope.getMinX()) / cellsize).intValue();
		if(Math.round(envelope.getMaxX() - envelope.getMinX()) % cellsize != 0){
			ncols++;
		}
		int nrows = new Double(Math.round(envelope.getMaxY() - envelope.getMinY()) / cellsize).intValue();
		if(Math.round(envelope.getMaxY() - envelope.getMinY()) % cellsize != 0){
			nrows++;
		}
		return new EnteteRaster(ncols, nrows, envelope.getMinX(), envelope.getMinX() + ncols*cellsize, envelope.getMinY(), envelope.getMinY() + nrows*cellsize, cellsize, noDataValue);
	}
	
	public static EnteteRaster sum(EnteteRaster entete1, EnteteRaster entete2){
		if(entete1.cellsize != entete2.cellsize 
				|| entete1.noDataValue != entete2.noDataValue){
			throw new IllegalArgumentException();
		}
		double minx = Math.min(entete1.minx, entete2.minx);
		double maxx = Math.max(entete1.maxx, entete2.maxx);
		double miny = Math.min(entete1.miny, entete2.miny);
		double maxy = Math.max(entete1.maxy, entete2.maxy);
		
		return getEntete(new Envelope(minx, maxx, miny, maxy), entete1.cellsize, entete1.noDataValue);
	}

	public static EnteteRaster getEntete(Set<EnteteRaster> entetes) {
		
		EnteteRaster entete = entetes.iterator().next();
		
		for(EnteteRaster e : entetes){
			entete = sum(entete, e);
		}
		
		return entete;
	}
	
	public static Rectangle getROI(EnteteRaster refEntete, Envelope env) {
		int ncols = new Double(Math.round(env.getMaxX() - env.getMinX()) / refEntete.cellsize).intValue();
		if(Math.round(env.getMaxX() - env.getMinX()) % refEntete.cellsize != 0){
			ncols++;
		}
		
		int nrows = new Double(Math.round(env.getMaxY() - env.getMinY()) / refEntete.cellsize).intValue();
		if(Math.round(env.getMaxY() - env.getMinY()) % refEntete.cellsize != 0){
			nrows++;
		}
		
		int x =   new Double(Math.round(env.getMinX() - refEntete.minx) / refEntete.cellsize).intValue();
		
		int y =   new Double(Math.round(refEntete.maxy - env.getMaxY()) / refEntete.cellsize).intValue();
		
		return new Rectangle(x, y, ncols, nrows);
	}
	
	public static EnteteRaster getEntete(EnteteRaster refEntete, Envelope envelope){
		int deltaMinX = new Double((envelope.getMinX() - refEntete.minx)/refEntete.cellsize).intValue();
		int deltaMaxX = new Double((refEntete.maxx - envelope.getMaxX())/refEntete.cellsize).intValue();
		int deltaMinY = new Double((envelope.getMinY() - refEntete.miny)/refEntete.cellsize).intValue();
		int deltaMaxY = new Double((refEntete.maxy - envelope.getMaxY())/refEntete.cellsize).intValue();
		
		//System.out.println(deltaMinX+" "+deltaMaxX+" "+deltaMinY+" "+deltaMaxY);
		
		if(deltaMinX < 0){
			deltaMinX--;
		}
		double minX = refEntete.minx + deltaMinX*refEntete.cellsize;
		
		if(deltaMaxX < 0){
			deltaMaxX--;
		}
		double maxX = refEntete.maxx - deltaMaxX*refEntete.cellsize;
		
		if(deltaMinY < 0){
			deltaMinY--;
		}
		double minY = refEntete.miny + deltaMinY*refEntete.cellsize;
		
		if(deltaMaxY < 0){
			deltaMaxY--;
		}
		double maxY = refEntete.maxy - deltaMaxY*refEntete.cellsize;
		//System.out.println(minX+" "+maxX+" "+minY+" "+maxY);
		
		int ncols = refEntete.width - deltaMinX - deltaMaxX;
		int nrows = refEntete.height - deltaMinY - deltaMaxY;
		
		//System.out.println(ncols+" "+nrows);
		
		return new EnteteRaster(ncols, nrows, minX, maxX, minY, maxY, refEntete.cellsize, refEntete.noDataValue);
	}

	/*
	public static EnteteRaster getEntete(Envelope envelope, float cellsize, int noDataValue) {
		
		int ncols;
		if(((((int)(envelope.getMaxX()*1000))/1000) - (((int)(envelope.getMinX()*1000))/1000))%cellsize == 0){
			ncols = new Double((Math.floor((envelope.getMaxX() - envelope.getMinX()) / cellsize))).intValue();
		}else{
			ncols = new Double((Math.floor((envelope.getMaxX() - envelope.getMinX()) / cellsize)) + 1).intValue();
		}
		int nrows;
		if(((((int)(envelope.getMaxY()*1000))/1000) - (((int)(envelope.getMinY()*1000)/1000)))%cellsize == 0){
			nrows = new Double((Math.floor((envelope.getMaxY() - envelope.getMinY()) / cellsize))).intValue();
		}else{
			nrows = new Double((Math.floor((envelope.getMaxY() - envelope.getMinY()) / cellsize)) + 1).intValue();
		}
		return new EnteteRaster(ncols, nrows, envelope.getMinX(), envelope.getMinX() + ncols*cellsize, envelope.getMinY(), envelope.getMinY() + nrows*cellsize, cellsize, noDataValue);
		//return new EnteteRaster(ncols, nrows, envelope.getMinX()-cellsize/2.0, envelope.getMinX()-cellsize/2.0 + ncols*cellsize, envelope.getMinY()-cellsize/2.0, envelope.getMinY()-cellsize/2.0 + nrows*cellsize, cellsize, noDataValue);
	}
	*/
	
}
