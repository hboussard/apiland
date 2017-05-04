package fr.inra.sad_paysage.apiland.simul.dependencie;

import java.util.ArrayList;
import java.util.List;

import fr.inra.sad_paysage.apiland.core.change.Changeable;
import fr.inra.sad_paysage.apiland.core.change.ChangeableObserver;
import fr.inra.sad_paysage.apiland.core.element.DynamicElement;
import fr.inra.sad_paysage.apiland.core.time.Instant;
import fr.inra.sad_paysage.apiland.core.time.delay.Delay;
import fr.inra.sad_paysage.apiland.simul.Simulator;
import fr.inra.sad_paysage.apiland.simul.model.AtomicModel;
import fr.inra.sad_paysage.apiland.simul.operation.Operation;

public class LayerDependence extends AtomicModel implements ChangeableObserver {

	private static final long serialVersionUID = 1L;
	
	private List<Operation> operations;
	
	private boolean update;

	public LayerDependence(String name, Instant start, Delay delay, Operation operation, Simulator simulator, DynamicElement element){
		super(name, start, delay, simulator, element);
		operations = new ArrayList<Operation>();
		addOperation(operation);
		update = true;
	}
	
	@Override
	public void update(Instant t, Changeable c, Object o) {
		update = true;
	}

	public void addOperation(Operation operation){
		operations.add(operation);
		//operation.setModel(this);
	}
	
	public void resetOperation(){
		operations = new ArrayList<Operation>();
	}
	
	protected List<Operation> getOperations(){
		return operations;
	}
	
	@Override
	public boolean make(Instant t) {
		if(update){
			boolean ok = true;
			for(Operation op : operations){
				if(!op.make(t, getElement())){
					ok = false;
				}
			}
			update = false;
			return ok;
		}
		return true;
	}
	
}
