package fr.inra.sad_paysage.apiland.treatment.view;

import fr.inra.sad_paysage.apiland.treatment.Treatment;
import fr.inra.sad_paysage.apiland.treatment.TreatmentState;
import fr.inra.sad_paysage.apiland.treatment.TreatmentView;

public class ConsoleView implements TreatmentView {

	@Override
	public void notify(Treatment t, TreatmentState s) {
		System.out.println("le traitement "+t+" passe à l'état "+s);
		switch(s){
		case DONE : t.displayPorts(); break;
		}
	}

	@Override
	public void updateProgression(int total) {
		// do nothing
	}

}
