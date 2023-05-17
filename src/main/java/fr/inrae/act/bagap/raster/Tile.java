package fr.inrae.act.bagap.raster;

import org.locationtech.jts.geom.Envelope;

public class Tile {

	private Envelope envelope;
	
	private int ncols, nrows;
	
	private double tileLength;
	
	public Tile(Envelope envelope, int ncols, int nrows, double tileLength){
		this.envelope = envelope;
		this.ncols = ncols;
		this.nrows = nrows;
		this.tileLength = tileLength;
	}
	
	@Override
	public String toString(){
		return envelope+" "+ncols+" "+nrows+" "+tileLength;
	}
	
	public Envelope getEnvelope() {
		return envelope;
	}
	
	public Envelope getEnvelope(int i, int j){
		//System.out.println(i+" "+j+" "+(envelope.getMinX()+(i+1)*tileLength)+" "+(envelope.getMaxY()-(j+1)*tileLength));
		return new Envelope(envelope.getMinX()+i*tileLength, envelope.getMinX()+(i+1)*tileLength, envelope.getMaxY()-(j+1)*tileLength, envelope.getMaxY()-j*tileLength);
	}
	
	public double getMinX(){
		return envelope.getMinX();
	}
	
	public double getMaxX(){
		return envelope.getMaxX();
	}
	
	public double getMinY(){
		return envelope.getMinY();
	}
	
	public double getMaxY(){
		return envelope.getMaxY();
	}

	public int getNcols() {
		return ncols;
	}

	public int getNrows() {
		return nrows;
	}

	public double getTileLength() {
		return tileLength;
	}
	
	/*
	public int getTileWidth(double cellSize) {
		return (int) Math.round(tileSize / cellSize);
	}
	
	public int getTileHeight(double cellSize){
		return (int) Math.round(tileSize / cellSize);
	}
	*/

	public static Tile getTile(String tileFolder){
		return getTile((TileCoverage) CoverageManager.getCoverage(tileFolder));
	}
	
	public static Tile getTile(TileCoverage tileCoverage){
		
		Envelope envelope = new Envelope(tileCoverage.getEntete().minx(), tileCoverage.getEntete().maxx(), tileCoverage.getEntete().miny(), tileCoverage.getEntete().maxy());
		int ncols = tileCoverage.ncols();
		int nrows = tileCoverage.nrows();
		double tileSize = tileCoverage.tileSize();
		
		return new Tile(envelope, ncols, nrows, tileSize);
	}
	
	public static Tile getTile(Tile refTile, Envelope envelope){
		int deltaMinX = new Double((envelope.getMinX() - refTile.getMinX())/refTile.tileLength).intValue();
		int deltaMaxX = new Double((refTile.getMaxX() - envelope.getMaxX())/refTile.tileLength).intValue();
		int deltaMinY = new Double((envelope.getMinY() - refTile.getMinY())/refTile.tileLength).intValue();
		int deltaMaxY = new Double((refTile.getMaxY() - envelope.getMaxY())/refTile.tileLength).intValue();
		
		//System.out.println(deltaMinX+" "+deltaMaxX+" "+deltaMinY+" "+deltaMaxY);
		
		if(deltaMinX < 0){
			deltaMinX--;
		}
		double minX = refTile.getMinX() + deltaMinX*refTile.tileLength;
		
		if(deltaMaxX < 0){
			deltaMaxX--;
		}
		double maxX = refTile.getMaxX() - deltaMaxX*refTile.tileLength;
		
		if(deltaMinY < 0){
			deltaMinY--;
		}
		double minY = refTile.getMinY() + deltaMinY*refTile.tileLength;
		
		if(deltaMaxY < 0){
			deltaMaxY--;
		}
		double maxY = refTile.getMaxY() - deltaMaxY*refTile.tileLength;
		//System.out.println(minX+" "+maxX+" "+minY+" "+maxY);
		
		Envelope env = new Envelope(minX, maxX, minY, maxY);
		int ncols = refTile.ncols - deltaMinX - deltaMaxX;
		int nrows = refTile.nrows - deltaMinY - deltaMaxY;
		
		//System.out.println(ncols+" "+nrows);
		
		return new Tile(env, ncols, nrows, refTile.tileLength);
	}
	
}
