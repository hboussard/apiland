package fr.inra.sad.bagap.apiland.treatment.view;

import fr.inra.sad.bagap.apiland.analysis.matrix.util.SpatialCsvManager;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;
import fr.inra.sad.bagap.apiland.treatment.Treatment;
import fr.inra.sad.bagap.apiland.treatment.TreatmentObserver;
import fr.inra.sad.bagap.apiland.treatment.TreatmentState;

public class CsvView implements TreatmentObserver {

	private String csvPort;
	
	private String matrixPort;
	
	private String asciiFolder;
	
	public CsvView(String csvPort, String matrixPort, String asciiFolder){
		this.csvPort = csvPort;
		this.matrixPort = matrixPort;
		this.asciiFolder = asciiFolder;
	}
	
	@Override
	public void notify(Treatment t, TreatmentState s) {
		switch(s){
		case DONE : 
			SpatialCsvManager.exportAsciiGrid((String)t.getInput(csvPort), asciiFolder, null, (Matrix) t.getInput(matrixPort));
			break;
		}
	}
	
	@Override
	public void updateProgression(Treatment t, int total) {
		// do nothing
	}

}
