package fr.inra.sad.bagap.apiland.analysis.matrix.process.metric;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

import com.csvreader.CsvReader;
import com.csvreader.CsvReader.CatastrophicException;
import com.csvreader.CsvReader.FinalizedException;

import fr.inra.sad.bagap.apiland.analysis.process.metric.Metric;
import fr.inra.sad.bagap.apiland.core.util.Couple;

public class MatrixMetricManager {

	private static Set<String> basicMetrics;
	
	private static Set<String> valueMetrics;
	
	private static Set<String> coupleMetrics;
	
	private static Set<String> setMetrics;
	
	//private static Set<String> qualitativeMetrics;
	
	private static Set<String> valuesMetrics;
	
	private static Set<String> couplesMetrics;
	
	private static Set<String> patchMetrics;
	
	private static Set<String> connectivityMetrics;
	
	private static Set<String> diversityMetrics;
	
	private static Set<String> grainMetrics;
	
	private static Set<String> quantitativeMetrics;
	
	private static Set<String> statsMetrics;
	
	private static Map<String, String> metrics;
	
	static {
		basicMetrics = new HashSet<String>();
		valueMetrics = new HashSet<String>();
		coupleMetrics = new HashSet<String>();
		setMetrics = new HashSet<String>();
		//qualitativeMetrics = new HashSet<String>();
		valuesMetrics = new HashSet<String>();
		couplesMetrics = new HashSet<String>();
		patchMetrics = new HashSet<String>();
		connectivityMetrics = new HashSet<String>();
		diversityMetrics = new HashSet<String>();
		grainMetrics = new HashSet<String>();
		quantitativeMetrics = new TreeSet<String>();
		statsMetrics = new TreeSet<String>();
		metrics = new HashMap<String, String>();
		CsvReader cr = null;
		try{
			//String buf = "c://Hugues/workspace/apiland-0.9.3.v4-analysis/src/fr/inra/sad_paysage/apiland/analysis/metric/metrics.csv";
			//BufferedReader buf = new BufferedReader(new InputStreamReader(MatrixMetricManager.class.getResourceAsStream("metrics.csv")));
			BufferedReader buf = new BufferedReader(new InputStreamReader(MatrixMetricManager.class.getResourceAsStream("metrics_2017.csv")));
			cr = new CsvReader(buf);
			cr.setDelimiter(';');
			cr.readHeaders();
			while(cr.readRecord()){
				if(!cr.get("name").startsWith("#")){
					metrics.put(cr.get("name"), cr.get("class"));
					if(cr.get("basic").equalsIgnoreCase("true")){
						basicMetrics.add(cr.get("name"));
					}
					switch(cr.get("process")){
					case "value" :
						valuesMetrics.add(cr.get("name"));
						break;
					case "couple" :
						couplesMetrics.add(cr.get("name"));
						break;
					case "patch" :
						patchMetrics.add(cr.get("name"));
						break;
					case "connectivity" :
						connectivityMetrics.add(cr.get("name"));
						break;
					case "diversity" :
						diversityMetrics.add(cr.get("name"));
						break;
					case "grain" :
						grainMetrics.add(cr.get("name"));
						break;
					case "quantitative" :
						quantitativeMetrics.add(cr.get("name"));
						break;
					}
					/*
					if(cr.get("process").equalsIgnoreCase("qualitative")){
						qualitativeMetrics.add(cr.get("name"));
					}else if(cr.get("process").equalsIgnoreCase("quantitative")){
						quantitativeMetrics.add(cr.get("name"));
					}*/
					if(cr.get("type").equalsIgnoreCase("value")){
						valueMetrics.add(cr.get("name"));
					}else if(cr.get("type").equalsIgnoreCase("couple")){
						coupleMetrics.add(cr.get("name"));
					}else if(cr.get("type").equalsIgnoreCase("set")){
						setMetrics.add(cr.get("name"));
					}else if(cr.get("type").equalsIgnoreCase("metric")){
						statsMetrics.add(cr.get("name"));
					}
				}
			}
			
		}catch(FinalizedException ex){
			ex.printStackTrace();
		}catch(CatastrophicException ex){
			ex.printStackTrace();
		}catch(IOException ex){
			ex.printStackTrace();
		}finally{
			cr.close();
		}
	}
	
	public static boolean isBasicMetric(String m){
		for(String metric : basicMetrics){
			if(m.startsWith(metric)){
				return true;
			}
		}
		return false;
	}
	
	private static String getBasicMetric(String m){
		for(String metric : basicMetrics){
			if(m.startsWith(metric)){
				return metric;
			}
		}
		throw new IllegalArgumentException();
	}
	
	private static boolean isSetMetric(String m){
		for(String metric : setMetrics){
			if(m.startsWith(metric)){
				return true;
			}
		}
		return false;
	}
	
	private static String getSetMetric(String m){
		for(String metric : setMetrics){
			if(m.startsWith(metric)){
				return metric;
			}
		}
		throw new IllegalArgumentException();
	}
	
	private static boolean isCoupleMetric(String m){
		for(String metric : coupleMetrics){
			if(m.startsWith(metric+"_")){
				return true;
			}
		}
		return false;
	}
	
