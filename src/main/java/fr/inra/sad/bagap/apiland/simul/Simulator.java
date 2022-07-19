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
package fr.inra.sad.bagap.apiland.simul;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import fr.inra.sad.bagap.apiland.core.element.map.DefaultDynamicMap;
import fr.inra.sad.bagap.apiland.core.element.map.DynamicMap;
import fr.inra.sad.bagap.apiland.simul.model.APILandModel;
import fr.inra.sad.bagap.apiland.simul.model.ModelManager;

/**
 * modeling class of a simulator
 * @author H.BOUSSARD
 */
public class Simulator implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/** settings manager */
	private SimulationManager manager;
	
	/** the whole scenarios */
	private Scenario[] scenarios;
	 
	/** simulation factory */
	private SimulationFactory factory;
	 
	/** simulator number */
	private int number;
	
	/** the model */
	private APILandModel model;
	
	/** the state */
	private SimulatorState state;
	
	/** is the simulator launch on batch mode ? */
	private boolean batchMode = false;
	
	/** to display simulation evolution */
	private transient int up = 0;
	
	/** output manager */
	private OutputManager outputs;
	
	/** constructor */
	public Simulator(){
		this(new SimulationManager(1), new SimulationFactory());
	}
	
	public Simulator(int s){
		this(new SimulationManager(s), new SimulationFactory());
	}
	
	public Simulator(SimulationManager manager){
		this(manager, new SimulationFactory());
	}
	
	public Simulator(SimulationFactory factory, int s){
		this(new SimulationManager(s), factory);
	}
	
	/**
	 * constructor
	 * @param manager a specific manager
	 * @param factory a specific factory
	 */
	public Simulator(SimulationManager manager, SimulationFactory factory){
		this.manager = manager;
		this.factory = factory;
		state = SimulatorState.IDLE;
		this.outputs = new OutputManager();
	}
	
	@Override
	public String toString(){
		return "simulator_"+number;
	}
	
	public String folder(){
		return manager().outputFolder();
	}
	
	/**
	 * to get the manager
	 * @return the manager
	 */
	public SimulationManager manager(){
		return manager;
	}
	
	/**
	 * to get the factory
	 * @return the factory
	 */
	public SimulationFactory factory(){
		return factory;
	}
	
	/**
	 * to get the simulation number
	 * @return the simulation number
	 */
	public int number(){
		return number;
	}
	
	/**
	 * to add a scenario
	 * @param scenario the scenario to add
	 */
	private void add(Scenario scenario){
		scenarios[scenario.number()-1] = scenario;
		//scenarios[scenarios.length-scenario.number()] = scenario;
		//scenarios[scenario.number()-1-126] = scenario;
	}
	
	/**
	 * the initialisation method
	 * @param number the simulation number
	 */
	public void init(int number){
		manager.display("simulator "+number+" initialization");
		this.number = number;
			
		// creation du dossier pour les resultats
		new File(manager().outputFolder()).mkdirs();
			
		manager.display("scenarios creation");
		// creation des scenarios ??? dans l'initialisation ???
		scenarios = new Scenario[manager.scenarios()];
		for(int i=1; i<=scenarios.length; i++){
			add(factory().createScenario(this, /*manager.number() + */i));
		}
		
		up(25);
			
		manager.display("APILand model creation");
		// creation du model
		model = new APILandModel("model", manager.start(), manager.delay(), this, new DefaultDynamicMap());
	
		up(25);
			
		// initialisation du model
		initModel();
		
		// initialisation des sorties
		outputs.init(this);
			
		up(25);
			
		// sauvegarde du model ??? toujours ? seulement lorsqu'il y a plus d'1 seul scenario
		if(manager.scenarios() > 1){
			//saveModel();
		}
			
		// changement d'etat
		state = SimulatorState.INIT;
		
		up(25);
	}

	/**
	 * the initialization method in batch mode
	 * @param path the path of data
	 * @param number the simulation number
	 */
	public void init(String path){
		batchMode = true; 
		manager.init(path+"/experience.properties");
		init(Integer.parseInt(new File(path).getName().replace("exp", "")));
	}
	
	/**
	 * to run
	 */
	public boolean run() {
		manager.display("run simulator");
		boolean simulationOk = true;
		if(state == SimulatorState.INIT){
			state = SimulatorState.RUN;
			Scenario s;
			for(int i=0; i<manager().scenarios(); i++){
				s = scenarios[i];
				if(batchMode){
					s.init(manager().path()+"/scenario_"+s.number()+".properties");
				}else{
					s.init();
				}
				if(!s.run()){
					simulationOk = false;
				}
				s.close();
			}
			
			// calul des sorties
			outputs.calculate(this);
			
			state = SimulatorState.FINISHED;
		}
		return simulationOk;
	}
	
	public void runAndDelete() {
		manager.display("run simulator");
		
		if(state == SimulatorState.INIT){
			state = SimulatorState.RUN;
			Scenario s;
			for(int i=0; i<manager().scenarios(); i++){
				s = scenarios[i];
				if(batchMode){
					s.init(manager().path()+"/scenario_"+s.number()+".properties");
				}else{
					s.init();
				}
				s.run();
				s.close();
				s.delete();
			}
			
			// calul des sorties
			outputs.calculate(this);
						
			state = SimulatorState.FINISHED;
		}
	}
	
	public void close(){
		manager.display("close simulator "+number);
		
		// fermeture des sorties
		outputs.close(this);
	}
	
	protected void initModel(){
		// do nothing
	}
	
	private void saveModel(){
		//manager.display("save simulator "+number+" model");
		try {
			ModelManager.save(model, manager().outputFolder()+"simulator.model");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Scenario[] scenarios() {
		return scenarios;
	}
	
	public void deleteScenario(Scenario s){
		for(int i=0; i<scenarios.length; i++){
			if(scenarios[i] != null && scenarios[i].equals(s)){
				scenarios[i] = null;
				return;
			}
		}
	}

	public DynamicMap map() {
		return model.map();
	}

	public APILandModel model() {
		return model;
	}

	public SimulatorState getState() {
		return state;
	}

	public void setState(SimulatorState state) {
		this.state = state; 
	}

	public void setSimulatorProcess(SimulatorProcess sp) {
		up = 0;
		manager.up(up);
		manager.setSimulatorProcess(sp);
	}

	public void up(int progress) {
		this.up += progress;
		manager().up(up);
	}
	
	public OutputManager outputs(){
		return outputs;
	}
	
	public void addOutput(OutputAnalysis out){
		outputs.add(out);
	}
	
}
