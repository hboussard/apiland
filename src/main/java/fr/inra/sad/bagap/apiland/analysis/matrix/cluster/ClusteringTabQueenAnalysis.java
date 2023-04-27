package fr.inra.sad.bagap.apiland.analysis.matrix.cluster;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import fr.inra.sad.bagap.apiland.analysis.Analysis;

public class ClusteringTabQueenAnalysis extends Analysis{

	private int noDataValue;
	
	private int nbNoDataValue;
	
	private int[] tabCover;
	
	private int height, width;
	
	private Set<Integer> interest;
	
	public ClusteringTabQueenAnalysis(int[] tabCover, int width, int height, int[] interest, int noDataValue){	
		this.tabCover = tabCover;
		this.height = height;
		this.width = width;
		this.interest = new HashSet<Integer>();
		for(int v : interest){
			this.interest.add(v);
		}
		this.noDataValue = noDataValue;
	}
	
	public ClusteringTabQueenAnalysis(float[] tabCover, int width, int height, int[] interest, int noDataValue){	
		this.tabCover = new int[tabCover.length];
		int ind = 0;
		for(float tc : tabCover){
			this.tabCover[ind++] = (int) tc;
		}
		this.height = height;
		this.width = width;
		this.interest = new HashSet<Integer>();
		for(int v : interest){
			this.interest.add(v);
		}
		this.noDataValue = noDataValue;
	}
	
	@Override
	protected void doRun() {
		
		int[] tabCluster = new int[tabCover.length];
		
		int actual = 1;
		Map<Integer, Integer> sames = new HashMap<Integer, Integer>();
		
		int v, vi, vj, vg, vd;  
		int vci, vcj, vcg, vcd, vvci, vvcj, vvcd, vvcg;
		for(int j=0; j<height; j++){
			for(int i=0; i<width; i++){
				
				v = (int) tabCover[j*width+i];
				if(v == noDataValue){
					nbNoDataValue++;
				}else if(interest.contains(v)){
					
					if(j>0 && i>0 && i<width-1){ // cas 1
						vci = tabCluster[j*width + (i-1)];
						vcj = tabCluster[(j-1)*width + i];
						vcg = tabCluster[(j-1)*width + (i-1)];
						vcd = tabCluster[(j-1)*width + (i+1)];
						
						vi = (int) tabCover[j*width + (i-1)];
						vj = (int) tabCover[(j-1)*width + i];
						vg = (int) tabCover[(j-1)*width + (i-1)];
						vd = (int) tabCover[(j-1)*width + (i+1)];
						
						if(vi == v && vi == vj && vci>0 && vcj>0){
							if(vci == vcj){
								tabCluster[j*width + i] = vcj;
							}else{
								tabCluster[j*width + i] = vcj;
																
								vvci = getSame(sames, vci);
								vvcj = getSame(sames, vcj);
								
								if(vvci < vvcj){
									sames.put(vvcj, vvci);
								}else if(vvcj < vvci){
									sames.put(vvci, vvcj);
								}
							}
						}else if(vi == v && vi == vd && vci>0 && vcd>0){
							if(vci == vcd){
								tabCluster[j*width + i] = vcd;
							}else{
								tabCluster[j*width + i] = vcd;
																
								vvci = getSame(sames, vci);
								vvcd = getSame(sames, vcd);
								
								if(vvci < vvcd){
									sames.put(vvcd, vvci);
								}else if(vvcd < vvci){
									sames.put(vvci, vvcd);
								}
							}
							
						}else if(vg == v && vg == vd && vcg>0 && vcd>0){
							if(vcg == vcd){
								tabCluster[j*width + i] = vcg;
							}else{
								tabCluster[j*width + i] = vcg;
																
								vvcg = getSame(sames, vcg);
								vvcd = getSame(sames, vcd);
								
								if(vvcg < vvcd){
									sames.put(vvcd, vvcg);
								}else if(vvcd < vvcg){
									sames.put(vvcg, vvcd);
								}
							}
						}else if(vi == v && vci>0){
							tabCluster[j*width + i] = vci;
						}else if(vj == v && vcj>0){
							tabCluster[j*width + i] = vcj;
						}else if(vg == v && vcg>0){
							tabCluster[j*width + i] = vcg;
						}else if(vd == v && vcd>0){
							tabCluster[j*width + i] = vcd;
						}else{
							sames.put(actual, -1);
							tabCluster[j*width + i] = actual++;
						}
						
					}else if(i>0 && j==0){ // cas 2
						vci = tabCluster[j*width + (i-1)];
						vi = (int) tabCover[j*width + (i-1)];
						if(vi == v && vci > 0){
							tabCluster[j*width + i] = vci;
						}else{
							sames.put(actual, -1);
							tabCluster[j*width + i] = actual++;
						}
					}else if(j>0 && i==0){ // cas 3
						vcj = tabCluster[(j-1)*width + i];
						vcd = tabCluster[(j-1)*width + (i+1)];
						
						vj = (int) tabCover[(j-1)*width + i];
						vd = (int) tabCover[(j-1)*width + (i+1)];
						
						if(vj == v && vcj>0){
							tabCluster[j*width + i] = vcj;
						}else if(vd == v && vcd >0){
							tabCluster[j*width + i] = vcd;
						}else{
							sames.put(actual, -1);
							tabCluster[j*width + i] = actual++;
						}
						
					}else if(j>0 && i==width-1){ // cas 4
						vci = tabCluster[j*width + (i-1)];
						vcj = tabCluster[(j-1)*width + i];
						vcg = tabCluster[(j-1)*width + (i-1)];
						
						vi = (int) tabCover[j*width + (i-1)];
						vj = (int) tabCover[(j-1)*width + i];
						vg = (int) tabCover[(j-1)*width + (i-1)];
						
						if(vi == v && vi == vj && vci>0 && vcj>0){
							if(vci == vcj){
								tabCluster[j*width + i] = vcj;
							}else{
								tabCluster[j*width + i] = vcj;
																
								vvci = getSame(sames, vci);
								vvcj = getSame(sames, vcj);
								
								if(vvci < vvcj){
									sames.put(vvcj, vvci);
								}else if(vvcj < vvci){
									sames.put(vvci, vvcj);
								}
							}
						}else if(vi == v && vci>0){
							tabCluster[j*width + i] = vci;
						}else if(vj == v && vcj>0){
							tabCluster[j*width + i] = vcj;
						}else if(vg == v && vcg>0){
							tabCluster[j*width + i] = vcg;
						}else{
							sames.put(actual, -1);
							tabCluster[j*width + i] = actual++;
						}
							
					}else{ // cas 5
						sames.put(actual, -1);
						tabCluster[j*width + i] = actual++;
					}
				}
			}
		}
		
		for(int j=0; j<height; j++){
			for(int i=0; i<width; i++){
				v = tabCluster[j*width + i];
				if(v>0){
					tabCluster[j*width + i] = getSame(sames, v);
				}
			}
		}
		
		setResult(tabCluster);
	}
	
	private int getSame(Map<Integer, Integer> sames, int v){
		int nv = sames.get(v);
		if(nv == -1){
			return v;
		}else{
			return getSame(sames, nv);
		}
	}

	@Override
	protected void doInit() {
		nbNoDataValue = 0;
	}

	@Override
	protected void doClose() {
		// do nothing
	}
	
	public int getNbNoDataValue(){
		return nbNoDataValue;
	}

}
