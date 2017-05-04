package fr.inra.sad_paysage.apiland.capfarm.model.economic.csp;

import org.chocosolver.solver.constraints.ICF;
import org.chocosolver.solver.search.loop.monitors.SMF;
import org.chocosolver.solver.search.strategy.ISF;
import org.chocosolver.solver.search.strategy.strategy.AbstractStrategy;
import org.chocosolver.solver.search.strategy.strategy.StrategiesSequencer;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.VF;
import org.chocosolver.util.tools.ArrayUtils;

import fr.inra.sad_paysage.apiland.capfarm.csp.CoverAllocationProblem;
import fr.inra.sad_paysage.apiland.capfarm.csp.CoverAllocator;
import fr.inra.sad_paysage.apiland.capfarm.model.territory.Parcel;
import fr.inra.sad_paysage.apiland.core.time.Instant;
import fr.inra.sad_paysage.apiland.capfarm.model.Cover;
import fr.inra.sad_paysage.apiland.capfarm.model.CoverUnit;

public class EconomicCoverAllocationProblem extends CoverAllocationProblem {
	
	private IntVar[] coverAreas;
	
	public EconomicCoverAllocationProblem(CoverAllocator allocator, Instant t) {
		super(allocator, t);
	}
	
	public IntVar[] coverAreas(){
		return coverAreas;
	}
	
	@Override
	protected void buildVariables(){
		
		super.buildVariables();
		
		coverAreas = new IntVar[covers().size()];
	}
	
	@Override
	protected void structureInitialisation(){
		
		super.structureInitialisation();
		
		int totarea = allocator().totalParcelsArea();
		int[] areas = new int[allocator().parcels().size()];
		int[] counts = new int[allocator().parcels().size()];
		for(int i=0; i<areas.length; i++){
			areas[i] = 0;
			counts[i] = 1;
		}
		for(Parcel p : parcels().keySet()){
			areas[parcels().get(p)] = p.getArea()/100;  // surface en ares
		}
					
		for(CoverUnit c : covers().keySet()){
			int ic = covers().get(c);
			coverAreas[ic] = VF.bounded("a_cvs_"+ic, 0, totarea, solver());
			solver().post(ICF.scalar(coversAndParcels(ic), areas, coverAreas[ic])); 
		}
		
	}
	
	@Override
	protected void configureSearch() {
		long r = new Double(Math.random() * 1000000000.0).longValue();
		
		AbstractStrategy<?> as1 = ISF.random(ArrayUtils.append(coversAndParcels), r);
		AbstractStrategy<?> as2 = ISF.random(parcelsImplantedCoverContinue, r);
		solver.set(ISF.lastConflict(solver, new StrategiesSequencer(as1, as2)));

		SMF.limitFail(solver, 1000);
	}
	
	@Override
	protected boolean solve() {
		int ip, ic;
		
		if(solver.findSolution()) {
			for(Parcel p : parcels().keySet()){
				ip = parcels().get(p);
				if(((IntVar) parcelsImplantedCoverContinue[ip]).getValue() == 0){
					for(Cover c : covers().keySet()){
						ic = covers().get(c);	
						if(((IntVar) coversAndParcels(ic, ip)).getValue() == 1){
							p.getAttribute("cover").setValue(time(), c);
							//p.getAttribute("cov").setValue(t, c.getCode());
							break;
						}
					}
				}
			}
			return true;
		}
		
		return false;
	}
	
}
