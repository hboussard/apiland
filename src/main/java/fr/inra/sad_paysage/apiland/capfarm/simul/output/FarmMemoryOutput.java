package fr.inra.sad_paysage.apiland.capfarm.simul.output;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.csvreader.CsvWriter;
import com.csvreader.CsvWriter.FinalizedException;

import fr.inra.sad_paysage.apiland.capfarm.model.territory.Parcel;
import fr.inra.sad_paysage.apiland.capfarm.simul.CoverLocationModel;
import fr.inra.sad_paysage.apiland.capfarm.simul.GlobalCoverLocationModel;
import fr.inra.sad_paysage.apiland.simul.OutputAnalysis;
import fr.inra.sad_paysage.apiland.simul.Scenario;
import fr.inra.sad_paysage.apiland.simul.Simulation;

public class FarmMemoryOutput extends OutputAnalysis {
	
	private Map<String, CsvWriter> cws;
	
	private boolean isInit;
	
	private String path;
	
	public FarmMemoryOutput(){
		// do nothing
	}
	
	public FarmMemoryOutput(String path){
		if(path == null){
			// do nothing
		}else{
			this.path = path;
		}
	}
	
	@Override
	public void init(Scenario scenario){
		if(path == null){
			path = scenario.folder()+"memory/";
		}
		cws = new HashMap<String, CsvWriter>();
		new File(path).mkdir();
		isInit = false;
	}
	
	public void firstInit(Simulation simulation){
		if(!isInit){
			try {
				CsvWriter cw;
				for(CoverLocationModel model : (GlobalCoverLocationModel) simulation.model().get("agriculture")){
					new File(path+model.getCoverAllocator().getCode()+"/"+model.getCoverAllocator().getFarmingSystem()+"/memory/").mkdirs();
					cw = new CsvWriter(path+model.getCoverAllocator().getCode()+"/"+model.getCoverAllocator().getFarmingSystem()+"/"+model.getCoverAllocator().getCode()+"_memory.csv");
					cw.setDelimiter(';');
					cw.write("memory");
					cw.write("file");
					cw.endRecord();
					cws.put(model.getCoverAllocator().getCode(), cw);
				}
			} catch (com.csvreader.CsvWriter.FinalizedException|IOException e) {
				e.printStackTrace();
			}
			isInit = true;
		}
	}
	
	@Override
	public void close(Scenario scenario){
		for(CsvWriter cw : cws.values()){
			cw.close();
		}
		cws = null;
	}
	
	@Override
	public void close(Simulation simulation){
		if(!simulation.isCancelled()){
			firstInit(simulation);
			try {
				//new File(simulation.scenario().simulator().folder()+"memory/").mkdir();
				CsvWriter cw;
				for(CoverLocationModel model : (GlobalCoverLocationModel) simulation.model().get("agriculture")){
					
					cw = cws.get(model.getCoverAllocator().getCode());
					cw.write(simulation.number()+"");
					cw.write("memory/"+model.getCoverAllocator().getCode()+"_memory_"+simulation.number()+".csv");
					cw.endRecord();
					
					cw = new CsvWriter(path+model.getCoverAllocator().getCode()+"/"+model.getCoverAllocator().getFarmingSystem()+"/memory/"+model.getCoverAllocator().getCode()+"_memory_"+simulation.number()+".csv");
					cw.setDelimiter(';');
					cw.write("parcel");
					cw.write("seq_cover");
					cw.endRecord();
					
					for(Parcel p : model.getCoverAllocator().parcels()){
						cw.write(p.getId());
						cw.write((String) p.getAttribute("seq_cover").getValue(simulation.manager().end()));
						cw.endRecord();
					}
					
					cw.close();
				}
				
			} catch (FinalizedException | IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
