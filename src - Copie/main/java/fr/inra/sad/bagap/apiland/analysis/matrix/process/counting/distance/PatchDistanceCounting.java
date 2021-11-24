package fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.distance;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.Counting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.CountingDecorator;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.patch.Patch;
import fr.inra.sad.bagap.apiland.patch.PatchComposite;

public class PatchDistanceCounting extends CountingDecorator {
	
	private PatchComposite patches;
	
	private boolean calculated;
	
	private double count, sum, max;	
	
	private Map<Integer, Double> counts;
	
	private Map<Integer, Double> sums;
	
	private Map<Integer, Double> maxs;
	
	private Map<Patch, Double> areas;
	
	public PatchDistanceCounting(Counting decorate) {
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
		//System.out.println("ask to add");
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
		maxs = new HashMap<Integer, Double>();
		areas = new HashMap<Patch, Double>();
		
		calculated = false;
	}
	
	@Override
	protected void doDelete() {
		patches = null;
		counts = null;
		sums = null;
		maxs = null;
		areas = null;
	}
	
	@Override
	public PatchComposite patches(){
		return patches;
	}
	
	public void calculate(){
		count = 0;
		max = 0;
		sum = 0;
		counts.clear();
		sums.clear();
		maxs.clear();
		areas.clear();
		double v;
		int size;
		double area;
		double sumWeighted;
		double s = Math.pow(Raster.getCellSize(), 2);
		for(Patch p : patches.patches()){
			v = p.getValue();
			if(v != 0){
				if(!counts.containsKey((int) v)){
					counts.put((int) v, 0.0);
					sums.put((int) v, 0.0);
					maxs.put((int) v, 0.0);
				}
				size = p.pixels().size();
				sumWeighted = 0;
				for(Pixel pixel : p.pixels()){
					sumWeighted += process().window().weighted()[pixel.y()][pixel.x()];
				}
				if(sumWeighted > 0){
					count += sumWeighted / size;
					counts.put((int) v, counts.get((int) v) + sumWeighted / size); 
					area = (sumWeighted * s) / 10000.0;
					areas.put(p, area);
					max = Math.max(max, area);
					maxs.put((int) v, Math.max(maxs.get((int) v), area)); 
					sum += area;
					sums.put((int) v, sums.get((int) v) + area);
				}
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
				if(areas.containsKey(p)){
					area = areas.get(p);
					shdi += area/sum * Math.log(area/sum);
				}
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
				if(areas.containsKey(p)){
					area = areas.get(p);
					sumClasse = sums.get(classe);
					shdi += area/sumClasse * Math.log(area/sumClasse);
				}
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
		return sum / count;
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
	
}
