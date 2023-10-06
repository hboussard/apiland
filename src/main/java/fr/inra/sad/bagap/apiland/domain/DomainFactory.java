package fr.inra.sad.bagap.apiland.domain;

public class DomainFactory {

	public static Domain<Double, Double> getDoubleDomain(String value) {
		value = value.replace(" ", "");
		if(value.startsWith("[")){
			if(value.endsWith("]")){
				String[] d = value.replace("[", "").replace("]", "").replace(" ", "").split(",", 2);
				if(!d[0].equalsIgnoreCase("")){
					double d0 = Double.parseDouble(d[0]);
					if(!d[1].equalsIgnoreCase("")){
						double d1 = Double.parseDouble(d[1]);
						return new BoundedDomain<Double, Double>(">=", d0, "<=", d1);
					}else{
						return new NumberDomain<Double, Double>(">=", d0);
					}
				}else{
					if(!d[1].equalsIgnoreCase("")){
						double d1 =Double.parseDouble(d[1]);
						return new NumberDomain<Double, Double>("<=", d1);
					}else{
						// domain ALL !!!
						return new AllDomain();
					}
				}
			}else{ // ends with "["
				String[] d = value.replace("[", "").split(",", 2);
				if(!d[0].equalsIgnoreCase("")){
					double d0 = Double.parseDouble(d[0]);
					if(!d[1].equalsIgnoreCase("")){
						double d1 = Double.parseDouble(d[1]);
						return new BoundedDomain<Double, Double>(">=", d0, "<", d1);
					}else{
						return new NumberDomain<Double, Double>(">=", d0);
					}
				}else{
					if(!d[1].equalsIgnoreCase("")){
						double d1 = Double.parseDouble(d[1]);
						return new NumberDomain<Double, Double>("<", d1);
					}else{
						// domain ALL !!!
						return new AllDomain();
					}
				}
			}
		}else{ // begins with "]"
			if(value.endsWith("]")){
				String[] d = value.replace("]", "").split(",", 2);
				if(!d[0].equalsIgnoreCase("")){
					double d0 = Double.parseDouble(d[0]);
					if(!d[1].equalsIgnoreCase("")){
						double d1 = Double.parseDouble(d[1]);
						return new BoundedDomain<Double, Double>(">", d0, "<=", d1);
					}else{
						return new NumberDomain<Double, Double>(">", d0);
					}
				}else{
					if(!d[1].equalsIgnoreCase("")){
						double d1 = Double.parseDouble(d[1]);
						return new NumberDomain<Double, Double>("<=", d1);
					}else{
						// domain ALL !!!
						return new AllDomain();
					}
				}
			}else{ // ends with "["
				String[] d = value.replace("[", "").replace("]", "").split(",", 2);
				if(!d[0].equalsIgnoreCase("")){
					double d0 = Double.parseDouble(d[0]);
					if(!d[1].equalsIgnoreCase("")){
						double d1 = Double.parseDouble(d[1]);
						return new BoundedDomain<Double, Double>(">", d0, "<", d1);
					}else{
						return new NumberDomain<Double, Double>(">", d0);
					}
				}else{
					if(!d[1].equalsIgnoreCase("")){
						double d1 = Double.parseDouble(d[1]);
						return new NumberDomain<Double, Double>("<", d1);
					}else{
						// domain ALL !!!
						return new AllDomain();
					}
				}
			}
		}
	}
	
	public static Domain<Float, Float> getFloatDomain(String value) {
		value = value.replace(" ", "");
		if(value.startsWith("[")){
			if(value.endsWith("]")){
				String[] d = value.replace("[", "").replace("]", "").replace(" ", "").split(",", 2);
				if(!d[0].equalsIgnoreCase("")){
					float d0 = Float.parseFloat(d[0]);
					if(!d[1].equalsIgnoreCase("")){
						float d1 = Float.parseFloat(d[1]);
						return new BoundedDomain<Float, Float>(">=", d0, "<=", d1);
					}else{
						return new NumberDomain<Float, Float>(">=", d0);
					}
				}else{
					if(!d[1].equalsIgnoreCase("")){
						float d1 =Float.parseFloat(d[1]);
						return new NumberDomain<Float, Float>("<=", d1);
					}else{
						// domain ALL !!!
						return new AllDomain();
					}
				}
			}else{ // ends with "["
				String[] d = value.replace("[", "").split(",", 2);
				if(!d[0].equalsIgnoreCase("")){
					float d0 = Float.parseFloat(d[0]);
					if(!d[1].equalsIgnoreCase("")){
						float d1 = Float.parseFloat(d[1]);
						return new BoundedDomain<Float, Float>(">=", d0, "<", d1);
					}else{
						return new NumberDomain<Float, Float>(">=", d0);
					}
				}else{
					if(!d[1].equalsIgnoreCase("")){
						float d1 = Float.parseFloat(d[1]);
						return new NumberDomain<Float, Float>("<", d1);
					}else{
						// domain ALL !!!
						return new AllDomain();
					}
				}
			}
		}else{ // begins with "]"
			if(value.endsWith("]")){
				String[] d = value.replace("]", "").split(",", 2);
				if(!d[0].equalsIgnoreCase("")){
					float d0 = Float.parseFloat(d[0]);
					if(!d[1].equalsIgnoreCase("")){
						float d1 = Float.parseFloat(d[1]);
						return new BoundedDomain<Float, Float>(">", d0, "<=", d1);
					}else{
						return new NumberDomain<Float, Float>(">", d0);
					}
				}else{
					if(!d[1].equalsIgnoreCase("")){
						float d1 = Float.parseFloat(d[1]);
						return new NumberDomain<Float, Float>("<=", d1);
					}else{
						// domain ALL !!!
						return new AllDomain();
					}
				}
			}else{ // ends with "["
				String[] d = value.replace("[", "").replace("]", "").split(",", 2);
				if(!d[0].equalsIgnoreCase("")){
					float d0 = Float.parseFloat(d[0]);
					if(!d[1].equalsIgnoreCase("")){
						float d1 = Float.parseFloat(d[1]);
						return new BoundedDomain<Float, Float>(">", d0, "<", d1);
					}else{
						return new NumberDomain<Float, Float>(">", d0);
					}
				}else{
					if(!d[1].equalsIgnoreCase("")){
						float d1 = Float.parseFloat(d[1]);
						return new NumberDomain<Float, Float>("<", d1);
					}else{
						// domain ALL !!!
						return new AllDomain();
					}
				}
			}
		}
	}
	
}
