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

public class StraightClusteringRookAnalysis2 extends MatrixAnalysis {

	private Set<Integer> interest;
	
	private PixelComposite[] previous;
	
	private PixelComposite[] actual;
	
	public StraightClusteringRookAnalysis2(Matrix m, Set<Integer> interest){
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
				
				for(Set<PixelComposite> same1 : sames){
					for(PixelComposite pc : same1){
						for(Set<PixelComposite> same2 : sames){
							if(same1 != same2 && same2.contains(pc)){
								System.out.println(i+" "+j+" "+pc);
								System.out.println(same1);
								System.out.println(same2);
								throw new IllegalArgumentException();
							}
						}
					}
				}
				
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
								/*
								actual[i-1].addAllSimplePixels(previous[i].getPixels());
								for(int x=0; x<matrix().width(); x++){
									if(previous[i] == previous[x]){
										previous[x] = actual[i-1];
									}
									if(actual[x] == previous[i]){
										actual[x] = actual[i-1];
									}
								}
								r.removeSimplePixelComposite(previous[i]);
								*/
								
								boolean ok = true;
								boolean ok1 = false;
								boolean ok2 = false;
								Set<PixelComposite> same1 = null;
								Set<PixelComposite> same2 = null;
								for(Set<PixelComposite> same : sames){
									boolean actual_i_1 = same.contains(actual[i-1]);
									boolean previous_i = same.contains(previous[i]);
		
									if(actual_i_1 && previous_i){
										//System.out.println("pass");
										ok = false;
										break;
										// do nothing											
									}else if(actual_i_1 && !previous_i){
										same1 = same;
										ok1 = true;
									}else if(previous_i && !actual_i_1){
										same2 = same;
										ok2 = true;
									}
									if(ok1 && ok2){
										break;
									}
								}
								if(i == 1297 && j == 4){
									System.out.println("ici "+ok+" "+ok1+" "+ok2);
									System.out.println(same1);
									System.out.println(same2);
								}
								if(ok){
									if(ok1 && ok2){
										System.out.println();
										System.out.println(sames.size());
										Iterator<Set<PixelComposite>> ite = sames.iterator();
										while(ite.hasNext()){
											ite.next();
											//if(ite.next().equals(same2)){
											System.out.println("pass");
											ite.remove();
											//}
										}
										System.out.println(sames.size());
										ite = sames.iterator();
										while(ite.hasNext()){
											ite.next();
											//if(ite.next().equals(same2)){
											System.out.println("pass");
											ite.remove();
											//}
										}
										System.out.println(sames.size());
										///sames.remove(same2);
										same1.addAll(same2);
										//same2.clear();
									}else if(ok1 && !ok2){
										same1.add(previous[i]);
									}else if(!ok1 && ok2){
										same2.add(actual[i-1]);
									}else{
										Set<PixelComposite> set = new HashSet<PixelComposite>();
										set.add(actual[i-1]);
										set.add(previous[i]);
										sames.add(set);
									}
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
		/*
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
			
		}*/
		//total = sames;
		System.out.println("fin");
		
		//System.out.println(r.size());
		
		System.out.println(sames.size());
		//System.out.println(total.size());
		//index = 0;
		
		//for(Set<PixelComposite> same : total){
		for(Set<PixelComposite> same : sames){
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
		System.out.println(lCode.size());
		
		//setResult(r.smooth());
		setResult(r);
	}

	@Override
	protected void doClose() {
		previous = null;
		actual = null;
	}
	
}
