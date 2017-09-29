package fr.inra.sad.bagap.apiland.capfarm.model;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import fr.inra.sad.bagap.apiland.capfarm.csp.CoverAllocator;
import fr.inra.sad.bagap.apiland.capfarm.model.constraint.CoverAllocationConstraint;
import fr.inra.sad.bagap.apiland.capfarm.model.territory.FarmTerritory;
import fr.inra.sad.bagap.apiland.capfarm.model.territory.Parcel;
import fr.inra.sad.bagap.apiland.core.time.Instant;

public class Farm implements CoverAllocator {

	private String code;
	
	private FarmingSystem system;
	
	private FarmTerritory territory;
	
	private String historic;
	
	public Farm(String code){
		this.code = code;
		this.system = new FarmingSystem(this);
	}
	
	@Override
	public String getCode() {
		return code;
	}
	
	@Override
	public void addCover(Cover c){
		system.addCover(c);
	}

	@Override
	public void addCovers(Collection<Cover> covers) {
		system.addCovers(covers);
	}

	@Override
	public FarmingSystem getFarmingSystem() {
		return system;
	}

	@Override
	public FarmTerritory getTerritory() {
		return territory;
	}

	public void setFarmingSystem(FarmingSystem system) {
		this.system = system;
	}

	public void setTerritory(FarmTerritory territory) {
		this.territory = territory;
	}
	
	@Override
	public Set<CoverAllocationConstraint<?,?>> getConstraints() {
		return system.getConstraints();
	}

	@Override	
	public int totalParcelsArea() {
		return territory.totalParcelsArea();
	}

	@Override
	public int totalEdgesLength() {
		return territory.totalEdgesLength();
	}

	@Override
	public int[] edgesLength(){
		return territory.edgesLength();
	}

	@Override
	public Set<Parcel> parcels() {
		return territory.parcels();
	}
	
	@Override
	public Parcel parcel(String id) {
		return (Parcel) territory.get(id);
	}

	@Override
	public Set<CoverUnit> coverUnits(){
		return system.coverUnits();
	}

	@Override
	public Set<CoverGroup> coverGroups(){
		return system.coverGroups();
	}
	
	@Override
	public void clearParcels() {
		territory.clearParcels();
		memory = 0;
	}
	
	@Override
	public void addConstraint(CoverAllocationConstraint<?, ?> constraint) {
		system.addConstraint(constraint);
		constraint.setAllocator(this);
	}

	@Override
	public int getDistanceFromFacilitiesToParcel(String facilities, Parcel parcel){
		return territory.getDistanceFromFacilitiesToParcel(facilities, parcel);
	}

	@Override
	public int getArea() {
		return territory.totalParcelsArea();
	}

	@Override
	public CoverUnit getCoverUnit(String code) {
		return system.getCoverUnit(code);
	}

	@Override
	public void clearFarmingSystem() {
		this.system = new FarmingSystem(this);
	}
	
	public void setHistoric(String historic){
		this.historic = historic;
	}
	
	public String getHistoric(){
		return this.historic;
	}
	
	public boolean hasHistoric(){
		return this.historic != null;
	}

	@Override
	public void checkFarmingSystem(Instant start, Instant end, boolean verbose) {
		for(CoverAllocationConstraint<?, ?> ca : system.getConstraints()){
			ca.check(start, end, true);
		}
	}
	
	// gestion du memory
	int memory = 0;
	boolean hasMemory = false;
	
	@Override
	public void setMemory(int memory){
		this.memory = memory;
	}
	
	@Override
	public void setMemory(boolean memory){
		this.hasMemory = memory;
	}
	
	@Override
	public boolean hasMemory() {
		return hasMemory;
	}

	@Override
	public int getMemory() {
		return memory;
	}

	// test de la procedure de FixedCoverAllocator
	
	private String solutionFile;
	
	private Map<Parcel, CoverUnit> solution;
	
	@Override
	public void setSolution(Map<Parcel, CoverUnit> solution){
		this.solution = solution;
	}
	
	public void setSolution(String solutionfixFile){
		this.solutionFile = solutionfixFile;
	}
	
	public String getFixFile(){
		return solutionFile;
	}
	
	public boolean hasSolution(){
		return solutionFile != null;
	}
	
	@Override
	public Map<Parcel, CoverUnit> getSolution() {
		return solution;
	}

}
