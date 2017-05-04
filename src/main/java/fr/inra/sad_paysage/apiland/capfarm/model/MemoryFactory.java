package fr.inra.sad_paysage.apiland.capfarm.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.csvreader.CsvReader;
import com.csvreader.CsvReader.CatastrophicException;
import com.csvreader.CsvReader.FinalizedException;

import fr.inra.sad_paysage.apiland.capfarm.csp.CoverAllocator;
import fr.inra.sad_paysage.apiland.capfarm.simul.CoverLocationModel;
import fr.inra.sad_paysage.apiland.capfarm.simul.MemoryCoverLocationModel;
import fr.inra.sad_paysage.apiland.core.composition.AttributeType;
import fr.inra.sad_paysage.apiland.core.composition.DynamicAttributeType;
import fr.inra.sad_paysage.apiland.core.element.type.DynamicElementTypeFactory;
import fr.inra.sad_paysage.apiland.core.time.Instant;
import fr.inra.sad_paysage.apiland.core.time.Interval;

public class MemoryFactory {

	public static void init(CoverLocationModel model, Instant t, String farmFolder){
		
		CoverAllocator farm = model.getCoverAllocator();
		if(farm.isMemory()){
			int memory = farm.getMemory();
			try {
				String memoryFile = farmFolder+"/"+farm.getCode()+"/"+farm.getFarmingSystem()+"/"+farm.getCode()+"_memory.csv";
				//System.out.println(memoryFile);
				if(memory == 0){
					CsvReader cr = new CsvReader(memoryFile);
					cr.setDelimiter(';');
					cr.readHeaders();
					List<Integer> memories = new ArrayList<Integer>(); 
					while(cr.readRecord()){
						memories.add(Integer.parseInt(cr.get("memory")));
					}
					cr.close();
					//System.out.println(farm.getCode());
					memory = memories.get(new Double(Math.random() * memories.size()).intValue());
				}
				
				AttributeType type= DynamicElementTypeFactory.createAttributeType("memory", Interval.class, CoverUnit.class);
				
				CsvReader cr = new CsvReader(memoryFile);
				cr.setDelimiter(';');
				cr.readHeaders();
				while(cr.readRecord()){
					if(Integer.parseInt(cr.get("memory")) == memory){
						initMemoryFile((MemoryCoverLocationModel) model, t, (DynamicAttributeType) type,
								farmFolder+"/"+farm.getCode()+"/"+farm.getFarmingSystem()+"/"+cr.get("file"));
						break;
					}
				}
				cr.close();
				
			} catch (IOException | FinalizedException | CatastrophicException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static void initMemoryFile(MemoryCoverLocationModel model, Instant t, DynamicAttributeType type, String memoryFile) {
		try {
			CsvReader cr = new CsvReader(memoryFile);
			cr.setDelimiter(';');
			cr.readHeaders();
			String seq;
			String[] sequence;
			Instant year;
			String[] infos;
			int nb;
			String parcel;
			while(cr.readRecord()){
				parcel = cr.get("parcel");
				model.initParcel(parcel, type);
				sequence = cr.get("seq_cover").replaceAll(" ", "").split("-");
				year = t;
				for(String cover : sequence){
					nb = 1;
					if(cover.contains("(")){
						infos = cover.replace(")", "").split("\\(");
						cover = infos[0];
						nb = Integer.parseInt(infos[1]);
					}
					model.setCover(parcel, (CoverUnit) Cover.get(cover), year);
					year = Instant.get(year.dayOfMonth(), year.month(), year.year()+nb);
				}
			}
			cr.close();
		} catch (IOException | FinalizedException | CatastrophicException e) {
			e.printStackTrace();
		}
	}
	
}
