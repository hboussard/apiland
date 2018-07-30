package fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.qualitative.value;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.inra.sad.bagap.apiland.analysis.VariableManager;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.Counting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.Counting.Count;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.MatrixMetric;
import fr.inra.sad.bagap.apiland.analysis.process.metric.ValueMetric;

public class CountClassValue90Metric extends MatrixMetric implements ValueMetric {
	
	public CountClassValue90Metric() {
		super(VariableManager.get("count_class_90"));
	}

	@Override
	public void doCalculate(Counting co) {
		if(co.countValues() > 0){
			
			int total = 0;
			int v;
			List<Integer> areas = new ArrayList<Integer>();
			
			for(int value : co.values()){
				v = co.countValue(value);
				areas.add(v);
				total += v;
			}
			
			/*for(Count c : co.counts()){
				v = c.get();
				areas.add(v);
				total += v;
			}*/
			Collections.sort(areas);
			Collections.reverse(areas);
			double tot90 = 0.9 * total;
			int count = 0;
			total = 0;
			for(int a : areas){
				count++;
				total += a;
				if(total >= tot90){
					break;
				}
			}
			
			value = count;
		}
	}

}


