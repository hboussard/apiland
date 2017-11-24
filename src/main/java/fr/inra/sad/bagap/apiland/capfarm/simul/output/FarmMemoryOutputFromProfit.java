package fr.inra.sad.bagap.apiland.capfarm.simul.output;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.csvreader.CsvWriter;
import com.csvreader.CsvWriter.FinalizedException;

import fr.inra.sad.bagap.apiland.capfarm.model.Farm;
import fr.inra.sad.bagap.apiland.capfarm.model.territory.Parcel;
import fr.inra.sad.bagap.apiland.capfarm.simul.CoverLocationModel;
import fr.inra.sad.bagap.apiland.capfarm.simul.GlobalCoverLocationModel;
import fr.inra.sad.bagap.apiland.core.time.Instant;
import fr.inra.sad.bagap.apiland.simul.OutputAnalysis;
import fr.inra.sad.bagap.apiland.simul.Scenario;
import fr.inra.sad.bagap.apiland.simul.Simulation;

public class FarmMemoryOutputFromProfit extends OutputAnalysis {
	
	private Map<String, CsvWriter> cws;
	
	//private Map<String, Integer> numbers;
	
	private String path;
	
	public FarmMemoryOutputFromProfit(){
		// do nothing
	}
	
	public FarmMemoryOutputFromProfit(String path){
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
		new File(path).mkdir();
		cws = new HashMap<String, CsvWriter>();
		try {
			CsvWriter cw;
			for(CoverLocationModel model : (GlobalCoverLocationModel) scenario.model().get("agriculture")){
				
				new File(path+model.getCoverAllocator().getCode()+"/"+model.getCoverAllocator().getFarmingSystem()+"/memory/").mkdirs();
				cw = new CsvWriter(path+model.getCoverAllocator().getCode()+"/"+model.getCoverAllocator().getFarmingSystem()+"/"+model.getCoverAllocator().getCode()+"_memory.csv");
				cw.setDelimiter(';');
				cw.write("memory");
				cw.write("file");
				cw.write("profit");
				cw.endRecord();
				cws.put(model.getCoverAllocator().getCode(), cw);
			}
		} catch (com.csvreader.CsvWriter.FinalizedException|IOException e) {
			e.printStackTrace();
		}
		//numbers = new HashMap<String, Integer>();
		//for(CoverLocationModel model : (GlobalCoverLocationModel) scenario.model().get("agriculture")){
		//	numbers.put(model.getCoverAllocator().getCode(), 1);
		//}
	}
	
	@Override
	public void close(Scenario scenario){
		for(CsvWriter cw : cws.values()){
			cw.close();
		}
		cws = null;
		//numbers = null;
	}
	
	@Override
	public void close(Simulation simulation){
		if(!simulation.isCancelled()){
			try {
				CsvWriter cw;
				for(CoverLocationModel model : (GlobalCoverLocationModel) simulation.model().get("agriculture")){
					cw = cws.get(model.getCoverAllocator().getCode());
					int profit = 0;
					for(int y=simulation.manager().start().year(); y<=simulation.manager().end().year(); y++){
						profit += (Integer) model.getCoverAllocator().getTerritory().getAttribute("profit").getValue(new Instant(simulation.manager().start().dayOfYear(), simulation.manager().start().month(), y));
					}
					cw.write(simulation.number()+"");
					//cw.write(numbers.get(model.getCoverAllocator().getCode())+"");
					//numbers.put(model.getCoverAllocator().getCode(), numbers.get(model.getCoverAllocator().getCode())+1);
					cw.write("memory/"+model.getCoverAllocator().getCode()+"_memory_"+simulation.number()+".csv");
					cw.write(profit+"");
					cw.endRecord();
					
					cw = new CsvWriter(path+model.getCoverAllocator().getCode()+"/"+model.getCoverAllocator().getFarmingSystem()+"/memory/"+model.getCoverAllocator().getCode()+"_memory_"+simulation.number()+".csv");
					cw.setDelimiter(';');
					cw.write("parcel");
					cw.write("seq_cover");
					//cw.write("historic");
					cw.endRecord();
						
					for(Parcel p : model.getCoverAllocator().parcels()){
						cw.write(p.getId());
						cw.write((String) p.getAttribute("strict_seq").getValue(simulation.manager().end()));
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
