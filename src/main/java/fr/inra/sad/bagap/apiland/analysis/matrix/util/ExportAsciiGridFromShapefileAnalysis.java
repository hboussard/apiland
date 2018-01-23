package fr.inra.sad.bagap.apiland.analysis.matrix.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.sql.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.geotools.data.shapefile.dbf.DbaseFileHeader;
import org.geotools.data.shapefile.dbf.DbaseFileReader;
import org.geotools.data.shapefile.files.ShpFiles;
import org.geotools.data.shapefile.shp.ShapeType;
import org.geotools.data.shapefile.shp.ShapefileException;
import org.geotools.data.shapefile.shp.ShapefileHeader;
import org.geotools.data.shapefile.shp.ShapefileReader;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygonal;
import com.vividsolutions.jts.geom.Puntal;
import com.vividsolutions.jts.geom.prep.PreparedGeometry;
import com.vividsolutions.jts.geom.prep.PreparedPoint;
import com.vividsolutions.jts.geom.prep.PreparedPolygon;
import com.vividsolutions.jts.index.strtree.STRtree;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

import fr.inra.sad.bagap.apiland.analysis.Analysis;
import fr.inra.sad.bagap.apiland.core.element.manager.DynamicLayerFactory;
import fr.inra.sad.bagap.apiland.core.element.manager.Tool;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;

public class ExportAsciiGridFromShapefileAnalysis extends Analysis {

	private String ascii;
	
	private String shape;
	
	private String attribute;
	
	private double cellsize;
	
	private double minX, maxX, minY, maxY;
	
	private Map<String, String> map;
	
	public ExportAsciiGridFromShapefileAnalysis(String ascii, String shape, String attribute, double cellsize, Map<String, String> map){
		this.ascii = ascii;
		this.shape = shape;
		this.attribute = attribute;
		this.cellsize = cellsize;
		double[] envelope = getEnvelopeFromShapefile(getShape(shape));
		this.minX = envelope[0];
		this.maxX = envelope[1];
		this.minY = envelope[2];
		this.maxY = envelope[3];
		this.map = map;
	}
	
	public ExportAsciiGridFromShapefileAnalysis(String ascii, String shape, String attribute, double cellsize,
			double minX, double maxX, double minY, double maxY, Map<String, String> map){
		this.ascii = ascii;
		this.shape = shape;
		this.attribute = attribute;
		this.cellsize = cellsize;
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
		this.map = map;
	}
	
	@Override
	protected void doInit() {
		// do nothing
	}

	@Override
	protected void doRun() {
		ShpFiles sf = getShape(shape);
		int nb = getNumGeometries(sf);
		if(nb < 100){
			runFewGeometries();
		}else{
			runLotGeometries();
		}
	}
	
	protected void runFewGeometries() {
		// declarations
		ShpFiles sf = getShape(shape);
		int pos = getAttributePosition(sf, attribute);
		ShapeType type = getShapeType(sf);
		int ncols = new Double((Math.floor((maxX - minX) / cellsize)) + 1).intValue();
		int nrows = new Double((Math.floor((maxY - minY) / cellsize)) + 1).intValue();
			
		Set<PreparedGeometry> geos = getPreparedGeometries(sf, pos, map);
		
		double x, y;
		PreparedGeometry current = null;
		
		double yorigin;
		if (maxY - minY % cellsize == 0) {
			yorigin = minY + Math.floor((maxY - minY) / cellsize) * cellsize;
		} else {
			yorigin = minY + (Math.floor((maxY - minY) / cellsize) + 1)	* cellsize;
		}
				
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(ascii));
			bw.write("ncols " + ncols); bw.newLine();
			bw.write("nrows " + nrows);	bw.newLine();
			bw.write("xllcorner " + minX); bw.newLine();
			bw.write("yllcorner " + minY); bw.newLine();
			bw.write("cellsize " + cellsize); bw.newLine();
			bw.write("NODATA_value " + Raster.getNoDataValue()); bw.newLine();
			
			GeometryFactory gf = new GeometryFactory();
			Point p;
			
