package fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.distance;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.apache.commons.lang3.ArrayUtils;

import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.Counting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.CountingDecorator;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.core.util.Couple;

public class CoupleDistanceCounting extends CountingDecorator {
	
	private int size;
	
	/** basic information on horizontal couples 
	 * 1 if Raster.getNodataValue
	 * 2 if 0
	 * 3 if homogeneous
	 * 4 if heterogeneous
	 * */
	private int[][]basicCouplesH;
	
	/** basic information on vertical couples 
	 * 1 if Raster.getNodataValue
	 * 2 if 0
	 * 3 if homogeneous
	 * 4 if heterogeneous
	 * */
	private int[][]basicCouplesV;
	
	/** the horizontal couples */
	private double[][] datasH;
	
	/** the vertical couples */
	private double[][] datasV;
	
	private double total, valid, count, homo, hetero;
	
	private Map<Double, Double> couples;
	
	private boolean calculated;
	
	///** the total count of horizontal couples */
	//private int[][] totalCouplesH;
	
	///** the total count of vertical couples */
	//private int[][] totalCouplesV;
	
	///** the total count of valid horizontal couples (different to Raster.noDataValue() */
	//private int[][] validCouplesH;
	
	///** the total count of valid vertical couples (different to Raster.noDataValue() */
	//private int[][] validCouplesV;
	
	///** the count of known horizontal couples */
	//private int[][] countCouplesH;
	
	///** the count of known vertical couples */
	//private int[][] countCouplesV;
	
	///** the total count of homogeneous horizontal couples */
	//private int[][] homogeneousCouplesH;
	
	///** the total count of homogeneous vertical couples */
	//private int[][] homogeneousCouplesV;
	
	///** the total count of unhomogeneous horizontal couples */
	//private int[][] unhomogeneousCouplesH;
	
	///** the total count of unhomogeneous vertical couples */
	//private int[][] unhomogeneousCouplesV;
	
	///** the count of horizontal couples */
	//private Map<Double, int[][]> countH;
	
	///** the count of vertical couples */
	//private Map<Double, int[][]> countV;
	
	public CoupleDistanceCounting(Counting decorate, int size) {
		super(decorate);
		this.size = size;
	}
	
	@Override
	public void doInit() {
		
		basicCouplesH = new int[size][size-1];
		for(int j=0; j<size; j++){
			Arrays.fill(basicCouplesH[j], 0);
		}
		
		basicCouplesV = new int[size-1][size];
		for(int j=0; j<size-1; j++){
			Arrays.fill(basicCouplesV[j], 0);
		}
		
		datasH = new double[size][size-1];
		for(int j=0; j<size; j++){
			Arrays.fill(datasH[j], 0);
		}
		
		datasV = new double[size-1][size];
		for(int j=0; j<size-1; j++){
			Arrays.fill(datasV[j], 0);
		}
		
		couples = new TreeMap<Double, Double>();
		
		calculated = false;
		
		/*
		totalCouplesH = new int[size][size-1];
		for(int j=0; j<size; j++){
			Arrays.fill(totalCouplesH[j], 0);
		}
		
		totalCouplesV = new int[size-1][size];
		for(int j=0; j<size-1; j++){
			Arrays.fill(totalCouplesV[j], 0);
		}
		
		validCouplesH = new int[size][size-1];
		for(int j=0; j<size; j++){
			Arrays.fill(validCouplesH[j], 0);
		}
		
		validCouplesV = new int[size-1][size];
		for(int j=0; j<size-1; j++){
			Arrays.fill(validCouplesV[j], 0);
		}
		
		countCouplesH = new int[size][size-1];
		for(int j=0; j<size; j++){
			Arrays.fill(countCouplesH[j], 0);
		}
		
		countCouplesV = new int[size-1][size];
		for(int j=0; j<size-1; j++){
			Arrays.fill(countCouplesV[j], 0);
		}
		
		homogeneousCouplesH = new int[size][size-1];
		for(int j=0; j<size; j++){
			Arrays.fill(homogeneousCouplesH[j], 0);
		}
		
		homogeneousCouplesV = new int[size-1][size];
		for(int j=0; j<size-1; j++){
			Arrays.fill(homogeneousCouplesV[j], 0);
		}
		
		unhomogeneousCouplesH = new int[size][size-1];
		for(int j=0; j<size; j++){
			Arrays.fill(unhomogeneousCouplesH[j], 0);
		}
		
		unhomogeneousCouplesV = new int[size-1][size];
		for(int j=0; j<size-1; j++){
			Arrays.fill(unhomogeneousCouplesV[j], 0);
		}
		*/
		
	
		
		//countV = new TreeMap<Double, int[][]>();
		//countH = new TreeMap<Double, int[][]>();
	}
	
