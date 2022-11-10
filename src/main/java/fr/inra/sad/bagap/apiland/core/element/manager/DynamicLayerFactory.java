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
package fr.inra.sad.bagap.apiland.core.element.manager;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.plaf.synth.SynthSpinnerUI;

import org.geotools.data.shapefile.files.ShpFiles;
import org.geotools.data.shapefile.dbf.DbaseFileException;
import org.geotools.data.shapefile.dbf.DbaseFileHeader;
import org.geotools.data.shapefile.dbf.DbaseFileReader;
import org.geotools.data.shapefile.dbf.DbaseFileWriter;
import org.geotools.data.shapefile.shp.ShapeType;
import org.geotools.data.shapefile.shp.ShapefileException;
import org.geotools.data.shapefile.shp.ShapefileReader;
import org.geotools.data.shapefile.shp.ShapefileWriter;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;

import fr.inra.sad.bagap.apiland.core.composition.AttributeType;
import fr.inra.sad.bagap.apiland.core.element.DefaultDynamicLayer;
import fr.inra.sad.bagap.apiland.core.element.DynamicElement;
import fr.inra.sad.bagap.apiland.core.element.DynamicFeature;
import fr.inra.sad.bagap.apiland.core.element.DynamicLayer;
import fr.inra.sad.bagap.apiland.core.element.IdManager;
import fr.inra.sad.bagap.apiland.core.element.type.DynamicElementType;
import fr.inra.sad.bagap.apiland.core.element.type.DynamicElementTypeFactory;
import fr.inra.sad.bagap.apiland.core.element.type.DynamicFeatureType;
import fr.inra.sad.bagap.apiland.core.element.type.DynamicLayerType;
import fr.inra.sad.bagap.apiland.core.space.Geometry;
import fr.inra.sad.bagap.apiland.core.space.GeometryFactory;
import fr.inra.sad.bagap.apiland.core.space.Linear;
import fr.inra.sad.bagap.apiland.core.space.Local;
import fr.inra.sad.bagap.apiland.core.space.Surfacic;
import fr.inra.sad.bagap.apiland.core.space.impl.GeometryImplType;
import fr.inra.sad.bagap.apiland.core.structure.RepresentationType;
import fr.inra.sad.bagap.apiland.core.time.Instant;
import fr.inra.sad.bagap.apiland.core.time.Interval;
import fr.inra.sad.bagap.apiland.core.time.Time;
import fr.inra.sad.bagap.apiland.core.time.delay.Delay;

public class DynamicLayerFactory {

	/*
	public static <E extends DynamicElement> DynamicLayer<E> initWithShapeAndTemporalAttribute(String shape, Instant t, String attribute, AbstractDate date) {
		DynamicLayerType layerType = new DynamicLayerType(DefaultDynamicLayer.class);
		DynamicFeatureType featureType = new DynamicFeatureType();
		featureType.addAttributeType(type);
		layerType.addElementType(featureType);
	}
	*/
	
	public static <E extends DynamicElement, D extends DynamicLayer<E>> D initWithShape(String shape, Instant t) {
		return initWithShape(shape, new Interval(t));
	}
	
	public static <E extends DynamicElement, D extends DynamicLayer<E>> D initWithShape(String shape, Time time) {
		DynamicLayerType layerType = new DynamicLayerType(DefaultDynamicLayer.class);
		DynamicFeatureType featureType = new DynamicFeatureType();
		layerType.addElementType(featureType);
		return initWithShape(shape,time,layerType,null,featureType);
	}
	
	public static <E extends DynamicElement, D extends DynamicLayer<E>> D initWithShape(String shape, Instant t, String id) {
		return initWithShape(shape, new Interval(t), id);
	}

	public static <E extends DynamicElement, D extends DynamicLayer<E>> D initWithShape(String shape, Time time, String id) {
		DynamicLayerType layerType = new DynamicLayerType(DefaultDynamicLayer.class);
		DynamicFeatureType featureType = new DynamicFeatureType(id);
		layerType.addElementType(featureType);
		return initWithShape(shape,time,layerType,null,featureType);
	}

