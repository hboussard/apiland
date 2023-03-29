package fr.inra.sad.bagap.apiland.analysis.matrix;

import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferFloat;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.awt.image.WritableRenderedImage;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.media.jai.ImageLayout;
import javax.media.jai.JAI;
import javax.media.jai.ParameterBlockJAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.RenderedOp;

import org.apache.commons.io.FileUtils;
import org.geotools.coverage.Category;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.imageio.geotiff.GeoTiffIIOMetadataEncoder;
import org.geotools.coverage.grid.io.imageio.geotiff.codes.GeoTiffGCSCodes;
import org.geotools.coverage.util.CoverageUtilities;
import org.geotools.data.DataSourceException;
import org.geotools.gce.arcgrid.ArcGridReader;
import org.geotools.gce.arcgrid.ArcGridWriter;
import org.geotools.gce.geotiff.GeoTiffFormat;
import org.geotools.gce.geotiff.GeoTiffReader;
import org.geotools.gce.geotiff.GeoTiffWriteParams;
import org.geotools.gce.geotiff.GeoTiffWriter;
import org.geotools.geometry.Envelope2D;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.util.ImageUtilities;
import org.geotools.metadata.i18n.Vocabulary;
import org.geotools.metadata.i18n.VocabularyKeys;
import org.geotools.parameter.AbstractParameterDescriptor;
import org.geotools.parameter.Parameter;
import org.geotools.referencing.CRS;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.sun.media.jai.codecimpl.util.RasterFactory;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.EnteteRaster;
import fr.inrae.act.bagap.raster.FileCoverage;

public class CoverageManager {

	// private static GeneralEnvelope env;

