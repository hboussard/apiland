package fr.inra.sad.bagap.apiland.analysis.matrix;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.media.jai.PlanarImage;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.image.util.ImageUtilities;

import fr.inra.sad.bagap.apiland.analysis.Analysis;
import fr.inra.sad.bagap.apiland.core.element.manager.Tool;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inrae.act.bagap.raster.CoverageManager;

public class HugeRCMDistanceAnalysis extends Analysis {

	private String input, inputFriction, output, temp;
	
	private int[] codes;
	
	private GridCoverage2D inCoverage, frictionCoverage;
	
	private float threshold;
	
	private int maxTile = 15000;
	
	private Map<Pixel, Map<String, float[]>> bords;
	
	private int width, height;
	
	private double imageMinX, imageMinY, imageMaxX, imageMaxY;
	
	private float cellSize;
	
	private int dWidth, dHeight;
	
	private short[] calculTab;
	
	private short[] majTab;
	
	public static void main(String[] args){
		
		//String input = "F:/chloe/chloe5/data/za.tif";
		//String inputFriction = "F:/chloe/chloe5/data/distance/permeabilite_pf.tif";
		//String output = "F:/chloe/chloe5/data/distance/continuites_pf.asc";
		//Set<Integer> c = new HashSet<Integer>();
		//c.add(4);
		//c.add(5);
		
		//String input = "F:/coterra/data/Coterra_2019_DNSB_erb/continuites_prairie/habitat_prairial.tif";
		//String inputFriction = "F:/coterra/data/Coterra_2019_DNSB_erb/continuites_prairie/permeabilite_prairiale.tif";
		//String output = "F:/coterra/data/Coterra_2019_DNSB_erb/continuites_prairie/continuites_prairiales.asc";
		//Set<Integer> c = new HashSet<Integer>();
		//c.add(1);
		
		String input = "F:/chloe/debug/mallet/Habitat_et_permeabilite/Habitat_haie_sup_0_mas_clean.asc";
		String inputFriction = "F:/chloe/debug/mallet/Habitat_et_permeabilite/Permeabilite_grand_rhino_bis.asc";
		String output = "F:/chloe/debug/mallet/Habitat_et_permeabilite/continuites_bis.asc";
		Set<Integer> c = new HashSet<Integer>();
		c.add(1);
		
		
		Raster.setNoDataValue(-1);
		new HugeRCMDistanceAnalysis(input, inputFriction, output, c).allRun();
	}
	
	public HugeRCMDistanceAnalysis(String input, String inputFriction, String output, Collection<Integer> codes) {
		this(input, inputFriction, output, codes, Raster.getNoDataValue());
	}
	
	public HugeRCMDistanceAnalysis(String input, String inputFriction, String output, Collection<Integer> codes, float threshold) {
		this.input = input;
		this.inputFriction = inputFriction;
		this.output = output;
		this.temp = Tool.deleteExtension(output);
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
		
		frictionCoverage = CoverageManager.get(inputFriction);
		
		dWidth = 0;
		dHeight = 0;
		bords = new HashMap<Pixel, Map<String, float[]>>();
		
		// tuilage à priori
		int dx = 0, dy = 0;
		Pixel p;
		for(int y=0; y<height; dy++, y+=maxTile-1){
			dx = 0;
			dWidth = 0;
			for(int x=0; x<width; dx++, x+=maxTile-1){
				dWidth++;
				p = new Pixel(dx, dy);
				bords.put(p, new HashMap<String, float[]>());
				
				int roiWidth = Math.min(maxTile, width - x);
				int roiHeight = Math.min(maxTile, height - y);
				
				if(dx > 0){
					float[] bord = new float[roiHeight];
					Arrays.fill(bord, -1);
					bords.get(p).put("left", bord);
					bords.get(new Pixel(dx-1, dy)).put("right", bord);
				}
				if(dy > 0){
					float[] bord = new float[roiWidth];
					Arrays.fill(bord, -1);
					bords.get(p).put("north", bord);
					bords.get(new Pixel(dx, dy-1)).put("south", bord);
				}
				
			}
			dHeight++;
		}
		
		calculTab = new short[dWidth*dHeight];
		Arrays.fill(calculTab, (short) -1);
		
		majTab = new short[dWidth*dHeight];
		Arrays.fill(majTab, (short) 0);
	}

