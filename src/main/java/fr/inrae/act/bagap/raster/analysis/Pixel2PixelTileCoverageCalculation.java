package fr.inrae.act.bagap.raster.analysis;

import java.io.File;

import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.CoverageManager;
import fr.inrae.act.bagap.raster.EnteteRaster;
import fr.inrae.act.bagap.raster.TileCoverage;

public abstract class Pixel2PixelTileCoverageCalculation {
	
	private String outputFolder;
	
	private Coverage[] inCoverage;
	
	public Pixel2PixelTileCoverageCalculation(String outputFolder, Coverage... inCoverage){
		this.outputFolder = outputFolder;
		this.inCoverage = inCoverage;
	}
	
	public void run(){
			
		String name = new File(outputFolder).getName();
		
		float[] v = new float[inCoverage.length];
		EnteteRaster localEntete;
		int ncols = ((TileCoverage) inCoverage[0]).ncols();
		int nrows = ((TileCoverage) inCoverage[0]).nrows();
		int size = (int) Math.pow(((TileCoverage) inCoverage[0]).tileWidth(), 2);
		float[] outData;
		Coverage[] localCoverage = new Coverage[inCoverage.length];
		float[][] localDatas = new float[inCoverage.length][size];
		boolean pass;
		for(int y=0; y<nrows; y++){
			for(int x=0; x<ncols; x++){
				
				pass = true;
				for(int j=0; j<v.length; j++){
					localCoverage[j] = ((TileCoverage) inCoverage[j]).getCoverage(x, y);
					if(localCoverage[j] == null){
						pass = false;
						if(j==1){
							System.out.println("bug en "+localCoverage[0].getEntete());
						}
						break;
					}
				}
				
				if(pass){
					
					outData = new float[size];
					
					localEntete = localCoverage[0].getEntete();
					for(int j=0; j<v.length; j++){
						localDatas[j] = localCoverage[j].getData();
						localCoverage[j].dispose();
					}
					
					for(int i=0; i<size; i++){
						for(int j=0; j<v.length; j++){
							v[j] = localDatas[j][i];
						}
						outData[i] = doTreat(v);
					}
					
					CoverageManager.writeGeotiff(outputFolder+name+"_"+((int) localEntete.minx()/1000)+"_"+((int) localEntete.maxy()/1000)+".tif", outData, localEntete);
				}
			}	
		}	
	}

	protected abstract float doTreat(float[] v);
	
	/*
	 * public void run(){
		if(inCoverage[0] instanceof TileCoverage){ // gerer par tuile
			
			float[] v = new float[inCoverage.length];
			EnteteRaster entete = inCoverage[0].getEntete();
			int ncols = ((TileCoverage) inCoverage[0]).ncols();
			int nrows = ((TileCoverage) inCoverage[0]).nrows();
			int size = (int) (tile.getTileSize() / entete.cellsize());
			float[] outData = new float[size];
			Coverage[] localTile = new Coverage[inCoverage.length];
			for(int y=0; y<nrows; y++){
				for(int x=0; x<ncols; x++){
					if(((TileCoverage) inCoverage[0]).getCoverage(x, y) != null){
						
						for(int j=0; j<v.length; j++){
							localTile[j] = ((TileCoverage) inCoverage[j]).getCoverage(x, y);
						}
						
						for(int i=0; i<size; i++){
							for(int j=0; j<v.length; j++){
								v[j] = localTile[j].getDatas()[i];
							}
							outData[i] = doTreat(v);
						}
						
						
						
					}
				}	
			}
			
			
			
			
			
		}else if(inCoverage[0] instanceof FileCoverage){ // gerer par parties

			// TODO
			
		}else{ // TabCoverage, gerer en entier
			float[] v = new float[inCoverage.length];
			int size = inCoverage[0].width()*inCoverage[0].height();
			float[] outData = new float[size];
			for(int i=0; i<size; i++){
				for(int j=0; j<v.length; j++){
					v[j] = inCoverage[j].getDatas()[i];
				}
				outData[i] = doTreat(v);
			}
			
			EnteteRaster entete = inCoverage[0].getEntete();
			outCoverage = new TabCoverage(outData, entete);
		}
	}
	
	public Coverage getResult(){
		return outCoverage;
	}
	 */
}