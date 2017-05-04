package fr.inra.sad_paysage.apiland.capfarm.simul.landscape;

import fr.inra.sad_paysage.apiland.capfarm.model.Farm;
import fr.inra.sad_paysage.apiland.capfarm.model.economic.EconomicCoverAllocationProblemFactory;
import fr.inra.sad_paysage.apiland.capfarm.model.economic.OptimizeEconomicCoverAllocationProblemFactory;
import fr.inra.sad_paysage.apiland.capfarm.model.territory.AgriculturalArea;
import fr.inra.sad_paysage.apiland.capfarm.simul.CSPCoverLocationModel;
import fr.inra.sad_paysage.apiland.capfarm.simul.CfmSimulator;
import fr.inra.sad_paysage.apiland.capfarm.simul.CoverAllocationProblemFactory;
import fr.inra.sad_paysage.apiland.capfarm.simul.GlobalCoverLocationModel;
import fr.inra.sad_paysage.apiland.capfarm.simul.MemoryCoverLocationModel;
import fr.inra.sad_paysage.apiland.core.element.DynamicLayer;
import fr.inra.sad_paysage.apiland.core.element.manager.DynamicLayerFactory;
import fr.inra.sad_paysage.apiland.simul.operation.OpRasterization;
import fr.inra.sad_paysage.apiland.simul.operation.OpRasterizationType;
import fr.inra.sad_paysage.apiland.simul.operation.OperationBuilder;

public class CfmLandscapeSimulator extends CfmSimulator {

	private static final long serialVersionUID = 1L;

	public CfmLandscapeSimulator(CfmLandscapeManager manager){
		super(manager);
	}

	@Override
	public CfmLandscapeManager manager(){
		return (CfmLandscapeManager) super.manager();
	}
	
	@Override
	protected void initFarms() {
		
		GlobalCoverLocationModel agriculture = new GlobalCoverLocationModel("agriculture", this);
		
		//affectation du territoire au model global
		agriculture.setTerritory((AgriculturalArea) map().get("territory").get("AA"));
		
		for(Farm f : manager().farms()){
			switch(manager().processMode()){
			case ACTIVATE : 
				CoverAllocationProblemFactory factory = null;
				switch(manager().mode()){
				case IDLE : factory = new CoverAllocationProblemFactory(); break;
				case ECONOMIC : factory = new EconomicCoverAllocationProblemFactory(); break;
				case OPTIMIZE : factory = new OptimizeEconomicCoverAllocationProblemFactory(); break;
				}
				agriculture.add(new CSPCoverLocationModel(this, f, factory)); 
				break;
			case MEMORY : 
				f.setMemory(0);
				agriculture.add(new MemoryCoverLocationModel(this, f)); 
				break;
			default : throw new IllegalArgumentException(manager().processMode()+" not implemented yet");
			}
		}
		
		model().add(agriculture);
	}
	
}
