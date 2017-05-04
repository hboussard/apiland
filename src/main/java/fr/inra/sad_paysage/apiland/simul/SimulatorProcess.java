package fr.inra.sad_paysage.apiland.simul;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;

public abstract class SimulatorProcess extends SwingWorker<Boolean, String> {
	
	private JTextArea[] textAreas;
	
	public SimulatorProcess(final JProgressBar progressBar, JTextArea... textAreas) {
		this.textAreas = textAreas;
        addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if("progress".equals(evt.getPropertyName())) {
                    progressBar.setValue((Integer) evt.getNewValue());
                }
            }
        });
    }
	
	@Override
	protected void process(List<String> strings) {
		for(String s : strings) {
			for(JTextArea ta : textAreas){
				ta.append(s + '\n');
			}
		}
	}
	
	@Override
	protected void done() {
		try {
			if(isCancelled()){
				publish("CANCEL : cancelled operation");
				return;
			}
			if(!get()){
				publish("ERROR : simulator error");
				return;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	public void display(String text){
		publish(text);
	}
	
	public void up(int progress){
		setProgress(progress);
	}
	
}
