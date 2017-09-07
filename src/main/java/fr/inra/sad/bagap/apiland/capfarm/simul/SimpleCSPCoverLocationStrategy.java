package fr.inra.sad.bagap.apiland.capfarm.simul;

import fr.inra.sad.bagap.apiland.capfarm.csp.CoverAllocator;
import fr.inra.sad.bagap.apiland.core.time.Instant;

public class SimpleCSPCoverLocationStrategy extends CSPCoverLocationStrategy {

	@Override
	public boolean make(CoverAllocationProblemFactory factory, CoverAllocator allocator, Instant t) {
		return factory.create(allocator, t).execute();
	}

}
