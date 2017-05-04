package fr.inra.sad_paysage.apiland.capfarm.model;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import fr.inra.sad_paysage.apiland.capfarm.model.constraint.CoverAllocationConstraint;

public class FarmingSystem implements Covering {

	private Farm farm;
	
	private Set<Cover> covers;
	
	private Set<CoverAllocationConstraint<?,?>> constraints;
	
	private GenericFarmingSystem generic;
	
	public FarmingSystem(Farm farm){
		this.farm = farm;
		this.covers = new TreeSet<Cover>();
		this.constraints = new TreeSet<CoverAllocationConstraint<?,?>>();
		this.generic = new GenericFarmingSystem("unknown_"+farm.getCode());
	}

	public String toString(){
		return generic.toString();
	}
	
	public Farm getFarm() {
		return farm;
	}

	public GenericFarmingSystem getGenericFarmingSystem(){
		return generic;
	}
	
	public void setGenericFarmingSystem(GenericFarmingSystem generic){
		this.generic = generic;
	}
	
	@Override
	public void addCover(Cover c){
		covers.add(c);
		generic.addCover(c);
	}
	
	@Override
	public void addCovers(Collection<Cover> covers) {
		this.covers.addAll(covers);
		generic.addCovers(covers);
	}
	
	public Set<Cover> getCovers() {
		return covers;
	}

	public Set<CoverUnit> coverUnits(){
		Set<CoverUnit> cov = new TreeSet<CoverUnit>();
		for(Cover c : covers){
			if(c instanceof CoverUnit){
				cov.add((CoverUnit) c);
			}
		}
		return cov;
	}
	
	public CoverUnit getCoverUnit(String code) {
		for(Cover c : covers){
			if(c instanceof CoverUnit && c.getCode().equalsIgnoreCase(code)){
				return (CoverUnit) c;
			}
		}
		throw new IllegalArgumentException("no cover "+code);
	}
	
	public Set<CoverGroup> coverGroups(){
		Set<CoverGroup> cov = new TreeSet<CoverGroup>();
		for(Cover c : covers){
			if(c instanceof CoverGroup){
				cov.add((CoverGroup) c);
			}
		}
		return cov;
	}
	
	public int coverUnitsCount(){
		return coverUnits().size();
	}
	
	public int coverGroupsCount(){
		return coverGroups().size();
	}
	
	public Set<CoverAllocationConstraint<?,?>> getConstraints() {
		return constraints;
	}
	
	public void addConstraint(CoverAllocationConstraint<?,?> constraint){
		constraints.add(constraint);
	}
	
	public void display(){
		for(CoverAllocationConstraint<?,?> fc : constraints){
			System.out.println(fc);
		}
	}
	
	public CoverAllocationConstraint<?,?> getConstraint(String code){
		for(CoverAllocationConstraint<?,?> c : constraints){
			if(c.code().equalsIgnoreCase(code)){
				return c;
			}
		}
		return null; 
	}

}
