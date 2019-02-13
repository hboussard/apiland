package fr.inra.sad.bagap.apiland.analysis.matrix.process.counting;

import fr.inra.sad.bagap.apiland.patch.PatchComposite;

public interface PatchCountingInterface {

	PatchComposite patches();

	double getPatchNumber();
	
	double getPatchNumber(int classe);

	double getLargestPatchSize();
	
	double getLargestPatchSize(int classe);
	
	double getMeanPatchSize();
	
	double getMeanPatchSize(int classe);
	
	/*
	double getStandardDeviationPatchSize();
	
	double getStandardDeviationPatchSize(int classe);
	
	double getVariationCoefficientPatchSize();
	
	double getVariationCoefficientPatchSize(int classe);
	*/
	
	double getShannonDiversityPatchSize();
	
	double getShannonDiversityPatchSize(int classe);
}
