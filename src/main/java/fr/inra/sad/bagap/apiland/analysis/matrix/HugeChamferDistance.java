package fr.inra.sad.bagap.apiland.analysis.matrix;

import java.awt.image.DataBuffer;
import java.awt.image.WritableRaster;
import java.awt.image.WritableRenderedImage;
import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.gce.geotiff.GeoTiffFormat;
import org.geotools.gce.geotiff.GeoTiffWriteParams;
import org.geotools.gce.geotiff.GeoTiffWriter;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValueGroup;

import com.sun.media.jai.codecimpl.util.RasterFactory;

import fr.inra.sad.bagap.apiland.analysis.Analysis;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;

public class HugeChamferDistance extends Analysis {

	private String input;
	
	private Collection<Integer> codes;
	
	private GridCoverage2D inCoverage/*, outCoverage*/;
	
	private float threshold;
	
	private int maxTile = 10000;
	
	private String path = "F:/Requete_SIG_LabPSE/raster/";
	//private String path = "C:/Hugues/data/ascii/qualitative_complete/td/tile/";
	
	public static void main(String[] args){
		/*
		String file = "F:/Requete_SIG_LabPSE/raster/occsol_sm.asc";
		Set<Integer> c = new HashSet<Integer>();
		//c.add(1);
		//c.add(2);
		c.add(3);
		c.add(4);
		c.add(5);
		Raster.setNoDataValue(-1);
		new HugeChamferDistance(file, c).allRun();
		*/
		/*
		String file = "C:/Hugues/data/ascii/qualitative_complete/raster2007.tif";
		Set<Integer> c = new HashSet<Integer>();
		c.add(1);
		
		new HugeChamferDistance(file, c).allRun();
		
		*/
	
		compile();
	}
	
	
	private static void compile() {
		String path = "F:/Requete_SIG_LabPSE/raster/";
		String file = "F:/Requete_SIG_LabPSE/raster/occsol_sm.asc";
		GridCoverage2D inC = CoverageManager.get(file);
		int width = (int) inC.getProperty("image_width");
		int height = (int) inC.getProperty("image_height");
		double imageMinX = inC.getEnvelope().getMinimum(0);
		double imageMinY = inC.getEnvelope().getMinimum(1);
		double imageMaxX = inC.getEnvelope().getMaximum(0);
		double imageMaxY = inC.getEnvelope().getMaximum(1);
		float cellSize = (float) ((java.awt.geom.AffineTransform) inC.getGridGeometry().getGridToCRS2D()).getScaleX();
		
		inC.dispose(true);
		inC = null;
		
		WritableRaster raster = RasterFactory.createBandedRaster(DataBuffer.TYPE_FLOAT, width, height, 1, null);
		
		// tuilage à priori
		int maxTile = 10000;
		int dx = 0, dy = 0;
		int dWidth = 0, dHeight = 0;
		for(int y=0; y<height; dy++, y+=maxTile-1){
			dx = 0;
			dWidth = 0;
			for(int x=0; x<width; dx++, x+=maxTile-1){
				dWidth++;
			}
			dHeight++;
		}
		
		dx = 0;
		dy = 0;
		for(int y=0; y<height; dy++, y+=maxTile-1){
			dx = 0;
			for(int x=0; x<width; dx++, x+=maxTile-1){
				
				int roiWidth = Math.min(maxTile, width - x);
				int roiHeight = Math.min(maxTile, height - y);
				double roiPosX = imageMinX + x*cellSize;
				double roiPosMaxX = roiPosX + roiWidth * cellSize;
				double roiPosY = Math.max(imageMinY, imageMaxY - (y+roiHeight)*cellSize);
				double roiPosMaxY = roiPosY + roiHeight * cellSize;
				
				System.out.println("traitement de la tuile "+dx+" "+dy+" / "+dWidth+" "+dHeight+" "+x+" "+y+" "+roiWidth+" "+roiHeight);
				
				GridCoverage2D cov = CoverageManager.get(path+"tile-"+x+"-"+y+".tif");
				float[] localDatas = CoverageManager.getData(cov, 0, 0, roiWidth, roiHeight);
				
				raster.setSamples(x, y, roiWidth, roiHeight, 0, localDatas);
			}
		}
		
		GridCoverage2D outC = CoverageManager.getCoverageUsingRaster(raster, width, height, imageMinX, imageMaxX, imageMinY, imageMaxY, cellSize);
		
		//GridCoverage2D outC = CoverageManager.getEmptyCoverage(width, height, imageMinX, imageMaxX, imageMinY, imageMaxY, cellSize);
		//((WritableRenderedImage) outC.getRenderedImage()).setData(raster);
		
		
		System.out.println("écriture sur fichier");
		try {
			GeoTiffWriteParams wp = new GeoTiffWriteParams();
			wp.setCompressionMode(GeoTiffWriteParams.MODE_EXPLICIT);
			//wp.setCompressionType("LZW");
			ParameterValueGroup params = new GeoTiffFormat().getWriteParameters();
			params.parameter(AbstractGridFormat.GEOTOOLS_WRITE_PARAMS.getName().toString()).setValue(wp);
			GeoTiffWriter writer = new GeoTiffWriter(new File(path+"distance_bocage.tif"));
			writer.write(outC, params.values().toArray(new GeneralParameterValue[1]));
			writer.dispose();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


	public HugeChamferDistance(String input, Collection<Integer> codes) {
		this(input, codes, Raster.getNoDataValue());
	}
	
	public HugeChamferDistance(String input, Collection<Integer> codes, float threshold) {
		this.input = input;
		this.codes = codes;
		this.threshold = threshold;
	}
	
	@Override
	protected void doInit() {
		inCoverage = CoverageManager.get(input);
	}
	
	@Override
	protected void doRun() {
		int width = (int) inCoverage.getProperty("image_width");
		int height = (int) inCoverage.getProperty("image_height");
		double imageMinX = inCoverage.getEnvelope().getMinimum(0);
		double imageMinY = inCoverage.getEnvelope().getMinimum(1);
		double imageMaxX = inCoverage.getEnvelope().getMaximum(0);
		double imageMaxY = inCoverage.getEnvelope().getMaximum(1);
		float cellSize = (float) ((java.awt.geom.AffineTransform) inCoverage.getGridGeometry().getGridToCRS2D()).getScaleX();
		
		//inCoverage = null;		
		//outCoverage = CoverageManager.getCoverageFromData(new float[width*height], width, height, imageMinX, imageMinY, cellSize);
		
		int dWidth = 0, dHeight = 0;
		Map<Pixel, Map<String, float[]>> bords = new HashMap<Pixel, Map<String, float[]>>();
		
		boolean finish = false;
		int pass = 1;
		while(!finish){
			
			System.out.println(pass);
			
			if(pass == 1){
				
				// gestion des codes en tableau
				int[] code = new int[codes.size()];
				int index = 0;
				for (int i : codes) {
					code[index++] = i;
				}
				
				// tuilage à priori
				int dx = 0, dy = 0;
				for(int y=0; y<height; dy++, y+=maxTile-1){
					dx = 0;
					dWidth = 0;
					for(int x=0; x<width; dx++, x+=maxTile-1){
						dWidth++;
						//System.out.println("creation des pixels "+dx+" "+dy);
						bords.put(new Pixel(dx, dy), new HashMap<String, float[]>());
					}
					dHeight++;
				}
				
				dx = 0;
				dy = 0;
				for(int y=0; y<height; dy++, y+=maxTile-1){
					dx = 0;
					for(int x=0; x<width; dx++, x+=maxTile-1){
						
						System.out.println("traitement de la tuile "+dx+" "+dy+" / "+dWidth+" "+dHeight);
						
						int roiWidth = Math.min(maxTile, width - x);
						int roiHeight = Math.min(maxTile, height - y);
						double roiPosX = imageMinX + x*cellSize;
						double roiPosMaxX = roiPosX + roiWidth * cellSize;
						double roiPosY = Math.max(imageMinY, imageMaxY - (y+roiHeight)*cellSize);
						//double roiPosY = Math.max(imageMinY, imageMaxY - ((dy+1)*maxTile-dy)*cellSize);
						double roiPosMaxY = roiPosY + roiHeight * cellSize;
						
						//System.out.println();
						//System.out.println(dx+" "+dy);
						//System.out.println(width+" "+height+" --> "+roiWidth+" "+roiHeight);
						//System.out.println(imageMinX+" "+imageMaxX+" --> "+roiPosX);
						//System.out.println(imageMinY+" "+imageMaxY+" --> "+roiPosY);
						//System.out.println(cellSize);
						
						//System.out.println("récupération des valeurs initiales");
						float[] inDatas = CoverageManager.getData(inCoverage, x, y, roiWidth, roiHeight);
						//float[] inDatas = CoverageManager.readData(input, x, x, roiWidth, roiHeight);
						//System.out.println("fait");
						float[] outDatas = new float[roiHeight*roiWidth];
						boolean hasValue = false;
						for (int yt = 0; yt < roiHeight; yt++) {
							for (int xt = 0; xt < roiWidth; xt++) {
								float v = inDatas[yt*roiWidth+xt];
								boolean ok = false;
								if (v != Raster.getNoDataValue()) {
									for (int c : code) {
										if (c == v) {
											ok = true;
											hasValue = true;
											break;
										}
									}
									if (ok) {
										outDatas[yt * roiWidth + xt] = 0; // inside the object -> distance=0
									} else {
										outDatas[yt * roiWidth + xt] = -2; // outside the object -> to be computed
									}
								}else{
									outDatas[yt * roiWidth + xt] = Raster.getNoDataValue(); // nodata_value -> to be not computed
								}
								
							}
						}
						
						hasValue = true;
						if(hasValue){
							CoverageChamferDistance ccd = new CoverageChamferDistance(outDatas, roiWidth, roiHeight, roiPosX, roiPosY, cellSize, codes, threshold);
							outDatas = (float[]) ccd.allRun();
							
							// stockage des valeurs de voisinnage
							finish = true;
							Pixel p = new Pixel(dx, dy);
							//System.out.println("traitement de la tuile "+x+" "+y);
							if(dx > 0){ // le bord gauche de la tuile est inclu et partagé
								float[] bord = new float[roiHeight];
								
								Pixel pGauche = new Pixel(dx-1, dy);
								float[] bordVoisin = bords.get(pGauche).get("droite"); 
								
								for(int j=0; j<roiHeight; j++){
									if(bordVoisin[j] > outDatas[j*roiWidth+0]){
										bord[j] = outDatas[j*roiWidth+0];
										finish = false;
									}else if(bordVoisin[j] < outDatas[j*roiWidth+0]){
										bord[j] = bordVoisin[j];
										finish = false;
									}else{
										bord[j] = outDatas[j*roiWidth+0];
									}
								}
								
								bords.get(pGauche).put("droite", bord);
								
								if(dy > 0){ // le bord nord-gauche de la tuile est inclu et partagé
									Pixel pNordGauche = new Pixel(dx-1, dy-1);
									bordVoisin = bords.get(pNordGauche).get("droite");
									
									if(bordVoisin[roiHeight-1] < bord[0]){
										bord[0] = bordVoisin[roiHeight-1];
										finish = false;
									}else if(bordVoisin[roiHeight-1] > bord[0]){
										bordVoisin[roiHeight-1] = bord[0];
										finish = false;
										bords.get(pNordGauche).put("droite", bordVoisin);
									}
								}
								
								bords.get(p).put("gauche", bord);
							}
							if(dx < (dWidth-1)){ // le bord droite de la tuile est inclu et partagé
								float[] bord = new float[roiHeight];
								for(int j=0; j<roiHeight; j++){
									bord[j] = outDatas[j*roiWidth +(roiWidth-1)];
								}
								
								if(dy > 0){ // le bord nord-droite de la tuile est inclu et partagé
									Pixel pNordDroite = new Pixel(dx+1, dy-1);
									float[] bordVoisin = bords.get(pNordDroite).get("gauche");
									
									if(bordVoisin[roiHeight-1] < bord[0]){
										bord[0] = bordVoisin[roiHeight-1];
										finish = false;
									}else if(bordVoisin[roiHeight-1] > bord[0]){
										bordVoisin[roiHeight-1] = bord[0];
										finish = false;
										bords.get(pNordDroite).put("gauche", bordVoisin);
									}
								}
								
								bords.get(p).put("droite", bord);
							}
							if(dy > 0){ // le bord nord de la tuile est inclu et partagé
								float[] bord = new float[roiWidth];
								
								Pixel pNord = new Pixel(dx, dy-1);
								float[] bordVoisin = bords.get(pNord).get("sud"); 
								
								for(int i=0; i<roiWidth; i++){
									if(bordVoisin[i] > outDatas[i]){
										bord[i] = outDatas[i];
										finish = false;
									}else if(bordVoisin[i] < outDatas[i]){
										bord[i] = bordVoisin[i];
										finish = false;
									}else{
										bord[i] = outDatas[i];
									}
								}
								bords.get(p).put("nord", bord);
								bords.get(pNord).put("sud", bord);
								
							}
							if(dy < (dHeight-1)){ // le bord sud de la tuile est inclu et partagé
								float[] bord = new float[roiWidth];
								for(int i=0; i<roiWidth; i++){
									bord[i] = outDatas[(roiHeight-1)*roiWidth + i];
								}
								bords.get(p).put("sud", bord);
							}
						}
						
						// stockage en mémoire fichier de l'état de la tuile
						//GridCoverage2D distance = CoverageManager.getCoverageFromData2D(outDatas, roiWidth, roiHeight, roiPosX, roiPosY, cellSize);
						//CoverageManager.exportAsciiGrid(distance, path+"tile-"+x+"-"+y+".asc");
						//CoverageManager.exportTif(distance, path+"tile-"+x+"-"+y+".tif");
						
						CoverageManager.writeGeotiff(new File(path+"tile-"+x+"-"+y+".tif"), outDatas, roiWidth, roiHeight, roiPosX, roiPosMaxX, roiPosY, roiPosMaxY);
						
						//distance = null;
					}	
					
				}
				//finish = true;
			}else{
				
				finish = true;
				/*
				int dx = 0, dy = 0;
				for(int y=0; y<height; dy++, y+=maxTile-1){
					dx = 0;
					for(int x=0; x<width; dx++, x+=maxTile-1){
						
						//System.out.println("traitement de la tuile "+dx+" "+dy);
						
						int roiWidth = Math.min(maxTile, width - x);
						int roiHeight = Math.min(maxTile, height - y);
						double roiPosX = imageMinX + x*cellSize;
						double roiPosY = Math.max(imageMinY, imageMaxY - ((dy+1)*maxTile+1)*cellSize);
						
						
						
						
						GridCoverage2D cov = CoverageManager.get("C:/Hugues/data/ascii/qualitative_complete/td/tile/tile-"+x+"-"+y+".asc");
						float[] outDatas = CoverageManager.getData(cov, 0, 0, roiWidth, roiHeight);
						
						
						CoverageManager.setData(outCoverage, x, y, roiWidth, roiHeight, outDatas);
						float[] outDatas2 = CoverageManager.getData(outCoverage, x, y, roiWidth, roiHeight);
						
						for(int j=0; j<roiHeight; j++){
							for(int i=0; i<roiWidth; i++){
								System.out.println(x+" "+y+" "+roiWidth+" "+roiHeight+" "+outDatas[j*roiWidth+i]+" "+outDatas2[j*roiWidth+i]);
							}
						}
					}
				}
				
				CoverageManager.exportAsciiGrid(outCoverage, "C:/Hugues/data/ascii/qualitative_complete/td/tile/distance.asc");
				*/
				
				int dx = 0, dy = 0;
				for(int y=0; y<height; dy++, y+=maxTile-1){
					dx = 0;
					for(int x=0; x<width; dx++, x+=maxTile-1){
						
						System.out.println("traitement de la tuile "+dx+" "+dy+" / "+dWidth+" "+dHeight);
						
						//System.out.println("traitement de la tuile "+dx+" "+dy);
						
						int roiWidth = Math.min(maxTile, width - x);
						int roiHeight = Math.min(maxTile, height - y);
						double roiPosX = imageMinX + x*cellSize;
						double roiPosMaxX = roiPosX + roiWidth * cellSize;
						double roiPosY = Math.max(imageMinY, imageMaxY - (y+roiHeight)*cellSize);
						double roiPosMaxY = roiPosY + roiHeight * cellSize;
						
						
						//GridCoverage2D cov = CoverageManager.get(path+"tile-"+x+"-"+y+".asc");
						GridCoverage2D cov = CoverageManager.get(path+"tile-"+x+"-"+y+".tif");
						float[] outDatas = CoverageManager.getData(cov, 0, 0, roiWidth, roiHeight);
						cov = null;
						
						// vérification des mises à jour à faire
						boolean maj = false;
						Pixel p = new Pixel(dx, dy);
						if(dx > 0){ // le bord gauche de la tuile est inclu et partagé
							float[] bord = bords.get(p).get("gauche");
							for(int j=0; j<roiHeight; j++){
								if(bord[j] < outDatas[j*roiWidth+0]){
									outDatas[j*roiWidth+0] = bord[j]; 
									maj = true;
								}
							}
						}
						if(dx < (dWidth-1)){ // le bord droite de la tuile est inclu et partagé
							float[] bord = bords.get(p).get("droite");
							for(int j=0; j<roiHeight; j++){
								if(bord[j] < outDatas[j*roiWidth + (roiWidth-1)]){
									outDatas[j*roiWidth + (roiWidth-1)] = bord[j];
									maj = true;
								}
							}
						}
						if(dy > 0){ // le bord nord de la tuile est inclu et partagé
							float[] bord = bords.get(p).get("nord");
							for(int i=0; i<roiWidth; i++){
								if(bord[i] < outDatas[i]){
									outDatas[i] = bord[i];
									maj = true;
								}
							}
						}
						if(dy < (dHeight-1)){ // le bord sud de la tuile est inclu et partagé
							float[] bord = bords.get(p).get("sud");
							for(int i=0; i<roiWidth; i++){
								if(bord[i] < outDatas[(roiHeight-1)*roiWidth + i]){
									outDatas[(roiHeight-1)*roiWidth + i] = bord[i];
									maj = true;
								}
							}
						}
						
						if(maj){
							CoverageChamferDistance ccd = new CoverageChamferDistance(outDatas, roiWidth, roiHeight, roiPosX, roiPosY, cellSize, codes, threshold);
							outDatas = (float[]) ccd.allRun();
							
							// stockage des valeurs de voisinnage
							finish = true;
							p = new Pixel(dx, dy);
							//System.out.println("traitement de la tuile "+x+" "+y);
							if(dx > 0){ // le bord gauche de la tuile est inclu et partagé
								float[] bord = new float[roiHeight];
								
								Pixel pGauche = new Pixel(dx-1, dy);
								float[] bordVoisin = bords.get(pGauche).get("droite"); 
								
								for(int j=0; j<roiHeight; j++){
									if(bordVoisin[j] > outDatas[j * roiWidth + 0]){
										bord[j] = outDatas[j * roiWidth + 0];
										finish = false;
									}else if(bordVoisin[j] < outDatas[j * roiWidth + 0]){
										bord[j] = bordVoisin[j];
										finish = false;
									}else{
										bord[j] = outDatas[j * roiWidth + 0];
									}
								}
								
								bords.get(pGauche).put("droite", bord);
								
								if(dy > 0){ // le bord nord-gauche de la tuile est inclu et partagé
									Pixel pNordGauche = new Pixel(dx-1, dy-1);
									bordVoisin = bords.get(pNordGauche).get("droite");
									
									if(bordVoisin[roiHeight-1] < bord[0]){
										bord[0] = bordVoisin[roiHeight-1];
										finish = false;
									}else if(bordVoisin[roiHeight-1] > bord[0]){
										bordVoisin[roiHeight-1] = bord[0];
										finish = false;
										bords.get(pNordGauche).put("droite", bordVoisin);
									}
								}
								
								bords.get(p).put("gauche", bord);
							}
							if(dx < (dWidth-1)){ // le bord droite de la tuile est inclu et partagé
								float[] bord = new float[roiHeight];
								for(int j=0; j<roiHeight; j++){
									bord[j] = outDatas[j * roiWidth + (roiWidth-1)];
								}
								
								if(dy > 0){ // le bord nord-droite de la tuile est inclu et partagé
									Pixel pNordDroite = new Pixel(dx+1, dy-1);
									float[] bordVoisin = bords.get(pNordDroite).get("gauche");
									
									if(bordVoisin[roiHeight-1] < bord[0]){
										bord[0] = bordVoisin[roiHeight-1];
										finish = false;
									}else if(bordVoisin[roiHeight-1] > bord[0]){
										bordVoisin[roiHeight-1] = bord[0];
										finish = false;
										bords.get(pNordDroite).put("gauche", bordVoisin);
									}
								}
								
								bords.get(p).put("droite", bord);
							}
							if(dy > 0){ // le bord nord de la tuile est inclu et partagé
								float[] bord = new float[roiWidth];
								
								Pixel pNord = new Pixel(dx, dy-1);
								float[] bordVoisin = bords.get(pNord).get("sud"); 
								
								for(int i=0; i<roiWidth; i++){
									if(bordVoisin[i] > outDatas[i]){
										bord[i] = outDatas[i];
										finish = false;
									}else if(bordVoisin[i] < outDatas[i]){
										bord[i] = bordVoisin[i];
										finish = false;
									}else{
										bord[i] = outDatas[i];
									}
								}
								bords.get(p).put("nord", bord);
								bords.get(pNord).put("sud", bord);
								
							}
							if(dy < (dHeight-1)){ // le bord sud de la tuile est inclu et partagé
								float[] bord = new float[roiWidth];
								for(int i=0; i<roiWidth; i++){
									bord[i] = outDatas[(roiHeight-1)*roiWidth + i];
								}
								bords.get(p).put("sud", bord);
							}
							
							// stockage en mémoire fichier de l'état de la tuile
							//GridCoverage2D distance = CoverageManager.getCoverageFromData2D(outDatas, roiWidth, roiHeight, roiPosX, roiPosY, cellSize);
							//CoverageManager.exportAsciiGrid(distance, path+"tile-"+x+"-"+y+".asc");
							//CoverageManager.exportTif(distance, path+"tile-"+x+"-"+y+".tif");
							//distance = null;
							
							CoverageManager.writeGeotiff(new File(path+"tile-"+x+"-"+y+".tif"), outDatas, roiWidth, roiHeight, roiPosX, roiPosMaxX, roiPosY, roiPosMaxY);
						}
					}	
				}
				/*
				if(pass == 2){
					finish = true;
				}*/
				
			}
			
			pass++;
			
		}
		
		
		// nomalisation
		int dx = 0, dy = 0;
		for(int y=0; y<height; dy++, y+=maxTile-1){
			dx = 0;
			for(int x=0; x<width; dx++, x+=maxTile-1){
				
				//System.out.println("traitement de la tuile "+dx+" "+dy);
				
				int roiWidth = Math.min(maxTile, width - x);
				int roiHeight = Math.min(maxTile, height - y);
				double roiPosX = imageMinX + x*cellSize;
				double roiPosMaxX = roiPosX + roiWidth * cellSize;
				double roiPosY = Math.max(imageMinY, imageMaxY - (y+roiHeight)*cellSize);
				double roiPosMaxY = roiPosY + roiHeight * cellSize;
				
				
				//GridCoverage2D cov = CoverageManager.get(path+"tile-"+x+"-"+y+".asc");
				GridCoverage2D cov = CoverageManager.get(path+"tile-"+x+"-"+y+".tif");
				float[] outDatas = CoverageManager.getData(cov, 0, 0, roiWidth, roiHeight);
				cov = null;
				
				CoverageChamferDistance ccd = new CoverageChamferDistance(outDatas, roiWidth, roiHeight, roiPosX, roiPosY, cellSize, codes, threshold);
				outDatas = ccd.normalize();
				
				// stockage en mémoire fichier de l'état de la tuile
				//GridCoverage2D distance = CoverageManager.getCoverageFromData2D(outDatas, roiWidth, roiHeight, roiPosX, roiPosY, cellSize);
				//CoverageManager.exportAsciiGrid(distance, path+"tile-"+x+"-"+y+".asc");
				//CoverageManager.exportTif(distance, path+"tile-"+x+"-"+y+".tif");
				//distance = null;
				
				CoverageManager.writeGeotiff(new File(path+"tile-"+x+"-"+y+".tif"), outDatas, roiWidth, roiHeight, roiPosX, roiPosMaxX, roiPosY, roiPosMaxY);
			}
		}
		

		/*
		CoverageChamferDistance ccd = new CoverageChamferDistance(CoverageManager.getData(coverage, x, y, roiWidth, roiHeight), roiWidth, roiHeight, roiPosX, roiPosY, cellSize, codes, threshold);
		GridCoverage2D distance = (GridCoverage2D) ccd.allRun();
		CoverageManager.exportAsciiGrid(distance, "C:/Hugues/data/ascii/qualitative_complete/td/tile/tile-"+x+"-"+y+".asc");
		*/
		
		/*
		CoverageChamferDistance ccd = new CoverageChamferDistance(CoverageManager.getData(coverage, 0, 0, width, height), width, height, imageMinX, imageMinY, cellSize, codes, threshold);
		GridCoverage2D distance = (GridCoverage2D) ccd.allRun();
		CoverageManager.exportAsciiGrid(distance, "C:/Hugues/data/ascii/qualitative_complete/td/test.asc");
		*/
		//float[] distance = (float[]) ccd.allRun();
		//setResult(distance);
	}

	@Override
	protected void doClose() {
		inCoverage = null;
	}


}
