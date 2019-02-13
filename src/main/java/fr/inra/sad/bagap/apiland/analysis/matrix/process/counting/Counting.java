package fr.inra.sad.bagap.apiland.analysis.matrix.process.counting;

import java.util.Set;

import fr.inra.sad.bagap.apiland.analysis.matrix.process.MatrixProcess;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.patch.PatchComposite;

public abstract class Counting implements 
	BasicCountingInterface, 
	ValueCountingInterface,
	CoupleCountingInterface,
	//ClassCountingInterface,
	QuantitativeCountingInterface,
	FullQuantitativeCountingInterface,
	PatchCountingInterface {
	
	private MatrixProcess process;
	
	public class Count{
		private int value = 0;
		public void add(){value++;};
		public void minus(){value--;};
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
		//System.out.println("add (Counting)");
		add(value, pixel.x(), pixel.y(), filter, ch, cv);
	}
	
	/**
	 * to add a value to the counting
	 * @param value : the value to add
	 * @param filter : the filter
	 * @param ch : horizontal couple
	 * @param cv : vertical couple
	 */
	public abstract void add(double value, int x, int y, int filter, double ch, double cv);
	
	public abstract void down(int d, int place);
	
	public abstract void addValue(double value, int x, int y);
	
	public abstract void removeValue(double value, int x, int y);
	
	public abstract void addCouple(double value, int x1, int y1, int x2, int y2);
	
	public abstract void removeCouple(double couple, int x1, int y1, int x2, int y2);

	/** to delete properly the counting */
	public void delete(){
		process = null;
	}

	@Override
	public double totalValues() {
		throw new UnsupportedOperationException();
	}

	@Override
	public double validValues() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Set<Integer> values() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public double countValues() {
		throw new UnsupportedOperationException();
	}

	@Override
	public double countValue(int v) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public double countClass() {
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
	public double getStandardError() {
		throw new UnsupportedOperationException();
	}

	@Override
	public double countPositives() {
		throw new UnsupportedOperationException();
	}

	@Override
	public double countNegatives() {
		throw new UnsupportedOperationException();
	}

	@Override
	public double size() {
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
	public double totalCouples() {
		throw new UnsupportedOperationException();
	}

	@Override
	public double validCouples() {
		throw new UnsupportedOperationException();
	}

	@Override
	public double countCouples() {
		throw new UnsupportedOperationException();
	}

	@Override
	public double homogeneousCouples() {
		throw new UnsupportedOperationException();
	}

	@Override
	public double heterogeneousCouples() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public double countCouple(double c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public PatchComposite patches() {
		throw new UnsupportedOperationException();
	}

	@Override
	public double getPatchNumber(){
		throw new UnsupportedOperationException();
	}
	
	@Override
	public double getLargestPatchSize(){
		throw new UnsupportedOperationException();
	}
	
	@Override
	public double getLargestPatchSize(int classe){
		throw new UnsupportedOperationException();
	}
	
	@Override
	public double getPatchNumber(int classe){
		throw new UnsupportedOperationException();
	}
	
	@Override
	public double getMeanPatchSize(){
		throw new UnsupportedOperationException();
	}
	
	@Override
	public double getMeanPatchSize(int classe){
		throw new UnsupportedOperationException();
	}
	
	@Override
	public double getShannonDiversityPatchSize(){
		throw new UnsupportedOperationException();
	}
	
	@Override
	public double getShannonDiversityPatchSize(int classe){
		throw new UnsupportedOperationException();
	}
	
	/*
	@Override
	public double getStandardDeviationPatchSize(){
		throw new UnsupportedOperationException();
	}
	
	@Override
	public double getStandardDeviationPatchSize(int classe){
		throw new UnsupportedOperationException();
	}
	
	@Override
	public double getVariationCoefficientPatchSize(){
		throw new UnsupportedOperationException();
	}
	
	@Override
	public double getVariationCoefficientPatchSize(int classe){
		throw new UnsupportedOperationException();
	}
	*/
	
	@Override
	public double getMaximum() {
		throw new UnsupportedOperationException();
	}

	@Override
	public double getMinimum() {
		throw new UnsupportedOperationException();
	}
	
	/*@Override
	public double[][] datas(){
		throw new UnsupportedOperationException();
	}*/
	
	@Override
	public double getTruncatedAverage(double threshold){
		throw new UnsupportedOperationException();
	}
	
	@Override
	public int theoreticalSize() {
		throw new UnsupportedOperationException();
	}

	public boolean isBinding(Class<? extends Counting> binding) {
		return this.getClass().equals(binding);
	}

	
	//public abstract void down(int delta);
	
}
