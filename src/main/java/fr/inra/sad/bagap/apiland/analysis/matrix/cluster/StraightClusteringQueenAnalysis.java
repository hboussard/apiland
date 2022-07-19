package fr.inra.sad.bagap.apiland.analysis.matrix.cluster;

import java.util.HashSet;
import java.util.Set;

import fr.inra.sad.bagap.apiland.analysis.matrix.MatrixAnalysis;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.PixelComposite;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.RasterComposite;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;

public class StraightClusteringQueenAnalysis extends MatrixAnalysis {

	private Set<Integer> interest;
	
	private PixelComposite[] previous;
	
	private PixelComposite[] actual;
	
	public StraightClusteringQueenAnalysis(Matrix m, Set<Integer> interest){
		super(m);
		this.interest = interest;
	}
	
	@Override
	protected void doRun() {
		double v;
		RasterComposite r = new RasterComposite();
		actual = new PixelComposite[matrix().width()];
		int index = 1;
		
		int totProgress = matrix().height() * matrix().width();
		
		
		Set<Set<PixelComposite>> sames = new HashSet<Set<PixelComposite>>();
		
		for(int i=0; i<matrix().width(); i++){
			//System.out.println(i+" "+0);
			v = matrix().get(i, 0);
			if(interest.contains((int) v)){
				if(i>0 && actual[i-1] != null){
					actual[i-1].addSimplePixel(new Pixel(i,0));
					actual[i] = actual[i-1];
				}else{
					PixelComposite pc = new PixelComposite();
					pc.setValue(index++);
					pc.addSimplePixel(new Pixel(i,0));
					actual[i] = pc;
					r.addSimplePixelComposite(pc);
				}
			}
			updateProgression(totProgress);
		}
		previous = actual;
		
		Set<Set<PixelComposite>> sets1 = new HashSet<Set<PixelComposite>>();
		Set<Set<PixelComposite>> sets2 = new HashSet<Set<PixelComposite>>();
		
		for(int j=1; j<matrix().height(); j++){
			actual = new PixelComposite[matrix().width()];
			System.out.println(j);
			for(int i=0; i<matrix().width(); i++){
				//System.out.println(i+" "+j);
				v = matrix().get(i, j);
				if(interest.contains((int) v)){
					
					Pixel p = new Pixel(i,j);
					
					if(i>0 && actual[i-1] != null){
						actual[i-1].addSimplePixel(p);
						actual[i] = actual[i-1];
						if(i<matrix().width()-1 && previous[i+1] != null){
							if(actual[i-1] != previous[i+1]){
								//previous[i+1].setValue(actual[i-1].getValue());
								int value = previous[i+1].getValue();
								boolean ok1 = false;
								boolean ok2 = false;
								Set<PixelComposite> set1 = null;
								Set<PixelComposite> set2 = null;
								sets1.clear();
								sets2.clear();
								for(Set<PixelComposite> same : sames){
									if(same.contains(actual[i-1])){
										if(ok1){
											sets1.add(same);
										}else{
											set1 = same;
											ok1 = true;	
										}
									}
									if(same.contains(previous[i+1])){
										if(ok2){
											sets2.add(same);
										}else{
											set2 = same;
											ok2 = true;
										}
									}
								}
								if(ok1 && sets1.size()>0){
									for(Set<PixelComposite> set : sets1){
										for(PixelComposite pc : set){
											pc.setValue(value);
										}
										set1.addAll(set);
									}
								}
								if(ok2 && sets2.size()>0){
									for(Set<PixelComposite> set : sets2){
										for(PixelComposite pc : set){
											pc.setValue(value);
										}
										set2.addAll(set);
									}
								}
								if(ok1 && ok2){
									set2.addAll(set1);
									sames.remove(set1);
								}else if(ok1 && !ok2){
									set1.add(previous[i+1]);
								}else if(!ok1 && ok2){
									set2.add(actual[i-1]);
								}else{
									Set<PixelComposite> set = new HashSet<PixelComposite>();
									set.add(actual[i-1]);
									set.add(previous[i+1]);
									sames.add(set);
								}
							}
						}
					}else if(i>0 && previous[i-1] != null){
						previous[i-1].addSimplePixel(p);
						actual[i] = previous[i-1];
						if(i<matrix().width()-1 && previous[i+1] != null){
							if(previous[i-1] != previous[i+1]){
								//previous[i+1].setValue(previous[i-1].getValue());
								int value = previous[i-1].getValue();
								boolean ok1 = false;
								boolean ok2 = false;
								Set<PixelComposite> set1 = null;
								Set<PixelComposite> set2 = null;
								sets1.clear();
								sets2.clear();
								for(Set<PixelComposite> same : sames){
									if(same.contains(previous[i-1])){
										if(ok1){
											sets1.add(same);
										}else{
											set1 = same;
											ok1 = true;	
										}
									}
									if(same.contains(previous[i+1])){
										if(ok2){
											sets2.add(same);
										}else{
											set2 = same;
											ok2 = true;
										}
									}
								}
								if(ok1 && sets1.size()>0){
									for(Set<PixelComposite> set : sets1){
										for(PixelComposite pc : set){
											pc.setValue(value);
										}
										set1.addAll(set);
									}
								}
								if(ok2 && sets2.size()>0){
									for(Set<PixelComposite> set : sets2){
										for(PixelComposite pc : set){
											pc.setValue(value);
										}
										set2.addAll(set);
									}
								}
								if(ok1 && ok2){
									set1.addAll(set2);
									sames.remove(set2);
								}else if(ok1 && !ok2){
									set1.add(previous[i+1]);
								}else if(!ok1 && ok2){
									set2.add(previous[i-1]);
								}else{
									Set<PixelComposite> set = new HashSet<PixelComposite>();
									set.add(previous[i-1]);
									set.add(previous[i+1]);
									sames.add(set);
								}
							}
						}
					}else if(previous[i] != null){
						previous[i].addSimplePixel(p);
						actual[i] = previous[i];
					}else if(i<matrix().width()-1 && previous[i+1] != null){
						previous[i+1].addSimplePixel(p);
						actual[i] = previous[i+1];
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
		}
		
		for(Set<PixelComposite> same1 : sames){
			for(Set<PixelComposite> same2 : sames){
				
			}
		}
		
		for(Set<PixelComposite> sets : sames){
			int i = -1;
			for(PixelComposite pc : sets){
				if(i == -1){
					i = pc.getValue();
				}else{
					pc.setValue(i);
				}
			}
			
		}
		
		/*
		Set<Raster> even = new HashSet<Raster>();
		Set<Raster[]> doublons = new HashSet<Raster[]>();
		for(Raster pc1 : r.getRasters()){
			even.add(pc1);
			for(Raster pc2 : r.getRasters()){
				if(!even.contains(pc2)){
					for(Pixel p : pc1){
						if(pc2.contains(p)){
							Raster[] doubles = new Raster[2];
							doubles[0] = pc1;
							doubles[1] = pc2;
							doublons.add(doubles);
							System.out.println("1");
							break;
						}
					}
				}
			}
		}*/
		
		
		
		setResult(r.smooth());
	}

	@Override
	protected void doClose() {
		previous = null;
		actual = null;
	}
	
}
