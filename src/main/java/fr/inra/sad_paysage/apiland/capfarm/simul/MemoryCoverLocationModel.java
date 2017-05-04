package fr.inra.sad_paysage.apiland.capfarm.simul;

import java.util.Map;
import java.util.TreeMap;

import fr.inra.sad_paysage.apiland.capfarm.model.Farm;
import fr.inra.sad_paysage.apiland.capfarm.model.territory.Parcel;
import fr.inra.sad_paysage.apiland.core.composition.DynamicAttribute;
import fr.inra.sad_paysage.apiland.core.composition.DynamicAttributeType;
import fr.inra.sad_paysage.apiland.core.time.Instant;
import fr.inra.sad_paysage.apiland.capfarm.model.CoverUnit;

public class MemoryCoverLocationModel extends CoverLocationModel {

	private static final long serialVersionUID = 1L;
	
	private Map<String, DynamicAttribute<CoverUnit>> covers;

	public MemoryCoverLocationModel(CfmSimulator simulator, Farm farm) {
		super(simulator, farm);
		covers = new TreeMap<String, DynamicAttribute<CoverUnit>>();
	}

	@Override
	public boolean make(Instant t) {
		for(Parcel p : getCoverAllocator().parcels()){
			if(covers.get(p.getId()).getActive(t).getTime().start().equals(t)){
				p.getAttribute("cover").setValue(t, covers.get(p.getId()).getValue(t));
			}
		}
		return true;
	}
	
	public void initParcel(String id, DynamicAttributeType type){
		covers.put(id, new DynamicAttribute<CoverUnit>(type));
	}

	public void setCover(String parcel, CoverUnit cover, Instant year) {
		covers.get(parcel).setValue(year, cover);
	}

}
