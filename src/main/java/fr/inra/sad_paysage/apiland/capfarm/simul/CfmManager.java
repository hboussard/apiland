package fr.inra.sad_paysage.apiland.capfarm.simul;

import java.util.HashSet;
import java.util.Set;

import fr.inra.sad_paysage.apiland.capfarm.CAPFarm;
import fr.inra.sad_paysage.apiland.capfarm.model.territory.Territory;
import fr.inra.sad_paysage.apiland.core.time.Instant;
import fr.inra.sad_paysage.apiland.core.time.delay.YearDelay;
import fr.inra.sad_paysage.apiland.simul.OutputAnalysis;
import fr.inra.sad_paysage.apiland.simul.SimulationManager;

public class CfmManager extends SimulationManager {

	private static final long serialVersionUID = 1L;
	
	private Territory territory;
	
	private Set<OutputAnalysis> outputs;
	
	private CfmMode mode = CfmMode.IDLE;
	
	private CfmProcessMode processMode = CfmProcessMode.ACTIVATE;
	
	private String paramProcessMode;
	
	private String probaTimeFolder;
	
	private boolean check;

	public CfmManager(int s){
		super(s);
		setDelay(new YearDelay(1));
		outputs = new HashSet<OutputAnalysis>();
		check = false;
		probaTimeFolder = CfmManager.class.getResource("proba_times/").toString();
	}
	
	@Override
	public void setPath(String path){
		super.setPath(path);
		expFolder = path;
	}
	
	@Override
	public void setStart(Instant t){
		CAPFarm.t = t;
		super.setStart(t);
	}

	public void setMode(CfmMode mode){
		this.mode = mode;
	}
	
	public void setProcessMode(CfmProcessMode mode, String paramProcessMode){
		this.processMode = mode;
		this.paramProcessMode = paramProcessMode;
	}
	
	public String paramProcessMode(){
		return paramProcessMode;
	}

	public void setTerritory(Territory territory){
		this.territory = territory;
	}
	
	public void addOutput(OutputAnalysis output) {
		this.outputs.add(output);
	}

	public Territory territory(){
		return territory;
	}

	public Set<OutputAnalysis> outputs(){
		return outputs;
	}
	
	public CfmMode mode(){
		return mode;
	}
	
	public CfmProcessMode processMode(){
		return processMode;
	}
	
	public String probaTimeFolder(){
		return probaTimeFolder;
	}
	
	public boolean checkConstraints(){
		return check;
	}
	
	public void checkConstraints(boolean check) {
		this.check = check;
	}
}