	@Override
	protected void doAdd(double value, int x, int y, int filter, double ch,	double cv) {
		//System.out.println(value+" "+x+" "+y+" "+filter+" "+ch+" "+cv);
		// corner ?
		if(filter != 4){
			// horizontal west couple ?
			if(filter != 3){	
				if(ch != Raster.getNoDataValue()){
					if(ch != 0){
						/*
						if(!countH.containsKey(ch)){
							countH.put(ch, new int[size][size-1]);
						}
						countH.get(ch)[y][x-1] = 1;
						*/
						datasH[y][x-1] = ch;
						
						if(Couple.isHomogeneous(ch)){
							basicCouplesH[y][x-1] = 3;
						}else{
							basicCouplesH[y][x-1] = 4;
						}
					}else{
						basicCouplesH[y][x-1] = 2;
					}
				}else{
					basicCouplesH[y][x-1] = 1;
				}
			}
							
			// vertical north couple ?
			if(filter != 2){	
				
				if(cv != Raster.getNoDataValue()){
					if(cv != 0){
						/*
						if(!countV.containsKey(cv)){
							countV.put(cv, new int[size-1][size]);
						}
						countV.get(cv)[y-1][x] = 1;
						*/
						datasV[y-1][x] = cv;
						
						if(Couple.isHomogeneous(cv)){
							basicCouplesV[y-1][x] = 3;
						}else{
							basicCouplesV[y-1][x] = 4;
						}
					}else{
						basicCouplesV[y-1][x] = 2;
					}
				}else{
					basicCouplesV[y-1][x] = 1;
				}
			}
		}
		
		/*
		// corner ?
		if(filter != 4){
			// horizontal west couple ?
			if(filter != 3){	
				//System.out.println(x+" "+y);
				totalCouplesH[y][x-1] = 1;
				if(ch != Raster.getNoDataValue()){
					validCouplesH[y][x-1] = 1;
					if(ch != 0){
						countCouplesH[y][x-1] = 1;
						if(!countH.containsKey(ch)){
							countH.put(ch, new int[size][size-1]);
						}
						countH.get(ch)[y][x-1] = 1;
						if(Couple.isHomogeneous(ch)){
							homogeneousCouplesH[y][x-1] = 1;
						}else{
							unhomogeneousCouplesH[y][x-1] = 1;
						}
					}
				}
			}
					
			// vertical north couple ?
			if(filter != 2){	
				//System.out.println(x+" "+y);
				totalCouplesV[y-1][x] = 1;
				if(cv != Raster.getNoDataValue()){
					validCouplesV[y-1][x] = 1;
					if(cv != 0){
						countCouplesV[y-1][x] = 1;
						if(!countV.containsKey(cv)){
							countV.put(cv, new int[size-1][size]);
						}
						countV.get(cv)[y-1][x] = 1;
						if(Couple.isHomogeneous(cv)){
							homogeneousCouplesV[y-1][x] = 1;
						}else{
							unhomogeneousCouplesV[y-1][x] = 1;
						}
					}
				}
			}
		}
		*/
	}
	
