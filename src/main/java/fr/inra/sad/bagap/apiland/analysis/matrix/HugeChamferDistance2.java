package fr.inra.sad.bagap.apiland.analysis.matrix;

import java.awt.image.DataBuffer;
import java.awt.image.WritableRaster;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.stream.ImageOutputStream;
import javax.media.jai.NullOpImage;
import javax.media.jai.OpImage;
import javax.media.jai.PlanarImage;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.image.util.ImageUtilities;

import com.sun.media.imageioimpl.plugins.tiff.TIFFImageWriter;
import com.sun.media.jai.codec.FileSeekableStream;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageDecoder;
import com.sun.media.jai.codec.ImageEncoder;
import com.sun.media.jai.codec.SeekableStream;
import com.sun.media.jai.codec.TIFFDecodeParam;
import com.sun.media.jai.codec.TIFFEncodeParam;
import com.sun.media.jai.codecimpl.util.RasterFactory;

import fr.inra.sad.bagap.apiland.analysis.Analysis;
import fr.inra.sad.bagap.apiland.core.element.manager.Tool;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;

public class HugeChamferDistance2 extends Analysis {

	private String input;
	
	private String output;
	
	private String temp;
	
	private int[] codes;
	
	private GridCoverage2D inCoverage;
	
	private float threshold;
	
	private int maxTile = 5000;
	
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
	
	private short[] calculTab;
	
	private short[] majTab;
	
	public static void main(String[] args){
		
		//String input = "C:/Hugues/temp/distance/test/carto_vallee_seiche_eau_dessus.asc";
		//String output = "C:/Hugues/temp/distance/test/distance_bois.asc";
		
		String input = "F:/data/sig/bretagne/Bretagne_2018_dispositif_bocage_reb_4.tif";
		String output = "F:/FDCCA/bretagne/grain2/distance_bois_bretagne.asc";
		
		//String input = "F:/temp/distance/site1.asc";
		//String output = "F:/temp/distance/distance5/distance_bois.asc";
		
		Set<Integer> c = new HashSet<Integer>();
		//c.add(1);
		//c.add(2);
		//c.add(3);
		c.add(4);
		c.add(5);
		//c.add(6);
		Raster.setNoDataValue(-1);
		new HugeChamferDistance2(input, output, c).allRun();

	}
	
	public HugeChamferDistance2(String input, String output, Collection<Integer> codes) {
		this(input, output, codes, Raster.getNoDataValue());
	}
	
