package fr.inrae.act.bagap.apiland.image;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class ReadTiff {

	public static void main(String[] args){
		
		String s = "0110100001100101011011000110110001101111";
	    String str = "";

	    for (int i = 0; i < s.length()/8; i++) {

	        int a = Integer.parseInt(s.substring(8*i,(i+1)*8),2);
	        str += (char)(a);
	    }

	    System.out.println(str);
		
		String path = "F:/chloe/winterschool/data/start/";
		String input = path+"raster2007.tif";
		try {
			DataInputStream lecteur;
		    
		    lecteur = new DataInputStream(new FileInputStream(input));
		   
		    System.out.println((char)lecteur.readByte());
		    System.out.println((char)lecteur.readByte());
		    System.out.println((int)lecteur.readByte());
		    System.out.println((int)lecteur.readByte());
		    System.out.println(lecteur.readByte());
		    System.out.println(lecteur.readByte());
		    System.out.println(lecteur.readByte());
		    System.out.println(lecteur.readByte());
		    System.out.println(lecteur.readByte());
		    System.out.println(lecteur.readByte()); 
		    
		    lecteur.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
}
