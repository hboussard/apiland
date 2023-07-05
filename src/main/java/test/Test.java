package test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

import javax.media.jai.PlanarImage;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.data.shapefile.dbf.DbaseFileHeader;
import org.geotools.data.shapefile.dbf.DbaseFileReader;
import org.geotools.data.shapefile.files.ShpFiles;
import org.geotools.data.shapefile.shp.ShapefileException;
import org.geotools.data.shapefile.shp.ShapefileReader;
import org.geotools.image.util.ImageUtilities;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;

import fr.inra.sad.bagap.apiland.analysis.matrix.util.ExportAsciiGridFromShapefileAnalysis;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.core.util.VisuImageJ;
import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.CoverageManager;
import fr.inrae.act.bagap.raster.EnteteRaster;
import fr.inrae.act.bagap.raster.RasterPolygon;
import fr.inrae.act.bagap.raster.converter.ShapeFile2CoverageConverter;

public class Test {

	public static void main(String[] args) {
		/*
		Coverage cov = CoverageManager.getCoverage("G:/chloe/winterschool/data/start/raster2007.asc");
		EnteteRaster entete = cov.getEntete();
		float[] data = cov.getDatas();
		cov.dispose();
		
		CoverageManager.writeGeotiff("G:/chloe/winterschool/data/start/raster2007.tif", data, entete);
		*/
		//new VisuImageJ("G:/chloe/winterschool/data/start/raster2007.tif");
		
		//new VisuImageJ("H:/temp/test.za.tif");
		
		/*
		Coverage cov = CoverageManager.getCoverage("H:/temp/claire/data/bocage_5m/ZA_1952_bocage_5m.asc");
		EnteteRaster entete = cov.getEntete();
		float[] data = cov.getDatas();
		cov.dispose();
		
		CoverageManager.writeGeotiff("H:/temp/claire/data/bocage_5m/ZA_1952_bocage_5m.tif", data, entete);
		new VisuImageJ("H:/temp/claire/data/bocage_5m/ZA_1952_bocage_5m.tif");
		*/
		
		
		String shapeInput = "C:/Hugues/data/data_ZA/PF_OS_L93/PF_2018/OCS_PUMA_ZA_2018.shp";
		String attribute = "OS_2018";
		float cellSize = 5;
		int noDataValue = Raster.getNoDataValue();
		
		//String asciiOutput1 = "H:/temp/test/ascii1.asc";
		//rasterize1(asciiOutput1, shapeInput, attribute, cellSize);
		
		//String asciiOutput2 = "H:/temp/test/ascii2.asc";
		//rasterize2(asciiOutput2, shapeInput, attribute, cellSize);
	
		//String asciiOutput3 = "H:/temp/test/ascii3.tif";
		//ShapeFile2CoverageConverter.rasterize(asciiOutput3, shapeInput, attribute, cellSize, noDataValue);
		//ShapeFile2CoverageConverter.getCoverage(shapeInput, attribute, cellSize, noDataValue);
		
		String asciiOutput4 = "H:/temp/test/ascii4.tif";
		double minx = 361700;
		double maxx = 362700;
		double miny = 6833000;
		double maxy = 6834000;
		//ShapeFile2CoverageConverter.rasterize(asciiOutput4, shapeInput, attribute, cellSize, noDataValue, minx, maxx, miny, maxy);
		//ShapeFile2CoverageConverter.getCoverage(shapeInput, attribute, cellSize, noDataValue, minx, maxx, miny, maxy);
		
	}
	/*
	private static void rasterize1(String asciiOutput, String shapeInput, String attribute, double cellSize){
		
		long begin = System.currentTimeMillis();
		
		ExportAsciiGridFromShapefileAnalysis a = new ExportAsciiGridFromShapefileAnalysis(asciiOutput, shapeInput, attribute, cellSize, null);
		a.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	
	private static void rasterize2(String asciiOutput, String shapeInput, String attribute, double cellSize){
		
		long begin = System.currentTimeMillis();
		
		EnteteRaster entete = getEntete(shapeInput);
		
		System.out.println(entete.minx()+" "+entete.maxx()+" "+entete.miny()+" "+entete.maxy());
		
		float[] data = getDataFromShapefile(shapeInput, entete, attribute, -1);
		CoverageManager.writeAsciiGrid(asciiOutput, data, entete);
				
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
		
	}
	
	private static EnteteRaster getEntete(String zone) {
		try{
			
			ShpFiles sf = new ShpFiles(zone);
			ShapefileReader sfr = new ShapefileReader(sf, true, false, new GeometryFactory());
			
			Geometry the_geom;
			
			double minx = Double.MAX_VALUE;
			double maxx = Double.MIN_VALUE;
			double miny = Double.MAX_VALUE;
			double maxy = Double.MIN_VALUE;
			
			while(sfr.hasNext()){
				
				the_geom = (Geometry) sfr.nextRecord().shape();
				
				minx = Math.min(minx, the_geom.getEnvelopeInternal().getMinX());
				maxx = Math.max(maxx, the_geom.getEnvelopeInternal().getMaxX());
				miny = Math.min(miny, the_geom.getEnvelopeInternal().getMinY());
				maxy = Math.max(maxy, the_geom.getEnvelopeInternal().getMaxY());
			}
			
			System.out.println(minx+" "+maxx+" "+miny+" "+maxy);
			
			sfr.close();
			sf.dispose();
			
			return EnteteRaster.getEntete(new Envelope(minx, maxx, miny, maxy), 5, -1);
			
		} catch (ShapefileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
		return null;
	}
	
	private static float[] getDataFromShapefile(String inputShape, EnteteRaster entete, String attribute, int noDataValue){
		try{
			
			ShpFiles sf = new ShpFiles(inputShape);
			ShapefileReader sfr = new ShapefileReader(sf, true, false, new GeometryFactory());
			DbaseFileReader dfr = new DbaseFileReader(sf, true,	Charset.defaultCharset());
			DbaseFileHeader dfh = dfr.getHeader();
			int pos = -1;
			for (int f=0; f<dfh.getNumFields(); f++) {
				if (dfh.getFieldName(f).equalsIgnoreCase(attribute)) {
					pos = f;
				}
			}
			
			float[] datas = new float[entete.width()*entete.height()];
			Arrays.fill(datas, noDataValue);
			
			Geometry the_geom;
			Polygon the_poly;
			RasterPolygon rp;
			int indrp;
			int xdelta, ydelta, xrp, yrp;
			long value;
			while(sfr.hasNext()){
				dfr.read();
				value = (long) dfr.readField(pos);
				
				the_geom = (Geometry) sfr.nextRecord().shape();
				
				if(the_geom instanceof Polygon){
					the_poly = (Polygon) the_geom;
				}else if(the_geom instanceof MultiPolygon){
					the_poly = (Polygon) ((MultiPolygon) the_geom).getGeometryN(0);
				}else{
					the_poly = null;
					//System.out.println(dfr.readEntry()[0]+" "+((MultiPolygon) the_geom).getNumGeometries());
					throw new IllegalArgumentException("probleme geometrique");
				}
				
				rp = RasterPolygon.getRasterPolygon(the_poly, entete.minx(), entete.maxy(), entete.cellsize());
				indrp = 0;
				xdelta = rp.getDeltaI();
				ydelta = rp.getDeltaJ();
				for(double v : rp.getDatas()){
					if(v == 1){
						xrp = indrp % rp.getWidth();
						yrp = indrp / rp.getWidth();
						if(xdelta+xrp >= 0 && xdelta+xrp < entete.width() && ydelta+yrp >= 0 && ydelta+yrp < entete.height()){
							datas[(ydelta+yrp)*entete.width() + (xdelta+xrp)] = value;
						}
					}
					indrp++;
				}			
			}
			
			sfr.close();
			dfr.close();
			sf.dispose();
			
			return datas;
			
		} catch (ShapefileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
		
		return null;
	}
	
	private static boolean intersectEnvelope(Envelope envelope, double minx, double maxx, double miny, double maxy ){
		return ((envelope.getMinX() >= minx && envelope.getMinX() <= maxx) || (envelope.getMinX() <= minx && envelope.getMaxX() >= maxx) || (envelope.getMaxX() >= minx && envelope.getMaxX() <= maxx))
				&& ((envelope.getMinY() >= miny && envelope.getMinY() <= maxy) || (envelope.getMinY() <= miny && envelope.getMaxY() >= maxy) || (envelope.getMaxY() >= miny && envelope.getMaxY() <= maxy));
	}

*/
}