	public static void writeGeotiffHuge(GridCoverage2D coverage, File out, float[] datas, int width, int height, int roiWidth, int roiHeight, int posX, int posY, double minX, double maxX, double minY, double maxY) {
		
		try {
			final WritableRaster raster = RasterFactory.createBandedRaster(DataBuffer.TYPE_FLOAT, width, height, 1, null);
			raster.setSamples(posX, posY, roiWidth, roiHeight, 0, datas);
			
			//System.out.println(minX+" "+maxX+" "+minY+" "+maxY);
			ReferencedEnvelope env = new ReferencedEnvelope(minX, maxX, minY, maxY, CRS.decode("EPSG:2154"));
			GridCoverageFactory gcf = new GridCoverageFactory();
			//GridCoverage2D coverage = gcf.create("TIMEGRID", raster, env);
			((WritableRenderedImage) coverage.getRenderedImage()).setData(raster);
			GeoTiffWriteParams wp = new GeoTiffWriteParams();
			wp.setCompressionMode(GeoTiffWriteParams.MODE_EXPLICIT);
			wp.setCompressionType("LZW");
			ParameterValueGroup params = new GeoTiffFormat().getWriteParameters();
			params.parameter(AbstractGridFormat.GEOTOOLS_WRITE_PARAMS.getName().toString()).setValue(wp);
			GeoTiffWriter writer = new GeoTiffWriter(out);
			
			writer.write(coverage, params.values().toArray(new GeneralParameterValue[1]));
			writer.dispose();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void writeGeotiff(String out, float[] datas, EnteteRaster entete) {
		writeGeotiff(new File(out), datas, entete);
	}
	
	public static void writeGeotiff(File out, float[] datas, EnteteRaster entete) {
		
		try {
			WritableRaster raster = RasterFactory.createBandedRaster(DataBuffer.TYPE_FLOAT, entete.width(), entete.height(), 1, null);
			raster.setSamples(0, 0, entete.width(), entete.height(), 0, datas);
			
			//System.out.println(minX+" "+maxX+" "+minY+" "+maxY);
			ReferencedEnvelope env = new ReferencedEnvelope(entete.minx(), entete.maxx(), entete.miny(), entete.maxy(), CRS.decode("EPSG:2154"));
			GridCoverageFactory gcf = new GridCoverageFactory();
			GridCoverage2D coverage = gcf.create("TIMEGRID", raster, env);
			
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
	
	
	public static void writeGeotiff(String out, float[] datas, int width, int height, double minX, double maxX, double minY, double maxY) {
		writeGeotiff(new File(out), datas, width, height, minX, maxX, minY, maxY);
	}
	
	public static void writeGeotiff(File out, float[] datas, int width, int height, double minX, double maxX, double minY, double maxY) {
		
		try {
			WritableRaster raster = RasterFactory.createBandedRaster(DataBuffer.TYPE_FLOAT, width, height, 1, null);
			raster.setSamples(0, 0, width, height, 0, datas);
			
			//System.out.println(minX+" "+maxX+" "+minY+" "+maxY);
			ReferencedEnvelope env = new ReferencedEnvelope(minX, maxX, minY, maxY, CRS.decode("EPSG:2154"));
			GridCoverageFactory gcf = new GridCoverageFactory();
			GridCoverage2D coverage = gcf.create("TIMEGRID", raster, env);
			
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
	
	private static CoordinateReferenceSystem crs;

	public static Coverage getCoverage(String raster) {
		// coverage et infos associees
		GridCoverage2DReader reader = null;
		
		try {	
			if(raster.endsWith(".asc")){
				File file = new File(raster);
				reader = new ArcGridReader(file);
			}else if(raster.endsWith(".tif")){
				File file = new File(raster);
				reader = new GeoTiffReader(file);
			}else{
				throw new IllegalArgumentException(raster+" is not a recognize raster");
			}
			GridCoverage2D coverage2D = (GridCoverage2D) reader.read(null);
			reader.dispose(); // a� tester, ca va peut-etre bloquer la lecture des donnees
						
			int inWidth = (Integer) coverage2D.getProperty("image_width");
			int inHeight = (Integer) coverage2D.getProperty("image_height");
			double inMinX = coverage2D.getEnvelope().getMinimum(0);
			double inMinY = coverage2D.getEnvelope().getMinimum(1);
			double inMaxX = coverage2D.getEnvelope().getMaximum(0);
			double inMaxY = coverage2D.getEnvelope().getMaximum(1);
			float inCellSize = (float) ((java.awt.geom.AffineTransform) coverage2D.getGridGeometry().getGridToCRS2D()).getScaleX();
						
			EnteteRaster entete = new EnteteRaster(inWidth, inHeight, inMinX, inMaxX, inMinY, inMaxY, inCellSize, -1);
			Coverage coverage = new FileCoverage(coverage2D, entete);
			
			return coverage;
		}catch (DataSourceException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	public static GridCoverage2D get(String raster) {
		AbstractGridCoverage2DReader reader = null;
		try {
			File file = new File(raster);
			
			if (raster.endsWith(".asc")) {
				reader = new ArcGridReader(file);
			} else if (raster.endsWith(".tif") || raster.endsWith(".tiff")) {
				reader = new GeoTiffReader(file);
			} else {
				throw new IllegalArgumentException("format not supported for " + raster);
			}
			return (GridCoverage2D) reader.read(null);
		
		} catch (DataSourceException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			reader.dispose();
		}

		return null;
		//GeneralEnvelope env = (GeneralEnvelope) coverage.getEnvelope();
		//crs = coverage.getCoordinateReferenceSystem();

		/*
		 * Map<?,?> m = coverage.getProperties(); for(Entry<?,?> e :
		 * m.entrySet()){ System.out.println(e.getValue()+" "+e.getKey()); }
		 * System.out.println(coverage.getCoordinateReferenceSystem());
		 */
		//return coverage;
	}

	public static float[] getData(GridCoverage2D coverage, int roiX, int roiY, int roiWidth, int roiHeight) {

		Rectangle roi = new Rectangle(roiX, roiY, roiWidth, roiHeight);
		float[] inDatas = new float[roiWidth * roiHeight];
		inDatas = coverage.getRenderedImage().getData(roi).getSamples(roi.x, roi.y, roi.width, roi.height, 0, inDatas);

		return inDatas;
	}

	public static float[] readData(String file, int roiX, int roiY, int roiWidth, int roiHeight) {
		try {
			ParameterBlockJAI pbj = new ParameterBlockJAI("imageread");
			pbj.setParameter("Input", ImageIO.createImageInputStream(new File(file)));

			ImageLayout layout = new ImageLayout();
			layout.setMinX(roiX);
			layout.setMinY(roiY);
			layout.setTileWidth(roiWidth);
			layout.setTileHeight(roiHeight);
			RenderingHints hints = new RenderingHints(JAI.KEY_IMAGE_LAYOUT, layout);

			RenderedOp ro = JAI.create("imageread", pbj, hints);

			float[] inDatas = new float[roiWidth * roiHeight];
			return ro.getData().getSamples(roiX, roiY, roiWidth, roiHeight, 0, inDatas);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static float[][] getData2D(GridCoverage2D coverage, int roiX, int roiY, int roiWidth, int roiHeight) {

		Rectangle roi = new Rectangle(roiX, roiY, roiWidth, roiHeight);
		float[] inDatas = new float[roiWidth * roiHeight];
		inDatas = coverage.getRenderedImage().getData(roi).getSamples(roi.x, roi.y, roi.width, roi.height, 0, inDatas);

		float[][] inDatas2D = new float[roiHeight][roiWidth];
		for (int j = 0; j < roiHeight; j++) {
			for (int i = 0; i < roiWidth; i++) {
				inDatas2D[j][i] = inDatas[j * roiWidth + i];
			}
		}

		return inDatas2D;
	}

	public static void setData(GridCoverage2D coverage, int roiX, int roiY, int roiWidth, int roiHeight,
			float[] datas) {
		Rectangle roi = new Rectangle(roiX, roiY, roiWidth, roiHeight);
		// ((WritableRaster)
		// coverage.getRenderedImage().getData(roi)).setPixels(roi.x, roi.y,
		// roi.width, roi.height, datas);
		// ((WritableRaster)
		// coverage.getRenderedImage().getData(roi)).setPixels(0, 0, roi.width,
		// roi.height, datas);
		((WritableRaster) coverage.getRenderedImage().getData()).setPixels(roiX, roiY, roiWidth, roiHeight, datas);
		// ((WritableRaster)
		// coverage.getRenderedImage().getData(roi)).setSamples(roi.x, roi.y,
		// roi.width, roi.height, 0, datas);
		/*
		 * float[] f = new float[1]; for(int j = 0; j < roiHeight; j++){ for(int
		 * i = 0; i < roiWidth; i++){ f[0] = datas[j*roiWidth + i];
		 * ((WritableRaster)
		 * coverage.getRenderedImage().getData(roi)).setPixel(i, j, f); } }
		 */
	}
	
	public static GridCoverage2D getEmptyCoverage(int width, int height, double minX, double maxX, double minY, double maxY, double cellSize){
		
		try {
			final WritableRaster raster = RasterFactory.createBandedRaster(DataBuffer.TYPE_FLOAT, width, height, 1, null);
			//raster.setSamples(posX, posY, roiWidth, roiHeight, 0, datas);
			//System.out.println(minX+" "+maxX+" "+minY+" "+maxY);
			ReferencedEnvelope env = new ReferencedEnvelope(minX, maxX, minY, maxY, CRS.decode("EPSG:2154"));
			GridCoverageFactory gcf = new GridCoverageFactory();
			GridCoverage2D coverage = gcf.create("TIMEGRID", raster, env);
			
			return coverage;
		} catch (MismatchedDimensionException | FactoryException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static GridCoverage2D getCoverageUsingRaster(WritableRaster raster, int width, int height, double minX, double maxX, double minY, double maxY, double cellSize){
		
		try {
			ReferencedEnvelope env = new ReferencedEnvelope(minX, maxX, minY, maxY, CRS.decode("EPSG:2154"));
			GridCoverageFactory gcf = new GridCoverageFactory();
			GridCoverage2D coverage = gcf.create("TIMEGRID", raster, env);
			
			return coverage;
		} catch (MismatchedDimensionException | FactoryException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static GridCoverage2D getCoverageFromData(float[] datas, int width, int height, double posX, double posY, double cellSize) {

		GridCoverage2D cov = null;
		try {
			CoordinateReferenceSystem crs = CRS.decode("EPSG:2154");
			GridCoverageFactory gcf = new GridCoverageFactory();
			Envelope2D e = new Envelope2D(crs, posX, posY, width * cellSize, height * cellSize);
			
			float[][] d = new float[height][width];
			for (int j = 0; j < height; j++) {
				for (int i = 0; i < width; i++) {
					d[j][i] = datas[j * width + i];
				}
			}
			cov = gcf.create("output", d, e);
				// System.out.println(((java.awt.geom.AffineTransform)
				// cov.getGridGeometry().getGridToCRS2D()).getScaleX());
			
		} catch (FactoryException e1) {
			e1.printStackTrace();
		}
		
		return cov;

		/*
		 * DataBufferFloat buffer = new DataBufferFloat(datas, datas.length);
		 * int[] bandMasks = {0xFF0000, 0xFF00, 0xFF, 0xFF000000}; // ARGB (yes,
		 * ARGB, as the masks are R, G, B, A always) order WritableRaster raster
		 * = java.awt.image.Raster.c.createPackedRaster(buffer, width, height,
		 * width, bandMasks, null);
		 */

		// return gcf.create("output", d, env);

		// CoordinateReferenceSystem sourceCRS = CRS.decode("EPSG:2154");
		// CoordinateReferenceSystem sourceCRS = null;
		// System.out.println(crs);
		// Envelope2D e = new Envelope2D(crs, posX, posY, posX+(width*cellSize),
		// posY+(height*cellSize));

		// GridCoverage2D cov = gcf.create("output", d, e);
		// System.out.println(((java.awt.geom.AffineTransform)
		// cov.getGridGeometry().getGridToCRS2D()).getScaleX());
		// return cov;

		// return gcf.create("output", d, e);

		/*
		 * Category[] categories = new Category[1]; categories[0] = new
		 * Category("No Data", null, -1, false); GridSampleDimension gsd = new
		 * GridSampleDimension("sample", categories, null);
		 * GridSampleDimension[] gsds = new GridSampleDimension[1]; gsds[0] =
		 * gsd; return gcf.create("output", raster, env, gsds);
		 */
		/*
		 * } catch (FactoryException e1) { e1.printStackTrace(); } return null;
		 */
	}

	public static GridCoverage2D getCoverageFromData2D(float[][] datas, int width, int height, double posX, double posY, double cellSize) {
		GridCoverage2D cov = null;
		try {
			CoordinateReferenceSystem crs = CRS.decode("EPSG:2154");
			GridCoverageFactory gcf = new GridCoverageFactory();
			Envelope2D e = new Envelope2D(crs, posX, posY, width * cellSize, height * cellSize);
			cov =  gcf.create("output", datas, e);
		} catch (FactoryException e1) {
			e1.printStackTrace();
		}
		
		return cov;
	}

	public static void exportAsciiGrid(GridCoverage2D coverage, String output) {
		// System.out.println(output);
		try {
			ArcGridWriter writer = new ArcGridWriter(new File(output));

			/*
			 * ParameterValueGroup params =
			 * writer.getFormat().getWriteParameters();
			 * System.out.println(params);
			 */
			/*
			 * AbstractParameterDescriptor.createValue() GeneralParameterValue[]
			 * gpv = new GeneralParameterValue[1]; gpv[0] = new
			 * GeneralParameterValue("GC_NODATA", Raster.getNoDataValue());
			 */
			/*
			 * Map<String, Object> properties = new HashMap<String, Object>();
			 * NoDataContainer noDataContainer = new
			 * NoDataContainer(Raster.getNoDataValue());
			 * CoverageUtilities.setNoDataProperty(properties, noDataContainer);
			 */

			GeneralParameterValue[] gpv = null;
			/*
			 * GeneralParameterValue[] gpv = new GeneralParameterValue[1];
			 * gpv[0] = Parameter.create("No Data", Raster.getNoDataValue());
			 */
			/*
			 * if(coverage.getRenderedImage() instanceof
			 * javax.media.jai.WritableRenderedImageAdapter){
			 * System.out.println("pass1");
			 * ((javax.media.jai.WritableRenderedImageAdapter)
			 * coverage.getRenderedImage()).setProperty("GC_NODATA", new
			 * NoDataContainer(Raster.getNoDataValue()));
			 * System.out.println(coverage.getRenderedImage().getProperty(
			 * "GC_NODATA").getClass()); }else if(coverage.getRenderedImage()
			 * instanceof javax.media.jai.RenderedOp){
			 * System.out.println("pass2"); ((javax.media.jai.RenderedOp)
			 * coverage.getRenderedImage()).setProperty("GC_NODATA", new
			 * NoDataContainer(Raster.getNoDataValue())); }else{
			 * System.out.println(coverage.getRenderedImage().getClass()); }
			 * 
			 * System.out.println(coverage.getProperty("GC_NODATA").getClass());
			 * NoDataContainer ndc = (NoDataContainer)
			 * coverage.getProperty("GC_NODATA"); System.out.println(
			 * "nouveau nodata_value est "+ndc.getAsSingleValue());
			 * 
			 * 
			 * ParameterValueGroup params =
			 * writer.getFormat().getWriteParameters();
			 * System.out.println(params);
			 * params.parameter("GC_NODATA").setValue(Raster.getNoDataValue());
			 * gpv = { params.parameter("GC_NODATA") };
			 */

			/*
			 * GridSampleDimension sd = (GridSampleDimension)
			 * coverage.getSampleDimension(0);
			 * 
			 * List<Category> categories = sd.getCategories();
			 * Iterator<Category> it = categories.iterator(); Category
			 * candidate; double inNoData = Double.NaN; String noDataName =
			 * Vocabulary.format(VocabularyKeys.NODATA); while (it.hasNext()) {
			 * candidate = (Category) it.next(); String name =
			 * candidate.getName().toString(); System.out.println("candidat "
			 * +name); if (name.equalsIgnoreCase("No Data") ||
			 * name.equalsIgnoreCase(noDataName)) { inNoData =
			 * candidate.getRange().getMaximum(); System.out.println("valeur "
			 * +inNoData); } }
			 */

			writer.write(coverage, gpv);
			writer.dispose();
		} catch (IllegalArgumentException | IOException e) {
			e.printStackTrace();
		}

	}

	public static void write(WritableRaster raster, int width, int height, double imageMinX, double imageMaxX, double imageMinY, double imageMaxY, float cellSize, String output) {

		GridCoverage2D outC = CoverageManager.getCoverageUsingRaster(raster, width, height, imageMinX, imageMaxX, imageMinY, imageMaxY, cellSize);
		
		if(output.endsWith(".tif")){
			writeTiff(outC, output);
		}else if(output.endsWith(".asc")){
			writeAsciiGrid(outC, output);
		}else{
			throw new IllegalArgumentException("extension of "+output+" unknown");
		}
		
		outC.dispose(true);
		PlanarImage planarImage = (PlanarImage) outC.getRenderedImage();
		ImageUtilities.disposePlanarImageChain(planarImage);
		outC = null;
		
	}

	public static void writeAsciiGrid(String ascii, float[] datas, EnteteRaster entete) {
		
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(ascii));
			
			writer.write("ncols "+entete.width());
			writer.newLine();
			writer.write("nrows "+entete.height());
			writer.newLine();
			writer.write("xllcorner "+entete.minx());
			writer.newLine();
			writer.write("yllcorner "+entete.miny());
			writer.newLine();
			writer.write("cellsize "+entete.cellsize());
			writer.newLine();
			writer.write("NODATA_value "+entete.noDataValue());
			writer.newLine();
			
			for(int j=0; j<entete.height(); j++){
				for(int i=0; i<entete.width(); i++){
					writer.write(datas[j*entete.width()+i]+" ");
				}
				writer.newLine();
			}
			
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void writeAsciiGrid(String ascii, float[] datas, int width, int height, double minx, double miny, double cellsize, int noDataValue) {
		
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(ascii));
			
			writer.write("ncols "+width);
			writer.newLine();
			writer.write("nrows "+height);
			writer.newLine();
			writer.write("xllcorner "+minx);
			writer.newLine();
			writer.write("yllcorner "+miny);
			writer.newLine();
			writer.write("cellsize "+cellsize);
			writer.newLine();
			writer.write("NODATA_value "+noDataValue);
			writer.newLine();
			
			for(int j=0; j<height; j++){
				for(int i=0; i<width; i++){
					writer.write(datas[j*width+i]+" ");
				}
				writer.newLine();
			}
			
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void writeAsciiGrid(GridCoverage2D outC, String output) {
		try {
			ArcGridWriter writer = new ArcGridWriter(new File(output));
			writer.write(outC, null);
			writer.dispose();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static void writeTiff(GridCoverage2D outC, String output) {
		try {
			
			GeoTiffWriteParams wp = new GeoTiffWriteParams();
			wp.setCompressionMode(GeoTiffWriteParams.MODE_EXPLICIT);
			//wp.setForceToBigTIFF(true);
			//wp.setCompressionType("LZW");
			ParameterValueGroup params = new GeoTiffFormat().getWriteParameters();
			params.parameter(AbstractGridFormat.GEOTOOLS_WRITE_PARAMS.getName().toString()).setValue(wp);
			GeoTiffWriter writer = new GeoTiffWriter(new File(output));
			writer.write(outC, params.values().toArray(new GeneralParameterValue[1]));
			writer.dispose();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void retile(String inputRaster, String outputRaster, double minx, double maxx, double miny, double maxy){
		try {
			// coverage et infos associees
			GridCoverage2DReader reader;
			if(inputRaster.endsWith(".asc")){
				reader = new ArcGridReader(new File(inputRaster));
			}else if(inputRaster.endsWith(".tif")){
				reader = new GeoTiffReader(new File(inputRaster));
			}else{
				throw new IllegalArgumentException(inputRaster+" is not a recognize raster");
			}
			GridCoverage2D coverage2D = (GridCoverage2D) reader.read(null);
			reader.dispose(); // a� tester, ca va peut-etre bloquer la lecture des donnees
				
			int inWidth = (Integer) coverage2D.getProperty("image_width");
			int inHeight = (Integer) coverage2D.getProperty("image_height");
			double inMinX = coverage2D.getEnvelope().getMinimum(0);
			double inMinY = coverage2D.getEnvelope().getMinimum(1);
			double inMaxX = coverage2D.getEnvelope().getMaximum(0);
			double inMaxY = coverage2D.getEnvelope().getMaximum(1);
			float inCellSize = (float) ((java.awt.geom.AffineTransform) coverage2D.getGridGeometry().getGridToCRS2D()).getScaleX();
					
			int roiX = (int) ((minx-inMinX)/inCellSize);
			double outMinX = inMinX + roiX * inCellSize;
				
			int roiY = (int) ((inMaxY-maxy)/inCellSize);
			double outMaxY = inMaxY - roiY * inCellSize;
			
			int roiWidth = (int) ((maxx-outMinX)/inCellSize);
			int roiHeight = (int) ((outMaxY-miny)/inCellSize);
			
			System.out.println(roiX+" "+roiY+" "+roiWidth+" "+roiHeight);
			
			Rectangle roi = new Rectangle(roiX, roiY, roiWidth, roiHeight);
			float[] datas = new float[roiWidth * roiHeight];
			datas = coverage2D.getRenderedImage().getData(roi).getSamples(roi.x, roi.y, roi.width, roi.height, 0, datas);
					
			
			if(outputRaster.endsWith("tif")){
				CoverageManager.writeGeotiff(new File(outputRaster), datas, roiWidth, roiHeight, outMinX, maxx, miny, outMaxY);
			}else if(outputRaster.endsWith(".asc")){
				CoverageManager.writeAsciiGrid("F://dreal/ophelie/cgtv_baie_lancieux.asc", datas, roiWidth, roiHeight, outMinX, miny, inCellSize, Raster.getNoDataValue());
			}else{
				throw new IllegalArgumentException(outputRaster+" is not a recognize raster");
			}
			
					
		} catch (DataSourceException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * public static void split(GridCoverage2D coverage, short splitWidth, short
	 * splitHeight, short overlay){
	 * 
	 * Rectangle roi = new Rectangle(roiX, roiY, roiWidth, roiHeight); float[]
	 * inDatas = new float[roiWidth*roiHeight]; System.out.println(
	 * "r�cup�ration des donn�es"); inDatas =
	 * coverage.getRenderedImage().getData(roi).getSamples(roi.x, roi.y,
	 * roi.width, roi.height, 0, inDatas); }
	 */
}
