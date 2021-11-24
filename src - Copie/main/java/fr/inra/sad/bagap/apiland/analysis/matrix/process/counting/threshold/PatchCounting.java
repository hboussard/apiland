package fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.threshold;

import java.util.HashMap;
import java.util.Map;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.Counting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.CountingDecorator;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.patch.Patch;
import fr.inra.sad.bagap.apiland.patch.PatchComposite;

public class PatchCounting extends CountingDecorator {
	
	private PatchComposite patches;
	
	private boolean calculated;
	
	private double count, sum, max, squaresum;
	
	private Map<Integer, Double> counts;
	
	private Map<Integer, Double> sums;
	
	private Map<Integer, Double> squaresums;
	
	private Map<Integer, Double> maxs;
	
	private Map<Patch, Double> areas;
	
	public PatchCounting(Counting decorate) {
		super(decorate);
	}

	@Override
	protected void doDown(int d, int place){
		patches.upPixels();
		calculated = false;
	}
	
	@Override
	protected void doAdd(double value, int x, int y, int filter, double ch, double cv) {
		doAddValue(value, x, y);
	}
	
	@Override
	protected void doAddValue(double value, int x, int y) {
		//System.out.println("add "+value+" "+x+" "+y);
		if(value != Raster.getNoDataValue()){
			patches.addPixel(new Pixel(x, y), (int) value);
		}
	}
	
	@Override
	protected void doRemoveValue(double value, int x, int y) {
		//System.out.println("ask to remove");
		if(value != Raster.getNoDataValue()){
			patches.removePixel(new Pixel(x, y), (int) value);
		}
	}
	
	@Override
	protected void doInit() {
		patches = new PatchComposite();
		
		counts = new HashMap<Integer, Double>();
		sums = new HashMap<Integer, Double>();
		squaresums = new HashMap<Integer, Double>();
		maxs = new HashMap<Integer, Double>();
		areas = new HashMap<Patch, Double>();
		
		calculated = false;
	}
	
	@Override
	protected void doDelete() {
		patches = null;
		counts = null;
		sums = null;
		squaresums = null;
		maxs = null;
		areas = null;
	}
	
	@Override
	public PatchComposite patches(){
		return patches;
	}
	
	private void calculate(){
		count = 0.0;
		sum = 0.0;
		squaresum = 0.0;
		max = 0.0;
		counts.clear();
		sums.clear();
		squaresums.clear();
		maxs.clear();
		areas.clear();
		double area;
		double v;
		for(Patch p : patches.patches()){
			v = p.getValue();
			if(v != 0){
				if(!counts.containsKey((int) v)){
					counts.put((int) v, 0.0);
					sums.put((int) v, 0.0);
					squaresums.put((int) v, 0.0);
					maxs.put((int) v, 0.0);
				}
				count++;
				counts.put((int) v, counts.get((int) v) + 1); 
				area = p.getArea() / 10000.0;
				areas.put(p, area);
				max = Math.max(max, area);
				maxs.put((int) v, Math.max(maxs.get((int) v), area)); 
				sum += area;
				sums.put((int) v, sums.get((int) v) + area);
				squaresum += Math.pow(area, 2);
				squaresums.put((int) v, squaresums.get((int) v) + Math.pow(area, 2));
				//if(p.getValue() == classe){
			}
		}
		calculated = true;
	}
	
	@Override
	public double getShannonDiversityPatchSize(){
		if(!calculated){
			calculate();
		}
		double area, shdi = 0;
		for(Patch p : patches.patches()){
			if(p.getValue() != 0){
				area = areas.get(p);
				shdi += area/sum * Math.log(area/sum);
			}
		}
		if(shdi != 0){
			shdi *= -1;
		}
		return shdi;
	}
	
	@Override
	public double getShannonDiversityPatchSize(int classe){
		if(!calculated){
			calculate();
		}
		double area, sumClasse, shdi = 0;
		for(Patch p : patches.patches()){
			if(p.getValue() == classe){
				area = areas.get(p);
				sumClasse = sums.get(classe);
				shdi += area/sumClasse * Math.log(area/sumClasse);
			}
		}
		if(shdi != 0){
			shdi *= -1;
		}
		return shdi;
	}
	
	@Override
	public double getPatchNumber(){
		if(!calculated){
			calculate();
		}
		return count;
	}
	
	@Override
	public double getPatchNumber(int classe){
		if(!calculated){
			calculate();
		}
		if(counts.containsKey(classe)){
			return counts.get(classe);
		}
		return 0;
	}
	
	@Override
	public double getLargestPatchSize(){
		if(!calculated){
			calculate();
		}
		return max;
	}
	
	@Override
	public double getLargestPatchSize(int classe){
		if(!calculated){
			calculate();
		}
		if(maxs.containsKey(classe)){
			return maxs.get(classe);
		}
		return 0;
	}
	
	@Override
	public double getMeanPatchSize(){
		if(!calculated){
			calculate();
		}
		/*
		if(count != 0){
			return sum/count;
		}else{
			return 0;
		}*/
		return sum/count;
	}
	
	@Override
	public double getMeanPatchSize(int classe){
		if(!calculated){
			calculate();
		}
		if(sums.containsKey(classe)){
			return sums.get(classe) / counts.get(classe);
		}
		return 0;
	}
	
	@Override
	public double getStandardDeviationPatchSize(){
		if(!calculated){
			calculate();
		}
		double value = (squaresum/count) - Math.pow(sum/count, 2);
		
		return value;
	}
	
	@Override
	public double getStandardDeviationPatchSize(int classe){
		if(!calculated){
			calculate();
		}
		if(sums.containsKey(classe)){
			double value = (squaresums.get(classe)/counts.get(classe)) - Math.pow(sums.get(classe)/counts.get(classe), 2);
			
			return value;
		}
		return 0;
	}
	
	
	
}