	private static String getCoupleMetric(String m){
		for(String metric : coupleMetrics){
			if(m.startsWith(metric)){
				return metric;
			}
		}
		throw new IllegalArgumentException();
	}
	
	private static boolean isValueMetric(String m){
		for(String metric : valueMetrics){
			if(m.startsWith(metric)){
				return true;
			}
		}
		return false;
	}
	
	private static String getValueMetric(String m){
		for(String metric : valueMetrics){
			if(m.startsWith(metric)){
				return metric;
			}
		}
		throw new IllegalArgumentException();
	}
	
	public static boolean isStatsMetric(String m){
		for(String metric : statsMetrics){
			if(m.startsWith(metric)){
				return true;
			}
		}
		return false;
	}
	
	private static String getStatsMetric(String m){
		for(String metric : statsMetrics){
			if(m.startsWith(metric)){
				return metric;
			}
		}
		throw new IllegalArgumentException();
	}
	
	public static MatrixMetric get(String m, Metric refMetric){
		try {

			if(isStatsMetric(m)){
	
				String metric = getStatsMetric(m);
				Class<?> c = Class.forName(metrics.get(metric));
				return (MatrixMetric) c.getConstructor(Metric.class).newInstance(refMetric);
				
			}else{
				throw new IllegalArgumentException();
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		throw new IllegalArgumentException();
	}
	
	public static MatrixMetric get(String m){
		// redecoupage de la metrique
		//System.out.println(m);
		try {
			if(isSetMetric(m)){
				String metric = getSetMetric(m);
				Class<?> c = Class.forName(metrics.get(metric));
				Set<Integer> set = new HashSet<Integer>();
				metric = m.replace(metric+"_", "");
				StringTokenizer st = new StringTokenizer(metric, "-");
				while(st.hasMoreTokens()){
					set.add(new Integer(st.nextToken()));
				}
				return (MatrixMetric) c.getConstructor(Set.class).newInstance(set);
				
			}else if(isCoupleMetric(m)){
				
				String metric = getCoupleMetric(m);
				//System.out.println(m+" "+metric);
				Class<?> c = Class.forName(metrics.get(metric));
				metric = m.replace(metric+"_", "");
				StringTokenizer st = new StringTokenizer(metric, "-");
				int c1 = new Integer(st.nextToken());
				int c2 = new Integer(st.nextToken());
				return (MatrixMetric) c.getConstructor(Integer.class, Integer.class).newInstance(c1, c2);
				
			}else if(isValueMetric(m)){
				
				String metric = getValueMetric(m);
				Class<?> c = Class.forName(metrics.get(metric));
				metric = m.replace(metric+"_", "");
				int v = new Integer(metric);
				return (MatrixMetric) c.getConstructor(Integer.class).newInstance(v);
				
			}else if(isStatsMetric(m)){
				
				throw new IllegalArgumentException();
				
			}else{
				
				Class<?> c = Class.forName(metrics.get(m));
				return (MatrixMetric) c.getConstructor().newInstance();
				
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		throw new IllegalArgumentException();
	}
	
	public static Set<String> getMetricNames(){
		return metrics.keySet();
	}
	
	public static int getMetricSize(){
		return metrics.size();
	}
	
	public static Set<String> getStatsMetricNames(Set<String> m){
		Set<String> metrics = new TreeSet<String>();
		
		for(String sm : statsMetrics){
			for(String bm : m){
				metrics.add(sm+"_"+bm);
			}
		}
		
		return metrics;
	}
	
	public static Set<String> getGrainMetricNames(Set<Integer> values){
		Set<String> metrics = new TreeSet<String>();
		
		for(String m : grainMetrics){
			if(valueMetrics.contains(m)){
				if(values.size() < 50){
					for(int v : values){
						metrics.add(m+"_"+(int)v);
					}
				}
			}else if(coupleMetrics.contains(m)){
				if(values.size() < 250){
					for(String cou : getCouples(values)){
						metrics.add(m+"_"+cou);
					}
				}
			}else if(setMetrics.contains(m)){
				for(String comb : getCombinations(values)){
					metrics.add(m+"_"+comb);
				}
			}else{
				metrics.add(m);
			}
		}
		
		return metrics;
	}
	
	public static Set<String> getDiversityMetricNames(Set<Integer> values){
		Set<String> metrics = new TreeSet<String>();
		
		for(String m : diversityMetrics){
			if(valueMetrics.contains(m)){
				if(values.size() < 50){
					for(int v : values){
						metrics.add(m+"_"+(int)v);
					}
				}
			}else if(coupleMetrics.contains(m)){
				if(values.size() < 250){
					for(String cou : getCouples(values)){
						metrics.add(m+"_"+cou);
					}
				}
			}else if(setMetrics.contains(m)){
				for(String comb : getCombinations(values)){
					metrics.add(m+"_"+comb);
				}
			}else{
				metrics.add(m);
			}
		}
		
		return metrics;
	}
	
	public static Set<String> getConnectivityMetricNames(Set<Integer> values){
		Set<String> metrics = new TreeSet<String>();
		
		for(String m : connectivityMetrics){
			if(valueMetrics.contains(m)){
				if(values.size() < 50){
					for(int v : values){
						metrics.add(m+"_"+(int)v);
					}
				}
			}else if(coupleMetrics.contains(m)){
				if(values.size() < 250){
					for(String cou : getCouples(values)){
						metrics.add(m+"_"+cou);
					}
				}
			}else if(setMetrics.contains(m)){
				for(String comb : getCombinations(values)){
					metrics.add(m+"_"+comb);
				}
			}else{
				metrics.add(m);
			}
		}
		
		return metrics;
	}
	
	public static Set<String> getPatchMetricNames(Set<Integer> values){
		Set<String> metrics = new TreeSet<String>();
		
		for(String m : patchMetrics){
			if(valueMetrics.contains(m)){
				if(values.size() < 50){
					for(int v : values){
						metrics.add(m+"_"+(int)v);
					}
				}
			}else if(coupleMetrics.contains(m)){
				if(values.size() < 250){
					for(String cou : getCouples(values)){
						metrics.add(m+"_"+cou);
					}
				}
			}else if(setMetrics.contains(m)){
				for(String comb : getCombinations(values)){
					metrics.add(m+"_"+comb);
				}
			}else{
				metrics.add(m);
			}
		}
		
		return metrics;
	}
	
	public static Set<String> getCoupleMetricNames(Set<Integer> values){
		Set<String> metrics = new TreeSet<String>();
		
		for(String m : couplesMetrics){
			if(valueMetrics.contains(m)){
				if(values.size() < 50){
					for(int v : values){
						metrics.add(m+"_"+(int)v);
					}
				}
			}else if(coupleMetrics.contains(m)){
				if(values.size() < 250){
					for(String cou : getCouples(values)){
						metrics.add(m+"_"+cou);
					}
				}
			}else if(setMetrics.contains(m)){
				for(String comb : getCombinations(values)){
					metrics.add(m+"_"+comb);
				}
			}else{
				metrics.add(m);
			}
		}
		
		return metrics;
	}
	
	public static Set<String> getValueMetricNames(Set<Integer> values){
		Set<String> metrics = new TreeSet<String>();
		
		for(String m : valuesMetrics){
			if(valueMetrics.contains(m)){
				if(values.size() < 50){
					for(int v : values){
						metrics.add(m+"_"+(int)v);
					}
				}
			}else if(coupleMetrics.contains(m)){
				if(values.size() < 250){
					for(String cou : getCouples(values)){
						metrics.add(m+"_"+cou);
					}
				}
			}else if(setMetrics.contains(m)){
				for(String comb : getCombinations(values)){
					metrics.add(m+"_"+comb);
				}
			}else{
				metrics.add(m);
			}
		}
		
		return metrics;
	}
	
	/*
	public static Set<String> getQualitativeMetricNames(Set<Integer> values){
		Set<String> metrics = new TreeSet<String>();
		
		for(String m : qualitativeMetrics){
			if(valueMetrics.contains(m)){
				if(values.size() < 50){
					for(int v : values){
						metrics.add(m+"_"+(int)v);
					}
				}
			}else if(coupleMetrics.contains(m)){
				if(values.size() < 250){
					for(String cou : getCouples(values)){
						metrics.add(m+"_"+cou);
					}
				}
			}else if(setMetrics.contains(m)){
				for(String comb : getCombinations(values)){
					metrics.add(m+"_"+comb);
				}
			}else{
				metrics.add(m);
			}
		}
		
		return metrics;
	}*/

	private static Set<String> getCombinations(Set<Integer> values) {
		Set<String> combinations = new HashSet<String>();
		
		if(values.size() > 2){
			Set<Set<Integer>> set = new HashSet<Set<Integer>>(); 
			Set<Set<Integer>> temp;
			Set<Integer> s;
			for(int i=0; i<values.size()-1; i++){
				temp = new HashSet<Set<Integer>>();  
				for(double v : values){
					for(Set<Integer> ss : set){
						s = new HashSet<Integer>(ss);
						s.add((int) v);
						temp.add(s);
					}
					s = new HashSet<Integer>();
					s.add((int) v);
					temp.add(s);
				}
				set.addAll(temp);
			}
			
			StringBuffer combination;
			for(Set<Integer> ss : set){
				if(ss.size() > 1){
					combination = new StringBuffer();
					for(int i : ss){
						combination.append(i);
						combination.append('-');
					}
					;
					combinations.add(combination.subSequence(0, combination.length()-1).toString());
				}
			}
		}
		
		return combinations;
	}

	private static Set<String> getCouples(Set<Integer> values) {
		Set<String> couples = new HashSet<String>();
		
		Set<Double> ever = new TreeSet<Double>();
		double c;
		for(double v1 : values){
			for(double v2 : values){
				c = Couple.get(v1, v2);
				if(!ever.contains(c)){
					couples.add((int)v1+"-"+(int)v2);
					ever.add(c);
				}
			}
		}
		
		return couples;
	}
	
	public static Set<String> getQuantitativeMetricNames(){
		return quantitativeMetrics;
	}
	
}
