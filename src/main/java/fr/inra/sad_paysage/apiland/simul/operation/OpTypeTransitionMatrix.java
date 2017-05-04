package fr.inra.sad_paysage.apiland.simul.operation;

import fr.inra.sad_paysage.apiland.core.time.delay.Delay;
import fr.inra.sad_paysage.apiland.core.util.TransitionMatrix;

public class OpTypeTransitionMatrix extends OpTypeComposition{

	private static final long serialVersionUID = 1L;

	private TransitionMatrix<?> tMat;
	
	private Delay delay;
	
	public OpTypeTransitionMatrix(){
		super();
	}
	
	@Override
	public void reset(){
		super.reset();
		tMat = null;
		delay = null;
	}
	
	public TransitionMatrix<?> getTransitionMatrix(){
		return tMat;
	}
	
	public Delay getDelay(){
		return delay;
	}
	
	public boolean setParameter(String name, Object value){
		if(name.equalsIgnoreCase("transition_matrix")){
			try{
				tMat = (TransitionMatrix<?>)value;
				return true;
			}catch(IllegalArgumentException e){
				e.printStackTrace();
			}
		}
		if(name.equalsIgnoreCase("delay")){
			try{
				delay = (Delay)value;
				return true;
			}catch(IllegalArgumentException e){
				e.printStackTrace();
			}
		}
		return super.setParameter(name, value);
	}

	@Override
	public OpTransitionMatrix getOperation() {
		return new OpTransitionMatrix(this);
	}
	
}