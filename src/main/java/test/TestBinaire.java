package test;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.CoverageManager;
import fr.inrae.act.bagap.raster.EnteteRaster;

public class TestBinaire {

	public static void main(String[] args) {
		/*
	    try {
	    	String s = "admin";
			byte[] bytes = s.getBytes("US-ASCII");
			for(int i=0; i<bytes.length; i++){
				System.out.println((char) bytes[i]);
			}
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}*/

		readFile();
		//exportTiff();
	}
	
	private static void exportTiff() {
		
		Coverage cov = CoverageManager.getCoverage("G:/FRC_Pays_de_la_Loire/data/testRaster/raster2007.asc");
		float[] data = cov.getData();
		EnteteRaster entete = cov.getEntete();
		cov.dispose();
		
		CoverageManager.write("G:/FRC_Pays_de_la_Loire/data/testRaster/raster2007_2.tif", data, entete);
		
	}

	private static void readFile(){

		FileOutputStream d;
		
		try {
			//in = new DataInputStream(new BufferedInputStream(new FileInputStream("G:/FRC_Pays_de_la_Loire/data/testRaster/typeBoisement.tif")));
			//DataInputStream in = new DataInputStream(new FileInputStream("G:/FRC_Pays_de_la_Loire/data/testRaster/typeBoisement.tif"));
			//FileInputStream in = new FileInputStream("G:/FRC_Pays_de_la_Loire/data/testRaster/typeBoisement.tif");
			FileInputStream in = new FileInputStream("G:/FRC_Pays_de_la_Loire/data/testRaster/raster2007_LZW.tif");
			
			FileOutputStream out = new FileOutputStream("G:/FRC_Pays_de_la_Loire/data/testRaster/test2007_LZW.tif");
			//int max = 0;
			for(int i=0; i<80000; i++){
				///max = Math.max(max, in.read());
				//max = Math.max(max, in.read());
				//max = Math.max(max, in.read());
				//max = Math.max(max, in.read());
				//System.out.println(i+" "+in.read()+" "+in.read()+" "+in.read()+" "+in.read());
				int r = in.read();
				char c = (char) r;
				
				//System.out.print(r+" '"+c+"'\n");
				
				out.write(r);
			}
			//System.out.println(max);
			
			in.close();
			out.flush();
			out.close();
		} catch (EOFException eof) {
			// fin normale de la lecture
			System.out.println();
		} catch (IOException ioe) {
			System.err.println(ioe);
			System.exit(1);
		}
	}
	
	private static void readData(){
		DataInputStream in;
		try {
			//in = new DataInputStream(new BufferedInputStream(new FileInputStream("G:/FRC_Pays_de_la_Loire/data/testRaster/typeBoisement.tif")));
			//in = new DataInputStream(new FileInputStream("G:/FRC_Pays_de_la_Loire/data/testRaster/typeBoisement.tif"));
			in = new DataInputStream(new FileInputStream("G:/FRC_Pays_de_la_Loire/data/testRaster/raster2007.asc"));
			
			//int max = 0;
			for(int i=0; i<37; i++){
				///max = Math.max(max, in.read());
				//max = Math.max(max, in.read());
				//max = Math.max(max, in.read());
				//max = Math.max(max, in.read());
				//System.out.println(i+" "+in.read()+" "+in.read()+" "+in.read()+" "+in.read());
				System.out.println(i+" "+in.readLine());
			}
			//System.out.println(max);
			
			in.close();
		} catch (EOFException eof) {
			// fin normale de la lecture
			System.out.println();
		} catch (IOException ioe) {
			System.err.println(ioe);
			System.exit(1);
		}
	}

}
