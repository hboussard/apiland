package fr.inra.sad.bagap.apiland.analysis.matrix.process.counting;

import fr.inra.sad.bagap.apiland.analysis.matrix.process.SimpleWindowMatrixProcess;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;

// BasicCounting is available both for Threshold Window and Distance Window
// implies that as the (classic) threshold window counting (ex : ValueCounting, CoupleCounting, etc)
// is managed by decorating BasicCounting, Distance window counting could be managed by decorating BasicCoutning
// is it the same decoration ?
public class BasicCounting extends Counting {

	/** the count of known values (different to 0 and Raster.noDataValues) */
	private int countValues;
	
	//private int theoricalSize;
	
	/** the total count of values (including 0 and Raster.noDataValues)*/
	private int totalValues;
	
	/** the total count of valid values (including 0 but different to Raster.noDataValue() */
	private int validValues;
	
	/** to init properly the counting */
	@Override
	public void init() {
		countValues = 0;
		totalValues = 0;
		validValues = 0;
		//theoricalSize = process().window().theoricalSize();
	}
	
	@Override
	public SimpleWindowMatrixProcess process(){
		return (SimpleWindowMatrixProcess) super.process();
	}
	
	/**
	 * to add a value to the counting
	 * @param value : the value to add
	 * @param filter : the filter
	 * @param ch : horizontal couple
	 * @param cv : vertical couple
	 */
	@Override
	public void add(double value, int x, int y, int filter, double ch, double cv) {
		addValue(value, x, y);
	}
	
	@Override
	public void addValue(double value, int x, int y) {
		totalValues++;
		if(value != Raster.getNoDataValue()){
			validValues++;
			if(value != 0){
				countValues++;
			}
		}
	}
	
	@Override
	public void addCouple(double couple) {
		// do nothing
	}

	@Override
	public void removeValue(double value, int x, int y) {
		totalValues--;
		if(value != Raster.getNoDataValue()){
			validValues--;
			if(value != 0){
				countValues--;
			}
		}
	}
	
	@Override
	public void removeCouple(double couple) {
		// do nothing
	}
	
	@Override
	public void down() {
		int outx, outy;
		// on enleve les valeurs qui ne sont plus actuelles
		//System.out.println("enleve les valeurs "+window().removeDownList());
		for(Pixel p : process().window().removeDownList()){
			outx = process().window().outXWindow(p.x());
			outy = process().window().outYWindow(p.y());
			if(outy >= 0 && outy < process().processType().matrix().height()
					&& outx >= 0 && outx < process().processType().matrix().width()){
				//System.out.println("remove "+p+" "+values[p.y()][p.x()]);
				process().counting().removeValue(process().values()[p.y()][p.x()], p.x(), p.y());
			}
		}
		// on ajoute les valeurs qui sont devenues actuelles
		//System.out.println("ajoute les valeurs "+window().addDownList());
		for(Pixel p : process().window().addDownList()){
			outx = process().window().outXWindow(p.x());
			outy = process().window().outYWindow(p.y());
			if(outy >= 0 && outy < process().processType().matrix().height()
					&& outx >= 0 && outx < process().processType().matrix().width()){
				//System.out.println("add "+p+" "+values[p.y()][p.x()]);
				process().counting().addValue(process().values()[p.y()][p.x()], p.x(), p.y());
			}
		}
	}
	
	@Override
	public int totalValues(){
		return totalValues;
	}
	
	@Override
	public int validValues(){
		return validValues;
	}
	
	@Override
	public int theoricalSize(){
		return process().window().theoricalSize();
	}
	
	@Override
	public int countValues(){
		return countValues;
	}

	@Override
	public void delete() {
		// do nothing
	}
	
}
