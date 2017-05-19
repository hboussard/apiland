package fr.inra.sad_paysage.apiland.capfarm.simul.output;

import fr.inra.sad_paysage.apiland.core.element.manager.DynamicLayerFactory;
import fr.inra.sad_paysage.apiland.core.element.type.DynamicElementTypeFactory;
import fr.inra.sad_paysage.apiland.core.time.Instant;
import fr.inra.sad_paysage.apiland.core.time.Interval;
import fr.inra.sad_paysage.apiland.simul.OutputAnalysis;
import fr.inra.sad_paysage.apiland.simul.Simulation;

public class LandscapeShapefileOutput extends OutputAnalysis {
	
	@Override
	public void close(Simulation simulation){
		if(! simulation.isCancelled()){
			Instant t = simulation.manager().start();
			
			simulation.model().map().get("territory").getType().addAttributeType(DynamicElementTypeFactory.createAttributeType("test", Interval.class, Double.class));
			
			while(t.isBefore(simulation.manager().end()) || t.equals(simulation.manager().end())){
				DynamicLayerFactory.exportShape(simulation.model().map().get("territory"), 
						t,
						simulation.scenario().folder()+"s"+simulation.number()+"_landscape_"+t.year());
				
				t = simulation.manager().delay().next(t);
			}
		}
	}
	
}
