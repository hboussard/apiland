package fr.inra.sad_paysage.apiland.capfarm.model.economic;

import fr.inra.sad_paysage.apiland.capfarm.csp.CoverAllocationProblem;
import fr.inra.sad_paysage.apiland.capfarm.csp.CoverAllocator;
import fr.inra.sad_paysage.apiland.capfarm.model.economic.csp.EconomicCoverAllocationProblem;
import fr.inra.sad_paysage.apiland.capfarm.simul.CoverAllocationProblemFactory;
import fr.inra.sad_paysage.apiland.core.time.Instant;
import fr.inra.sad_paysage.apiland.capfarm.model.CoverUnit;

public class EconomicCoverAllocationProblemFactory extends CoverAllocationProblemFactory {

	@Override
	public CoverAllocationProblem create(CoverAllocator coverAllocator, Instant t){
		
		CoverUnit[] covers = new CoverUnit[coverAllocator.coverUnits().size()];
		int index = 0;
		for(CoverUnit cu : coverAllocator.coverUnits()){
			covers[index++] = cu;
		}
		
		EconomicProfil ep = EconomicProfilFactory.create(covers);
		return new EconomicCoverAllocationProblem(coverAllocator, t);
	}
	
}