			for(int j=0; j<nrows; j++) {
				x = minX + (cellsize / 2.0);
				y = yorigin - (cellsize / 2.0) - j * cellsize;
				for(int i=0; i<ncols; i++) {
					x = minX + (cellsize / 2.0) + i * cellsize;
					
					p = gf.createPoint(new Coordinate(x, y));
					
					if(current != null && type.isPolygonType() && current.intersects(p)) {
						bw.write(current.getGeometry().getUserData()+" ");
					}else{
						boolean ok = false;
						for (PreparedGeometry g : geos) {
							if((type.isPolygonType() && g.intersects(p))
									|| (type.isLineType() && g.getGeometry().distance(p) <= cellsize)) {
								current = g;
								bw.write(current.getGeometry().getUserData()+" ");
								ok = true;
								break;
							}
						}
						if (!ok) {
							bw.write(Raster.getNoDataValue()+" ");
						}
					}
					//System.out.println(j+" "+i+" "+3);
					updateProgression(nrows * ncols);
				}
				bw.newLine();
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected void runLotGeometries() {
		// declarations
		ShpFiles sf = getShape(shape);
		int pos = getAttributePosition(sf, attribute);
		int nb = getNumGeometries(sf);
		ShapeType type = getShapeType(sf);
		int ncols = new Double((Math.floor((maxX - minX) / cellsize)) + 1).intValue();
		int nrows = new Double((Math.floor((maxY - minY) / cellsize)) + 1).intValue();
				
		double x, y;
		Geometry current = null;
		STRtree spatialIndex = null;
		int decoup = nb / 1000000 + 1;
		int d = -1;
		double yorigin;
		if (maxY - minY % cellsize == 0) {
			yorigin = minY + Math.floor((maxY - minY) / cellsize) * cellsize;
		} else {
			yorigin = minY + (Math.floor((maxY - minY) / cellsize) + 1)	* cellsize;
		}
				
		PreparedPoint pp = null;
		try {
			pp = new PreparedPoint((Puntal) new WKTReader().read("POINT (0 0)"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(ascii));
			bw.write("ncols " + ncols); bw.newLine();
			bw.write("nrows " + nrows);	bw.newLine();
			bw.write("xllcorner " + minX); bw.newLine();
			bw.write("yllcorner " + minY); bw.newLine();
			bw.write("cellsize " + cellsize); bw.newLine();
			bw.write("NODATA_value " + Raster.getNoDataValue()); bw.newLine();
			
			int delta = 1000;
			for(int j=0; j<nrows; j++) {
				//System.out.println("line " + j + "/" + nrows);
				//System.out.println(j+" 1");
				if (j * decoup / nrows != d) {
					d = j * decoup / nrows;
					spatialIndex = getSpatialIndex(sf, pos, map,
							new Envelope(minX - delta, maxX + delta, 
									minY - delta + (decoup - d - 1) * ((maxY - minY) / decoup), 
									maxY + delta - d * (maxY - minY) / decoup));
				}		
				//System.out.println(j+" 2");
				x = minX + (cellsize / 2.0);
				y = yorigin - (cellsize / 2.0) - j * cellsize;
				pp.getGeometry().getCoordinate().y = y;	
				//System.out.println(j+" 3");
				for(int i=0; i<ncols; i++) {
					//System.out.println(j+" "+i+" "+1);
					x = minX + (cellsize / 2.0) + i * cellsize;
					pp.getGeometry().getCoordinate().x = x;
					pp.getGeometry().geometryChanged();
					//System.out.println(j+" "+i+" "+2);
					if(current != null && type.isPolygonType() && pp.intersects(current)) {
						//System.out.println("ici");
						bw.write(current.getUserData()+" ");
					}else{
						//System.out.println("là "+1);
						List<Geometry> geometries = spatialIndex.query(new Envelope(new Coordinate(x-1, y+1), new Coordinate(x+1, y-1)));
						//System.out.println("là "+2);
						boolean ok = false;
						for (Geometry g : geometries) {
							//System.out.println("là "+3);
							if((type.isPolygonType() && pp.intersects(g))
									|| (type.isLineType() && pp.getGeometry().distance(g) <= cellsize)) {
								//System.out.println("là "+4);
								current = g;
								bw.write(g.getUserData()+" ");
								ok = true;
								break;
							}
						}
						if (!ok) {
							bw.write(Raster.getNoDataValue()+" ");
						}
					}
					//System.out.println(j+" "+i+" "+3);
					updateProgression(nrows * ncols);
				}
				bw.newLine();
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doClose() {
		try {
			//String prj_input = DynamicLayerFactory.class.getResource("lambert93.prj").toString().replace("file:/", "");
			//Tool.copy(prj_input, ascii.replace(".asc", "")+".prj");
			Tool.copy(DynamicLayerFactory.class.getResourceAsStream("lambert93.prj"), ascii.replace(".asc", "")+".prj");
		} catch (IOException e) {
			e.printStackTrace();
		}
		ascii = null;
		shape = null;
		attribute = null;
		map = null;
	}
	
	private ShpFiles getShape(String shape){
		try {
			ShpFiles sf;
			if (shape.endsWith(".shp")) {
				sf = new ShpFiles(shape);
			} else {
				sf = new ShpFiles(shape + ".shp");
			}
			return sf;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		throw new IllegalArgumentException();
	}
	
	private int getNumGeometries(ShpFiles sf){
		try {
			ShapefileReader sfr = new ShapefileReader(sf, true,	false, new com.vividsolutions.jts.geom.GeometryFactory());
		
			GeometryCollection gc;
			Geometry g;
			int size = 0;
			while (sfr.hasNext()) {
				gc = (GeometryCollection) sfr.nextRecord().shape();
				if(gc != null){
					size += gc.getNumGeometries();
				}
			}
			sfr.close();
			return size;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ShapefileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		throw new IllegalArgumentException();
	}
	
	private ShapeType getShapeType(ShpFiles sf){
		try {
			ShapefileReader sfr = new ShapefileReader(sf, true, false, new com.vividsolutions.jts.geom.GeometryFactory());
			ShapeType type = sfr.getHeader().getShapeType();
			sfr.close();
			return type;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ShapefileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		throw new IllegalArgumentException();
	}

	private int getAttributePosition(ShpFiles sf, String attribute){
		try {
			DbaseFileReader dfr = new DbaseFileReader(sf, true, Charset.defaultCharset());
			DbaseFileHeader dfh = dfr.getHeader();
			for (int f=0; f<dfh.getNumFields(); f++) {
				if (dfh.getFieldName(f).equalsIgnoreCase(attribute)) {
					dfr.close();
					return f;
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ShapefileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		throw new IllegalArgumentException();
	}
	
	private double[] getEnvelopeFromShapefile(ShpFiles sf){
		try {
			ShapefileReader sfr = new ShapefileReader(sf, true, false, new com.vividsolutions.jts.geom.GeometryFactory());
			ShapefileHeader sfh = sfr.getHeader();
			
			double[] envelope = new double[4];
			envelope[0] = sfh.minX();
			envelope[1] = sfh.maxX();
			envelope[2] = sfh.minY();
			envelope[3] = sfh.maxY();
			sfr.close();
			
			return envelope;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ShapefileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		throw new IllegalArgumentException();
	}
	
	private Set<PreparedGeometry> getPreparedGeometries(ShpFiles sf, int pos, Map<String, String> map){
		try {
			ShapefileReader sfr = new ShapefileReader(sf, true,	false, new com.vividsolutions.jts.geom.GeometryFactory());
			DbaseFileReader dfr = new DbaseFileReader(sf, true,	Charset.defaultCharset());
			GeometryCollection gc;
			Geometry g;
			Object f;
			Set<PreparedGeometry> set = new HashSet<PreparedGeometry>();
			while (sfr.hasNext()) {
				dfr.read();
				gc = (GeometryCollection) sfr.nextRecord().shape();
				if(gc != null){
					for(int n=0; n<gc.getNumGeometries(); n++) {
						g = gc.getGeometryN(n);
						if(map == null){
							g.setUserData(dfr.readField(pos));
						}else{
							f = dfr.readField(pos);
							if(map.containsKey(f+"")){
								g.setUserData(map.get(f+""));
							}else{
								g.setUserData(f);
							}
						}
						set.add(new PreparedPolygon((Polygonal) g));
					}
				}
			}
			sfr.close();
			dfr.close();
			return set;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ShapefileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		throw new IllegalArgumentException();
	}
	
	private STRtree getSpatialIndex(ShpFiles sf, int pos, Map<String, String> map, Envelope env){
		try {
			ShapefileReader sfr = new ShapefileReader(sf, true,	false, new com.vividsolutions.jts.geom.GeometryFactory());
			DbaseFileReader dfr = new DbaseFileReader(sf, true,	Charset.defaultCharset());
			GeometryCollection gc;
			Geometry g;
			STRtree spatialIndex = new STRtree();
			Object f;
			while (sfr.hasNext()) {
				dfr.read();
				//Object[] o = dfr.readEntry();
				gc = (GeometryCollection) sfr.nextRecord().shape();
				/*System.out.println(sfr.nextRecord());
				System.out.println(gc);
				for(Object oo : o){
					System.out.println(oo);
				}*/
				if(gc != null){
					for(int n=0; n<gc.getNumGeometries(); n++) {
						g = gc.getGeometryN(n);
						if(g.getEnvelopeInternal().intersects(env)) {
							if(map == null){
								g.setUserData(dfr.readField(pos));
							}else{
								f = dfr.readField(pos);
								if(map.containsKey(f+"")){
									g.setUserData(map.get(f+""));
								}else{
									g.setUserData(f);
								}
							}
							spatialIndex.insert(g.getEnvelopeInternal(), g);
						}
					}
				}
			}
			sfr.close();
			dfr.close();
			spatialIndex.build();
			return spatialIndex;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ShapefileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		throw new IllegalArgumentException();
	}
	
	/**
	 *  rappel JTS
	 *  C (Character) -> String
	 *  N (Numeric)   -> Integer or Long or Double (depends on field's decimal count and fieldLength)
	 *  F (Floating)  -> Double
	 *  L (Logical)   -> Boolean
	 *  D (Date)      -> java.util.Date (Without time)
	 */
	private Class<?> getAttributeType(ShpFiles sf, String attribute){
		try {
			DbaseFileReader dfr = new DbaseFileReader(sf, true, Charset.defaultCharset());
			DbaseFileHeader dfh = dfr.getHeader();
			for(int f=0; f<dfh.getNumFields(); f++){
				if(dfh.getFieldName(f).equalsIgnoreCase(attribute)){
					switch(dfh.getFieldType(f)){
					case 'C' : return String.class;
					case 'F' : return Double.class;
					case 'L' : return Boolean.class;
					case 'D' : return Date.class;
					case 'N' : 
						if(dfh.getFieldDecimalCount(f) > 0){
							return Double.class;
						}
						if(dfh.getFieldLength(f) > 10){
							return Long.class;
						}
						return Integer.class;
					default : throw new IllegalArgumentException();
					}
				}
			}
			dfr.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ShapefileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		throw new IllegalArgumentException();
	}


}
