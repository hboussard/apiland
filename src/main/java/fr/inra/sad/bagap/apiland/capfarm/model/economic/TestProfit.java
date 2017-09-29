package fr.inra.sad.bagap.apiland.capfarm.model.economic;

import fr.inra.sad.bagap.apiland.capfarm.model.CoverFactory;
import fr.inra.sad.bagap.apiland.capfarm.model.CoverUnit;
import fr.inra.sad.bagap.apiland.capfarm.model.Farm;

public class TestProfit {

	private static final String path = "C:/Hugues/modelisation/capfarm/api/data/";
	
	public static void main(String[] args) {
		// cr�ation d'une ferme
		Farm farm = new Farm("151774");
				
		// int�gration des couverts
		CoverFactory.init(farm, path+"covers.txt", path+"groups.txt");
		
		CoverUnit[] covers = new CoverUnit[farm.coverUnits().size()];
		int index = 0;
		for(CoverUnit cu : farm.coverUnits()){
			covers[index++] = cu;
		}
		
		EconomicProfil ps = EconomicProfilFactory.create(covers);
		ps.profits();
				
	}

}