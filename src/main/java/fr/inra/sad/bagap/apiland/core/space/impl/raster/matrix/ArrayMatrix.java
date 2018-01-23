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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.core.util.Couple;

public class ArrayMatrix implements Matrix, Iterable<Pixel>{

	private static final long serialVersionUID = 1L;

	private double[][] tab;
	
	private int height, width;
	
	private double minX, maxY ,minY, maxX;
	
	private int noDataValue;
	
	private double cellsize; 
	
	private String file;
	
	private Set<Integer> values;

	public ArrayMatrix(double[][] tab) {
		this.tab = tab;
		this.height = tab.length;
		this.width = tab[0].length;
	}
	
	public ArrayMatrix(int width, int height) {
		this.height = height;
		this.width = width;
		this.tab = new double[height][width];
		values = new HashSet<Integer>();
	}
	
	public ArrayMatrix(final int width, final int height, final double cellsize, final double minX , final double maxX, final double minY, final double maxY, final int noDataValue) {
		this.height = height;
		this.width = width;
		this.cellsize = cellsize;
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
		this.noDataValue = noDataValue;
		this.tab = new double[height][width];
		values = new HashSet<Integer>();
	}
	
	public ArrayMatrix(final int width, final int height, final double cellsize, final double minX , 
			final double maxX, final double minY, final double maxY, final int noDataValue, 
			double[][] tab, Set<Integer> values, String file) {
		this(width, height, cellsize, minX, maxX, minY, maxY, noDataValue);
		this.tab = tab;
		this.values = values;
		this.file = file;
	}
	
	public ArrayMatrix(Matrix matrix) {
		this.height = matrix.height();
		this.width = matrix.width();
		this.cellsize = matrix.cellsize();
		this.minX = matrix.minX();
		this.maxX = matrix.maxX();
		this.minY = matrix.minY();
		this.maxY = matrix.maxY();
		this.noDataValue = matrix.noDataValue();
		this.tab = new double[height][width];
		values = new HashSet<Integer>();
	}
	
	@Override
	public String getFile(){
		return this.file;
	}
	
	@Override
	public void setFile(String file){
		this.file = file;
	}
	
	@Override
	public void display(){
		System.out.println();
		System.out.println(this);
		System.out.println("area = "+getActiveArea());
		for(double[] t : tab){
			for(double d : t){
				System.out.print(d+" ");
			}
			System.out.println();
		}
	}
	
	@Override
	public void init(double v){
		for(double[] t : tab){
			for(int i=0; i<t.length; i++){
				t[i] = v;
			}
		}
	}
	
	@Override
	public double get(final int x, final int y) {
		try{
			return tab[y][x];
		}catch(Exception ex){
			return noDataValue;
		}
	}
	
	@Override
	public double get(final Pixel p){
		return get(p.x(),p.y());
	}
	
	@Override
	public void put(final int x, final int y, final double value){
		tab[y][x] = value;
		if(value != noDataValue){
			values.add(new Double(value).intValue());
		}
	}
	
	@Override
	public void put(final Pixel p, final double value){
		put(p.x(),p.y(), value);
	}
	
	@Override
	public void put(final Raster r, final double value){
		for(Pixel p : r){
			put(p, value);
		}
	}

	@Override
	public int height() {
		return height;
	}

	@Override
	public int width() {
		return width;
	}

	@Override
	public Iterator<Pixel> iterator() {
		return new IteratorMatrix(this);
	}
	
	/*
	public double getArea(){
		double area = 0.0;
		for(Pixel p : this){
			if(get(p) != noDataValue){
				area += Math.pow(cellsize,2);
			}
		}
		return area;
	}
	*/
	
	@Override
	public double getActiveArea() {
		double area = 0.0;
		double pow = Math.pow(cellsize,2);
		for(Pixel p : this){
			if(get(p) != noDataValue){
				area += pow;
			}
		}
		return area;
	}

	@Override
	public double cellsize() {
		return cellsize;
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
	public double maxX() {
		return maxX;
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
	public Set<Integer> values() {
		return values;
	}

	@Override
	public void visualize() {
		MatrixManager.visualize(this);
	}

	@Override
	public MatrixType getType() {
		return MatrixType.ARRAY;
	}

	@Override
	public void getCouples(Matrix horizontals, Matrix verticals) {
		for(int y=0; y<height()-1; y++){
			for(int x=0; x<width()-1; x++){
				//System.out.println(x+" "+y);
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
	public boolean contains(int x, int y) {
		if(x >= 0 && x < width()
				&& y >= 0 && y < height()){
			return true;	
		}
		return false;
	}


	@Override
	public boolean contains(Pixel p) {
		return contains(p.x(), p.y());
	}

	@Override
	public double minV() {
		double min = Double.MAX_VALUE;
		for(double[] t : tab){
			for(int i=0; i<t.length; i++){
				min = Math.min(min, t[i]);
			}
		}
		return min;
	}

	@Override
	public double maxV() {
		double max = Double.MIN_VALUE;
		for(double[] t : tab){
			for(int i=0; i<t.length; i++){
				max = Math.max(max, t[i]);
			}
		}
		return max;
	}

	/*
	@Override
	public void setHeight(int height) {
		this.height = height;
	}

	@Override
	public void setWidth(int width) {
		this.width = width;
	}
	 */
	
}
