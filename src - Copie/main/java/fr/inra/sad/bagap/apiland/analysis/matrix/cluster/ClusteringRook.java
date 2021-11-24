package fr.inra.sad.bagap.apiland.analysis.matrix.cluster;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import fr.inra.sad.bagap.apiland.analysis.matrix.MatrixAnalysis;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.MatrixFactory;

public class ClusteringRook extends MatrixAnalysis {

	private Collection<Integer> interest;
	
	public ClusteringRook(Matrix m, Collection<Integer> interest){
		super(m);	
		this.interest = interest;
	}
	
	@Override
	protected void doRun() {
		Matrix m2 = MatrixFactory.get(matrix().getType()).create(matrix().width(), matrix().height(), matrix().cellsize(), matrix().minX(), matrix().maxX(), matrix().minY(), matrix().maxY(), matrix().noDataValue());
		m2.init(0);
		int actual = 1;
		//Set<Set<Double>> sames = new HashSet<Set<Double>>();
		Map<Double, Double> sames = new HashMap<Double, Double>();
		//double[] sames = new double[9999999];
		
		int total = matrix().width() * matrix().height() * 3;
		
		for(int j=0; j<matrix().height(); j++){
			for(int i=0; i<matrix().width(); i++){
				
				double v = matrix().get(i, j);
				if(interest.contains((int) v)){
					if(j>0 && i==0){
						double vj = m2.get(i, j-1);
						if(vj > 0){
							m2.put(i, j, vj);
						}else{
							sames.put((double) actual, -1.0);
							//sames[actual] = -1.0;
							m2.put(i, j, actual++);
						}
					}else if(i>0 && j==0){
						double vi = m2.get(i-1, j);
						if(vi > 0){
							m2.put(i, j, vi);
						}else{
							sames.put((double) actual, -1.0);
							m2.put(i, j, actual++);
						}
					}else if(j>0 && i>0){
						double vi = m2.get(i-1, j);
						double vj = m2.get(i, j-1);
						if(vi > 0 && vj > 0){
							if(vi == vj){
								m2.put(i, j, vj);
							}else{
								m2.put(i, j, vj);
								
								double vvi = getSame(sames, vi);
								double vvj = getSame(sames, vj);
								
								if(vvi < vvj){
									sames.put(vvj, vvi);
								}else if(vvj < vvi){
									sames.put(vvi, vvj);
								}
							}
						}else if(vi > 0 && vj == 0){
							m2.put(i, j, vi);
						}else if(vi == 0 && vj > 0){
							m2.put(i, j, vj);
						}else{
							sames.put((double) actual, -1.0);
							m2.put(i, j, actual++);
						}
					}else{
						sames.put((double) actual, -1.0);
						m2.put(i, j, actual++);
					}
				}
				
				updateProgression(total);
			}
		}
		
		m2.resetValues();
		
		for(int j=0; j<m2.height(); j++){
			for(int i=0; i<m2.width(); i++){
				double v = m2.get(i, j);
				if(v>0){
					m2.put(i, j, getSame(sames, v));
				}
				
				updateProgression(total);
			}
		}
		
		setResult(m2);
		
	}
	
	double getSame(Map<Double, Double> sames, double v){
		//System.out.println(sames.size());
		double nv = sames.get(v);
		if(nv == -1){
			return v;
		}else{
			return getSame(sames, nv);
		}
	}

}
