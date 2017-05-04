package fr.inra.sad_paysage.apiland.analysis.matrix.counting;

import java.util.Set;

public interface CoupleCountingInterface {

	Set<Double> couples();
	
	int totalCouples();
	
	int validCouples();
	
	int countCouples();
	
	int homogeneousCouples();
	
	int unhomogeneousCouples();
	
	int countCouple(double c);
	
}
