package fr.inrae.act.bagap.raster;

import java.awt.Rectangle;
import java.util.Arrays;
import java.util.Set;

import org.locationtech.jts.geom.Envelope;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;

public class TileCoverage extends Coverage {
	
	private int ncols, nrows, tileWidth, tileHeight;
	
	private Coverage[] grid; 
	
	public TileCoverage(Set<Coverage> tiles, EnteteRaster entete) {
		super(entete);
		initGrid(tiles);
	}
	
	public int ncols(){
		return ncols;
	}
	
	public int nrows(){
		return nrows;
	}
	
	public double tileSize(){
		return tileWidth * getEntete().cellsize();
	}
	
	private void initGrid(Set<Coverage> tiles){
		
		Coverage cov = tiles.iterator().next();
		tileWidth = cov.width();
		tileHeight = cov.height();
		//System.out.println("tile : "+tileWidth+" "+tileHeight);
		
		ncols = new Double(Math.round(getEntete().maxx() - getEntete().minx()) / (tileWidth * (int) getEntete().cellsize())).intValue();
		if(Math.round(getEntete().maxx() - getEntete().minx()) % (tileWidth * (int) getEntete().cellsize()) != 0){
			ncols++;
		}
		nrows = new Double(Math.round(getEntete().maxy() - getEntete().miny()) / (tileHeight * (int) getEntete().cellsize())).intValue();
		if(Math.round(getEntete().maxy() - getEntete().miny()) % (tileHeight * (int) getEntete().cellsize()) != 0){
			nrows++;
		}
		
		//System.out.println("tableau : "+ncols+" "+nrows);
		grid = new Coverage[ncols*nrows];
		int posX, posY;
		for(Coverage tile : tiles){
			posX = new Double((tile.getEntete().minx() - getEntete().minx()) / (tileWidth * (int) getEntete().cellsize())).intValue();
			posY = new Double((getEntete().maxy() - tile.getEntete().maxy()) / (tileHeight * (int) getEntete().cellsize())).intValue();
			
			//System.out.println(posX+" "+posY);
			grid[posY*ncols + posX] = tile;
		}
		
	}

	@Override
	public float[] getDatas() {
		float[] datas = new float[width()*height()];
		float[] d;
		Coverage tile;
		for(int j=0; j<nrows; j++){
			for(int i=0; i<ncols; i++){
				tile = grid[j*ncols + i];
				if(tile != null){
					d = tile.getDatas();
					for(int y=0; y<tileHeight; y++){
						for(int x=0; x<tileWidth; x++){
							datas[((j*tileHeight)+y)*width() + ((i*tileWidth)+x)] = d[y*tileWidth + x];
						}
					}
				}else{
					for(int y=0; y<tileHeight; y++){
						for(int x=0; x<tileWidth; x++){
							datas[((j*tileHeight)+y)*width() + ((i*tileWidth)+x)] = Raster.getNoDataValue();
						}
					}
				}
			}	
		}
		
		return datas;
	}

	@Override
	public float[] getDatas(Rectangle roi) {
		
		EnteteRaster roiEntete = EnteteRaster.getEntete(getEntete(), roi); 
		Envelope roiEnv = roiEntete.getEnvelope();
		//System.out.println("enveloppe globale : "+roiEnv);
		
		float[] datas = new float[roiEntete.width()*roiEntete.height()];
		Arrays.fill(datas, Raster.getNoDataValue());
		
		Envelope tileEnv, localEnv;
		Coverage tile;
		Rectangle localRoi, tileRoi;
		float[] localData;
		for(int j=0; j<nrows; j++){
			for(int i=0; i<ncols; i++){
				tile = grid[j*ncols + i];
				if(tile != null){
					tileEnv = tile.getEntete().getEnvelope();
					if(roiEnv.intersects(tileEnv)){
						localEnv = roiEnv.intersection(tileEnv);
						if(!(localEnv.getMinX() == localEnv.getMaxX() || localEnv.getMinY() == localEnv.getMaxY())){
							localRoi = EnteteRaster.getROI(tile.getEntete(), localEnv);
							localData = tile.getDatas(localRoi);
							tileRoi = EnteteRaster.getROI(roiEntete, localEnv);
							for(int y=0; y<tileRoi.height; y++){
								for(int x=0; x<tileRoi.width; x++){
									datas[(y+tileRoi.y)*roiEntete.width() + x+tileRoi.x] = localData[y*localRoi.width + x];
								}
							}
						}
					}
				}
			}
		}
		
		return datas;
	}

	@Override
	public void dispose() {
		for(Coverage tile : grid){
			if(tile != null){
				tile.dispose();
			}
		}
		grid = null;
	}

}
