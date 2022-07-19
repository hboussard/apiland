package fr.inra.sad.bagap.apiland.core.element.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import org.geotools.data.shapefile.dbf.DbaseFileHeader;
import org.geotools.data.shapefile.dbf.DbaseFileReader;
import org.geotools.data.shapefile.dbf.DbaseFileWriter;
import org.geotools.data.shapefile.files.ShpFiles;
import org.geotools.data.shapefile.shp.ShapeType;
import org.geotools.data.shapefile.shp.ShapefileException;
import org.geotools.data.shapefile.shp.ShapefileReader;
import org.geotools.data.shapefile.shp.ShapefileWriter;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.impl.CoordinateArraySequence;
import org.locationtech.jts.simplify.DouglasPeuckerSimplifier;

public class Tool {

	public static void main(String[] args){
		String path = "C:/Hugues/enseignements/AAE/data/Ba/sig/";
		String input = path+"Parcellaire5";
		String output = path+"Parcellaire8";
		//formatShapeFile(input, output);
		List<Integer> ids = new ArrayList<Integer>();
		ids.add(8);
		ids.add(37);
		ids.add(38);
		ids.add(39);
		ids.add(42);
		retrieveUnits(input, output, ids);
	}
	
	public static String deleteExtension(String file){
		String line="";
		StringTokenizer st = new StringTokenizer(file,".");
		String last="";
		while(st.hasMoreTokens()){
			last = last + line;
			line = st.nextToken();
		}
		return last;
	}
	
	public static void formatShapeFile(String input, String output){
		try(FileOutputStream fos = new FileOutputStream(output+".dbf");
				FileOutputStream shp = new FileOutputStream(output + ".shp");
				FileOutputStream shx = new FileOutputStream(output + ".shx");){
			
			ShpFiles sf = new ShpFiles(input+".shp");
			ShapefileReader sfr = new ShapefileReader(sf, true, false, new org.locationtech.jts.geom.GeometryFactory());
			DbaseFileReader dfr = new DbaseFileReader(sf, true, Charset.defaultCharset());
			dfr.getHeader();
			
			DbaseFileHeader header = new DbaseFileHeader();
			header.setNumRecords(dfr.getHeader().getNumRecords());
			for(int i=0; i<dfr.getHeader().getNumFields(); i++){
				header.addColumn(dfr.getHeader().getFieldName(i), dfr.getHeader().getFieldType(i), dfr.getHeader().getFieldLength(i), dfr.getHeader().getFieldDecimalCount(i));
			}
			
			int nb = 0;
			
			boolean nid = false;
			if(!hasFieldName(header, "id")){
				nid = true;
				header.addColumn("id", 'c', 8, 0);
				nb++;
			}
			
			boolean nfarm = false;
			if(!hasFieldName(header, "farm")){
				nfarm = true;
				header.addColumn("farm", 'c', 8, 0);
				nb++;
			}
			
			boolean narea = false;
			if(!hasFieldName(header, "area")){
				narea = true;
				header.addColumn("area", 'c', 2, 0);
				nb++;
			}
			
			boolean ntype = false;
			if(!hasFieldName(header, "type")){
				ntype = true;
				header.addColumn("type", 'c', 8, 0);
				nb++;
			}
			
			boolean nfacility = false;
			if(!hasFieldName(header, "facility")){
				nfacility = true;
				header.addColumn("facility", 'c', 20, 0);
				nb++;
			}
			
			DbaseFileWriter dfw = new DbaseFileWriter(header, fos.getChannel());
			ShapefileWriter sfw = new ShapefileWriter(shp.getChannel(), shx.getChannel());
			
			sfw.writeHeaders(
					new Envelope(sfr.getHeader().minX(), sfr.getHeader().maxX(), sfr.getHeader().minY(), sfr.getHeader().maxY()), 
					ShapeType.POLYGON, dfr.getHeader().getNumRecords()-1, sfr.getHeader().getFileLength());
			
			Geometry the_geom;
			Object[] entry, nentry;
			int id = 0;
			while(sfr.hasNext()){
				
				the_geom = (Geometry) sfr.nextRecord().shape();
				entry = dfr.readEntry();
				
				nentry = new Object[entry.length+nb];
					
				int i=0;
				for(Object e : entry){
					nentry[i] = entry[i];
					i++;
				}
					
				if(nid){
					nentry[i++] = id++;
				}
					
				if(nfarm){
					nentry[i++] = "f0";
				}
					
				if(narea){
					nentry[i++] = "AA";
				}
					
				if(ntype){
					nentry[i++] = "parcel";
				}
					
				if(nfacility){
					nentry[i++] = "";
				}
				dfw.write(nentry);
				sfw.writeGeometry(the_geom);
			}
			
			sfr.close();
			dfr.close();
			dfw.close();
			sfw.close();
		} catch (ShapefileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			copy(input+".prj", output+".prj");
		}
	}
	
