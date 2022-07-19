package fr.inra.sad.bagap.apiland.capfarm.simul.ecological;

public class EcologicalModelMetric {

	private String metric;
	
	private int buffer;
	
	public EcologicalModelMetric(String metric, int buffer){
		this.metric = metric;
		this.buffer = buffer;
	}

	public String getMetric() {
		return metric;
	}

	public String toString(){
		return metric+" "+buffer;
	}
	
	public int getBuffer() {
		return buffer;
	}
	
}
