package fr.inra.sad.bagap.apiland.analysis.matrix.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class SearchAndReplaceAsciiGridCleaner implements AsciiGridCleaner {

	private Map<String, String> changes;
	
	private int index = 0;
	
	public SearchAndReplaceAsciiGridCleaner(Map<Integer, String> changes){
		this.changes = new HashMap<String, String>();
		if(changes != null){
			for(Entry<Integer, String> change : changes.entrySet()){
				this.changes.put(String.valueOf(change.getKey()), change.getValue());
			}
		}
	}
	
	@Override
	public String clean(String line) {
		index++;
		
		if(index > 6){
			StringBuffer outLine = new StringBuffer();
			for(String v : line.split(" ")){
				//System.out.println(v);
				if(changes.containsKey(v)){
					//System.out.println("true "+changes.get(v));
					outLine.append(changes.get(v)+' ');
					//outLine.append(changes.get(v)+' ');
				}else{
					//System.out.println("false");
					outLine.append(v+' ');
				}
			}
			return outLine.toString();
		}else{
			return line;
		}
	}

}
