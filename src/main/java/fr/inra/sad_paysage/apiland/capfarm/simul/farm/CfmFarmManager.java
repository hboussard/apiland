package fr.inra.sad_paysage.apiland.capfarm.simul.farm;

import fr.inra.sad_paysage.apiland.capfarm.model.Farm;
import fr.inra.sad_paysage.apiland.capfarm.simul.CfmManager;

public class CfmFarmManager extends CfmManager {

	private static final long serialVersionUID = 1L;

	private Farm farm;
	
	public CfmFarmManager(Farm farm, int s){
		super(s);
		setFarm(farm);
	}
	
	private void setFarm(Farm farm) {
		this.farm = farm;
	}
	
	public Farm farm(){
		return farm;
	}



}
