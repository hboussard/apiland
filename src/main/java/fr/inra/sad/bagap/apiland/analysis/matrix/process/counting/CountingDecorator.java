package fr.inra.sad.bagap.apiland.analysis.matrix.process.counting;

import java.util.Set;

import fr.inra.sad.bagap.apiland.analysis.matrix.process.SimpleWindowMatrixProcess;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.patch.PatchComposite;

public abstract class CountingDecorator extends Counting {

	private Counting decorate;
	
	public CountingDecorator(Counting decorate){
		this.decorate = decorate;
	}
	
	@Override
	public String toString(){
		return this.getClass()+" "+decorate.toString();
	}
	
	@Override
	public boolean isBinding(Class<? extends Counting> binding) {
		if(super.isBinding(binding)){
			return true;
		}
		return decorate.isBinding(binding);
	}
	
	@Override
	public SimpleWindowMatrixProcess process(){
		return (SimpleWindowMatrixProcess) decorate.process();
	}
	
	@Override
	public final void init() {
		decorate.init();
		doInit();
	}

	protected abstract void doInit();

	@Override
	public void add(double value, int x, int y, int filter, double ch, double cv) {
		//System.out.println("add (CountingDecorator)");
		decorate.add(value, x, y, filter, ch, cv);
		doAdd(value, x, y, filter, ch, cv);
	}
	
	protected final void doAdd(double value, Pixel pixel, int filter, double ch, double cv){
		//System.out.println("doAdd (CountingDecorator)");
		doAdd(value, pixel.x(), pixel.y(), filter, ch, cv);
	}
	
	protected abstract void doAdd(double value, int x, int y, int filter, double ch, double cv);
	
	@Override
	public final void addValue(double value, int x, int y){
		//System.out.println("addValue (CountingDecorator)");
		decorate.addValue(value, x, y);
		doAddValue(value, x, y);
	}

	protected void doAddValue(double value, int x, int y) {
		//System.out.println("doAddValue (CountingDecorator)");
		// do nothing
	}

	@Override
	public final void removeValue(double value, int x, int y) {
		decorate.removeValue(value, x, y);
		doRemoveValue(value, x, y);
	}
	
	protected void doRemoveValue(double value, int x, int y) {
		// do nothing
	}
	
	@Override
	public final void addCouple(double couple, int x1, int y1, int x2, int y2){
		decorate.addCouple(couple, x1, y1, x2, y2);
		doAddCouple(couple, x1, y1, x2, y2);
	}
	
	protected void doAddCouple(double couple, int x1, int y1, int x2, int y2) {
		// do nothing
	}
	
	@Override
	public final void removeCouple(double couple, int x1, int y1, int x2, int y2){
		decorate.removeCouple(couple, x1, y1, x2, y2);
		doRemoveCouple(couple, x1, y1, x2, y2);
	}
	
	protected void doRemoveCouple(double value, int x1, int y1, int x2, int y2){
		// do nothing
	}
	
	@Override
	public final void delete() {
		decorate.delete();
		doDelete();
	}
	
	protected abstract void doDelete();

	@Override
	public void down(int d, int place) {
		decorate.down(d, place);
		doDown(d ,place);
	}
	
	protected void doDown(int d, int place){
		// do nothing
	}
	
	@Override
	public double totalValues() {
		return decorate.totalValues();
	}

	@Override
	public double validValues() {
		return decorate.validValues();
	}
	
	@Override
	public int theoreticalSize() {
		return decorate.theoreticalSize();
	}
	
	@Override
	public Set<Integer> values() {
		return decorate.values();
	}
	
	@Override
	public double countValues() {
		return decorate.countValues();
	}

	@Override
	public double countValue(int v) {
		return decorate.countValue(v);
	}
	
	@Override
	public double countClass() {
		return decorate.countClass();
	}

	/*
	@Override
	public double averageClass() {
		return decorate.averageClass();
	}

	@Override
	public double varianceClass() {
		return decorate.varianceClass();
	}
	
	@Override
	public double standardDeviationClass() {
		return decorate.standardDeviationClass();
	}
	*/

	@Override
	public double getVariance() {
		return decorate.getVariance();
	}

	@Override
	public double getAverage() {
		return decorate.getAverage();
	}

	@Override
	public double getSum() {
		return decorate.getSum();
	}

	@Override
	public double getSquareSum() {
		return decorate.getSquareSum();
	}

	@Override
	public double getStandardDeviation() {
		return decorate.getStandardDeviation();
	}

	@Override
	public double getStandardError() {
		return decorate.getStandardError();
	}

	@Override
	public double countPositives() {
		return decorate.countPositives();
	}

	@Override
	public double countNegatives() {
		return decorate.countNegatives();
	}

	@Override
	public double size() {
		return decorate.size();
	}

	@Override
	public double getVariationCoefficient() {
		return decorate.getVariationCoefficient();
	}
	
	@Override
	public Set<Double> couples() {
		return decorate.couples();
	}

	@Override
	public double totalCouples() {
		return decorate.totalCouples();
	}

	@Override
	public double validCouples() {
		return decorate.validCouples();
	}

	@Override
	public double countCouples() {
		return decorate.countCouples();
	}

	@Override
	public double homogeneousCouples() {
		return decorate.homogeneousCouples();
	}

	@Override
	public double heterogeneousCouples() {
		return decorate.heterogeneousCouples();
	}

	@Override
	public double countCouple(double c) {
		return decorate.countCouple(c);
	}
	
	@Override
	public PatchComposite patches() {
		return decorate.patches();
	}
	
	@Override
	public double getPatchNumber(){
		return decorate.getPatchNumber();
	}
	
	@Override
	public double getPatchNumber(int classe){
		return decorate.getPatchNumber(classe);
	}
	
	@Override
	public double getLargestPatchSize(){
		return decorate.getLargestPatchSize();
	}
	
	@Override
	public double getLargestPatchSize(int classe){
		return decorate.getLargestPatchSize(classe);
	}
	
	@Override
	public double getMeanPatchSize(){
		return decorate.getMeanPatchSize();
	}
	
	@Override
	public double getMeanPatchSize(int classe){
		return decorate.getMeanPatchSize(classe);
	}
	
	@Override
	public double getShannonDiversityPatchSize(){
		return decorate.getShannonDiversityPatchSize();
	}
	
	@Override
	public double getShannonDiversityPatchSize(int classe){
		return decorate.getShannonDiversityPatchSize(classe);
	}
	
	/*
	@Override
	public double getStandardDeviationPatchSize(){
		return decorate.getStandardDeviationPatchSize();
	}
	
	@Override
	public double getStandardDeviationPatchSize(int classe){
		return decorate.getStandardDeviationPatchSize(classe);
	}
	
	@Override
	public double getVariationCoefficientPatchSize(){
		return decorate.getVariationCoefficientPatchSize();
	}
	
	@Override
	public double getVariationCoefficientPatchSize(int classe){
		return decorate.getVariationCoefficientPatchSize(classe);
	}
	*/
	
	@Override
	public double getMaximum() {
		return decorate.getMaximum();
	}

	@Override
	public double getMinimum() {
		return decorate.getMinimum();
	}
	
	/*
	@Override
	public double[][] datas(){
		return decorate.datas();
	}
	*/
	
	@Override
	public double getTruncatedAverage(double threshold){
		return decorate.getTruncatedAverage(threshold);
	}

}
