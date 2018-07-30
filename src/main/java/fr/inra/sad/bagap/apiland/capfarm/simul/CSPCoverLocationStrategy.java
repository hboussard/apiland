package fr.inra.sad.bagap.apiland.capfarm.simul;

import fr.inra.sad.bagap.apiland.capfarm.csp.CoverAllocationProblemFactory;
import fr.inra.sad.bagap.apiland.capfarm.csp.CoverAllocator;
import fr.inra.sad.bagap.apiland.core.time.Instant;

public abstract class CSPCoverLocationStrategy {

	public abstract boolean make(CoverAllocationProblemFactory factory, CoverAllocator allocator, Instant t);
	
}
