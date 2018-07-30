package fr.inra.sad.bagap.apiland.core.space.impl.raster;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.geotools.data.shapefile.files.ShpFiles;
import org.geotools.data.shapefile.shp.ShapefileException;
import org.geotools.data.shapefile.shp.ShapefileReader;

import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;

import fr.inra.sad.bagap.apiland.core.composition.Attribute;
import fr.inra.sad.bagap.apiland.core.element.DynamicFeature;
import fr.inra.sad.bagap.apiland.core.element.DynamicLayer;
import fr.inra.sad.bagap.apiland.core.space.Geometry;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.ArrayMatrixFactory;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.MatrixFactory;
import fr.inra.sad.bagap.apiland.core.time.Instant;

public class RasterManager {
	
	public static Matrix exportMatrix(DynamicLayer<?> layer, String name, Instant t, Map<String, String> map, String cercle, double buffer){
		try{
			ShpFiles sf = new ShpFiles(cercle);
			ShapefileReader sfr = new ShapefileReader(sf, true, false, new com.vividsolutions.jts.geom.GeometryFactory());
			Polygon polygon = (Polygon) ((MultiPolygon) sfr.nextRecord().shape()).getGeometryN(0);
			sfr.close();

			return exportMatrix(layer, name, t, map, polygon, buffer);
			
		} catch (ShapefileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		throw new IllegalArgumentException();
	}
	
	public static Matrix exportMatrix(DynamicLayer<?> layer, String name, Instant t, Map<String, String> map, Polygon cercle, double buffer){
		double minX = cercle.getEnvelopeInternal().getMinX() - buffer;
		double maxX = cercle.getEnvelopeInternal().getMaxX() + buffer;
		double minY = cercle.getEnvelopeInternal().getMinY() - buffer;
		double maxY = cercle.getEnvelopeInternal().getMaxY() + buffer;
		
		return exportMatrix(layer, name, t, map, minX, maxX, minY, maxY);
	}
	
	public static Matrix exportMatrix(DynamicLayer<?> layer, String name, Instant t, Map<String, String> map, double minX, double maxX, double minY, double maxY){
		
		/*
		int tx, ty;
		tx = new Double((Math.floor((minX - layer.minX()) / Raster.getCellSize())) + 1).intValue();
		ty = new Double((Math.floor((layer.maxY() - maxY) / Raster.getCellSize())) + 1).intValue();
		*/
		int ncols = new Double((Math.floor((maxX - minX) / Raster.getCellSize())) + 1).intValue();
		int nrows = new Double((Math.floor((maxY - minY) / Raster.getCellSize())) + 1).intValue();
		
		Matrix m = ArrayMatrixFactory.get().create(ncols, nrows, Raster.getCellSize(), minX, maxX, minY, maxY, Raster.getNoDataValue());
		m.init(m.noDataValue());
		int value;
		
		Iterator<DynamicFeature> ite = layer.deepIterator();
		DynamicFeature f;
		int x, y;
		while(ite.hasNext()){
			f = ite.next();

			value = Raster.getNoDataValue();
			if(f.hasAttribute(name)){
				if(f.getAttribute(name).getValue(t) != null){
					if(map != null){
						//System.out.println(f.getAttribute(name).getValue(t));
						//value = (int) f.getAttribute(name).getValue(t);
						//System.out.println(f.getAttribute(name).getValue(t));
						//System.out.println(map.get(f.getAttribute(name).getValue(t)));
						value = Integer.parseInt(map.get(f.getAttribute(name).getValue(t).toString()));
					}else{
						value = Integer.parseInt(f.getAttribute(name).getValue(t).toString());
					}
				}
				
				if(f.getRepresentation("raster").isActive(t)){
					for(Pixel p : (Raster) f.getRepresentation("raster").getGeometry(t).get()){
						x = p.x();// - tx;
						y = p.y();// - ty;
						if(m.contains(x, y)){
							//System.out.println(x+" "+y+" "+value);
							m.put(x, y, value);
						}
					}
				}
			}else if(f.getLayer().getType().hasIdName(name)){
				String lid = f.getLayer().getId();
				if(map != null){
					value = Integer.parseInt(map.get(lid));
				}else{
					value = Integer.parseInt(lid);
				}
					
				if(f.getRepresentation("raster").isActive(t)){
					for(Pixel p : (Raster) f.getRepresentation("raster").getGeometry(t).get()){
						x = p.x();// - tx;
						y = p.y();// - ty;
						if(m.contains(x, y)){
							//System.out.println(x+" "+y+" "+value);
							m.put(x, y, value);
						}
					}
				}
			}
		}
		
		return m;
	}
	
	public static Matrix exportMatrix(DynamicLayer<?> layer, String name, Instant t){
		return exportMatrix(layer, name, t, null, -1, -1, -1, -1);
	}
	
	public static Matrix exportMatrix(DynamicLayer<?> layer, String name, Instant t, Map<String, Integer> map){
		return exportMatrix2(layer, name, t, map, layer.minX(), layer.maxX(), layer.minY(), layer.maxY());
	}
	
	public static Matrix exportMatrix2(DynamicLayer<?> layer, String name, Instant t, Map<String, Integer> map, double minX, double maxX, double minY, double maxY){
		
		int tx, ty;
		
		if(minX == -1 && maxX == -1 && minY == -1 && maxY == -1){
			minX = layer.minX();
			maxX = layer.maxX();
			minY = layer.minY();
			maxY = layer.maxY();
			tx = 0;
			ty = 0;
		}else{
			tx = new Double((Math.floor((minX - layer.minX()) / Raster.getCellSize())) + 1).intValue();
			ty = new Double((Math.floor((layer.maxY() - maxY) / Raster.getCellSize())) + 1).intValue();
		}
		
		int ncols = new Double((Math.floor((maxX - minX) / Raster.getCellSize())) + 1).intValue();
		int nrows = new Double((Math.floor((maxY - minY) / Raster.getCellSize())) + 1).intValue();
		
		Matrix m = ArrayMatrixFactory.get().create(ncols, nrows, Raster.getCellSize(), minX, maxX, minY, maxY, Raster.getNoDataValue());
		
		//System.out.println(m.getClass());
		m.init(m.noDataValue());
		int value;
		Geometry g;
		
		for(int j=0; j<nrows; j++){
			for(int i=0; i<ncols; i++){
				//CoordinateManager.getProjectedX(m, x)
			}
		}
		
		Iterator<DynamicFeature> ite = layer.deepIterator();
		DynamicFeature f;
		int x, y;
		while(ite.hasNext()){
			f = ite.next();
			
			if(map != null){
				value = map.get(f.getAttribute(name).getValue(t).toString());
			}else{
				value = (Integer) f.getAttribute(name).getValue(t);
			}
			
			if(f.getRepresentation("raster").getGeometry(t) != null){
				for(Pixel p : (Raster) f.getRepresentation("raster").getGeometry(t).get()){
					x = p.x() - tx;
					y = p.y() - ty;
					if(m.contains(x, y)){
						m.put(x, y, value);
					}
				}
			}
			
			/*
			g = f.getRepresentation("raster").getGeometry(t);
			if(g != null){
				for(Pixel p : (Raster) g.get()){
					System.out.println(p);
					m.put(p, value);
				}
			}
			*/
		}
		
		return m;
	}

	
	public static Matrix exportMatrix(Raster r, Matrix ref) {
		Matrix m = MatrixFactory.get(ref.getType()).create(
				ref.width(), ref.height(), ref.cellsize(), 
				ref.minX(), ref.maxX(), ref.minY(), ref.maxY(), ref.noDataValue());
		m.init(Raster.getNoDataValue());
		
		for(Raster rr : ((RasterComposite) r).getRasters()){
			int value = ((PixelComposite) rr).getValue();
			for(Pixel p : rr){
				m.put(p, value);
			}
		}
		
		return m;
	}

}
