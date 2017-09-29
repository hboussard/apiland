package fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.qualitative.couple;

import fr.inra.sad.bagap.apiland.analysis.VariableManager;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.Counting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.MatrixMetric;
import fr.inra.sad.bagap.apiland.analysis.process.metric.CoupleMetric;
import fr.inra.sad.bagap.apiland.analysis.process.metric.ValueMetric;
import fr.inra.sad.bagap.apiland.core.util.Couple;

public class AggregationClassIndex extends MatrixMetric implements ValueMetric, CoupleMetric {

	private int classMetric;
	
	public AggregationClassIndex(Integer cm){
		super(VariableManager.get("AI_"+cm));
		classMetric = cm;
	}
	
	@Override
	public void doCalculate(Counting co) {
		value = -1;
		if(co.countCouples() > 0){
			int ai = co.countValue(classMetric);
			int n=1, n2;
			for(; ; n++){
				n2 = (int) Math.pow(n, 2);
				if(n2 == ai){
					break;
				}else if(n2 > ai){
					n--;
					n2 = (int) Math.pow(n, 2);
					break;
				}
			}
			int m = ai - n2;
			int G;
			if(m == 0){
				G = 2*n*(n-1);
			}else if(m <= n){
				G = 2*n*(n-1) + 2*m -1;
			}else{
				G = 2*n*(n-1) + 2*m -2;
			}
			int g = co.countCouple(Couple.get(classMetric, classMetric));
			if(G != 0){
				value =  new Double(g) / G * 100.0;
			}else{
				value = 0;
			}
		}
	}
	
}
