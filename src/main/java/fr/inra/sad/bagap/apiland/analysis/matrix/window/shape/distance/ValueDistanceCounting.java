package fr.inra.sad.bagap.apiland.analysis.matrix.window.shape.distance;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.Counting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.CountingDecorator;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.Counting.Count;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;

public class ValueDistanceCounting extends CountingDecorator {

	// structure de données pour les valeurs
	// en fonction de la distance "composante X" du pixel au pixel central
	// pour chaque valeur, une liste de pixels
	Map<Integer, Set<Pixel>> datas;
	
	// structure de données pour la fonction de distance
	// ou structure de données pour les distances par pixel
	// ou délégation à la forme de fenêtre
	
	private double[][] weigted;
	
	public ValueDistanceCounting(Counting decorate) {
		super(decorate);
	}

	@Override
	protected void doInit() {
		datas = new TreeMap<Integer, Set<Pixel>>();
		//process().
		
		//process().window().size()
		//process().values()
	}

	@Override
	protected void doAdd(double value, int x, int y, int filter, double ch, double cv) {
		doAddValue(value, x, y);
	}
	
	@Override
	protected void doAddValue(double value, int x, int y) {
		if(value != Raster.getNoDataValue() && value != 0){
			Pixel p = new Pixel(x, y);
			if(!datas.containsKey((int) value)){
				datas.put((int) value, new HashSet<Pixel>());
			}
			datas.get((int) value).add(p);
		}
	}
	
	@Override
	protected void doRemoveValue(double value, int x, int y) {
		if(value != Raster.getNoDataValue() && (value != 0)){
			Pixel p = new Pixel(x, y);
			datas.get((int) value).remove(p);
			if(datas.get((int) value).size() == 0){
				datas.remove((int) value);
			}
		}
	}
	
	@Override
	public void doDelete() {
		datas.clear();
		datas = null;
	}
	
	@Override
	public Set<Integer> values(){
		return datas.keySet();
	}
	
	@Override
	public int countValue(int v){
		if(datas.containsKey(v)){
			return datas.get(v).size();
		}
		return 0;
	}


}
