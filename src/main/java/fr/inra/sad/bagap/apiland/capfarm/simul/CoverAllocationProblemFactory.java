package fr.inra.sad.bagap.apiland.capfarm.simul;

import fr.inra.sad.bagap.apiland.capfarm.csp.CoverAllocationProblem;
import fr.inra.sad.bagap.apiland.capfarm.csp.CoverAllocator;
import fr.inra.sad.bagap.apiland.core.time.Instant;

public class CoverAllocationProblemFactory {

	public CoverAllocationProblem create(CoverAllocator coverAllocator, Instant t){
		return new CoverAllocationProblem(coverAllocator, t);
	}
	
}
