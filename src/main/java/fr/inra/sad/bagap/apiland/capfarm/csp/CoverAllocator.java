package fr.inra.sad.bagap.apiland.capfarm.csp;

import java.util.Map;
import java.util.Set;
import fr.inra.sad.bagap.apiland.capfarm.model.CoverGroup;
import fr.inra.sad.bagap.apiland.capfarm.model.CoverUnit;
import fr.inra.sad.bagap.apiland.capfarm.model.Covering;
import fr.inra.sad.bagap.apiland.capfarm.model.FarmingSystem;
import fr.inra.sad.bagap.apiland.capfarm.model.constraint.CoverAllocationConstraint;
import fr.inra.sad.bagap.apiland.capfarm.model.territory.Parcel;
import fr.inra.sad.bagap.apiland.core.element.DynamicElement;
import fr.inra.sad.bagap.apiland.core.time.Instant;

public interface CoverAllocator extends Covering {

	String getCode();
	
	DynamicElement getTerritory();
	
	int getArea();
	
	Set<CoverUnit> coverUnits();
	
	CoverUnit getCoverUnit(String code);
	
	Set<CoverGroup> coverGroups();
	
	Set<Parcel> parcels();
	
	Parcel parcel(String id);
	
	Set<CoverAllocationConstraint<?,?>> getConstraints();
	
	int totalParcelsArea();
	
	int totalEdgesLength();
	
	int[] edgesLength();
	
	void addConstraint(CoverAllocationConstraint<?, ?> constraint);
	
	int getDistanceFromFacilitiesToParcel(String facilities, Parcel parcel);
	
	void clearParcels();

	FarmingSystem getFarmingSystem();
	
	void clearFarmingSystem();
	
	//void setGenericFarmingSystem(GenericFarmingSystem system);
	
	void checkFarmingSystem(Instant start, Instant end, boolean verbose);
	
	boolean hasMemory();
	
	int getMemory();
	
	void setMemory(int memory);
	
	void setMemory(boolean memory);
	
	// test de la procedure de FixedAllocationProblem
	Map<Parcel, CoverUnit> getSolution();
	
	void setSolution(Map<Parcel, CoverUnit> solution);

	
	
}
