/*Copyright 2010 by INRA SAD-Paysage (Rennes)

Author: Hugues BOUSSARD 
email : hugues.boussard@rennes.inra.fr

This library is a JAVA toolbox made to create and manage dynamic landscapes.

This software is governed by the CeCILL-C license under French law and
abiding by the rules of distribution of free software.  You can  use,
modify and/ or redistribute the software under the terms of the CeCILL-C
license as circulated by CEA, CNRS and INRIA at the following URL
"http://www.cecill.info".

As a counterpart to the access to the source code and rights to copy,
modify and redistribute granted by the license, users are provided only
with a limited warranty  and the software's author,  the holder of the
economic rights,  and the successive licensors  have only  limited
liability.

In this respect, the user's attention is drawn to the risks associated
with loading,  using,  modifying and/or developing or reproducing the
software by the user in light of its specific status of free software,
that may mean  that it is complicated to manipulate, and that also
therefore means  that it is reserved for developers and experienced
professionals having in-depth computer knowledge. Users are therefore
encouraged to load and test the software's suitability as regards their
requirements in conditions enabling the security of their systems and/or
data to be ensured and,  more generally, to use and operate it in the
same conditions as regards security.

The fact that you are presently reading this means that you have had
knowledge of the CeCILL-C license and that you accept its terms.
*/
package fr.inra.sad_paysage.apiland.core.space.impl.raster.matrix;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.media.jai.PlanarImage;

/*
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.data.DataSourceException;
import org.geotools.gce.arcgrid.ArcGridReader;
import org.geotools.referencing.operation.transform.AffineTransform2D;
*/
import com.csvreader.CsvReader;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.Puntal;
import com.vividsolutions.jts.geom.prep.PreparedPoint;
import com.vividsolutions.jts.geom.prep.PreparedPolygon;
import com.vividsolutions.jts.index.strtree.STRtree;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

import fr.inra.sad_paysage.apiland.core.element.DynamicElement;
import fr.inra.sad_paysage.apiland.core.element.DynamicFeature;
import fr.inra.sad_paysage.apiland.core.element.DynamicLayer;
import fr.inra.sad_paysage.apiland.core.element.type.DynamicElementType;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.Raster;
import fr.inra.sad_paysage.apiland.core.time.Instant;

public abstract class MatrixFactory {
	
	public static MatrixFactory get(MatrixType type){
		if(type == MatrixType.ARRAY){
			return ArrayMatrixFactory.get();
		}else if(type == MatrixType.SPARSE){
			return SparseMatrixFactory.get();
		}else if(type == MatrixType.YALE_SPARSE){
			return YaleSparseMatrixFactory.get();
		}else if(type == MatrixType.JAI){
			return  JaiMatrixFactory.get();
		}
		return ArrayMatrixFactory.get();
		//throw new IllegalArgumentException();
	}
	
	public abstract Matrix create(Matrix mRef);
	
	public abstract Matrix create(Matrix mRef, int divisor);
	
	public abstract Matrix create(int width, int height, double cellsize, 
			double minX, double maxX, double minY, double maxY, int noData);
	
	public abstract Matrix create(int width, int height, double cellsize, 
			double minX, double maxX, double minY, double maxY, int noData, PlanarImage ref);
	
