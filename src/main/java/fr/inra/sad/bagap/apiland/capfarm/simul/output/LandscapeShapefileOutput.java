package fr.inra.sad.bagap.apiland.capfarm.simul.output;

import fr.inra.sad.bagap.apiland.core.element.manager.DynamicLayerFactory;
import fr.inra.sad.bagap.apiland.core.time.Instant;
import fr.inra.sad.bagap.apiland.simul.OutputAnalysis;
import fr.inra.sad.bagap.apiland.simul.Simulation;

public class LandscapeShapefileOutput extends OutputAnalysis {
	
	@Override
	public void close(Simulation simulation){
		if(! simulation.isCancelled()){
			Instant t = simulation.manager().start();
			
			while(t.isBefore(simulation.manager().end()) || t.equals(simulation.manager().end())){
				DynamicLayerFactory.exportShape(simulation.model().map().get("territory"), 
						t,
						simulation.scenario().folder()+"s"+simulation.manager().number()+"_landscape_"+t.year());
				
				t = simulation.manager().delay().next(t);
			}
		}
	}
	
}
