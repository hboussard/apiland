package fr.inra.sad.bagap.apiland.capfarm.model.economic;

import java.util.Set;

import fr.inra.sad.bagap.apiland.capfarm.model.CoverUnit;

public class EconomicProfil {
	
	private CoverUnit[] covers;
	
	private int[] yields; // kg par hectare, ex : 2000
	
	private double[] prices; // euro par tonne, ex : 165
	
	private double[] charges; // euro par hectare
	
	private int[] bonus; // euro par hectare
	
	private int[] works; // nb de jours travail
	
	public EconomicProfil(CoverUnit[] covers, int[] yields, double[] prices, double[] charges, int[] bonus, int[] works){
		this.covers = covers;
		this.yields = yields;
		this.prices = prices;
		this.charges = charges;
		this.bonus = bonus;
		this.works = works;
	}
	
	public int profit(CoverUnit cu, int area){
		int p = profit(getCoverIndex(cu));
		//System.out.println(p +" "+ area+" "+(p*area));
		return p * area;
	}
		
	private int getCoverIndex(CoverUnit cu){
		for(int i=0; i<covers.length; i++){
			if(covers[i].equals(cu)){
				return i;
			}
		}
		return -1;
	}
	
	public int[] profits(){
		int[] profits = new int[covers.length];
		for(int i=0; i<covers.length; i++){
			profits[i] = profit(i);
		}
		return profits;
	}
	
	public int[] profits(Set<CoverUnit> cs){
		int[] profits = new int[covers.length];
		for(int i=0; i<covers.length; i++){
			if(cs.contains(covers[i])){
				profits[i] = profit(i);
			}else{
				profits[i] = 0;
			}
		}
		return profits;
	}
	
	// en millieme d'euros par mcarre
	private int profit(int index){
		//System.out.println(yields[index]+" "+prices[index]+" "+new Double(((prices[index] / 1000.0) * (yields[index] / 10000.0) * 1000.0)).intValue());
		//return new Double(((prices[index] / 1000.0) * (yields[index] / 10000.0) + (bonus[index] / 10000.0) - (charges[index] / 10000.0)) * 1000.0).intValue();
		//return new Double(((prices[index] / 1000.0) * (yields[index] / 10000.0 + (bonus[index] / 10000.0) - (charges[index] / 10000.0)) * 1000.0)).intValue();
		//return new Double(((prices[index] / 1000.0) * (yields[index] / 10000.0) * 1000.0)).intValue();
		//return 10;
		//return yields[index] / 10;
		return yields[index];
	}
	
	public int[] works(){
		return works;
	}
	
	public void display(int[] areas){
		int profit_total = 0;
		for(int i=0; i<covers.length; i++){
			//System.out.println(covers[i].getName()+", rendement = "+yields[i]+"kg/ha et prix = "+prices[i]+"€/t ==> profit = "+profit(i)+" milliemes d'euro/mcarre");
			//System.out.println("surface = "+areas[i]);
			profit_total += areas[i]*profit(i);
		}
		System.out.println("profit total = "+profit_total);
	}
	
}