	public Matrix init(String in){
		Matrix m = null;
		try{
			CsvReader cr = new CsvReader(in);
			cr.setDelimiter(' ');
			
			cr.readRecord();
			int width = new Integer(cr.get(1));
			cr.readRecord();
			int height = new Integer(cr.get(1));
			cr.readRecord();
			double minX = new Double(cr.get(1));
			cr.readRecord();
			double minY = new Double(cr.get(1));
			cr.readRecord();
			double cellsize = new Double(cr.get(1));
			cr.readRecord();
			int noData = new Integer(cr.get(1));
			double maxX = minX + width * cellsize;
			double maxY = minY + height * cellsize;
			
			m = create(width,height,cellsize,minX,maxX,minY,maxY,noData);
			
			int j = 0;
			while(cr.readRecord()){
				for(int i=0; i<width; i++){
					m.put(i, j, new Double(cr.get(i)));
				}
				j++;
			}
			m.setFile(in);
			cr.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return m;
	}
	
	public Matrix init(Matrix mRef, Raster r, double value){
		Matrix m = create(mRef);
		m.init(m.noDataValue());
		for(Pixel p : r){
			m.put(p, value);
		}
		return m;
	}
	
	public <E extends DynamicElement> Matrix initWithCentralPoint(DynamicLayer<E> layer, Instant t, String attName, double cellsize, double buffer) {
		return initWithCentralPoint(layer, layer.getType().getElementType(), t, attName, cellsize, buffer);
	}
	
	public <E extends DynamicElement> Matrix initWithCentralPoint(DynamicLayer<E> layer, DynamicElementType type, Instant t, String attName, double cellsize, double buffer) {
		Raster.setCellSize(cellsize);
		if(type.hasAttributeType(attName)){
			
			int ncols = new Double((Math.floor((layer.maxX() - layer.minX() + 2*buffer)/cellsize)) + 1).intValue();
			int nrows = new Double((Math.floor((layer.maxY() - layer.minY() + 2*buffer)/cellsize)) + 1).intValue();
			
			Matrix mt = create(ncols,nrows,cellsize,layer.minX(), layer.maxX(), layer.minY(), layer.maxY(), Raster.getNoDataValue());	
			
			boolean ok;
			Envelope env;
			PreparedPoint p = null;
			double x;
			double y;
			List<DynamicFeature> l = null;
			DynamicFeature ftemp = null;
			double vtemp = Raster.getNoDataValue();
			//int modulo = nrows/100;
			int modulo = 1;
			WKTReader r = new WKTReader();
			STRtree sIndex = new STRtree();
			//Iterator<E> ite = (Iterator<E>)layer.activeDeepIterator(t);
			
			for(E f : layer){
				sIndex.insert(f.getGeometry(t).get().getJTS().getEnvelopeInternal(), f);
			}
			sIndex.build();
			
			double yorigin;
			if(layer.maxY() - layer.minY() % cellsize == 0){
				yorigin = layer.minY() + buffer + Math.floor((layer.maxY() - layer.minY())/cellsize)*cellsize;
			}else{
				yorigin = layer.minY() + buffer + (Math.floor((layer.maxY() - layer.minY())/cellsize) + 1)*cellsize;
			}
			
			for(int j=0; j<nrows; j++){
				x = layer.minX() - buffer + (cellsize/2);
				y = yorigin - (cellsize/2) - j*cellsize;
				if(j%modulo == 0){
					env = new Envelope(new Coordinate(layer.minX(), y + 2*cellsize), new Coordinate(layer.maxX(), y - 2*cellsize));
					l = sIndex.query(env);
				}
					
				for(int i=0; i<ncols; i++){
					x = layer.minX() - buffer + (cellsize/2) + i*cellsize;
					ok = false;
					try {
						p = new PreparedPoint((Puntal)r.read("POINT ("+x+" "+y+")"));
					} catch (ParseException e) {
						e.printStackTrace();
					}
					//System.out.println(p);
					if(ftemp != null){
						if(p.intersects(ftemp.getGeometry(t).get().getJTS())){
							//mt.put(i, j, new Integer((Integer)f.getAttribute(attName).getValue(t)).doubleValue());
							//mt.put(i, j, new Long((Long)f.getAttribute(attName).getValue(t)).doubleValue());
							mt.put(i, j, vtemp);
							ok = true;
						}
					}
					if(!ok){
						for(DynamicFeature f : l){
							if(p.intersects(f.getGeometry(t).get().getJTS())){
								//mt.put(i, j, new Integer((Integer)f.getAttribute(attName).getValue(t)).doubleValue());
								//mt.put(i, j, new Long((Long)f.getAttribute(attName).getValue(t)).doubleValue());
								vtemp = new Double(f.getAttribute(attName).getValue(t).toString());
								ftemp = f;
								mt.put(i, j, vtemp);
								ok = true;
								break;
							}
						}
					}
					
					if(!ok){
						mt.put(i, j, Raster.getNoDataValue());
					}
				}
			}	
			return mt;
		}
		throw new IllegalArgumentException("attribute '"+attName+"' does not exist");
	}

	public <E extends DynamicElement> Matrix initWithMajorSurface(DynamicLayer<E> layer, Instant t, String attName, double cellsize) {
		return initWithMajorSurface(layer,layer.getType().getElementType(),t,attName,cellsize);
	}
	
	public <E extends DynamicElement> Matrix initWithMajorSurface(DynamicLayer<E> layer, DynamicElementType type, Instant t, String attName, double cellsize) {
		Raster.setCellSize(cellsize);
		if(type.hasAttributeType(attName)){
			
			int ncols = new Double((Math.floor((layer.maxX() - layer.minX())/cellsize)) + 1).intValue();
			int nrows = new Double((Math.floor((layer.maxY() - layer.minY())/cellsize)) + 1).intValue();
			
			Matrix mt = create(ncols,nrows,cellsize,layer.minX(), layer.maxX(), layer.minY(), layer.maxY(), Raster.getNoDataValue());
				
			boolean ok, okMax;
			Envelope env;
			PreparedPolygon p = null;
			Polygon poly = null;
			double x;
			double y;
			double area, max;
			List<DynamicFeature> l = null;
			DynamicFeature ftemp = null;
			double vtemp = Raster.getNoDataValue();
			int modulo = 1;
			WKTReader r = new WKTReader();
			STRtree sIndex = new STRtree();
			
			for(E f : layer){
				sIndex.insert(f.getGeometry(t).get().getJTS().getEnvelopeInternal(), f);
			}
			sIndex.build();
			
			double yorigin;
			if(layer.maxY() - layer.minY() % cellsize == 0){
				yorigin = layer.minY() + Math.floor((layer.maxY() - layer.minY())/cellsize)*cellsize;
			}else{
				yorigin = layer.minY() + (Math.floor((layer.maxY() - layer.minY())/cellsize) + 1)*cellsize;
			}
			
			for(int j=0; j<nrows; j++){
				x = layer.minX() + (cellsize/2);
				y = yorigin - (cellsize/2) - j*cellsize;
				if(j%modulo == 0){
					env = new Envelope(new Coordinate(layer.minX(), y + 2*cellsize), new Coordinate(layer.maxX(), y - 2*cellsize));
					l = sIndex.query(env);
				}
					
				for(int i=0; i<ncols; i++){
					x = layer.minX() + (cellsize/2) + i*cellsize;
					ok = false;
					okMax = false;
					
					try {
						poly = (Polygon) r.read("POINT ("+x+" "+y+")").buffer(cellsize/2);
						p = new PreparedPolygon(poly);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					
					if(ftemp != null){
						if(p.intersects(ftemp.getGeometry(t).get().getJTS())){
							if(poly.intersection(ftemp.getGeometry(t).get().getJTS()).getArea() >= (Math.PI*cellsize*cellsize/2)){
								mt.put(i, j, vtemp);
								ok = true;
							}		
						}
					}
					
					if(!ok){
						max = 0;
						for(DynamicFeature f : l){
							//System.out.println(poly.getCentroid()+" "+f.getGeometry(t).get().getJTS().getCentroid());
							if(p.intersects(f.getGeometry(t).get().getJTS())){
								area = poly.intersection(f.getGeometry(t).get().getJTS()).getArea();
								if(area >= (Math.PI*cellsize*cellsize/2)){
									vtemp = new Double(f.getAttribute(attName).getValue(t).toString());
									ftemp = f;
									mt.put(i, j, vtemp);
									ok = true;
									break;
								}else{
									if(area > max){
										max = area;
										ftemp = f;
										okMax = true;
									}
								}
							}
						}
						if(okMax){
							vtemp = new Double(ftemp.getAttribute(attName).getValue(t).toString());
							mt.put(i, j, vtemp);
							ok = true;
						}
					}
					
					if(!ok){
						mt.put(i, j, Raster.getNoDataValue());
					}
				}
			}	
			return mt;
		}
		throw new IllegalArgumentException("attribute '"+attName+"' does not exist");
	}
	
	public <E extends DynamicElement> Matrix initLines(DynamicLayer<E> layer, Instant t, String attName, 
			double cellsize, double defaultValue, double... wantedValue) {
		return initLines(layer,layer.getType().getElementType(),t,attName,cellsize, defaultValue, wantedValue);
	}
	
	public <E extends DynamicElement> Matrix initLines(DynamicLayer<E> layer, DynamicElementType type, 
			Instant t, String attName, double cellsize, double defaultValue, double... wantedValue) {
		Raster.setCellSize(cellsize);
		if(type.hasAttributeType(attName)){
			
			int ncols = new Double((Math.floor((layer.maxX() - layer.minX())/cellsize)) + 1).intValue();
			int nrows = new Double((Math.floor((layer.maxY() - layer.minY())/cellsize)) + 1).intValue();
			
			Matrix mt = create(ncols,nrows,cellsize,layer.minX(), layer.maxX(), layer.minY(), layer.maxY(), Raster.getNoDataValue());
				
			boolean ok, okMax;
			Envelope env;
			Point p = null;
			double x;
			double y;
			double v;
			int vi, vIndex;
			List<DynamicFeature> l = null;
			int modulo = 2;
			WKTReader r = new WKTReader();
			STRtree sIndex = new STRtree();
			
			for(E f : layer){
				for(double wv : wantedValue){
					if(new Double(f.getAttribute(attName).getValue(t).toString()) == wv){
						sIndex.insert(f.getGeometry(t).get().getJTS().getEnvelopeInternal(), f);
						break;
					}
				}
			}
			sIndex.build();
			
			double yorigin;
			if(layer.maxY() - layer.minY() % cellsize == 0){
				yorigin = layer.minY() + Math.floor((layer.maxY() - layer.minY())/cellsize)*cellsize;
			}else{
				yorigin = layer.minY() + (Math.floor((layer.maxY() - layer.minY())/cellsize) + 1)*cellsize;
			}
			
			for(int j=0; j<nrows; j++){
				x = layer.minX() + (cellsize/2);
				y = yorigin - cellsize/2 - j*cellsize;
				if(j%modulo == 0){
					env = new Envelope(new Coordinate(layer.minX(), y + 2*cellsize), new Coordinate(layer.maxX(), y - 2*cellsize));
					l = sIndex.query(env);
				}
					
				for(int i=0; i<ncols; i++){
					x = layer.minX() + (cellsize/2) + i*cellsize;
					ok = false;
					try {
						p = (Point)(r.read("POINT ("+x+" "+y+")"));
					} catch (ParseException e) {
						e.printStackTrace();
					}
		
					if(!ok){
						vi = -1;
						vIndex = 999999999;
						okMax = false;
						for(DynamicFeature f : l){
							if(p.distance(f.getGeometry(t).get().getJTS()) < cellsize/2){
								v = new Double(f.getAttribute(attName).getValue(t).toString());
								for(int wi=0; wi<wantedValue.length; wi++){
									if(v == wantedValue[wi]){
										vi = wi;
										break;
									}
								}
								if(vi == 0){
									mt.put(i, j, v);
									ok = true;
									break;
								}else {
									vIndex = Math.min(vIndex, vi);
									okMax = true;
								}
							}
						}
						if(okMax){
							mt.put(i, j, wantedValue[vIndex]);
							ok = true;
						}
					}
					
					if(!ok){
						mt.put(i, j, defaultValue);
					}
				}
			}	
			return mt;
		}
		throw new IllegalArgumentException("attribute '"+attName+"' does not exist");
	}
	
	public <E extends DynamicElement> Matrix init2(DynamicLayer<E> layer, DynamicLayer<DynamicFeature> layer2, Instant t, String attName, double cellsize) {
		return init2(layer,layer2, layer.getType().getElementType(),t,attName,cellsize);
	}
	
	public <E extends DynamicElement> Matrix init2(DynamicLayer<E> layer, DynamicLayer<DynamicFeature> layer2, DynamicElementType type, Instant t, String attName, double cellsize) {
		Raster.setCellSize(cellsize);
		if(type.hasAttributeType(attName)){
				//&& type.getAttributeType(attName).getBinding().equals(Integer.class)){
				//&& type.getAttributeType(attName).getBinding().equals(Long.class)){	
			int ncols = new Double((layer.maxX() - layer.minX())/cellsize).intValue();
			int nrows = new Double((layer.maxY() - layer.minY())/cellsize).intValue();
			Matrix mt = create(ncols,nrows,cellsize,layer.minX(), layer.maxX(), layer.minY(), layer.maxY(),-1);
				
			boolean ok;
			Envelope env;
			PreparedPoint p = null;
			double x;
			double y;
			List<DynamicFeature> l = null;
			List<DynamicFeature> l2 = null;
			DynamicFeature ftemp = null;
			double vtemp = -1;
			//int modulo = nrows/100;
			int modulo = 1;
			WKTReader r = new WKTReader();
			STRtree sIndex = new STRtree();
			for(E f : layer){
				sIndex.insert(f.getGeometry(t).get().getJTS().getEnvelopeInternal(), f);
			}
			sIndex.build();
			
			STRtree sIndex2 = new STRtree();
			for(DynamicFeature f : layer2){
				sIndex2.insert(f.getGeometry(t).get().getJTS().getEnvelopeInternal(), f);
			}
			sIndex2.build();
			
			for(int j=0; j<nrows; j++){
				x = layer.minX();
				y = layer.maxY() - j*cellsize;
				if(j%modulo == 0){
					env = new Envelope(new Coordinate(layer.minX(), y + 2*cellsize), new Coordinate(layer.maxX(), y - 2*cellsize));
					l = sIndex.query(env);
					l2 = sIndex2.query(env);
				}
					
				for(int i=0; i<ncols; i++){
					x = layer.minX() + i*cellsize;
					ok = false;
					try {
						p = new PreparedPoint((Puntal)r.read("POINT ("+x+" "+y+")"));
					} catch (ParseException e) {
						e.printStackTrace();
					}
					//System.out.println(p);
					if(ftemp != null){
						if(p.intersects(ftemp.getGeometry(t).get().getJTS())){
							//mt.put(i, j, new Integer((Integer)f.getAttribute(attName).getValue(t)).doubleValue());
							//mt.put(i, j, new Long((Long)f.getAttribute(attName).getValue(t)).doubleValue());
							mt.put(i, j, vtemp);
							ok = true;
						}
					}
					if(!ok){
						for(DynamicFeature f : l){
							if(p.intersects(f.getGeometry(t).get().getJTS())){
								//mt.put(i, j, new Integer((Integer)f.getAttribute(attName).getValue(t)).doubleValue());
								//mt.put(i, j, new Long((Long)f.getAttribute(attName).getValue(t)).doubleValue());
								vtemp = new Double(f.getAttribute(attName).getValue(t).toString());
								ftemp = f;
								mt.put(i, j, vtemp);
								ok = true;
								break;
							}
						}
					}
					if(!ok){
						for(DynamicFeature f : l2){
							if(p.intersects(f.getGeometry(t).get().getJTS())){
								//mt.put(i, j, new Integer((Integer)f.getAttribute(attName).getValue(t)).doubleValue());
								//mt.put(i, j, new Long((Long)f.getAttribute(attName).getValue(t)).doubleValue());
								mt.put(i, j, new Double(f.getAttribute(attName).getValue(t).toString()));
								ok = true;
								break;
							}
						}
					}
					
					if(!ok){
						mt.put(i, j, -1);
					}
				}
			}	
			return mt;
		}
		throw new IllegalArgumentException("attribute '"+attName+"' does not exist");
	}


	/*
	public ArrayMatrix create(DynamicLayer<DynamicFeature> layer, double cellsize) {
		int ncols = new Double((layer.maxX() - layer.minX())/cellsize).intValue();
		int nrows = new Double((layer.maxY() - layer.minY())/cellsize).intValue();

		ArrayMatrix mt = new ArrayMatrix(ncols,nrows,cellsize,layer.minX(), layer.maxX(), layer.minY(), layer.maxY(),-1);
		mt.init(-1);
		
		return mt;
	}*/
	
	/*
	public ArrayMatrix createAndExport(DynamicLayer<DynamicFeature> layer, Instant t, String attName) {
		if(layer.getFeatureType().hasAttributeType(attName)){
			int cellSize = 10;
			int ncols = new Double((layer.maxX() - layer.minX())/cellSize).intValue();
			int nrows = new Double((layer.maxY() - layer.minY())/cellSize).intValue();
			
			STRtree sIndex = new STRtree(); 
			for(DynamicFeature f : layer){
				sIndex.insert(f.getGeometry(t).getVectorial().getJTS().getEnvelopeInternal(), f);
			}
			sIndex.build();
			
			try{
				BufferedWriter bw = new BufferedWriter(new FileWriter("./data/test.asc"));
				bw.write("ncols "+ncols);
				bw.newLine();
				bw.write("nrows "+nrows);
				bw.newLine();
				bw.write("xllcorner "+layer.minX());
				bw.newLine();
				bw.write("yllcorner "+layer.maxY());
				bw.newLine();
				bw.write("cellsize "+cellSize);
				bw.newLine();
				bw.write("NODATA_value -1");
				bw.newLine();
				
				boolean ok;
				Envelope env;
				PreparedPoint p;
				double x;
				double y;
				List<DynamicFeature> l = null;
				//env = new Envelope(new Coordinate(layer.getMinX(),layer.getMaxY()),new Coordinate(layer.getMinY(),layer.getMaxX()));
				//l = sIndex.query(env);
				int modulo = nrows/100;
				for(int j=0; j<nrows; j++){
					x = layer.minX();
					y = layer.maxY() - j*cellSize;
					
					if(j%modulo == 0){
						env = new Envelope(new Coordinate(x,y),new Coordinate(layer.maxX(),y - modulo*cellSize));
						l = sIndex.query(env);
					}
					
					for(int i=0; i<ncols; i++){
						x = layer.minX() + i*cellSize;
						ok = false;
						p = new PreparedPoint((Point)new WKTReader().read("POINT ("+x+" "+y+")"));
						for(DynamicFeature f : l){
							if(p.intersects(f.getGeometry(t).getVectorial().getJTS())){
								bw.write(f.getAttribute(attName).getValue(t)+" ");
								ok = true;
								break;
							}
						}
						if(!ok){
							bw.write(-1+" ");
						}
					}
					bw.newLine();
				}
				bw.close();
				new VisuImageJ("./data/test.asc");
			}catch(IOException e){
				e.printStackTrace(); 
			}catch(ParseException ex){
				ex.printStackTrace();
			}	
			return null;
		}
		throw new IllegalArgumentException("attribute '"+attName+"' does not exist");
	}
	*/
	/*
	public static CoverageMatrix readAsciiFile(String file){
		try{
			ArcGridReader agr = new ArcGridReader(new File(file));
			GridCoverage2D g = 	(GridCoverage2D)agr.read(null);
			
			double cellsize = ((AffineTransform2D)g.getGridGeometry().getGridToCRS()).getScaleX();
			
			return new CoverageMatrix(g,file);
		}catch(DataSourceException ex){
			ex.printStackTrace();
			return null;
		}catch(IOException ex){
			ex.printStackTrace();
			return null;
		}
	}
	*/
}
