package fr.inra.sad.bagap.apiland.capfarm.csp;

import fr.inra.sad.bagap.apiland.core.time.Instant;

public class CoverAllocationProblemFactory {

	public CoverAllocationProblem create(CoverAllocator coverAllocator, Instant t){
		return new CoverAllocationProblem(coverAllocator, t);
	}
	
}
