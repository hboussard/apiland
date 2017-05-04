package fr.inra.sad_paysage.apiland.simul.operation;

import fr.inra.sad_paysage.apiland.simul.operation.OperationType;

public abstract class OpTranslationType extends OperationType{

	private static final long serialVersionUID = 1L;

	private String representation;
	
	@Override
	public void reset(){
		super.reset();
		representation = null;
	}
	
	private void setRepresentation(String representation){
		this.representation = representation;
	}
	
	public String getRepresentation() {
		return representation;
	}
	
	@Override
	public boolean setParameter(String name, Object value){
		if(name.equalsIgnoreCase("representation")){
			try{
				setRepresentation((String)value);
				return true;
			}catch(IllegalArgumentException e){
				e.printStackTrace();
			}
		}
		return super.setParameter(name, value);
	}

	
}
