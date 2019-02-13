package fr.inra.sad.bagap.apiland.analysis.matrix.process.counting;

import java.util.Set;

public interface CoupleCountingInterface {

	Set<Double> couples();
	
	double totalCouples();
	
	double validCouples();
	
	double countCouples();
	
	double homogeneousCouples();
	
	double heterogeneousCouples();
	
	double countCouple(double c);
	
}
