package fr.inra.sad.bagap.apiland.analysis.matrix.cluster;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.csvreader.CsvWriter;
import com.csvreader.CsvWriter.FinalizedException;

import fr.inra.sad.bagap.apiland.analysis.Count;
import fr.inra.sad.bagap.apiland.analysis.matrix.MatrixAnalysis;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.PixelComposite;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.RasterComposite;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;

public abstract class NewClusteringAnalysis extends MatrixAnalysis {

	private Collection<Integer> interest;
	
	private String csv;
	
	private List<Double> minimumAreas;
	
	private double minimumTotal;
	
	private Map<Integer, Map<Integer, Count>> patchs;
	
	public NewClusteringAnalysis(Matrix m, Collection<Integer> interest, List<Double> minimumAreas, double minimumTotal, String outputFolder, String name){
		super(m);
		this.interest = interest;
		this.csv = outputFolder+"/cluster_"+name+".csv";
		this.minimumAreas = minimumAreas;
		this.minimumTotal = minimumTotal;
	}
	
	@Override
	protected void doRun() {
		RasterComposite r = new RasterComposite();
		Set<Pixel> ever = new TreeSet<Pixel>();
		double v;
		int oldy = -1;
		int index = 0;
		int total = matrix().width() * matrix().height();
		
		patchs = new TreeMap<Integer, Map<Integer, Count>>();
		
		for(Pixel p : matrix()){
			if(p.y() > oldy){ // nettoyage du trop plein 
				Iterator<Pixel> ite = ever.iterator();
				while(ite.hasNext()){
					if(ite.next().y() == oldy){
						ite.remove();
					}else{
						break;
					}
				}
				// fin nettoyage
				oldy = p.y();
			}
			
			if(!ever.contains(p)){
				ever.add(p);
				v = matrix().get(p);
				if(v != Raster.getNoDataValue()){
					if(interest == null || interest.contains((int) v)){
						PixelComposite pc = diffuseFromPixel(matrix(), p, ever, new PixelComposite(p, ++index));
						
						int vu = pc.getValue();
						patchs.put(vu, new HashMap<Integer, Count>());
						double totalv = 0.0;
						for(Pixel pi : pc){
							int vv = (int) matrix(0).get(pi);
							if(vv != 0 && vv != Raster.getNoDataValue()){
								if(!patchs.get(vu).containsKey(vv)){
									patchs.get(vu).put(vv, new Count());
								}
								patchs.get(vu).get(vv).add();
								totalv++;
							}
						}
						if(minimumTotal != -1){
							if(minimumTotal <= totalv * Math.pow(Raster.getCellSize(), 2) / 10000.0){
								r.addSimplePixelComposite(pc);
							}else{
								patchs.remove(vu);
							}
						}else{
							r.addSimplePixelComposite(pc);
						}
					}
				}
			}
			
			updateProgression(total);
		}
		
		r = (RasterComposite) r.smooth();

		//setResult(r.smooth());
		setResult(r);
	}
	
	private PixelComposite diffuseFromPixel(Matrix m, Pixel p, Set<Pixel> ever, PixelComposite pc) {
		
		LinkedList<Pixel> next = new LinkedList<Pixel>();
		next.add(p);
		
		while(!next.isEmpty()){
			Pixel np = next.poll();
			treatPixel(m, np, ever, next);
			pc.addSimplePixel(np);
		}
		
		return pc;
	}
	
	private void treatPixel(Matrix m, Pixel p, Set<Pixel> ever, LinkedList<Pixel> next){
		Iterator<Pixel> ite = getMargins(p);
		Pixel np;
		while(ite.hasNext()){
			np = ite.next();
			if(!ever.contains(np) && m.contains(np)){
				int v = (int) m.get(np);
				if(interest.contains(v)){
					next.addLast(np);
					ever.add(np);
				}
			}
		}
	}
	
	public abstract Iterator<Pixel> getMargins(Pixel p);
	/*{
		//return p.getMargins();
		return p.getCardinalMargins();
	}*/
	
	@Override
	protected void doClose() {
		/*
		Map<Integer, Map<Integer, Count>> patchs = new TreeMap<Integer, Map<Integer, Count>>();
		
		for(Raster pc : (RasterComposite) getResult()){
			
			int vu = ((PixelComposite) pc).getValue();
			patchs.put(vu, new HashMap<Integer, Count>());
			for(Pixel p : pc){
				int vv = (int) matrix(0).get(p);
				if(vv != 0 && vv != Raster.getNoDataValue()){
					if(!patchs.get(vu).containsKey(vv)){
						patchs.get(vu).put(vv, new Count());
					}
					patchs.get(vu).get(vv).add();
				}
			}
		}*/
		
		try {
			CsvWriter cw = new CsvWriter(csv);
			cw.setDelimiter(';');
			
			cw.write("id");
			cw.write("nb_pixels_patch");
			cw.write("surface_patch");
			
			for(int vM : interest){
				if(vM != 0 && vM != Raster.getNoDataValue()){
					cw.write("nb_pixels_patch_"+vM);
					cw.write("surface_patch_"+vM);
					cw.write("rate_patch_"+vM);
				}
			}
			
			cw.endRecord();
			
			for(Entry<Integer, Map<Integer, Count>> e : patchs.entrySet()){
				int vu = e.getKey();
			
				cw.write(vu+"");
					
				int nbpp = 0;
				for(int vM : interest){
					if(e.getValue().containsKey(vM)){
						nbpp += patchs.get(vu).get(vM).get();
					}
				}
				cw.write(nbpp+"");
				cw.write((nbpp*Math.pow(Raster.getCellSize(), 2))+"");
				
				int nbpsp = 0;					
				for(int vM : interest){
					if(e.getValue().containsKey(vM)){
						nbpsp = patchs.get(vu).get(vM).get();
						cw.write(nbpsp+"");
						cw.write((nbpsp*Math.pow(Raster.getCellSize(), 2))+"");
						cw.write((nbpsp*100.0/nbpp)+"");
					}else{
						cw.write("0");
						cw.write("0.0");
						cw.write("0.0");
					}
				}
					
				cw.endRecord();
			}
			
			cw.close();
		} catch (FinalizedException | IOException e) {
			e.printStackTrace();
		}
		
	}

}
