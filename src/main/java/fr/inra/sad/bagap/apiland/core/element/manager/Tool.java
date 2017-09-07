package fr.inra.sad.bagap.apiland.core.element.manager;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.geotools.data.shapefile.dbf.DbaseFileHeader;
import org.geotools.data.shapefile.dbf.DbaseFileReader;
import org.geotools.data.shapefile.dbf.DbaseFileWriter;
import org.geotools.data.shapefile.files.ShpFiles;
import org.geotools.data.shapefile.shp.ShapeType;
import org.geotools.data.shapefile.shp.ShapefileException;
import org.geotools.data.shapefile.shp.ShapefileReader;
import org.geotools.data.shapefile.shp.ShapefileWriter;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

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
	
	public static void formatShapeFile(String input, String output){
		try(FileOutputStream fos = new FileOutputStream(output+".dbf");
				FileOutputStream shp = new FileOutputStream(output + ".shp");
				FileOutputStream shx = new FileOutputStream(output + ".shx");){
			
			ShpFiles sf = new ShpFiles(input+".shp");
			ShapefileReader sfr = new ShapefileReader(sf, true, false, new com.vividsolutions.jts.geom.GeometryFactory());
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
			try {
				copy(input+".prj", output+".prj");
			} catch (IOException e) {
				e.printStackTrace();
			}
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
			ShapefileReader sfr = new ShapefileReader(sf, true, false, new com.vividsolutions.jts.geom.GeometryFactory());
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
			try {
				copy(input+".prj", output+".prj");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void copy(String sourceFile, String destFile) throws IOException { 
		//System.out.println(sourceFile);
		//System.out.println(destFile);
		try(InputStream input = new FileInputStream(sourceFile);
				OutputStream output = new FileOutputStream(destFile)){ 
			byte[] buf = new byte[8192]; 
			int len;
			while((len=input.read(buf)) >= 0) 
				output.write(buf, 0, len); 
		} 
	}
	
}
