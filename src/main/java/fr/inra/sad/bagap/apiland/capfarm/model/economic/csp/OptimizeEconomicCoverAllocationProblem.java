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
	
	private IntVar profit, unprofit/*, work*/;
	
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
	
		profit = VF.bounded("profit", 0, 2000000000, solver); 
		unprofit = VF.bounded("unprofit", -2000000000, 0, solver);
		
		//work = VF.bounded("work", 0, 999999999, solver);
		
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
		/*
		Set<Integer> codes = new HashSet<Integer>();
		if(allocator().getCode().equalsIgnoreCase("O1")){
			codes.add(179);
			codes.add(191);
			codes.add(209);
			codes.add(220);
			codes.add(247);
			codes.add(268);
			codes.add(270);
			codes.add(271);
			codes.add(272);
			codes.add(275);
			codes.add(278);
			codes.add(280);
			codes.add(281);
			codes.add(282);
			codes.add(283);
			codes.add(286);
			codes.add(300);
			codes.add(301);
			codes.add(304);
			codes.add(305);
			codes.add(307);
			codes.add(308);
			codes.add(309);
			codes.add(310);
			codes.add(311);
			codes.add(325);
			codes.add(326);
			codes.add(327);
			codes.add(354);
		}else if(allocator().getCode().equalsIgnoreCase("O2")){
			codes.add(184);
			codes.add(187);
			codes.add(215);
			codes.add(217);
			codes.add(223);
			codes.add(231);
			codes.add(240);
			codes.add(241);
			codes.add(269);
			codes.add(279);
			codes.add(298);
			codes.add(319);
			codes.add(323);
		}else if(allocator().getCode().equalsIgnoreCase("O3")){
			codes.add(142);
			codes.add(154);
			codes.add(166);
			codes.add(167);
			codes.add(170);
			codes.add(172);
			codes.add(175);
			codes.add(178);
			codes.add(181);
			codes.add(182);
			codes.add(186);
			codes.add(194);
			codes.add(197);
			codes.add(203);
			codes.add(248);
			codes.add(249);
			codes.add(254);
			codes.add(255);
			codes.add(290);
			codes.add(362);
		}else if(allocator().getCode().equalsIgnoreCase("O4")){
			codes.add(176);
			codes.add(185);
			codes.add(188);
			codes.add(189);
			codes.add(193);
			codes.add(200);
			codes.add(201);
			codes.add(205);
			codes.add(210);
			codes.add(212);
			codes.add(230);
			codes.add(322);
			codes.add(324);
		}else if(allocator().getCode().equalsIgnoreCase("O5")){
			codes.add(139);
			codes.add(141);
			codes.add(144);
			codes.add(146);
			codes.add(147);
			codes.add(152);
			codes.add(156);
			codes.add(157);
			codes.add(158);
			codes.add(174);
			codes.add(321);
		}else if(allocator().getCode().equalsIgnoreCase("O6")){
			codes.add(183);
			codes.add(195);
			codes.add(213);
			codes.add(214);
			codes.add(221);
			codes.add(239);
			codes.add(243);
			codes.add(264);
			codes.add(265);
			codes.add(314);
			codes.add(315);
		}else if(allocator().getCode().equalsIgnoreCase("O7")){
			codes.add(229);
			codes.add(234);
			codes.add(235);
			codes.add(236);
			codes.add(237);
			codes.add(238);
			codes.add(242);
			codes.add(244);
			codes.add(245);
			codes.add(316);
		}else if(allocator().getCode().equalsIgnoreCase("O8")){
			codes.add(224);
			codes.add(226);
			codes.add(233);
			codes.add(317);
		}else if(allocator().getCode().equalsIgnoreCase("O9")){
			codes.add(162);
			codes.add(169);
			codes.add(171);
			codes.add(250);
			codes.add(251);
			codes.add(266);
			codes.add(267);
			codes.add(273);
			codes.add(313);
		}else if(allocator().getCode().equalsIgnoreCase("O10")){
			codes.add(140);
			codes.add(149);
			codes.add(150);
			codes.add(153);
			codes.add(160);
			codes.add(164);
		}else{
			System.out.println(allocator().getCode()+" inexistant");
		}
		*/
		
		for(Parcel p : parcels().keySet()){
			/*
			if(codes.contains(Integer.parseInt(p.getId()))){
				areas[parcels().get(p)] = p.getArea()/100;  // surface en ares
			}else{
				areas[parcels().get(p)] = 0;  // surface en ares
			}
			*/
			areas[parcels().get(p)] = p.getArea()/100;  // surface en ares
			//areas[parcels().get(p)] = p.getArea();  // surface en m²
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
			
		//solver().post(ICF.scalar(coverCounts, ep.works(), work));
	}
	
	@Override
	protected void configureSearch() {
		long r = new Double(Math.random() * 1000000000.0).longValue();
		
		AbstractStrategy<?> as1 = ISF.random(ArrayUtils.append(coversAndParcels), r);
		AbstractStrategy<?> as2 = ISF.random(parcelsImplantedCoverContinue, r);
		solver.set(ISF.lastConflict(solver, new StrategiesSequencer(as1, as2)));

		//SMF.limitFail(solver, 100000);
		SMF.limitTime(solver, 30000);
	}
	
	@Override
	protected boolean solve() {
		int ip, ic;
				
		solver.findOptimalSolution(ResolutionPolicy.MINIMIZE, profit);
		//solver.findParetoFront(ResolutionPolicy.MINIMIZE, unprofit/*, work*/);
		//solver.findOptimalSolution(ResolutionPolicy.MAXIMIZE, profit);
		if(solver.findSolution()){
			try {
				solver.restoreLastSolution();
				//System.out.println(time().year());
				//System.out.println("profit = "+profit.getValue());
				allocator().getTerritory().getAttribute("profit").setValue(time(), profit.getValue());
				//System.out.println("travail = "+work.getValue());
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
		}
		return false;
	}
	
}
