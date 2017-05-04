package fr.inra.sad_paysage.apiland.analysis.matrix.counting;

import java.util.Collection;
import java.util.Set;
import fr.inra.sad_paysage.apiland.analysis.matrix.process.SimpleWindowMatrixProcess;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad_paysage.apiland.patch.PatchComposite;

public abstract class CountingDecorator extends Counting {

	private Counting decorate;
	
	public CountingDecorator(Counting decorate){
		this.decorate = decorate;
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
		decorate.add(value, x, y, filter, ch, cv);
		doAdd(value, x, y, filter, ch, cv);
	}
	
	protected final void doAdd(double value, Pixel pixel, int filter, double ch, double cv){
		doAdd(value, pixel.x(), pixel.y(), filter, ch, cv);
	}
	
	protected abstract void doAdd(double value, int x, int y, int filter, double ch, double cv);
	
	@Override
	public final void addValue(double value, int x, int y){
		decorate.addValue(value, x, y);
		doAddValue(value, x, y);
	}

	protected void doAddValue(double value, int x, int y) {
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
	public final void addCouple(double couple){
		decorate.addCouple(couple);
		doAddCouple(couple);
	}
	
	protected void doAddCouple(double couple) {
		// do nothing
	}
	
	@Override
	public final void removeCouple(double couple){
		decorate.removeCouple(couple);
		doRemoveCouple(couple);
	}
	
	protected void doRemoveCouple(double value){
		// do nothing
	}
	
	@Override
	public final void delete() {
		decorate.delete();
		doDelete();
	}
	
	protected abstract void doDelete();

	@Override
	public void down() {
		decorate.down();
		doDown();
	}
	
	protected void doDown(){
		// do nothing
	}
	
	@Override
	public int totalValues() {
		return decorate.totalValues();
	}

	@Override
	public int validValues() {
		return decorate.validValues();
	}
	
	@Override
	public int theoricalSize() {
		return decorate.theoricalSize();
	}
	
	@Override
	public Set<Integer> values() {
		return decorate.values();
	}
	
	@Override
	public Collection<Count> counts(){
		return decorate.counts();
	}

	@Override
	public int countValues() {
		return decorate.countValues();
	}

	@Override
	public int countValue(int v) {
		return decorate.countValue(v);
	}
	
	@Override
	public int countClass() {
		return decorate.countClass();
	}

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
	public double getMaximum() {
		return decorate.getMaximum();
	}

	@Override
	public double getMinimum() {
		return decorate.getMinimum();
	}

	@Override
	public double getStandardError() {
		return decorate.getStandardError();
	}

	@Override
	public int countPositives() {
		return decorate.countPositives();
	}

	@Override
	public int countNegatives() {
		return decorate.countNegatives();
	}

	@Override
	public int size() {
		return decorate.validValues();
	}

	@Override
	public double getVariationCoefficient() {
		return decorate.validValues();
	}
	
	@Override
	public Set<Double> couples() {
		return decorate.couples();
	}

	@Override
	public int totalCouples() {
		return decorate.totalCouples();
	}

	@Override
	public int validCouples() {
		return decorate.validCouples();
	}

	@Override
	public int countCouples() {
		return decorate.countCouples();
	}

	@Override
	public int homogeneousCouples() {
		return decorate.homogeneousCouples();
	}

	@Override
	public int unhomogeneousCouples() {
		return decorate.unhomogeneousCouples();
	}

	@Override
	public int countCouple(double c) {
		return decorate.countCouple(c);
	}
	
	@Override
	public PatchComposite patches() {
		return decorate.patches();
	}
	
}
