package fr.inra.sad_paysage.apiland.capfarm.simul.output;

import fr.inra.sad_paysage.apiland.capfarm.simul.CoverLocationModel;
import fr.inra.sad_paysage.apiland.capfarm.simul.GlobalCoverLocationModel;
import fr.inra.sad_paysage.apiland.core.element.manager.DynamicLayerFactory;
import fr.inra.sad_paysage.apiland.core.time.Instant;
import fr.inra.sad_paysage.apiland.simul.OutputAnalysis;
import fr.inra.sad_paysage.apiland.simul.Simulation;

public class FarmShapefileOutput extends OutputAnalysis {
	
	@Override
	public void close(Simulation simulation){
		if(! simulation.isCancelled()){
			Instant t = simulation.manager().start();
			while(t.isBefore(simulation.manager().end()) || t.equals(simulation.manager().end())){
				for(CoverLocationModel model : (GlobalCoverLocationModel) simulation.model().get("agriculture")){
					System.out.println(simulation.folder()+model.getName()+"_"+t.year());
					DynamicLayerFactory.exportShape(
							model.getCoverAllocator().getTerritory(), 
							t,
							simulation.folder()+model.getName()+"_"+t.year());
				}
				
				t = simulation.manager().delay().next(t);
			}
		}
	}
	
}
