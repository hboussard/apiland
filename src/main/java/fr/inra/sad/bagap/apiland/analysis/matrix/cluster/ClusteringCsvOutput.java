package fr.inra.sad.bagap.apiland.analysis.matrix.cluster;

import java.io.IOException;
import java.util.Map;
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
		ClusteringAnalysis ca = (ClusteringAnalysis) ma;
		switch(state){
			case DONE : 
				Map<Integer, PixelComposite> map = new TreeMap<Integer, PixelComposite>();
				for(Raster rr : ((RasterComposite) ca.getResult()).getRasters()){
					map.put(((PixelComposite) rr).getValue(), ((PixelComposite) rr));
				}

				CsvWriter cw = new CsvWriter(csv);
				cw.setDelimiter(';');

				try {
					cw.write("id");
					cw.write("type");
					cw.write("count");
					cw.write("area");
					cw.endRecord();
					for(PixelComposite pc : map.values()){
						cw.write(pc.getValue()+"");
						cw.write((int) pc.getUserData()+"");
						cw.write(pc.size()+"");
						cw.write(pc.getArea()+"");
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
