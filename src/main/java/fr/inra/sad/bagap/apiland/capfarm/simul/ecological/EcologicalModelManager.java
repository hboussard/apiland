package fr.inra.sad.bagap.apiland.capfarm.simul.ecological;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class EcologicalModelManager {

	private static Map<String, EcologicalModelAnalysis> models = new TreeMap<String, EcologicalModelAnalysis>();
	
	private static Map<Integer, Set<EcologicalModelMetric>> metricsByBuffer = new TreeMap<Integer, Set<EcologicalModelMetric>>();
	
	public static boolean empty(){
		return models.size() == 0;
	}
	
	public static void display(){
		for(EcologicalModelAnalysis ema : models.values()){
			System.out.println();
			ema.display();
		}
	}

	public static void add(EcologicalModelAnalysis model) {
		models.put(model.getName(), model);
		for(EcologicalModelMetric metric : model.getMetrics()){
			if(!metricsByBuffer.containsKey(metric.getBuffer())){
				metricsByBuffer.put(metric.getBuffer(), new HashSet<EcologicalModelMetric>());
			}
			metricsByBuffer.get(metric.getBuffer()).add(metric);
		}
	}
	
	public static Map<Integer, Set<EcologicalModelMetric>> getMetricsbyBuffer(){
		return metricsByBuffer;
	}
	
}
