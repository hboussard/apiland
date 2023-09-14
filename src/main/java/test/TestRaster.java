package test;

import java.awt.Color;
import java.awt.image.DataBuffer;
import java.awt.image.WritableRaster;
import java.io.File;

import javax.media.jai.PlanarImage;

import org.geotools.coverage.Category;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.gce.geotiff.GeoTiffFormat;
import org.geotools.gce.geotiff.GeoTiffWriteParams;
import org.geotools.gce.geotiff.GeoTiffWriter;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.util.ImageUtilities;
import org.geotools.referencing.CRS;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValueGroup;

import com.sun.media.jai.codecimpl.util.RasterFactory;

import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.CoverageManager;
import fr.inrae.act.bagap.raster.EnteteRaster;

public class TestRaster {

	
	public static void main(String[] args){
		
		
		Coverage cov = CoverageManager.getCoverage("G:/FRC_Pays_de_la_Loire/data/testRaster/typeBoisement.tif");
		float[] data = cov.getData();
		EnteteRaster entete = cov.getEntete();
		cov.dispose();
		
		//writeGeotiff(new File("G:/FRC_Pays_de_la_Loire/data/testRaster/test1.tif"), data, entete);
		//writeGeotiffDoubleX(new File("G:/FRC_Pays_de_la_Loire/data/testRaster/test2X.tif"), data, entete);
		//writeGeotiffFactorXY(new File("G:/FRC_Pays_de_la_Loire/data/testRaster/testX50Y50.tif"), data, entete, 50, 50);
		//writeGeotiffHuge(new File("G:/FRC_Pays_de_la_Loire/data/testRaster/testHuge.tif"), data, entete);
		writeGeotiff2("G:/FRC_Pays_de_la_Loire/data/testRaster/test4.tif", 50000, 50000);
	}
	
