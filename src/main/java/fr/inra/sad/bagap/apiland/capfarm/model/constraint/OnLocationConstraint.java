package fr.inra.sad.bagap.apiland.capfarm.model.constraint;

import java.util.Set;
import org.chocosolver.solver.constraints.ICF;

import fr.inra.sad.bagap.apiland.capfarm.csp.CoverAllocationProblem;
import fr.inra.sad.bagap.apiland.capfarm.model.Cover;
import fr.inra.sad.bagap.apiland.capfarm.model.CoverUnit;
import fr.inra.sad.bagap.apiland.capfarm.model.territory.Parcel;
import fr.inra.sad.bagap.apiland.core.time.Instant;
import fr.inra.sad.bagap.apiland.core.time.delay.Delay;
import fr.inra.sad.bagap.apiland.core.time.delay.YearDelay;

public class OnLocationConstraint extends CoverAllocationConstraint<Integer, Integer> {

	private static final long serialVersionUID = 1L;

	public OnLocationConstraint(String code, boolean checkOnly, ConstraintMode mode, Set<Cover> covers, Set<Parcel> parcels) {
		super(code, checkOnly, ConstraintType.OnLocation, mode, covers, parcels, null);
	}

	@Override
	public void post(CoverAllocationProblem cap){
		
		switch(mode()){
		case ONLY : // les couverts sélectionnés sont seulement sur ces parcelles
					// = les couverts sélectionnés ne peuvent pas être ailleurs
			for(Parcel p : cap.allocator().parcels()){
				if(!location().contains(p)){
					int ip = cap.parcels().get(p);
					for(CoverUnit c : covers()){
						int ic = cap.covers().get(c);
						cap.solver().post(ICF.arithm(cap.coversAndParcels(ic, ip), "=", 0));
					}
				}
			}
			break;
		case NEVER : // les couverts sélectionnés ne sont jamais sur ces parcelles
			for(Parcel p : location()){
				int ip = cap.parcels().get(p);
				for(CoverUnit c : covers()){
					int ic = cap.covers().get(c);
					cap.solver().post(ICF.arithm(cap.coversAndParcels(ic, ip), "=", 0));
				}
			}
			break;
		case ALWAYS : // les couverts sélectionnés sont les seuls sur ces parcelles
					// = les autres couverts ne sont pas sur ces parcelles
			for(Parcel p : location()){
				int ip = cap.parcels().get(p);
				for(CoverUnit cu : cap.allocator().coverUnits()){
					if(!covers().contains(cu)){
						int ic = cap.covers().get(cu);
						cap.solver().post(ICF.arithm(cap.coversAndParcels(ic, ip), "=", 0));
					}
				}
			}
			break;
		case AROUND : // les couverts sélectionnés sont partout à l'extérieur de ces parcelles
					// = les autres couverts ne sont pas sur les autres parcelles
			for(Parcel p : cap.allocator().parcels()){
				if(!location().contains(p)){
					int ip = cap.parcels().get(p);
					for(CoverUnit cu : cap.allocator().coverUnits()){
						if(!covers().contains(cu)){
							int ic = cap.covers().get(cu);
							cap.solver().post(ICF.arithm(cap.coversAndParcels(ic, ip), "=", 0));
						}
					}
				}
			}
			
			break;
		default : throw new IllegalArgumentException("mode "+mode()+" is not supported for constraint type "+type());	
		}
	}

	@Override
	public boolean check(Instant start, Instant end, boolean verbose){
		boolean ok = true;
		StringBuilder sb = new StringBuilder();
		sb.append("on location ");	
		if(verbose){
			if(ok){
				sb.append("GOOD : cover "+covers().toString()+" is "+mode()+" on "+domain());
			}
			System.out.println(sb.toString());
		}
		return ok;
	}
}
