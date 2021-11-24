package fr.inra.sad.bagap.apiland.capfarm.csp;

import fr.inra.sad.bagap.apiland.core.time.Instant;

public class FixedCoverAllocationProblemFactory extends CoverAllocationProblemFactory {

	public FixedCoverAllocationProblem create(CoverAllocator coverAllocator, Instant t){
		return new FixedCoverAllocationProblem(coverAllocator, t, coverAllocator.getSolution());
	}
	
}
