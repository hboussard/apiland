package fr.inra.sad.bagap.apiland.capfarm.simul.output;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.csvreader.CsvWriter;
import fr.inra.sad.bagap.apiland.capfarm.simul.CoverLocationModel;
import fr.inra.sad.bagap.apiland.capfarm.simul.GlobalCoverLocationModel;
import fr.inra.sad.bagap.apiland.core.time.Instant;
import fr.inra.sad.bagap.apiland.simul.OutputAnalysis;
import fr.inra.sad.bagap.apiland.simul.Scenario;
import fr.inra.sad.bagap.apiland.simul.Simulation;

public class ProfitOutput extends OutputAnalysis {
	
	private Map<String, CsvWriter> cws;
	
	@Override
	public void init(Scenario scenario){
		cws = new HashMap<String, CsvWriter>();
		try {
			for(CoverLocationModel model : (GlobalCoverLocationModel) scenario.model().get("agriculture")){
				CsvWriter cw = new CsvWriter(scenario.folder()+"profit"+model.getCoverAllocator().getCode()+".csv");
				cw.setDelimiter(';');
				cw.write("simulation");
				cw.write("farm");
				cw.write("year");
				cw.write("profit");
				cw.endRecord();
				cws.put(model.getCoverAllocator().getCode(), cw);
			}
			
		} catch (com.csvreader.CsvWriter.FinalizedException|IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void close(Scenario scenario){
		for(CsvWriter cw : cws.values()){
			cw.close();
		}
	}
	
	@Override
	public void close(Simulation simulation){
		if(!simulation.isCancelled()){
			try {
				for(CoverLocationModel model : (GlobalCoverLocationModel) simulation.model().get("agriculture")){
					CsvWriter cw = cws.get(model.getCoverAllocator().getCode());
					for(int y=simulation.manager().start().year(); y<=simulation.manager().end().year(); y++){
						cw.write(""+simulation.number());
						cw.write(""+model.getCoverAllocator().getCode());
						cw.write(y+"");
						cw.write(model.getCoverAllocator().getTerritory().getAttribute("profit").getValue(new Instant(simulation.manager().start().dayOfYear(), simulation.manager().start().month(), y))+"");
						cw.endRecord();
					}
				}
			} catch (com.csvreader.CsvWriter.FinalizedException|IOException e) {
				e.printStackTrace();
			}
		}
	}	
	
}