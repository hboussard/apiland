package fr.inra.sad_paysage.apiland.analysis.matrix.metric.qualitative.couple;

import fr.inra.sad_paysage.apiland.analysis.VariableManager;
import fr.inra.sad_paysage.apiland.analysis.matrix.counting.Counting;
import fr.inra.sad_paysage.apiland.analysis.matrix.metric.MatrixMetric;
import fr.inra.sad_paysage.apiland.analysis.metric.CoupleMetric;
import fr.inra.sad_paysage.apiland.analysis.metric.ValueMetric;
import fr.inra.sad_paysage.apiland.core.util.Couple;

public class AggregationClassIndex extends MatrixMetric implements ValueMetric, CoupleMetric {

	private int classMetric;
	
	public AggregationClassIndex(Integer cm){
		super(VariableManager.get("AI_"+cm));
		classMetric = cm;
	}
	
	@Override
	public void doCalculate(Counting co) {
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
			value =  new Double(g) / G * 100.0;
		}
	}
	
}
