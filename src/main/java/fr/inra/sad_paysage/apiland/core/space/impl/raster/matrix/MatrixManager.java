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
package fr.inra.sad_paysage.apiland.core.space.impl.raster.matrix;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;

import fr.inra.sad_paysage.apiland.core.util.VisuImageJ;

public class MatrixManager {

	public static void visualize(String f) {
		File file = new File(f);
		if(file.isDirectory()){
			for(File ff : file.listFiles()){
				new VisuImageJ(ff.toString());
			}
		}else{
			new VisuImageJ(f);
		}
	}
	
	public static void visualize(Matrix m){
		new VisuImageJ(m.getFile());
	}
	
	public static void exportAsciiGridAndVisualize(Matrix matrix, String name){
		exportAsciiGrid(matrix,name);
		visualize(matrix);
	}
	
	public static void exportAsciiGrid(Matrix matrix, String name){
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter(name));
			bw.write("ncols "+matrix.width());
			bw.newLine();
			bw.write("nrows "+matrix.height());
			bw.newLine();
			bw.write("xllcorner "+matrix.minX());
			bw.newLine();
			bw.write("yllcorner "+matrix.minY());
			bw.newLine();
			bw.write("cellsize "+matrix.cellsize());
			bw.newLine();
			bw.write("NODATA_value "+matrix.noDataValue());
			bw.newLine();
			for(int i=0; i<matrix.height(); i++){
				for(int j=0; j<matrix.width(); j++){
					bw.write(matrix.get(j, i)+" ");
				}
				bw.newLine();
			}
			bw.close();
			matrix.setFile(name);
		}catch(IOException e){
			e.printStackTrace(); 
		}	
	}
	
	public static void findAndReplace(String in, String out, String v1, String v2){
		try{
			CsvReader cr = new CsvReader(in);
			CsvWriter cw = new CsvWriter(out);
			
			cr.setDelimiter(' ');
			cw.setDelimiter(' ');
			
			cr.readRecord();
			cw.write(cr.get(0));
			int ncols = new Integer(cr.get(1));
			cw.write(cr.get(1));
			cw.endRecord();
			
			cr.readRecord();
			cw.write(cr.get(0));
			cw.write(cr.get(1));
			cw.endRecord();
			
			cr.readRecord();
			cw.write(cr.get(0));
			cw.write(cr.get(1));
			cw.endRecord();
			
			cr.readRecord();
			cw.write(cr.get(0));
			cw.write(cr.get(1));
			cw.endRecord();
			
			cr.readRecord();
			cw.write(cr.get(0));
			cw.write(cr.get(1));
			cw.endRecord();
			
			cr.readRecord();
			cw.write(cr.get(0));
			cw.write((cr.get(1)));
			cw.endRecord();
			
			while(cr.readRecord()){
				for(int i=0; i<ncols; i++){
					//System.out.println(cr.get(i));
					if(cr.get(i).equalsIgnoreCase(v1)){
						cw.write(v2);
					}else{
						cw.write(cr.get(i));
					}
				}
				cw.endRecord();
			}

			cr.close();
			cw.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void findAndReplaceComma2Dot(String in, String out){
		try{
			CsvReader cr = new CsvReader(in);
			CsvWriter cw = new CsvWriter(out);
			
			cr.setDelimiter(' ');
			cw.setDelimiter(' ');
			
			cr.readRecord();
			cw.write(cr.get(0));
			int ncols = new Integer(cr.get(1));
			cw.write(cr.get(1));
			cw.endRecord();
			
			cr.readRecord();
			cw.write(cr.get(0));
			cw.write(cr.get(1));
			cw.endRecord();
			
			cr.readRecord();
			cw.write(cr.get(0));
			cw.write(cr.get(1));
			cw.endRecord();
			
			cr.readRecord();
			cw.write(cr.get(0));
			cw.write(cr.get(1));
			cw.endRecord();
			
			cr.readRecord();
			cw.write(cr.get(0));
			cw.write(cr.get(1));
			cw.endRecord();
			
			cr.readRecord();
			cw.write(cr.get(0));
			cw.write((cr.get(1)));
			cw.endRecord();
			
			DecimalFormatSymbols comma = new DecimalFormatSymbols();
			comma.setDecimalSeparator(',');
			DecimalFormat fcomma = new DecimalFormat("0.00000", comma);
			
			DecimalFormatSymbols dot = new DecimalFormatSymbols();
			dot.setDecimalSeparator('.');
			DecimalFormat fdot = new DecimalFormat("0.00000", dot);
			
			Number n;
			while(cr.readRecord()){
				for(int i=0; i<ncols; i++){
					n =  fcomma.parse(cr.get(i));
					if(n instanceof Long){
						cw.write(n+"");
					}else{
						cw.write(fdot.format((Double) n));
					}
					
				}
				cw.endRecord();
			}

			cr.close();
			cw.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Matrix retile(Matrix m1, Matrix m2) {
		if(m1.cellsize() != m2.cellsize()){
			throw new IllegalArgumentException("matrix have not the same cellsize");
		}
		return retile(m1, m2.width(), m2.height(), m2.minX(), m2.minY());
	}
	
	public static Matrix retile(Matrix m, int dX, int dY, int ncols, int nrows) {
		Matrix mn = MatrixFactory.get(m.getType()).create(ncols, nrows, 
				m.cellsize(), m.minX()+dX*m.cellsize(), m.minX()+dX*m.cellsize()+ncols*m.cellsize(),
				m.minY()+dY*m.cellsize(), m.minY()+dY*m.cellsize()+ nrows*m.cellsize(), m.noDataValue());
		for(int j=0; j<nrows; j++){
			for(int i=0; i<ncols; i++){
				mn.put(i, j, m.get(i+dX, j+dY));
			}
		}
		return mn;
	}
	
	public static Matrix retile(Matrix m, int ncols, int nrows, double xllcorner, double yllcorner) {
		
		Matrix mn = MatrixFactory.get(m.getType()).create(ncols, nrows, m.cellsize(), xllcorner, xllcorner + ncols * m.cellsize(), yllcorner, yllcorner + nrows * m.cellsize(), m.noDataValue());
		
		for(int j=0; j<nrows; j++){
			for(int i=0; i<ncols; i++){
				mn.put(i, j, m.get(i+5000, j+5000));
			}
		}
		/*
		int xdist = new Double((m.minX() - xllcorner) / m.cellsize()).intValue();
		int ydist = new Double((m.minY() - yllcorner) / m.cellsize()).intValue();
		int rest = nrows - m.height() - ydist;
		//System.out.println(xdist+" "+ydist+" "+rest+" "+(ydist-rest));
		ydist = rest;
		int j=0;
		for(; j<ydist; j++){
			for(int i=0; i<ncols; i++){
				mn.put(i, j, m.noDataValue());
			}
		}
		for(; j<ydist+m.height(); j++){
			int i=0;
			for(; i<xdist; i++){
				mn.put(i, j, m.noDataValue());
			}
			//for(; i<xdist+m.width(); i++){
			for(; i<ncols; i++){
				mn.put(i, j, m.get(i-xdist, j-ydist));
			}
			for(; i<ncols; i++){
				mn.put(i, j, m.noDataValue());
			}
		}
		for(; j<nrows; j++){
			for(int i=0; i<ncols; i++){
				mn.put(i, j, m.noDataValue());
			}
		}
		*/
		return mn;
	}
}
