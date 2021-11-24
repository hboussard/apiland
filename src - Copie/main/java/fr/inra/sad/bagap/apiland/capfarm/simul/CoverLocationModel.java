package fr.inra.sad.bagap.apiland.capfarm.simul;

import fr.inra.sad.bagap.apiland.capfarm.csp.CoverAllocator;
import fr.inra.sad.bagap.apiland.simul.model.AtomicModel;

public abstract class CoverLocationModel extends AtomicModel {

	private static final long serialVersionUID = 1L;

	private CoverAllocator coverAllocator;
	
	public CoverLocationModel(CfmSimulator simulator, CoverAllocator coverAllocator) {
		super(coverAllocator.getCode(), simulator, coverAllocator.getTerritory());
		this.coverAllocator = coverAllocator;
	}
	
	@Override
	public String toString(){
		return "model "+coverAllocator.getCode();
	}

	public CoverAllocator getCoverAllocator(){
		return coverAllocator;
	}

}
