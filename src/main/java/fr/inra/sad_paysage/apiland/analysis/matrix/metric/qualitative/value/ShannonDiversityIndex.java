package fr.inra.sad_paysage.apiland.analysis.matrix.metric.qualitative.value;

import fr.inra.sad_paysage.apiland.analysis.VariableManager;
import fr.inra.sad_paysage.apiland.analysis.matrix.counting.Counting;
import fr.inra.sad_paysage.apiland.analysis.matrix.metric.MatrixMetric;
import fr.inra.sad_paysage.apiland.analysis.matrix.process.SimpleWindowMatrixProcess;
import fr.inra.sad_paysage.apiland.analysis.metric.ValueMetric;

public class ShannonDiversityIndex extends MatrixMetric implements ValueMetric {

	//int tot = 0;
	
	public ShannonDiversityIndex() {
		super(VariableManager.get("SHDI"));
	}
	
	@Override
	public void doCalculate(Counting co) {
		//System.out.println("indice "+((SimpleWindowMatrixProcess) co.process()).window().pixel());
		if(co.countValues() > 0){
			value = 0;
			double p; 
			for(int v : co.values()){
				p = co.countValue(v) / (double)co.validValues();
				if(p != 0){
					value += p*Math.log(p);
				}
			}
			if(value != 0){
				value *= -1;
			}
		}
	}
	
}