	@Override
	protected void doAddCouple(double couple, int x1, int y1, int x2, int y2) {
		//System.out.println("add "+couple);
		if(y1 == y2){ // couple horizontal
			int x = Math.min(x1, x2);
			if(couple != Raster.getNoDataValue()){
				if(couple != 0){
					/*
					if(!countH.containsKey(couple)){
						countH.put(couple, new int[size][size-1]);
					}
					countH.get(couple)[y1][x] = 1;
					*/
					datasH[y1][x] = couple;
				
					if(Couple.isHomogeneous(couple)){
						basicCouplesH[y1][x] = 3;
					}else{
						basicCouplesH[y1][x] = 4;
					}
				}else{
					basicCouplesH[y1][x] = 2;
				}
			}else{
				basicCouplesH[y1][x] = 1;
			}
		}else{ // couple vertical
			int y = Math.min(y1, y2);
			if(couple != Raster.getNoDataValue()){
				if(couple != 0){
					/*
					if(!countV.containsKey(couple)){
						countV.put(couple, new int[size-1][size]);
					}
					countV.get(couple)[y][x1] = 1;
					*/
					datasV[y][x1] = couple;
					
					if(Couple.isHomogeneous(couple)){
						basicCouplesV[y][x1] = 3;
					}else{
						basicCouplesV[y][x1] = 4;
					}
				}else{
					basicCouplesV[y][x1] = 2;
				}
			}else{
				basicCouplesV[y][x1] = 1;
			}
		}
		
		
		/*
		if(y1 == y2){ // couple horizontal
			int x = Math.min(x1,  x2);
			totalCouplesH[y1][x] = 1;
			if(couple != Raster.getNoDataValue()){
				validCouplesH[y1][x] = 1;
				if(couple != 0){
					countCouplesH[y1][x] = 1;
					if(!countH.containsKey(couple)){
						countH.put(couple, new int[size][size-1]);
					}
					countH.get(couple)[y1][x] = 1;
					if(Couple.isHomogeneous(couple)){
						homogeneousCouplesH[y1][x] = 1;
					}else{
						unhomogeneousCouplesH[y1][x] = 1;
					}
				}
			}
		}else{ // couple vertical
			int y = Math.min(y1,  y2);
			totalCouplesV[y][x1] = 1;
			if(couple != Raster.getNoDataValue()){
				validCouplesV[y][x1] = 1;
				if(couple != 0){
					countCouplesV[y][x1] = 1;
					if(!countV.containsKey(couple)){
						countV.put(couple, new int[size-1][size]);
					}
					countV.get(couple)[y][x1] = 1;
					if(Couple.isHomogeneous(couple)){
						homogeneousCouplesV[y][x1] = 1;
					}else{
						unhomogeneousCouplesV[y][x1] = 1;
					}
				}
			}
		}
		*/
	}
	
	@Override
	protected void doRemoveCouple(double couple, int x1, int y1, int x2, int y2){
		//System.out.println("remove "+couple);
		if(y1 == y2){ // couple horizontal
			int x = Math.min(x1,  x2);
			basicCouplesH[y1][x] = 0;
			if(couple != Raster.getNoDataValue() && couple != 0){
				//countH.get(couple)[y1][x] = 0;
				datasH[y1][x] = 0;
			}
		}else{ // couple vertical
			int y = Math.min(y1,  y2);
			basicCouplesV[y][x1] = 0;
			if(couple != Raster.getNoDataValue() && couple != 0){
				//countV.get(couple)[y][x1] = 0;
				datasV[y][x1] = 0;
			}
		}
		
		
		/*
		if(y1 == y2){ // couple horizontal
			int x = Math.min(x1,  x2);
			totalCouplesH[y1][x] = 0;
			if(couple != Raster.getNoDataValue()){
				validCouplesH[y1][x] = 0;
				if(couple != 0){
					countCouplesH[y1][x] = 0;
					countH.get(couple)[y1][x] = 0;
					if(Couple.isHomogeneous(couple)){
						homogeneousCouplesH[y1][x] = 0;
					}else{
						unhomogeneousCouplesH[y1][x] = 0;
					}
				}
			}
		}else{ // couple vertical
			int y = Math.min(y1,  y2);
			totalCouplesV[y][x1] = 0;
			if(couple != Raster.getNoDataValue()){
				validCouplesV[y][x1] = 0;
				if(couple != 0){
					countCouplesV[y][x1] = 0;
					countV.get(couple)[y][x1] = 0;
					if(Couple.isHomogeneous(couple)){
						homogeneousCouplesV[y][x1] = 0;
					}else{
						unhomogeneousCouplesV[y][x1] = 0;
					}
				}
			}
		}
		*/
	}
	
