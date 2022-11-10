package fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.distance;

import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.Counting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.CountingDecorator;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.patch.Patch;
import fr.inra.sad.bagap.apiland.patch.PatchComposite;

public class PatchDistanceCountingOld extends CountingDecorator {
	
	private PatchComposite patches;
	
	public PatchDistanceCountingOld(Counting decorate) {
		super(decorate);
	}

	@Override
	protected void doDown(int d, int place){
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
		double nb = 0;
		for(Patch p : patches.patches()){
			if(p.getValue() != 0){
				double pnb = 0;
				for(Pixel pixel : p.pixels()){
					pnb += process().window().weighted()[pixel.y()][pixel.x()];
				}
				pnb /= p.pixels().size();
				/*
				if(pnb > 0){
					pnb /= p.pixels().size();
				}else{
					pnb = 0;
				}
				*/
				nb += pnb;
			}
		}
		if(nb > 0){
			return nb;
		}else{
			return 0;
		}
		//return nb;
	}
	
	@Override
	public double getPatchNumber(int classe){
		double nb = 0;
		for(Patch p : patches.patches()){
			if(p.getValue() == classe){
				double pnb = 0;
				for(Pixel pixel : p.pixels()){
					pnb += process().window().weighted()[pixel.y()][pixel.x()];
				}
				pnb /= p.pixels().size();
				nb += pnb;
			}
		}
		if(nb > 0){
			return nb;
		}else{
			return 0;
		}
		//return nb;
	}
	
	@Override
	public double getLargestPatchSize(){
		double value = 0;
		for(Patch p : patches.patches()){
			if(p.getValue() != 0){
				double area = 0;
				for(Pixel pixel : p.pixels()){
					area += process().window().weighted()[pixel.y()][pixel.x()];
				}
				area *= Math.pow(Raster.getCellSize(), 2);
				value = Math.max(value, area);
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
				double area = 0;
				for(Pixel pixel : p.pixels()){
					area += process().window().weighted()[pixel.y()][pixel.x()];
				}
				area *= Math.pow(Raster.getCellSize(), 2);
				value = Math.max(value, area);
			}
		}
		value /= 10000.0; // en hectares
		return value;
	}
	
	@Override
	public double getMeanPatchSize(){
		double area = 0;
		double nb = 0;
		for(Patch p : patches.patches()){
			if(p.getValue() != 0){
				double parea = 0;
				double pnb = 0;
				double weight;
				for(Pixel pixel : p.pixels()){
					weight = process().window().weighted()[pixel.y()][pixel.x()];
					parea += weight;
					pnb += weight;
				}
				if(parea > 0){
					parea *= Math.pow(Raster.getCellSize(), 2);
					area += parea;
				}
				
				if(pnb > 0){
					pnb /= p.pixels().size();
					nb += pnb;
				}
				
			}
		}
		if(nb != 0){
			return area / nb;
		}else{
			return 0;
		}
	}
	
	@Override
	public double getMeanPatchSize(int classe){
		double area = 0;
		double nb = 0;
		for(Patch p : patches.patches()){
			if(p.getValue() == classe){
				double parea = 0;
				double pnb = 0;
				double weight;
				for(Pixel pixel : p.pixels()){
					weight = process().window().weighted()[pixel.y()][pixel.x()];
					parea += weight;
					pnb += weight;
				}
				if(parea > 0){
					parea *= Math.pow(Raster.getCellSize(), 2);
					area += parea;
				}
				if(pnb > 0){
					pnb /= p.pixels().size();
					nb += pnb;
				}
			}
		}
		if(nb != 0){
			return area / nb;
		}else{
			return 0;
		}
	}
	
	/*
	@Override
	public double getStandardDeviationPatchSize(){
		double value = 0;
		Stats s = new Stats();
		for(Patch p : patches.patches()){
			if(p.getValue() != 0){
				double parea = 0;
				for(Pixel pixel : p.pixels()){
					parea += process().window().weighted()[pixel.y()][pixel.x()];
				}
				parea *= Math.pow(Raster.getCellSize(), 2);
				s.add(parea);
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
				double parea = 0;
				for(Pixel pixel : p.pixels()){
					parea += process().window().weighted()[pixel.y()][pixel.x()];
				}
				parea *= Math.pow(Raster.getCellSize(), 2);
				s.add(parea);
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
				double parea = 0;
				for(Pixel pixel : p.pixels()){
					parea += process().window().weighted()[pixel.y()][pixel.x()];
				}
				s.add(parea);
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
				double parea = 0;
				for(Pixel pixel : p.pixels()){
					parea += process().window().weighted()[pixel.y()][pixel.x()];
				}
				s.add(parea);
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
