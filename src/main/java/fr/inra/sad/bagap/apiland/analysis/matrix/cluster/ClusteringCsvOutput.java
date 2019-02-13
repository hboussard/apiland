package fr.inra.sad.bagap.apiland.analysis.matrix.cluster;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import com.csvreader.CsvWriter;
import com.csvreader.CsvWriter.FinalizedException;

import fr.inra.sad.bagap.apiland.analysis.Analysis;
import fr.inra.sad.bagap.apiland.analysis.AnalysisObserver;
import fr.inra.sad.bagap.apiland.analysis.AnalysisState;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.PixelComposite;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.RasterComposite;

public class ClusteringCsvOutput implements AnalysisObserver{

	private String csv;
	
	public ClusteringCsvOutput(String csv){
		this.csv = csv;
	}
	
	@SuppressWarnings("incomplete-switch")
	@Override
	public void notify(Analysis ma, AnalysisState state) {
		switch(state){
			case DONE : 
				Map<Integer, Set<PixelComposite>> map = new TreeMap<Integer, Set<PixelComposite>>();
				for(Raster rr : ((RasterComposite) ma.getResult()).getRasters()){
					int v = ((PixelComposite) rr).getValue();
					if(!map.containsKey(v)){
						map.put(v, new HashSet<PixelComposite>());
					}
					map.get(v).add((PixelComposite) rr);
				}
				
				CsvWriter cw = new CsvWriter(csv);
				cw.setDelimiter(';');

				try {
					cw.write("id");
					//cw.write("type");
					cw.write("count");
					cw.write("area");
					cw.endRecord();
					for(Entry<Integer, Set<PixelComposite>> e : map.entrySet()){
						cw.write(e.getKey()+"");
						//cw.write((int) pc.getUserData()+"");
						int size = 0;
						for(PixelComposite pc : e.getValue()){
							size += pc.size();
						}
						cw.write(size+"");
						cw.write((size*Math.pow(Raster.getCellSize(), 2))+"");
						cw.endRecord();
					}
					cw.close();
				} catch (FinalizedException | IOException e) {
					e.printStackTrace();
				}
				
				break;
		}
	}

	@Override
	public void updateProgression(Analysis a, int total) {
		// do nohting
	}
	

}
