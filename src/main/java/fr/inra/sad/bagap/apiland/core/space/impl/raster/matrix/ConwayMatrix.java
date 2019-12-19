package fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix;

import java.util.Iterator;
import java.util.Set;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;

public class ConwayMatrix implements Matrix, Iterable<Pixel> {

	private static final long serialVersionUID = 1L;

	private double minX, maxX, minY, maxY, cellsize;
	
	private int width, height;
	
	private Double[][] values;
	
	private String file;
	
	public ConwayMatrix(double cellsize, double minx, double maxx, double miny, double maxy, int width, int height, Double[][] values){
		this.minX = minx;
		this.maxX = maxx;
		this.minY = miny;
		this.maxY = maxy;
		this.width = width;
		this.height = height;
		this.cellsize = cellsize;
		Raster.setCellSize(this.cellsize);
		this.values = values;
	}
	
	@Override
	public double get(int x, int y) {
		int ix = 0;
		for(int i=0; i<values[y].length; i+=2){
			ix += values[y][i].intValue();
			if(x <= ix){
				return values[y][i+1];
			}
		}
		return Raster.getNoDataValue();
	}
	
	@Override
	public int width() {
		return width;
	}

	@Override
	public int height() {
		return height;
	}
	
	@Override
	public int size() {
		return width * height;
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
	public double maxY() {
		return maxY;
	}
	
	@Override
	public double cellsize() {
		return cellsize;
	}
	
	@Override
	public String getFile() {
		return file;
	}

	@Override
	public void setFile(String file) {
		this.file = file;
	}

	
	@Override
	public Iterator<Pixel> iterator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public double get(Pixel p) {
		return get(p.x(), p.y());
	}

	@Override
	public void put(int x, int y, double value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void put(Pixel p, double value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void put(Raster r, double value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void init(double v) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int tileWidth() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int tileHeight() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int numXTiles() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int numYTiles() {
		throw new UnsupportedOperationException();
	}

	

	@Override
	public double minV() {
		throw new UnsupportedOperationException();
	}

	@Override
	public double maxV() {
		throw new UnsupportedOperationException();
	}

	
	@Override
	public int noDataValue() {
		return Raster.getNoDataValue();
	}

	@Override
	public void display() {
		throw new UnsupportedOperationException();
	}

	

	@Override
	public Set<Integer> values() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void getCouples(Matrix horizontals, Matrix verticals) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void visualize() {
		throw new UnsupportedOperationException();
	}

	@Override
	public MatrixType getType() {
		throw new UnsupportedOperationException();
	}

	@Override
	public double getActiveArea() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean contains(Pixel p) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean contains(int x, int y) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void resetValues() {
		// TODO Auto-generated method stub
		
	}

}
