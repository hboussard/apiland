/*Copyright 2010 by INRA SAD-Paysage (Rennes)

Author: Hugues BOUSSARD 
email : hugues.boussard@rennes.inra.fr

This library is a JAVA toolbox made to create and manage dynamic landscapes.

This software is governed by the CeCILL-C license under French law and
abiding by the rules of distribution of free software.  You can  use,
modify and/ or redistribute the software under the terms of the CeCILL-C
license as circulated by CEA, CNRS and INRIA at the following URL
"http://www.cecill.info".

As a counterpart to the access to the source code and rights to copy,
modify and redistribute granted by the license, users are provided only
with a limited warranty  and the software's author,  the holder of the
economic rights,  and the successive licensors  have only  limited
liability.

In this respect, the user's attention is drawn to the risks associated
with loading,  using,  modifying and/or developing or reproducing the
software by the user in light of its specific status of free software,
that may mean  that it is complicated to manipulate, and that also
therefore means  that it is reserved for developers and experienced
professionals having in-depth computer knowledge. Users are therefore
encouraged to load and test the software's suitability as regards their
requirements in conditions enabling the security of their systems and/or
data to be ensured and,  more generally, to use and operate it in the
same conditions as regards security.

The fact that you are presently reading this means that you have had
knowledge of the CeCILL-C license and that you accept its terms.
*/
package fr.inra.sad.bagap.apiland.simul;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import fr.inra.sad.bagap.apiland.core.time.Instant;
import fr.inra.sad.bagap.apiland.core.time.Time;
import fr.inra.sad.bagap.apiland.core.time.delay.DayDelay;
import fr.inra.sad.bagap.apiland.core.time.delay.Delay;
import fr.inra.sad.bagap.apiland.core.time.delay.MonthDelay;
import fr.inra.sad.bagap.apiland.core.time.delay.YearDelay;

public class SimulationManager implements Serializable{

	private static final long serialVersionUID = 1L;

	/** start of simulation */
	private Instant start;
	
	/** end of simulation */
	private Instant end;
	
	/** delay between 2 simulation times */
	private Delay delay; 
	
	/** count of simulations, default = 1 */
	private int simulations = 1; 
	
	/** count of scenarios, default = 1 */
	private int scenarios = 1; 
	
	/**
	 * scenario number
	 */
	private int number;
	
	/** count of good endeed simulations, default = 0*/
	private int success = 0;
	
	/** work path */
	private String path; 
	
	/** experience folder */
	protected String expFolder; 
	
	///** output variables manager */
	//private transient OutputManager outputM;
	
	/** simulation process */
	private transient SimulatorProcess sp;
	
	private transient boolean cancelled = false; 
	
	private transient boolean console = true;
	
	public SimulationManager(int s){
		this.number = s;
		//outputM = new OutputManager();
	}
	
	public void init(String propertiesFile){
		try{
			Properties properties = new Properties();
			FileInputStream in = new FileInputStream(propertiesFile);
			properties.load(in);
			in.close();
			
			expFolder = new File(propertiesFile).getParent();
			
			if(properties.containsKey("start")){
				start = Time.get(properties.getProperty("start"));
			}
			
			if(properties.containsKey("end")){
				end = Time.get(properties.getProperty("end"));
			}
			
			if(properties.containsKey("delayType")){
				String delayType = properties.getProperty("delayType");
				
				if(delayType.equalsIgnoreCase("day")){
					delay = new DayDelay(new Integer(properties.getProperty("delay")));
				}else if(delayType.equalsIgnoreCase("month")){
					delay = new MonthDelay(new Integer(properties.getProperty("delay")));
				}else if(delayType.equalsIgnoreCase("year")){
					delay = new YearDelay(new Integer(properties.getProperty("delay")));
				}
			}
			
			if(properties.containsKey("simulations")){
				simulations = new Integer(properties.getProperty("simulations"));
			}
				
			if(properties.containsKey("scenarios")){
				scenarios = new Integer(properties.getProperty("scenarios"));
			}
			
			if(properties.containsKey("path")){
				path = properties.getProperty("path");
			}
				
		}catch(FileNotFoundException ex){
			ex.printStackTrace();
		}catch(IOException ex){
			ex.printStackTrace();
		}
	}

	/*
	public OutputManager output(){
		return outputM;
	}
	*/
	
	public int number(){
		return number;
	}
	
	public Instant start() {
		return start;
	}

	public Instant end() {
		return end;
	}

	public Delay delay() {
		return delay;
	}

	public int simulations() {
		return simulations;
	}

	public int scenarios() {
		return scenarios;
	}

	public String path() {
		return path;
	}
	
	public String expFolder() {
		return expFolder;
	}

	public void setSimulations(int s){
		this.simulations = s;
	}
	
	public void setScenarios(int s){
		this.scenarios = s;
	}
	
	public void setSuccess(int s){
		this.success = s;
	}
	
	public int success(){
		return success;
	}
	
	public void setSettings(String key, String value){
	
		if(key.equalsIgnoreCase("start")){
			start = Time.get(value);
			return;
		}
		if(key.equalsIgnoreCase("end")){
			end = Time.get(value);
			return;
		}
		if(key.equalsIgnoreCase("delay")){
			delay = new DayDelay(new Integer(value));
			return;
		}
		if(key.equalsIgnoreCase("simulations")){
			simulations = new Integer(value);
			return;
		}
		if(key.equalsIgnoreCase("scenarios")){
			scenarios = new Integer(value);
			return;
		}
		if(key.equalsIgnoreCase("path")){
			path = value;
			return;
		}
		if(key.equalsIgnoreCase("expFolder")){
			expFolder = value;
			return;
		}
	}
	
	public void setSimulatorProcess(SimulatorProcess sp){
		this.sp = sp;
	}
	
	public void setConsole(boolean console){
		this.console = console;
	}
	
	public void display(String text){
		if(console){
			System.out.println(text);
		}
		if(sp != null){
			sp.display(text);
		}
	}
	
	public void up(int progress){
		if(sp != null){
			sp.up(progress);
		}
	}
	
	public void setCancelled(boolean cancel){
		this.cancelled = cancel;
	}
	
	public boolean isCancelled(){
		if(sp != null){
			return sp.isCancelled();
		}
		return cancelled;
	}
	
	public List<Integer> years(){
		List<Integer> years = new ArrayList<Integer>();
		int y = Time.getYear(start);
		while(y <= Time.getYear(end)){
			years.add(y++);
		}
		return years;
	}
	
	public void setPath(String path){
		this.path = path;
	}

	public void setStart(Instant t){
		this.start = t;
	}
	
	public void setEnd(Instant t){
		this.end = t;
	}
	
	public void setTime(Instant start, Instant end){
		setStart(start);
		setEnd(end);
	}
	
	public void setDelay(Delay d){
		this.delay = d;
	}
	
}
