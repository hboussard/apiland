package fr.inra.sad.bagap.apiland.analysis.matrix;

import java.awt.Toolkit;
import java.awt.image.DataBuffer;
import java.awt.image.WritableRaster;
import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.media.jai.PlanarImage;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.gce.geotiff.GeoTiffFormat;
import org.geotools.gce.geotiff.GeoTiffWriteParams;
import org.geotools.gce.geotiff.GeoTiffWriter;
import org.geotools.image.util.ImageUtilities;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValueGroup;

import com.sun.media.jai.codecimpl.util.RasterFactory;

import fr.inra.sad.bagap.apiland.analysis.Analysis;
import fr.inra.sad.bagap.apiland.core.element.manager.Tool;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;

public class HugeChamferDistance extends Analysis {

	private String input;
	
	private String output;
	
	private String temp;
	
	private int[] codes;
	
	private GridCoverage2D inCoverage;
	
	private float threshold;
	
	private int maxTile = 1000;
	
	private String path;
	
	private int width;
	
	private int height;
	
	private double imageMinX;
	
	private double imageMinY;
	
	private double imageMaxX;
	
	private double imageMaxY;
	
	private float cellSize;
	
	private int dWidth; 
	
	private int dHeight;
	
	private Map<Pixel, Map<String, float[]>> bords;
	
	private int[] majTab;
	
	public static void main(String[] args){
		
		//String input = "C:/Hugues/temp/distance/test/carto_vallee_seiche_eau_dessus.asc";
		//String output = "C:/Hugues/temp/distance/test/distance_bois.asc";
		
		//String input = "F:/data/sig/grand_ouest/Bretagne_2018_dispositif_bocage_reb/Bretagne_2018_dispositif_bocage_reb.tif";
		//String output = "F:/temp/distance/distance_bois.tif";
		/*
		String input = "F:/temp/distance/site1.asc";
		String output = "F:/temp/distance/distance3/distance_bois.asc";
		
		Set<Integer> c = new HashSet<Integer>();
		//c.add(1);
		//c.add(2);
		//c.add(3);
		c.add(4);
		c.add(5);
		c.add(6);
		Raster.setNoDataValue(-1);
		new HugeChamferDistance(input, output, c).allRun();*/
		
	}
	
	public HugeChamferDistance(String input, String output, Collection<Integer> codes) {
		this(input, output, codes, Raster.getNoDataValue());
	}
	
	public HugeChamferDistance(String input, String output, Collection<Integer> codes, float threshold) {
		this.input = input;
		this.output = output;
		this.temp = Tool.deleteExtension(output);
		this.path = new File(input).getParent()+"\\";
		// gestion des codes en tableau
		this.codes = new int[codes.size()];
		int index = 0;
		for(int i : codes) {
			this.codes[index++] = i;
		}
		this.threshold = threshold;
	}
	
	@Override
	protected void doInit() {
		inCoverage = CoverageManager.get(input);
		width = (int) inCoverage.getProperty("image_width");
		height = (int) inCoverage.getProperty("image_height");
		imageMinX = inCoverage.getEnvelope().getMinimum(0);
		imageMinY = inCoverage.getEnvelope().getMinimum(1);
		imageMaxX = inCoverage.getEnvelope().getMaximum(0);
		imageMaxY = inCoverage.getEnvelope().getMaximum(1);
		cellSize = (float) ((java.awt.geom.AffineTransform) inCoverage.getGridGeometry().getGridToCRS2D()).getScaleX();
		
		dWidth = 0;
		dHeight = 0;
		bords = new HashMap<Pixel, Map<String, float[]>>();
		
		// tuilage à priori
		int dx = 0, dy = 0;
		for(int y=0; y<height; dy++, y+=maxTile-1){
			dx = 0;
			dWidth = 0;
			for(int x=0; x<width; dx++, x+=maxTile-1){
				dWidth++;
				bords.put(new Pixel(dx, dy), new HashMap<String, float[]>());
			}
			dHeight++;
		}
		
		majTab = new int[dWidth*dHeight];
		Arrays.fill(majTab, -1);
	}
	
