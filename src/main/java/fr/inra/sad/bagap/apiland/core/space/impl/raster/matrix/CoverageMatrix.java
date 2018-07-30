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
import javax.media.jai.PlanarImage;
import org.geotools.coverage.grid.GridCoverage2D;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.core.util.Couple;

public class CoverageMatrix implements Matrix{
	
	private static final long serialVersionUID = 1L;

	private GridCoverage2D grid;
	
	private double[] v = new double[1] ;
	
	private String file;
	
	private double cellsize;
	
	private double minX, maxX, minY, maxY;
	
	private int noDataValue;
	
	private Set<Integer> values;
	
	/*public MatrixCoverage(GridCoverage2D grid, double cellsize, double minX, double maxX, double minY, double maxY, int noDataValue){
		this.grid = grid;
		this.cellsize = cellsize;
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
		this.noDataValue = noDataValue;
	}*/
	
	public CoverageMatrix(GridCoverage2D grid, String file){
		this.grid = grid;
		this.file = file;
	}
	
	@Override
	public MatrixType getType() {
		return MatrixType.ARRAY;
	}
	
	@Override
	public void visualize() {
		MatrixManager.visualize(this);
	}
	
	@Override
	public String getFile(){
		return file;
	}
	
	@Override
	public void display(){
		System.out.println(this);
		for(int i=0; i<grid.getRenderedImage().getHeight(); i++){
			for(int j=0; j<grid.getRenderedImage().getWidth(); j++){
				System.out.print(get(j,i)+" ");
			}
			System.out.println();
		}
	}
	
	@Override
	public double get(int x, int y){
		if(x>=0 && x<width()
				&& y>=0 && y<height()){
			return ((PlanarImage)grid.getRenderedImage()).getData().getPixel(x, y, v)[0];
		}
		return -1;
	}
	
	@Override
	public double get(Pixel p){
		return get(p.x(), p.y());
	}

	@Override
	public int height() {
		return ((PlanarImage)grid.getRenderedImage()).getHeight();
	}

	@Override
	public int width() {
		return ((PlanarImage)grid.getRenderedImage()).getWidth();
	}

	public GridCoverage2D getGrid() {
		return grid;
	}

	@Override
	public double cellsize() {
		return ((PlanarImage)grid.getRenderedImage()).getData().getTransferType();
	}

	@Override
	public double maxY() {
		return ((PlanarImage)grid.getRenderedImage()).getMaxY();
	}

	@Override
	public double minX() {
		return ((PlanarImage)grid.getRenderedImage()).getMinX();
	}

	@Override
	public double maxX() {
		return ((PlanarImage)grid.getRenderedImage()).getMaxX();
	}

	@Override
	public double minY() {
		return ((PlanarImage)grid.getRenderedImage()).getMinY();
	}

	@Override
	public int noDataValue() {
		return -1;
	}

	@Override
	public void setFile(String file) {
		this.file = file;
	}

	@Override
	public Set<Integer> values() {
		if(values == null){
			values = new HashSet<Integer>();
			for(int j=0; j<height(); j++){
				for(int i=0; i<width(); i++){
					values.add(new Double(((PlanarImage)grid.getRenderedImage()).getData().getPixel(i,j,v)[0]).intValue());
				}
			}
		}
		return values;
	}

	@Override
	public Iterator<Pixel> iterator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void put(int x, int y, double value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void put(Pixel p, double value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void put(Raster r, double value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getActiveArea() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void init(double v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getCouples(Matrix horizontals, Matrix verticals) {
		
	}

	@Override
	public int tileWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int tileHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int numXTiles() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int numYTiles() {
		// TODO Auto-generated method stub
		return 0;
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
	
	
	
}
