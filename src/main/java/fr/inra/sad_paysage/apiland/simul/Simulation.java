/*Copyright 2010 by INRA SAD-Paysage (Rennes)

Author: Hugues BOUSSARD 
email : hugues.boussard@rennes.inra.fr

This library is a JAVA toolbox made to create and manage dynamic landscapes.

This software is governed by the CeCILL-C license under French law and
abiding by the rules of distribution of free software.  You can  use,
modify and/ or redistribute the software under the terms of the CeCILL-C
license as circulated by CEA, CNRS and INRIA at the following URL
"http://www.cecill.info".

As a counterpart to the access to the source code and rights to copy,
modify and redistribute granted by the license, users are provided only
with a limited warranty  and the software's author,  the holder of the
economic rights,  and the successive licensors  have only  limited
liability.

In this respect, the user's attention is drawn to the risks associated
with loading,  using,  modifying and/or developing or reproducing the
software by the user in light of its specific status of free software,
that may mean  that it is complicated to manipulate, and that also
therefore means  that it is reserved for developers and experienced
professionals having in-depth computer knowledge. Users are therefore
encouraged to load and test the software's suitability as regards their
requirements in conditions enabling the security of their systems and/or
data to be ensured and,  more generally, to use and operate it in the
same conditions as regards security.

The fact that you are presently reading this means that you have had
knowledge of the CeCILL-C license and that you accept its terms.
*/
package fr.inra.sad_paysage.apiland.simul;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import fr.inra.sad_paysage.apiland.core.element.map.DynamicMap;
import fr.inra.sad_paysage.apiland.core.time.Instant;
import fr.inra.sad_paysage.apiland.simul.model.APILandModel;
import fr.inra.sad_paysage.apiland.simul.model.ModelManager;

public class Simulation implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Scenario scenario;
	
	protected boolean run;
	
	private boolean cancel;
	
	private int number;
	
	private APILandModel model;
	
	private String folder;

	/** output manager */
	//private OutputManager outputM;
	
	public Simulation(Scenario scenario, int number){
		this.scenario = scenario;
		this.number = number;
		this.cancel = false;
		//this.outputM = new OutputManager();
	}
	
	@Override
	public String toString(){
		return "simulation_"+scenario.number()+"_"+number;
	}
	
	public String folder(){
		return folder;
	}
	
	public SimulationManager manager(){
		return scenario.manager();
	}
	
	public Simulator simulator(){
		return scenario.simulator();
	}
	
	public OutputManager outputs(){
		return scenario.outputs();
	}
	
	public int number(){
		return number;
	}
	
	public Scenario scenario(){
		return scenario;
	}
	
	protected void init(){
		
		// model loading
		//loadModel();// tout le temps
		// seulement lorsqu'il y a plus d'1 simulation
		/*if(manager().simulations() > 1){
			loadModel();
		}else{*/
			model = scenario.model();
			model.setSimulation(this);
			model.setCurrent(manager().start());
		//}
		
		folder = manager().expFolder()+"/outputs/scenario_"+scenario.number()+"/simulation_"+number+"/";
		new File(folder).mkdir();
		run = false;

		// specific model initialization
		initModel();
		
		// initialisation des sorties
		outputs().init(this);
	}
	
	protected void initModel(){}
	
	public boolean run(){
		manager().display("run simulation "+simulator().number()+"_"+scenario().number()+"_"+number);
		
		model.initCurrent(manager().start());
		if(!run && !manager().isCancelled()){
			if(!model.run(manager().end())){
				return false;
			}
		}
		
		// calcul des sorties
		outputs().calculate(this);
		return true;
	}
	
	private void loadModel() {
		//manager().display("load simulation "+simulator().number()+"_"+scenario().number()+"_"+number+" model");
		try {
			model = (APILandModel) ModelManager.load(scenario().folder()+"/scenario.model");
			model.setSimulation(this);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private void saveModel(){
		//manager().display("save simulation "+simulator().number()+"_"+scenario().number()+"_"+number+" model");
		try {
			ModelManager.save(model, folder+"simulation.model");
			model = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected void close(){
		manager().display("close simulation "+simulator().number()+"_"+scenario().number()+"_"+number);
		//saveModel();
		run = true;
		
		// fermeture des sorties
		outputs().close(this);
		
		if(new File(folder).list().length == 0){
			new File(folder).delete();
		}
	}
	
	public APILandModel model(){
		return model;
	}
	
	public DynamicMap map(){
		return model.map();
	}

	public boolean isCancelled(){
		return cancel;
	}
	
	public void abort(Instant t, String message) {
		manager().setCancelled(true);
		cancel = true;
		System.err.println(this+" was interrupted at "+t+" : "+message);
	}
	
	public void delete(){
		scenario = null;
		model = null;
	}
	
	/*
	public void addOutput(OutputAnalysis out){
		simulator().addOutput(out);
	}
	*/
}
