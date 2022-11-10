package fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.geotools.data.shapefile.dbf.DbaseFileHeader;
import org.geotools.data.shapefile.dbf.DbaseFileReader;
import org.geotools.data.shapefile.files.ShpFiles;
import org.geotools.data.shapefile.shp.ShapefileException;
import org.geotools.data.shapefile.shp.ShapefileReader;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.Puntal;
import org.locationtech.jts.geom.prep.PreparedPoint;
import org.locationtech.jts.index.strtree.STRtree;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

public class DirectExport {
	
	private static String s = "/home/sad20/temp/laurence/buffer.shp";
	private static String a = "/home/sad20/temp/laurence/buffer2.asc";
	
	public static void main(String[] args){
		shapeFile2Ascii(s, "test", a, 50);
	}
	
	/*
	private static String in = "/home/sad20/data/demo2.asc";
	
	private static String out = "/home/sad20/data/demo3.asc";
	
	public static void main(String[] args){
		addNoDataValueIfNeeded(in, out, -1);
	}
	*/
	/*
	private static String input = "/home/sad20/data/";
	
	private static String output = "/home/sad20/data/test/";
	
	public static void main(String[] args){
		addNoDataValueInAFolder(input, output, -1);
	}
	*/
	
	public static void shapeFile2Ascii(String shape, String attName, String ascii, double cellsize){
		
		try{
			ShpFiles sf;
			if(shape.endsWith(".shp")){
				sf = new ShpFiles(shape);
			}else{
				sf = new ShpFiles(shape + ".shp");
			}
		
			ShapefileReader sfr = new ShapefileReader(sf, true, true, new org.locationtech.jts.geom.GeometryFactory());
			DbaseFileReader dfr = new DbaseFileReader(sf, true, Charset.defaultCharset());
			
			DbaseFileHeader dfh = dfr.getHeader();
			int attPos = -1;
			for (int i=0; i<dfh.getNumFields(); i++) {
				if(dfh.getFieldName(i).equalsIgnoreCase(attName)){
					attPos = i;
					break;
				}
			}
			
			double minX = 999999999;
			double minY = 999999999;
			double maxX = -999999999;
			double maxY = -999999999;
			
			MultiPolygon mp;
			Set<Polygon> polys = new HashSet<Polygon>();
			Object[] entry = new Object[12];
			long value;
			Polygon poly;
			while (sfr.hasNext()) {
				mp = (MultiPolygon)sfr.nextRecord().shape();
				minX = Math.min(minX, mp.getEnvelopeInternal().getMinX());
				minY = Math.min(minY, mp.getEnvelopeInternal().getMinY());
				maxX = Math.max(maxX, mp.getEnvelopeInternal().getMaxX());
				maxY = Math.max(maxY, mp.getEnvelopeInternal().getMaxY());
				entry = dfr.readEntry(entry);
				if(entry[attPos] instanceof Long){
					value = (Long)entry[attPos];
				}else{
					value = (Integer)entry[attPos];
				}
				
				for(int i=0; i<mp.getNumGeometries(); i++){
					poly = (Polygon)mp.getGeometryN(i);
					poly.setUserData(value);
					polys.add(poly);
				}
			}
			
			int ncols = new Double((maxX - minX)/cellsize).intValue();
			int nrows = new Double((maxY - minY)/cellsize).intValue();
			
			boolean ok;
			Envelope env;
			PreparedPoint p = null;
			double x;
			double y;
			List<Polygon> l = null;
			Polygon ftemp = null;
			long vtemp = -1;
			//int modulo = nrows/10;
			int modulo = 1;
			WKTReader r = new WKTReader();
			STRtree sIndex = new STRtree();
			
			try{
				BufferedWriter bw = new BufferedWriter(new FileWriter(ascii));
				bw.write("ncols "+ncols);
				bw.newLine();
				bw.write("nrows "+nrows);
				bw.newLine();
				bw.write("xllcorner "+minX);
				bw.newLine();
				bw.write("yllcorner "+minY);
				bw.newLine();
				bw.write("cellsize "+cellsize);
				bw.newLine();
				bw.write("NODATA_value -1");
				bw.newLine();
				
				for(Polygon po : polys){
					sIndex.insert(po.getEnvelopeInternal(), po);
				}
				sIndex.build();
				
				int index;
				
				for(int j=0; j<nrows; j++){
					
					x = minX;
					y = maxY - j*cellsize;
					if(j%modulo == 0){
						env = new Envelope(new Coordinate(x,y),new Coordinate(maxX,y - modulo*cellsize));
						l = sIndex.query(env);
					}
					
					index = 0;
					
					for(int i=0; i<ncols; i++){
						
						x = minX + i*cellsize;
						ok = false;
						try {
							p = new PreparedPoint((Puntal)r.read("POINT ("+x+" "+y+")"));
						} catch (ParseException e) {
							e.printStackTrace();
						}
						if(ftemp != null){
							if(p.intersects(ftemp)){
								bw.write(vtemp+" ");
								ok = true;
							}
						}
						if(!ok){
							for(Polygon pol : l){
								if(p.intersects(pol)){
									vtemp = (Long)pol.getUserData();
									ftemp = pol;
									bw.write(vtemp+" ");
									ok = true;
									break;
								}
							}
						}
						if(!ok){
							bw.write("-1 ");
						}
						index++;
					}
					bw.newLine();
					
					System.out.println(j+" "+index);
				}	
				
				bw.close();
			}catch (IOException ex) {
				ex.printStackTrace();
			}
			
			sfr.close();
			dfr.close();
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (ShapefileException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public static void addNoDataValueInAFolder(String input, String output, int noDataValue){
		File foutput = new File(output);
		if(!foutput.exists()){
			foutput.mkdir();
		}
		File finput = new File(input);
		if(finput.isDirectory()){
			String in, out;
			for(File f : finput.listFiles()){
				if(f.isFile() && f.getName().endsWith(".asc")){
					addNoDataValueIfNeeded(input+f.getName(), output+f.getName(), noDataValue);
				}
			}
		}
	}
	
	public static void addNoDataValueIfNeeded(String in, String out, int noDataValue){
		try{
			BufferedReader br = new BufferedReader(new FileReader(in));
			BufferedWriter bw = new BufferedWriter(new FileWriter(out));
			
			bw.write(br.readLine()); bw.newLine();
			bw.write(br.readLine()); bw.newLine();
			bw.write(br.readLine()); bw.newLine();
			bw.write(br.readLine()); bw.newLine();
			bw.write(br.readLine()); bw.newLine();
			
			String nodata = br.readLine();
			if(!nodata.startsWith("NODATA_value")){
				bw.write("NODATA_value "+noDataValue);
				bw.newLine();
			}
			
			String line = nodata;
			
			while(line != null){
				bw.write(line);
				bw.newLine();
				line = br.readLine();
			}
			
			br.close();
			bw.close();
		}catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	/*
	 * public static void script(){
		Instant t = Instant.get(1,1,2000);
		DynamicLayer<DynamicElement> layer = DynamicLayerFactory.initWithShape(shape, t);
		
		layer.getType().display();
		minX = 999999999;
		minY = 999999999;
		maxX = -999999999;
		maxY = -999999999;
		
		int index = 0;
		for(DynamicElement el : layer){
			index++;
			minX = Math.min(minX,  el.minX());
			minY = Math.min(minY,  el.minY());
			maxX = Math.max(maxX,  el.maxX());
			maxY = Math.max(maxY,  el.maxY());
		}
		System.out.println("nombre d'éléments : "+index);
		System.out.println(minX+" "+maxX+" "+minY+" "+maxY);
		
		exportAscii(layer, "/home/sad20/agents/camille/fichier_couche/Bzh_PrP_35Nord_raster.asc", t, "os", 100);
	}
	 */
	
	/*
	public static <E extends DynamicElement> void exportAscii(DynamicLayer<E> layer, String file, Instant t, String attName, double cellsize) {
		Raster.setCellSize(cellsize);
		
		int ncols = new Double((maxX - minX)/cellsize).intValue();
		int nrows = new Double((maxY - minY)/cellsize).intValue();
				
		System.out.println(nrows+" "+ncols+" "+(nrows*ncols));
		
		boolean ok;
		Envelope env;
		PreparedPoint p = null;
		double x;
		double y;
		List<DynamicFeature> l = null;
		DynamicElement ftemp = null;
		double vtemp = Raster.getNoDataValue();
		//int modulo = nrows/10;
		int modulo = 1;
		WKTReader r = new WKTReader();
		STRtree sIndex = new STRtree();
		
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			bw.write("ncols "+ncols);
			bw.newLine();
			bw.write("nrows "+nrows);
			bw.newLine();
			bw.write("xllcorner "+minX);
			bw.newLine();
			bw.write("yllcorner "+minY);
			bw.newLine();
			bw.write("cellsize "+cellsize);
			bw.newLine();
			bw.write("NODATA_value "+Raster.getNoDataValue());
			bw.newLine();
		
			// gros polygones particuliers
			E p1 = null;
			E p2 = null; 
			
			double max = 0;
			for(E f : layer){
				sIndex.insert(f.getGeometry(t).get().getJTS().getEnvelopeInternal(), f);
				if(f.getId().equalsIgnoreCase("1748")){
					System.out.println("initialisation de 1748 ok");
					p1 = f;
				}
				max = Math.max(max, f.getArea(t));
				if(f.getId().equalsIgnoreCase("8797")){
					System.out.println("initialisation de 8797 ok");
					p2 = f;
					System.out.println(p2.getArea(t));
				}
				if(f.getArea(t)>50000000){
					System.out.println("sup "+f.getId());
				}
			}
			System.out.println(max+" surface");
			sIndex.build();
				
			int index;
			
			for(int j=0; j<nrows; j++){
				
				x = minX;
				y = maxY - j*cellsize;
				if(j%modulo == 0){
					env = new Envelope(new Coordinate(x,y),new Coordinate(maxX,y - modulo*cellsize));
					l = sIndex.query(env);
				}
				
				index = 0;
				
				for(int i=0; i<ncols; i++){
					
					x = minX + i*cellsize;
					ok = false;
					try {
						p = new PreparedPoint((Puntal)r.read("POINT ("+x+" "+y+")"));
					} catch (ParseException e) {
						e.printStackTrace();
					}
					if(ftemp != null){
						if(p.intersects(ftemp.getGeometry(t).get().getJTS())){
							//mt.put(i, j, vtemp);
							bw.write(vtemp+" ");
							ok = true;
						}
					}
					if(!ok){
						if(p.intersects(p1.getGeometry(t).get().getJTS())){
							vtemp = new Double(p1.getAttribute(attName).getValue(t).toString());
							ftemp = p1;
							//mt.put(i, j, vtemp);
							bw.write(vtemp+" ");
							ok = true;
						}else if(p.intersects(p2.getGeometry(t).get().getJTS())){
							System.out.println(p);
							vtemp = new Double(p2.getAttribute(attName).getValue(t).toString());
							ftemp = p2;
							//mt.put(i, j, vtemp);
							bw.write(vtemp+" ");
							ok = true;
						}
					}
					if(!ok){
						for(DynamicFeature f : l){
						//for(DynamicElement f : layer){
							if(p.intersects(f.getGeometry(t).get().getJTS())){
								vtemp = new Double(f.getAttribute(attName).getValue(t).toString());
								ftemp = f;
								//mt.put(i, j, vtemp);
								bw.write(vtemp+" ");
								ok = true;
								break;
							}
						}
					}
					
				
					if(!ok){
						//mt.put(i, j, Raster.getNoDataValue());
						bw.write(Raster.getNoDataValue()+" ");
					}
					index++;
				}
				System.out.println(j+" "+index);
				bw.newLine();
			}	
			bw.close();
		}catch(IOException e){
			e.printStackTrace(); 
		}	
	}*/
	
}
