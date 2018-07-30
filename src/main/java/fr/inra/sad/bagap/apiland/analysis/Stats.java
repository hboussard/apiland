/*Copyright 2010 by INRA SAD-Paysage (Rennes)

Author: Hugues BOUSSARD 
email : hugues.boussard@rennes.inra.fr

This library is a JAVA toolbox made to create and manage dynamic landscapes.

This software is governed by the CeCILL-C license under French law and
abiding by the rules of distribution of free software.  You can  use,
modify and/ or redistribute the software under the terms of the CeCILL-C
license as circulated by CEA, CNRS and INRIA at the following URL
"http://www.cecill.info".

As a counterpart to the access to the source code and rights to copy,
modify and redistribute granted by the license, users are provided only
with a limited warranty  and the software's author,  the holder of the
economic rights,  and the successive licensors  have only  limited
liability.

In this respect, the user's attention is drawn to the risks associated
with loading,  using,  modifying and/or developing or reproducing the
software by the user in light of its specific status of free software,
that may mean  that it is complicated to manipulate, and that also
therefore means  that it is reserved for developers and experienced
professionals having in-depth computer knowledge. Users are therefore
encouraged to load and test the software's suitability as regards their
requirements in conditions enabling the security of their systems and/or
data to be ensured and,  more generally, to use and operate it in the
same conditions as regards security.

The fact that you are presently reading this means that you have had
knowledge of the CeCILL-C license and that you accept its terms.
*/
package fr.inra.sad.bagap.apiland.analysis;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.QuantitativeCountingInterface;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;

public class Stats implements QuantitativeCountingInterface {

	private double sum, squaresum, variance, average, stddeviation, min, max, stderror, varK;
	
	private int nb, nbPos, nbNeg;
	
	private List<Double> values;
	
	private static DecimalFormat format;
	
	static{
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		format = new DecimalFormat("0.00000", symbols);
	}
	
	public Stats(){
		values = new ArrayList<Double>();
		reset();
	}
	
	private static double format(double v){
		if(Double.isNaN(v)){
			return -1;
		}
		int f = new Double(Math.floor(v)).intValue();
		if(v == f){
			return f;
		}
		return Double.parseDouble(format.format(v));
	}
	
	public void reset(){
		values.clear();
		sum = 0;
		squaresum = 0;
		variance = 0;
		average = 0;
		stddeviation = 0;
		min = 0;
		max = 0;
		stderror = 0;
		nb = 0;
		nbPos = 0;
		nbNeg = 0;
		varK = 0;
	}
	
	public void add(double v){
		values.add(v);
	}
	
	public void calculate(){
		boolean hasMin = false;
		boolean hasMax = false;
		for(double v : values){
			if(hasMin){
				min = Math.min(min, v);
			}else{
				min = v;
				hasMin = true;
			}if(hasMax){
				max = Math.max(max, v);
			}else{
				max = v;
				hasMax = true;
			}
			if(v > 0){
				nbPos++;
			}else if(v < 0){
				nbNeg++;
			}
			sum += v;
			squaresum += (v * v); 
		}
		nb = values.size();
		if(nb == 0){
			min = Raster.getNoDataValue();
			max = Raster.getNoDataValue();
			nbPos = Raster.getNoDataValue();
			nbNeg = Raster.getNoDataValue();
			sum = Raster.getNoDataValue();
			squaresum = Raster.getNoDataValue();
			average = Raster.getNoDataValue();
			variance = Raster.getNoDataValue();
			stddeviation = Raster.getNoDataValue();
			varK = Raster.getNoDataValue();
			stderror = Raster.getNoDataValue();
		}else{
			average = getSum() / size();
			variance = Math.abs(getSquareSum()/size() - getAverage()*getAverage());
			stddeviation = Math.sqrt(getVariance());
			varK = getStandardDeviation()/getAverage();
			stderror = getStandardDeviation()/Math.sqrt(values.size());
		}
		/*
		System.out.println();
		System.out.println("size : "+nb+" "+size());
		System.out.println("min "+min+" "+getMinimum());
		System.out.println("max "+max+" "+getMaximum());
		System.out.println("pos "+nbPos+" "+nbPositives());
		System.out.println("neg "+nbNeg+" "+nbNegatives());
		System.out.println("sum "+sum+" "+getSum());
		System.out.println("sumsquare "+squaresum+" "+getSquareSum());
		System.out.println("average "+average+" "+getAverage());
		System.out.println("variance "+variance+" "+getVariance());
		System.out.println("stddeviation "+stddeviation+" "+getStandardDeviation());
		System.out.println("varK "+varK+" "+getVariationCoefficient());
		System.out.println("stderror "+stderror+" "+getStandardError());
		*/
	}
	
	@Override
	public double getVariance(){
		return format(variance);
	}
	
	@Override
	public double getAverage(){
		return format(average);
	}
	
	@Override
	public double getSum(){
		return format(sum);
	}
	
	@Override
	public double getSquareSum(){
		return format(squaresum);
	}
	
	@Override
	public double getStandardDeviation(){
		return format(stddeviation);
	}
	
	@Override
	public double getMaximum(){
		return format(max);
	}
	
	@Override
	public double getMinimum(){
		return format(min);
	}
	
	@Override
	public double getStandardError(){
		return format(stderror);
	}
	
	@Override
	public double getVariationCoefficient(){
		return format(varK);
	}
	
	@Override
	public int countPositives(){
		return nbPos;
	}
	
	@Override
	public int countNegatives(){
		return nbNeg;
	}
	
	@Override
	public int size(){
		return nb;
	}
	
}
