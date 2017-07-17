package fr.inra.sad_paysage.apiland.analysis.matrix.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.Polygonal;
import com.vividsolutions.jts.geom.Puntal;
import com.vividsolutions.jts.geom.prep.PreparedGeometry;
import com.vividsolutions.jts.geom.prep.PreparedLineString;
import com.vividsolutions.jts.geom.prep.PreparedPoint;
import com.vividsolutions.jts.geom.prep.PreparedPolygon;
import com.vividsolutions.jts.index.strtree.STRtree;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

import fr.inra.sad_paysage.apiland.core.element.DynamicElement;
import fr.inra.sad_paysage.apiland.core.element.DynamicFeature;
import fr.inra.sad_paysage.apiland.core.element.DynamicLayer;
import fr.inra.sad_paysage.apiland.core.element.type.DynamicElementType;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.Raster;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.matrix.JaiMatrixFactory;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.matrix.Matrix;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.matrix.MatrixManager;
import fr.inra.sad_paysage.apiland.core.time.Instant;
import fr.inra.sad_paysage.apiland.core.util.VisuImageJ;

public class AsciiGridManager {

	public static void visualize(String ascii){
		new VisuImageJ(ascii);
	}
	
	public static void inverse(String input, String output) {
		try {
			Matrix m = JaiMatrixFactory.get().createWithAsciiGrid(input);
			Matrix m2 = JaiMatrixFactory.get().create(m);
			for(int y=0; y<m.height(); y++){
				for(int x=0; x<m.width(); x++){
					m2.put(m.width()-x-1, m.height()-y-1, m.get(x, y));
				}
			}
			MatrixManager.exportAsciiGrid(m2, output);
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void clean(String inAscii, String outAscii) {

		File f = new File(inAscii);
		String tempAscii = f.getParent() + "/"
				+ f.getName().replace(".asc", "") + "_c.asc";

		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			BufferedWriter bw = new BufferedWriter(new FileWriter(tempAscii));

			List<AsciiGridCleaner> cleaners = new ArrayList<AsciiGridCleaner>();
			cleaners.add(new AddNoDataValueAsciiGridCleaner(bw));
			cleaners.add(new SpaceAsciiGridCleaner());
			cleaners.add(new DecimalCharAsciiGridCleaner());
			cleaners.add(new IntegerAsciiGridCleaner());

			String line;
			while ((line = br.readLine()) != null) {
				for (AsciiGridCleaner agc : cleaners) {
					line = agc.clean(line);
				}
				bw.write(line);
				bw.newLine();
			}

			br.close();
			bw.close();

			new File(outAscii).delete();
			new File(tempAscii).renameTo(new File(outAscii));
			System.out.println("netoyage du fichier ascii effectué !");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void searchAndReplace(String inAscii, String outAscii,
			int noData, Map<Integer, Number> changes) {

		File f = new File(inAscii);
		String tempAscii = f.getParent() + "/"
				+ f.getName().replace(".asc", "") + "_c.asc";

		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			BufferedWriter bw = new BufferedWriter(new FileWriter(tempAscii));

			List<AsciiGridCleaner> cleaners = new ArrayList<AsciiGridCleaner>();
			cleaners.add(new SpaceAsciiGridCleaner());
			cleaners.add(new DecimalCharAsciiGridCleaner());
			cleaners.add(new IntegerAsciiGridCleaner());
			cleaners.add(new NoDataValueAsciiGridCleaner(noData));
			cleaners.add(new SearchAndReplaceAsciiGridCleaner(changes));

			String line;
			while ((line = br.readLine()) != null) {
				for (AsciiGridCleaner agc : cleaners) {
					line = agc.clean(line);
				}
				bw.write(line);
				bw.newLine();
			}

			br.close();
			bw.close();

			new File(tempAscii).renameTo(new File(outAscii));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static <E extends DynamicElement> void exportAsciiGridFromDynamicLayer (
			String ascii, DynamicLayer<E> layer, Instant t, String attName,	double cellsize) {
		exportAsciiGridFromDynamicLayer(ascii, layer, t, attName, cellsize, layer.minX(), layer.maxX(), layer.maxY(), layer.minY());
	}

	public static <E extends DynamicElement> void exportAsciiGridFromDynamicLayer (
			String ascii, DynamicLayer<E> layer, Instant t, String attName,	double cellsize, 
			double minX, double maxX, double minY, double maxY) {
		try {
			Raster.setCellSize(cellsize);
			DynamicElementType type = layer.getType().getElementType();
			if (type.hasAttributeType(attName)) {

				if(maxY < minY){
					double temp = maxY;
					maxY = minY;
					minY = temp;
				}
				
				int ncols = new Double((Math.floor((maxX - minX) / cellsize)) + 1).intValue();
				int nrows = new Double((Math.floor((maxY - minY) / cellsize)) + 1).intValue();

				BufferedWriter bw = new BufferedWriter(new FileWriter(ascii));

				bw.write("ncols " + ncols);
				bw.newLine();
				bw.write("nrows " + nrows);
				bw.newLine();
				bw.write("xllcorner " + minX);
				bw.newLine();
				bw.write("yllcorner " + minY);
				bw.newLine();
				bw.write("cellsize " + cellsize);
				bw.newLine();
				bw.write("NODATA_value " + Raster.getNoDataValue());
				bw.newLine();

				boolean ok;
				Envelope env;
				PreparedPoint p = null;
				double x;
				double y;
				List<DynamicFeature> l = null;
				DynamicFeature ftemp = null;
				double vtemp = Raster.getNoDataValue();
				int modulo = 1;
				WKTReader r = new WKTReader();
				STRtree sIndex = new STRtree();
				for (E f : layer) {
					sIndex.insert(f.getGeometry(t).get().getJTS().getEnvelopeInternal(), f);
				}
				sIndex.build();

				double yorigin;
				if (maxY - minY % cellsize == 0) {
					yorigin = minY + Math.floor((maxY - minY) / cellsize) * cellsize;
				} else {
					yorigin = minY + (Math.floor((maxY - minY) / cellsize) + 1) * cellsize;
				}

				//System.out.println(nrows+" "+ncols);
				
				for (int j = 0; j < nrows; j++) {
					System.out.println(j+" / "+nrows);
					x = minX + (cellsize / 2);
					y = yorigin - (cellsize / 2) - j * cellsize;
					if (j % modulo == 0) {
						env = new Envelope(new Coordinate(minX, y + 2 * cellsize), new Coordinate(maxX, y - 2 * cellsize));
						l = sIndex.query(env);
					}

					for (int i = 0; i < ncols; i++) {
						x = minX + (cellsize / 2) + i * cellsize;
						ok = false;
						try {
							p = new PreparedPoint((Puntal) r.read("POINT (" + x	+ " " + y + ")"));
						} catch (ParseException e) {
							e.printStackTrace();
						}
						if (ftemp != null) {
							if (p.intersects(ftemp.getGeometry(t).get().getJTS())) {
								bw.write(vtemp + " ");
								ok = true;
							}
						}
						if (!ok) {
							for (DynamicFeature f : l) {
								if (p.intersects(f.getGeometry(t).get().getJTS())) {
									// vtemp = map.get(f.getAttribute(attName).getValue(t).toString());
									vtemp = new Double(f.getAttribute(attName).getValue(t).toString());
									ftemp = f;
									bw.write(vtemp + " ");
									ok = true;
									break;
								}
							}
						}

						if (!ok) {
							bw.write(Raster.getNoDataValue() + " ");
						}
					}
					bw.newLine();
				}
				bw.close();
			} else {
				throw new IllegalArgumentException("attribute '" + attName	+ "' does not exist");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static <E extends DynamicElement> void exportAsciiGridFromDynamicLayer2 ( 
			String ascii, DynamicLayer<E> layer, Instant t, String attName,	double cellsize, 
			double minX, double maxX, double minY, double maxY) {
		try {
			Raster.setCellSize(cellsize);
			DynamicElementType type = layer.getType().getElementType();
			if (type.hasAttributeType(attName)) {

				int ncols = new Double((Math.floor((maxX - minX) / cellsize)) + 1).intValue();
				int nrows = new Double((Math.floor((maxY - minY) / cellsize)) + 1).intValue();

				BufferedWriter bw = new BufferedWriter(new FileWriter(ascii));

				bw.write("ncols " + ncols);
				bw.newLine();
				bw.write("nrows " + nrows);
				bw.newLine();
				bw.write("xllcorner " + minX);
				bw.newLine();
				bw.write("yllcorner " + minY);
				bw.newLine();
				bw.write("cellsize " + cellsize);
				bw.newLine();
				bw.write("NODATA_value " + Raster.getNoDataValue());
				bw.newLine();

				Map<String, Set<Polygon>> values = new TreeMap<String, Set<Polygon>>();
				for (E f : layer) {
					Geometry g = f.getGeometry(t).get().getJTS();
					String v = f.getAttribute(attName).getValue(t).toString();
					if(!values.containsKey(v)){
						values.put(v, new HashSet<Polygon>());
					}
					if(g instanceof Polygon){
						values.get(v).add((Polygon) f.getGeometry(t).get().getJTS());
					}else if(g instanceof MultiPolygon) {
						for(int i=0; i<g.getNumGeometries(); i++){
							values.get(v).add((Polygon) g.getGeometryN(i));
						}
					}else{
						throw new IllegalArgumentException(f.getGeometry(t).get().getJTS()+"");
					}
				}
				Map<String, PreparedPolygon> features = new TreeMap<String, PreparedPolygon>();
				for(Entry<String, Set<Polygon>> e : values.entrySet()){
					Polygon[] pp = new Polygon[e.getValue().size()];
					int index = 0;
					for(Polygon p : e.getValue()){
						pp[index++] = p;
					}
					features.put(e.getKey(), new PreparedPolygon(new MultiPolygon(pp, new GeometryFactory())));
				}

				double yorigin;
				if (maxY - minY % cellsize == 0) {
					yorigin = minY + Math.floor((maxY - minY) / cellsize) * cellsize;
				} else {
					yorigin = minY + (Math.floor((maxY - minY) / cellsize) + 1) * cellsize;
				}
				Point p = null;
				double x;
				double y;
				WKTReader r = new WKTReader();
				boolean ok;
				for (int j = 0; j < nrows; j++) {
					System.out.println(j+" / "+nrows);
					x = minX + (cellsize / 2);
					y = yorigin - (cellsize / 2) - j * cellsize;

					for (int i = 0; i < ncols; i++) {
						x = minX + (cellsize / 2) + i * cellsize;
						ok = false;
						try {
							p = (Point) r.read("POINT (" + x	+ " " + y + ")");
						} catch (ParseException e) {
							e.printStackTrace();
						}
						for(Entry<String, PreparedPolygon> pp : features.entrySet()){
							if(pp.getValue().intersects(p)){
								bw.write(pp.getKey()+" ");
								ok = true;
							}
						}		

						if (!ok) {
							bw.write(Raster.getNoDataValue() + " ");
						}
					}
					bw.newLine();
				}
				bw.close();
			} else {
				throw new IllegalArgumentException("attribute '" + attName	+ "' does not exist");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public static <E extends DynamicElement> void exportAsciiGridFromDynamicLayerWithSingleValue (
			String ascii, DynamicLayer<E> layer, Instant t, String value,	double cellsize) {
		exportAsciiGridFromDynamicLayerWithSingleValue(ascii, layer, t, value, cellsize, layer.minX(), layer.maxX(), layer.maxY(), layer.minY());
	}
	
	public static <E extends DynamicElement> void exportAsciiGridFromDynamicLayerWithSingleValue (
			String ascii, DynamicLayer<E> layer, Instant t, String value, double cellsize, 
			double minX, double maxX, double minY, double maxY) {
		try {
			Raster.setCellSize(cellsize);
			
			int ncols = new Double((Math.floor((maxX - minX) / cellsize)) + 1).intValue();
			int nrows = new Double((Math.floor((maxY - minY) / cellsize)) + 1).intValue();

			BufferedWriter bw = new BufferedWriter(new FileWriter(ascii));

			bw.write("ncols " + ncols);
			bw.newLine();
			bw.write("nrows " + nrows);
			bw.newLine();
			bw.write("xllcorner " + minX);
			bw.newLine();
			bw.write("yllcorner " + minY);
			bw.newLine();
			bw.write("cellsize " + cellsize);
			bw.newLine();
			bw.write("NODATA_value " + Raster.getNoDataValue());
			bw.newLine();
			
			//PreparedPolygon ppoly = new PreparedPolygon((MultiPolygon) layer.iterator().next().getGeometry(t).get().getJTS());
			/*
			Polygon[] pp = new Polygon[layer.size()];
			Polygon g;
			int index = 0;
			for(E e : layer){
				g = (Polygon) layer.iterator().next().getGeometry(t).get().getJTS();
				pp[index++] = g;				
			}
			PreparedPolygon ppoly = new PreparedPolygon(new MultiPolygon(pp, new GeometryFactory()));
			*/
			
		
			Set<Geometry> pp = new HashSet<Geometry>();
			Geometry g;
			for(E e : layer){
				//System.out.println(e.getId());
				g = e.getGeometry(t).get().getJTS();
				if(g instanceof GeometryCollection){
					for(int i=0; i<g.getNumGeometries(); i++){
						pp.add(g.getGeometryN(i));
					}
				}else{
					pp.add(g);
				}
				
			}
			
			//System.out.println(layer.size()+" "+pp.size());
			
			PreparedGeometry ppoly = null;
			if(pp.iterator().next() instanceof Polygon){
				ppoly = new PreparedPolygon(new MultiPolygon(pp.toArray(new Polygon[pp.size()]), new GeometryFactory()));
			}else if(pp.iterator().next() instanceof LineString){
				ppoly = new PreparedLineString(new MultiLineString(pp.toArray(new LineString[pp.size()]), new GeometryFactory()));
			}
			
			Point p = null;
			double x;
			double y;
			WKTReader r = new WKTReader();

			double yorigin;
			if (maxY - minY % cellsize == 0) {
				yorigin = minY + Math.floor((maxY - minY) / cellsize) * cellsize;
			} else {
				yorigin = minY + (Math.floor((maxY - minY) / cellsize) + 1) * cellsize;
			}

			for (int j = 0; j < nrows; j++) {
				//System.out.println(j+" / "+nrows);
				x = minX + (cellsize / 2);
				y = yorigin - (cellsize / 2) - j * cellsize;

				for (int i = 0; i < ncols; i++) {
					x = minX + (cellsize / 2) + i * cellsize;
					try {
						p = (Point) r.read("POINT (" + x	+ " " + y + ")");
					} catch (ParseException e) {
						e.printStackTrace();
					}
					if (ppoly.intersects(p)) {
						//System.out.println(p+" "+value);
						bw.write(value + " ");
					}else{
						bw.write(Raster.getNoDataValue() + " ");
					}
				}
				bw.newLine();
			}
			bw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public static <E extends DynamicElement> void exportAsciiGridFromDynamicLayerWithSingleFeature (
			String ascii, DynamicLayer<E> layer, Instant t, String value, double cellsize, 
			double minX, double maxX, double minY, double maxY) {
		try {
			Raster.setCellSize(cellsize);
			
			int ncols = new Double((Math.floor((maxX - minX) / cellsize)) + 1).intValue();
			int nrows = new Double((Math.floor((maxY - minY) / cellsize)) + 1).intValue();

			BufferedWriter bw = new BufferedWriter(new FileWriter(ascii));

			bw.write("ncols " + ncols);
			bw.newLine();
			bw.write("nrows " + nrows);
			bw.newLine();
			bw.write("xllcorner " + minX);
			bw.newLine();
			bw.write("yllcorner " + minY);
			bw.newLine();
			bw.write("cellsize " + cellsize);
			bw.newLine();
			bw.write("NODATA_value " + Raster.getNoDataValue());
			bw.newLine();

			PreparedPolygon ppoly = new PreparedPolygon((Polygonal) layer.iterator().next().getGeometry(t).get().getJTS());
			Point p = null;
			double x;
			double y;
			WKTReader r = new WKTReader();

			double yorigin;
			if (maxY - minY % cellsize == 0) {
				yorigin = minY + Math.floor((maxY - minY) / cellsize) * cellsize;
			} else {
				yorigin = minY + (Math.floor((maxY - minY) / cellsize) + 1) * cellsize;
			}

			for (int j = 0; j < nrows; j++) {
				System.out.println(j+" / "+nrows);
				x = minX + (cellsize / 2);
				y = yorigin - (cellsize / 2) - j * cellsize;

				for (int i = 0; i < ncols; i++) {
					x = minX + (cellsize / 2) + i * cellsize;
					try {
						p = (Point) r.read("POINT (" + x	+ " " + y + ")");
					} catch (ParseException e) {
						e.printStackTrace();
					}
					if (ppoly.intersects(p)) {
						bw.write(value + " ");
					}else{
						bw.write(Raster.getNoDataValue() + " ");
					}
				}
				bw.newLine();
			}
			bw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public static void exportAsciiGridFromShapefile(String ascii, String shape, String attribute, double cellsize, Map<String, String> map) {
		new ExportAsciiGridFromShapefileAnalysis(ascii, shape, attribute, cellsize, map).allRun();
	}
	
	public static void exportAsciiGridFromShapefile(String ascii, String shape, String attribute, double cellsize,
			double minX, double maxX, double minY, double maxY, Map<String, String> map) {
		new ExportAsciiGridFromShapefileAnalysis(ascii, shape, attribute, cellsize, minX, maxX, minY, maxY, map).allRun();
	}
	
}
