package fr.inra.sad_paysage.apiland.treatment.view;

import fr.inra.sad_paysage.apiland.core.space.impl.raster.matrix.Matrix;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.matrix.MatrixManager;
import fr.inra.sad_paysage.apiland.treatment.Treatment;
import fr.inra.sad_paysage.apiland.treatment.TreatmentState;
import fr.inra.sad_paysage.apiland.treatment.TreatmentObserver;

public class MatrixView implements TreatmentObserver {
	
	private String matrixPort;
	
	private String ascii;
	
	public MatrixView(String matrixPort, String ascii){
		this.matrixPort = matrixPort;
		this.ascii = ascii;
	}
	
	public String getPort(){
		return matrixPort;
	}
	
	@Override
	public void notify(Treatment t, TreatmentState s) {
		switch(s){
		case DONE : 
			MatrixManager.exportAsciiGrid((Matrix) t.getOutput(matrixPort), ascii); 
			break;
		}
	}
	
	@Override
	public void updateProgression(Treatment t, int total) {
		// do nothing
	}

}
