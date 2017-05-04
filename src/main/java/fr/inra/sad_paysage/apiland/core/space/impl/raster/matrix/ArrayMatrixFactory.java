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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.media.jai.PlanarImage;

import com.csvreader.CsvReader;

import fr.inra.sad_paysage.apiland.core.space.impl.raster.Raster;

public class ArrayMatrixFactory extends MatrixFactory{
	
	private static double minx, maxx, miny, maxy, cellsize;

	private static int width, height, noDataValue = -1;
	
	private static double[][] tab;

	private static Set<Integer> values;
	
	private static ArrayMatrixFactory factory = new ArrayMatrixFactory();
	
	private ArrayMatrixFactory(){}
	
	public static ArrayMatrixFactory get(){
		return factory;
	}
	
	@Override
	public ArrayMatrix create(Matrix mRef){
		return new ArrayMatrix(mRef);
	}
	
	@Override
	public ArrayMatrix create(int width, int height, double cellsize,
			double minX, double maxX, double minY, double maxY, int noData) {
		return new ArrayMatrix(width, height, cellsize, minX, maxX, minY, maxY, noData);
	}

	public Matrix createWithAsciiGrid(String ascii) {
		return createWithAsciiGrid(ascii, true);
	}
	
	public Matrix createWithAsciiGrid(String ascii, boolean read) {
		initWithAsciiGrid(ascii);
		if(read){
			findValues(ascii);
		}
		return new ArrayMatrix(width, height, cellsize, minx, maxx, miny, maxy, noDataValue, tab, values, ascii);
	}

	private void initWithAsciiGrid(String ascii) throws NumberFormatException {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(ascii));
			String line = br.readLine();
			String sep = String.valueOf(line.charAt(5));
			
			String[] s = line.split(sep);
			width = Integer.parseInt(s[s.length-1]);

			s = br.readLine().split(sep);
			height = Integer.parseInt(s[s.length-1]);

			s = br.readLine().split(sep);
			minx = Double.parseDouble(s[s.length-1]);

			s = br.readLine().split(sep);
			miny = Double.parseDouble(s[s.length-1]);

			s = br.readLine().split(sep);
			cellsize = Double.parseDouble(s[s.length-1]);

			maxx = minx + width * cellsize;
			maxy = miny + height * cellsize;

			s = br.readLine().split(sep);
			if (s[0].equalsIgnoreCase("NODATA_value")) {
				noDataValue = Integer.parseInt(s[s.length-1]);
				Raster.setNoDataValue(noDataValue);
			} else {
				noDataValue = Raster.getNoDataValue();
				br.close();
				throw new NumberFormatException();
			}
			br.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public void findValues(String ascii) {
		tab = new double[height][width];
		values = new HashSet<Integer>();
		try {
			CsvReader cr = new CsvReader(ascii);
			cr.setDelimiter(' ');

			cr.readRecord();
			cr.readRecord();
			cr.readRecord();
			cr.readRecord();
			cr.readRecord();
			cr.readRecord();

			double v;
			//int index = 0;
			int y = 0;
			while (cr.readRecord()) {
				//System.out.println(++index);
				for (int x = 0; x < width; x++) {
					if (!cr.get(x).equalsIgnoreCase("")) {
						v = Double.parseDouble(cr.get(x));
						tab[y][x] = v;
						if (v != noDataValue) {
							values.add((new Double(cr.get(x))).intValue());
						}
					}
				}
				y++;
			}

			cr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Matrix create(Matrix mRef, int divisor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Matrix create(int width, int height, double cellsize, double minX, double maxX, double minY, double maxY,
			int noData, PlanarImage ref) {
		// TODO Auto-generated method stub
		return null;
	}

	
}