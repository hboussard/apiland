package fr.inra.sad.bagap.apiland.capfarm.simul;

import java.io.File;
import java.io.IOException;

import com.csvreader.CsvReader;
import com.csvreader.CsvReader.CatastrophicException;
import com.csvreader.CsvReader.FinalizedException;

import fr.inra.sad.bagap.apiland.capfarm.csp.ProbaTimeManager;
import fr.inra.sad.bagap.apiland.simul.OutputAnalysis;
import fr.inra.sad.bagap.apiland.simul.Simulator;

public abstract class CfmSimulator extends Simulator {

	private static final long serialVersionUID = 1L;

	public CfmSimulator(CfmManager manager){
		super(manager, new CfmFactory());
	}

	@Override
	public CfmManager manager(){
		return (CfmManager) super.manager();
	}
	
	public void allRun() {
		init(1);
		run();
		close();
	}
	
	@Override
	protected void initModel(){
		initProbaTimes();
		initTerritory();
		initFarms();
		initOutput();
	}

	private void initProbaTimes() {
		try {
			File folder = new File(manager().probaTimeFolder().replace("file:/", ""));
		
			ProbaTimeManager.initProbaTimes();
			String name;
			int time;
			CsvReader cr;
			String[] probas;
			
			for(File duration : folder.listFiles()){
				name = duration.getName().replace(".txt", "");
				ProbaTimeManager.addProbaType(name);
				cr = new CsvReader(duration.toString());
				cr.setDelimiter(';');
				cr.readHeaders();
				while(cr.readRecord()){
					time = Integer.parseInt(cr.get("time"));
					ProbaTimeManager.addProbaTime(name, time);
					probas = cr.get("probas").split("\\|");
					ProbaTimeManager.addProbaTimes(name, time, probas);
				}
				cr.close();
			}
		} catch (IOException | FinalizedException | CatastrophicException e) {
			e.printStackTrace();
		}
	}
	
	protected abstract void initFarms();

	private void initTerritory(){
		map().put("territory", manager().territory());
	}
	
	private void initOutput() {
		for(OutputAnalysis oa : manager().outputs()){
			addOutput(oa);
		}
	}

	
	/*
	private void initRasterization() {

		String cercle2km = "C:/Hugues/projets/agriconnect/restitution/data/sig/cercle_ouvert_2km.shp";
		DynamicLayer<?> layer = DynamicLayerFactory.initWithShape(cercle2km, manager().start());
	
		// rasterisation de la couche "territoire"
		OperationBuilder builder = new OperationBuilder(new OpRasterizationType());
		builder.setParameter("representation", "the_geom");
		builder.setParameter("cellsize", 10);
		int delta = 0;
		builder.setParameter("minX", layer.minX()-delta);
		builder.setParameter("maxX", layer.maxX()+delta);
		builder.setParameter("minY", layer.minY()-delta);
		builder.setParameter("maxY", layer.maxY()+delta);
		OpRasterization operation = builder.build();
		
		// rasterisation effectuee 1 seule fois
		operation.make(manager().start(), map().get("territory"));
	}
	*/
}

