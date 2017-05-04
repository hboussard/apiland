package fr.inra.sad_paysage.apiland.analysis.matrix.metric.qualitative.couple;

import fr.inra.sad_paysage.apiland.analysis.VariableManager;
import fr.inra.sad_paysage.apiland.analysis.matrix.counting.Counting;
import fr.inra.sad_paysage.apiland.analysis.matrix.metric.MatrixMetric;
import fr.inra.sad_paysage.apiland.analysis.metric.CoupleMetric;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.Raster;
import fr.inra.sad_paysage.apiland.core.util.Couple;

public class EdgeCoupleMetric extends MatrixMetric implements CoupleMetric {

	private double couple;
	
	public EdgeCoupleMetric(Integer i1, Integer i2) {
		super(VariableManager.get("edge_"+i1+"-"+i2));
		this.couple = Couple.get(i1, i2);
	}

	@Override
	public void doCalculate(Counting co) {
		if(co.countCouples() > 0){
			value = co.countCouple(couple) * Raster.getCellSize();
		}
	}

}
