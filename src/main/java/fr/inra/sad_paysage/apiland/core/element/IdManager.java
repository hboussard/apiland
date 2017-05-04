package fr.inra.sad_paysage.apiland.core.element;

public class IdManager {

	private static IdManager instance = new IdManager();
	
	private int id;
	
	private IdManager(){
		id = 0;
	}
	
	public static IdManager get(){
		return instance;
	}
	
	public String getId(){
		return (id++)+"";
	}
	
}
