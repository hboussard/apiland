package fr.inra.sad_paysage.apiland.capfarm.model;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.csvreader.CsvReader;
import com.csvreader.CsvReader.CatastrophicException;
import com.csvreader.CsvReader.FinalizedException;

import fr.inra.sad_paysage.apiland.capfarm.model.territory.Parcel;
import fr.inra.sad_paysage.apiland.core.time.Instant;

public class FixedFactory {

	public static void init(Farm farm, Instant start){
		if(farm.hasSolution()){
			try {
				
				CsvReader cr = new CsvReader(farm.getFixFile());
				cr.setDelimiter(';');
				cr.readHeaders();
				
				Map<Parcel, CoverUnit> fixed = new HashMap<Parcel, CoverUnit>();
				while(cr.readRecord()){
					String parcel = cr.get("parcel");
					String cover = cr.get("cover");
					fixed.put(farm.parcel(parcel), (CoverUnit) Cover.get(cover));
				}
				
				farm.setSolution(fixed);
				
				cr.close();
			} catch (IOException | FinalizedException | CatastrophicException e) {
				e.printStackTrace();
			}
		}
	}
}
