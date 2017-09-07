package fr.inra.sad_paysage.apiland.analysis.matrix.calculation.cluster;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.csvreader.CsvWriter;
import com.csvreader.CsvWriter.FinalizedException;

import fr.inra.sad_paysage.apiland.analysis.Analysis;
import fr.inra.sad_paysage.apiland.analysis.AnalysisObserver;
import fr.inra.sad_paysage.apiland.analysis.AnalysisState;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.PixelComposite;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.Raster;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.RasterComposite;

public class ClusteringWithDistanceCsvOutput implements AnalysisObserver{

	private String csv;
	
	private Set<Integer> values;
	
	public ClusteringWithDistanceCsvOutput(String csv, Set<Integer> values){
		this.csv = csv;
		this.values = values;
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
					for(int v : values){
						cw.write("idmin_"+v);
						cw.write("dmin_"+v);
					}
					cw.endRecord();
					Set<Pixel> bounds1, bounds2;
					for(PixelComposite pc : map.values()){
						cw.write(pc.getValue()+"");
						cw.write((int) pc.getUserData()+"");
						cw.write(pc.size()+"");
						cw.write(pc.getArea()+"");
						System.out.println("calcul de distance à partir de "+pc.getValue()+"("+pc.getUserData()+")");
						bounds1 = pc.getBounds();
						for(int v : values){
							double min = Double.MAX_VALUE;
							int cl = -1;
							if(v != (int) pc.getUserData()){
								for(PixelComposite pc2 : map.values()){
									if(((int) pc2.getUserData()) == v){
										bounds2 = pc2.getBounds();
										//System.out.println("calcul de distance entre "+pc.getValue()+"("+pc.getUserData()+") et "+pc2.getValue()+"("+pc2.getUserData()+")");
										//double d = Raster.distance(pc, pc2);
										double d = Raster.distance(bounds1, bounds2);
										//System.out.println("--> distance = "+d);
										if(d < min){
											min = d;
											cl = pc2.getValue();
										}
										//min = Math.min(min, Raster.distance(pc, pc2));
									}
								}
							}
							if(min == Double.MAX_VALUE){
								min = -1;
							}
							cw.write(cl+"");
							cw.write(min+"");
						}
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
	
	/*
	if(values.size() > 1){
		if(!sameType){
			if(sameMap){
				
				ClusteringAnalysis ca = null;
				
				switch(typeCluster){
				case 1 : ca = new ClusteringRookAnalysis(m, values); break;
				case 2 : ca = new ClusteringQueenAnalysis(m, values); break;
				case 3 : ca = new ClusteringDistanceAnalysis(m, distance, values); break;
				}
				
				ca.addObserver(new AnalysisObserver(){
					
					@Override
					public void notifyFromAnalysis(Analysis ma, AnalysisState state) {
						ClusteringAnalysis ca = (ClusteringAnalysis) ma;
						switch(state){
							case DONE : 
								Map<Integer, PixelComposite> map = new TreeMap<Integer, PixelComposite>();
								for(Raster rr : ((RasterComposite) ca.getResult()).getRasters()){
									map.put(((PixelComposite) rr).getValue(), ((PixelComposite) rr));
								}
				
								CsvWriter cw = new CsvWriter(asciiOutput+"/"+name+"_"+suf+".csv");
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
					public void updateProgression(int total) {}
					
				});
				
				Raster r = (Raster) ca.allRun();
				
				Matrix m2 = RasterManager.exportMatrix(r, m);
				
				Pixel2PixelMatrixCalculation ppt = new Pixel2PixelMatrixCalculation(m, m2){
					@Override
					protected double treatPixel(Pixel p) {
						double v = m2.get(p);
						if(v != Raster.getNoDataValue()){
							return v;
						}
						if(m.get(p) != Raster.getNoDataValue()){
							return 0;
						}
						return Raster.getNoDataValue();
					}
				};
				
				MatrixManager.exportAsciiGrid(ppt.allRun(), asciiOutput+"/"+name+"_"+suf+".asc");
				
				
			}else{
				
				for(Integer v : values){
					
					Set<Integer> vv = new HashSet<Integer>();
					vv.add(v);
					
					ClusteringAnalysis ca = null;
					
					switch(typeCluster){
					case 1 : ca = new ClusteringRookAnalysis(m, vv); break;
					case 2 : ca = new ClusteringQueenAnalysis(m, vv); break;
					case 3 : ca = new ClusteringDistanceAnalysis(m, distance, vv); break;
					}
					
					ca.addObserver(new AnalysisObserver(){
						
						@Override
						public void notifyFromAnalysis(Analysis ma, AnalysisState state) {
							ClusteringAnalysis ca = (ClusteringAnalysis) ma;
							switch(state){
								case DONE : 
									Map<Integer, PixelComposite> map = new TreeMap<Integer, PixelComposite>();
									for(Raster rr : ((RasterComposite) ca.getResult()).getRasters()){
										map.put(((PixelComposite) rr).getValue(), ((PixelComposite) rr));
									}
					
									CsvWriter cw = new CsvWriter(asciiOutput+"/"+name+"_"+suf+".csv");
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
						public void updateProgression(int total) {}
						
					});
					
					Raster r = (Raster) ca.allRun();
					
					Matrix m2 = RasterManager.exportMatrix(r, m);
					
					Pixel2PixelMatrixCalculation ppt = new Pixel2PixelMatrixCalculation(m, m2){
						@Override
						protected double treatPixel(Pixel p) {
							double v = m2.get(p);
							if(v != Raster.getNoDataValue()){
								return v;
							}
							if(m.get(p) != Raster.getNoDataValue()){
								return 0;
							}
							return Raster.getNoDataValue();
						}
					};
					
					MatrixManager.exportAsciiGrid(ppt.allRun(), asciiOutput+"/"+name+"_"+v+"_"+suf+".asc");
				}
				
			}
		}else{
			
			ClusteringAnalysis ca = null;
			
			switch(typeCluster){
			case 1 : ca = new ClusteringRookAnalysis(m, values); break;
			case 2 : ca = new ClusteringQueenAnalysis(m, values); break;
			case 3 : ca = new ClusteringDistanceAnalysis(m, distance, values); break;
			}
			
			ca.addObserver(new AnalysisObserver(){
				
				@Override
				public void notifyFromAnalysis(Analysis ma, AnalysisState state) {
					ClusteringAnalysis ca = (ClusteringAnalysis) ma;
					switch(state){
						case DONE : 
							Map<Integer, PixelComposite> map = new TreeMap<Integer, PixelComposite>();
							for(Raster rr : ((RasterComposite) ca.getResult()).getRasters()){
								map.put(((PixelComposite) rr).getValue(), ((PixelComposite) rr));
							}
			
							CsvWriter cw = new CsvWriter(asciiOutput+"/"+name+"_"+suf+".csv");
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
				public void updateProgression(int total) {}
				
			});
			
			Raster r = (Raster) ca.allRun();
			
			Matrix m2 = RasterManager.exportMatrix(r, m);
			
			Pixel2PixelMatrixCalculation ppt = new Pixel2PixelMatrixCalculation(m, m2){
				@Override
				protected double treatPixel(Pixel p) {
					double v = m2.get(p);
					if(v != Raster.getNoDataValue()){
						return v;
					}
					if(m.get(p) != Raster.getNoDataValue()){
						return 0;
					}
					return Raster.getNoDataValue();
				}
			};
			
			MatrixManager.exportAsciiGrid(ppt.allRun(), asciiOutput+"/"+name+"_"+suf+".asc");
			
		}
	}else{
		
	}
	*/
	
	//Matrix friction = ArrayMatrixFactory.get().createWithAsciiGrid("c://hugues/temp/chloe/friction/friction.asc", true);
	//Friction friction = new Friction("c://hugues/temp/chloe/friction/friction.txt");
	

}
