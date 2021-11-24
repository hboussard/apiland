package fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.qualitative.couple;

import fr.inra.sad.bagap.apiland.analysis.VariableManager;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.Counting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.MatrixMetric;
import fr.inra.sad.bagap.apiland.analysis.process.metric.CoupleMetric;
import fr.inra.sad.bagap.apiland.analysis.process.metric.DistanceValueMetric;
import fr.inra.sad.bagap.apiland.core.util.Couple;
import fr.inra.sad.bagap.apiland.core.util.DistanceValueMatrix;

public class EdgeContrastMetric extends MatrixMetric implements CoupleMetric, DistanceValueMetric {

	private int v;
	
	private DistanceValueMatrix distances;
	
	public EdgeContrastMetric(Integer v) {
		super(VariableManager.get("ECON_"+v));
		this.v = v;
	}
	
	@Override
	public void setDistanceMatrix(DistanceValueMatrix dvm){
		distances = dvm;
	}

	@Override
	protected void doCalculate(Counting co) {
		if(co.countCouples() > 0){
			value = 0;
			int sum = 0;
			for(double c : co.couples()){
				if(!Couple.isHomogeneous(c) && Couple.contains(c, v)){
					int nbC = (int) co.countCouple(c);
					sum += nbC;
					value += nbC * distances.getDistance(v, Couple.getOther(c, v));
				}
			}
			value = (value/sum) * 100; 
		}
	}
	

}
