package fr.inra.sad_paysage.apiland.capfarm.model.territory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import org.geotools.data.shapefile.files.ShpFiles;
import org.geotools.data.shapefile.dbf.DbaseFileHeader;
import org.geotools.data.shapefile.dbf.DbaseFileReader;

import fr.inra.sad_paysage.apiland.capfarm.model.Farm;
import fr.inra.sad_paysage.apiland.core.element.manager.DynamicLayerFactory;
import fr.inra.sad_paysage.apiland.core.element.type.DynamicElementTypeFactory;
import fr.inra.sad_paysage.apiland.core.element.type.DynamicFeatureType;
import fr.inra.sad_paysage.apiland.core.element.type.DynamicLayerType;
import fr.inra.sad_paysage.apiland.core.space.Surface;
import fr.inra.sad_paysage.apiland.core.space.impl.GeometryImplType;
import fr.inra.sad_paysage.apiland.core.structure.DynamicRepresentationType;
import fr.inra.sad_paysage.apiland.core.time.Instant;
import fr.inra.sad_paysage.apiland.core.time.Interval;
import fr.inra.sad_paysage.apiland.capfarm.model.Cover;
import fr.inra.sad_paysage.apiland.capfarm.model.CoverManager;
import fr.inra.sad_paysage.apiland.capfarm.model.CoverUnit;

public class TerritoryFactory {
	
	public static void init(Territory territory, Farm farm){
		for(FarmTerritory ft : (AgriculturalArea) territory.get("AA")){
			if(ft.getId().equalsIgnoreCase(farm.getCode())){
				farm.setTerritory(ft);
				return;
			}
		}
		throw new IllegalArgumentException("wrong farm code under territory");
	}
	
