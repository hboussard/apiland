package fr.inra.sad.bagap.apiland.simul.operation;

import fr.inra.sad.bagap.apiland.core.element.manager.DynamicElementBuilder;

public class OpTypeDecoupage extends OpTypeComposition{

	private static final long serialVersionUID = 1L;

	private DynamicElementBuilder builder;
	
	public OpTypeDecoupage() {
		super();
	}

	@Override
	public OpDecoupage getOperation() {
		return new OpDecoupage(this);
	}
	
	private void setBuilder(DynamicElementBuilder builder){
		this.builder = builder;
	}
	
	public DynamicElementBuilder getBuilder(){
		return this.builder;
	}
	
	public boolean setParameter(String name, Object value){
		if(name.equalsIgnoreCase("builder")){
			try{
				setBuilder((DynamicElementBuilder)value);
				return true;
			}catch(IllegalArgumentException e){
				e.printStackTrace();
			}
		}
		return super.setParameter(name, value);
	}
	
	

}