	public HugeChamferDistance2(String input, String output, Collection<Integer> codes, float threshold) {
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
				//finish = true;
			}else{
				
				if(inCoverage != null){
					inCoverage.dispose(true);
					PlanarImage planarImage = (PlanarImage) inCoverage.getRenderedImage();
					ImageUtilities.disposePlanarImageChain(planarImage);
					inCoverage = null;
				}
				
				finish = true;
				
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
							
							// vérification des mises à jour à faire
							Pixel p = new Pixel(dx, dy);
							if(calculTab[dy*dWidth+dx] == -1){ // la tuile n'a jamais été calculée
								
								if(dx > 0){ // le bord gauche de la tuile est inclu et partagé
									if(calculTab[dy*dWidth+(dx-1)] >= 0){ // si la tuile à gauche a été calculée
										float[] bord = bords.get(p).get("left");
										for(int j=0; j<roiHeight; j++){
											outDatas[j*roiWidth+0] = bord[j]; 
										}
									}
								}
								if(dy > 0){ // le bord nord de la tuile est inclu et partagé
									if(calculTab[(dy-1)*dWidth+dx] >= 0){ // si la tuile au nord a été calculée
										float[] bord = bords.get(p).get("north");
										for(int i=0; i<roiWidth; i++){
											outDatas[i] = bord[i];
										}
									}
								}
								if(dx < (dWidth-1)){ // le bord droite de la tuile est inclu et partagé
									if(calculTab[dy*dWidth+(dx+1)] >= 0){ // si la tuile à droite a été calculée
										float[] bord = bords.get(p).get("right");
										for(int j=0; j<roiHeight; j++){
											outDatas[j*roiWidth + (roiWidth-1)] = bord[j];
										}
									}
								}
								if(dy < (dHeight-1)){ // le bord sud de la tuile est inclu et partagé
									if(calculTab[(dy+1)*dWidth+dx] >= 0){ // si la tuile au sud a été calculée
										float[] bord = bords.get(p).get("south");
										for(int i=0; i<roiWidth; i++){
											outDatas[(roiHeight-1)*roiWidth + i] = bord[i];
										}
									}
								}
							}else{ // tuile a déjà été calculé
								
								if(dx > 0){ // le bord gauche de la tuile est inclu et partagé
									float[] bord = bords.get(p).get("left");
									for(int j=0; j<roiHeight; j++){
										if(bord[j] < outDatas[j*roiWidth+0]){
											outDatas[j*roiWidth+0] = bord[j]; 
										}
									}
								}
								if(dy > 0){ // le bord nord de la tuile est inclu et partagé
									float[] bord = bords.get(p).get("north");
									for(int i=0; i<roiWidth; i++){
										if(bord[i] < outDatas[i]){
											outDatas[i] = bord[i];
										}
									}
								}
								if(dx < (dWidth-1)){ // le bord droite de la tuile est inclu et partagé
									float[] bord = bords.get(p).get("right");
									for(int j=0; j<roiHeight; j++){
										if(bord[j] < outDatas[j*roiWidth + (roiWidth-1)]){
											outDatas[j*roiWidth + (roiWidth-1)] = bord[j];
										}
									}
								}
								if(dy < (dHeight-1)){ // le bord sud de la tuile est inclu et partagé
									float[] bord = bords.get(p).get("south");
									for(int i=0; i<roiWidth; i++){
										if(bord[i] < outDatas[(roiHeight-1)*roiWidth + i]){
											outDatas[(roiHeight-1)*roiWidth + i] = bord[i];
										}
									}
								}
							}
								
							CoverageChamferDistance ccd = new CoverageChamferDistance(outDatas, roiWidth, roiHeight, cellSize);
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
		
		// normalisation
		normalisation();
		
		
		// compilation
		compile4();
	}

