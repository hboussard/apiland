package fr.inra.sad.bagap.apiland.analysis.matrix;

import java.util.Collection;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.JaiMatrixFactory;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;

public class ChamferDistanceCalculation extends MatrixCalculation {
 
	private final static int[][] chamfer13 = new int[][] {
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
	
	private Matrix matrix;
	
	//private Set<Integer> codes;
	private Collection<Integer> codes;
	
	private double threshold;
 
	public ChamferDistanceCalculation(Matrix input, Collection<Integer> codes) {
		this(input, codes, Raster.getNoDataValue());
	}
	
	public ChamferDistanceCalculation(Matrix input, Collection<Integer> codes, double threshold) {
		super(input);
		this.chamfer = ChamferDistanceCalculation.chamfer13;
		this.normalizer = this.chamfer[0][2];
		this.matrix = input;
		this.codes = codes;
		this.threshold = threshold;
	}

	@Override
	protected void doRun() {
		setResult(compute(matrix, codes));
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
 
	private Matrix compute(Matrix input, Collection<Integer> codes) {
		int[] tab = new int[codes.size()];
		int index = 0;
		for(int i : codes){
			tab[index++] = i;
		}
		return compute(input, tab);
	}
	
	private Matrix compute(Matrix input, int... code) {
		//Matrix output = MatrixFactory.get(input.getType()).create(input.width(), input.height(), input.cellsize(), input.minX(), input.maxX(), input.minY(), input.maxY(), input.noDataValue());
		Matrix output = JaiMatrixFactory.get().create(input.width(), input.height(), input.cellsize(), input.minX(), input.maxX(), input.minY(), input.maxY(), input.noDataValue());
		//Matrix output = MatrixFactory.get(input.getType()).create(input);
		this.width = input.width();
		this.height = input.height();
		
		int total = (this.width * this.height) * 5;
		//System.out.println("1");
		// initialize distance
		boolean hasCode = false;
		for(int y=0; y<height; y++){
			//System.out.println("1 : "+y);
			for(int x=0; x<width; x++){
				double v = input.get(x, y);
				boolean ok = false;
				if(v != Raster.getNoDataValue()){
					for(int c : code){
						if(c == v){
							ok = true;
							hasCode = true;
							break;
						}
					}
				}
				if(ok){
					output.put(x, y, 0); // inside the object -> distance=0
				}else{
					output.put(x, y, Raster.getNoDataValue()); // outside the object -> to be computed
				}
				updateProgression(total);
			}
		}
		if(!hasCode){
			return output;
		}
		//System.out.println("2");
		// forward
		for(int y=0; y<=height-1; y++) {
			//System.out.println("2 : "+y);
			for(int x=0; x<=width-1; x++) {
				double v = output.get(x, y);
				if(v == Raster.getNoDataValue()) {
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
				updateProgression(total);
			}
		}
		//System.out.println("3");
		// backward
		for(int y=height-1; y>=0; y--) {
			//System.out.println("3 : "+y);
			for(int x=width-1; x>=0; x--) {
				double v = output.get(x, y);
				if(v == Raster.getNoDataValue()) {
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
				updateProgression(total);
			}
		}
		//System.out.println("4");
		// normalize
		for(int y=0; y<height; y++){
			//System.out.println("4 : "+y);
			for(int x=0; x<width; x++){
				double v = output.get(x, y);
				if(v == Raster.getNoDataValue()) {
					continue;
				}
				//System.out.println(output.get(x, y)+" "+normalizer+" "+input.cellsize());
				output.put(x, y, (v/normalizer) * input.cellsize());
				updateProgression(total);
			}
		}
		//System.out.println("5");
		//nettoyage
		for(int y=0; y<height; y++){
			//System.out.println("1 : "+y);
			for(int x=0; x<width; x++){
				double v = input.get(x, y);
				if(v == Raster.getNoDataValue()){
					output.put(x, y, Raster.getNoDataValue());
				}else{
					double v2 = output.get(x, y);
					if(threshold != Raster.getNoDataValue() && v2 > threshold){
						output.put(x, y, threshold);
					}
				}
				updateProgression(total);
			}
		}
		
		return output;
	}

}