package fr.inra.sad.bagap.apiland.analysis.matrix.process.counting;

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
