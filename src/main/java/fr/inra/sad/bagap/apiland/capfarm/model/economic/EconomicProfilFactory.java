package fr.inra.sad.bagap.apiland.capfarm.model.economic;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import com.csvreader.CsvReader;
import com.csvreader.CsvReader.CatastrophicException;
import com.csvreader.CsvReader.FinalizedException;
import fr.inra.sad.bagap.apiland.capfarm.model.CoverUnit;

public class EconomicProfilFactory {

	public static EconomicProfil create(CoverUnit[] covers){
		int[] yields = {5000, 5500, 5200, 5100, 5200, 5200, 5100, 5000, 5500, 5200};		
		double[] prices = {155.5, 165.5, 145, 145.5, 140.5, 175.5, 145.5, 145, 145.5, 140.5};
		double[] charges = {150, 160, 240, 200, 350, 370, 240, 240, 200, 350};
		int[] bonus = {50, 60, 10, 20, 150, 100, 24, 60, 10, 20};
		int[] works = {10, 5, 5, 20, 5, 15, 7, 5, 15, 7};
		
		return new EconomicProfil(covers, yields, prices, charges, bonus, works);
	}
	
	public static EconomicProfil create(CoverUnit[] covers, String file){
		try {
			CsvReader cr = new CsvReader(file);
			cr.setDelimiter(';');
			cr.readHeaders();
			
			Map<String, Integer> myields = new HashMap<String, Integer>();
			Map<String, Double> mprices = new HashMap<String, Double>();
			Map<String, Double> mcharges = new HashMap<String, Double>();
			Map<String, Integer> mbonus = new HashMap<String, Integer>();
			Map<String, Integer> mworks = new HashMap<String, Integer>();
			
			while(cr.readRecord()){
				String c = cr.get("cover");
				myields.put(c, Integer.parseInt(cr.get("yield")));
				mprices.put(c, Double.parseDouble(cr.get("price")));
				mcharges.put(c, Double.parseDouble(cr.get("charge")));
				mbonus.put(c, Integer.parseInt(cr.get("bonus")));
				mworks.put(c, Integer.parseInt(cr.get("work")));
			}
			
			int[] yields = new int[covers.length];
			double[] prices = new double[covers.length];
			double[] charges = new double[covers.length];
			int[] bonus = new int[covers.length];
			int[] works = new int[covers.length];
			
			for(int i=0; i<covers.length; i++){
				yields[i] = myields.get(covers[i].getCode());
				prices[i] = mprices.get(covers[i].getCode());
				charges[i] = mcharges.get(covers[i].getCode());
				bonus[i] = mbonus.get(covers[i].getCode());
				works[i] = mworks.get(covers[i].getCode());
			}
			
			EconomicProfil ep = new EconomicProfil(covers, yields, prices, charges, bonus, works);
			
			cr.close();
			
			return ep;
		} catch (IOException | FinalizedException | CatastrophicException e) {
			e.printStackTrace();
		}
		
		throw new IllegalArgumentException();
	}
	
}
