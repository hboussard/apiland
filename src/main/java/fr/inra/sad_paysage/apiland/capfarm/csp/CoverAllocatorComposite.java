package fr.inra.sad_paysage.apiland.capfarm.csp;

import java.util.Collection;
import java.util.Set;

import fr.inra.sad_paysage.apiland.capfarm.model.FarmingSystem;
import fr.inra.sad_paysage.apiland.capfarm.model.constraint.CoverAllocationConstraint;
import fr.inra.sad_paysage.apiland.capfarm.model.territory.FarmTerritory;
import fr.inra.sad_paysage.apiland.capfarm.model.territory.Parcel;
import fr.inra.sad_paysage.apiland.core.element.DynamicElement;
import fr.inra.sad_paysage.apiland.core.time.Instant;
import fr.inra.sad_paysage.apiland.capfarm.model.Cover;
import fr.inra.sad_paysage.apiland.capfarm.model.CoverGroup;
import fr.inra.sad_paysage.apiland.capfarm.model.CoverUnit;

public class CoverAllocatorComposite implements CoverAllocator {
	
	private FarmingSystem system;
	
	private FarmTerritory territory;
	
	public CoverAllocatorComposite(){}
	
	@Override
	public Set<CoverUnit> coverUnits() {
		return system.coverUnits();
	}

	@Override
	public Set<CoverGroup> coverGroups() {
		return system.coverGroups();
	}
	
	@Override
	public Set<CoverAllocationConstraint<?, ?>> getConstraints() {
		return system.getConstraints();
	}
	
	@Override
	public void addConstraint(CoverAllocationConstraint<?, ?> constraint) {
		system.addConstraint(constraint);
	}
	
	@Override
	public Set<Parcel> parcels() {
		return territory.parcels();
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
	public int[] edgesLength() {
		return territory.edgesLength();
	}

	@Override
	public Parcel parcel(String id) {
		return (Parcel) territory.get(id);
	}

	public int getDistanceFromFacilitiesToParcel(String facilities, Parcel parcel){
		return territory.getDistanceFromFacilitiesToParcel(facilities, parcel);
	}

	@Override
	public String getCode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DynamicElement getTerritory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clearParcels() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addCover(Cover c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addCovers(Collection<Cover> covers) {
		system.addCovers(covers);
	}

	@Override
	public int getArea() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public FarmingSystem getFarmingSystem() {
		return system;
	}

	@Override
	public CoverUnit getCoverUnit(String code) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clearFarmingSystem() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void checkFarmingSystem(Instant start, Instant end, boolean verbose) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isMemory() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getMemory() {
		// TODO Auto-generated method stub
		return 0;
	}

	
}