	private static boolean hasFieldName(DbaseFileHeader header, String name){
		for(int i=0; i<header.getNumFields(); i++){
			if(header.getFieldName(i).equalsIgnoreCase(name)){
				return true;
			}
		}
		return false;
	}
	
	public static void retrieveUnits(String input, String output, List<Integer> ids){
		try(FileOutputStream fos = new FileOutputStream(output+".dbf");
				FileOutputStream shp = new FileOutputStream(output + ".shp");
				FileOutputStream shx = new FileOutputStream(output + ".shx");){
			
			ShpFiles sf = new ShpFiles(input+".shp");
			ShapefileReader sfr = new ShapefileReader(sf, true, false, new org.locationtech.jts.geom.GeometryFactory());
			DbaseFileReader dfr = new DbaseFileReader(sf, true, Charset.defaultCharset());
			dfr.getHeader();
			
			DbaseFileHeader header = new DbaseFileHeader();
			header.setNumRecords(dfr.getHeader().getNumRecords()-ids.size());
			for(int i=0; i<dfr.getHeader().getNumFields(); i++){
				header.addColumn(dfr.getHeader().getFieldName(i), dfr.getHeader().getFieldType(i), dfr.getHeader().getFieldLength(i), dfr.getHeader().getFieldDecimalCount(i));
			}
			
			DbaseFileWriter dfw = new DbaseFileWriter(header, fos.getChannel());
			ShapefileWriter sfw = new ShapefileWriter(shp.getChannel(), shx.getChannel());
			
			sfw.writeHeaders(
					new Envelope(sfr.getHeader().minX(), sfr.getHeader().maxX(), sfr.getHeader().minY(), sfr.getHeader().maxY()), 
					ShapeType.POLYGON, dfr.getHeader().getNumRecords()-ids.size(), sfr.getHeader().getFileLength());
			
			Geometry the_geom;
			Object[] entry;
			int nid;
			while(sfr.hasNext()){
				
				the_geom = (Geometry) sfr.nextRecord().shape();
				entry = dfr.readEntry();
				
				nid = (int) entry[0];
				if(!ids.contains(nid)){
					dfw.write(entry);
					sfw.writeGeometry(the_geom);
				}
			}
			
			sfr.close();
			dfr.close();
			dfw.close();
			sfw.close();
		} catch (ShapefileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			copy(input+".prj", output+".prj");
		}
	}
	
	public static void copyFolder(String source, String target){
		
		File fs = new File(source);
		
		File ft = new File(target);
		ft.mkdirs();
		
		for(File ffs : fs.listFiles()){
			if(ffs.isDirectory()){
				copyFolder(ffs.getAbsolutePath(), target+"/"+ffs.getName());
			}else{
				copy(ffs.getAbsolutePath(), target+"/"+ffs.getName());
			}
		}
	}
	
	public static void deleteFolder(String folder){
		File fs = new File(folder);
		
		for(File f : fs.listFiles()){
			if(f.isDirectory()){
				deleteFolder(f.getAbsolutePath());
			}else{
				f.delete();
			}
		}
		fs.delete();
	}
	
	public static void copyFolderAndDelete(String source, String target){
		
		File fs = new File(source);
		
		File ft = new File(target);
		ft.mkdirs();
		
		for(File ffs : fs.listFiles()){
			if(ffs.isDirectory()){
				copyFolder(ffs.getAbsolutePath(), target+ffs.getName());
			}else{
				copy(ffs.getAbsolutePath(), target+ffs.getName());
			}
			ffs.delete();
		}
		fs.delete();
	}
	
