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
package fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.core.util.Couple;

public class YaleSparseMatrix implements Matrix{

	private List<Double> vA;
	
	private int[] iA;
	
	private List<Integer> xA;
	
	private int width;
	
	private double minX, maxY ,minY, maxX;
	
	private int noDataValue;
	
	private double cellsize; 
	
	private String file;
	
	private Set<Integer> values;
	
	public YaleSparseMatrix(int width, int height, double cellsize, double minX , double maxX, double minY, double maxY, int noDataValue) {
		this.width = width;
		this.cellsize = cellsize;
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
		this.noDataValue = noDataValue;
		this.vA = new ArrayList<Double>();
		this.iA = new int[height + 1];
		this.xA = new ArrayList<Integer>();
		values = new HashSet<Integer>();
	}
	
	public YaleSparseMatrix(Matrix matrix) {
		this.width = matrix.width();
		this.cellsize = matrix.cellsize();
		this.minX = matrix.minX();
		this.maxY = matrix.maxY();
		this.noDataValue = matrix.noDataValue();
		this.vA = new ArrayList<Double>();
		this.iA = new int[matrix.height() + 1];
		this.xA = new ArrayList<Integer>();
		values = new HashSet<Integer>();
	}
	
	@Override
	public double getActiveArea() {
		double area = 0.0;
		for(Pixel p : this){
			if(get(p) != noDataValue){
				area += Math.pow(cellsize,2);
			}
		}
		return area;
	}
	
	@Override
	public MatrixType getType() {
		return MatrixType.YALE_SPARSE;
	}
	
	@Override
	public void init(double v) {
		noDataValue = new Double(v).intValue();
	}
	
	@Override
	public void visualize() {
		MatrixManager.visualize(this);
	}
	
	@Override
	public double cellsize() {
		return cellsize;
	}

	@Override
	public void display() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double get(int x, int y) {
		try{
			int n = iA[y+1] - iA[y];
			for(int i=0; i<n; i++){
				if(xA.get(iA[y]+i) == x){
					return vA.get(iA[y]+i);
				}
			}
			return noDataValue;
		}catch(Exception ex){
			return noDataValue;
		}
		
	}

	@Override
	public double get(Pixel p) {
		return get(p.x(),p.y());
	}
	
	@Override
	public void put(int x, int y, double value) {
		if(value != noDataValue){
			try{
				int n = iA[y+1] - iA[y];
				for(int i=0; i<n; i++){
					if(xA.get(iA[y]+i) == x){
						vA.set(iA[y]+i, value);
						return;
					}else if(xA.get(iA[y]+i) > x){
						xA.add(iA[y]+i, x);
						vA.add(iA[y]+i, value);
						for(int j=y+1; j<iA.length; j++){
							iA[j]++;
						}
						return;
					}
				}
				xA.add(iA[y]+n, x);
				vA.add(iA[y]+n, value);
				for(int j=y+1; j<iA.length; j++){
					iA[j]++;
				}
				values.add(new Double(value).intValue());
			}catch(Exception ex){}
		}
	}
		

	@Override
	public void put(Pixel p, double value) {
		put(p.x(),p.y(),value);
	}

	@Override
	public void put(Raster r, double value) {
		for(Pixel p : r){
			put(p,value);
		}
	}

	@Override
	public String getFile() {
		return file;
	}

	@Override
	public int height() {
		return iA.length - 1;
	}

	@Override
	public double maxX() {
		return maxX;
	}

	@Override
	public double maxY() {
		return maxY;
	}

	@Override
	public double minX() {
		return minX;
	}

	@Override
	public double minY() {
		return minY;
	}

	@Override
	public int noDataValue() {
		return noDataValue;
	}

	@Override
	public void setFile(String file) {
		this.file = file;
	}

	@Override
	public Set<Integer> values() {
		return values;
	}

	@Override
	public int width() {
		return width;
	}

	@Override
	public Iterator<Pixel> iterator() {
		return new IteratorMatrix(this);
	}

	@Override
	public void getCouples(Matrix horizontals, Matrix verticals) {
		for(int y=0; y<height()-1; y++){
			for(int x=0; x<width()-1; x++){
				System.out.println(x+" "+y);
				verticals.put(x, y, Couple.get(
						new Double(get(x, y)).intValue(),
						new Double(get(x, y+1)).intValue()));
				horizontals.put(x, y, Couple.get(
						new Double(get(x, y)).intValue(),
						new Double(get(x+1, y)).intValue()));
			}
		}
		for(int y=0; y<height()-1; y++){
			verticals.put(width()-1, y, Couple.get(
					new Double(get(width()-1, y)).intValue(),
					new Double(get(width()-1, y+1)).intValue()));
			
		}
		for(int x=0; x<width()-1; x++){
			horizontals.put(x, height()-1, Couple.get(
					new Double(get(x, height()-1)).intValue(),
					new Double(get(x+1, height()-1)).intValue()));
		}
	}

	@Override
	public int tileWidth() {
		return width();
	}

	@Override
	public int tileHeight() {
		return height();
	}

	@Override
	public int numXTiles() {
		return 1;
	}

	@Override
	public int numYTiles() {
		return 1;
	}

	@Override
	public boolean contains(Pixel p) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public double minV() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double maxV() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean contains(int x, int y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void resetValues() {
		// TODO Auto-generated method stub
		
	}

}