	public static <E extends DynamicElement, D extends DynamicLayer<E>> D initWithShape(String shape, Instant t, DynamicLayerType layerType) {
		return initWithShape(shape, new Interval(t), layerType);
	}
	
	public static <E extends DynamicElement, D extends DynamicLayer<E>> D initWithShape(String shape, Time time, DynamicLayerType layerType){
		return initWithShape(shape, time, layerType, null, layerType.getDeepElementType());
	}
	
	public static <E extends DynamicElement, D extends DynamicLayer<E>> D initWithShape(String shape, Instant t, DynamicLayerType layerType, DynamicElementType type) {
		return initWithShape(shape, new Interval(t), layerType, type);
	}
	
	public static <E extends DynamicElement, D extends DynamicLayer<E>> D initWithShape(String shape, Time time, DynamicLayerType layerType, DynamicElementType type){
		return initWithShape(shape, time, layerType, null, type);
	}

	public static <E extends DynamicElement, D extends DynamicLayer<E>> D initWithShape(String shape, Instant t, DynamicLayerType layerType, String condition, DynamicElementType... types){
		return initWithShape(shape, new Interval(t), layerType, condition, types);
	}
	
	public static <E extends DynamicElement, D extends DynamicLayer<E>> D initWithShape(String shape, Time time, DynamicLayerType layerType, String condition, DynamicElementType... types){
		D layer = null;
		try {
			DynamicElementBuilder builder = new DynamicElementBuilder(layerType);
			layer = (D)builder.build();
			
			ShpFiles sf;
			if(shape.endsWith(".shp")){
				sf = new ShpFiles(shape);
			}else{
				sf = new ShpFiles(shape + ".shp");
			}
			
			ShapefileReader sfr = new ShapefileReader(sf, true, false, new org.locationtech.jts.geom.GeometryFactory());
			DbaseFileReader dfr = new DbaseFileReader(sf, true, Charset.defaultCharset());
			DbaseFileHeader dfh = dfr.getHeader();
			
			Map<String, Integer> posIds = new HashMap<String, Integer>();
			Map<String, Integer> positions = new HashMap<String, Integer>();
			AttributeType aType;
			String field;
			for (int i=0; i<dfh.getNumFields(); i++) {
				field = dfh.getFieldName(i).toLowerCase();
				if (layerType.containsIdName(field)) {
					posIds.put(field, i);
				} else {
					positions.put(field, i);
					if(!layerType.containsAttributeByName(field)){
						aType = DynamicElementTypeFactory.createAttributeType(field, time.getClass(), dfh.getFieldClass(i));
						for(DynamicElementType type : types){
							type.addAttributeType(aType);
						}
					}
				}
			}
			
			if (sfr.getHeader().getShapeType().isPolygonType()) {
				initWithGeometries(layer,time,sfr,dfr,posIds,positions,Surfacic.class,condition,types);
			} else if (sfr.getHeader().getShapeType().isLineType()) {
				initWithGeometries(layer,time,sfr,dfr,posIds,positions,Linear.class,condition,types);
			} else if (sfr.getHeader().getShapeType().isPointType()) {
				initWithGeometries(layer,time,sfr,dfr,posIds,positions,Local.class,condition,types);
			} else {
				throw new IllegalArgumentException(sfr.getHeader().getShapeType().name);
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
		
		layer.setName(shape);
		
		return layer;
	}
	
	private static <E extends DynamicElement> void initWithGeometries(DynamicLayer<E> layer, Time time, 
			ShapefileReader sfr, DbaseFileReader dfr, Map<String, Integer> posIds, Map<String, Integer> positions, 
			Class <? extends fr.inra.sad.bagap.apiland.core.space.GeometryType> geometryClass, String condition, DynamicElementType... types){
		org.locationtech.jts.geom.Geometry geo;
		try {
			RepresentationType rType = DynamicElementTypeFactory.createRepresentationType("the_geom", time.getClass(),geometryClass, GeometryImplType.VECTOR);
			for(DynamicElementType t : types){
				t.addRepresentationType(rType);
			}
			int index = 1;
			while (sfr.hasNext()) {
				//System.out.println(index++);
				Object shape = sfr.nextRecord().shape();
				
				if(geometryClass.equals(Surfacic.class)){
					if(((MultiPolygon)shape).getNumGeometries() > 1){
						geo = (MultiPolygon) shape;
					}else if(((MultiPolygon) shape).getNumGeometries() == 1){
						geo = (org.locationtech.jts.geom.Polygon)((MultiPolygon) shape).getGeometryN(0);
					}else{
						throw new IllegalArgumentException();
					}
				}else if(geometryClass.equals(Linear.class)){
					if(((MultiLineString) shape).getNumGeometries() > 1){
						geo = (MultiLineString) shape;
					}else{
						geo = (org.locationtech.jts.geom.LineString)((MultiLineString) shape).getGeometryN(0);
					}
				}else if(geometryClass.equals(Local.class)){
					if(((MultiPoint) shape).getNumGeometries() > 1){
						geo = (MultiPoint) shape;
					}else{
						geo = (org.locationtech.jts.geom.Point)((MultiPoint) shape).getGeometryN(0);
					}
				}else{
					throw new IllegalArgumentException();
				}
				//System.out.println(++index);
				importEntry(layer, time, GeometryFactory.create(geo), dfr, dfr.readEntry(), posIds, positions, condition, types);
			}
			
			double minX = Double.MAX_VALUE;
			double maxX = Double.MIN_VALUE;
			double minY = Double.MAX_VALUE;
			double maxY = Double.MIN_VALUE;
			
			//System.out.println("ici "+minX+" "+maxX+" "+minY+" "+maxY);
			
			Iterator<DynamicFeature> ite = layer.deepIterator();
			DynamicFeature e;
			int i = 0;
			while(ite.hasNext()){
				e = ite.next();
				minX = Math.min(minX, e.minX());
				maxX = Math.max(maxX, e.maxX());
				minY = Math.min(minY, e.minY());
				maxY = Math.max(maxY, e.maxY());
				
				//System.out.println(minX+" "+maxX+" "+minY+" "+maxY+" "+(i++));
			}
			
			layer.getStructure().setMinX(minX);
			layer.getStructure().setMaxX(maxX);
			layer.getStructure().setMinY(minY);
			layer.getStructure().setMaxY(maxY);
			
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public static <E extends DynamicElement> void importEntry(DynamicLayer<E> layer, Time time, 
			Geometry geometry, DbaseFileReader dfr, Object[] entry, 
			Map<String, Integer> posIds, Map<String, Integer> positions, 
			String condition, DynamicElementType... types){
		
		boolean childType = false;
		for(DynamicElementType t1 : types){
			for(DynamicElementType t2 : layer.getType().getElementTypes()){
				if(t1.equals(t2)){
					childType = true;
				}
			}
		}
		
		DynamicElementType type = null;
		DynamicElement element = null;
		DynamicElementBuilder builder = null;
		
		if(childType){
			if(condition != null){
				Object o = entry[positions.get(condition)];
				for(DynamicElementType t : types){
					if(t.hasCondition(o)){
						type = t;
					}
				}
				if(type == null){
					throw new IllegalArgumentException();
				}
			}else{
				type = types[0];
			}
			
			builder = new DynamicElementBuilder(type);
			builder.setTime(time);
			for(AttributeType a : type.getAttributeTypes()){
				if(positions.containsKey(a.getName())){
					if(a.getBinding().equals(Boolean.class) 
							&& dfr.getHeader().getFieldClass(positions.get(a.getName())).equals(Integer.class)){
						builder.setAttribute(a.getName(), time,	new Boolean(((Integer) entry[positions.get(a.getName())]) != 0));
					} else{
						builder.setAttribute(a.getName(), time,	(Serializable)entry[positions.get(a.getName())]);
					}
				}
			}
			builder.setRepresentation("the_geom", time, geometry);
			element = builder.build();
			
			if(posIds.size() == 0){
				element.setId(IdManager.get().getId());
			}else{
				if (entry[posIds.get(element.getType().getIdName().toLowerCase())] instanceof String) {
					element.setId((String)entry[posIds.get(element.getType().getIdName().toLowerCase())]);
				}else if(dfr.getHeader().getFieldClass(posIds.get(element.getType().getIdName().toLowerCase())).equals(Integer.class)){
					element.setId((entry[posIds.get(element.getType().getIdName().toLowerCase())]).toString());
				}else if(dfr.getHeader().getFieldClass(posIds.get(element.getType().getIdName().toLowerCase())).equals(Long.class)){
					element.setId((entry[posIds.get(element.getType().getIdName().toLowerCase())]).toString());
				}else{
					element.setId((entry[posIds.get(element.getType().getIdName().toLowerCase())]).toString());
				}
			}
			
			//System.out.println("id : "+element.getId());
			
		}else{
			
			if(layer.getType().getElementTypes().size() > 1){
				Object o = entry[positions.get(condition)];				
				for(DynamicElementType t : layer.getType().getElementTypes()){
					if(t.hasDeepCondition(o)){
						type = t;
						break;
					}
				}
				if(type == null){
					throw new IllegalArgumentException("'"+o+"' is not recognized as a type");
				}
			}else{
				type = layer.getType().getElementType();
			}
			
			String id;
			if(posIds.size() == 0){
				id = IdManager.get().getId();
			}else if(!posIds.containsKey(type.getIdName())){
				id = IdManager.get().getId();
			}else{
				if (entry[posIds.get(type.getIdName())] instanceof String) {
					id = (String) entry[posIds.get(type.getIdName())];
				}else if(dfr.getHeader().getFieldClass(posIds.get(type.getIdName())).equals(Integer.class)){
					id = (entry[posIds.get(type.getIdName())]).toString();
				}else if(dfr.getHeader().getFieldClass(posIds.get(type.getIdName())).equals(Long.class)){
					id = (entry[posIds.get(type.getIdName())]).toString();
				}else{
					id = (entry[posIds.get(type.getIdName())]).toString();
				}
			}
			//System.out.println("id "+id);
			//element = null;
			element = layer.get(id);
			if(element == null){
				builder = new DynamicElementBuilder(type);
				for(AttributeType a : type.getAttributeTypes()){
					if(positions.containsKey(a.getName())){
						if(dfr.getHeader().getFieldClass(positions.get(a.getName())).equals(Integer.class)){
							builder.setAttribute(a.getName(), time,	(Serializable) entry[positions.get(a.getName())]);
						}else if(dfr.getHeader().getFieldClass(positions.get(a.getName())).equals(Long.class)){
							builder.setAttribute(a.getName(), time,	(Serializable) entry[positions.get(a.getName())]);
						}else{
							builder.setAttribute(a.getName(), time,	(Serializable) entry[positions.get(a.getName())]);
						}
					}
				}
				element = builder.build();
				
				element.setId(id);
			}
			
			importEntry((DynamicLayer<E>) element, time, geometry, dfr, entry, posIds, positions, condition, types);
		}
		
		layer.add((E) element);
	}
	
	public static boolean exportShape(DynamicElement element, Instant time, String shape){
		return exportShape(element,time,shape,((DynamicLayerType)element.getType()).getFeatureTypes());
	}
	
	public static boolean exportShape(DynamicElement element, Instant time, String shape, double minx, double maxx, double miny, double maxy){
		return exportShape(element, time, shape, minx, maxx, miny, maxy,((DynamicLayerType)element.getType()).getFeatureTypes());
	}
	
	private static boolean exportShape(DynamicElement element, Instant time,
			String shape, List<DynamicFeatureType> types) {
		DynamicElementType[] d = new DynamicElementType[types.size()];
		int i=0;
		for(DynamicElementType t : types){
			d[i] = t;
			i++;
		}
		return exportShape(element,time,shape,d);
	}
	
	private static boolean exportShape(DynamicElement element, Instant time,
			String shape, double minx, double maxx, double miny, double maxy, List<DynamicFeatureType> types) {
		DynamicElementType[] d = new DynamicElementType[types.size()];
		int i=0;
		for(DynamicElementType t : types){
			d[i] = t;
			i++;
		}
		return exportShape(element, time, shape, minx, maxx, miny, maxy, d);
	}

	public static boolean exportShape(DynamicElement element, Instant time, String shape, Class<? extends DynamicElement>... classes ){
		DynamicElementType[] d = new DynamicElementType[classes.length];
		int i=0;
		for(Class<? extends DynamicElement> c : classes){
			if(element.getType().hasElementType(c)){
				d[i] = element.getType().getElementType(c);
				i++;
			}
		}
		return exportShape(element,time,shape,d);
	}
	
	public static boolean exportShape(DynamicElement element, Instant time, String shape, Set<DynamicElementType> types) {
		DynamicElementType[] d = new DynamicElementType[types.size()];
		int i=0;
		for(DynamicElementType t : types){
			d[i] = t;
			i++;
		}
		return exportShape(element,time,shape,d);
	}
	
	public static boolean exportShape(DynamicElement element, Instant time, String shape, DynamicElementType... types) {
		return exportShape(element, time, shape, -1, -1, -1, -1, types);
	}
	
	public static boolean exportShape(DynamicElement element, Instant time, String shape, 
			double minx, double maxx, double miny, double maxy, DynamicElementType... types) {
		try {
			int count = element.count(time, types);
			
			DbaseFileHeader header = new DbaseFileHeader();
			header.setNumRecords(count);
			
			Set<String> setId = new TreeSet<String>();
			for(DynamicElementType t : types){
				setId = t.getInheritedIdNames(element.getType(),setId);
			}
			setId.remove(element.getType().getIdName());
			
			for(String id : setId){
				//System.out.println(id);
				header.addColumn(id, 'C', 8, 0);
			}
			
			Set<AttributeType> setAtt = new TreeSet<AttributeType>();
			for(DynamicElementType t : types){
				setAtt = t.getInheritedAttributeTypes(element.getType(),setAtt);
			}
			
			for (AttributeType t : setAtt){
				//System.out.println(t.getName()+" "+t.getBinding());
				if (t.getBinding().equals(String.class)) {
					header.addColumn(t.getName(), 'C', 80, 0);
				} else if (t.getBinding().equals(Long.class)) {
					header.addColumn(t.getName(), 'N', 12, 0);
				} else if (t.getBinding().equals(Integer.class)) {
					header.addColumn(t.getName(), 'N', 8, 0);
				} else if (t.getBinding().equals(Double.class)) {
					header.addColumn(t.getName(), 'N', 8, 3);
				} else if (t.getBinding().equals(Boolean.class)) {
					header.addColumn(t.getName(), 'L', 1, 0);
				} else {
					header.addColumn(t.getName(), 'C', 20, 0);
				}
			}
			
			WritableByteChannel out = new FileOutputStream(shape + ".dbf").getChannel();
			DbaseFileWriter dbfW = new DbaseFileWriter(header, out);
			FileOutputStream shp = new FileOutputStream(shape + ".shp");
			FileOutputStream shx = new FileOutputStream(shape + ".shx");
			ShapefileWriter shapeW = new ShapefileWriter(shp.getChannel(), shx.getChannel());
			
			if(minx == -1 && maxx == -1 && miny == -1 && maxy == -1){
				
				minx = Double.MAX_VALUE;
				maxx = Double.MIN_VALUE;
				miny = Double.MAX_VALUE;
				maxy = Double.MIN_VALUE;
				
				Iterator<DynamicFeature> ite = ((DynamicLayer) element).deepIterator();
				DynamicFeature f;
				while(ite.hasNext()){
					f = ite.next();
					minx = Math.min(minx, f.minX());
					maxx = Math.max(maxx, f.maxX());
					miny = Math.min(miny, f.minY());
					maxy = Math.max(maxy, f.maxY());
				}
			}
		
			if (types[0].getRepresentationType("the_geom")
					.getSpatialBinding().equals(Surfacic.class)) {
				shapeW.writeHeaders(new Envelope(minx, maxx,
						miny, maxy), ShapeType.POLYGON, count, 1000000);
			} else if (types[0].getRepresentationType("the_geom")
					.getSpatialBinding().equals(Linear.class)) {
				shapeW.writeHeaders(new Envelope(minx, maxx,
						miny, maxy), ShapeType.ARC, count, 1000000);
			} else if (types[0].getRepresentationType("the_geom")
					.getSpatialBinding().equals(Local.class)) {
				shapeW.writeHeaders(new Envelope(minx, maxx,
						miny, maxy), ShapeType.POINT, count, 1000000);
			} else {
				throw new IllegalArgumentException();
			}
			

			exportElement(element, time, dbfW, shapeW, setId, setAtt, types);

			dbfW.close();
			shapeW.close();
			out.close();
			shp.close();
			shx.close();
		} catch (DbaseFileException ex) {
			ex.printStackTrace();
			return false;
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
			return false;
		} catch (IOException ex) {
			ex.printStackTrace();
			return false;
		} finally{
			try {
				//String prj_input = DynamicLayerFactory.class.getResource("lambert93.prj").toString().replace("file:/", "");
				//Tool.copy(prj_input, shape+".prj");
				Tool.copy(DynamicLayerFactory.class.getResourceAsStream("lambert93.prj"), shape+".prj");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	private static void exportElement(DynamicElement element, Instant time, DbaseFileWriter dbfW, 
			ShapefileWriter shapeW, Set<String> setId, Set<AttributeType> setAtt, DynamicElementType... types) {
		
		boolean export = false;
		for(DynamicElementType t : types){
			if (element.getType().equals(t)){
				export = true;
			}
		}
		
		if (!export) {
			if(element instanceof DynamicLayer){
				for(DynamicElement e : (DynamicLayer<DynamicElement>)element){
					exportElement(e,time,dbfW,shapeW,setId,setAtt,types);
				}
			}
		}else{
			if(element.isActive(time)){
				try {
					Object[] record = new Object[setId.size()+setAtt.size()];
					
					int index = 0;
					for(String id : setId){
						record[index++] = element.getInheritedId(id);
					}
					
					for(AttributeType att : setAtt){
						//record[index++] = element.getInheritedAttribute(att.getName()).getValue(time);
						if(element.getType().hasAttributeType(att.getName())){
							//System.out.println("export de l'attribut "+ att.getName());
							record[index++] = element.getInheritedAttribute(att.getName()).getValue(time);
						}else{
							record[index++] = null;
						}
					}
					
					dbfW.write(record);
					
					if(element.getGeometry(time) == null){
						shapeW.writeGeometry(null);
					}else{
						//element.getGeometry(time).get().getJTS().setSRID(new Integer(element.getId()));
						if(element.getGeometry(time).get().getJTS() instanceof LineString){
							shapeW.writeGeometry(element.getGeometry(time).get().getJTS().getFactory().createMultiLineString(new LineString[]{(LineString)element.getGeometry(time).get().getJTS()}));
						}else{
							shapeW.writeGeometry(element.getGeometry(time).get().getJTS());
						}
					}
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
	}
	
	public static boolean exportTimeShape(DynamicElement element, Instant start, Instant end, Delay delay, String shape){
		return exportTimeShape(element, start, end, delay, shape, ((DynamicLayerType)element.getType()).getFeatureTypes());
	}
	
	private static boolean exportTimeShape(DynamicElement element, Instant start, Instant end, Delay delay,	String shape, List<DynamicFeatureType> types) {
		DynamicElementType[] d = new DynamicElementType[types.size()];
		int i=0;
		for(DynamicElementType t : types){
			d[i] = t;
			i++;
		}
		return exportTimeShape(element, start, end, delay, shape, d);
	}
	
	public static boolean exportTimeShape(DynamicElement element, Instant start, Instant end, Delay delay, String shape, DynamicElementType... types) {
		return exportTimeShape(element, start, end, delay, shape, -1, -1, -1, -1, types);
	}
	
	public static boolean exportTimeShape(DynamicElement element, Instant start, Instant end, Delay delay, String shape, 
			double minx, double maxx, double miny, double maxy, DynamicElementType... types) {
		try {
			// revoir le nombre d'élément en fonction du nombre de pas de temps
			int count = 0; 
			Instant time = start;
			while(time.isBefore(end) || time.equals(end)){
				count += element.count(time, types);
				time = delay.next(time);
			}
			
			System.out.println("nombre d'élément = "+count);
			
			DbaseFileHeader header = new DbaseFileHeader();
			header.setNumRecords(count);
			
			Set<String> setId = new TreeSet<String>();
			for(DynamicElementType t : types){
				setId = t.getInheritedIdNames(element.getType(),setId);
			}
			setId.remove(element.getType().getIdName());
			
			for(String id : setId){
				//System.out.println(id);
				header.addColumn(id, 'C', 8, 0);
			}
			
			// ajout des colonnes 'start' et 'end' de type DATE
			header.addColumn("start", 'D', 10, 0);
			header.addColumn("end", 'D', 10, 0);
			
			Set<AttributeType> setAtt = new TreeSet<AttributeType>();
			for(DynamicElementType t : types){
				setAtt = t.getInheritedAttributeTypes(element.getType(),setAtt);
			}
			
			for (AttributeType t : setAtt){ 
				//System.out.println(t.getName()+" "+t.getBinding());
				if (t.getBinding().equals(String.class)) {
					header.addColumn(t.getName(), 'C', 80, 0);
				} else if (t.getBinding().equals(Long.class)) {
					header.addColumn(t.getName(), 'N', 12, 0);
				} else if (t.getBinding().equals(Integer.class)) {
					header.addColumn(t.getName(), 'N', 8, 0);
				} else if (t.getBinding().equals(Double.class)) {
					header.addColumn(t.getName(), 'N', 8, 3);
				} else if (t.getBinding().equals(Boolean.class)) {
					header.addColumn(t.getName(), 'L', 1, 0);
				} else {
					header.addColumn(t.getName(), 'C', 20, 0);
				}
			}
			
			WritableByteChannel out = new FileOutputStream(shape + ".dbf").getChannel();
			DbaseFileWriter dbfW = new DbaseFileWriter(header, out);
			FileOutputStream shp = new FileOutputStream(shape + ".shp");
			FileOutputStream shx = new FileOutputStream(shape + ".shx");
			ShapefileWriter shapeW = new ShapefileWriter(shp.getChannel(), shx.getChannel());
			
			if(minx == -1 && maxx == -1 && miny == -1 && maxy == -1){
				
				minx = Double.MAX_VALUE;
				maxx = Double.MIN_VALUE;
				miny = Double.MAX_VALUE;
				maxy = Double.MIN_VALUE;
				
				Iterator<DynamicFeature> ite = ((DynamicLayer) element).deepIterator();
				DynamicFeature f;
				while(ite.hasNext()){
					f = ite.next();
					minx = Math.min(minx, f.minX());
					maxx = Math.max(maxx, f.maxX());
					miny = Math.min(miny, f.minY());
					maxy = Math.max(maxy, f.maxY());
				}
			}
		
			if (types[0].getRepresentationType("the_geom").getSpatialBinding().equals(Surfacic.class)) {
				shapeW.writeHeaders(new Envelope(minx, maxx, miny, maxy), ShapeType.POLYGON, count, 1000000);
			} else if (types[0].getRepresentationType("the_geom").getSpatialBinding().equals(Linear.class)) {
				shapeW.writeHeaders(new Envelope(minx, maxx, miny, maxy), ShapeType.ARC, count, 1000000);
			} else if (types[0].getRepresentationType("the_geom").getSpatialBinding().equals(Local.class)) {
				shapeW.writeHeaders(new Envelope(minx, maxx, miny, maxy), ShapeType.POINT, count, 1000000);
			} else {
				throw new IllegalArgumentException();
			}
			
			// boucle sur le pas de temps pour l'export des elements
			time = start;
			while(time.isBefore(end) || time.equals(end)){
				exportTimeElement(element, time, delay, dbfW, shapeW, setId, setAtt, types);
				time = delay.next(time);
			}

			dbfW.close();
			shapeW.close();
			out.close();
			shp.close();
			shx.close();
		} catch (DbaseFileException ex) {
			ex.printStackTrace();
			return false;
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
			return false;
		} catch (IOException ex) {
			ex.printStackTrace();
			return false;
		} finally{
			try {
				//String prj_input = DynamicLayerFactory.class.getResource("lambert93.prj").toString().replace("file:/", "");
				//Tool.copy(prj_input, shape+".prj");
				Tool.copy(DynamicLayerFactory.class.getResourceAsStream("lambert93.prj"), shape+".prj");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	private static void exportTimeElement(DynamicElement element, Instant time, Delay delay, DbaseFileWriter dbfW, 
			ShapefileWriter shapeW, Set<String> setId, Set<AttributeType> setAtt, DynamicElementType... types) {
		
		Instant next = delay.next(time);
		boolean export = false;
		for(DynamicElementType t : types){
			if (element.getType().equals(t)){
				export = true;
			}
		}
		
		if (!export) {
			if(element instanceof DynamicLayer){
				for(DynamicElement e : (DynamicLayer<DynamicElement>)element){
					exportTimeElement(e, time, delay, dbfW, shapeW, setId, setAtt, types);
				}
			}
		}else{
			if(element.isActive(time)){
				try {
					Object[] record = new Object[setId.size()+setAtt.size()+2];
					
					int index = 0;
					for(String id : setId){
						record[index++] = element.getInheritedId(id);
					}
					
					// export des dates "start" et "end"
					Calendar c = new GregorianCalendar();
					c.set(time.year(), time.month()-1, time.dayOfMonth());
					record[index++] = c.getTime();
					c.set(next.year(), next.month()-2, next.dayOfMonth());
					record[index++] = c.getTime();
					//record[index++] = new Date(time.year(), time.month(), time.dayOfMonth());
					//record[index++] = new Date(next.year(), next.month(), next.dayOfMonth());
					//record[index++] = time.year()+"-"+time.month()+"-"+time.dayOfMonth();
					//record[index++] = next.year()+"-"+next.month()+"-"+next.dayOfMonth();
					
					for(AttributeType att : setAtt){
						//record[index++] = element.getInheritedAttribute(att.getName()).getValue(time);
						if(element.getType().hasAttributeType(att.getName())){
							//System.out.println("export de l'attribut "+ att.getName());
							record[index++] = element.getInheritedAttribute(att.getName()).getValue(time);
						}else{
							record[index++] = null;
						}
					}
					
					dbfW.write(record);
					
					if(element.getGeometry(time) == null){
						shapeW.writeGeometry(null);
					}else{
						//element.getGeometry(time).get().getJTS().setSRID(new Integer(element.getId()));
						if(element.getGeometry(time).get().getJTS() instanceof LineString){
							shapeW.writeGeometry(element.getGeometry(time).get().getJTS().getFactory().createMultiLineString(new LineString[]{(LineString)element.getGeometry(time).get().getJTS()}));
						}else{
							shapeW.writeGeometry(element.getGeometry(time).get().getJTS());
						}
					}
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
	}

}
