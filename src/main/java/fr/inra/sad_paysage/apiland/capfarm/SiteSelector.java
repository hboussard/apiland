package fr.inra.sad_paysage.apiland.capfarm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
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

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

public class SiteSelector {
	/*
	private static String path = "C:/Hugues/projets/agriconnect/article/article_FSD/data/ouvert/"; // chemin d'accès au dossier
	private static String input = path+"site_ouvert"; // nom de couche
	private static String attributEA = "FARM"; // nom de l'attribut "exploitation"
	private static String name = "site_ouvert_1km"; // nom du fichier de sortie
	private static String attributCommune = "COMMUNE_IL"; // nom de l'attribut "commune"
	private static double codeCommune = 35104; // code commune
	*/
	private static String path;
	private static String input;
	private static String attributEA = "FARM"; // nom de l'attribut "exploitation"
	private static String name = "site_ouvert_1km"; // nom du fichier de sortie
	private static String attributCommune = "COMMUNE_IL"; // nom de l'attribut "commune"
	private static double codeCommune = 35104; // code commune
	
	
	private static double x;
	private static double y;
	
	// Agriconnect site ouvert
	//private static double x = 367883.4587;
	//private static double y = 6783447.113;
	
	// Agriconnect site fermé
	//private static double x = 357770.2965;
	//private static double y = 6830159.9776;
	
	// gester 31 (Haute Garonne)
	//private static double x = 578354;
	//private static double y = 6256011;
	
	// gester 36 (Indre)
	//private static double x = 594794 
	//private static double y = 6652998
	
	private static double rayon = 500;
	
	private static double minX, maxX, minY, maxY;
	
	private static int fileLength;
	
	private static DbaseFileHeader header;
	
	
	
	public static String getPath() {
		return path;
	}

	public static void setPath(String path) {
		SiteSelector.path = path;
	}

	public static String getInput() {
		return input;
	}

	public static void setInput(String input) {
		SiteSelector.input = input;
	}

	public static String getAttributEA() {
		return attributEA;
	}

	public static void setAttributEA(String attributEA) {
		SiteSelector.attributEA = attributEA;
	}

	public static String getName() {
		return name;
	}

	public static void setName(String name) {
		SiteSelector.name = name;
	}

	public static String getAttributCommune() {
		return attributCommune;
	}

	public static void setAttributCommune(String attributCommune) {
		SiteSelector.attributCommune = attributCommune;
	}

	public static double getCodeCommune() {
		return codeCommune;
	}

	public static void setCodeCommune(double codeCommune) {
		SiteSelector.codeCommune = codeCommune;
	}

	public static double getRayon() {
		return rayon;
	}

	public static void setRayon(double rayon) {
		SiteSelector.rayon = rayon;
	}

	public static double getMinX() {
		return minX;
	}

	public static void setMinX(double minX) {
		SiteSelector.minX = minX;
	}

	public static double getMaxX() {
		return maxX;
	}

	public static void setMaxX(double maxX) {
		SiteSelector.maxX = maxX;
	}

	public static double getMinY() {
		return minY;
	}

	public static void setMinY(double minY) {
		SiteSelector.minY = minY;
	}

	public static double getMaxY() {
		return maxY;
	}

	public static void setMaxY(double maxY) {
		SiteSelector.maxY = maxY;
	}

	public static int getFileLength() {
		return fileLength;
	}

	public static void setFileLength(int fileLength) {
		SiteSelector.fileLength = fileLength;
	}

	public static DbaseFileHeader getHeader() {
		return header;
	}

	public static void setHeader(DbaseFileHeader header) {
		SiteSelector.header = header;
	}

	public static void main(String[] args){
		
		//getEAFromBuffer(); // à lancer après avoir modifier tes paramètres
	}
	
	private static void getEAFromBuffer(){
		Set<String> eas = getExploitationsFromBuffer();
		Set<Polygon> ilots = getIlots(eas);
		writeIlots(ilots);
		exportBuffer();
	}
	
	private static void exportBuffer() {
		String output = path+"buffer_31";
		try(FileOutputStream fos = new FileOutputStream(output+".dbf");
				FileOutputStream shp = new FileOutputStream(output + ".shp");
				FileOutputStream shx = new FileOutputStream(output + ".shx");){
			
			DbaseFileHeader h = new DbaseFileHeader();
			h.setNumRecords(1);
			DbaseFileWriter dfw = new DbaseFileWriter(h, fos.getChannel());
			ShapefileWriter sfw = new ShapefileWriter(shp.getChannel(), shx.getChannel());
			sfw.writeHeaders(new Envelope(minX, maxX, minY, maxY), ShapeType.POLYGON, 1, fileLength);
			
			WKTReader wkt = new WKTReader();
			Polygon zone = (Polygon) ((Point) wkt.read("POINT ("+x+" "+y+")")).buffer(rayon);
			sfw.writeGeometry(zone);
			dfw.write(new Object[0]);
			
			dfw.close();
			sfw.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} 
		copyFile(input+".prj", output+".prj");
	}

	private static void getEAFromCommune(){
		Set<String> eas = getExploitationsFromCode();
		Set<Polygon> ilots = getIlots(eas);
		writeIlots(ilots);
	}
	
