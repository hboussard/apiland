package fr.inra.sad_paysage.apiland.treatment;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class LocalTreatmentManager implements TreatmentManager {
	
	private List<Treatment> treatments;
	
	private Map<Treatment, Map<String, Map<Treatment, String>>> links;

	public LocalTreatmentManager(){
		treatments = new LinkedList<Treatment>();
		links = new HashMap<Treatment, Map<String, Map<Treatment, String>>>();
	}
	
	@Override
	public void addTreatment(Treatment t){
		throw new UnsupportedOperationException();
	}

	protected void addTreatment(TreatmentManager manager, Treatment t){
		treatments.add(t);
		t.setTreatmentManager(manager);
	}
	
	@Override
	public void addLink(Treatment t1, String port1, Treatment t2, String port2){
		if(!t1.hasPort(port1)){
			System.err.println("warning : the port named '"+port1+"' does not exist in the treatment '"+t1+"'");
			//throw new IllegalArgumentException("name error : the port '"+port1+"' does not exist in the treatment '"+t1+"'");
		}else{
			if(!t2.hasPort(port2)){
				throw new IllegalArgumentException("name error : the port '"+port2+"' does not exist in the treatment '"+t2+"'");
			}
			if(!t1.getPortBinding(port1).isAssignableFrom(t2.getPortBinding(port2))){
				throw new IllegalArgumentException("cast error : the port '"+port1+"' of the treatment '"+t1+"' " +
						"and the port '"+port2+"' of the treatment '"+t2+"' do not have the same cast : " +
						"'"+t1.getPortBinding(port1)+"' against '"+t2.getPortBinding(port2)+"'");
			}
		}
		if(!links.containsKey(t1)){
			links.put(t1, new HashMap<String, Map<Treatment, String>>());
		}
		if(!links.get(t1).containsKey(port1)){
			links.get(t1).put(port1, new HashMap<Treatment, String>());
		}
		links.get(t1).get(port1).put(t2, port2);
	}
	
	@Override
	public void flow(Treatment t, String port, Object o){
		if(links.containsKey(t)){
			if(links.get(t).containsKey(port)){
				for(Entry<Treatment, String> e : links.get(t).get(port).entrySet()){
					System.out.println("flow de "+t+"("+port+") vers "+e.getKey()+"("+e.getValue()+") avec la valeur "+o);
					if(t.manager().equals(e.getKey())){
						e.getKey().setOutput(e.getValue(), o);
					}else{
						e.getKey().setInput(e.getValue(), o);
					}
				}
			}
		}
	}

	@Override
	public Iterator<Treatment> iterator() {
		return treatments.iterator();
	}

	@Override
	public void clear() {
		treatments.clear();
		treatments = null;
		links.clear();
		links = null;
	}
	
}
