package fr.inra.sad.bagap.apiland.core.element.map;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/** 
 * modeling class of the map manager
 * @author H.Boussard
 */
public class MapManager {

	/** 
	 * to save a map in a file
	 * @param map map to save
	 * @param file file
	 * @throws IOException
	 */
	public static void save(DynamicMap map, String file) throws IOException{
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
		try{
			out.writeObject(map);
		}
		finally{
			out.flush();
			out.close();
		}
	}

	/**
	 * to load a map from a file
	 * @param file file to load
	 * @return a map
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static DynamicMap load(String file) throws IOException, ClassNotFoundException {
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
		DynamicMap map = (DynamicMap)in.readObject();
		in.close();
		return map;
	}
	
	
}
