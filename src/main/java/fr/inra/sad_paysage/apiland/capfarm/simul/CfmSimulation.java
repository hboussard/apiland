package fr.inra.sad_paysage.apiland.capfarm.simul;

import java.io.File;

import fr.inra.sad_paysage.apiland.capfarm.model.Farm;
import fr.inra.sad_paysage.apiland.capfarm.model.FixedFactory;
import fr.inra.sad_paysage.apiland.capfarm.model.HistoricFactory;
import fr.inra.sad_paysage.apiland.capfarm.model.MemoryFactory;
import fr.inra.sad_paysage.apiland.capfarm.model.territory.Parcel;
import fr.inra.sad_paysage.apiland.core.composition.DynamicAttribute;
import fr.inra.sad_paysage.apiland.core.composition.TemporalValue;
import fr.inra.sad_paysage.apiland.core.time.Future;
import fr.inra.sad_paysage.apiland.core.time.Instant;
import fr.inra.sad_paysage.apiland.simul.Scenario;
import fr.inra.sad_paysage.apiland.simul.Simulation;
import fr.inra.sad_paysage.apiland.capfarm.model.CoverUnit;

public class CfmSimulation extends Simulation {

	private static final long serialVersionUID = 1L;

	public CfmSimulation(Scenario scenario, int number) {
		super(scenario, number);
	}

	@Override
	protected void initModel(){
		for(CoverLocationModel model : (GlobalCoverLocationModel) model().get("agriculture")){
			model.getCoverAllocator().clearParcels();
			MemoryFactory.init(model, manager().start(), manager().paramProcessMode());
			HistoricFactory.init((Farm) model.getCoverAllocator(), manager().start());
			FixedFactory.init((Farm) model.getCoverAllocator(), manager().start());
		}
	}
	
	@Override
	public CfmManager manager(){
		return (CfmManager) super.manager();
	}
	
	@Override
	protected void close(){
		closeSequences();
		
		super.close();
		
		if(!isCancelled() && manager().checkConstraints()){
			for(CoverLocationModel model : (GlobalCoverLocationModel) model().get("agriculture")){
				model.getCoverAllocator().checkFarmingSystem(manager().start(), manager().end(), true);
			}
		}
		
		if(isCancelled()){
			new File(folder()).delete();
		}
	}
	
	public void closeSequences(){
		if(!isCancelled()){
			StringBuilder sb_cover;
			StringBuilder sb_year;
			String strict_seq = "";
			String seq_cover = "";
			String seq_year = "";
			int length = 0;
			CoverUnit last = null;
			for(CoverLocationModel model : (GlobalCoverLocationModel) model().get("agriculture")){
				for(Parcel p : model.getCoverAllocator().parcels()){
					// sequence en rapport aux couverts et a leurs durees
					sb_cover = new StringBuilder();
					for(TemporalValue<CoverUnit> tv : ((DynamicAttribute<CoverUnit>) p.getAttribute("cover")).getDynamics()){
						last = tv.getValue();
						if(tv.getTime().end() instanceof Future){
							length = manager().end().year() + 1 - tv.getTime().start().year();
						}else{
							length = tv.getTime().end().year()-tv.getTime().start().year();
						}
						
						if(length > 1){
							sb_cover.append(tv.getValue()+"("+length+") - ");
						}else{
							sb_cover.append(tv.getValue()+" - ");
						}
					}
					//System.out.println(((DynamicAttribute<CoverUnit>) p.getAttribute("cover")).getDynamics().size());
					sb_cover.delete(sb_cover.length()-3, sb_cover.length());
					seq_cover = sb_cover.toString();
					
					// sequence stricte en rapport aux couverts et a leurs durees
					sb_cover = new StringBuilder();
					for(TemporalValue<CoverUnit> tv : ((DynamicAttribute<CoverUnit>) p.getAttribute("cover")).getDynamics().cut(manager().start(), manager().end())){
						last = tv.getValue();
						if(tv.getTime().end() instanceof Future){
							length = manager().end().year() + 1 - tv.getTime().start().year();
						}else{
							length = tv.getTime().end().year()-tv.getTime().start().year();
						}
						
						if(length > 1){
							sb_cover.append(tv.getValue()+"("+length+") - ");
						}else{
							sb_cover.append(tv.getValue()+" - ");
						}
					}
					sb_cover.delete(sb_cover.length()-3, sb_cover.length());
					strict_seq = sb_cover.toString();
					
					// sequence en rapport aux annees
					sb_year = new StringBuilder();
					Instant t = manager().start();
					while(t.isBefore(manager().end()) || t.equals(manager().end())){
						sb_year.append(p.getAttribute("cover").getValue(t)+" - ");
						t = manager().delay().next(t);
					}
					sb_year.delete(sb_year.length()-3, sb_year.length());
					seq_year = sb_year.toString();
					
					p.getAttribute("strict_seq").setValue(null, strict_seq);
					p.getAttribute("seq_cover").setValue(null, seq_cover);
					p.getAttribute("seq_year").setValue(null, seq_year);
					p.getAttribute("length").setValue(null, length);
					p.getAttribute("repet").setValue(null, ((DynamicAttribute<CoverUnit>) p.getAttribute("cover")).getCountOfLastValue(last));
				}
			}
		}else{
			//check(manager().start().year(), manager().end().year());
		}
		//check(manager().start().year(), manager().end().year());
	}
	
}
