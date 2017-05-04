package fr.inra.sad_paysage.apiland.capfarm.simul.output;

import fr.inra.sad_paysage.apiland.capfarm.model.territory.Parcel;
import fr.inra.sad_paysage.apiland.capfarm.simul.CoverLocationModel;
import fr.inra.sad_paysage.apiland.capfarm.simul.GlobalCoverLocationModel;
import fr.inra.sad_paysage.apiland.core.time.Instant;
import fr.inra.sad_paysage.apiland.simul.OutputAnalysis;
import fr.inra.sad_paysage.apiland.simul.Simulation;

public class ConsoleOutput extends OutputAnalysis {
	
	@Override
	public void close(Simulation simulation){
		if(!simulation.isCancelled()){
			for(CoverLocationModel model : (GlobalCoverLocationModel) simulation.model().get("agriculture")){
				System.out.println(model.getCoverAllocator().getCode());
				for(Parcel p : model.getCoverAllocator().parcels()){
					System.out.print(p.getId()+" : ");
					Instant t = simulation.manager().end();
					System.out.println(p.getAttribute("seq_cover").getValue(t));
				}
			}
		}
	}

}