	public static Territory init(String shape, Instant t){
		ShpFiles sf;
		Map<Class<?>, Set<String>> conditions = new HashMap<Class<?>, Set<String>>();
		try {
			sf = (shape.endsWith(".shp"))?new org.geotools.data.shapefile.files.ShpFiles(shape):new ShpFiles(shape+".shp");
			DbaseFileReader dfr = new DbaseFileReader(sf, true, Charset.defaultCharset());
			DbaseFileHeader dfh = dfr.getHeader();
			Set<String> reserved = new TreeSet<String>();
			reserved.add("id");
			reserved.add("farm");
			reserved.add("area");
			reserved.add("type");
			reserved.add("facility");
			reserved.add("occsol");
			for(int f=0; f<dfh.getNumFields(); f++) {
				if(!reserved.contains(dfh.getFieldName(f).toLowerCase())){
					if(!conditions.containsKey(dfh.getFieldClass(f))){
						conditions.put(dfh.getFieldClass(f), new HashSet<String>());
					}
					conditions.get(dfh.getFieldClass(f)).add(dfh.getFieldName(f).toLowerCase());
				}
			}
			dfr.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// definition du type "Parcel"
		DynamicFeatureType parcelType = new DynamicFeatureType("id", Parcel.class);
		parcelType.addRepresentationType(new DynamicRepresentationType("raster", Interval.class, Surface.class, GeometryImplType.RASTER));
		parcelType.addAttributeType(DynamicElementTypeFactory.createAttributeType("type", null, String.class));
		parcelType.addAttributeType(DynamicElementTypeFactory.createAttributeType("seq_cover", null, String.class));
		parcelType.addAttributeType(DynamicElementTypeFactory.createAttributeType("strict_seq", null, String.class));
		parcelType.addAttributeType(DynamicElementTypeFactory.createAttributeType("seq_year", null, String.class));
		parcelType.addAttributeType(DynamicElementTypeFactory.createAttributeType("length", null, Integer.class));
		parcelType.addAttributeType(DynamicElementTypeFactory.createAttributeType("repet", null, Integer.class));
		parcelType.addAttributeType(DynamicElementTypeFactory.createAttributeType("cover", Interval.class, CoverUnit.class));
		parcelType.addAttributeType(DynamicElementTypeFactory.createAttributeType("area", Interval.class, Double.class));
		for(Entry<Class<?>, Set<String>> e : conditions.entrySet()){
			for(String c : e.getValue()){
				parcelType.addAttributeType(DynamicElementTypeFactory.createAttributeType(c, null, e.getKey()));
			}	
		}
		// ajout des attributs de comptage d'occurence de couverts
		for(Cover c  : CoverManager.coverUnits()){
			System.out.println(c);
		}
		parcelType.setCondition("parcel");
			
		// definition du type "Facility"
		DynamicFeatureType facilityType = new DynamicFeatureType("id", Facility.class);
		facilityType.addRepresentationType(new DynamicRepresentationType("raster", Interval.class, Surface.class, GeometryImplType.RASTER));
		facilityType.addAttributeType(DynamicElementTypeFactory.createAttributeType("type", null, String.class));
		facilityType.addAttributeType(DynamicElementTypeFactory.createAttributeType("facility", null, String.class));
		facilityType.addAttributeType(DynamicElementTypeFactory.createAttributeType("cover", Interval.class, CoverUnit.class));
		facilityType.setCondition("facility");
						
		// definition du type "FarmTerritory"
		DynamicLayerType farmTerritoryType = new DynamicLayerType("farm", FarmTerritory.class);
		farmTerritoryType.addAttributeType(DynamicElementTypeFactory.createAttributeType("seed", Interval.class, Long.class));
		farmTerritoryType.addElementType(parcelType);
		farmTerritoryType.addElementType(facilityType);
		
		// definition du type "AgriculturalArea"
		DynamicLayerType agriculturalAreaType = new DynamicLayerType("area", AgriculturalArea.class);
		agriculturalAreaType.addElementType(farmTerritoryType);
			
		// definition du type "TrameUnit"
		DynamicFeatureType trameUnitType = new DynamicFeatureType("id", TrameUnit.class);
		trameUnitType.addRepresentationType(new DynamicRepresentationType("raster", Interval.class, Surface.class, GeometryImplType.RASTER));
		trameUnitType.addAttributeType(DynamicElementTypeFactory.createAttributeType("type", null, String.class));
		trameUnitType.addAttributeType(DynamicElementTypeFactory.createAttributeType("cover", Interval.class, CoverUnit.class));
		trameUnitType.setCondition("trame");
		
		// definition du type "TrameArea"
		DynamicLayerType trameAreaType = new DynamicLayerType("area", TrameArea.class);
		trameAreaType.addElementType(trameUnitType);
		
		// definition du type "Territoire"
		DynamicLayerType territoryType = new DynamicLayerType(Territory.class);
		territoryType.addElementType(agriculturalAreaType);
		territoryType.addElementType(trameAreaType);
		
		Territory territory = DynamicLayerFactory.initWithShape(shape, t, territoryType, "type", parcelType, facilityType, trameUnitType);
		
		for(FarmTerritory ft : (AgriculturalArea) territory.get("AA")){
			//System.out.println(ft.getId()+" "+ft.getClass());
			for(FarmUnit fu : ft){
				//System.out.println("	"+fu.getId()+" "+fu.getClass());
				if(fu instanceof Parcel){
					fu.getAttribute("area").setValue(t, fu.getArea(t));
				}
			}
		}
		
		// affectation des moyens de production aux batiments.
		String[] mp;
		for(FarmTerritory ft : (AgriculturalArea) territory.get("AA")){
			for(Facility b : ft.facilities()){
				if(b.getAttribute("facility").isActive(t)){
					mp = ((String) b.getAttribute("facility").getValue(t)).split(";");
					for(String m : mp){
						b.addFacility(m);
					}
				}
				b.getAttribute("cover").setValue(t, CoverManager.getCoverUnit("MP", "MP"));
			}
		}
		
		if(territory.contains("TA")){
			for(TrameUnit tu : (TrameArea) territory.get("TA")){
				String occsol = (String) tu.getAttribute("occsol").getValue(t);
				switch(occsol){
				case "Bati" : 
					tu.getAttribute("cover").setValue(t, CoverManager.getCoverUnit("BA", "BA")); break;
				case "BE" : 
					tu.getAttribute("cover").setValue(t, CoverManager.getCoverUnit("BE", "BE")); break;
				case "Bois" : 
					tu.getAttribute("cover").setValue(t, CoverManager.getCoverUnit("WO", "WO")); break;
				case "Eau" : 
					tu.getAttribute("cover").setValue(t, CoverManager.getCoverUnit("WA", "WA")); break;
				case "Prairie" : 
					tu.getAttribute("cover").setValue(t, CoverManager.getCoverUnit("P", "P")); break;
				case "Route" : 
					tu.getAttribute("cover").setValue(t, CoverManager.getCoverUnit("RO", "RO")); break;
				case "Haie" : 
					tu.getAttribute("cover").setValue(t, CoverManager.getCoverUnit("HA", "Haie")); break;
				}
			}
		}
		
		
		return territory;
	}
	
}
