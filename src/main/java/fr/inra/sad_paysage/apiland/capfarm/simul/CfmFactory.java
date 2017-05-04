package fr.inra.sad_paysage.apiland.capfarm.simul;

import fr.inra.sad_paysage.apiland.simul.Scenario;
import fr.inra.sad_paysage.apiland.simul.SimulationFactory;
import fr.inra.sad_paysage.apiland.simul.Simulator;

public class CfmFactory extends SimulationFactory {

	private static final long serialVersionUID = 1L;

	@Override
	public CfmScenario createScenario(Simulator simulator, int number) {
		return new CfmScenario(simulator, number);
	}

	@Override
	public CfmSimulation createSimulation(Scenario scenario, int number) {
		return new CfmSimulation(scenario, number);
	}

	
}
