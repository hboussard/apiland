package fr.inra.sad.bagap.apiland.capfarm.model.economic;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.csvreader.CsvReader;
import com.csvreader.CsvReader.CatastrophicException;
import com.csvreader.CsvReader.FinalizedException;

import fr.inra.sad.bagap.apiland.capfarm.model.CoverUnit;

public class ManagmentProfilFactory {

	public static ManagmentProfil create(CoverUnit[] covers){
		int[] works = {10, 5, 5, 20, 5, 15, 7, 5, 15, 7};
		
		return new MaeliaManagmentProfil(covers, works);
	}
	
	public static ManagmentProfil create(CoverUnit[] covers, String file){
		try {
			CsvReader cr = new CsvReader(file);
			cr.setDelimiter(';');
			cr.readHeaders();
			
			Map<String, Integer> mworks = new HashMap<String, Integer>();
			
			while(cr.readRecord()){
				String c = cr.get("cover");
				mworks.put(c, Integer.parseInt(cr.get("work")));
			}
			
			int[] works = new int[covers.length];
			
			for(int i=0; i<covers.length; i++){
				works[i] = mworks.get(covers[i].getCode());
			}
			
			ManagmentProfil mp = new MaeliaManagmentProfil(covers, works);
			
			cr.close();
			
			return mp;
		} catch (IOException | FinalizedException | CatastrophicException e) {
			e.printStackTrace();
		}
		
		throw new IllegalArgumentException();
	}
	
	
}
