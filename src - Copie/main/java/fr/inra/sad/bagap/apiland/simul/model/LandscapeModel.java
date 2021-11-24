package fr.inra.sad.bagap.apiland.simul.model;

import java.util.ArrayList;
import java.util.List;

import fr.inra.sad.bagap.apiland.core.element.map.DynamicMap;
import fr.inra.sad.bagap.apiland.core.time.Instant;
import fr.inra.sad.bagap.apiland.simul.Simulator;
import fr.inra.sad.bagap.apiland.simul.operation.Operation;

public class LandscapeModel extends Model {

	private static final long serialVersionUID = 1L;

	private DynamicMap map;
	
	private List<Operation> operations;
	
	private List<LandscapeAction> actions;
	
	public LandscapeModel(Simulator simulator, DynamicMap map) {
		super("landscape", simulator);
		this.map = map;
		this.operations = new ArrayList<Operation>();
		this.actions = new ArrayList<LandscapeAction>();
	}
	
	public DynamicMap map() {
		return map;
	}
	
	public void addOperation(Operation operation){
		operations.add(operation);
	}
	
	public void removeOperation(Operation operation){
		operations.remove(operation);
	}
	
	public void removeAllOperations(){
		operations.clear();
	}
	
	/*
	public void removeOperationsByElement(DynamicElement element){
		Iterator<Operation> ite = operations.iterator();
		while(ite.hasNext()){
			if(ite.next().getElement().equals(element)){
				ite.remove();
			}
		}
	}
	*/
	
	public List<Operation> getOperations(){
		return operations;
	}

	public void addAction(LandscapeAction action){
		actions.add(action);
	}
	
	@Override
	public boolean run(Instant t) {
		for(Operation op : operations){
			if(!op.make(t, getElement())){
				return false;
			}
		}
		return true;
		/*
		Iterator<LandscapeAction> ite = actions.iterator();
		while(ite.hasNext()){
			if(!ite.next().action(t)){
				ite.remove();
			}
		}
		*/
	}
	
	@Override
	public void delete(){
		super.delete();
		for(Operation o : operations){
			o.delete();
		}
		operations.clear();
		operations = null;
		for(LandscapeAction la : actions){
			la.delete();
		}
		actions.clear();
		actions = null;
		map.delete();
		map = null;
	}

}
