package fr.inra.sad_paysage.apiland.simul;

public abstract class OutputVariable {

	private String name;
	
	public OutputVariable(String name){
		this.name = name;
	}
	
	@Override
	public String toString(){
		return name;
	}
	
	public String getName(){
		return name;
	}
	
}
