package fr.inra.sad.bagap.apiland.treatment.view;

import fr.inra.sad.bagap.apiland.treatment.Treatment;
import fr.inra.sad.bagap.apiland.treatment.TreatmentObserver;
import fr.inra.sad.bagap.apiland.treatment.TreatmentState;

public class ConsoleView implements TreatmentObserver {

	@Override
	public void notify(Treatment t, TreatmentState s) {
		System.out.println("le traitement "+t+" passe a l'etat "+s);
		switch(s){
		case DONE : t.displayPorts(); break;
		}
	}

	@Override
	public void updateProgression(Treatment t, int total) {
		// do nothing
	}

}
