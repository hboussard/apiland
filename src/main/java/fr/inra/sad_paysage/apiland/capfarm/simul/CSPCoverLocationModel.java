package fr.inra.sad_paysage.apiland.capfarm.simul;

import fr.inra.sad_paysage.apiland.capfarm.csp.CoverAllocator;
import fr.inra.sad_paysage.apiland.core.time.Instant;

public class CSPCoverLocationModel extends CoverLocationModel {

	private static final long serialVersionUID = 1L;
	
	private CoverAllocationProblemFactory factory;
	
	private CSPCoverLocationStrategy strategy;

	public CSPCoverLocationModel(CfmSimulator simulator, CoverAllocator coverAllocator, CoverAllocationProblemFactory factory) {
		super(simulator, coverAllocator);
		this.factory = factory;
		strategy = new SimpleCSPCoverLocationStrategy();
	}
	
	@Override
	public boolean make(Instant t) {
		System.out.println("allocation at "+t.year());
		// delegation à une strategie d'allocation
		return strategy.make(factory, getCoverAllocator(), t);
	}
	
}
