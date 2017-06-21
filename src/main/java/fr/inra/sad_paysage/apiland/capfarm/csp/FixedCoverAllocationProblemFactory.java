package fr.inra.sad_paysage.apiland.capfarm.csp;

import fr.inra.sad_paysage.apiland.capfarm.simul.CoverAllocationProblemFactory;
import fr.inra.sad_paysage.apiland.core.time.Instant;

public class FixedCoverAllocationProblemFactory extends CoverAllocationProblemFactory {

	public FixedCoverAllocationProblem create(CoverAllocator coverAllocator, Instant t){
		return new FixedCoverAllocationProblem(coverAllocator, t, coverAllocator.getSolution());
	}
	
}