	@Override
	public void doDelete(){
		
		basicCouplesH = null;
		
		basicCouplesV = null;
		
		datasH = null;
		
		datasV = null;
		
		couples.clear();
		couples = null;
		
		/*
		totalCouplesH = null;
		
		totalCouplesV = null;
		
		validCouplesH = null;
		
		validCouplesV = null;
		
		countCouplesH = null;
		
		countCouplesV = null;
		
		homogeneousCouplesH = null;
		
		homogeneousCouplesV = null;
		
		unhomogeneousCouplesH = null;
		
		unhomogeneousCouplesV = null;
		*/
		/*
		countH.clear();
		countH = null;
		
		countV.clear();
		countV = null;
		*/
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
				//System.out.println("remove H "+p);
				process().counting().removeCouple(Couple.get(process().values()[p.y()][p.x()-1], process().values()[p.y()][p.x()]), p.x()-1, p.y(), p.x(), p.y());
			}
		}
				
		// on ajoute les couples de valeurs horizontaux qui sont devenus actuels
		//System.out.println("ajoute les couples horizontaux "+process().window().addHorizontalDownList());
		for(Pixel p : process().window().addHorizontalDownList()){
			outx = process().window().outXWindow(p.x());
			outy = process().window().outYWindow(p.y());
			if(outy >= 0 && outy < process().processType().matrix().height()
					&& outx >= 0 && outx < process().processType().matrix().width()){
				//System.out.println("add H "+p);
				if(p.y() < process().window().diameter() - d + place){
				process().counting().addCouple(Couple.get(process().values()[p.y()][p.x()-1], process().values()[p.y()][p.x()]), p.x()-1, p.y(), p.x(), p.y());
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
				//System.out.println("remove V "+p);
				process().counting().removeCouple(Couple.get(process().values()[p.y()-1][p.x()], process().values()[p.y()][p.x()]), p.x(), p.y()-1, p.x(), p.y());
			}
		}
		
		// on ajoute les couples de valeurs verticaux sui sont devenus actuels
		//System.out.println("ajoute les couples verticaux "+process().window().addVerticalDownList());
		for(Pixel p : process().window().addVerticalDownList()){
			outx = process().window().outXWindow(p.x());
			outy = process().window().outYWindow(p.y());
			if(outy >= 0 && outy < process().processType().matrix().height()
					&& outx >= 0 && outx < process().processType().matrix().width()){
				//System.out.println("add V "+p);
				if(p.y() < process().window().diameter() - d + place){
				process().counting().addCouple(Couple.get(process().values()[p.y()-1][p.x()], process().values()[p.y()][p.x()]), p.x(), p.y()-1, p.x(), p.y());
				}
			}
		}
		
		int[] th = new int[size-1];
		Arrays.fill(th, 0);
		basicCouplesH = Arrays.copyOfRange(basicCouplesH, 1, basicCouplesH.length);
		basicCouplesH = ArrayUtils.addAll(basicCouplesH, th);
		
		int[] tv = new int[size];
		Arrays.fill(tv, 0);
		basicCouplesV = Arrays.copyOfRange(basicCouplesV, 1, basicCouplesV.length);
		basicCouplesV = ArrayUtils.addAll(basicCouplesV, tv);
		
		double[] tdh = new double[size-1];
		Arrays.fill(tdh, 0);
		datasH = Arrays.copyOfRange(datasH, 1, datasH.length);
		datasH = ArrayUtils.addAll(datasH, tdh);
		
		double[] tdv = new double[size];
		Arrays.fill(tdv, 0);
		datasV = Arrays.copyOfRange(datasV, 1, datasV.length);
		datasV = ArrayUtils.addAll(datasV, tdv);
		
		
		calculated = false;
		
		/*
		int[] thtotal = new int[size-1];
		Arrays.fill(thtotal, 0);
		totalCouplesH = Arrays.copyOfRange(totalCouplesH, 1, totalCouplesH.length);
		totalCouplesH = ArrayUtils.addAll(totalCouplesH, thtotal);
		
		int[] tvtotal = new int[size];
		Arrays.fill(tvtotal, 0);
		totalCouplesV = Arrays.copyOfRange(totalCouplesV, 1, totalCouplesV.length);
		totalCouplesV = ArrayUtils.addAll(totalCouplesV, tvtotal);
		
		int[] thvalid = new int[size-1];
		Arrays.fill(thvalid, 0);
		validCouplesH = Arrays.copyOfRange(validCouplesH, 1, validCouplesH.length);
		validCouplesH = ArrayUtils.addAll(validCouplesH, thvalid);
		
		int[] tvvalid = new int[size];
		Arrays.fill(tvvalid, 0);
		validCouplesV = Arrays.copyOfRange(validCouplesV, 1, validCouplesV.length);
		validCouplesV = ArrayUtils.addAll(validCouplesV, tvvalid);
	
		int[] thcount = new int[size-1];
		Arrays.fill(thcount, 0);
		countCouplesH = Arrays.copyOfRange(countCouplesH, 1, countCouplesH.length);
		countCouplesH = ArrayUtils.addAll(countCouplesH, thcount);
		
		int[] tvcount = new int[size];
		Arrays.fill(tvcount, 0);
		countCouplesV = Arrays.copyOfRange(countCouplesV, 1, countCouplesV.length);
		countCouplesV = ArrayUtils.addAll(countCouplesV, tvcount);
		
		int[] thhomo = new int[size-1];
		Arrays.fill(thhomo, 0);
		homogeneousCouplesH = Arrays.copyOfRange(homogeneousCouplesH, 1, homogeneousCouplesH.length);
		homogeneousCouplesH = ArrayUtils.addAll(homogeneousCouplesH, thhomo);
		
		int[] tvhomo = new int[size];
		Arrays.fill(tvhomo, 0);
		homogeneousCouplesV = Arrays.copyOfRange(homogeneousCouplesV, 1, homogeneousCouplesV.length);
		homogeneousCouplesV = ArrayUtils.addAll(homogeneousCouplesV, tvhomo);
		
		int[] thunhomo = new int[size-1];
		Arrays.fill(thunhomo, 0);
		unhomogeneousCouplesH = Arrays.copyOfRange(unhomogeneousCouplesH, 1, unhomogeneousCouplesH.length);
		unhomogeneousCouplesH = ArrayUtils.addAll(unhomogeneousCouplesH, thunhomo);
		
		int[] tvunhomo = new int[size];
		Arrays.fill(tvunhomo, 0);
		unhomogeneousCouplesV = Arrays.copyOfRange(unhomogeneousCouplesV, 1, unhomogeneousCouplesV.length);
		unhomogeneousCouplesV = ArrayUtils.addAll(unhomogeneousCouplesV, tvunhomo);
		*/
		
		/*
		for(Double c : countH.keySet()){
			int[] tnewFill = new int[size];
			Arrays.fill(tnewFill, 0);
			countH.put(c, Arrays.copyOfRange(countH.get(c), 1, countH.get(c).length));
			countH.put(c, ArrayUtils.addAll(countH.get(c), tnewFill));
		}
		
		for(Double c : countV.keySet()){
			int[] tnewFill = new int[size];
			Arrays.fill(tnewFill, 0);
			countV.put(c, Arrays.copyOfRange(countV.get(c), 1, countV.get(c).length));
			countV.put(c, ArrayUtils.addAll(countV.get(c), tnewFill));
		}
		*/
	}
	
	private void calculate(){
		total = 0;
		valid = 0;
		count = 0;
		homo = 0;
		hetero = 0;
		couples.clear();
		
		double v, c;
		double weight;
		
		for(int j=0; j<size; j++){
			for(int i=0; i<size-1; i++){
				
				c = datasH[j][i];
				if(c > 0 && !couples.containsKey(c)){
					couples.put(c, 0.0);
				}
				
				weight = process().window().weightedH()[j][i];
				
				if(c > 0){
					couples.put(c, couples.get(c) + weight);
				}
				
				v = basicCouplesH[j][i];
				if(v > 0){
					total += weight;
					if(v > 1){
						valid += weight;
						if(v > 2){
							count += weight;
							if(v > 3){
								hetero += weight;
							}else{
								homo += weight;
							}
						}
					}
				}
			}
		}
		for(int j=0; j<size-1; j++){
			for(int i=0; i<size; i++){
				
				c = datasV[j][i];
				if(c > 0 && !couples.containsKey(c)){
					couples.put(c, 0.0);
				}
				
				weight = process().window().weightedV()[j][i];
				if(c > 0){
					couples.put(c, couples.get(c) + weight);
				}
				
				v = basicCouplesV[j][i];
				if(v > 0){
					total += weight;
					if(v > 1){
						valid += weight;
						if(v > 2){
							count += weight;
							if(v > 3){
								hetero += weight;
							}else{
								homo += weight;
							}
						}
					}
				}
			}
		}
		
		calculated = true;
	}
	
	@Override
	public Set<Double> couples(){
		if(!calculated){
			calculate();
		}
		return couples.keySet();
		
		/*
		Set<Double> couples = new TreeSet<Double>();
		for(Double d : countV.keySet()){
			couples.add(d);
		}
		for(Double d : countH.keySet()){
			couples.add(d);
		}
		return couples;
		*/
	}
	
	@Override
	public double totalCouples(){
		if(!calculated){
			calculate();
		}
		return total;
		
		/*
		double count = 0.0;
		for(int j=0; j<size; j++){
			for(int i=0; i<size-1; i++){
				if(totalCouplesH[j][i] == 1){
					count += process().window().weigtedH()[j][i];
				}
			}
		}
		for(int j=0; j<size-1; j++){
			for(int i=0; i<size; i++){
				if(totalCouplesV[j][i] == 1){
					count += process().window().weigtedV()[j][i];
				}
			}
		}
		return count;
		*/
	}
	
	@Override
	public double validCouples(){
		if(!calculated){
			calculate();
		}
		return valid;
		/*
		double count = 0.0;
		for(int j=0; j<size; j++){
			for(int i=0; i<size-1; i++){
				if(validCouplesH[j][i] == 1){
					count += process().window().weigtedH()[j][i];
				}
			}
		}
		for(int j=0; j<size-1; j++){
			for(int i=0; i<size; i++){
				if(validCouplesV[j][i] == 1){
					count += process().window().weigtedV()[j][i];
				}
			}
		}
		return count;
		*/
	}
	
	@Override
	public double countCouples(){
		if(!calculated){
			calculate();
		}
		return count;
		/*
		double count = 0.0;
		for(int j=0; j<size; j++){
			for(int i=0; i<size-1; i++){
				if(countCouplesH[j][i] == 1){
					count += process().window().weigtedH()[j][i];
				}
			}
		}
		for(int j=0; j<size-1; j++){
			for(int i=0; i<size; i++){
				if(countCouplesV[j][i] == 1){
					count += process().window().weigtedV()[j][i];
				}
			}
		}
		return count;
		*/
	}
	
	@Override
	public double homogeneousCouples(){
		if(!calculated){
			calculate();
		}
		return homo;
		/*
		double count = 0.0;
		for(int j=0; j<size; j++){
			for(int i=0; i<size-1; i++){
				if(homogeneousCouplesH[j][i] == 1){
					count += process().window().weigtedH()[j][i];
				}
			}
		}
		for(int j=0; j<size-1; j++){
			for(int i=0; i<size; i++){
				if(homogeneousCouplesV[j][i] == 1){
					count += process().window().weigtedV()[j][i];
				}
			}
		}
		return count;
		*/
	}
	
	@Override
	public double heterogeneousCouples(){
		if(!calculated){
			calculate();
		}
		return hetero;
		
		/*
		double count = 0.0;
		for(int j=0; j<size; j++){
			for(int i=0; i<size-1; i++){
				if(unhomogeneousCouplesH[j][i] == 1){
					count += process().window().weigtedH()[j][i];
				}
			}
		}
		for(int j=0; j<size-1; j++){
			for(int i=0; i<size; i++){
				if(unhomogeneousCouplesV[j][i] == 1){
					count += process().window().weigtedV()[j][i];
				}
			}
		}
		return count;
		*/
	}
	
	@Override
	public double countCouple(double c){
		if(!calculated){
			calculate();
		}
		if(couples.containsKey(c)){
			return couples.get(c);
		}
		return 0;
		
		/*
		double count = 0.0;
		if(countH.containsKey(c)){
			for(int j=0; j<size; j++){
				for(int i=0; i<size-1; i++){
					if(countH.get(c)[j][i] == 1){
						count += process().window().weigtedH()[j][i];
					}
				}
			}
		}
		if(countV.containsKey(c)){
			for(int j=0; j<size-1; j++){
				for(int i=0; i<size; i++){
					if(countV.get(c)[j][i] == 1){
						count += process().window().weigtedV()[j][i];
					}
				}
			}
		}
		return count;
		*/
	}
	
	
	
}
