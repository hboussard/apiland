package fr.inra.sad_paysage.apiland.analysis.matrix.counting;

import fr.inra.sad_paysage.apiland.analysis.matrix.process.MatrixProcess;
import fr.inra.sad_paysage.apiland.analysis.metric.BasicMetric;
import fr.inra.sad_paysage.apiland.analysis.metric.ClassMetric;
import fr.inra.sad_paysage.apiland.analysis.metric.CoupleMetric;
import fr.inra.sad_paysage.apiland.analysis.metric.Metric;
import fr.inra.sad_paysage.apiland.analysis.metric.PatchMetric;
import fr.inra.sad_paysage.apiland.analysis.metric.QuantitativeMetric;
import fr.inra.sad_paysage.apiland.analysis.metric.ValueMetric;

public class CountingFactory {

	public static Counting create(MatrixProcess process) {
		Counting counting = new BasicCounting();
		counting.setProcess(process);
		
		for(Metric m : process.processType().metrics()){
			if(m instanceof BasicMetric){
				// do nothing
			}
			if(m instanceof CoupleMetric){
				if(!counting.isBinding(CoupleCounting.class)){
					counting = new CoupleCounting(counting);
				}
			}
			if(m instanceof ValueMetric){
				if(!counting.isBinding(ValueCounting.class)){
					counting = new ValueCounting(counting);
				}
			}
			if(m instanceof ClassMetric){
				if(!counting.isBinding(ClassCounting.class)){
					counting = new ClassCounting(counting);
				}
			}
			if(m instanceof QuantitativeMetric){
				if(!counting.isBinding(QuantitativeCounting.class)){
					counting = new QuantitativeCounting(counting);
				}
			}
			if(m instanceof PatchMetric){
				if(!counting.isBinding(PatchCounting.class)){
					counting = new PatchCounting(counting);
				}
			}
		}
		
		return counting;
	}
	
}
