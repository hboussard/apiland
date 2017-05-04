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
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import fr.inra.sad_paysage.apiland.core.element.map.DynamicMap;
import fr.inra.sad_paysage.apiland.simul.model.APILandModel;
import fr.inra.sad_paysage.apiland.simul.model.ModelManager;

public class Scenario implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private boolean run;
	
	private int number;
	
	//private Simulation[] simulations;
	//private List<Simulation> simulations;
	
	private Simulator simulator;
	
	private APILandModel model;
	
	private Map<String,String> settings;
	
	private String folder;

	/** output manager */
	//private OutputManager outputM;
	
	public Scenario(Simulator simulator, int number){
		this.simulator = simulator;
		this.number = number;
		this.settings = new HashMap<String,String>();
		//this.outputM = new OutputManager();
	}
	
	@Override
	public String toString(){
		return "scenario_"+number;
	}
	
	public String folder(){
		return folder;
	}
	
	public SimulationManager manager(){
		return simulator.manager();
	}
	
	public SimulationFactory factory(){
		return simulator.factory();
	}
	
	public Simulator simulator(){
		return simulator;
	}
	
	public OutputManager outputs(){
		return simulator.outputs();
	}
	
	public int number(){
		return number;
	}
	
	private void add(Simulation simulation){
		//simulations[simulation.number()-1] = simulation;
		//simulations.add(simulation);
	}
	
	protected void init(){
		// model loading
		// seulement lorsqu'il y a plus d'1 scenario
		/*if(manager().scenarios() > 1){
			loadModel();
		}else{*/
			model = simulator.model();
		//}
		
		folder = manager().expFolder()+"/outputs/scenario_"+number+"/";
		new File(folder).mkdir();
		
		//simulations = new Simulation[manager().simulations()];
		//simulations = new ArrayList<Simulation>(manager().simulations());
		run = false;
		
		for(Entry<String,String> e : settings.entrySet()){
			manager().setSettings(e.getKey(), e.getValue());
		}
		
		// model initialization
		initModel();
		
		// initialisation des sorties
		outputs().init(this);
		
		// model saving
		//saveModel(); // tout le temps
		// seulement lorsqu'il y a plus d'1 simulation
		if(manager().simulations() > 1){
			//saveModel();
		}
		
	}
	
	protected void init(String propertiesFile){
		manager().init(propertiesFile);
		init();
	}
	
	protected void initModel(){ 
		// do nothing
	}
	
	private void loadModel() {
		//manager().display("load scenario "+simulator().number()+"_"+number+" model");
		try {
			model = (APILandModel)ModelManager.load(manager().expFolder()+"/outputs/simulator.model");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private void saveModel(){
		//manager().display("save scenario "+simulator().number()+"_"+number+" model");
		try {
			ModelManager.save(model, folder+"scenario.model");
			//model = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run(){
		manager().display("run scenario "+simulator().number()+"_"+number);
		if(!run){
			Simulation s;
			int success = 0;
			for(int i=0; conditionContinuation(i, success)/*i<manager().simulations()*/; i++){
				// reinitialisation du manager
				manager().setCancelled(false);
				
				s = factory().createSimulation(this,(i+1));
				add(s);
				s.init();
				if(s.run()){
					success++;
				}
				s.close();			
				
				// progression de la barre
				//simulator.up(100/(manager().scenarios()*manager().simulations()));
			}
			
			// calcul des sorties
			outputs().calculate(this);
		}
	}
	
	protected boolean conditionContinuation(int index, int success) {
		return index<manager().simulations() || success<manager().success();
	}

	protected void close(){
		manager().display("close scenario "+simulator().number()+"_"+number);	
		
		run = true;
		
		// fermeture des sorties
		outputs().close(this);
		
		if(new File(folder).list().length == 0){
			new File(folder).delete();
		}
	}

	/*public Simulation[] simulations() {
		//return simulations;
		return simulations.toArray(new Simulation[simulations.size()]);
	}*/

	public APILandModel model() {
		return model;
	}
	
	public DynamicMap map(){
		return model.map();
	}
	
	public void setSettings(String key, String value){
		settings.put(key, value);
	}

	public void setSimulations(Simulation[] simulations) {
		//this.simulations = simulations;
		throw new UnsupportedOperationException();
	}

	public void setSimulator(Simulator simulator) {
		this.simulator = simulator;
	}

	public void setModel(APILandModel model) {
		this.model = model;
	}

	public void setSettings(Map<String, String> settings) {
		this.settings = settings;
	}

	public void delete(){
		simulator().deleteScenario(this);
		simulator = null;
		model.delete();
		model = null;
		/*for(int i=0; i<simulations.length; i++){
			simulations[i].delete();
			simulations[i] = null;
		}*/
		//simulations.clear();
		//simulations = null;
		settings.clear();
		settings = null;
	}
	
	/*
	public OutputManager getOutputManager(){
		return outputM;
	}*/
	
	/*
	public void addOutput(OutputAnalysis out){
		outputM.add(out);
	}
	*/
	
}
