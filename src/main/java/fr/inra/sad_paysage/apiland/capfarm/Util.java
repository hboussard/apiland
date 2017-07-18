package fr.inra.sad_paysage.apiland.capfarm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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

	private final static String path = "C:/Hugues/agents/helene/test2/";
	
	public static void main(String[] args) {
		
		//preparedCoversAndNextCovers(path+"test.xlsm", path+"essai/");
		
		//generateShapefile(path+"Yann_Corbeau", "YC", 30);
		//generateHistoric(path+"Yann_Corbeau_cfm_land", "id", "cov_2017");
		
		//generateShapefile(path+"bergerie");
		//generateHistoric(path+"bergerie_cfm", "id", "os16", "os17");
	}
	
	public static void preparedCoversAndNextCovers(String input, String output){
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(new File(input));

			XSSFSheet sheetCovers = workbook.getSheet("cultures");
			Map<String, String> genericCovers = new TreeMap<String, String>();
			for(Row r : sheetCovers){
				if(r.getRowNum() > 1){
					//System.out.println(r.getCell(0).getStringCellValue()+" "+r.getCell(1).getStringCellValue());
					genericCovers.put(r.getCell(1).getStringCellValue(), r.getCell(0).getStringCellValue());
				}
			}
			
			XSSFSheet sheet = workbook.getSheet("suivants-txt");
			
			// récupération des cultures d'interet
			List<String> covers = new ArrayList<String>();
			for(Cell c : sheet.getRow(0)){
				String cover = c.getStringCellValue();
				if(genericCovers.keySet().contains(cover)){
					//System.out.println(cover+" ");
					covers.add(cover);
				}
			}
			boolean[][] next = new boolean[covers.size()][covers.size()];
		
			// récupération des précédents-suivants
			for(int j=0; j<covers.size(); j++){
				Row r = sheet.getRow(j+1);
				for(int i=0; i<covers.size(); i++){
					System.out.print(r.getCell(i+1).toString()+" ");
					next[j][i] = new Boolean(r.getCell(i+1).toString());
				}
				System.out.println();
			}
			
			workbook.close();
			
			File out = new File(output);
			out.mkdirs();
			
			// écriture du fichier de couverts
			CsvWriter cw = new CsvWriter(output+"covers.txt");
			cw.setDelimiter(';');
			cw.write("code");
			cw.write("name");
			cw.endRecord();
			for(String cover : covers){
				cw.write(genericCovers.get(cover));
				cw.write(cover);
				cw.endRecord();
			}
			cw.close();
			
			// écriture du fichier précédents-suivants
			CsvWriter cw2 = new CsvWriter(output+"next_covers.txt");
			cw2.setDelimiter(';');
			cw2.write("previous");
			for(String cover : covers){
				cw2.write(genericCovers.get(cover));
			}
			cw2.endRecord();
			for(int j=0; j<covers.size(); j++){
				cw2.write(genericCovers.get(covers.get(j)));
				for(int i=0; i<covers.size(); i++){
					if(next[j][i]){
						cw2.write("1");
					}else{
						cw2.write("0");
					}
				}
				cw2.endRecord();
			}
			cw2.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (FinalizedException e) {
			e.printStackTrace();
		}
	}
	
	public static void observeShapefile(String input){
		String output = input;
		
		try{
			
			ShpFiles sf = new ShpFiles(input+".shp");
			ShapefileReader sfr = new ShapefileReader(sf, true, false, new com.vividsolutions.jts.geom.GeometryFactory());
			DbaseFileReader dfr = new DbaseFileReader(sf, true, Charset.defaultCharset());
			
			sfr.close();
			dfr.close();
		} catch (ShapefileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			copy(input+".prj", output+".prj");
		}
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
	
	public static void generateShapefile(String input, String codeFarm){
		generateShapefile(input, codeFarm, 0);
	}
	
	public static void generateShapefile(String input, String codeFarm, int startIndex){
		
		String output = input+"_cfm";
		
		try(FileOutputStream fos = new FileOutputStream(output+".dbf");
				FileOutputStream shp = new FileOutputStream(output+".shp");
				FileOutputStream shx = new FileOutputStream(output+".shx");){
			
			ShpFiles sf = new ShpFiles(input+".shp");
			ShapefileReader sfr = new ShapefileReader(sf, true, false, new com.vividsolutions.jts.geom.GeometryFactory());
			DbaseFileReader dfr = new DbaseFileReader(sf, true, Charset.defaultCharset());
			
			// entete du fichier shape en entrée
			Set<String> entete = new TreeSet<String>();
			
			for(int i=0; i<dfr.getHeader().getNumFields(); i++){
				entete.add(dfr.getHeader().getFieldName(i));
			}
			
			// gestion du header de sortie
			DbaseFileHeader header = new DbaseFileHeader();
			header.setNumRecords(dfr.getHeader().getNumRecords());
			
			header.addColumn("id", 'c', 8, 0);
			header.addColumn("area", 'c', 2, 0);
			header.addColumn("type", 'c', 8, 0);
			header.addColumn("facility", 'c', 20, 0);
			header.addColumn("farm", 'c', 8, 0);
				
			// ajouter les champs utilisateurs
			// et les observer leurs caractéristiques pour utilisation en contraintes
			for(String e : entete){
				header.addColumn(e, 'c', 5, 0);
			}

			
			DbaseFileWriter dfw = new DbaseFileWriter(header, fos.getChannel());
			ShapefileWriter sfw = new ShapefileWriter(shp.getChannel(), shx.getChannel());
			
			sfw.writeHeaders(
					new Envelope(sfr.getHeader().minX(), sfr.getHeader().maxX(), sfr.getHeader().minY(), sfr.getHeader().maxY()), 
					ShapeType.POLYGON, dfr.getHeader().getNumRecords(), 1000000);
			
			Object[] data, entry = new Object[header.getNumFields()];
			int id = startIndex;
			while(sfr.hasNext()){
				
				data = dfr.readEntry();
				
				entry[0] = id++; // id
				entry[1] = "AA"; // area
				entry[2] = "parcel"; // type
				entry[3] = ""; // facility
				entry[4] = codeFarm; // farm
				for(int i=5; i<header.getNumFields(); i++){
					entry[i] = data[i-5];
				}
				
				Geometry g = (Geometry) sfr.nextRecord().shape();
				sfw.writeGeometry(g);
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
