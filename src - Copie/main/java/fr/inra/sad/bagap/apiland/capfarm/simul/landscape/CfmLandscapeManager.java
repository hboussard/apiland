package fr.inra.sad.bagap.apiland.capfarm.simul.landscape;

import java.util.Set;

import fr.inra.sad.bagap.apiland.capfarm.csp.CoverAllocator;
import fr.inra.sad.bagap.apiland.capfarm.model.Farm;
import fr.inra.sad.bagap.apiland.capfarm.simul.CfmManager;

public class CfmLandscapeManager extends CfmManager {

	private static final long serialVersionUID = 1L;

	private CoverAllocator coverAllocator;
	
	public CfmLandscapeManager(int s){
		super(s);
	}
	
	public CfmLandscapeManager(CoverAllocator coverAllocator, int s){
		super(s);
		this.coverAllocator = coverAllocator;
	}
	
	public CoverAllocator getCoverAllocator(){
		return coverAllocator;
	}
	
	private Set<Farm> farms;
	
	public void setFarms(Set<Farm> farms) {
		this.farms = farms;
	}
	
	public Set<Farm> farms(){
		return farms;
	}

}