	public static void writeGeotiff2(String fileName, int nrows, int ncols) {
		
		try {
			
			float[][] imagePixelData = new float[nrows][ncols]; 
			int index = 1;
			for (int col = 0; col < ncols; col++) {
				for (int row = 0; row < nrows; row++) {
					imagePixelData[row][col] = index++;
					if(index > 100){
						index = 1;
					}
				}
			}
			
			ReferencedEnvelope env = new ReferencedEnvelope(0, ncols*5, 0, nrows*5, CRS.decode("EPSG:2154"));
			
			GridCoverage2D coverage = new GridCoverageFactory().create("OTPAnalyst", imagePixelData, env);
			
			GeoTiffWriteParams wp = new GeoTiffWriteParams();
			wp.setCompressionMode(GeoTiffWriteParams.MODE_EXPLICIT);
		    wp.setCompressionType("LZW");
		    ParameterValueGroup params = new GeoTiffFormat().getWriteParameters();
		    params.parameter(AbstractGridFormat.GEOTOOLS_WRITE_PARAMS.getName().toString()).setValue(wp);
		    GeoTiffWriter writer = new GeoTiffWriter(new File(fileName));
		    writer.write(coverage, (GeneralParameterValue[]) params.values().toArray(new GeneralParameterValue[1]));
		    
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	public static void writeGeotiffHuge(File out, float[] datas, EnteteRaster entete) {
		
		try {
			WritableRaster raster = RasterFactory.createBandedRaster(DataBuffer.TYPE_FLOAT, entete.width(), entete.height(), 1, null);
			raster.setSamples(0, 0, entete.width(), entete.height(), 0, datas);
			
			//Category noDataCategory = Category.NODATA;
			Category noDataCategory = new Category(
	                Category.NODATA.getName(),
	                new Color(0, 0, 0, 0),
	                entete.noDataValue());
	               
			Category[] categories = new Category[] {noDataCategory};
			GridSampleDimension[] bands;
			bands = new GridSampleDimension[1];
			bands[0] = new GridSampleDimension(null, categories, null);
			
			//System.out.println(minX+" "+maxX+" "+minY+" "+maxY);
			ReferencedEnvelope env = new ReferencedEnvelope(entete.minx(), entete.maxx() + (50-1)*(entete.width()*entete.cellsize()), entete.miny(), entete.maxy() + (50-1)*(entete.height()*entete.cellsize()), CRS.decode("EPSG:2154"));
			
			GridCoverageFactory gcf = new GridCoverageFactory();
			//GridCoverage2D coverage = gcf.create("TIMEGRID", raster, env);
			GridCoverage2D coverage = gcf.create("TIMEGRID", raster, env, bands);
			
			//((WritableRenderedImage) coverage.getRenderedImage()).setData(raster);
			GeoTiffWriteParams wp = new GeoTiffWriteParams();
			wp.setCompressionMode(GeoTiffWriteParams.MODE_EXPLICIT);
			//wp.setCompressionType("LZW");
			ParameterValueGroup params = new GeoTiffFormat().getWriteParameters();
			params.parameter(AbstractGridFormat.GEOTOOLS_WRITE_PARAMS.getName().toString()).setValue(wp);
			GeoTiffWriter writer = new GeoTiffWriter(out);
			
			writer.write(coverage, params.values().toArray(new GeneralParameterValue[1]));
			
			coverage.dispose(true);
			PlanarImage planarImage = (PlanarImage) coverage.getRenderedImage();
			ImageUtilities.disposePlanarImageChain(planarImage);
			coverage = null;
			writer.dispose();
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void writeGeotiffFactorXY(File out, float[] datas, EnteteRaster entete, int factorX, int factorY) {
		
		try {
			WritableRaster raster = RasterFactory.createBandedRaster(DataBuffer.TYPE_FLOAT, entete.width()*factorX, entete.height()*factorY, 1, null);
			
			for(int j=0; j<factorY; j++){
				for(int i=0; i<factorX; i++){
					raster.setSamples(i*entete.width(), j*entete.height(), entete.width(), entete.height(), 0, datas);
				}
			}
			
			
			//Category noDataCategory = Category.NODATA;
			Category noDataCategory = new Category(
	                Category.NODATA.getName(),
	                new Color(0, 0, 0, 0),
	                entete.noDataValue());
	               
			Category[] categories = new Category[] {noDataCategory};
			GridSampleDimension[] bands;
			bands = new GridSampleDimension[1];
			bands[0] = new GridSampleDimension(null, categories, null);
			
			//System.out.println(minX+" "+maxX+" "+minY+" "+maxY);
			ReferencedEnvelope env = new ReferencedEnvelope(entete.minx(), entete.maxx() + (factorX-1)*(entete.width()*entete.cellsize()), entete.miny(), entete.maxy() + (factorY-1)*(entete.height()*entete.cellsize()), CRS.decode("EPSG:2154"));
			GridCoverageFactory gcf = new GridCoverageFactory();
			//GridCoverage2D coverage = gcf.create("TIMEGRID", raster, env);
			GridCoverage2D coverage = gcf.create("TIMEGRID", raster, env, bands);
			
			//((WritableRenderedImage) coverage.getRenderedImage()).setData(raster);
			GeoTiffWriteParams wp = new GeoTiffWriteParams();
			wp.setCompressionMode(GeoTiffWriteParams.MODE_EXPLICIT);
			//wp.setCompressionType("LZW");
			ParameterValueGroup params = new GeoTiffFormat().getWriteParameters();
			params.parameter(AbstractGridFormat.GEOTOOLS_WRITE_PARAMS.getName().toString()).setValue(wp);
			GeoTiffWriter writer = new GeoTiffWriter(out);
			
			writer.write(coverage, params.values().toArray(new GeneralParameterValue[1]));
			
			coverage.dispose(true);
			PlanarImage planarImage = (PlanarImage) coverage.getRenderedImage();
			ImageUtilities.disposePlanarImageChain(planarImage);
			coverage = null;
			writer.dispose();
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void writeGeotiffDoubleX(File out, float[] datas, EnteteRaster entete) {
		
		try {
			WritableRaster raster = RasterFactory.createBandedRaster(DataBuffer.TYPE_FLOAT, entete.width()*2, entete.height(), 1, null);
			raster.setSamples(0, 0, entete.width(), entete.height(), 0, datas);
			raster.setSamples(entete.width(), 0, entete.width(), entete.height(), 0, datas);
			
			//Category noDataCategory = Category.NODATA;
			Category noDataCategory = new Category(
	                Category.NODATA.getName(),
	                new Color(0, 0, 0, 0),
	                entete.noDataValue());
	               
			Category[] categories = new Category[] {noDataCategory};
			GridSampleDimension[] bands;
			bands = new GridSampleDimension[1];
			bands[0] = new GridSampleDimension(null, categories, null);
			
			//System.out.println(minX+" "+maxX+" "+minY+" "+maxY);
			ReferencedEnvelope env = new ReferencedEnvelope(entete.minx(), entete.maxx() + (entete.width()*entete.cellsize()), entete.miny(), entete.maxy(), CRS.decode("EPSG:2154"));
			GridCoverageFactory gcf = new GridCoverageFactory();
			//GridCoverage2D coverage = gcf.create("TIMEGRID", raster, env);
			GridCoverage2D coverage = gcf.create("TIMEGRID", raster, env, bands);
			
			//((WritableRenderedImage) coverage.getRenderedImage()).setData(raster);
			GeoTiffWriteParams wp = new GeoTiffWriteParams();
			wp.setCompressionMode(GeoTiffWriteParams.MODE_EXPLICIT);
			//wp.setCompressionType("LZW");
			ParameterValueGroup params = new GeoTiffFormat().getWriteParameters();
			params.parameter(AbstractGridFormat.GEOTOOLS_WRITE_PARAMS.getName().toString()).setValue(wp);
			GeoTiffWriter writer = new GeoTiffWriter(out);
			
			writer.write(coverage, params.values().toArray(new GeneralParameterValue[1]));
			
			coverage.dispose(true);
			PlanarImage planarImage = (PlanarImage) coverage.getRenderedImage();
			ImageUtilities.disposePlanarImageChain(planarImage);
			coverage = null;
			writer.dispose();
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void writeGeotiff(File out, float[] datas, EnteteRaster entete) {
		
		try {
			WritableRaster raster = RasterFactory.createBandedRaster(DataBuffer.TYPE_FLOAT, entete.width(), entete.height(), 1, null);
			raster.setSamples(0, 0, entete.width(), entete.height(), 0, datas);
			
			//Category noDataCategory = Category.NODATA;
			Category noDataCategory = new Category(
	                Category.NODATA.getName(),
	                new Color(0, 0, 0, 0),
	                entete.noDataValue());
	               
			Category[] categories = new Category[] {noDataCategory};
			GridSampleDimension[] bands;
			bands = new GridSampleDimension[1];
			bands[0] = new GridSampleDimension(null, categories, null);
			
			//System.out.println(minX+" "+maxX+" "+minY+" "+maxY);
			ReferencedEnvelope env = new ReferencedEnvelope(entete.minx(), entete.maxx(), entete.miny(), entete.maxy(), CRS.decode("EPSG:2154"));
			GridCoverageFactory gcf = new GridCoverageFactory();
			//GridCoverage2D coverage = gcf.create("TIMEGRID", raster, env);
			GridCoverage2D coverage = gcf.create("TIMEGRID", raster, env, bands);
			
			//((WritableRenderedImage) coverage.getRenderedImage()).setData(raster);
			GeoTiffWriteParams wp = new GeoTiffWriteParams();
			wp.setCompressionMode(GeoTiffWriteParams.MODE_EXPLICIT);
			//wp.setCompressionType("LZW");
			ParameterValueGroup params = new GeoTiffFormat().getWriteParameters();
			params.parameter(AbstractGridFormat.GEOTOOLS_WRITE_PARAMS.getName().toString()).setValue(wp);
			GeoTiffWriter writer = new GeoTiffWriter(out);
			
			writer.write(coverage, params.values().toArray(new GeneralParameterValue[1]));
			
			coverage.dispose(true);
			PlanarImage planarImage = (PlanarImage) coverage.getRenderedImage();
			ImageUtilities.disposePlanarImageChain(planarImage);
			coverage = null;
			writer.dispose();
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}
