package fr.inra.sad.bagap.apiland.capfarm.model.economic.csp;

import org.chocosolver.solver.constraints.ICF;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.search.loop.monitors.SMF;
import org.chocosolver.solver.search.strategy.ISF;
import org.chocosolver.solver.search.strategy.strategy.AbstractStrategy;
import org.chocosolver.solver.search.strategy.strategy.StrategiesSequencer;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.VF;
import org.chocosolver.solver.variables.Variable;
import org.chocosolver.util.tools.ArrayUtils;

import fr.inra.sad.bagap.apiland.capfarm.csp.CoverAllocationProblem;
import fr.inra.sad.bagap.apiland.capfarm.csp.CoverAllocator;
import fr.inra.sad.bagap.apiland.capfarm.model.Cover;
import fr.inra.sad.bagap.apiland.capfarm.model.CoverUnit;
import fr.inra.sad.bagap.apiland.capfarm.model.economic.EconomicProfil;
import fr.inra.sad.bagap.apiland.capfarm.model.territory.Parcel;
import fr.inra.sad.bagap.apiland.core.time.Instant;

public class EconomicCoverAllocationProblem extends CoverAllocationProblem {
	
	private IntVar[] coverAreas;
	
	private EconomicProfil economiProfil;
	
	public EconomicCoverAllocationProblem(CoverAllocator allocator, Instant t, EconomicProfil ep) {
		super(allocator, t);
		this.economiProfil = ep;
	}
	
	public EconomicProfil getEconomicProfil(){
		return economiProfil;
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
			//System.out.println(p.getArea()+" "+p.getArea()/100);
			areas[parcels().get(p)] = p.getArea()/100;  // surface en ares
			//areas[parcels().get(p)] = p.getArea();  // surface en m²
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

		//SMF.limitFail(solver, 1000);
		SMF.limitTime(solver, 100);
	}
	
	@Override
	protected boolean solve() {
		if(solver.findSolution()) {
			for(Variable v : solver.getVars()){
				if(v.getName().equalsIgnoreCase("profit")){
					allocator().getTerritory().getAttribute("profit").setValue(time(), ((IntVar) v).getValue());
					//System.out.println("profit = "+((IntVar) v).getValue());
				}
			}
			for(Parcel p : parcels().keySet()){
				int ip = parcels().get(p);
				if(((IntVar) parcelsImplantedCoverContinue[ip]).getValue() == 0){
					for(Cover c : covers().keySet()){
						int ic = covers().get(c);	
						if(((IntVar) coversAndParcels[ic][ip]).getValue() == 1){
							p.getAttribute("cover").setValue(time(), c);
							//System.out.println(p.getId()+";"+c+";"+p.getArea());
							break;
						}
					}
				}
			}
			//System.out.println(((IntVar) solver.profit).getValue());
			return true;
		}
		return false;
	}
	
}
