package fr.inra.sad.bagap.apiland.capfarm.simul;

import fr.inra.sad.bagap.apiland.simul.Scenario;
import fr.inra.sad.bagap.apiland.simul.Simulator;

public class CfmScenario extends Scenario {

	private static final long serialVersionUID = 1L;

	public CfmScenario(Simulator simulator, int number) {
		super(simulator, number);
	}

	@Override
	public CfmManager manager(){
		return (CfmManager) super.manager();
	}
	
	
}
