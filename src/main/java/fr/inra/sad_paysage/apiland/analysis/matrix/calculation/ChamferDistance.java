package fr.inra.sad_paysage.apiland.analysis.matrix.calculation;

import java.util.Set;

import fr.inra.sad_paysage.apiland.core.space.impl.raster.matrix.Matrix;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.matrix.MatrixFactory;

/**
 * Chamfer distance
 * 
 * @author Code by Xavier Philippeau <br> Kernels by Verwer, Borgefors and Thiel 
 */
public class ChamferDistance  {
 
	/*
	public final static int[][] cheessboard = new int[][] {
		new int[] {1,0,1},
		new int[] {1,1,1}
	};
 
	public final static int[][] chamfer3 = new int[][] {
		new int[] {1,0,3},
		new int[] {1,1,4}
	};
 
	public final static int[][] chamfer5 = new int[][] {
		new int[] {1,0,5},
		new int[] {1,1,7},
		new int[] {2,1,11}
	};
 
	public final static int[][] chamfer7 = new int[][] {
		new int[] {1,0,14},
		new int[] {1,1,20},
		new int[] {2,1,31},
		new int[] {3,1,44}
	};*/
 
	public final static int[][] chamfer13 = new int[][] {
		new int[] {1, 0, 68},
		new int[] {1, 1, 96},
		new int[] {2, 1, 152},
		new int[] {3, 1, 215},
		new int[] {3, 2, 245},
		new int[] {4, 1, 280},
		new int[] {4, 3, 340},
		new int[] {5, 1, 346},
		new int[] {6, 1, 413}
	};
 
	private int[][] chamfer = null; 
	
	private int normalizer = 0; 
 
	private int width=0,height=0;
 
	public ChamferDistance() {
		this(ChamferDistance.chamfer13);
	}
 
	public ChamferDistance(int[][] chamfermask) {
		this.chamfer = chamfermask;
		this.normalizer = this.chamfer[0][2];
	}
 
	private void testAndSet(Matrix output, int x, int y, double newvalue) {
		if(x<0 || x>=width){
			return;
		}
		if(y<0 || y>=height){
			return;
		}
		double v = output.get(x, y);
		if (v>=0 && v<newvalue){
			return;
		}
		output.put(x, y, newvalue);
	}
	
	private void testAndSet(double[][] output, int x, int y, double newvalue) {
		if(x<0 || x>=width){
			return;
		}
		if(y<0 || y>=height){
			return;
		}
		double v = output[x][y];
		if (v>=0 && v<newvalue){
			return;
		}
		output[x][y] = newvalue;
	}
 
	public Matrix compute(Matrix input, Set<Integer> codes) {
		int[] tab = new int[codes.size()];
		int index = 0;
		for(int i : codes){
			tab[index++] = i;
		}
		return compute(input, tab);
	}
	
	public Matrix compute(Matrix input, int... code) {
		Matrix output = MatrixFactory.get(input.getType()).create(input.width(), input.height(), input.cellsize(), input.minX(), input.maxX(), input.minY(), input.maxY(), input.noDataValue());
		//Matrix output = MatrixFactory.get(input.getType()).create(input);
		this.width = input.width();
		this.height = input.height();
		
		// initialize distance
		for(int y=0; y<height; y++){
			for(int x=0; x<width; x++){
				double v = input.get(x, y);
				boolean ok = false;
				for(int c : code){
					if(c == v){
						ok = true;
						break;
					}
				}
				if(ok){
					output.put(x, y, 0); // inside the object -> distance=0
				}else{
					output.put(x, y, -1); // outside the object -> to be computed
				}
			}
		}
		// forward
		for(int y=0; y<=height-1; y++) {
			for(int x=0; x<=width-1; x++) {
				double v = output.get(x, y);
				if(v<0) {
					continue;
				}
				for(int k=0; k<chamfer.length; k++) {
					int dx = chamfer[k][0];
					int dy = chamfer[k][1];
					int dt = chamfer[k][2];
					
					testAndSet(output, x+dx, y+dy, v+dt);
					if(dy!=0) {
						testAndSet(output, x-dx, y+dy, v+dt);
					}
					if(dx!=dy) {
						testAndSet(output, x+dy, y+dx, v+dt);
						if (dy!=0) {
							testAndSet(output, x-dy, y+dx, v+dt);
						}
					}
				}
			}
		}
		
		// backward
		for(int y=height-1; y>=0; y--) {
			for(int x=width-1; x>=0; x--) {
				double v = output.get(x, y);
				if(v<0) {
					continue;
				}
				for(int k=0;k<chamfer.length;k++) {
					int dx = chamfer[k][0];
					int dy = chamfer[k][1];
					int dt = chamfer[k][2];
					
					testAndSet(output, x-dx, y-dy, v+dt);
					if(dy!=0){
						testAndSet(output, x+dx, y-dy, v+dt);
					}
					if(dx!=dy){
						testAndSet(output, x-dy, y-dx, v+dt);
						if (dy!=0) testAndSet(output, x+dy, y-dx, v+dt);
					}
				}
			}
		}
		
		// normalize
		for(int y=0; y<height; y++){
			for(int x=0; x<width; x++){
				output.put(x, y, (output.get(x, y)/normalizer) * input.cellsize());
			}
		}
		
		return output;
	}
	
