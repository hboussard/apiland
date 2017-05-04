package fr.inra.sad_paysage.apiland.capfarm.model;

import java.io.File;
import java.io.IOException;

import com.csvreader.CsvReader;
import com.csvreader.CsvReader.CatastrophicException;
import com.csvreader.CsvReader.FinalizedException;
import fr.inra.sad_paysage.apiland.core.time.Instant;

public class HistoricFactory {

	public static void init(Farm farm, Instant start){
		if(farm.hasHistoric()){
			try {
				
				File folder = new File(farm.getHistoric());
				String historicFile = null;
				if(folder.isFile()){
					historicFile = folder.getAbsolutePath();
					//System.out.println(historicFile);
				}else{
					int rH = new Double(Math.random() * folder.list().length).intValue();
					int i=0;
					for(String f : folder.list()){
						if(i == rH){
							historicFile = folder+"/"+f;
							//System.out.println("historic : "+f);
							break;
						}
						i++;
					}
				}
				
				// détermination du nombre d'années sur la première ligne
				CsvReader cr = new CsvReader(historicFile);
				cr.setDelimiter(';');
				cr.readHeaders();
				String[] sequence;
				String[] infos;
				int nb;
				String parcel;
				cr.readRecord();
				parcel = cr.get("parcel");
				sequence = cr.get("historic").replaceAll(" ", "").split("-");
				int nbYear = 0;
				for(String cover : sequence){
					nb = 1;
					if(cover.contains("(")){
						infos = cover.replace(")", "").split("\\(");
						cover = infos[0];
						nb = Integer.parseInt(infos[1]);
					}
					nbYear += nb;
				}
				cr.close();
				// fin détermination du nombre d'années
				
				cr = new CsvReader(historicFile);
				cr.setDelimiter(';');
				cr.readHeaders();
				Instant t;
				while(cr.readRecord()){
					parcel = cr.get("parcel");
					sequence = cr.get("historic").replaceAll(" ", "").split("-");
					t = Instant.get(start.dayOfMonth(), start.month(), start.year() - nbYear);
					for(String cover : sequence){
						nb = 1;
						if(cover.contains("(")){
							infos = cover.replace(")", "").split("\\(");
							cover = infos[0];
							nb = Integer.parseInt(infos[1]);
						}
						farm.parcel(parcel).getAttribute("seq_cover").setValue(t, (CoverUnit) Cover.get(cover));
						t = Instant.get(t.dayOfMonth(), t.month(), t.year()+nb);
						
					}
				}
				cr.close();
			} catch (IOException | FinalizedException | CatastrophicException e) {
				e.printStackTrace();
			}
		}
	}
	
}