	/**
	 * 3 cas
	 * 1. la tuile n'a jamais été mise à jour, peut-être que les bords vont l'y obliger...
	 * 2. la tuile n'a pas été mis à jour (cette fois), la mise à jour des bords se fera sous condition de l'tétat des tuiles voisines
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
	
	private void normalisation(){
		// normalisation
		int dx = 0, dy = 0;
		for(int y=0; y<height; dy++, y+=maxTile-1){
			dx = 0;
			for(int x=0; x<width; dx++, x+=maxTile-1){
						
				System.out.println("traitement de la tuile "+dx+" "+dy);
						
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
		//WritableRaster raster =	inCoverage.getRenderedImage().getData().createCompatibleWritableRaster(0, 0, width, height);
		
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
	
	public void compile4() {
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
						//new File(temp+"_"+dx+"-"+dy+".tif").delete();
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
						
						//new File(temp+"_"+dx+"-"+dy+".tif").delete();
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
	
	public void compile3() {
		
		try {
			
			//File output = File.createTempFile("f:/temp/distance/distance5/test", ".tif");
			//   output.deleteOnExit();
			
			File outputF = new File(output);

			TIFFImageWriter writer = new TIFFImageWriter(null);
			// 	  ImageWriter writer = ImageIO.getImageWritersByFormatName("PNG").next();
			//   ImageWriter writer = ImageIO.getImageWritersByFormatName("BMP").next();
			ImageOutputStream stream = ImageIO.createImageOutputStream(outputF);

		
			writer.setOutput(stream);
			ImageWriteParam param = writer.getDefaultWriteParam();
			param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			
			writer.prepareWriteSequence(null);
			
			//	       param.setCompressionType("None");
			//	       param.setCompressionType("PackBits");
			//	       param.setCompressionType("ZLib");
			//param.setCompressionType(param.getCompressionTypes()[compression]);
			//	       if (compression == 2) {
			//	           param.setCompressionQuality(0);
			//	       }
			
			// tuilage à priori
			int dx = 0, dy = 0;
			for(int y=0; y<height; dy++, y+=maxTile-1){
				dx = 0;
				for(int x=0; x<width; dx++, x+=maxTile-1){
					
					int roiWidth = Math.min(maxTile, width - x);
					int roiHeight = Math.min(maxTile, height - y);
						
					System.out.println("traitement de la tuile "+dx+" "+dy+" / "+dWidth+" "+dHeight+" "+x+" "+y+" "+roiWidth+" "+roiHeight);
						
					GridCoverage2D cov = CoverageManager.get(temp+"_"+dx+"-"+dy+".tif");
					//float[] localDatas = CoverageManager.getData(cov, 0, 0, roiWidth, roiHeight);
						
					
					long start = System.currentTimeMillis();
					//writer.write(null, new IIOImage(cov.getRenderedImage(), null, null), param);
					writer.writeToSequence(new IIOImage(cov.getRenderedImage(), null, null), param);
					//writer.writeInsert(0, new IIOImage(cov.getRenderedImage(), null, null), param);
					System.err.println("Write time: " + (System.currentTimeMillis() - start) + " ms");
					
						
					cov.dispose(true);
					PlanarImage planarImage = (PlanarImage) cov.getRenderedImage();
					ImageUtilities.disposePlanarImageChain(planarImage);
					cov = null;
						
					//new File(temp+"_"+dx+"-"+dy+".tif").delete();
					
				}
			}
			
			writer.endWriteSequence();
			writer.reset();
			writer.dispose();
			
			stream.flush();
			stream.close();
			
		} catch(IOException ex){
				ex.printStackTrace();
		}
	}
	
	
	
	public void compile2() {
		
		try {
			
			FileOutputStream fos = new FileOutputStream(output);
			TIFFEncodeParam encodeParam = new TIFFEncodeParam();
			encodeParam.setTileSize(width, height);
			ImageEncoder encoder = ImageCodec.createImageEncoder("tiff", fos, encodeParam);
		            
			// tuilage à priori
			int dx = 0, dy = 0;
			for(int y=0; y<height; dy++, y+=maxTile-1){
				dx = 0;
				for(int x=0; x<width; dx++, x+=maxTile-1){
						
					int roiWidth = Math.min(maxTile, width - x);
					int roiHeight = Math.min(maxTile, height - y);
						
					System.out.println("traitement de la tuile "+dx+" "+dy+" / "+dWidth+" "+dHeight+" "+x+" "+y+" "+roiWidth+" "+roiHeight);
						
					File file = new File(temp+"_"+dx+"-"+dy+".tif");
					SeekableStream stream = new FileSeekableStream(file);
					TIFFDecodeParam decodeParam = null;
					ImageDecoder decoder = ImageCodec.createImageDecoder("tiff", stream, decodeParam);
					
					PlanarImage image = new NullOpImage(decoder.decodeAsRenderedImage(0), null, OpImage.OP_IO_BOUND, null);
					
					encoder.encode(image);
					
					//new File(temp+"_"+dx+"-"+dy+".tif").delete();
				}
			}
			
			fos.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		/*
		try {
			PlanarImage image;
			File file = new File(output);
			SeekableStream stream = new FileSeekableStream(file);
			TIFFDecodeParam decodeParam = null;
			ImageDecoder decoder = ImageCodec.createImageDecoder("tiff", stream, decodeParam);
		            
			// tuilage à priori
			int dx = 0, dy = 0;
			for(int y=0; y<height; dy++, y+=maxTile-1){
				dx = 0;
				for(int x=0; x<width; dx++, x+=maxTile-1){
						
					int roiWidth = Math.min(maxTile, width - x);
					int roiHeight = Math.min(maxTile, height - y);
						
					System.out.println("traitement de la tuile "+dx+" "+dy+" / "+dWidth+" "+dHeight+" "+x+" "+y+" "+roiWidth+" "+roiHeight);
						
					image = new NullOpImage(decoder.decodeAsRenderedImage(0), null, OpImage.OP_IO_BOUND, null);
					String newFileName = temp+"_"+dx+"-"+dy+".tif";
					FileOutputStream fos = new FileOutputStream(newFileName);
					TIFFEncodeParam encodeParam = null;
					ImageEncoder encoder = ImageCodec.createImageEncoder("tiff", fos, encodeParam);
					encoder.encode(image);
					fos.close();
						
					//new File(temp+"_"+dx+"-"+dy+".tif").delete();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
	}

	@Override
	protected void doClose() {
		bords.clear();
		bords = null;
	}

}
