package fr.inra.sad_paysage.apiland.analysis.matrix.process;

import fr.inra.sad_paysage.apiland.analysis.matrix.counting.Counting;
import fr.inra.sad_paysage.apiland.analysis.matrix.counting.CountingFactory;
import fr.inra.sad_paysage.apiland.analysis.matrix.metric.MatrixMetric;
import fr.inra.sad_paysage.apiland.analysis.process.ProcessState;
import fr.inra.sad_paysage.apiland.analysis.process.Process;

/**
 * modeling class of a process
 * @author H.Boussard
 */
public abstract class MatrixProcess extends Process<MatrixMetric> {
	
	/** the counting */
	protected Counting counting;
	
	/** the max size and the current size */
	private int maxSize, currentSize;
	
	/** constructor */
	public MatrixProcess(){
		super();
		currentSize = 0;
	}
	
	/**
	 * constructor
	 * @param pt the process type
	 */
	public MatrixProcess(MatrixProcessType pt){
		super(pt);
		currentSize = 0;
	}
	
	@Override
	public boolean equals(Object other){
		if(other instanceof MatrixProcess){
			return ((MatrixProcess) other).processType().equals(this.processType());
		}
		return false;
	}
	
	@Override
	public MatrixProcessType processType(){
		return (MatrixProcessType) super.processType();
	}
	
	/**
	 * to get the process counting 
	 * @return the counting
	 */
	public Counting counting(){
		return counting;
	}
	
	/**
	 * to init properly the process
	 */
	public void init(){
		if(state().equals(ProcessState.IDLE)){
			counting = CountingFactory.create(this);
			counting.init();
			setState(ProcessState.INIT);
		}
	}

	/**
	 * to delete properly the process
	 */
	public void delete(){
		counting.delete();
		counting = null;
	}
	
	/** @return the maximum size */
	public int maxSize(){
		return maxSize;
	}

	/**
	 * to set the maximum size
	 * @param s the maximum size
	 */
	public void setMaxSize(int s) {
		this.maxSize = s;
	}

	/** @return the current size */
	public int currentSize(){
		return currentSize;
	}
	
	/**
	 * to add the current size +1
	 */
	public void addCurrentSize(){
		currentSize++;
	}
	
	/**
	 * to minus the current size -1
	 */
	public void minusCurrentSize(){
		if(currentSize > 0){
			currentSize--;
		}
	}
	
	/**
	 * to calculate the metrics
	 */
	public void calculateMetrics(){
		if(state() == ProcessState.READY
				|| state() == ProcessState.FINISH){
			for(MatrixMetric m : processType().metrics()){
				m.calculate(this, "");
			}
			setState(ProcessState.DONE);
		}
	}
	
	/**
	 * to not calculate the metrics
	 */
	public void unCalculateMetrics(){
		if(state() == ProcessState.READY
				|| state() == ProcessState.FINISH){
			for(MatrixMetric m : processType().metrics()){
				m.unCalculate(this, "");
			}
			setState(ProcessState.DONE);
		}
	}
	
}
