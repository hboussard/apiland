package fr.inra.sad_paysage.apiland.capfarm;

import java.io.File;
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
		
		//generateShapefile(path+"Yann_Corbeau", "YC", 30);
		//generateHistoric(path+"Yann_Corbeau_cfm_land", "id", "cov_2017");
		
		//generateShapefile(path+"bergerie");
		//generateHistoric(path+"bergerie_cfm", "id", "os16", "os17");
	}
	
	public static void preparedCoversAndNextCovers(String input){
		try {
			FileInputStream file = new FileInputStream(new File(path+"precedent-suivant.xlsm"));

			//Get the workbook instance for XLS file 
			XSSFWorkbook workbook = new XSSFWorkbook (file);

			//Get first sheet from the workbook
			XSSFSheet sheet = workbook.getSheetAt(2);
			
			for(Row r : sheet){
				System.out.print(r.getRowNum()+" : ");
				for(Cell c : r){
					
					if(c.getCellType() == Cell.CELL_TYPE_FORMULA){
						System.out.print(c.getStringCellValue()+" ");
					}else{
						System.out.print(c.toString()+" ");
					}
					
				}
				System.out.println();
			}
			workbook.close();
			
		} catch (IOException e) {
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
			
			// entete du fichier shape en entr�e
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
			// et les observer leurs caract�ristiques pour utilisation en contraintes
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
