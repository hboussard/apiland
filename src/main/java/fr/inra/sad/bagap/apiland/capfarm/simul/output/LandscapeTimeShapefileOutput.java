package fr.inra.sad.bagap.apiland.capfarm.simul.output;

import fr.inra.sad.bagap.apiland.core.element.manager.DynamicLayerFactory;
import fr.inra.sad.bagap.apiland.simul.OutputAnalysis;
import fr.inra.sad.bagap.apiland.simul.Simulation;

public class LandscapeTimeShapefileOutput extends OutputAnalysis {
	
	@Override
	public void close(Simulation simulation){
		if(! simulation.isCancelled()){
			DynamicLayerFactory.exportTimeShape(simulation.model().map().get("territory"), 
					simulation.manager().start(),
					simulation.manager().end(),
					simulation.manager().delay(),
					simulation.scenario().folder()+"s"+simulation.manager().number()+"_landscape");
			
		}
	}
	
}