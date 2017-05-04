package fr.inra.sad_paysage.apiland.capfarm.simul;

import fr.inra.sad_paysage.apiland.capfarm.csp.CoverAllocator;
import fr.inra.sad_paysage.apiland.core.time.Instant;

public abstract class CSPCoverLocationStrategy {

	public abstract boolean make(CoverAllocationProblemFactory factory, CoverAllocator allocator, Instant t);
	
}
