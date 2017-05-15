package fr.inra.sad_paysage.apiland.capfarm.model;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import com.csvreader.CsvReader;
import com.csvreader.CsvReader.CatastrophicException;
import com.csvreader.CsvReader.FinalizedException;

import fr.inra.sad_paysage.apiland.capfarm.model.constraint.GenericConstraintBuilder;
import fr.inra.sad_paysage.apiland.capfarm.model.constraint.GenericCoverAllocationConstraint;

import com.csvreader.CsvWriter;

public class FarmingSystemFactory {
	
	public static void importSystem(GenericFarmingSystem system, String constraints){
		GenericConstraintBuilder gcb = new GenericConstraintBuilder(system);
		try {
			// initialisation des contraintes
			CsvReader cr = new CsvReader(constraints);
			cr.setDelimiter(';');
			cr.readHeaders();
			String code;
			while(cr.readRecord()){
				code = cr.get("code");
				if(!code.startsWith("#")){
					gcb.setCode(code); 		
					gcb.setCover(cr.get("cover").replace("[", "").replace("]", "").replace(" ", "").split(",")); 
					gcb.setLocation(cr.get("location"));
					gcb.setType(cr.get("type"));
					gcb.setMode(cr.get("mode"));
					gcb.setDomain(cr.get("domain"));
					gcb.setParams(cr.get("params"));
					gcb.build();
				}
			}
			cr.close();	
		} catch (IOException e) {
			e.printStackTrace();
		} catch (FinalizedException e) {
			e.printStackTrace();
		} catch (CatastrophicException e) {
			e.printStackTrace();
		}
	}
	
	public static void exportSystem(FarmingSystem system, String constraints) {
		exportSystem(system.getGenericFarmingSystem(), constraints);
	}
	
	public static void exportSystem(GenericFarmingSystem system, String constraints) {
		try {
			// initialisation des contraintes
			CsvWriter cw = new CsvWriter(constraints);
			cw.setDelimiter(';');
			
			cw.write("code");
			cw.write("cover");
			cw.write("location");
			cw.write("type");
			cw.write("mode");
			cw.write("domain");
			cw.write("params");
			cw.endRecord();
			cw.endRecord();
			
			Collection<GenericCoverAllocationConstraint> cons;
			
			cons = system.getConstraintsMultipleCovers();
			if(cons.size() > 0){
				cw.write("# constraints on multiple covers");
				cw.endRecord();
				for(GenericCoverAllocationConstraint gc : cons){
					cw.write(gc.getCode());
					
					if(gc.getCovers().length == 0){
						cw.write("");
					}else if(gc.getCovers().length == 1){
						cw.write(gc.getCovers()[0]);
					}else{
						cw.write(Arrays.toString(gc.getCovers()));
					}
					
					cw.write(gc.getLocation());
					cw.write(gc.getType().toString());
					cw.write(gc.getMode().toString());
					cw.write(gc.getDomain());
					
					if(gc.getParams() == null){
						cw.write("");
					}else if(gc.getParams().length == 1){
						cw.write(gc.getParams()[0]);
					}else{
						cw.write(Arrays.toString(gc.getParams()));
					}
					
					cw.endRecord();
				}
			}
			cw.endRecord();
			for(Cover c : system.getCovers()){
				cons = system.getConstraints(c);
				if(cons.size() > 0){
					cw.write("# constraints on '"+c.getName()+"'");
					cw.endRecord();
					for(GenericCoverAllocationConstraint gc : cons){
						cw.write(gc.getCode());
						
						if(gc.getCovers().length == 0){
							cw.write("");
						}else if(gc.getCovers().length == 1){
							cw.write(gc.getCovers()[0]);
						}else{
							cw.write(Arrays.toString(gc.getCovers()));
						}
						
						cw.write(gc.getLocation());
						cw.write(gc.getType().toString());
						cw.write(gc.getMode().toString());
						cw.write(gc.getDomain());
						
						if(gc.getParams() == null){
							cw.write("");
						}else if(gc.getParams().length == 1){
							cw.write(gc.getParams()[0]);
						}else{
							cw.write(Arrays.toString(gc.getParams()));
						}
						
						cw.endRecord();
					}
					cw.endRecord();
				}
			}
			
			cw.close();	
		} catch (IOException e) {
			e.printStackTrace();
		} catch (com.csvreader.CsvWriter.FinalizedException e) {
			e.printStackTrace();
		}
	}

}