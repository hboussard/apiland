package fr.inra.sad.bagap.apiland.capfarm.simul.landscape;

import java.util.Set;

import fr.inra.sad.bagap.apiland.capfarm.model.Farm;
import fr.inra.sad.bagap.apiland.capfarm.simul.CfmManager;

public class CfmLandscapeManager extends CfmManager {

	private static final long serialVersionUID = 1L;

	public CfmLandscapeManager(int s){
		super(s);
	}
	
	private Set<Farm> farms;
	
	public void setFarms(Set<Farm> farms) {
		this.farms = farms;
	}
	
	public Set<Farm> farms(){
		return farms;
	}

}