	private static Set<String> getExploitationsFromCode(){
		Set<String> eas = new TreeSet<String>();
		
		try {
			ShpFiles sf = new ShpFiles(input+".shp");
			ShapefileReader sfr = new ShapefileReader(sf, true, false, new com.vividsolutions.jts.geom.GeometryFactory());
			DbaseFileReader dfr = new DbaseFileReader(sf, true, Charset.defaultCharset());
			header = dfr.getHeader();
			
			minX = sfr.getHeader().minX();
			maxX = sfr.getHeader().maxX();
			minY = sfr.getHeader().minY();
			maxY = sfr.getHeader().maxY();
			fileLength = sfr.getHeader().getFileLength();
			
			int indexEA = -1, indexCommune = -1;
			for(int i=0; i<header.getNumFields(); i++){
				if(header.getFieldName(i).equalsIgnoreCase(attributEA)){
					indexEA = i;
				}
				if(header.getFieldName(i).equalsIgnoreCase(attributCommune)){
					indexCommune = i;
				}
			}
			
			Object[] entry;
			while(sfr.hasNext()){
				Polygon p = (Polygon) ((MultiPolygon) sfr.nextRecord().shape()).getGeometryN(0);
				entry = dfr.readEntry();
				if(((double) entry[indexCommune]) == codeCommune){
					eas.add((String) entry[indexEA]);
				}
			}
			
			sfr.close();
			dfr.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ShapefileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return eas;
	}
	
	private static Set<String> getExploitationsFromBuffer(){
		Set<String> eas = new TreeSet<String>();
		
		try {
			ShpFiles sf = new ShpFiles(input+".shp");
			ShapefileReader sfr = new ShapefileReader(sf, true, false, new com.vividsolutions.jts.geom.GeometryFactory());
			DbaseFileReader dfr = new DbaseFileReader(sf, true, Charset.defaultCharset());
			header = dfr.getHeader();
			minX = sfr.getHeader().minX();
			maxX = sfr.getHeader().maxX();
			minY = sfr.getHeader().minY();
			maxY = sfr.getHeader().maxY();
			
			fileLength = sfr.getHeader().getFileLength();
			
			int indexEA = -1;
			for(int i=0; i<header.getNumFields(); i++){
				if(header.getFieldName(i).equalsIgnoreCase(attributEA)){
					indexEA = i;
				}
			}
			
			WKTReader wkt = new WKTReader();
			Polygon zone = (Polygon) ((Point) wkt.read("POINT ("+x+" "+y+")")).buffer(rayon);
			/*
			minX = zone.getEnvelopeInternal().getMinX();
			maxX = zone.getEnvelopeInternal().getMaxX();
			minY = zone.getEnvelopeInternal().getMinY();
			maxY = zone.getEnvelopeInternal().getMaxY();
			*/
			Object[] entry;
			while(sfr.hasNext()){
				Polygon p = (Polygon) ((MultiPolygon) sfr.nextRecord().shape()).getGeometryN(0);
				entry = dfr.readEntry();
				if(p.intersects(zone)){
					//eas.add((String) entry[indexEA]);
					eas.add(entry[indexEA]+"");
				}
			}
			
			sfr.close();
			dfr.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ShapefileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return eas;
	}
	
	private static Set<Polygon> getIlots(Set<String> eas){
		Set<Polygon> ilots = new TreeSet<Polygon>();
	
		try {
			ShpFiles sf = new ShpFiles(input+".shp");
			ShapefileReader sfr = new ShapefileReader(sf, true, false, new com.vividsolutions.jts.geom.GeometryFactory());
			DbaseFileReader dfr = new DbaseFileReader(sf, true, Charset.defaultCharset());
			
			int indexEA = -1;
			for(int i=0; i<header.getNumFields(); i++){
				if(header.getFieldName(i).equalsIgnoreCase(attributEA)){
					indexEA = i;
					break;
				}
			}
			
			Object[] entry;
			while(sfr.hasNext()){
				Polygon p = (Polygon) ((MultiPolygon) sfr.nextRecord().shape()).getGeometryN(0);
				entry = dfr.readEntry();
				//if(eas.contains(((String) entry[indexEA]))){
				if(eas.contains(entry[indexEA]+"")){
					p.setUserData(entry);
					ilots.add(p);
				}
			}
			
			sfr.close();
			dfr.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ShapefileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return ilots;
	}
	
	private static void writeIlots(Set<Polygon> ilots){
		String output = path+name;
		try(FileOutputStream fos = new FileOutputStream(output+".dbf");
				FileOutputStream shp = new FileOutputStream(output + ".shp");
				FileOutputStream shx = new FileOutputStream(output + ".shx");){
			
			header.setNumRecords(ilots.size());
			DbaseFileWriter dfw = new DbaseFileWriter(header, fos.getChannel());
			ShapefileWriter sfw = new ShapefileWriter(shp.getChannel(), shx.getChannel());
			sfw.writeHeaders(new Envelope(minX, maxX, minY, maxY), ShapeType.POLYGON, ilots.size(), fileLength);
			System.out.println(fileLength);
			
			for(Polygon p : ilots){
				sfw.writeGeometry(p);
				dfw.write((Object[]) p.getUserData());
			}
			
			dfw.close();
			sfw.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		copyFile(input+".prj", output+".prj");
	}
	
	public static boolean copyFile(String source, String dest){
		try{
			// Declaration et ouverture des flux
			java.io.FileInputStream sourceFile = new java.io.FileInputStream(new File(source));
	 
			try{
				java.io.FileOutputStream destinationFile = null;
	 
				try{
					destinationFile = new FileOutputStream(new File(dest));
	 
					// Lecture par segment de 0.5Mo 
					byte buffer[] = new byte[512 * 1024];
					int nbLecture;
					
					while ((nbLecture = sourceFile.read(buffer)) != -1){
						destinationFile.write(buffer, 0, nbLecture);
					}
				} finally {
					destinationFile.close();
				}
			} finally {
				sourceFile.close();
			}
		} catch (IOException e){
			e.printStackTrace();
			return false; // Erreur
		}
		return true; // Résultat OK  
	}
	
}
