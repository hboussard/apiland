package fr.inrae.act.bagap.raster;

import org.locationtech.jts.geom.Envelope;

public class Tile {

	private Envelope envelope;
	
	private int ncols, nrows;
	
	private double tileSize;
	
	public Tile(Envelope envelope, int ncols, int nrows, double tileSize){
		this.envelope = envelope;
		this.ncols = ncols;
		this.nrows = nrows;
		this.tileSize = tileSize;
	}
	
	@Override
	public String toString(){
		return envelope+" "+ncols+" "+nrows+" "+tileSize;
	}
	
	public Envelope getEnvelope() {
		return envelope;
	}
	
	public Envelope getEnvelope(int i, int j){
		return new Envelope(envelope.getMinX()+i*tileSize, envelope.getMinX()+(i+1)*tileSize, envelope.getMaxY()-(j+1)*tileSize, envelope.getMaxY()-j*tileSize);
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

	public double getTileSize() {
		return tileSize;
	}

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
		int deltaMinX = new Double((envelope.getMinX() - refTile.getMinX())/refTile.tileSize).intValue();
		int deltaMaxX = new Double((refTile.getMaxX() - envelope.getMaxX())/refTile.tileSize).intValue();
		int deltaMinY = new Double((envelope.getMinY() - refTile.getMinY())/refTile.tileSize).intValue();
		int deltaMaxY = new Double((refTile.getMaxY() - envelope.getMaxY())/refTile.tileSize).intValue();
		
		System.out.println(deltaMinX+" "+deltaMaxX+" "+deltaMinY+" "+deltaMaxY);
		
		if(deltaMinX < 0){
			deltaMinX--;
		}
		double minX = refTile.getMinX() + deltaMinX*refTile.tileSize;
		
		if(deltaMaxX < 0){
			deltaMaxX--;
		}
		double maxX = refTile.getMaxX() - deltaMaxX*refTile.tileSize;
		
		if(deltaMinY < 0){
			deltaMinY--;
		}
		double minY = refTile.getMinY() + deltaMinY*refTile.tileSize;
		
		if(deltaMaxY < 0){
			deltaMaxY--;
		}
		double maxY = refTile.getMaxY() - deltaMaxY*refTile.tileSize;
		System.out.println(minX+" "+maxX+" "+minY+" "+maxY);
		
		Envelope env = new Envelope(minX, maxX, minY, maxY);
		int ncols = refTile.ncols - deltaMinX - deltaMaxX;
		int nrows = refTile.nrows - deltaMinY - deltaMaxY;
		
		System.out.println(ncols+" "+nrows);
		
		return new Tile(env, ncols, nrows, refTile.getTileSize());
	}
	
}
