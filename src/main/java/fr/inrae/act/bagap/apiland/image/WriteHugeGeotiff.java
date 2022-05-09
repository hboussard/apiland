package fr.inrae.act.bagap.apiland.image;

import java.awt.image.DataBuffer;
import java.awt.image.WritableRaster;
import java.io.File;
import java.util.Map;
import java.util.Map.Entry;

import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.imageio.GeoToolsWriteParams;
import org.geotools.gce.geotiff.GeoTiffFormat;
import org.geotools.gce.geotiff.GeoTiffReader;
import org.geotools.gce.geotiff.GeoTiffWriteParams;
import org.geotools.gce.geotiff.GeoTiffWriter;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.util.ImageUtilities;
import org.geotools.referencing.CRS;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValueGroup;

import com.sun.media.jai.codecimpl.util.RasterFactory;

import fr.inra.sad.bagap.apiland.analysis.matrix.ArrayChamferDistanceAnalysis;
import fr.inra.sad.bagap.apiland.analysis.matrix.CoverageManager;

public class WriteHugeGeotiff {

	public static void main(String[] args){
		
		script1();
		
	}
	
	private static void script2(){
		try {
			
			String path = "F:/FRC_Pays_de_la_Loire/data/BocagePdlL_V3/test/";
			String temp = path+"distance_bois_2021";
			File out = new File(path+"out.tif");
			GeoTiffWriter writer = new GeoTiffWriter(out);
			int width = 54915;
			int height = 51446;
			int maxTile = 10000;
			
			double minX = 273459.6885533562;
			double maxX = 548034.6885533562;
			double minY = 6787311.697256473;
			double maxY = 6837311.697256473;
			// normalisation
			int dx = 0, dy = 0;
			int y = 0;
			
			//GeoTiffWriter writer = new GeoTiffWriter();
			//writer.write(gc, params);
			WritableRaster raster = RasterFactory.createBandedRaster(DataBuffer.TYPE_FLOAT, width, maxTile, 1, null);
			ReferencedEnvelope env = new ReferencedEnvelope(minX, maxX, minY, maxY, CRS.decode("EPSG:2154"));
			GridCoverageFactory gcf = new GridCoverageFactory();
			GridCoverage2D coverage = gcf.create("TIMEGRID", raster, env);
			
			for(int x=0; x<width; dx++, x+=maxTile-1){
									
				System.out.println("traitement de la tuile "+dx+" "+dy);
								
				int roiWidth = Math.min(maxTile, width - x);
				int roiHeight = Math.min(maxTile, height - y);
				/*
				double roiPosX = imageMinX + x*cellSize;
				double roiPosMaxX = roiPosX + roiWidth * cellSize;
				double roiPosY = Math.max(imageMinY, imageMaxY - (y+roiHeight)*cellSize);
				double roiPosMaxY = roiPosY + roiHeight * cellSize;
				*/		
				
				GridCoverage2D cov = CoverageManager.get(temp+"_"+dx+"-"+dy+".tif");
				float[] outDatas = CoverageManager.getData(cov, 0, 0, roiWidth, roiHeight);
				
				
				//((WritableRenderedImage) coverage.getRenderedImage()).setData(raster);
				GeoTiffWriteParams wp = new GeoTiffWriteParams();
				wp.setCompressionMode(GeoTiffWriteParams.MODE_EXPLICIT);
				//wp.setCompressionType("LZW");
				ParameterValueGroup params = new GeoTiffFormat().getWriteParameters();
				params.parameter(AbstractGridFormat.GEOTOOLS_WRITE_PARAMS.getName().toString()).setValue(wp);
				
				writer.write(coverage, params.values().toArray(new GeneralParameterValue[1]));
				cov.dispose(true);
				PlanarImage planarImage = (PlanarImage) cov.getRenderedImage();
				ImageUtilities.disposePlanarImageChain(planarImage);
				cov = null;
				break;
			}
			
			writer.dispose();
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		
	}
	
	private static void script1(){
		try {
			
			int width = 54915;
			int height = 51446;
			int maxTile = 10000;
			
			String path = "F:/FRC_Pays_de_la_Loire/data/BocagePdlL_V3/test2/";
			String temp = path+"distance_bois_2021";
			File out = new File(path+"distance_bois_2021.tif");
			
			System.out.println(Integer.MAX_VALUE+" "+(width*height));
			
			//((WritableRenderedImage) coverage.getRenderedImage()).setData(raster);
			GeoTiffWriteParams wp = new GeoTiffWriteParams();
			wp.setCompressionMode(GeoTiffWriteParams.MODE_EXPLICIT);
			//wp.setTilingMode(GeoToolsWriteParams.MODE_EXPLICIT);
			//wp.setTiling(maxTile,  maxTile);
			//wp.setCompressionType("LZW");
			ParameterValueGroup params = new GeoTiffFormat().getWriteParameters();
			params.parameter(AbstractGridFormat.GEOTOOLS_WRITE_PARAMS.getName().toString()).setValue(wp);
			//JAI.getDefaultInstance().getTileCache().setMemoryCapacity(maxTile*maxTile);
			
			//54915 51446
			//273459.6885533562 548034.6885533562 6580081.697256473 6837311.697256473
			
			
			
			double minX = 273459.6885533562;
			double maxX = 548034.6885533562;
			double minY = 6580081.697256473;
			double maxY = 6837311.697256473;
			
			//GeoTiffWriter writer = new GeoTiffWriter();
			//writer.write(gc, params);
			WritableRaster raster = RasterFactory.createBandedRaster(DataBuffer.TYPE_FLOAT, width, height, 1, null);
			ReferencedEnvelope env = new ReferencedEnvelope(minX, maxX, minY, maxY, CRS.decode("EPSG:2154"));
			GridCoverageFactory gcf = new GridCoverageFactory();
			GridCoverage2D coverage = gcf.create("TIMEGRID", raster, env);
			
			int dx = 0, dy = 0;
			for(int y=0; y<height; dy++, y+=maxTile-1){
				dx = 0;
				for(int x=0; x<width; dx++, x+=maxTile-1){
					System.out.println("traitement de la tuile "+dx+" "+dy);
					
					int roiWidth = Math.min(maxTile, width - x);
					int roiHeight = Math.min(maxTile, height - y);
					
					//double roiPosX = imageMinX + x*cellSize;
					//double roiPosMaxX = roiPosX + roiWidth * cellSize;
					//double roiPosY = Math.max(imageMinY, imageMaxY - (y+roiHeight)*cellSize);
					//double roiPosMaxY = roiPosY + roiHeight * cellSize;
							
					float[] outDatas = new float[roiWidth*roiHeight];
					//raster.setSamples(x, 0, roiWidth, roiHeight, 0, outDatas);
					
					GridCoverage2D cov = CoverageManager.get(temp+"_"+dx+"-"+dy+".tif");
					outDatas = CoverageManager.getData(cov, 0, 0, roiWidth, roiHeight);
					cov.dispose(true);
					PlanarImage planarImage = (PlanarImage) cov.getRenderedImage();
					ImageUtilities.disposePlanarImageChain(planarImage);
					cov = null;
					
					raster.setSamples(x, y, roiWidth, roiHeight, 0, outDatas);
					
					GeoTiffWriter writer = new GeoTiffWriter(out);
					writer.write(coverage, params.values().toArray(new GeneralParameterValue[1]));
					writer.dispose();
				}
			}
			
			coverage.dispose(true);
			PlanarImage planarImage2 = (PlanarImage) coverage.getRenderedImage();
			ImageUtilities.disposePlanarImageChain(planarImage2);
			coverage = null;
			
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		
	}
	
}
