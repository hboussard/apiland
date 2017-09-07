package fr.inra.sad.bagap.apiland.capfarm.csp;

import java.util.Map;
import java.util.Map.Entry;

import org.chocosolver.solver.constraints.ICF;

import fr.inra.sad.bagap.apiland.capfarm.model.CoverUnit;
import fr.inra.sad.bagap.apiland.capfarm.model.constraint.CoverAllocationConstraint;
import fr.inra.sad.bagap.apiland.capfarm.model.territory.Parcel;
import fr.inra.sad.bagap.apiland.core.time.Instant;

public class FixedCoverAllocationProblem extends CoverAllocationProblem {

	private Map<Parcel, CoverUnit> fixed;
	
	public FixedCoverAllocationProblem(CoverAllocator allocator, Instant t, Map<Parcel, CoverUnit> fixed) {
		super(allocator, t);
		this.fixed = fixed;
	}

	@Override
	protected void postConstraints() {
		
		//super.postConstraints();
		for(CoverAllocationConstraint<?,?> cst : allocator().getConstraints()){
			cst.setCheckOnly(true);
		}
		
		int ic;
		for(Entry<Parcel, Integer> p : parcels().entrySet()){
			
			ic = covers().get(fixed.get(p.getKey()));
			solver().post(ICF.arithm(coversAndParcels(ic, p.getValue()), "=", 1));
		}
	}
	
}