	@Override
	protected void doRun() {	
		
		boolean finish = false;
		int pass = 1;
		boolean maj;
		boolean hasValue;
		while(!finish){
			finish = true;
			System.out.println("pass "+pass);
			if(pass == 1){ // premiere passe
				
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
						
						// récupération des valeurs initiales
						float[] inDatas = CoverageManager.getData(inCoverage, x, y, roiWidth, roiHeight);
						
						// récupération des valeurs de friction
						float[] frictionDatas = CoverageManager.getData(frictionCoverage, x, y, roiWidth, roiHeight);
						
						// données de sortie
						float[] outDatas = new float[roiHeight*roiWidth];
						
						hasValue = false;
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
						
						// pour la gestion des pixels a traiter en ordre croissant de distance
						Map<Float, Set<Pixel>> waits = new TreeMap<Float, Set<Pixel>>();
						
						// afin de limiter le nombre de calculs de diffusion, ne diffuser qu'à partir des bords d'habitats
						for (int yt = 0; yt < roiHeight; yt++) {
							for (int xt = 0; xt < roiWidth; xt++) {
								if (outDatas[yt * roiWidth + xt] == 0) {
									maj = true;
									if(xt == 0 || outDatas[(xt-1)+yt*roiWidth] == 0){
										if(xt == 0 || yt == 0 || outDatas[(xt-1)+(yt-1)*roiWidth] == 0){
											if(yt == 0 || outDatas[xt+(yt-1)*roiWidth] == 0){
												if(xt == (roiWidth-1) || yt == 0 || outDatas[(xt+1)+(yt-1)*roiWidth] == 0){
													if(xt == (roiWidth-1) || outDatas[(xt+1)+yt*roiWidth] == 0){
														if(xt == (roiWidth-1) || yt == (roiHeight-1) || outDatas[(xt+1)+(yt+1)*roiWidth] == 0){
															if(yt == (roiHeight-1) || outDatas[xt+(yt+1)*roiWidth] == 0){
																if(xt == 0 || yt == (roiHeight-1) || outDatas[(xt-1)+(yt+1)*roiWidth] == 0){
																	// do nothing
																	maj = false;
																}
															}
														}
													}
												}
											}
										}
									}
									if(maj){
										setPixelAndValue(waits, new Pixel(xt, yt), 0.0f);
									}
								}
								
							}
						}
						
						if(hasValue){
							ArrayRCMDistanceAnalysis rcm = new ArrayRCMDistanceAnalysis(outDatas, frictionDatas, roiWidth, roiHeight, cellSize, waits);
							outDatas = (float[]) rcm.allRun();
							
							// maj des bords
							boolean isFinish = bordUpdateFromData(dx, dy, roiWidth, roiHeight, roiPosMinX, roiPosMaxX, roiPosMinY, roiPosMaxY, outDatas);
							if(!isFinish){
								finish = false;
							}
							
							calculTab[dy*dWidth+dx] = 1;
						}
						
						// stockage en mémoire fichier de l'état de la tuile
						CoverageManager.writeGeotiff(new File(temp+"_"+dx+"-"+dy+".tif"), outDatas, roiWidth, roiHeight, roiPosMinX, roiPosMaxX, roiPosMinY, roiPosMaxY);
						
					}	
				}
				// finish = true;
			}else{
				
				if(inCoverage != null){
					inCoverage.dispose(true);
					PlanarImage planarImage = (PlanarImage) inCoverage.getRenderedImage();
					ImageUtilities.disposePlanarImageChain(planarImage);
					inCoverage = null;
				}
				
				int dx = 0, dy = 0;
				for(int y=0; y<height; dy++, y+=maxTile-1){
					dx = 0;
					for(int x=0; x<width; dx++, x+=maxTile-1){
						
						//System.out.println("traitement de la tuile "+dx+" "+dy+" / "+dWidth+" "+dHeight);
						
						if(majTab[dy*dWidth + dx] == 1){ // la tuile doit être mise à jour
							
							System.out.println("traitement de la tuile "+dx+" "+dy+" / "+dWidth+" "+dHeight);
							
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
							
							// pour la gestion des pixels a traiter en ordre croissant de distance
							Map<Float, Set<Pixel>> waits = new TreeMap<Float, Set<Pixel>>();
							
							// vérification des mises à jour à faire
							Pixel p = new Pixel(dx, dy);
							if(calculTab[dy*dWidth+dx] == -1){ // la tuile n'a jamais été calculée
								
								if(dx > 0){ // le bord gauche de la tuile est inclu et partagé
									if(calculTab[dy*dWidth+(dx-1)] >= 0){ // si la tuile à gauche a été calculée
										float[] bord = bords.get(p).get("left");
										for(int j=0; j<roiHeight; j++){
											outDatas[j*roiWidth+0] = bord[j]; 
											setPixelAndValue(waits, new Pixel(0, j), bord[j]);
										}
									}
								}
								if(dy > 0){ // le bord nord de la tuile est inclu et partagé
									if(calculTab[(dy-1)*dWidth+dx] >= 0){ // si la tuile au nord a été calculée
										float[] bord = bords.get(p).get("north");
										for(int i=0; i<roiWidth; i++){
											outDatas[i] = bord[i];
											setPixelAndValue(waits, new Pixel(i, 0), bord[i]);
										}
									}
								}
								if(dx < (dWidth-1)){ // le bord droite de la tuile est inclu et partagé
									if(calculTab[dy*dWidth+(dx+1)] >= 0){ // si la tuile à droite a été calculée
										float[] bord = bords.get(p).get("right");
										for(int j=0; j<roiHeight; j++){
											outDatas[j*roiWidth + (roiWidth-1)] = bord[j];
											setPixelAndValue(waits, new Pixel(roiWidth-1, j), bord[j]);
										}
									}
								}
								if(dy < (dHeight-1)){ // le bord sud de la tuile est inclu et partagé
									if(calculTab[(dy+1)*dWidth+dx] >= 0){ // si la tuile au sud a été calculée
										float[] bord = bords.get(p).get("south");
										for(int i=0; i<roiWidth; i++){
											outDatas[(roiHeight-1)*roiWidth + i] = bord[i];
											setPixelAndValue(waits, new Pixel(i, roiHeight-1), bord[i]);
										}
									}
								}
							}else{ // tuile a déjà été calculé
								
								if(dx > 0){ // le bord gauche de la tuile est inclu et partagé
									float[] bord = bords.get(p).get("left");
									for(int j=0; j<roiHeight; j++){
										if(bord[j] < outDatas[j*roiWidth+0]){
											outDatas[j*roiWidth+0] = bord[j]; 
											setPixelAndValue(waits, new Pixel(0, j), bord[j]);
										}
									}
								}
								if(dy > 0){ // le bord nord de la tuile est inclu et partagé
									float[] bord = bords.get(p).get("north");
									for(int i=0; i<roiWidth; i++){
										if(bord[i] < outDatas[i]){
											outDatas[i] = bord[i];
											setPixelAndValue(waits, new Pixel(i, 0), bord[i]);
										}
									}
								}
								if(dx < (dWidth-1)){ // le bord droite de la tuile est inclu et partagé
									float[] bord = bords.get(p).get("right");
									for(int j=0; j<roiHeight; j++){
										if(bord[j] < outDatas[j*roiWidth + (roiWidth-1)]){
											outDatas[j*roiWidth + (roiWidth-1)] = bord[j];
											setPixelAndValue(waits, new Pixel(roiWidth-1, j), bord[j]);
										}
									}
								}
								if(dy < (dHeight-1)){ // le bord sud de la tuile est inclu et partagé
									float[] bord = bords.get(p).get("south");
									for(int i=0; i<roiWidth; i++){
										if(bord[i] < outDatas[(roiHeight-1)*roiWidth + i]){
											outDatas[(roiHeight-1)*roiWidth + i] = bord[i];
											setPixelAndValue(waits, new Pixel(i, roiHeight-1), bord[i]);
										}
									}
								}
							}
								
							// récupération des valeurs de friction
							float[] frictionDatas = CoverageManager.getData(frictionCoverage, x, y, roiWidth, roiHeight);
							
							ArrayRCMDistanceAnalysis ccd = new ArrayRCMDistanceAnalysis(outDatas, frictionDatas, roiWidth, roiHeight, cellSize, waits);
							outDatas = (float[]) ccd.allRun();
								
							// maj des bords
							boolean isFinish = bordUpdateFromData(dx, dy, roiWidth, roiHeight, roiPosMinX, roiPosMaxX, roiPosMinY, roiPosMaxY, outDatas);
							if(!isFinish){
								finish = false;
							}
								
							// stockage en mémoire fichier de l'état de la tuile
							CoverageManager.writeGeotiff(new File(temp+"_"+dx+"-"+dy+".tif"), outDatas, roiWidth, roiHeight, roiPosMinX, roiPosMaxX, roiPosMinY, roiPosMaxY);
								
							majTab[dy*dWidth+dx] = 0;
							calculTab[dy*dWidth+dx] = 1;
							
						}else{ // la tuile ne doit pas être mise à jour
							if(calculTab[dy*dWidth+dx] >= 0){
								calculTab[dy*dWidth+dx] = 0;
							}
						}
					}	
				}
			}
			pass++;
			
		}
		
		// compilation
		compileToAsciiGrid();
	}
	
	private void setPixelAndValue(Map<Float, Set<Pixel>> waits, Pixel pixel, float value) {
		if (!waits.containsKey(value)) {
			waits.put(value, new HashSet<Pixel>());
		}
		waits.get(value).add(pixel);
	}

	/**
	 * 3 cas
	 * 1. la tuile n'a jamais été mise à jour, peut-être que les bords vont l'y obliger...
	 * 2. la tuile n'a pas été mis à jour (cette fois), la mise à jour des bords se fera sous condition de l'état des tuiles voisines
	 * 3. la tuile vient d'être mise à jour, attention au bords jamais mis à jour
	 */
	private boolean bordUpdateFromData(int dx, int dy, int roiWidth, int roiHeight, double roiPosMinX, double roiPosMaxX, double roiPosMinY, double roiPosMaxY, float[] outDatas){
		
		boolean finish = true;
		Pixel p = new Pixel(dx, dy);
		
		//System.out.println("traitement de la tuile "+x+" "+y);
		if(dx > 0){ // le bord gauche de la tuile est inclu et partagé
			float[] bord = bords.get(p).get("left");
			for(int j=0; j<roiHeight; j++){
				if(bord[j] != outDatas[j * roiWidth + 0]){
					bord[j] = outDatas[j * roiWidth + 0];
					majTab[dy*dWidth+(dx-1)] = 1;
					finish = false;
				}
			}
		}
		
		if(dy > 0){ // le bord nord de la tuile est inclu et partagé
			float[] bord = bords.get(p).get("north");
			for(int i=0; i<roiWidth; i++){
				if(bord[i] != outDatas[i]){
					bord[i] = outDatas[i];
					majTab[(dy-1)*dWidth+dx] = 1;
					finish = false;
				}
			}
		}
		
		if(dx < (dWidth-1)){ // le bord droite de la tuile est inclu et partagé
			float[] bord = bords.get(p).get("right");
			for(int j=0; j<roiHeight; j++){
				if(bord[j] != outDatas[j * roiWidth + (roiWidth-1)]){
					bord[j] = outDatas[j * roiWidth + (roiWidth-1)];
					majTab[dy*dWidth+(dx+1)] = 1;
					finish = false;
				}
			}
		}
		
		if(dy < (dHeight-1)){ // le bord sud de la tuile est inclu et partagé
			float[] bord = bords.get(p).get("south");
			for(int i=0; i<roiWidth; i++){
				if(bord[i] != outDatas[(roiHeight-1)*roiWidth + i]){
					bord[i] = outDatas[(roiHeight-1)*roiWidth + i];
					majTab[(dy+1)*dWidth+dx] = 1;
					finish = false;
				}
			}
		}
			
		return finish;
	}
	
	public void compileToAsciiGrid() {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(output));
			writer.write("ncols "+width);
			writer.newLine();
			writer.write("nrows "+height);
			writer.newLine();
			writer.write("xllcorner "+imageMinX);
			writer.newLine();
			writer.write("yllcorner "+imageMinY);
			writer.newLine();
			writer.write("cellsize "+cellSize);
			writer.newLine();
			writer.write("NODATA_value "+Raster.getNoDataValue());
			writer.newLine();
			
			// tuilage à priori
			int dx = 0, dy = 0;
			for(int y=0; y<height; dy++, y+=maxTile-1){
				dx = 0;
				int roiHeight = Math.min(maxTile, height - y);
				
				float[] datas;
				if(y == 0){ // 1ere ligne
					datas = new float[width * roiHeight];
					
					for(int x=0; x<width; dx++, x+=maxTile-1){
						
						int roiWidth = Math.min(maxTile, width - x);
						
						System.out.println("traitement de la tuile "+dx+" "+dy+" / "+dWidth+" "+dHeight+" "+x+" "+y+" "+roiWidth+" "+roiHeight);
						
						if(x == 0){
							
							GridCoverage2D cov = CoverageManager.get(temp+"_"+dx+"-"+dy+".tif");
							float[] localDatas = CoverageManager.getData(cov, 0, 0, roiWidth, roiHeight);
							
							// do something
							for(int li=0; li<localDatas.length; li++){
								int ly = li/roiWidth;
								int lx = li%roiWidth;
								//System.out.println(lx+" "+ly+" "+(li%roiWidth)+" "+(li/roiWidth));
								datas[ly*width + lx] = localDatas[li];
							}
							
							
							cov.dispose(true);
							PlanarImage planarImage = (PlanarImage) cov.getRenderedImage();
							ImageUtilities.disposePlanarImageChain(planarImage);
							cov = null;
							
						}else{
							
							GridCoverage2D cov = CoverageManager.get(temp+"_"+dx+"-"+dy+".tif");
							float[] localDatas = CoverageManager.getData(cov, 1, 0, roiWidth - 1, roiHeight);
							
							// do something
							for(int li=0; li<localDatas.length; li++){
								int ly = li/(roiWidth-1);
								int lx = dx * maxTile - dx + 1 + li%(roiWidth-1);
								
								//System.out.println(lx+" "+ly+" "+li%(roiWidth-1)+" "+(li/(roiWidth-1)));
								datas[ly*width + lx] = localDatas[li];
							}
							
							cov.dispose(true);
							PlanarImage planarImage = (PlanarImage) cov.getRenderedImage();
							ImageUtilities.disposePlanarImageChain(planarImage);
							cov = null;
							
						} 
						new File(temp+"_"+dx+"-"+dy+".tif").delete();
					}
					
					for(int ly=0; ly<roiHeight; ly++){
						for(int li=0; li<width; li++){
							writer.write(datas[ly * width + li]+" ");
						}
						writer.newLine();
					}
				}else{
					datas = new float[width * (roiHeight-1)];
					
					for(int x=0; x<width; dx++, x+=maxTile-1){
						
						int roiWidth = Math.min(maxTile, width - x);
						
						System.out.println("traitement de la tuile "+dx+" "+dy+" / "+dWidth+" "+dHeight+" "+x+" "+y+" "+roiWidth+" "+roiHeight);
						
						if(x == 0){
							
							GridCoverage2D cov = CoverageManager.get(temp+"_"+dx+"-"+dy+".tif");
							float[] localDatas = CoverageManager.getData(cov, 0, 1, roiWidth, roiHeight - 1);
							
							// do something
							for(int li=0; li<localDatas.length; li++){
								int ly = li/roiWidth;
								int lx = li%roiWidth;
								
								//System.out.println(lx+" "+ly+" "+li%(roiWidth)+" "+(li/(roiWidth)));
								
								datas[ly*width + lx] = localDatas[li];
							}
							
							
							cov.dispose(true);
							PlanarImage planarImage = (PlanarImage) cov.getRenderedImage();
							ImageUtilities.disposePlanarImageChain(planarImage);
							cov = null;
							
						}else{
							
							GridCoverage2D cov = CoverageManager.get(temp+"_"+dx+"-"+dy+".tif");
							float[] localDatas = CoverageManager.getData(cov, 1, 1, roiWidth - 1, roiHeight - 1);
							
							// do something
							for(int li=0; li<localDatas.length; li++){
								int ly = li/(roiWidth-1);
								int lx = dx * maxTile - dx + 1 + li%(roiWidth-1);
								datas[ly*width + lx] = localDatas[li];
							}
							
							cov.dispose(true);
							PlanarImage planarImage = (PlanarImage) cov.getRenderedImage();
							ImageUtilities.disposePlanarImageChain(planarImage);
							cov = null;
							
						}
						
						new File(temp+"_"+dx+"-"+dy+".tif").delete();
					}
					
					for(int ly=0; ly<(roiHeight - 1); ly++){
						for(int li=0; li<width; li++){
							writer.write(datas[ly * width + li]+" ");
						}
						writer.newLine();
					}
					
				}
			}
			
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void doClose() {
		bords.clear();
		bords = null;
	}

	
}
