package fr.inra.sad_paysage.apiland.simul.operation;

import fr.inra.sad_paysage.apiland.simul.operation.Operation;

public abstract class OpTranslation extends Operation {

	private static final long serialVersionUID = 1L;

	private String representation;
	
	public OpTranslation(OpTranslationType type) {
		super(type);
		this.representation = type.getRepresentation();
	}
	
	public String getRepresentation() {
		return representation;
	}
	
	@Override
	public void delete(){
		super.delete();
		representation = null;
	}
	
}
