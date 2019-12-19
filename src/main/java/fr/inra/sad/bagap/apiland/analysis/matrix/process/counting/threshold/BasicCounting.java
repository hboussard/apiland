package fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.threshold;

import fr.inra.sad.bagap.apiland.analysis.matrix.process.SimpleWindowMatrixProcess;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.Counting;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;

// BasicCounting is available both for Threshold Window and Distance Window
// implies that as the (classic) threshold window counting (ex : ValueCounting, CoupleCounting, etc)
// is managed by decorating BasicCounting, Distance window counting could be managed by decorating BasicCoutning
// is it the same decoration ?
public class BasicCounting extends Counting {

	/** the count of known values (different to 0 and Raster.noDataValues) */
	private int countValues;
	
	/** the total count of values (including 0 and Raster.noDataValues)*/
	private int totalValues;
	
	/** the total count of valid values (including 0 but different to Raster.noDataValue() */
	private int validValues;
	
	private int test = 0;
	
	@Override
	public void init() {
		countValues = 0;
		totalValues = 0;
		validValues = 0;
	}
	
	@Override
	public SimpleWindowMatrixProcess process(){
		return (SimpleWindowMatrixProcess) super.process();
	}
		
	@Override
	public void add(double value, int x, int y, int filter, double ch, double cv) {
		//System.out.println("add (BasicCounting)");
		addValue(value, x, y);
	}
	
	@Override
	public void addValue(double value, int x, int y) {
		
		//System.out.println("add "+value+" "+x+" "+y+" "+totalValues);
		/*
		if(process().window().pixel().x() == 0){
			System.out.println("add "+process().window().diameter()+" "+process().window().pixel()+" "+x+" "+y+" --> "+value);
		}*/
		
		totalValues++;
		if(value != Raster.getNoDataValue()){
			//System.out.println("addValue (BasicCounting) "+value+" "+(++test));
			validValues++;
			if(value != 0){
				countValues++;
			}
		}
	}
	
	@Override
	public void addCouple(double couple, int x1, int y1, int x2, int y2) {
		// do nothing
	}

	@Override
	public void removeValue(double value, int x, int y) {
		/*
		if(process().window().diameter() == 5 && process().window().pixel().x() == 0){
			System.out.println("remove "+process().window().pixel()+" "+x+" "+y+" --> "+value);
		}
		*/
		//System.out.println("remove "+value+" "+x+" "+y+" "+totalValues);
		totalValues--;
		if(value != Raster.getNoDataValue()){
			validValues--;
			if(value != 0){
				countValues--;
			}
		}
	}
	
	@Override
	public void removeCouple(double couple, int x1, int y1, int x2, int y2) {
		// do nothing
	}
	
	@Override
	public void down(int d, int place) {
		int outx, outy;
		/*
		if(process().window().pixel().x() == 0){
			System.out.println("down "+process().window().diameter());
		}
		*/
		//System.out.println(process().window().removeDownList().size()+" "+process().window().addDownList().size());
		
		// on enleve les valeurs qui ne sont plus actuelles
		//System.out.println("enleve les valeurs "+window().removeDownList());
		for(Pixel p : process().window().removeDownList()){
			outx = process().window().outXWindow(p.x());
			outy = process().window().outYWindow(p.y());
			if(outy >= 0 && outy < process().processType().matrix().height()
					&& outx >= 0 && outx < process().processType().matrix().width()){
				//System.out.println("remove "+p+" "+values[p.y()][p.x()]);
				/*
				if(process().window().diameter() == 5 && process().window().pixel().x() == 0 && process().window().pixel().y() < 4){
					System.out.println("down remove "+process().window().pixel()+" "+outx+" "+outy+" "+p.x()+" "+p.y()+" "+process().values()[p.y()][p.x()]);
				}*/
				if(p.y() < (process().window().diameter() - d + place)){
					//System.out.println(p.y()+" "+process().window().diameter()+" "+d+" "+(process().window().diameter() - d)+" "+(p.y() - (process().window().diameter() - d + place)));
					process().counting().removeValue(process().values()[p.y()][p.x()], p.x(), p.y());
				}
				//System.out.println(p.y()+" "+process().window().diameter()+" "+d+" "+(process().window().diameter() - d)+" "+(p.y() - (process().window().diameter() - d + place)));
				//process().counting().removeValue(process().values()[p.y()][p.x()], p.x(), p.y());
			}
		}
		
		//if(place >= (1+d)){
		
		// on ajoute les valeurs qui sont devenues actuelles
		//System.out.println("ajoute les valeurs "+window().addDownList());
		for(Pixel p : process().window().addDownList()){
			outx = process().window().outXWindow(p.x());
			outy = process().window().outYWindow(p.y());
			if(outy >= 0 && outy < process().processType().matrix().height()
					&& outx >= 0 && outx < process().processType().matrix().width()){
				//if(d+p.y() < process().window().height()){
				/*
				if(process().window().diameter() == 5 && process().window().pixel().x() == 0 && process().window().pixel().y() < 4){
					System.out.println("down add "+process().window().pixel()+" "+outx+" "+outy+" "+p.x()+" "+p.y()+" "+process().values()[p.y()][p.x()]);
				}*/
				//System.out.println(p.y()+" "+process().window().diameter()+" "+d+" "+(process().window().diameter() - d)+" "+(p.y() - (process().window().diameter() - d + place)));
				if(p.y() < process().window().diameter() - d + place){
				//if((p.y() < process().window().diameter() - d + place) && (d <= process().window().diameter()/2)){
					//System.out.println(p.y()+" "+process().window().diameter()+" "+d+" "+(process().window().diameter() - d)+" "+(p.y() - (process().window().diameter() - d + place)));	
					process().counting().addValue(process().values()[p.y()][p.x()], p.x(), p.y());
				}
			}
		}
		
	}
	
	@Override
	public double totalValues(){
		return totalValues;
	}
	
	@Override
	public double validValues(){
		return validValues;
	}
	
	@Override
	public int theoreticalSize(){
		return process().window().theoreticalSize();
	}
	
	@Override
	public double countValues(){
		return countValues;
	}

	@Override
	public void delete() {
		// do nothing
	}
	
}
