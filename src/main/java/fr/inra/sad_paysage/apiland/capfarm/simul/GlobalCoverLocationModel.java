package fr.inra.sad_paysage.apiland.capfarm.simul;

import java.util.Set;
import java.util.TreeSet;

import fr.inra.sad_paysage.apiland.capfarm.model.Farm;
import fr.inra.sad_paysage.apiland.capfarm.model.territory.AgriculturalArea;
import fr.inra.sad_paysage.apiland.core.time.Instant;
import fr.inra.sad_paysage.apiland.simul.Simulator;
import fr.inra.sad_paysage.apiland.simul.model.CompositeModel;

public class GlobalCoverLocationModel extends CompositeModel<CoverLocationModel> {

	private static final long serialVersionUID = 1L;

	private AgriculturalArea territory;
	
	public GlobalCoverLocationModel(String name, Simulator simulator) {
		super(name, simulator);
	}
	
	public void setTerritory(AgriculturalArea allTerritory) {
		this.territory = allTerritory;	
	}
	
	public AgriculturalArea getTerritory(){
		return territory;
	}
	
	@Override
	public boolean run(Instant t) {
		boolean ok = true;
		while(!current().isAfter(t) && !manager().isCancelled()){
			for(CoverLocationModel m : this){
				if(!m.run(current())){
					ok = false;
				}
			}	
			if(ok){
				setCurrent(delay.next(current()));
			}else{
				simulation().abort(current(), "");
				break;
			}
		}
		return ok;
	}

	public Set<Farm> farms(){
		Set<Farm> farms = new TreeSet<Farm>();
		for(CoverLocationModel m : this){
			farms.add((Farm) m.getCoverAllocator());
		}
		return farms;
	}

}
