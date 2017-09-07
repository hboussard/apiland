package fr.inra.sad.bagap.apiland.simul.model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ModelManager {

	public static void save(Model model, String file) throws IOException{
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
		try{
			out.writeObject(model);
		}
		finally{
			out.flush();
			out.close();
		}
	}

	public static Model load(String file) throws IOException, ClassNotFoundException {
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
		Model model = (Model)in.readObject();
		in.close();
		return model;
	}
	
}
