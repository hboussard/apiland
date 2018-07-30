package fr.inra.sad.bagap.apiland.capfarm.simul;

import java.util.Set;
import java.util.TreeSet;
import fr.inra.sad.bagap.apiland.capfarm.model.Farm;
import fr.inra.sad.bagap.apiland.capfarm.model.territory.AgriculturalArea;
import fr.inra.sad.bagap.apiland.simul.Simulator;
import fr.inra.sad.bagap.apiland.simul.model.CompositeModel;

public class GlobalCoverLocationModel extends CompositeModel<CoverLocationModel> {

	private static final long serialVersionUID = 1L;

	private AgriculturalArea territory;
	
	public GlobalCoverLocationModel(String name, Simulator simulator) {
		super(name, simulator);
	}
	
	public void setTerritory(AgriculturalArea territory) {
		this.territory = territory;	
	}
	
	public AgriculturalArea getTerritory(){
		return territory;
	}
	
	public Set<Farm> farms(){
		Set<Farm> farms = new TreeSet<Farm>();
		for(CoverLocationModel m : this){
			farms.add((Farm) m.getCoverAllocator());
		}
		return farms;
	}

}
