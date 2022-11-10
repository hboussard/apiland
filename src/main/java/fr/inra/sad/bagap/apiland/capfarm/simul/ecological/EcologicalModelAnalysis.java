package fr.inra.sad.bagap.apiland.capfarm.simul.ecological;

import fr.inra.sad.bagap.apiland.analysis.Analysis;

public class EcologicalModelAnalysis extends Analysis {

	private String name;
	
	private double intercept;
	
	private double[] coeffHabitats;
	
	private int[] codeHabitats;
	
	private Double[] coeffMetrics;
	
	private EcologicalModelMetric[] metrics;
	
	public EcologicalModelAnalysis(String name, double intercept, double[] coeffHabitats, int[] codeHabitats, Double[] coeffMetrics, EcologicalModelMetric[] metrics){
		this.name = name;
		this.intercept = intercept;
		this.coeffHabitats = coeffHabitats;
		this.codeHabitats = codeHabitats;
		this.coeffMetrics = coeffMetrics;
		this.metrics = metrics;
	}
	
	public void display(){
		System.out.println("model : "+name);
		System.out.println("intercept : "+intercept);
		if(codeHabitats != null){
			for(int i=0; i<coeffHabitats.length; i++){
				System.out.println(coeffHabitats[i]+" "+codeHabitats[i]);	
			}
		}
		for(int i=0; i<coeffMetrics.length; i++){
			System.out.println(metrics[i]+" : "+coeffMetrics[i]);	
		}
	}
	
	
	public String getName(){
		return name;
	}
	
	public EcologicalModelMetric[] getMetrics(){
		return metrics;
	}
	
	@Override
	protected void doInit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void doRun() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void doClose() {
		// TODO Auto-generated method stub
		
	}

}
