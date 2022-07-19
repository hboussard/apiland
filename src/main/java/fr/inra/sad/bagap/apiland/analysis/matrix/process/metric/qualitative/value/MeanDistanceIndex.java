package fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.qualitative.value;

import fr.inra.sad.bagap.apiland.analysis.VariableManager;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.Counting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.MatrixMetric;
import fr.inra.sad.bagap.apiland.analysis.process.metric.FullQuantitativeMetric;

public class MeanDistanceIndex extends MatrixMetric implements FullQuantitativeMetric {
	
	private double threshold;
	
	public MeanDistanceIndex() {
		super(VariableManager.get("MD"));
		this.threshold = 100;
	}

	@Override
	public void doCalculate(Counting co) {
		
		value = co.getTruncatedAverage(threshold);
		/*
		int count = 0;
		double v;
		double v2 = 0.0;
		for(int j=0; j<co.datas().length; j++){
			for(int i=0; i<co.datas().length; i++){
				v = co.datas()[j][i];
				if(v != Raster.getNoDataValue()){
					count++;
					if(v<=100){
						v2 += v/100;
					}else{
						v2 += 1;
					}
					
				}
			}
		}
		
		if(count == 0){
			value = 0;
		}else{
			value = v2/count;
		}
		*/
	}
	
	/*
	int count = 0;
	int count1 = 0;
	int count2 = 0;
	int count3 = 0;
	int count4 = 0;
	int count5 = 0;
	int count6 = 0;
	int count7 = 0;
	int count8 = 0;
	int count9 = 0;
	int count10 = 0;
	int count11 = 0;
	int count12 = 0;
	double v;
	for(int j=0; j<co.datas().length; j++){
		for(int i=0; i<co.datas().length; i++){
			v = co.datas()[j][i];
			if(v != Raster.getNoDataValue()){
				count++;
				if(v == 0){
					count1++;
				}else if(v <= 10){
					count2++;
				}else if(v <= 20){
					count3++;
				}else if(v <= 30){
					count4++;
				}else if(v <= 40){
					count5++;
				}else if(v <= 50){
					count6++;
				}else if(v <= 60){
					count7++;
				}else if(v <= 70){
					count8++;
				}else if(v <= 80){
					count9++;
				}else if(v <= 90){
					count10++;
				}else if(v <= 100){
					count11++;
				}else{
					count12++;
				}
			}
		}
	}
	
	double pc2 = new Double(count2)/(count);
	double pc3 = new Double(count3)/(count);
	double pc4 = new Double(count4)/(count);
	double pc5 = new Double(count5)/(count);
	double pc6 = new Double(count6)/(count);
	double pc7 = new Double(count7)/(count);
	double pc8 = new Double(count8)/(count);
	double pc9 = new Double(count9)/(count);
	double pc10 = new Double(count10)/(count);
	double pc11 = new Double(count11)/(count);
	double pc12 = new Double(count12)/(count);
	
	value = pc2/11 + 2*pc3/11 + 3*pc4/11 + 4*pc5/11 + 5*pc6/11 + 6*pc7/11 + 7*pc8/11 + 8*pc9/11 + 9*pc10/11 + 10*pc11/11 + pc12;
	*/

}