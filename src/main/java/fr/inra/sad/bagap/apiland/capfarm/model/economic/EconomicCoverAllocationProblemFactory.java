package fr.inra.sad.bagap.apiland.capfarm.model.economic;

import fr.inra.sad.bagap.apiland.capfarm.csp.CoverAllocationProblem;
import fr.inra.sad.bagap.apiland.capfarm.csp.CoverAllocationProblemFactory;
import fr.inra.sad.bagap.apiland.capfarm.csp.CoverAllocator;
import fr.inra.sad.bagap.apiland.capfarm.model.CoverUnit;
import fr.inra.sad.bagap.apiland.capfarm.model.economic.csp.EconomicCoverAllocationProblem;
import fr.inra.sad.bagap.apiland.core.time.Instant;

public class EconomicCoverAllocationProblemFactory extends CoverAllocationProblemFactory {

	private String economicProfil, managmentProfil;
	
	public EconomicCoverAllocationProblemFactory(String economicProfil, String managmentProfil){
		this.economicProfil = economicProfil;
		this.managmentProfil = managmentProfil;
	}
	
	@Override
	public CoverAllocationProblem create(CoverAllocator coverAllocator, Instant t){
		
		CoverUnit[] covers = new CoverUnit[coverAllocator.coverUnits().size()];
		int index = 0;
		for(CoverUnit cu : coverAllocator.coverUnits()){
			covers[index++] = cu;
		}
		
		//EconomicProfil ep = EconomicProfilFactory.create(covers);
		EconomicProfil ep = null;
		if(economicProfil != null){
			ep = EconomicProfilFactory.create(covers, economicProfil);
		}
		ManagmentProfil mp = null;
		if(managmentProfil != null){
			mp = ManagmentProfilFactory.create(covers, managmentProfil);
		}
		
		return new EconomicCoverAllocationProblem(coverAllocator, t, ep, mp);
	}
	
}
