package fr.inra.sad.bagap.apiland.analysis.matrix.process.counting;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.patch.PatchComposite;

public class PatchCounting extends CountingDecorator {
	
	private PatchComposite patches;
	
	public PatchCounting(Counting decorate) {
		super(decorate);
	}

	@Override
	protected void doDown(){
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
	
}
