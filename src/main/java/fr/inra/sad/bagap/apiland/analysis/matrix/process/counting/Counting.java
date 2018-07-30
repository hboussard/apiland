package fr.inra.sad.bagap.apiland.analysis.matrix.process.counting;

import java.util.Collection;
import java.util.Set;

import fr.inra.sad.bagap.apiland.analysis.matrix.process.MatrixProcess;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.patch.PatchComposite;

public abstract class Counting implements 
	BasicCountingInterface, 
	ValueCountingInterface,
	CoupleCountingInterface,
	ClassCountingInterface,
	QuantitativeCountingInterface,
	PatchCountingInterface {
	
	private MatrixProcess process;
	
	public class Count{
		private int value = 0;
		void add(){value++;};
		void minus(){value--;};
		public int get(){return value;};
	}
	
	// note : the only possible MatrixProcess known by a Counting Object is a SimpleWindowProcess Object 
	// implies that the Counting Object has always access to the matrix of data while casting it
	public void setProcess(MatrixProcess process){
		this.process = process;
	}
	
	public MatrixProcess process(){
		return process;
	}
	
	/** to init properly the counting */
	public abstract void init();
	
	/**
	 * to add a value to the counting
	 * @param value : the value to add
	 * @param filter : the filter
	 * @param ch : horizontal couple
	 * @param cv : vertical couple
	 */
	public final void add(double value, Pixel pixel, int filter, double ch, double cv){
		add(value, pixel.x(), pixel.y(), filter, ch, cv);
	}
	
	public abstract void add(double value, int x, int y, int filter, double ch, double cv);
	
	public abstract void down();
	
	public abstract void addValue(double value, int x, int y);
	
	public abstract void removeValue(double value, int x, int y);
	
	public abstract void addCouple(double value);
	
	public abstract void removeCouple(double couple);

	/** to delete properly the counting */
	public abstract void delete();

	@Override
	public int totalValues() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int validValues() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Set<Integer> values() {
		throw new UnsupportedOperationException();
	}
	
	/*@Override
	public Collection<Count> counts() {
		throw new UnsupportedOperationException();
	}*/

	@Override
	public int countValues() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int countValue(int v) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public int countClass() {
		throw new UnsupportedOperationException();
	}

	@Override
	public double averageClass() {
		throw new UnsupportedOperationException();
	}

	@Override
	public double varianceClass() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public double standardDeviationClass() {
		throw new UnsupportedOperationException();
	}

	@Override
	public double getVariance() {
		throw new UnsupportedOperationException();
	}

	@Override
	public double getAverage() {
		throw new UnsupportedOperationException();
	}

	@Override
	public double getSum() {
		throw new UnsupportedOperationException();
	}

	@Override
	public double getSquareSum() {
		throw new UnsupportedOperationException();
	}

	@Override
	public double getStandardDeviation() {
		throw new UnsupportedOperationException();
	}

	@Override
	public double getMaximum() {
		throw new UnsupportedOperationException();
	}

	@Override
	public double getMinimum() {
		throw new UnsupportedOperationException();
	}

	@Override
	public double getStandardError() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int countPositives() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int countNegatives() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int size() {
		throw new UnsupportedOperationException();
	}

	@Override
	public double getVariationCoefficient() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Set<Double> couples() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int totalCouples() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int validCouples() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int countCouples() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int homogeneousCouples() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int unhomogeneousCouples() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public int countCouple(double c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public PatchComposite patches() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int theoricalSize() {
		throw new UnsupportedOperationException();
	}

	public boolean isBinding(Class<? extends Counting> binding) {
		return this.getClass().equals(binding);
	}

	
	//public abstract void down(int delta);
	
}
