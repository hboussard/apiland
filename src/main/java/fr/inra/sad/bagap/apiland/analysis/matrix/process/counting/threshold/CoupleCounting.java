package fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.threshold;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.Counting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.CountingDecorator;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.core.util.Couple;

/**
 * modeling class of an non-spatial qualitative process
 * @author H.Boussard
 */
public class CoupleCounting extends CountingDecorator {
	
	/** the total count of couples */
	private int totalCouples;
	
	/** the total count of valid couples (different to Raster.noDataValue() */
	private int validCouples;
	
	/** the count of known couples */
	private int countCouples;
	
	/** the total count of homogeneous couples */
	private int homogeneousCouples;
	
	/** the total count of unhomogeneous couples */
	private int heterogeneousCouples;
	
	/** the count of couples */
	private Map<Double, Count> countC;
	
	public CoupleCounting(Counting decorate) {
		super(decorate);
	}
	
	@Override
	public Set<Double> couples(){
		return countC.keySet();
	}
	
	@Override
	public double totalCouples(){
		return totalCouples;
	}
	
	@Override
	public double validCouples(){
		return validCouples;
	}
	
	@Override
	public double countCouples(){
		return countCouples;
	}
	
	@Override
	public double homogeneousCouples(){
		return homogeneousCouples;
	}
	
	@Override
	public double heterogeneousCouples(){
		return heterogeneousCouples;
	}
	
	@Override
	public double countCouple(double c){
		if(countC.containsKey(c)){
			return countC.get(c).get();
		}
		return 0;
	}
	
	@Override
	public void doInit() {
		totalCouples = 0;
		validCouples = 0;
		countCouples = 0;
		countC = new TreeMap<Double, Count>();
	}
	
	@Override
	protected void doAdd(double value, int x, int y, int filter, double ch,	double cv) {
		//System.out.println(value+" "+x+" "+y+" "+filter+" "+ch+" "+cv);
		// corner ?
		if(filter != 4){
			// horizontal west couple ?
			if(filter != 3){	
				totalCouples++;
				if(ch != Raster.getNoDataValue()){
					validCouples++;
					if(ch != 0.0){
						countCouples++;
						if(!countC.containsKey(ch)){
							countC.put(ch, new Count());
						}
						countC.get(ch).add();
						if(Couple.isHomogeneous(ch)){
							homogeneousCouples++;
						}else{
							heterogeneousCouples++;
						}
					}
				}
			}
					
			// vertical north couple ?
			if(filter != 2){	
				totalCouples++;
				if(cv != Raster.getNoDataValue()){
					validCouples++;
					if(cv != 0.0){
						countCouples++;
						if(!countC.containsKey(cv)){
							countC.put(cv, new Count());
						}
						countC.get(cv).add();
						if(Couple.isHomogeneous(cv)){
							homogeneousCouples++;
						}else{
							heterogeneousCouples++;
						}
					}
				}
			}
		}
	}
	
	@Override
	protected void doAddCouple(double couple, int x1, int y1, int x2, int y2) {
		//System.out.println("add "+couple);
		totalCouples++;
		if(couple != Raster.getNoDataValue()){
			validCouples++;
			if(couple != 0.0){
				countCouples++;
				if(!countC.containsKey(couple)){
					countC.put(couple, new Count());
				}
				countC.get(couple).add();
				if(Couple.isHomogeneous(couple)){
					homogeneousCouples++;
				}else{
					heterogeneousCouples++;
				}
			}
		}
	}
	
	@Override
	protected void doRemoveCouple(double couple, int x1, int y1, int x2, int y2){
		//System.out.println("remove "+couple);
		totalCouples--;
		if(couple != Raster.getNoDataValue()){
			validCouples--;
			if(couple != 0.0){
				countCouples--;
				countC.get(couple).minus();
				if(countC.get(couple).get() == 0){
					countC.remove(couple);
				}
				if(Couple.isHomogeneous(couple)){
					homogeneousCouples--;
				}else{
					heterogeneousCouples--;
				}
			}
		}
		
	}
	
	@Override
	public void doDelete(){
		countC.clear();
		countC = null;
	}

	// attention : la gestion du dodown des couples sera à revoir car non seulement elle va être dupliquéz au niveau de la classe DistanceCoupleCounting
	// mais en plus il y aura doublon de remove et de add des couples si présence de 2 métriques de couple (une threshold et une distance)
	// remonter le code de cette fonction dans BasicCounting peut être une solution à partir du moment ou ca ne rajoute pas de temps de calcul.
	@Override
	protected void doDown(int d, int place) {
		int outx, outy;
		// on enleve les couples de valeurs horizontaux qui ne sont plus actuels
		//System.out.println("enleve les couples horizontaux "+process().window().removeHorizontalDownList());
		for(Pixel p : process().window().removeHorizontalDownList()){
			outx = process().window().outXWindow(p.x());
			outy = process().window().outYWindow(p.y());
			if(outy >= 0 && outy < process().processType().matrix().height()
					&& outx >= 0 && outx < process().processType().matrix().width()){
				
				process().counting().removeCouple(Couple.get(process().values()[p.y()][p.x()-1], process().values()[p.y()][p.x()]), 0, 0, 0, 0);
			}
		}
				
		// on ajoute les couples de valeurs horizontaux qui sont devenus actuels
		//System.out.println("ajoute les couples horizontaux "+process().window().addHorizontalDownList());
		for(Pixel p : process().window().addHorizontalDownList()){
			outx = process().window().outXWindow(p.x());
			outy = process().window().outYWindow(p.y());
			if(outy >= 0 && outy < process().processType().matrix().height()
					&& outx >= 0 && outx < process().processType().matrix().width()){
				//System.out.println("add "+p);
				if(p.y() < process().window().diameter() - d + place){
				process().counting().addCouple(Couple.get(process().values()[p.y()][p.x()-1], process().values()[p.y()][p.x()]), 0, 0, 0, 0);
				}
			}
		}
				
		// on enleve les couples de valeurs verticaux qui ne sont plus actuels
		//System.out.println("enleve les couples verticaux "+process().window().removeVerticalDownList());
		for(Pixel p : process().window().removeVerticalDownList()){
			outx = process().window().outXWindow(p.x());
			outy = process().window().outYWindow(p.y());
			if(outy >= 0 && outy < process().processType().matrix().height()
					&& outx >= 0 && outx < process().processType().matrix().width()){
				
				process().counting().removeCouple(Couple.get(process().values()[p.y()-1][p.x()], process().values()[p.y()][p.x()]), 0, 0, 0, 0);
			}
		}
		
		// on ajoute les couples de valeurs verticaux sui sont devenus actuels
		//System.out.println("ajoute les couples verticaux "+process().window().addVerticalDownList());
		for(Pixel p : process().window().addVerticalDownList()){
			outx = process().window().outXWindow(p.x());
			outy = process().window().outYWindow(p.y());
			if(outy >= 0 && outy < process().processType().matrix().height()
					&& outx >= 0 && outx < process().processType().matrix().width()){
				//System.out.println("add couple " + p);
				if(p.y() < process().window().diameter() - d + place){
				process().counting().addCouple(Couple.get(process().values()[p.y()-1][p.x()], process().values()[p.y()][p.x()]), 0, 0, 0, 0);
				}
			}
		}
		
	}
	
}
