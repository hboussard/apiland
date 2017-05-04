package fr.inra.sad_paysage.apiland.capfarm.model.economic.constraint;

import java.util.Set;

import org.chocosolver.solver.constraints.ICF;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.VF;

import fr.inra.sad_paysage.apiland.capfarm.csp.CoverAllocationProblem;
import fr.inra.sad_paysage.apiland.capfarm.model.constraint.ConstraintMode;
import fr.inra.sad_paysage.apiland.capfarm.model.constraint.ConstraintType;
import fr.inra.sad_paysage.apiland.capfarm.model.constraint.CoverAllocationConstraint;
import fr.inra.sad_paysage.apiland.capfarm.model.domain.Domain;
import fr.inra.sad_paysage.apiland.capfarm.model.economic.EconomicProfil;
import fr.inra.sad_paysage.apiland.capfarm.model.economic.csp.EconomicCoverAllocationProblem;
import fr.inra.sad_paysage.apiland.capfarm.model.territory.Parcel;
import fr.inra.sad_paysage.apiland.core.time.Instant;
import fr.inra.sad_paysage.apiland.core.time.delay.Delay;
import fr.inra.sad_paysage.apiland.core.time.delay.YearDelay;
import fr.inra.sad_paysage.apiland.capfarm.model.Cover;
import fr.inra.sad_paysage.apiland.capfarm.model.CoverUnit;

public class ProfitConstraint extends CoverAllocationConstraint<Integer, Integer>{

	private static final long serialVersionUID = 1L;
	
	private EconomicProfil ep;
	
	public ProfitConstraint(String code, boolean checkOnly, ConstraintMode mode, Set<Cover> covers, Set<Parcel> parcels, Domain<Integer, Integer> domain, EconomicProfil ep) {
		super(code, checkOnly, ConstraintType.Profit, mode, covers, parcels, domain);
		this.ep = ep;
	}
	
	@Override
	public void post(CoverAllocationProblem cap) {
		
		EconomicCoverAllocationProblem ecap = (EconomicCoverAllocationProblem) cap;
		
		IntVar profit = VF.bounded("profit_"+code(), 0, 2000000000, ecap.solver()); 
		
		ecap.solver().post(ICF.scalar(ecap.coverAreas(), ep.profits(covers()), profit));
	
		switch(mode()){
		case ONLY :
			post(ecap, domain(), profit);
			break;
		case NEVER : 
			post(ecap, domain().inverse(), profit);
			break;
		default : throw new IllegalArgumentException("mode "+mode()+" is not supported for constraint type "+type());
		}
	}
	
	private void post(CoverAllocationProblem cap, Domain<Integer, Integer> domain, IntVar profit){
		cap.solver().post(domain.postIntVar(profit));
	}

	@Override
	public boolean check(Instant start, Instant end, boolean verbose){
		boolean ok = true;
		StringBuilder sb = new StringBuilder();
		double supermin = Integer.MAX_VALUE;
		double supermax = Integer.MIN_VALUE;
		Delay d = new YearDelay(1);
		CoverUnit c;
		for(Instant t=start; t.isBefore(end) || t.equals(end); t=d.next(t)){
			double profit = 0;
			for(Parcel p : location()){
				c = (CoverUnit) p.getAttribute("cover").getValue(t);
				if(covers().contains(c)){
					//System.out.println(c+" : "+p.getArea()+" "+p.getId()+" "+ep.profit(c, p.getArea())/100.0);
					profit += ep.profit(c, p.getArea()) / 100;
				}
			}
			
			System.out.println(t.year()+" : profit "+profit);
			supermin = Math.min(supermin, profit);
			supermax = Math.max(supermax, profit);
			
			switch(mode()){
			case ONLY :
				if(!domain().accept(new Double(profit).intValue())){
					ok = false;
					if(verbose){
						sb.append("BAD : profit  = "+profit+" not in domain "+domain()+"\n");
					}else{
						return ok;
					}
				}
				break;
			case NEVER : 
				if(domain().accept(new Double(profit).intValue())){
					ok = false;
					if(verbose){
						sb.append("BAD : profit = "+profit+" not in domain "+domain()+"\n");
					}else{
						return ok;
					}
				}
				break;
			default : 
				throw new IllegalArgumentException("mode "+mode()+" is not supported for constraint type "+type());
			}
		}
		if(verbose){
			if(ok){
				supermin /= 100;
				supermax /= 100;
				if(supermin == supermax){
					sb.append("GOOD : cover "+covers().toString()+" has profit = "+supermin);
				}else{
					sb.append("GOOD : cover "+covers().toString()+" has profit between min = "+supermin+" and max = "+supermax);
				}
			}
			System.out.println(sb.toString());
		}
		return ok;	
	}
	
	
}