	@Override
	protected void doRun() {
		
		//inCoverage = null;		
		
		boolean finish = false;
		int pass = 1;
		while(!finish){
			System.out.println("pass "+pass);
			if(pass == 1){
				finish = true;
				int dx = 0;
				int dy = 0;
				for(int y=0; y<height; dy++, y+=maxTile-1){
					dx = 0;
					for(int x=0; x<width; dx++, x+=maxTile-1){
						
						System.out.println("traitement de la tuile "+dx+" "+dy+" / "+dWidth+" "+dHeight);
						
						int roiWidth = Math.min(maxTile, width - x);
						int roiHeight = Math.min(maxTile, height - y);
						double roiPosMinX = imageMinX + x*cellSize;
						double roiPosMaxX = roiPosMinX + roiWidth * cellSize;
						double roiPosMinY = Math.max(imageMinY, imageMaxY - (y+roiHeight)*cellSize);
						double roiPosMaxY = roiPosMinY + roiHeight * cellSize;
						
						//System.out.println("récupération des valeurs initiales");
						float[] inDatas = CoverageManager.getData(inCoverage, x, y, roiWidth, roiHeight);
						
						float[] outDatas = new float[roiHeight*roiWidth];
						boolean hasValue = false;
						for (int yt = 0; yt < roiHeight; yt++) {
							for (int xt = 0; xt < roiWidth; xt++) {
								float v = inDatas[yt*roiWidth+xt];
								boolean ok = false;
								if (v != Raster.getNoDataValue()) {
									for (int c : codes) {
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
						
						//hasValue = true;
						if(hasValue){
							CoverageChamferDistance ccd = new CoverageChamferDistance(outDatas, roiWidth, roiHeight, cellSize);
							outDatas = (float[]) ccd.allRun();
							majTab[dy*dWidth+dx] = 1;
						}
						
						// stockage des valeurs de voisinnage
						boolean isFinish = stockage(dx, dy, roiWidth, roiHeight, roiPosMinX, roiPosMaxX, roiPosMinY, roiPosMaxY, outDatas);
						if(!isFinish){
							finish = false;
						}
					}	
				}
				//finish = true;
			}else{
				if(inCoverage != null){
					inCoverage.dispose(true);
					inCoverage = null;
				}
				finish = true;
				
				int dx = 0, dy = 0;
				for(int y=0; y<height; dy++, y+=maxTile-1){
					dx = 0;
					for(int x=0; x<width; dx++, x+=maxTile-1){
						
						//System.out.println("traitement de la tuile "+dx+" "+dy+" / "+dWidth+" "+dHeight);
						
						int roiWidth = Math.min(maxTile, width - x);
						int roiHeight = Math.min(maxTile, height - y);
						double roiPosMinX = imageMinX + x*cellSize;
						double roiPosMaxX = roiPosMinX + roiWidth * cellSize;
						double roiPosMinY = Math.max(imageMinY, imageMaxY - (y+roiHeight)*cellSize);
						double roiPosMaxY = roiPosMinY + roiHeight * cellSize;
						
						GridCoverage2D cov = CoverageManager.get(temp+"_"+dx+"-"+dy+".tif");
						
						float[] outDatas = CoverageManager.getData(cov, 0, 0, roiWidth, roiHeight);
						cov.dispose(true);
						PlanarImage planarImage = (PlanarImage) cov.getRenderedImage();
						ImageUtilities.disposePlanarImageChain(planarImage);
						cov = null;
						
						// vérification des mises à jour à faire
						boolean maj = false;
						Pixel p = new Pixel(dx, dy);
						int num = 0;
						if(dy > 0){ // le bord nord de la tuile est inclu et partagé
							float[] bord = bords.get(p).get("nord");
							for(int i=0; i<roiWidth; i++){
								if(bord[i] < outDatas[i]){
									outDatas[i] = bord[i];
									maj = true;
									num++;
								}
							}
						}
						if(dy < (dHeight-1)){ // le bord sud de la tuile est inclu et partagé
							float[] bord = bords.get(p).get("sud");
							for(int i=0; i<roiWidth; i++){
								if(bord[i] < outDatas[(roiHeight-1)*roiWidth + i]){
									outDatas[(roiHeight-1)*roiWidth + i] = bord[i];
									maj = true;
									num++;
								}
							}
						}
						if(dx > 0){ // le bord gauche de la tuile est inclu et partagé
							float[] bord = bords.get(p).get("gauche");
							for(int j=0; j<roiHeight; j++){
								if(bord[j] < outDatas[j*roiWidth+0]){
									outDatas[j*roiWidth+0] = bord[j]; 
									maj = true;
									num++;
								}
							}
						}
						if(dx < (dWidth-1)){ // le bord droite de la tuile est inclu et partagé
							float[] bord = bords.get(p).get("droite");
							for(int j=0; j<roiHeight; j++){
								if(bord[j] < outDatas[j*roiWidth + (roiWidth-1)]){
									outDatas[j*roiWidth + (roiWidth-1)] = bord[j];
									maj = true;
									num++;
								}
							}
						}
						
						if(maj){
							
							System.out.println("traitement de la tuile "+num+" "+dx+" "+dy+" / "+dWidth+" "+dHeight);
							
							CoverageChamferDistance ccd = new CoverageChamferDistance(outDatas, roiWidth, roiHeight, cellSize);
							outDatas = (float[]) ccd.allRun();
							
							majTab[dy*dWidth+dx] = 1;
						}else{
							if(majTab[dy*dWidth+dx] >= 0){
								majTab[dy*dWidth+dx] = 0;
							}
						}
							
						// stockage des valeurs de voisinnage
						boolean isFinish = stockage(dx, dy, roiWidth, roiHeight, roiPosMinX, roiPosMaxX, roiPosMinY, roiPosMaxY, outDatas);
						if(!isFinish){
							finish = false;
						}
					}	
				}
			}
			pass++;
			/*
			if (pass>=4){
				finish = true;
			}
			*/
		}
		
		// normalisation
		normalisation();
		
		// compilation
		compile();
	}

	/**
	 * 3 cas
	 * 1. la tuile n'a jamais été mise à jour, peut-être que les bords voisins vont l'y obliger...
	 * 2. la tuile n'a pas été mis à jour (cette fois), la mise à jour des bords se fera sous condition de l'tétat des tuiles voisines
	 * 3. la tuile vient d'être mise à jour, attention au bords jamais mis à jour
	 */
	private boolean stockage(int dx, int dy, int roiWidth, int roiHeight, double roiPosMinX, double roiPosMaxX, double roiPosMinY, double roiPosMaxY, float[] outDatas){
		// stockage des valeurs de voisinnage
		
		boolean finish = true;
		
		Pixel p = new Pixel(dx, dy);
		
		
		
		if(majTab[dy*dWidth+dx] == -1){ // la tuile n'a jamais été calculée
			
			//System.out.println("traitement de la tuile "+x+" "+y);
			if(dx > 0){ // le bord gauche de la tuile est inclu et partagé
				if(majTab[dy*dWidth+(dx-1)] >= 0){
					Pixel pGauche = new Pixel(dx-1, dy);
					bords.get(p).put("gauche", bords.get(pGauche).get("droite"));
					finish = false;
				}
			}
			if(dy > 0){ // le bord nord de la tuile est inclu et partagé
				if(majTab[(dy-1)*dWidth+dx] >= 0){
					Pixel pNord = new Pixel(dx, dy-1);
					bords.get(p).put("nord", bords.get(pNord).get("sud"));
					finish = false;
				}
			}
			
		}else if(majTab[dy*dWidth+dx] == 0){ // la tuile n'a pas été calculée (cette fois)
			//System.out.println("traitement de la tuile "+x+" "+y);
			if(dx > 0){ // le bord gauche de la tuile est inclu et partagé
				if(majTab[dy*dWidth+(dx-1)] == 1){ // le voisin de gauche vient d'être calculé
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
					bords.get(p).put("gauche", bord);
				}
			}
			
			if(dy > 0){ // le bord nord de la tuile est inclu et partagé
				if(majTab[(dy-1)*dWidth+dx] == 1){
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
				}
			}
		}else{ // la tuile a été calculée
			//System.out.println("traitement de la tuile "+x+" "+y);
			if(dx > 0){ // le bord gauche de la tuile est inclu et partagé
				
				if(majTab[dy*dWidth+(dx-1)] >= 0){ // le voisin gauche a déjà été calculé
					
				}else{ // le voisin gauche n'a jamais été calculé
					
				}
				
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
					
					if(bordVoisin[bordVoisin.length-1] < bord[0]){
						bord[0] = bordVoisin[bordVoisin.length-1];
						finish = false;
					}else if(bordVoisin[bordVoisin.length-1] > bord[0]){
						bordVoisin[bordVoisin.length-1] = bord[0];
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
					
					if(bordVoisin[bordVoisin.length-1] < bord[0]){
						bord[0] = bordVoisin[bordVoisin.length-1];
						finish = false;
					}else if(bordVoisin[bordVoisin.length-1] > bord[0]){
						bordVoisin[bordVoisin.length-1] = bord[0];
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
				
				if(bordVoisin[bordVoisin.length-1] < bord[0]){
					bord[0] = bordVoisin[bordVoisin.length-1];
					finish = false;
				}else if(bordVoisin[bordVoisin.length-1] > bord[0]){
					bordVoisin[bordVoisin.length-1] = bord[0];
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
				
				if(bordVoisin[bordVoisin.length-1] < bord[0]){
					bord[0] = bordVoisin[bordVoisin.length-1];
					finish = false;
				}else if(bordVoisin[bordVoisin.length-1] > bord[0]){
					bordVoisin[bordVoisin.length-1] = bord[0];
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
		CoverageManager.writeGeotiff(new File(temp+"_"+dx+"-"+dy+".tif"), outDatas, roiWidth, roiHeight, roiPosMinX, roiPosMaxX, roiPosMinY, roiPosMaxY);
		
		return finish;
	}
	
	private void normalisation(){
		// normalisation
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
						
				GridCoverage2D cov = CoverageManager.get(temp+"_"+dx+"-"+dy+".tif");
				float[] outDatas = CoverageManager.getData(cov, 0, 0, roiWidth, roiHeight);
				cov.dispose(true);
				PlanarImage planarImage = (PlanarImage) cov.getRenderedImage();
				ImageUtilities.disposePlanarImageChain(planarImage);
				cov = null;
						
				CoverageChamferDistance ccd = new CoverageChamferDistance(outDatas, roiWidth, roiHeight, cellSize);
				outDatas = ccd.normalize();
						
				// stockage en mémoire fichier de l'état de la tuile
				CoverageManager.writeGeotiff(new File(temp+"_"+dx+"-"+dy+".tif"), outDatas, roiWidth, roiHeight, roiPosX, roiPosMaxX, roiPosY, roiPosMaxY);
			}
		}
	}
	
	private void compile() {
		
		WritableRaster raster = RasterFactory.createBandedRaster(DataBuffer.TYPE_FLOAT, width, height, 1, null);
		
		// tuilage à priori
		int dx = 0, dy = 0;
		for(int y=0; y<height; dy++, y+=maxTile-1){
			dx = 0;
			for(int x=0; x<width; dx++, x+=maxTile-1){
				
				int roiWidth = Math.min(maxTile, width - x);
				int roiHeight = Math.min(maxTile, height - y);
				
				System.out.println("traitement de la tuile "+dx+" "+dy+" / "+dWidth+" "+dHeight+" "+x+" "+y+" "+roiWidth+" "+roiHeight);
				
				GridCoverage2D cov = CoverageManager.get(temp+"_"+dx+"-"+dy+".tif");
				float[] localDatas = CoverageManager.getData(cov, 0, 0, roiWidth, roiHeight);
				
				raster.setSamples(x, y, roiWidth, roiHeight, 0, localDatas);
				
				cov.dispose(true);
				PlanarImage planarImage = (PlanarImage) cov.getRenderedImage();
				ImageUtilities.disposePlanarImageChain(planarImage);
				cov = null;
				
				new File(temp+"_"+dx+"-"+dy+".tif").delete();
			}
		}
		
		CoverageManager.write(raster, width, height, imageMinX, imageMaxX, imageMinY, imageMaxY, cellSize, output);
	}

	@Override
	protected void doClose() {
		bords.clear();
		bords = null;
	}
	
	/*
	protected void doRunOld() {
		width = (int) inCoverage.getProperty("image_width");
		height = (int) inCoverage.getProperty("image_height");
		imageMinX = inCoverage.getEnvelope().getMinimum(0);
		imageMinY = inCoverage.getEnvelope().getMinimum(1);
		imageMaxX = inCoverage.getEnvelope().getMaximum(0);
		imageMaxY = inCoverage.getEnvelope().getMaximum(1);
		cellSize = (float) ((java.awt.geom.AffineTransform) inCoverage.getGridGeometry().getGridToCRS2D()).getScaleX();
		
		//inCoverage = null;		
		
		int dWidth = 0, dHeight = 0;
		Map<Pixel, Map<String, float[]>> bords = new HashMap<Pixel, Map<String, float[]>>();
		
		boolean finish = false;
		int pass = 1;
		while(!finish){
			
			System.out.println("pass "+pass);
			
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
				
				finish = true;
				dx = 0;
				dy = 0;
				for(int y=0; y<height; dy++, y+=maxTile-1){
					dx = 0;
					for(int x=0; x<width; dx++, x+=maxTile-1){
						
						System.out.println("traitement de la tuile "+dx+" "+dy+" / "+dWidth+" "+dHeight);
						
						int roiWidth = Math.min(maxTile, width - x);
						int roiHeight = Math.min(maxTile, height - y);
						double roiPosMinX = imageMinX + x*cellSize;
						double roiPosMaxX = roiPosMinX + roiWidth * cellSize;
						double roiPosMinY = Math.max(imageMinY, imageMaxY - (y+roiHeight)*cellSize);
						double roiPosMaxY = roiPosMinY + roiHeight * cellSize;
						
						//System.out.println("récupération des valeurs initiales");
						float[] inDatas = CoverageManager.getData(inCoverage, x, y, roiWidth, roiHeight);
						
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
							CoverageChamferDistance ccd = new CoverageChamferDistance(outDatas, roiWidth, roiHeight, cellSize);
							outDatas = (float[]) ccd.allRun();
							
							// stockage des valeurs de voisinnage
							
							Pixel p = new Pixel(dx, dy);
							//System.out.println("traitement de la tuile "+x+" "+y);
							if(dx > 0){ // le bord gauche de la tuile est inclu et partagé
								//System.out.println("1");
								float[] bord = new float[roiHeight];
								
								Pixel pGauche = new Pixel(dx-1, dy);
								float[] bordVoisin = bords.get(pGauche).get("droite"); 
								
								for(int j=0; j<roiHeight; j++){
									//System.out.println(bordVoisin[j]+" "+outDatas[j*roiWidth+0]);
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
									
									if(bordVoisin[bordVoisin.length-1] < bord[0]){
										bord[0] = bordVoisin[bordVoisin.length-1];
										finish = false;
									}else if(bordVoisin[bordVoisin.length-1] > bord[0]){
										bordVoisin[bordVoisin.length-1] = bord[0];
										finish = false;
										bords.get(pNordGauche).put("droite", bordVoisin);
									}
								}
								
								bords.get(p).put("gauche", bord);
							}
							if(dx < (dWidth-1)){ // le bord droite de la tuile est inclu et partagé
								//System.out.println("2");
								float[] bord = new float[roiHeight];
								for(int j=0; j<roiHeight; j++){
									bord[j] = outDatas[j*roiWidth + (roiWidth-1)];
								}
								
								if(dy > 0){ // le bord nord-droite de la tuile est inclu et partagé
									Pixel pNordDroite = new Pixel(dx+1, dy-1);
									float[] bordVoisin = bords.get(pNordDroite).get("gauche");
									
									if(bordVoisin[bordVoisin.length-1] < bord[0]){
										bord[0] = bordVoisin[bordVoisin.length-1];
										finish = false;
									}else if(bordVoisin[bordVoisin.length-1] > bord[0]){
										bordVoisin[bordVoisin.length-1] = bord[0];
										finish = false;
										bords.get(pNordDroite).put("gauche", bordVoisin);
									}
								}
								
								bords.get(p).put("droite", bord);
							}
							if(dy > 0){ // le bord nord de la tuile est inclu et partagé
								//System.out.println("3");
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
								//System.out.println("4");
								float[] bord = new float[roiWidth];
								for(int i=0; i<roiWidth; i++){
									bord[i] = outDatas[(roiHeight-1)*roiWidth + i];
								}
								bords.get(p).put("sud", bord);
							}
						}
						
						// stockage en mémoire fichier de l'état de la tuile
						CoverageManager.writeGeotiff(new File(path+"tile-"+x+"-"+y+".tif"), outDatas, roiWidth, roiHeight, roiPosMinX, roiPosMaxX, roiPosMinY, roiPosMaxY);
					}	
				}
			}else{
				
				finish = true;
				
				int dx = 0, dy = 0;
				for(int y=0; y<height; dy++, y+=maxTile-1){
					dx = 0;
					for(int x=0; x<width; dx++, x+=maxTile-1){
						
						System.out.println("traitement de la tuile "+dx+" "+dy+" / "+dWidth+" "+dHeight);
						
						int roiWidth = Math.min(maxTile, width - x);
						int roiHeight = Math.min(maxTile, height - y);
						double roiPosX = imageMinX + x*cellSize;
						double roiPosMaxX = roiPosX + roiWidth * cellSize;
						double roiPosY = Math.max(imageMinY, imageMaxY - (y+roiHeight)*cellSize);
						double roiPosMaxY = roiPosY + roiHeight * cellSize;
						
						GridCoverage2D cov = CoverageManager.get(path+"tile-"+x+"-"+y+".tif");
						float[] outDatas = CoverageManager.getData(cov, 0, 0, roiWidth, roiHeight);
						cov = null;
						
						// vérification des mises à jour à faire
						boolean maj = false;
						Pixel p = new Pixel(dx, dy);
						
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
						
						if(maj){
							CoverageChamferDistance ccd = new CoverageChamferDistance(outDatas, roiWidth, roiHeight, cellSize);
							outDatas = (float[]) ccd.allRun();
							
							// stockage des valeurs de voisinnage
							
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
									
									if(bordVoisin[bordVoisin.length-1] < bord[0]){
										bord[0] = bordVoisin[bordVoisin.length-1];
										finish = false;
									}else if(bordVoisin[bordVoisin.length-1] > bord[0]){
										bordVoisin[bordVoisin.length-1] = bord[0];
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
									
									if(bordVoisin[bordVoisin.length-1] < bord[0]){
										bord[0] = bordVoisin[bordVoisin.length-1];
										finish = false;
									}else if(bordVoisin[bordVoisin.length-1] > bord[0]){
										bordVoisin[bordVoisin.length-1] = bord[0];
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
							CoverageManager.writeGeotiff(new File(path+"tile-"+x+"-"+y+".tif"), outDatas, roiWidth, roiHeight, roiPosX, roiPosMaxX, roiPosY, roiPosMaxY);
						}
					}	
				}
			}
			pass++;
		}
			
		// normalisation
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
				
				GridCoverage2D cov = CoverageManager.get(path+"tile-"+x+"-"+y+".tif");
				float[] outDatas = CoverageManager.getData(cov, 0, 0, roiWidth, roiHeight);
				cov = null;
				
				CoverageChamferDistance ccd = new CoverageChamferDistance(outDatas, roiWidth, roiHeight, cellSize);
				outDatas = ccd.normalize();
				
				// stockage en mémoire fichier de l'état de la tuile
				CoverageManager.writeGeotiff(new File(path+"tile-"+x+"-"+y+".tif"), outDatas, roiWidth, roiHeight, roiPosX, roiPosMaxX, roiPosY, roiPosMaxY);
			}
		}
		
		compile();
	}
	*/


}
