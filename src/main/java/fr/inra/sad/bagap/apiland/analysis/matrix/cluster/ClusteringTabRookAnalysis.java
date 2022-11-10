package fr.inra.sad.bagap.apiland.analysis.matrix.cluster;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import fr.inra.sad.bagap.apiland.analysis.Analysis;

public class ClusteringTabRookAnalysis extends Analysis{

	private int[] tabCover;
	
	private int height, width;
	
	private Set<Integer> interest;
	
	public ClusteringTabRookAnalysis(int[] tabCover, int height, int width, int[] interest){	
		this.tabCover = tabCover;
		this.height = height;
		this.width = width;
		this.interest = new HashSet<Integer>();
		for(int v : interest){
			this.interest.add(v);
		}
	}
	
	@Override
	protected void doRun() {
		
		int[] tabCluster = new int[tabCover.length];
		
		int actual = 1;
		Map<Integer, Integer> sames = new HashMap<Integer, Integer>();
		
		int v, vi, vj;  
		int vci, vcj, vvci, vvcj;
		for(int j=0; j<height; j++){
			for(int i=0; i<width; i++){
				
				v = (int) tabCover[j*width+i];
				if(interest.contains(v)){
					
					if(j>0 && i>0 && i<width-1){ // cas 1
						vci = tabCluster[j*width + (i-1)];
						vcj = tabCluster[(j-1)*width + i];
						
						vi = (int) tabCover[j*width + (i-1)];
						vj = (int) tabCover[(j-1)*width + i];
						
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
						
						vj = (int) tabCover[(j-1)*width + i];
						
						if(vj == v && vcj>0){
							tabCluster[j*width + i] = vcj;
						}else{
							sames.put(actual, -1);
							tabCluster[j*width + i] = actual++;
						}
						
					}else if(j>0 && i==width-1){ // cas 4
						vci = tabCluster[j*width + (i-1)];
						vcj = tabCluster[(j-1)*width + i];
						
						vi = (int) tabCover[j*width + (i-1)];
						vj = (int) tabCover[(j-1)*width + i];
						
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
		// do nothing
	}

	@Override
	protected void doClose() {
		// do nothing
	}

}
