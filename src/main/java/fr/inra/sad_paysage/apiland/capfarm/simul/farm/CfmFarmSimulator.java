package fr.inra.sad_paysage.apiland.capfarm.simul.farm;

import fr.inra.sad_paysage.apiland.capfarm.model.economic.EconomicCoverAllocationProblemFactory;
import fr.inra.sad_paysage.apiland.capfarm.model.economic.OptimizeEconomicCoverAllocationProblemFactory;
import fr.inra.sad_paysage.apiland.capfarm.simul.CSPCoverLocationModel;
import fr.inra.sad_paysage.apiland.capfarm.simul.CfmSimulator;
import fr.inra.sad_paysage.apiland.capfarm.simul.CoverAllocationProblemFactory;
import fr.inra.sad_paysage.apiland.capfarm.simul.GlobalCoverLocationModel;
import fr.inra.sad_paysage.apiland.capfarm.simul.MemoryCoverLocationModel;

public class CfmFarmSimulator extends CfmSimulator {

	private static final long serialVersionUID = 1L;

	public CfmFarmSimulator(CfmFarmManager manager){
		super(manager);
	}

	@Override
	public CfmFarmManager manager(){
		return (CfmFarmManager) super.manager();
	}
	
	@Override
	protected void initFarms() {
		GlobalCoverLocationModel agriculture = new GlobalCoverLocationModel("agriculture", this);
		
		//affectation du territoire au model global
		//agriculture.setTerritory((AgriculturalArea) map().get("territory").get("AA"));
		
		CoverAllocationProblemFactory factory = null;
		switch(manager().mode()){
		case IDLE : factory = new CoverAllocationProblemFactory(); break;
		case ECONOMIC : factory = new EconomicCoverAllocationProblemFactory(); break;
		case OPTIMIZE : factory = new OptimizeEconomicCoverAllocationProblemFactory(); break;
		}
		
		switch(manager().processMode()){
		case ACTIVATE : agriculture.add(new CSPCoverLocationModel(this, manager().farm(), factory)); break;
		case MEMORY : agriculture.add(new MemoryCoverLocationModel(this, manager().farm())); break;
		default : throw new IllegalArgumentException(manager().processMode()+" not implemented yet");
		}
		
		model().add(agriculture);
	}

}