	public double[][] compute(int[][] input) {
 
		this.width = input[0].length;
		this.height = input.length;
		double[][] output = new double[width][height]; 
		
		// initialize distance
		for(int y=0; y<height; y++)
			for(int x=0; x<width; x++)
				if(input[x][y] == 1){
					output[x][y] = 0; // inside the object -> distance=0
				}else{
					output[x][y] = -1; // outside the object -> to be computed
				}
		// forward
		for(int y=0; y<=height-1; y++) {
			for(int x=0; x<=width-1; x++) {
				double v = output[x][y];
				if(v<0) {
					continue;
				}
				for(int k=0; k<chamfer.length; k++) {
					int dx = chamfer[k][0];
					int dy = chamfer[k][1];
					int dt = chamfer[k][2];
 
					testAndSet(output, x+dx, y+dy, v+dt);
					if(dy!=0) {
						testAndSet(output, x-dx, y+dy, v+dt);
					}
					if(dx!=dy) {
						testAndSet(output, x+dy, y+dx, v+dt);
						if (dy!=0) {
							testAndSet(output, x-dy, y+dx, v+dt);
						}
					}
				}
			}
		}
 
		// backward
		for(int y=height-1; y>=0; y--) {
			for(int x=width-1; x>=0; x--) {
				double v = output[x][y];
				if(v<0) {
					continue;
				}
				for(int k=0;k<chamfer.length;k++) {
					int dx = chamfer[k][0];
					int dy = chamfer[k][1];
					int dt = chamfer[k][2];
 
					testAndSet(output, x-dx, y-dy, v+dt);
					if(dy!=0){
						testAndSet(output, x+dx, y-dy, v+dt);
					}
					if(dx!=dy){
						testAndSet(output, x-dy, y-dx, v+dt);
						if (dy!=0) testAndSet(output, x+dy, y-dx, v+dt);
					}
				}
			}
		}
 
		// normalize
		for(int y=0; y<height; y++){
			for(int x=0; x<width; x++){
				output[x][y] = output[x][y]/normalizer;
			}
		}
		return output;
	}
	
	/*
	public static void main(String[] args){
		
		int[][] array = new int[10][10];
		for(int j=0; j<10; j++){
			for(int i=0; i<10; i++){
				if(i == 5 && j == 5){
					array[j][i] = 1;
				}else{
					array[j][i] = 0;
				}
			}
		}
		
		double[][] distancemap = new ChamferDistance().compute(array);
		for(int j=0; j<10; j++){
			for(int i=0; i<10; i++){
				System.out.print(distancemap[i][j]+" ");
			}
			System.out.println();
		}
		System.out.println();
		
		String path = "C:/Hugues/temp/chloe/qualitative/";
		try {
			//Matrix m = JaiMatrixFactory.get().createWithAsciiGrid(path+"raster2007.asc");
			Matrix m = JaiMatrixFactory.get().createWithAsciiGrid(path+"r.asc");
			MatrixManager.visualize(m);
			
			Matrix m1 = new ChamferDistance().compute(m, 1);
			MatrixManager.exportAsciiGridAndVisualize(m1, path+"m1.asc");
		
			
			Map<Domain<Double,Double>, Integer> domains = new HashMap<Domain<Double,Double>, Integer>();
			domains.put(new BoundedDomain(">", 0, "<=", 500), 1);
			domains.put(new BoundedDomain(">", 500, "<=", 1000), 2);
			domains.put(new BoundedDomain(">", 1000, "<=", 5000), 3);
			domains.put(new NumberDomain(">", 5000), 4);
			
			Matrix c = new Classification(m1, domains).allRun();
			MatrixManager.exportAsciiGridAndVisualize(c, path+"c.asc");
			
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
	}
*/
}