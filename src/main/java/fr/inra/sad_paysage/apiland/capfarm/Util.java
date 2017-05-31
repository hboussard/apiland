package fr.inra.sad_paysage.apiland.capfarm;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.geotools.data.shapefile.dbf.DbaseFileHeader;
import org.geotools.data.shapefile.dbf.DbaseFileReader;
import org.geotools.data.shapefile.dbf.DbaseFileWriter;
import org.geotools.data.shapefile.files.ShpFiles;
import org.geotools.data.shapefile.shp.ShapeType;
import org.geotools.data.shapefile.shp.ShapefileException;
import org.geotools.data.shapefile.shp.ShapefileReader;
import org.geotools.data.shapefile.shp.ShapefileWriter;

import com.csvreader.CsvWriter;
import com.csvreader.CsvWriter.FinalizedException;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

public class Util {

	private final static String path = "C:/Hugues/modelisation/capfarm/methodo/data/sig/";
	
	public static void main(String[] args) {
		
		generateShapefile(path+"bergerie");
		generateHistoric(path+"bergerie_cfm", "id", "os16", "os17");
	}
	
	public static void oberveShapefile(String input, String colCover){
		
	}
	
	public static void generateShapefile(String input){
		generateShapefile(input, null, null, null, null, null);
	}

	
	public static void generateHistoric(String input, String colId, String... colOs){
		try{
			ShpFiles sf = new ShpFiles(input+".shp");
			DbaseFileReader dfr = new DbaseFileReader(sf, true, Charset.defaultCharset());
			
			int posId = -1;
			Map<String, Integer> pos = new HashMap<String, Integer>();
			for(int i=0; i<dfr.getHeader().getNumFields(); i++){
				if(dfr.getHeader().getFieldName(i).equalsIgnoreCase(colId)){
					posId = i;
				}
				for(String col : colOs){
					if(dfr.getHeader().getFieldName(i).equalsIgnoreCase(col)){
						pos.put(col,  i);
					}
				}
			}
			
			CsvWriter cw = new CsvWriter(input+"_historic.csv");
			cw.setDelimiter(';');
			cw.write("parcel");
			cw.write("historic");
			cw.endRecord();
			
			Object[] entry;
			while(dfr.hasNext()){
				entry = dfr.readEntry();
				cw.write(entry[posId]+"");
				
				StringBuffer sb = new StringBuffer();
				for(String col : colOs){
					sb.append(entry[pos.get(col)]);
					sb.append('-');
				}
				sb.deleteCharAt(sb.length()-1);
				cw.write(sb.toString());
				cw.endRecord();
			}
			
			cw.close();
			dfr.close();
		} catch (ShapefileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (FinalizedException e) {
			e.printStackTrace();
		}
	}
	
	public static void generateShapefile(String input, String colId, String colArea, String colType, String colFacility, String colFarm){
		
		String output = input+"_cfm";
		
		try(FileOutputStream fos = new FileOutputStream(output+".dbf");
				FileOutputStream shp = new FileOutputStream(output+".shp");
				FileOutputStream shx = new FileOutputStream(output+".shx");){
			
			ShpFiles sf = new ShpFiles(input+".shp");
			ShapefileReader sfr = new ShapefileReader(sf, true, false, new com.vividsolutions.jts.geom.GeometryFactory());
			DbaseFileReader dfr = new DbaseFileReader(sf, true, Charset.defaultCharset());
			
			
			DbaseFileHeader header = new DbaseFileHeader();
			
			Set<String> entete = new TreeSet<String>();
			for(int i=0; i<dfr.getHeader().getNumFields(); i++){
				entete.add(dfr.getHeader().getFieldName(i));
			}
			
			// gestion de "ID"
			if(colId == null){
				if(entete.contains("id") || entete.contains("ID") || entete.contains("Id")){ 
					// ou bien s'il est déclaré sous un autre nom...
					
					// vérifier le format --> STRING (ou numeric ?)
					// vérfifier l'identification
				}else{
					// à créer à partir d'un nombre initial (par défaut = 0)
					
					header.addColumn("id", 'c', 8, 0);
				}
			}else{
				
			}
			
			
			// gestion de "AREA"
			if(entete.contains("area")){// gérer la casse
				// ou bien s'il est déclaré sous un autre nom...
				
				// vérifier le format --> STRING sur 2 caractères
				// vérfifier le domaine "AA" ou "TA"
			}else{
				// à créer (par défaut = 'AA')
				header.addColumn("area", 'c', 2, 0);
			}
			
			// gestion de "TYPE"
			if(entete.contains("type")){// gérer la casse
				// ou bien s'il est déclaré sous un autre nom...
				
				// vérifier le format --> STRING sur 8 caractères
				// vérfifier le domaine "parcel" ou "facility"
			}else{
				// à créer (par défaut = 'parcel')
				header.addColumn("type", 'c', 8, 0);
			}
			
			// gestion de "facility"
			if(entete.contains("facility")){// gérer la casse
				// ou bien s'il est déclaré sous un autre nom...
				
				// vérifier le format --> STRING sur 20 caractères
				// vérfifier le domaine "parcel" ou "facility"
			}else{
				// à créer (par défaut = '')
				header.addColumn("facility", 'c', 20, 0);
			}

			// gestion de "FARM"
			if(entete.contains("farm")){// gérer la casse
				// ou bien s'il est déclaré sous un autre nom...
				
				// vérifier le format --> STRING
			}else{
				// à créer (par défaut = en générer un unique pour tous --> paramètres)
				
				header.addColumn("farm", 'c', 8, 0);
			}
				
			// ajouter les champs utilisateurs
			// et les observer leurs caractéristiques pour utilisation en contraintes
			
			for(String e : entete){
				header.addColumn(e, 'c', 5, 0);
			}
					
			//dfw.getHeader().addColumn(arg0, arg1, arg2, arg3);

			header.setNumRecords(dfr.getHeader().getNumRecords());
			
			DbaseFileWriter dfw = new DbaseFileWriter(header, fos.getChannel());
			ShapefileWriter sfw = new ShapefileWriter(shp.getChannel(), shx.getChannel());
			
			sfw.writeHeaders(
					new Envelope(sfr.getHeader().minX(), sfr.getHeader().maxX(), sfr.getHeader().minY(), sfr.getHeader().maxY()), 
					ShapeType.POLYGON, dfr.getHeader().getNumRecords(), sfr.getHeader().getFileLength());
			
			Object[] data, entry = new Object[header.getNumFields()];
			int id = 0;
			while(sfr.hasNext()){
				
				data = dfr.readEntry();
				
				entry[0] = id++; // id
				entry[1] = "AA"; // area
				entry[2] = "parcel"; // type
				entry[3] = ""; // facility
				entry[4] = "BN"; // farm
				entry[5] = data[0];
				entry[6] = data[1];
				
				sfw.writeGeometry((Geometry) sfr.nextRecord().shape());
				dfw.write(entry);
			}
			
			sfr.close();
			dfr.close();
			dfw.close();
			sfw.close();
		} catch (ShapefileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			copy(input+".prj", output+".prj");
		}
	}
	
	private static void copy(String sourceFile, String destFile){ 
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
	
}
