package fr.inra.sad.bagap.apiland.analysis.matrix.cluster;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import fr.inra.sad.bagap.apiland.analysis.matrix.MatrixAnalysis;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.PixelComposite;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.RasterComposite;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;

public class StraightClusteringRookAnalysis extends MatrixAnalysis {

	private Set<Integer> interest;
	
	private PixelComposite[] previous;
	
	private PixelComposite[] actual;
	
	public StraightClusteringRookAnalysis(Matrix m, Set<Integer> interest){
		super(m);
		this.interest = interest;
	}
	
	@Override
	protected void doRun() {
		double v;
		
		actual = new PixelComposite[matrix().width()];
		int index = 1;
		RasterComposite r = new RasterComposite();
		Set<Set<PixelComposite>> sames = new HashSet<Set<PixelComposite>>();
		
		int totProgress = matrix().height() * matrix().width();
		
		for(int i=0; i<matrix().width(); i++){
			//System.out.println(i+" "+0);
			v = matrix().get(i, 0);
			if(interest.contains((int) v)){
				
				Pixel p = new Pixel(i,0);
				
				if(i>0 && actual[i-1] != null){
					actual[i-1].addSimplePixel(p);
					actual[i] = actual[i-1];
				}else{
					PixelComposite pc = new PixelComposite();
					pc.setValue(index++);
					pc.addSimplePixel(p);
					actual[i] = pc;
					r.addSimplePixelComposite(pc);
					
				}
			}
			updateProgression(totProgress);
		}
		previous = actual;
		
		for(int j=1; j<matrix().height(); j++){
			actual = new PixelComposite[matrix().width()];
			//System.out.println(j);
			for(int i=0; i<matrix().width(); i++){
				//System.out.println(i+" "+j);
				v = matrix().get(i, j);
				if(interest.contains((int) v)){
					
					Pixel p = new Pixel(i,j);
					
					if(i>0){
						if(actual[i-1] != null && previous[i] == null){
							//System.out.println("pass 1");
							actual[i-1].addSimplePixel(p);
							actual[i] = actual[i-1];
						}else if(actual[i-1] == null && previous[i] != null){
							//System.out.println("pass 2");
							previous[i].addSimplePixel(p);
							actual[i] = previous[i];
						}else if(actual[i-1] != null && previous[i] != null){
							//System.out.println("pass 3");
							actual[i-1].addSimplePixel(p);
							actual[i] = actual[i-1];
							if(actual[i-1] != previous[i]){
								boolean ok = false;
								for(Set<PixelComposite> same : sames){
									if(same.contains(actual[i])){
										same.add(previous[i]);
										ok = true;
										break;
									}
									if(same.contains(previous[i])){
										same.add(actual[i]);
										ok = true;
										break;
									}
								}
								if(!ok){
									Set<PixelComposite> set = new HashSet<PixelComposite>();
									set.add(actual[i-1]);
									set.add(previous[i]);
									sames.add(set);
								}
							}
						}else{
							//System.out.println("pass 4");
							PixelComposite pc = new PixelComposite();
							pc.setValue(index++);
							pc.addSimplePixel(p);
							actual[i] = pc;
							r.addSimplePixelComposite(pc);
							
						}
					}else{
						if(previous[i] != null){
							//System.out.println("pass 5");
							previous[i].addSimplePixel(p);
							actual[i] = previous[i];
						}else{
							//System.out.println("pass 6");
							PixelComposite pc = new PixelComposite();
							pc.setValue(index++);
							pc.addSimplePixel(p);
							actual[i] = pc;
							r.addSimplePixelComposite(pc);
							
						}
					}
				}
				updateProgression(totProgress);
			}
			previous = actual;
		}
		
		/*
		System.out.println("fin");
		for(Set<PixelComposite> same : sames){
			System.out.println("same");
			for(PixelComposite pc : same){
				System.out.println(pc.getValue());
			}
		}*/
		System.out.println("début");
		//Set<Set<PixelComposite>> init = sames;
		Set<Set<PixelComposite>> total = new HashSet<Set<PixelComposite>>();
		Set<Set<PixelComposite>> even = new HashSet<Set<PixelComposite>>();
		boolean modif = true;
		
		while(modif){
			//System.out.println("sames "+sames.size());
			modif = false;
			total.clear();
			even.clear();
			for(Set<PixelComposite> same1 : sames){
				//System.out.println("ici");
				if(!even.contains(same1)){
					even.add(same1);			 
					for(Set<PixelComposite> same2 : sames){
						if(!even.contains(same2)){
							for(PixelComposite pc1 : same1){
								if(same2.contains(pc1)){
									same1.addAll(same2);
									even.add(same2);
									modif = true;
									break;
								}
							}

						}
					}
					total.add(same1);
				}
			}
			if(modif){
				sames.clear();
				sames.addAll(total);
			}
			//System.out.println("total "+total.size());
			
		}
		//total = sames;
		System.out.println("fin");
		
		//System.out.println(r.size());
		
		//System.out.println(total.size());
		//index = 0;
		
		for(Set<PixelComposite> same : total){
			int i = -1;
			for(PixelComposite pc : same){
				if(i == -1){
					i = pc.getValue();
				}
				pc.setValue(i);
			}
		}
		
		List<Integer> lCode = new ArrayList<Integer>();
		int vc;
		boolean ok;
		for(Raster raster : ((RasterComposite) r).getRasters()){
			vc = ((PixelComposite) raster).getValue();
			ok = false;
			for(int i=0; i<lCode.size(); i++){
				if(lCode.get(i) == vc){
					((PixelComposite) raster).setValue(i+1);
					ok = true;
					break;
				}
			}
			if(!ok){
				lCode.add(vc);
				((PixelComposite) raster).setValue(lCode.size()+1);
			}
		}
		//System.out.println(lCode.size());
		
		//setResult(r.smooth());
		setResult(r);
	}

	@Override
	protected void doClose() {
		previous = null;
		actual = null;
	}
	
}
