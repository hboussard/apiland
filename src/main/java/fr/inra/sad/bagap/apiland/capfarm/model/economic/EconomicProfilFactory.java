package fr.inra.sad.bagap.apiland.capfarm.model.economic;

import fr.inra.sad.bagap.apiland.capfarm.model.CoverUnit;

public class EconomicProfilFactory {

	public static EconomicProfil create(CoverUnit[] covers){
		int[] yields = {5000, 5500, 5200, 5100, 5200, 5200, 5100, 5000, 5500, 5200};		
		double[] prices = {155.5, 165.5, 145, 145.5, 140.5, 175.5, 145.5, 145, 145.5, 140.5};
		double[] charges = {150, 160, 240, 200, 350, 370, 240, 240, 200, 350};
		int[] bonus = {50, 60, 10, 20, 150, 100, 24, 60, 10, 20};
		int[] works = {10, 5, 5, 20, 5, 15, 7, 5, 15, 7};
		return new EconomicProfil(covers, yields, prices, charges, bonus, works);
	}
	
}
