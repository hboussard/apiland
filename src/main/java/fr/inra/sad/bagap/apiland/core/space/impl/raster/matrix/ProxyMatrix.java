package fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix;

import java.util.Iterator;
import java.util.Set;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;

public class ProxyMatrix implements Matrix {

	private static final long serialVersionUID = 1L;

	private int height, width;
	
	private double minX, maxY ,minY, maxX;
	
	private int noDataValue;
	
	private double cellsize; 
	
	public void setWidth(int w){
		this.width = w;
	}
	
	public void setHeight(int h){
		this.height = h;
	}
	
	public void setMinX(double m){
		this.minX = m;
	}
	
	public void setMaxX(double m){
		this.maxX = m;
	}
	
	public void setMinY(double m){
		this.minY = m;
	}
	
	public void setMaxY(double m){
		this.maxX = m;
	}
	
	public void setNoDataValue(int ndv){
		this.noDataValue = ndv;
	}
	
	public void setCellSize(double cs){
		this.cellsize = cs;
	}
	
	@Override
	public int width() {
		return width;
	}

	@Override
	public int height() {
		return height;
	}
	
	public int size(){
		return height * width;
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
	public int noDataValue() {
		return noDataValue;
	}
	
	@Override
	public Iterator<Pixel> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double get(int x, int y) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double get(Pixel p) {
		// TODO Auto-generated method stub
		return 0;
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
	public void init(double v) {
		// TODO Auto-generated method stub
		
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
	public void display() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getFile() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFile(String file) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<Integer> values() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void getCouples(Matrix horizontals, Matrix verticals) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visualize() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public MatrixType getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getActiveArea() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean contains(Pixel p) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean contains(int x, int y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void resetValues() {
		// TODO Auto-generated method stub
		
	}

}
