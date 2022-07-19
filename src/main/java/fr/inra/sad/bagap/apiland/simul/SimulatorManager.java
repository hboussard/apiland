package fr.inra.sad.bagap.apiland.simul;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import fr.inra.sad.bagap.apiland.core.element.map.MapManager;
import fr.inra.sad.bagap.apiland.simul.model.ModelManager;

public class SimulatorManager {

	public static void save(Simulator simulator, String file) throws IOException{
		System.out.println("save the simulator");
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
		try{
			out.writeObject(simulator);
		}
		finally{
			out.flush();
			out.close();
		}
	}

	public static Simulator load(String file) throws IOException, ClassNotFoundException {
		System.out.println("load the simulator");
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
		Simulator simulator = (Simulator)in.readObject();
		in.close();
		return simulator;
	}
	
	public void saveDynamicMap(Simulator simulator){
		System.out.println("save the map of experience "+simulator.number());
		try {
			MapManager.save(simulator.map(), simulator.manager().path()+"simulation/experience_"+simulator.number()+"/experience.map");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void saveExternalModels(Simulator simulator){
		System.out.println("save the model of experience "+simulator.number());
		try {
			ModelManager.save(simulator.model(), simulator.manager().path()+"simulation/experience_"+simulator.number()+"/experience.model");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
