package fr.inra.sad.bagap.apiland.analysis.matrix.process.counting;

import fr.inra.sad.bagap.apiland.analysis.matrix.process.MatrixProcess;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.WindowMatrixProcess;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.distance.BasicDistanceCounting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.distance.CoupleDistanceCounting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.distance.PatchDistanceCounting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.distance.QuantitativeDistanceCounting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.distance.ValueDistanceCounting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.threshold.BasicCounting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.threshold.ClassCounting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.threshold.CoupleCounting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.threshold.FullQuantitativeCounting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.threshold.PatchCounting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.threshold.PatchCountingOld;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.threshold.QuantitativeCounting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.threshold.ValueCounting;
import fr.inra.sad.bagap.apiland.analysis.process.metric.BasicMetric;
import fr.inra.sad.bagap.apiland.analysis.process.metric.ClassMetric;
import fr.inra.sad.bagap.apiland.analysis.process.metric.CoupleMetric;
import fr.inra.sad.bagap.apiland.analysis.process.metric.FullQuantitativeMetric;
import fr.inra.sad.bagap.apiland.analysis.process.metric.Metric;
import fr.inra.sad.bagap.apiland.analysis.process.metric.PatchMetric;
import fr.inra.sad.bagap.apiland.analysis.process.metric.QuantitativeMetric;
import fr.inra.sad.bagap.apiland.analysis.process.metric.ValueMetric;

public class CountingFactory {

	public static Counting create(MatrixProcess process) {
		
		Counting counting = null;
		
		int size = ((WindowMatrixProcess) process).window().diameter();
		
		if(process.processType().isDistanceType()){
			
			counting = new BasicDistanceCounting(size);
			counting.setProcess(process);
			
			for(Metric m : process.processType().metrics()){
				if(m instanceof BasicMetric){
					// do nothing
				}
				if(m instanceof ValueMetric){
					if(!counting.isBinding(ValueDistanceCounting.class)){
						counting = new ValueDistanceCounting(counting, size);
					}
				}
				if(m instanceof CoupleMetric){
					if(!counting.isBinding(CoupleDistanceCounting.class)){
						counting = new CoupleDistanceCounting(counting, size);
					}
				}
				if(m instanceof PatchMetric){
					if(!counting.isBinding(PatchDistanceCounting.class)){
						counting = new PatchDistanceCounting(counting);
					}
				}
				if(m instanceof QuantitativeMetric){
					if(!counting.isBinding(QuantitativeDistanceCounting.class)){
						counting = new QuantitativeDistanceCounting(counting, size);
					}
				}
				if(m instanceof FullQuantitativeMetric){
					if(!counting.isBinding(QuantitativeDistanceCounting.class)){
						counting = new QuantitativeDistanceCounting(counting, size);
					}
				}
			}
		}else{
			counting = new BasicCounting();
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
				if(m instanceof FullQuantitativeMetric){
					if(!counting.isBinding(FullQuantitativeCounting.class)){
						counting = new FullQuantitativeCounting(counting, size);
					}
				}
				if(m instanceof PatchMetric){
					if(!counting.isBinding(PatchCounting.class)){
						counting = new PatchCounting(counting);
					}
				}
			}
		}
		
		//System.out.println(counting.toString());
		
		return counting;
	}
	
}
