package fr.inra.sad.bagap.apiland.capfarm.model.economic.csp;

import org.chocosolver.solver.ResolutionPolicy;
import org.chocosolver.solver.constraints.ICF;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.search.loop.monitors.SMF;
import org.chocosolver.solver.search.strategy.ISF;
import org.chocosolver.solver.search.strategy.strategy.AbstractStrategy;
import org.chocosolver.solver.search.strategy.strategy.StrategiesSequencer;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.VF;
import org.chocosolver.util.tools.ArrayUtils;

import fr.inra.sad.bagap.apiland.capfarm.csp.CoverAllocationProblem;
import fr.inra.sad.bagap.apiland.capfarm.csp.CoverAllocator;
import fr.inra.sad.bagap.apiland.capfarm.model.Cover;
import fr.inra.sad.bagap.apiland.capfarm.model.CoverUnit;
import fr.inra.sad.bagap.apiland.capfarm.model.economic.EconomicProfil;
import fr.inra.sad.bagap.apiland.capfarm.model.territory.Parcel;
import fr.inra.sad.bagap.apiland.core.time.Instant;

public class OptimizeEconomicCoverAllocationProblem extends CoverAllocationProblem {

	private EconomicProfil ep;
	
	private IntVar profit, unprofit, work;
	
	private IntVar[] coverAreas, coverCounts;
	
	public OptimizeEconomicCoverAllocationProblem(CoverAllocator allocator, Instant t, EconomicProfil ep) {
		super(allocator, t);
		this.ep = ep;
	}

	public EconomicProfil getEconomicProfil(){
		return ep;
	}
	
	public IntVar[] coverAreas(){
		return coverAreas;
	}
	
	@Override
	protected void buildVariables(){
		
		super.buildVariables();
	
		profit = VF.bounded("profit", 0, 999999999, solver); 
		unprofit = VF.bounded("unprofit", -999999999, 0, solver);
		
		work = VF.bounded("work", 0, 999999999, solver);
		
		coverAreas = new IntVar[covers().size()];
		coverCounts = new IntVar[covers().size()];
		
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
			coverCounts[ic] = VF.bounded("c_cvs_"+ic, 0, 100, solver());
			solver().post(ICF.scalar(coversAndParcels(ic), counts, coverCounts[ic]));
		}
			
		solver().post(ICF.arithm(profit, "+", unprofit, "=", 0));
		solver().post(ICF.scalar(coverAreas, ep.profits(), profit));
			
		solver().post(ICF.scalar(coverCounts, ep.works(), work));
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
				
		solver.findParetoFront(ResolutionPolicy.MINIMIZE, unprofit, work);
		//solver.findOptimalSolution(ResolutionPolicy.MAXIMIZE, profit);
		try {
			solver.restoreLastSolution();
			System.out.println(time().year());
			System.out.println("profit = "+profit.getValue());
			System.out.println("travail = "+work.getValue());
			//int[] areas = new int[coverAreas.length];
			//int index = 0;
			//for(CoverUnit c : covers().keySet()){
			//	int it = covers().get(c);
			//	System.out.println(c+" "+coverAreas[it].getValue()+" "+coverCounts[it].getValue());
			//	areas[index++] = coverAreas[it].getValue();
			//}
			//ep.display(areas);
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
		} catch (ContradictionException e) {
			e.printStackTrace();
		}
		
		return false;
	}


	
	
}
