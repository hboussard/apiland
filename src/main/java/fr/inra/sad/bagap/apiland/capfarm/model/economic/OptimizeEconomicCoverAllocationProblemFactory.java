package fr.inra.sad.bagap.apiland.capfarm.model.economic;

import fr.inra.sad.bagap.apiland.capfarm.csp.CoverAllocationProblem;
import fr.inra.sad.bagap.apiland.capfarm.csp.CoverAllocationProblemFactory;
import fr.inra.sad.bagap.apiland.capfarm.csp.CoverAllocator;
import fr.inra.sad.bagap.apiland.capfarm.model.CoverUnit;
import fr.inra.sad.bagap.apiland.capfarm.model.economic.csp.OptimizeEconomicCoverAllocationProblem;
import fr.inra.sad.bagap.apiland.core.time.Instant;

public class OptimizeEconomicCoverAllocationProblemFactory extends CoverAllocationProblemFactory {

	private String economicProfil;
	
	public OptimizeEconomicCoverAllocationProblemFactory(String economicProfil){
		this.economicProfil = economicProfil;
	}
	
	@Override
	public CoverAllocationProblem create(CoverAllocator coverAllocator, Instant t){
		
		CoverUnit[] covers = new CoverUnit[coverAllocator.coverUnits().size()];
		int index = 0;
		for(CoverUnit cu : coverAllocator.coverUnits()){
			covers[index++] = cu;
		}
		//EcoomicProfil ep = EconomicProfilFactory.create(covers);
		
		//EconomicProfil ep = EconomicProfilFactory.create("C:/Hugues/modelisation/maelia/coupling/profil_economique.csv");
		EconomicProfil ep = EconomicProfilFactory.create(covers, economicProfil);
		
		return new OptimizeEconomicCoverAllocationProblem(coverAllocator, t, ep);
	}
	
}
