package fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.qualitative.value;

import fr.inra.sad.bagap.apiland.analysis.VariableManager;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.Counting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.MatrixMetric;
import fr.inra.sad.bagap.apiland.analysis.process.metric.ValueMetric;

// TODO : ajouter une métrique prenant en compte la taille du pixel (au carré) pour avoir la surface de chaque type de cover
public class CountValueMetric extends MatrixMetric implements ValueMetric {

	private int v;
	
	public CountValueMetric(Integer v) {
		super(VariableManager.get("NV_"+v));
		this.v = v;
	}

	@Override
	public void doCalculate(Counting co) {
		if(co.validValues() > 0){
			value = co.countValue(v);
		}
	}

}