	public static void copy(String sourceFile, String destFile) { 
		
		//System.out.println(sourceFile);
		//System.out.println(destFile);
		
		File f = new File (destFile);
		new File(f.getParent()).mkdirs();
		
		try(InputStream input = new FileInputStream(sourceFile);
				OutputStream output = new FileOutputStream(destFile)){
			byte[] buf = new byte[8192]; 
			int len;
			while((len=input.read(buf)) >= 0) 
				output.write(buf, 0, len); 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	public static void copyAndDelete(String sourceFile, String destFile) throws IOException { 
		copy(sourceFile, destFile);
		new File(sourceFile).delete();
	}
	
	public static void copy(InputStream sourceFile, String destFile) throws IOException { 
		//System.out.println(sourceFile);
		//System.out.println(destFile);
		/*try(InputStream input = new FileInputStream(sourceFile);
				OutputStream output = new FileOutputStream(destFile)){*/
		try(InputStream input = sourceFile;
				OutputStream output = new FileOutputStream(destFile)){
			byte[] buf = new byte[8192]; 
			int len;
			while((len=input.read(buf)) >= 0) 
				output.write(buf, 0, len); 
		} 
	}
	
	public static void addIntegerAttribute2PolygonShapefile(String shapefile, String attribute, Integer value){
		
		String name = shapefile.replace(".shp", "").replace("SHP", "");
		String output = name+"_temp";
		
		try(FileOutputStream fos = new FileOutputStream(output+".dbf");
				FileOutputStream shp = new FileOutputStream(output+".shp");
				FileOutputStream shx = new FileOutputStream(output+".shx");){
			
			ShpFiles sf = new ShpFiles(name+".shp");
			ShapefileReader sfr = new ShapefileReader(sf, true, false, new org.locationtech.jts.geom.GeometryFactory());
			DbaseFileReader dfr = new DbaseFileReader(sf, true, Charset.defaultCharset());
			DbaseFileHeader inHeader = dfr.getHeader(); 
			
			// gestion du header de sortie
			DbaseFileHeader outHeader = new DbaseFileHeader();
			outHeader.setNumRecords(inHeader.getNumRecords());
			
			for(int i=0; i<inHeader.getNumFields(); i++){
				outHeader.addColumn(inHeader.getFieldName(i), inHeader.getFieldType(i), inHeader.getFieldLength(i), 0);
			}
			outHeader.addColumn(attribute, 'N', 6, 0);
			
			DbaseFileWriter dfw = new DbaseFileWriter(outHeader, fos.getChannel());
			ShapefileWriter sfw = new ShapefileWriter(shp.getChannel(), shx.getChannel());
			
			sfw.writeHeaders(
					new Envelope(sfr.getHeader().minX(), sfr.getHeader().maxX(), sfr.getHeader().minY(), sfr.getHeader().maxY()), 
					ShapeType.POLYGON, dfr.getHeader().getNumRecords(), 1000000);
			
			Object[] data, entry = new Object[outHeader.getNumFields()];
			
			while(sfr.hasNext()){
				
				data = dfr.readEntry();
				
				for(int i=0; i<inHeader.getNumFields(); i++){
					entry[i] = data[i];
				}
				entry[inHeader.getNumFields()] = value;
				
				
				Geometry g = (Geometry) sfr.nextRecord().shape();
				sfw.writeGeometry(g);
				dfw.write(entry);
			}
			
			sfr.close();
			dfr.close();
			dfw.close();
			sfw.close();
			
			copyAndDelete(output+".shp", name+".shp");
			copyAndDelete(output+".dbf", name+".dbf");
			copyAndDelete(output+".shx", name+".shx");
			
		} catch (ShapefileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			copy(name+".prj", output+".prj");
		}
	}
	
	public static void addIntegerAttribute2MultilineShapefile(String shapefile, String attribute, Integer value){
		
		String name = shapefile.replace(".shp", "").replace("SHP", "");
		String output = name+"_temp";
		
		try(FileOutputStream fos = new FileOutputStream(output+".dbf");
				FileOutputStream shp = new FileOutputStream(output+".shp");
				FileOutputStream shx = new FileOutputStream(output+".shx");){
			
			ShpFiles sf = new ShpFiles(name+".shp");
			ShapefileReader sfr = new ShapefileReader(sf, true, false, new org.locationtech.jts.geom.GeometryFactory());
			DbaseFileReader dfr = new DbaseFileReader(sf, true, Charset.defaultCharset());
			DbaseFileHeader inHeader = dfr.getHeader(); 
			
			// gestion du header de sortie
			DbaseFileHeader outHeader = new DbaseFileHeader();
			outHeader.setNumRecords(inHeader.getNumRecords());
			
			for(int i=0; i<inHeader.getNumFields(); i++){
				outHeader.addColumn(inHeader.getFieldName(i), inHeader.getFieldType(i), inHeader.getFieldLength(i), 0);
			}
			outHeader.addColumn(attribute, 'N', 6, 0);
			
			DbaseFileWriter dfw = new DbaseFileWriter(outHeader, fos.getChannel());
			ShapefileWriter sfw = new ShapefileWriter(shp.getChannel(), shx.getChannel());
			
			sfw.writeHeaders(
					new Envelope(sfr.getHeader().minX(), sfr.getHeader().maxX(), sfr.getHeader().minY(), sfr.getHeader().maxY()), 
					ShapeType.ARC, dfr.getHeader().getNumRecords(), 1000000);
			
			Object[] data, entry = new Object[outHeader.getNumFields()];
			
			while(sfr.hasNext()){
				
				data = dfr.readEntry();
				
				for(int i=0; i<inHeader.getNumFields(); i++){
					entry[i] = data[i];
				}
				entry[inHeader.getNumFields()] = value;
				
				
				Geometry g = (Geometry) sfr.nextRecord().shape();
				sfw.writeGeometry(g);
				dfw.write(entry);
			}
			
			sfr.close();
			dfr.close();
			dfw.close();
			sfw.close();
			
			copyAndDelete(output+".shp", name+".shp");
			copyAndDelete(output+".dbf", name+".dbf");
			copyAndDelete(output+".shx", name+".shx");
			
		} catch (ShapefileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			copy(name+".prj", output+".prj");
		}
	}
	
	public static void symplifyMultiline(String shapefile, double tolerance, double maxSize){
		
		String name = shapefile.replace(".shp", "").replace("SHP", "");
		String output = name+"_simplified";
		
		try(FileOutputStream fos = new FileOutputStream(output+".dbf");
				FileOutputStream shp = new FileOutputStream(output+".shp");
				FileOutputStream shx = new FileOutputStream(output+".shx")){
			
			ShpFiles sf = new ShpFiles(name+".shp");
			ShapefileReader sfr = new ShapefileReader(sf, true, false, new org.locationtech.jts.geom.GeometryFactory());
			DbaseFileReader dfr = new DbaseFileReader(sf, true, Charset.defaultCharset());
			DbaseFileHeader inHeader = dfr.getHeader();
			
			Set<LineString> lines = new HashSet<LineString>();
			while(sfr.hasNext()){
				Object[] data = dfr.readEntry();
				Geometry g = (Geometry) sfr.nextRecord().shape();
				
				if(g instanceof MultiLineString){
					MultiLineString mls = (MultiLineString) g;
					for(int i=0; i<mls.getNumGeometries(); i++){
						LineString ls = (LineString) mls.getGeometryN(i);
						LineString lss = (LineString) DouglasPeuckerSimplifier.simplify(ls, tolerance);
						lss.setUserData(data);
						lines.add(lss);
					}
				}else if(g instanceof LineString){
					LineString ls = (LineString) g;
					
					LineString lss = (LineString) DouglasPeuckerSimplifier.simplify(ls, tolerance);
					lss.setUserData(data);
					lines.add(lss);
				}				
			}
			
			Set<LineString> lines2 = new HashSet<LineString>();
			for(LineString ls : lines){
				if(ls.getLength() > maxSize){
					double total = 0;
					Coordinate[] cs = ls.getCoordinates();
					List<Coordinate> cl = new ArrayList<Coordinate>();
					Coordinate c1 = cs[0];
					cl.add(c1);
					for(int ic=1; ic<cs.length-1; ic++){
						Coordinate c2 = cs[ic];
						cl.add(c2);
						total += Math.sqrt(Math.pow(c1.x - c2.x, 2) + Math.pow(c1.y - c2.y, 2));
						c1 = c2;
						if(total > maxSize){
							LineString ls2 = new LineString(new CoordinateArraySequence(cl.toArray(new Coordinate[cl.size()])), ls.getFactory());
							ls2.setUserData(ls.getUserData());
							lines2.add(ls2);
							cl.clear();
							cl.add(c1);
							total = 0;
						}
					}
					Coordinate c2 = cs[cs.length-1];
					cl.add(c2);
					LineString ls2 = new LineString(new CoordinateArraySequence(cl.toArray(new Coordinate[cl.size()])), ls.getFactory());
					ls2.setUserData(ls.getUserData());
					lines2.add(ls2);
				}else{
					lines2.add(ls);
				}
			}
			
			
			// gestion du header de sortie
			DbaseFileHeader outHeader = new DbaseFileHeader();
			outHeader.setNumRecords(lines2.size());
			
			for(int i=0; i<inHeader.getNumFields(); i++){
				outHeader.addColumn(inHeader.getFieldName(i), inHeader.getFieldType(i), inHeader.getFieldLength(i), 0);
			}
			
			DbaseFileWriter dfw = new DbaseFileWriter(outHeader, fos.getChannel());
			ShapefileWriter sfw = new ShapefileWriter(shp.getChannel(), shx.getChannel());
			
			sfw.writeHeaders(
					new Envelope(sfr.getHeader().minX(), sfr.getHeader().maxX(), sfr.getHeader().minY(), sfr.getHeader().maxY()), 
					ShapeType.ARC, lines2.size(), 1000000);
						
			MultiLineString mls;
			for(LineString ls : lines2){
				mls = new MultiLineString(new LineString[]{ls}, ls.getFactory());
				sfw.writeGeometry(mls);
				dfw.write((Object[]) ls.getUserData());
			}
			
			sfr.close();
			dfr.close();
			dfw.close();
			sfw.close();
			
			
			copyAndDelete(output+".shp", name+".shp");
			copyAndDelete(output+".dbf", name+".dbf");
			copyAndDelete(output+".shx", name+".shx");
			
		} catch (ShapefileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			copy(name+".prj", output+".prj");
		}
	}
}
