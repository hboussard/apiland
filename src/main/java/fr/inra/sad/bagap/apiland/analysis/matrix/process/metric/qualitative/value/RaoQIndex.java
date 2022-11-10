package fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.qualitative.value;

import java.util.Set;
import java.util.TreeSet;
import fr.inra.sad.bagap.apiland.analysis.VariableManager;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.Counting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.MatrixMetric;
import fr.inra.sad.bagap.apiland.analysis.process.metric.DistanceValueMetric;
import fr.inra.sad.bagap.apiland.analysis.process.metric.ValueMetric;
import fr.inra.sad.bagap.apiland.core.util.DistanceValueMatrix;

public class RaoQIndex extends MatrixMetric implements ValueMetric, DistanceValueMetric {

	private DistanceValueMatrix distances;
	
	public RaoQIndex() {
		super(VariableManager.get("RaoQ"));
	}
	
	@Override
	public void setDistanceMatrix(DistanceValueMatrix dvm){
		distances = dvm;
	}
	
	@Override
	public void doCalculate(Counting co) {
		if(co.countValues() > 0){
			value = 0;
			double p1, p2; 
			Set<Integer> ever = new TreeSet<Integer>();
			for(int v1 : co.values()){
				ever.add(v1);
				p1 = co.countValue(v1) / (double)co.validValues();
				for(int v2 : co.values()){
					if(!ever.contains(v2)){
						p2 = co.countValue(v2) / (double)co.validValues();
						//System.out.println(v1+" "+v2);
						value += (/*1 +*/ distances.getDistance(v1, v2)) * p1 * p2;
					}
				}
			}
		}
	}
	
}
