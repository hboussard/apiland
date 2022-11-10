package fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.qualitative.couple;

import fr.inra.sad.bagap.apiland.analysis.VariableManager;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.Counting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.MatrixMetric;
import fr.inra.sad.bagap.apiland.analysis.process.metric.CoupleMetric;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.core.util.Couple;

public class EdgeCoupleMetric extends MatrixMetric implements CoupleMetric {

	private double couple;
	
	public EdgeCoupleMetric(Integer i1, Integer i2) {
		super(VariableManager.get("E_"+i1+"-"+i2));
		this.couple = Couple.get(i1, i2);
	}

	@Override
	public void doCalculate(Counting co) {
		if(co.countCouples() > 0){
			value = co.countCouple(couple) * Raster.getCellSize();
		}
	}

}
