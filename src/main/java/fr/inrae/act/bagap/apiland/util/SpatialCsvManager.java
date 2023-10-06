package fr.inrae.act.bagap.apiland.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import au.com.bytecode.opencsv.CSVReader;
import fr.inra.sad.bagap.apiland.core.element.manager.DynamicLayerFactory;
import fr.inra.sad.bagap.apiland.core.element.manager.Tool;
import fr.inra.sad.bagap.apiland.core.space.CoordinateManager;
import fr.inra.sad.bagap.apiland.core.space.Point;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.RefPoint;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.MatrixManager;
import fr.inrae.act.bagap.raster.CoverageManager;
import fr.inrae.act.bagap.raster.EnteteRaster;

import org.jumpmind.symmetric.csv.CsvReader;
import org.jumpmind.symmetric.csv.CsvWriter;

public class SpatialCsvManager {
	
	public static void merge(String outputCsv, String[] inputCsv, Set<String> ids){
		
		try {
			CsvWriter cw = new CsvWriter(outputCsv);
			cw.setDelimiter(';');
			for(String id : ids){
				cw.write(id);
			}
			
			Map<CsvReader, Set<Integer>> readers = new HashMap<CsvReader, Set<Integer>>();
			CsvReader reader;
			Set<Integer> index;
			for(String icsv : inputCsv){
				
				reader = new CsvReader(icsv);
				reader.setDelimiter(';');
				reader.readHeaders();
				
				index = new HashSet<Integer>();
				for(int c=0; c<reader.getHeaderCount(); c++){
					if(!ids.contains(reader.getHeader(c))){
						index.add(c);
					}
				}
				readers.put(reader, index);	
			}
			
			for(Entry<CsvReader, Set<Integer>> r : readers.entrySet()){
				for(int ind : r.getValue()){
					cw.write(r.getKey().getHeader(ind));
				}
			}
			cw.endRecord();
			
			boolean again = true;
			boolean first;
			while(again){
				first = true;
				for(Entry<CsvReader, Set<Integer>> r : readers.entrySet()){
					
					if(r.getKey().readRecord()){
						if(first){
							for(String id : ids){
								cw.write(r.getKey().get(id));
							}
							first = false;
						}
						
						for(int i : r.getValue()){
							cw.write(r.getKey().get(i));
						}
					}else{
						again = false;
					}
				}
				cw.endRecord();
			}
						
			cw.close();
			for(CsvReader r : readers.keySet()){
				r.close();
			}
			readers.clear();
			readers = null;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	public static void mergeXY(String outputCsv, String[] inputCsv, String idX, String idY, EnteteRaster entete){
		
		try {
			CsvWriter cw = new CsvWriter(outputCsv);
			cw.setDelimiter(';');
			cw.write(idX);
			cw.write(idY);
			
			Map<CsvReader, Set<Integer>> readers = new HashMap<CsvReader, Set<Integer>>();
			CsvReader reader;
			Set<Integer> index;
			for(String icsv : inputCsv){
				
				reader = new CsvReader(icsv);
				reader.setDelimiter(';');
				reader.readHeaders();
				
				index = new HashSet<Integer>();
				for(int c=0; c<reader.getHeaderCount(); c++){
					if(!idX.equalsIgnoreCase(reader.getHeader(c)) && !idY.equalsIgnoreCase(reader.getHeader(c))){
						index.add(c);
					}
				}
				readers.put(reader, index);	
			}
			
			for(Entry<CsvReader, Set<Integer>> r : readers.entrySet()){
				for(int ind : r.getValue()){
					cw.write(r.getKey().getHeader(ind));
				}
			}
			cw.endRecord();
			
			Map<CsvReader, Map<RefPoint, List<String>>> waits = new HashMap<CsvReader, Map<RefPoint, List<String>>>();
			Map<CsvReader, List<String>> local;
			boolean export;
			RefPoint lp, rp;
			List<String> values;
			for(int j=0; j<entete.height(); j++){
				for(int i=0; i<entete.width(); i++){
					export = false;
					lp = new RefPoint(CoordinateManager.getProjectedX(entete, i), CoordinateManager.getProjectedY(entete, j));
					local = new HashMap<CsvReader, List<String>>();
					for(Entry<CsvReader, Set<Integer>> r : readers.entrySet()){
						
						if(waits.containsKey(r.getKey())){
							if(waits.get(r.getKey()).containsKey(lp)){
								local.put(r.getKey(), waits.get(r.getKey()).get(lp));
								waits.remove(r.getKey());
								export = true;
							}else{
								values = new ArrayList<String>();
								for(int ind : r.getValue()){
									values.add(entete.noDataValue()+"");
								}
								local.put(r.getKey(), values);
							}
						}else if(r.getKey().readRecord()){
							rp = new RefPoint(Double.parseDouble(r.getKey().get(idX)), Double.parseDouble(r.getKey().get(idY)));
							values = new ArrayList<String>();
							for(int ind : r.getValue()){
								values.add(r.getKey().get(ind));
							}
							if(rp.equals(lp)){
								local.put(r.getKey(), values);
								export = true;
							}else{
								if(!waits.containsKey(r.getKey())){
									waits.put(r.getKey(), new HashMap<RefPoint, List<String>>());
								}
								waits.get(r.getKey()).put(rp, values);
								values = new ArrayList<String>();
								for(int ind : r.getValue()){
									values.add(entete.noDataValue()+"");
								}
								local.put(r.getKey(), values);
							}
						}else{
							values = new ArrayList<String>();
							for(int ind : r.getValue()){
								values.add(entete.noDataValue()+"");
							}
							local.put(r.getKey(), values);
						}
					}
					
					if(export){
						cw.write(CoordinateManager.getProjectedX(entete, i)+"");
						cw.write(CoordinateManager.getProjectedY(entete, j)+"");
						for(Entry<CsvReader, List<String>> r : local.entrySet()){
							for(String v : r.getValue()){
								cw.write(v);
							}
						}
						cw.endRecord();
					}
					//System.out.println(CoordinateManager.getProjectedX(entete, i)+" "+CoordinateManager.getProjectedY(entete, j));
					
				}	
			}
			/*
			while(again){
				
				for(Entry<CsvReader, Set<Integer>> r : readers.entrySet()){
					
					if(r.getKey().readRecord()){
						if(first){
							for(String id : ids){
								cw.write(r.getKey().get(id));
							}
							first = false;
						}
						
						for(int i : r.getValue()){
							cw.write(r.getKey().get(i));
						}
					}else{
						again = false;
					}
				}
				cw.endRecord();
			}
					*/	
			cw.close();
			for(CsvReader r : readers.keySet()){
				r.close();
			}
			readers.clear();
			readers = null;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	public static void exportGeoTiff(String csv, String output, String variable, EnteteRaster entete) {
		
		double minX = entete.minx();
		double minY = entete.miny();
		int width = entete.width();
		int height = entete.height();
		float cellSize = entete.cellsize();
		int noDataValue = entete.noDataValue();
		
		float[] data = new float[width*height];
		
		try {	
			CsvReader cr = new CsvReader(csv);
			cr.setDelimiter(';');
			cr.readHeaders();
			
			cr.readRecord();
			double x = Double.parseDouble(cr.get("X"));
			double y = Double.parseDouble(cr.get("Y"));
			int i=0, j=0;
			for(double nextY=minY + (height-1)*cellSize + cellSize/2; nextY>=minY; nextY-=cellSize, j++){
				i=0;
				for(double nextX=minX + cellSize - cellSize/2; nextX<(minX + width*cellSize); nextX+=cellSize, i++){
					
					if((Math.abs(y-nextY) < (cellSize/2.0)) && (Math.abs(x-nextX) < (cellSize/2.0))){
						
						data[j*width+i] = Float.parseFloat(cr.get(variable));
						
						if(cr.readRecord()){
							x = Double.parseDouble(cr.get("X"));
							y = Double.parseDouble(cr.get("Y"));
						}
					}else{
						
						data[j*width+i] = noDataValue;
					}
				}
			}
			
			CoverageManager.writeGeotiff(output, data, entete);
			
			cr.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public static void exportAsciiGrid(String csv, String folder, String outputName, Matrix matrix) {
		exportAsciiGrid(csv, folder, outputName, null, matrix.width(), matrix.height(), matrix.minX(), matrix.minY(), 1, matrix.cellsize(), matrix.noDataValue());
	}
	
	public static void exportAsciiGridAndVisualize(String csv, String folder, String outputName, Matrix matrix) {
	
		exportAsciiGrid(csv, folder, outputName, matrix);
		
		MatrixManager.visualize(folder);
	}
	
	public static void exportAsciiGrid(String csv, String folder, String outputName, Matrix matrix, int delta) {
		exportAsciiGrid(csv, folder, outputName, null, matrix.width(), matrix.height(), matrix.minX(), matrix.minY(), delta, matrix.cellsize(), matrix.noDataValue());
	}
	
	public static void exportAsciiGridAndVisualize(String csv, String folder, String outputName, Matrix matrix, int delta) {
		
		exportAsciiGrid(csv, folder, outputName, matrix, delta);
		
		MatrixManager.visualize(folder);
	}
	
	public static void exportAsciiGrid(String csv, String folder, String outputName, Set<String> metrics, Matrix matrix, int delta) {
		exportAsciiGrid(csv, folder, outputName, metrics, matrix.width(), matrix.height(), matrix.minX(), matrix.minY(), delta, matrix.cellsize(), matrix.noDataValue());
	}
	
	public static void exportAsciiGridAndVisualize(String csv, String folder, String outputName, Set<String> metrics, Matrix matrix, int delta) {
		
		exportAsciiGrid(csv, folder, outputName, metrics, matrix, delta);
		
		MatrixManager.visualize(folder);
	}

	public static void exportAsciiGrid(Set<String> csvFiles, String folder, String outputName, Set<String> metrics, 
			int width, int height, double minX, double minY, int delta, double cellSize, int noDataValue) {
		
		String name;
		for(String csv : csvFiles){
			name = new File(csv).getName().replace(".csv", "");
			exportAsciiGrid(csv, folder+"/"+name+"_", outputName, metrics, width, height, minX, minY, delta, cellSize, noDataValue);
		}
	}
	
	public static void exportAsciiGridAndVisualize(Set<String> csvFiles, String folder, String outputName, Set<String> metrics, 
			int width, int height, double minX, double minY, int delta, double cellSize, int noDataValue) {
		
		exportAsciiGrid(csvFiles, folder, outputName, metrics, width, height, minX, minY, delta, cellSize, noDataValue);
		
		MatrixManager.visualize(folder);
	}
	
	public static void exportAsciiGrid2(String csv, String folder, String outputName, Set<String> metrics, 
		int width, int height, double minX, double minY, int delta, double cellSize, int noDataValue) {
		
		try{
			Map<String, BufferedWriter> writers = new HashMap<String, BufferedWriter>();
			
			CsvReader cr = new CsvReader(csv);
			cr.setDelimiter(';');
			cr.readHeaders();
			
			if(metrics != null && metrics.size() != 0){
				for(String metric : metrics){
					for(String header : cr.getHeaders()){
						if(header.contains(metric)){
							writers.put(header, new BufferedWriter(new FileWriter(folder+header+".asc")));	
						}
					}
				}
			}else{
				for(String header : cr.getHeaders()){
					if(!header.equalsIgnoreCase("X") && !header.equalsIgnoreCase("Y")){
						writers.put(header, new BufferedWriter(new FileWriter(folder+header+".asc")));	
					}
				}
			}
			
			for(BufferedWriter w : writers.values()){
				int nc = new Double(width/delta).intValue();
				if(width%delta != 0){
					nc++;
				}
				w.write("ncols "+nc+"\n");
				int nr = new Double(height/delta).intValue();
				if(height%delta != 0){
					nr++;
				}
				w.write("nrows "+nr+"\n");
				w.write("xllcorner "+minX+"\n");
				w.write("yllcorner "+minY+"\n");
				w.write("cellsize "+delta*cellSize+"\n");
				w.write("NODATA_value "+noDataValue+"\n");
			}
			
			double y = -1, ytemp;
			while(cr.readRecord()){
				ytemp = Double.parseDouble(cr.get("Y"));
				if(y == -1){
					for(Entry<String, BufferedWriter> e : writers.entrySet()){
						e.getValue().write(cr.get(e.getKey())+"");
					}
					y = ytemp;
				}else if(ytemp != y){
					for(Entry<String, BufferedWriter> e : writers.entrySet()){
						e.getValue().write("\n"+cr.get(e.getKey()));
					}
					y = ytemp;
				}else{
					for(Entry<String, BufferedWriter> e : writers.entrySet()){
						e.getValue().write(" "+cr.get(e.getKey()));
					}
				}
			}
			for(Entry<String, BufferedWriter> e : writers.entrySet()){
				e.getValue().write('\n');
				e.getValue().close();
			}
			
			cr.close();
			
		}catch(IOException ex){
			ex.printStackTrace();
		}
	}
	
	public static void exportAsciiGrid(String csv, String folder, String outputAsc, Set<String> metrics, 
			int width, int height, double minX, double minY, int delta, double cellSize, int noDataValue) {
			
		try{
			Map<String, BufferedWriter> writers = new HashMap<String, BufferedWriter>();
				
			String name = new File(csv).getName().replace(".csv", "");
			CsvReader cr = new CsvReader(csv);
			cr.setDelimiter(';');
			cr.readHeaders();
			
			if(metrics.size() == 1 && outputAsc != null){
				for(String header : cr.getHeaders()){
					//if(header.contains(metrics.iterator().next())){
					if(header.equalsIgnoreCase(metrics.iterator().next())){
						writers.put(header, new BufferedWriter(new FileWriter(outputAsc)));	
					}
				}
			}else if(metrics != null && metrics.size() != 0){
				for(String metric : metrics){
					for(String header : cr.getHeaders()){
						//if(header.contains(metric)){
						if(header.equalsIgnoreCase(metric)){
							writers.put(header, new BufferedWriter(new FileWriter(folder+name+"_"+header+".asc")));	
						}
					}
				}
			}else{
				for(String header : cr.getHeaders()){
					if(!header.equalsIgnoreCase("X") && !header.equalsIgnoreCase("Y")){
						writers.put(header, new BufferedWriter(new FileWriter(folder+name+"_"+header+".asc")));	
					}
				}
			}
				
			for(BufferedWriter w : writers.values()){
				int nc = new Double(width/delta).intValue();
				if(width%delta != 0){
					nc++;
				}
				w.write("ncols "+nc);
				w.newLine();
				int nr = new Double(height/delta).intValue();
				if(height%delta != 0){
						nr++;
				}
				w.write("nrows "+nr);
				w.newLine();
				w.write("xllcorner "+minX);
				w.newLine();
				w.write("yllcorner "+minY);
				w.newLine();
				w.write("cellsize "+delta*cellSize);
				w.newLine();
				w.write("NODATA_value "+noDataValue);
				w.newLine();
			}
				
			cr.readRecord();
			double x = Double.parseDouble(cr.get("X"));
			double y = Double.parseDouble(cr.get("Y"));
			boolean ok = true; 
			for(double nextY=minY + (height-1)*cellSize + cellSize/2; nextY>=minY; nextY-=cellSize){
				for(double nextX=minX + cellSize - cellSize/2; nextX<(minX + width*cellSize); nextX+=cellSize){
					
					if((Math.abs(y-nextY) < (cellSize/2.0)) && (Math.abs(x-nextX) < (cellSize/2.0))){
						for(Entry<String, BufferedWriter> e : writers.entrySet()){
							e.getValue().write(cr.get(e.getKey())+" ");
						}
						if(cr.readRecord()){
							x = Double.parseDouble(cr.get("X"));
							y = Double.parseDouble(cr.get("Y"));
						}
					}else{
						for(Entry<String, BufferedWriter> e : writers.entrySet()){
							e.getValue().write(noDataValue+" ");
						}
					}
				}
				
				for(Entry<String, BufferedWriter> e : writers.entrySet()){
					e.getValue().newLine();
				}
			}
			
			for(Entry<String, BufferedWriter> e : writers.entrySet()){
				e.getValue().close();
				try {
					//String prj_input = DynamicLayerFactory.class.getResource("lambert93.prj").toString().replace("file:/", "");
					//Tool.copy(prj_input, folder+name+"_"+e.getKey()+".prj");
					Tool.copy(DynamicLayerFactory.class.getResourceAsStream("lambert93.prj"), folder+name+"_"+e.getKey()+".prj");
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
			
			cr.close();
			
		}catch(IOException ex){
			ex.printStackTrace();
		}finally{
			
		}
	}

	public static void sort(String csv) {
		try{
			CSVReader reader = new CSVReader(new FileReader(csv), ';');
			String[] header = reader.readNext(); // lecture du header
		    
		    Set<String[]> set = new TreeSet<String[]>(new Comparator<String[]>(){
		    	@Override
				public int compare(String[] line1, String[] line2) {
					int y = line1[1].compareTo(line2[1]);
					if(y < 0){
						return 1;
					}else if(y > 0){
						return -1;
					}else{
						int x = line1[0].compareTo(line2[0]);
						if(x < 0){
							return -1;
						}else if(x > 0){
							return 1;
						}else{
							return 0;
						}
					}
				}
		    });
		    String[] line;
		    while ((line = reader.readNext()) != null) {
		    	set.add(line);
		    }
		    reader.close();
		    
		    File f = new File(csv);
		    String name = f.getName().replace(".csv", "");
		    CsvWriter cw = new CsvWriter(f.getParent()+"/"+name+"_sort.csv");
		    cw.setDelimiter(';');
		    for(String h : header){
		    	cw.write(h);
		    }
		    cw.endRecord();
		    
		    for(String[] l : set){
		    	for(String c : l){
		    		cw.write(c);
		    	}
		    	cw.endRecord();
		    }
		    
		    cw.close();
		    
		    new File(f.getParent()+"/"+name+"_sort.csv").renameTo(f);
		}catch(FileNotFoundException ex){
			ex.printStackTrace();
		}catch(IOException ex){
			ex.printStackTrace();
		}
		
	}
	
	public static void exportFromAsciiGrid(String ascii, String csv){
		
		String name = new File(ascii).getName().replace(".asc",  "");
		
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(ascii));
			String line = br.readLine();
			String sep = String.valueOf(line.charAt(5));
			
			String[] s = line.split(sep);
			int width = Integer.parseInt(s[s.length-1]);
			//System.out.println(width);

			s = br.readLine().split(sep);
			int height = Integer.parseInt(s[s.length-1]);
			//System.out.println("and "+height);
			
			s = br.readLine().split(sep);
			double minx = Double.parseDouble(s[s.length-1]);

			s = br.readLine().split(sep);
			double miny = Double.parseDouble(s[s.length-1]);

			s = br.readLine().split(sep);
			double cellsize = Double.parseDouble(s[s.length-1]);

			double maxx = minx + width * cellsize;
			double maxy = miny + height * cellsize;
			
			s = br.readLine().split(sep);
			int noDataValue = Integer.parseInt(s[s.length-1]);
			
			CsvWriter cw = new CsvWriter(csv);
			cw.setDelimiter(';');
			cw.write("X");
			cw.write("Y");
			cw.write(name);
			cw.endRecord();
			
			double x, y = maxy;
			while(br.ready()){
				x = minx;
				s = br.readLine().split(" ");
				for(String v : s){
					if(Double.parseDouble(v) != noDataValue){
						cw.write(x+"");
						cw.write(y+"");
						cw.write(v);
						cw.endRecord();
					}
					x += cellsize;
				}
				y -= cellsize;
			}
			
			cw.close();
			br.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public static void exportFromAsciiGrid(Set<String> asciis, String csv){
		try {
			Map<String, BufferedReader> readers = new TreeMap<String, BufferedReader>();
			for(String a : asciis){
				readers.put(new File(a).getName().replace(".asc",  ""), new BufferedReader(new FileReader(a)));
			}
			int width = 0, height, noDataValue = -1;
			double minx = 0, maxx, miny, maxy = 0, cellsize = 0;
			for(BufferedReader br : readers.values()){
				String line = br.readLine();
				String sep = String.valueOf(line.charAt(5));
				
				String[] s = line.split(sep);
				width = Integer.parseInt(s[s.length-1]);
				
				s = br.readLine().split(sep);
				height = Integer.parseInt(s[s.length-1]);
				
				s = br.readLine().split(sep);
				minx = Double.parseDouble(s[s.length-1]) + cellsize/2.0;

				s = br.readLine().split(sep);
				miny = Double.parseDouble(s[s.length-1]) - cellsize/2.0;

				s = br.readLine().split(sep);
				cellsize = Double.parseDouble(s[s.length-1]);

				maxx = minx + width * cellsize;
				maxy = miny + height * cellsize;
				
				s = br.readLine().split(sep);
				noDataValue = Integer.parseInt(s[s.length-1]);
			}
			
			CsvWriter cw = new CsvWriter(csv);
			cw.setDelimiter(';');
			cw.write("X");
			cw.write("Y");
			for(String n : readers.keySet()){
				cw.write(n);
			}
			cw.endRecord();
			
			double x, y = maxy;
			Map<String, String[]> lines = new TreeMap<String, String[]>();
			while(readers.values().iterator().next().ready()){
				x = minx;
				lines.clear();
				for(Entry<String, BufferedReader> e : readers.entrySet()){
					lines.put(e.getKey(), e.getValue().readLine().split(" "));
				}
				
				for(int i=0; i<width; i++){
					boolean ok = true;
					for(String[] line : lines.values()){
						if(Double.parseDouble(line[i]) == noDataValue){
							ok = false;
							break;
						}
					}
					if(ok){
						cw.write(x+"");
						cw.write(y+"");
						for(String[] line : lines.values()){
							cw.write(line[i]);
						}
						cw.endRecord();
					}
					x += cellsize;
				}
				y -= cellsize;
			}
			
			cw.close();
			for(BufferedReader br : readers.values()){
				br.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
