package fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.threshold;

import java.util.HashMap;
import java.util.Map;

import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.Counting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.CountingDecorator;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.patch.Patch;
import fr.inra.sad.bagap.apiland.patch.PatchComposite;

public class PatchCountingOld extends CountingDecorator {
	
	private PatchComposite patches;
	
	public PatchCountingOld(Counting decorate) {
		super(decorate);
	}

	@Override
	protected void doDown(int d, int place){
		/*
		int outx, outy;
		int v;
		// on enleve les valeurs qui ne sont plus actuelles
		//System.out.println("enleve les valeurs "+window().removeDownList());
		for(Pixel p : process().window().removeDownList()){
			outx = process().window().outXWindow(p.x());
			outy = process().window().outYWindow(p.y());
			if(outy >= 0 && outy < process().processType().matrix().height()
					&& outx >= 0 && outx < process().processType().matrix().width()){
				v = (int) process().values()[p.y()][p.x()];
				if(v != Raster.getNoDataValue()){
					patches.removePixel(p, v);
				}
			}
		}
		// on ajoute les valeurs qui sont devenues actuelles
		//System.out.println("ajoute les valeurs "+window().addDownList());
		for(Pixel p : process().window().addDownList()){
			outx = process().window().outXWindow(p.x());
			outy = process().window().outYWindow(p.y());
			if(outy >= 0 && outy < process().processType().matrix().height()
					&& outx >= 0 && outx < process().processType().matrix().width()){
				v = (int) process().values()[p.y()][p.x()];
				if(v != Raster.getNoDataValue()){
					patches.addPixel(p, v);
				} 
			}
		}
		*//*
		if(process().window().pixel().x() == 5000){
			patches.info();
		}*/
		
		patches.upPixels();
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
	}
	
	@Override
	protected void doDelete() {
		patches = null;
	}
	
	@Override
	public PatchComposite patches(){
		return patches;
	}
	
	@Override
	public double getPatchNumber(){
		int nb = 0;
		for(Patch p : patches.patches()){
			if(p.getValue() != 0){
				nb++;
			}
		}
		return nb;
	}
	
	@Override
	public double getPatchNumber(int classe){
		int nb = 0;
		for(Patch p : patches.patches()){
			if(p.getValue() == classe){
				nb++;
			}
		}
		return nb;
	}
	
	@Override
	public double getLargestPatchSize(){
		double value = 0;
		for(Patch p : patches.patches()){
			if(p.getValue() != 0){
				value = Math.max(value, p.getArea());
			}
		}
		value /= 10000.0; // en hectares
		return value;
	}
	
	@Override
	public double getLargestPatchSize(int classe){
		double value = 0;
		for(Patch p : patches.patches()){
			if(p.getValue() == classe){
				value = Math.max(value, p.getArea());
			}
		}
		value /= 10000.0; // en hectares
		return value;
	}
	
	@Override
	public double getMeanPatchSize(){
		double value = 0;
		double count = 0;
		for(Patch p : patches.patches()){
			if(p.getValue() != 0){
				value += p.getArea();
				count++;
			}
		}
		if(count == 0){
			value = 0;
		}else{
			value /= (double) count;
		}
		return value;
	}
	
	@Override
	public double getMeanPatchSize(int classe){
		double value = 0;
		double count = 0;
		for(Patch p : patches.patches()){
			if(p.getValue() == classe){
				value += p.getArea();
				count++;
			}
		}
		if(count == 0){
			value = 0;
		}else{
			value /= (double) count;
		}
		return value;
	}
	
	@Override
	public double getShannonDiversityPatchSize(){
		
		Map<Patch, Double> areas = new HashMap<Patch, Double>();
		double area, sum = 0;
		for(Patch p : patches.patches()){
			if(p.getValue() != 0){
				area = p.getArea() / 10000.0; // en hectares
				areas.put(p, area);
				sum += area;
			}
		}
		
		double value = 0;
		for(Patch p : patches.patches()){
			if(p.getValue() != 0){
				area = areas.get(p);
				value += area/sum * Math.log(area/sum);
			}
		}
		
		if(value != 0){
			value *= -1;
		}
		
		return value;
	}
	
	@Override
	public double getShannonDiversityPatchSize(int classe){
		
		Map<Patch, Double> areas = new HashMap<Patch, Double>();
		double area, sum = 0;
		for(Patch p : patches.patches()){
			if(p.getValue() == classe){
				area = p.getArea() / 10000.0; // en hectares
				areas.put(p, area);
				sum += area;
			}
		}
		
		double value = 0;
		for(Patch p : patches.patches()){
			if(p.getValue() == classe){
				area = areas.get(p);
				value += area/sum * Math.log(area/sum);
			}
		}
		
		if(value != 0){
			value *= -1;
		}
		
		return value;
	}
	
	/*
	@Override
	public double getStandardDeviationPatchSize(){
		double value = 0;
		Stats s = new Stats();
		for(Patch p : patches.patches()){
			if(p.getValue() != 0){
				s.add(p.getArea());
			}
		}
		s.calculate();
		if(s.size() == 0){
			value = 0;
		}else{
			value = s.getStandardDeviation();
		}
		return value;
	}
	
	@Override
	public double getStandardDeviationPatchSize(int classe){
		double value = 0;
		Stats s = new Stats();
		for(Patch p : patches.patches()){
			if(p.getValue() == classe){
				s.add(p.getArea());
			}
		}
		s.calculate();
		if(s.size() == 0){
			value = 0;
		}else{
			value = s.getStandardDeviation();
		}
		return value;
	}
	
	@Override
	public double getVariationCoefficientPatchSize(){
		double value = 0;
		Stats s = new Stats();
		for(Patch p : patches.patches()){
			if(p.getValue() != 0){
				s.add(p.getArea());
			}
		}
		s.calculate();
		if(s.size() == 0){
			value = 0;
		}else{
			value = s.getVariationCoefficient();
		}
		return value;
	}
	
	@Override
	public double getVariationCoefficientPatchSize(int classe){
		double value = 0;
		Stats s = new Stats();
		for(Patch p : patches.patches()){
			if(p.getValue() == classe){
				s.add(p.getArea());
			}
		}
		s.calculate();
		if(s.size() == 0){
			value = 0;
		}else{
			value = s.getVariationCoefficient();
		}
		return value;
	}
	*/
}
