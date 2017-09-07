package fr.inra.sad.bagap.apiland.capfarm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.geotools.data.shapefile.files.ShpFiles;
import org.geotools.data.shapefile.dbf.DbaseFileHeader;
import org.geotools.data.shapefile.dbf.DbaseFileReader;
import org.geotools.data.shapefile.dbf.DbaseFileWriter;
import org.geotools.data.shapefile.files.ShpFileType;
import org.geotools.data.shapefile.shp.ShapeType;
import org.geotools.data.shapefile.shp.ShapefileReader;
import org.geotools.data.shapefile.shp.ShapefileWriter;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiPolygon;

import fr.inra.sad.bagap.apiland.capfarm.model.CoverFactory;
import fr.inra.sad.bagap.apiland.capfarm.model.Farm;
import fr.inra.sad.bagap.apiland.capfarm.model.FarmingSystemFactory;
import fr.inra.sad.bagap.apiland.capfarm.model.GenericFarmingSystem;
import fr.inra.sad.bagap.apiland.capfarm.model.constraint.ConstraintBuilder;
import fr.inra.sad.bagap.apiland.capfarm.model.constraint.ConstraintMode;
import fr.inra.sad.bagap.apiland.capfarm.model.constraint.ConstraintType;
import fr.inra.sad.bagap.apiland.capfarm.model.constraint.GenericConstraintBuilder;
import fr.inra.sad.bagap.apiland.capfarm.model.territory.AgriculturalArea;
import fr.inra.sad.bagap.apiland.capfarm.model.territory.FarmTerritory;
import fr.inra.sad.bagap.apiland.capfarm.model.territory.Territory;
import fr.inra.sad.bagap.apiland.capfarm.model.territory.TerritoryFactory;
import fr.inra.sad.bagap.apiland.capfarm.simul.farm.CfmFarmManager;
import fr.inra.sad.bagap.apiland.capfarm.simul.farm.CfmFarmSimulator;
import fr.inra.sad.bagap.apiland.capfarm.simul.landscape.CfmLandscapeManager;
import fr.inra.sad.bagap.apiland.capfarm.simul.landscape.CfmLandscapeSimulator;
import fr.inra.sad.bagap.apiland.capfarm.simul.output.ConsoleOutput;
import fr.inra.sad.bagap.apiland.core.time.Instant;

public class Main {

	private static final String path = "C:/Hugues/modelisation/capfarm/api/data/";
	private static Instant start = Instant.get(1, 7, 2010);
	private static Instant end = Instant.get(1, 7, 2015);
	private static int s;
	
	public static void main(String[] args) {
		CAPFarm.t = start;
		scriptOneFarmOneType();
		//copyShapefile(path+"sig/site_indre", path+"sig/site_indre2");
	}
	
	private static void scriptOneFarmOneType(){
		
		// integration du territoire
		Territory territory = TerritoryFactory.init(path+"sig/site_indre4.shp", start);
		
		// création d'une ferme avec un historique de couverts
		final Farm farm = new Farm("151776");
		farm.setHistoric("C:/Hugues/modelisation/capfarm/api/data/historic.txt");
		
		// intégration du territoire d'une ferme
		TerritoryFactory.init(territory, farm);
		
		// intégration des couverts
		CoverFactory.init(farm, path+"covers.txt", path+"groups.txt");
		
		// phase 1
		System.out.println("phase 1");
		
		// mise en place des contraintes
		ConstraintBuilder cb = new ConstraintBuilder(farm);
		
		cb.setCode("C8");	
		cb.setType(ConstraintType.Duration);
		cb.setDomain("1");
		cb.build();
		
		FarmingSystemFactory.exportSystem(farm.getFarmingSystem(), path + "system.csv");
		farm.getFarmingSystem().display();
		//farm.getFarmingSystem().getGenericFarmingSystem().display();
		
		CfmFarmManager sm = new CfmFarmManager(farm, s);
		sm.setPath(path);
		sm.setStart(start);
		sm.setEnd(end);
		
		sm.addOutput(new ConsoleOutput());
		
		// création et lancement du simulateur
		CfmFarmSimulator s = new CfmFarmSimulator(sm);
		s.allRun();
	}
	
	private static void scriptOneFarm(){
		// création d'une ferme
		final Farm farm = new Farm("151776");
		
		// intégration des couverts
		CoverFactory.init(farm, path+"covers.txt", path+"groups.txt");
		
		// intégration du territoire d'une ferme
		Territory territory = TerritoryFactory.init(path+"sig/site_indre2.shp", start);
		TerritoryFactory.init(territory, farm);
		
		// mise en place des contraintes
		ConstraintBuilder cb = new ConstraintBuilder(farm);
		
		cb.setCode("C8");	
		cb.setType(ConstraintType.Duration);
		cb.setDomain("1");
		cb.build();
		
		cb.setCode("C9");
		cb.setCover("EACH");
		cb.setType(ConstraintType.Repetition);
		cb.setDomain("0");
		cb.build();
		/*
		cb.setCode("C10");
		cb.setCover("M");
		cb.setType(ConstraintType.NextCover);
		cb.setDomain("[M, B, C]");
		cb.build();
		*/
		cb.setCode("C11");
		cb.setType(ConstraintType.NextCover);
		cb.setParams("C:/Hugues/modelisation/capfarm/api/data/next_covers.txt");
		cb.build();
		
		FarmingSystemFactory.exportSystem(farm.getFarmingSystem(), path + "system.csv");
		farm.getFarmingSystem().display();
		//farm.getFarmingSystem().getGenericFarmingSystem().display();
		
		CfmFarmManager sm = new CfmFarmManager(farm, s);
		sm.setPath(path);
		sm.setStart(start);
		sm.setEnd(end);
		//sm.setMode(CfmMode.ECONOMIC);
		//sm.addOutput(new FarmShapefileOutput());
		sm.addOutput(new ConsoleOutput());
		
		// création et lancement du simulateur
		CfmFarmSimulator s = new CfmFarmSimulator(sm);
		s.allRun();
	}

	private static void scriptMultiFarms(){
		
		// intégration du territoire d'une ferme
		Territory territory = TerritoryFactory.init(path+"sig/site_indre2.shp", start);
		
		// création des fermes
		int index = 0;
		for(FarmTerritory ft : (AgriculturalArea) territory.get("AA")){
			if(!ft.getId().equalsIgnoreCase("153486")
					&& !ft.getId().equalsIgnoreCase("153893")){
				index++;
				
				// création d'une ferme
				Farm farm = new Farm(ft.getId());
				System.out.println("farm "+ft.getId());
						
				// intégration des couverts
				CoverFactory.init(farm, path+"covers.txt", path+"groups.txt");
						
				TerritoryFactory.init(territory, farm);
				//Territory territory = TerritoryFactory.init(farm, path+"sig/landscape2.shp", start);
							
				// mise en place des contraintes
				ConstraintBuilder cb = new ConstraintBuilder(farm);
				
				
				
				//farm.getStrategy().display();
						
				// paramétrisation du simulateur
				CfmFarmManager sm = new CfmFarmManager(farm, s);
				sm.setPath(path);
				sm.setStart(start);
				sm.setEnd(end);
				//sm.addOutput(new FarmShapefileOutput());
				sm.addOutput(new ConsoleOutput());
						
				// création et lancement du simulateur
				CfmFarmSimulator s = new CfmFarmSimulator(sm);
				s.allRun();	
			}
		}
		
		System.out.println(index);
	}

	private static void scriptFarmTypeOneFarm(){

		// integration du territoire
		Territory territory = TerritoryFactory.init(path+"sig/site_indre2.shp", start);
				
		// création d'une ferme
		Farm farm = new Farm("151774");
		// intégration du territoire d'une ferme
		TerritoryFactory.init(territory, farm);
		
		// création d'un type de ferme
		GenericFarmingSystem system = new GenericFarmingSystem("porcin");
		
		// intégration des couverts
		CoverFactory.init(system, path+"covers.txt", path+"groups.txt");
		
		importWithAPI(system);
		importWithFile(system, path+"porcin.csv");
		//...
		
		FarmingSystemFactory.exportSystem(system, path+"system_test.csv");
		
		// affectation de ce système de production à une ferme
		new ConstraintBuilder(farm).build(system);
		
		// paramétrisation du simulateur
		CfmFarmManager sm = new CfmFarmManager(farm, s);
		sm.setPath(path);
		sm.setStart(start);
		sm.setEnd(end);
		//sm.addOutput(new ShapefileOutput());
		sm.addOutput(new ConsoleOutput());
				
		// création et lancement du simulateur
		CfmFarmSimulator s = new CfmFarmSimulator(sm);
		s.allRun();
	}
	
	private static void scriptFarmTypeMultiFarms(){
		// integration du territoire
		Territory territory = TerritoryFactory.init(path+"sig/site_indre2.shp", start);
				
		// création d'un type de ferme
		GenericFarmingSystem system = new GenericFarmingSystem("porcin");
				
		// intégration des couverts
		CoverFactory.init(system, path+"covers.txt", path+"groups.txt");
			
		importWithAPI(system);
		//importWithFile(system, path+"system_test.csv");
		//...
		
		//FarmingSystemFactory.exportSystem(system, path+"system_test.csv");
		
		system.display();
				
		Set<Farm> farms = new HashSet<Farm>();
		// création des fermes
		for(FarmTerritory ft : (AgriculturalArea) territory.get("AA")){
			Farm farm = new Farm(ft.getId());
			// intégration du territoire d'une ferme
			TerritoryFactory.init(territory, farm);
			// affectation de ce système de production à une ferme
			new ConstraintBuilder(farm).build(system);
			farms.add(farm);
		}
				
		// paramétrisation du simulateur
		CfmLandscapeManager sm = new CfmLandscapeManager(s);
		sm.setPath(path);
		sm.setStart(start);
		sm.setEnd(end);
		sm.setFarms(farms);
		sm.setTerritory(territory);
		//sm.addOutput(new LandscapeShapefileOutput());
		sm.addOutput(new ConsoleOutput());
						
		// création et lancement du simulateur
		CfmLandscapeSimulator s = new CfmLandscapeSimulator(sm);
		s.allRun();
	}

	private static void importWithAPI(GenericFarmingSystem system){
		GenericConstraintBuilder gcb = new GenericConstraintBuilder(system);
		
		gcb.setCode("C8");	
		gcb.setType(ConstraintType.Duration);
		gcb.setDomain("[1,1]");
		//gcb.setDomain("1,1");
		//gcb.setDomain("[=1]");
		//gcb.setDomain("=1");
		//gcb.setDomain("[1]");
		//gcb.setDomain("1");
		gcb.setParams("middle");
		gcb.build();
		
		gcb.setCode("C1"); 		
		gcb.setCover("W"); 
		gcb.setType(ConstraintType.ParcelArea);
		gcb.setMode(ConstraintMode.ALWAYS);
		gcb.setDomain("[,0.5]");
		//gcb.setDomain(",0.5");
		//gcb.setDomain("[<=0.5]");
		//gcb.setDomain("<=0.5");
		gcb.build();
		
		gcb.setCode("C2"); 
		gcb.setCover("M"); 	
		gcb.setType(ConstraintType.TotalArea);
		gcb.setMode(ConstraintMode.ONLY);
		gcb.setDomain("[10%,]");
		//gcb.setDomain("10%,");
		//gcb.setDomain("[>=10%]");
		//gcb.setDomain(">=10%");
		gcb.build();
		/*
		gcb.setCode("C3"); 
		gcb.setCover("B"); 	
		gcb.setType(ConstraintType.TotalArea);
		gcb.setMode(ConstraintMode.ONLY);
		gcb.setDomain("[10%,]");
		//gcb.setDomain("10%,");
		//gcb.setDomain("[>=10%]");
		//gcb.setDomain(">=10%");
		gcb.build();
		*/
		gcb.setCode("C4"); 
		gcb.setCover("O"); 	
		gcb.setType(ConstraintType.TotalArea);
		gcb.setMode(ConstraintMode.ONLY);
		gcb.setDomain("[min(10%|0.5),]");
		//gcb.setDomain("[0.5,]");
		gcb.build();
		
		/*
		gcb.setCode("C16"); 
		gcb.setCover("ME"); 	
		gcb.setType(ConstraintType.DistanceFromFacilities);
		gcb.setMode(ConstraintMode.ONLY);
		gcb.setDomain("[,2]");
		gcb.setParam("head");
		gcb.build();
		
		gcb.setCode("C4"); 
		gcb.setCover("ME"); 	
		gcb.setType(ConstraintType.DistanceBetweenCovers);
		gcb.setMode(ConstraintMode.NEVER);
		gcb.setDomain("[,0.4]");
		gcb.setParam("ME");
		gcb.build();
		
		gcb.setCode("C5"); 
		gcb.setCover("ME"); 	
		gcb.setType(ConstraintType.OnBooleanCondition);
		gcb.setMode(ConstraintMode.ONLY);
		gcb.setDomain("[irrigate=TRUE]");
		gcb.build();
		
		gcb.setCode("C6"); 
		gcb.setCover("ME"); 	
		gcb.setType(ConstraintType.OnNumericCondition);
		gcb.setMode(ConstraintMode.AROUND);
		gcb.setDomain("[humidity>0.4]");
		gcb.build();
		
		gcb.setCode("C7"); 
		gcb.setCover("ME"); 	
		gcb.setType(ConstraintType.Delay);
		gcb.setMode(ConstraintMode.ONLY);
		gcb.setDomain("[3,6]");
		gcb.setParam("ME");
		gcb.build();
		*/
		
		/*
		gcb.setCode("C9");
		gcb.setCover("ME");
		gcb.setType(ConstraintType.Repetition);
		gcb.setDomain("[1,1]");
		gcb.setParam("middle");
		gcb.build();
		
		gcb.setCode("C10");
		gcb.setCover("ME");
		gcb.setType(ConstraintType.NextCover);
		gcb.setDomain("[ME,B, C]");
		gcb.build();
		
		gcb.setCode("C11");
		gcb.setCover("O", "C");
		gcb.setType(ConstraintType.TemporalPattern);
		gcb.setMode(ConstraintMode.ONLY);
		gcb.setParam("ME-ME");
		gcb.build();
		
		gcb.setCode("C12");
		gcb.setCover("T", "P");
		gcb.setType(ConstraintType.TemporalPattern);
		gcb.setMode(ConstraintMode.ONLY);
		gcb.setParam("ME-ME");
		gcb.build();
		
		gcb.setCode("C13"); 
		gcb.setCover("ME"); 	
		gcb.setType(ConstraintType.TotalArea);
		gcb.setMode(ConstraintMode.ONLY);
		gcb.setDomain("[8,]");
		gcb.build();
		
		gcb.setCode("C14"); 
		gcb.setCover("B"); 	
		gcb.setType(ConstraintType.TotalArea);
		gcb.setMode(ConstraintMode.ONLY);
		gcb.setDomain("[8,]");
		gcb.build();
		
		gcb.setCode("C15"); 
		gcb.setCover("C"); 	
		gcb.setType(ConstraintType.TotalArea);
		gcb.setMode(ConstraintMode.ONLY);
		gcb.setDomain("[8,]");
		gcb.build();
		*/
	}

	private static void importWithFile(GenericFarmingSystem system, String file){
		FarmingSystemFactory.importSystem(system, file);
	}
	
	private static void copyShapefile(String input, String output) {
		try(FileOutputStream fos = new FileOutputStream(output+".dbf");
				FileOutputStream shp = new FileOutputStream(output + ".shp");
				FileOutputStream shx = new FileOutputStream(output + ".shx");){
			
			ShpFiles sf = new ShpFiles(input+".shp");
			ShapefileReader sfr = new ShapefileReader(sf, true, false, new com.vividsolutions.jts.geom.GeometryFactory());
			DbaseFileReader dfr = new DbaseFileReader(sf, true, Charset.defaultCharset());
			DbaseFileHeader header = dfr.getHeader();
			
			double minX = sfr.getHeader().minX();
			double maxX = sfr.getHeader().maxX();
			double minY = sfr.getHeader().minY();
			double maxY = sfr.getHeader().maxY();
			int fileLength = sfr.getHeader().getFileLength();
			
			Map<Long, Set<Geometry>> geoms = new HashMap<Long, Set<Geometry>>();
			Geometry g;
			Object[] o;
			while(sfr.hasNext()){
				g = ((MultiPolygon) sfr.nextRecord().shape()).getGeometryN(0);
				o = dfr.readEntry();
				g.setUserData(o);
				
				if(!geoms.containsKey(o[1])){
					geoms.put((Long) o[1], new HashSet<Geometry>());
				}
				geoms.get(o[1]).add(g);
			}
			sfr.close();
			dfr.close();
			
			Iterator<Entry<Long, Set<Geometry>>> ite = geoms.entrySet().iterator();
			int size = 0;
			while(ite.hasNext()){
				Entry<Long, Set<Geometry>> e = ite.next();
				if(e.getValue().size() == 1){
					ite.remove();
				}else{
					size += e.getValue().size();
				}
			}
			
			DbaseFileHeader h = header;
			h.setNumRecords(size);
			DbaseFileWriter dfw = new DbaseFileWriter(h, fos.getChannel());
			ShapefileWriter sfw = new ShapefileWriter(shp.getChannel(), shx.getChannel());
			sfw.writeHeaders(new Envelope(minX, maxX, minY, maxY), ShapeType.POLYGON, size, fileLength);
			
			ite = geoms.entrySet().iterator();
			while(ite.hasNext()){
				for(Geometry g2 : ite.next().getValue()){
					sfw.writeGeometry(g2);
					dfw.write((Object[]) g2.getUserData());
				}
			}		
			
			dfw.close();
			sfw.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		copyFile(input+".prj", output+".prj");
	}
	
	public static boolean copyFile(String source, String dest){
		try{
			// Declaration et ouverture des flux
			java.io.FileInputStream sourceFile = new java.io.FileInputStream(new File(source));
	 
			try{
				java.io.FileOutputStream destinationFile = null;
	 
				try{
					destinationFile = new FileOutputStream(new File(dest));
	 
					// Lecture par segment de 0.5Mo 
					byte buffer[] = new byte[512 * 1024];
					int nbLecture;
					
					while ((nbLecture = sourceFile.read(buffer)) != -1){
						destinationFile.write(buffer, 0, nbLecture);
					}
				} finally {
					destinationFile.close();
				}
			} finally {
				sourceFile.close();
			}
		} catch (IOException e){
			e.printStackTrace();
			return false; // Erreur
		}
		return true; // Résultat OK  
	}

	/*
	 * 
	 * cb.setCode("C8");	
		cb.setType(ConstraintType.Duration);
		//cb.setLocation("[irrigate]");
		//cb.setLocation("[irrigate]+[342,703,407]");
		//cb.setLocation("AND([irrigate]+[342],[340])");
		//cb.setLocation("[342]+AND([irrigate]+[342],[340])");
		//cb.setLocation("AND([irrigate]+[342],[340])+[342]");
		//cb.setLocation("AND([irrigate]+[342],[340])+AND([342,340],[342,407],[703,342])");
		//cb.setLocation("AND([irrigate]+[342],AND([342,340],[342,407],[703,342]))");
		//cb.setLocation("[407]+AND([irrigate]+[342],AND([342,340],[342,407],[703,342]))");
		//cb.setLocation("[407,342]-AND([irrigate]+[342],AND([342,340],[342,407],[703,342]))");
		//cb.setLocation("XOR([irrigate]+[342],[340])");
		//cb.setLocation("[407]+XOR([irrigate]+[342],[340])");
		//cb.setLocation("XOR([irrigate]+[342],[340])+[407]");
		//cb.setLocation("XOR([irrigate]+[342],[340])+XOR([342,340],[342,407],[703,342])");
		//cb.setLocation("XOR([irrigate]+[342],XOR([342,340],[342,407],[703,342]))");
		//cb.setLocation("[340]+XOR([irrigate]+[342],XOR([342,340],[342,407],[703,342]))");
		//cb.setLocation("[744,407,342]-XOR([irrigate]+[342],XOR([342,340],[342,407],[703,342]))");
		//cb.setLocation("XOR([irrigate]+[342],AND([342,340],[342,407],[703,342]))");
		//cb.setLocation("XOR([irrigate],DISTANCE(head<1))");
		//cb.setLocation("[irrigate]-[342,340]");
		//cb.setLocation("-[342,340]");
		
		cb.setDomain("1");
		cb.build();
		/*
		cb.setCode("C9");
		cb.setCover("M");
		cb.setType(ConstraintType.Repetition);
		cb.setDomain("[,5]");
		cb.setParams("fast");
		cb.build();
		*/
		/*
		cb.setCode("C10");
		cb.setCover("M");
		cb.setType(ConstraintType.NextCover);
		cb.setDomain("[M, B, C]");
		cb.build();
		*//*
		cb.setCode("C11");
		cb.setMode("NEVER");
		cb.setType(ConstraintType.NextCover);
		cb.setParams("C:/Hugues/modelisation/capfarm/api/data/next_covers.txt");
		cb.build();
		*/
		/*
		cb.setCode("C9"); 
		cb.setCover("M"); 	
		cb.setType(ConstraintType.Delay);
		cb.setMode(ConstraintMode.ONLY);
		cb.setDomain("[,3]");
		cb.setParams("B","C");
		cb.build();
		
		cb.setCode("C11");
		cb.setCover("M");
		cb.setType(ConstraintType.TemporalPattern);
		cb.setMode(ConstraintMode.NEVER);
		cb.setParams("M-M");
		cb.build();
		*/
		/*
		cb.setCode("C13"); 		
		cb.setCover("M"); 
		cb.setType(ConstraintType.ParcelArea);
		cb.setMode(ConstraintMode.ONLY);
		cb.setDomain("[5,]");
		cb.build();
		*/
		/*
		cb.setCode("C15"); 
		cb.setCover("B"); 	
		cb.setType(ConstraintType.TotalArea);
		cb.setMode(ConstraintMode.ONLY);
		cb.setDomain("[,20]");
		cb.build();
		*/
		/*
		cb.setCode("C16"); 
		cb.setCover("M"); 	
		cb.setType(ConstraintType.DistanceFromFacilities);
		cb.setMode(ConstraintMode.ONLY);
		cb.setDomain("[,2]");
		cb.setParams("head");
		cb.build();
		*/
		/*
		cb.setCode("C4"); 
		cb.setCover("M"); 	
		cb.setType(ConstraintType.DistanceBetweenCovers);
		cb.setMode(ConstraintMode.NEVER);
		cb.setDomain("[,0.4]");
		cb.setParams("M");
		cb.build();
		*/
		/*
		cb.setCode("C18"); 
		cb.setCover("M"); 	
		cb.setType(ConstraintType.OnBooleanCondition);
		cb.setMode(ConstraintMode.ONLY);
		cb.setDomain("[irrigate=TRUE]");
		cb.build();
		*/
		/*
		cb.setCode("C19"); 
		cb.setCover("M"); 	
		cb.setType(ConstraintType.OnNumericCondition);
		cb.setMode(ConstraintMode.NEVER);
		cb.setDomain("[humidity>0.4]");
		cb.build();
		*/
	/*
	cb.setCode("C2"); 
	cb.setCover("M"); 	
	cb.setType(ConstraintType.TotalArea);
	cb.setMode(ConstraintMode.ONLY);
	cb.setDomain("[25%,]");
	cb.build();
		
	cb.setCode("C3"); 
	cb.setCover("B"); 		
	cb.setType(ConstraintType.TotalArea);
	cb.setMode(ConstraintMode.ONLY);
	cb.setDomain("[20%,]");
	cb.build();
		
	cb.setCode("C8");	
	cb.setType(ConstraintType.Duration);
	cb.setDomain("[1,1]");
	cb.setParam("middle");
	cb.build();
	
	cb.setCode("C2"); 
	cb.setCover("ME"); 	
	cb.setType(ConstraintType.TotalArea);
	cb.setMode(ConstraintMode.ONLY);
	cb.setDomain("[1%,]");
	cb.build();
	
	cb.setCode("C3"); 
	cb.setCover("BL"); 	
	cb.setType(ConstraintType.TotalArea);
	cb.setMode(ConstraintMode.ONLY);
	cb.setDomain("[1%,]");
	cb.build();
	*/
	/*
	cb.setCode("C2"); 
	cb.setCover("ME"); 	
	cb.setType(ConstraintType.TotalArea);
	cb.setMode(ConstraintMode.ONLY);
	cb.setDomain("[20%,]");
	//cb.setDomain("[5,]");
	cb.build();
	*/
	/*
	cb.setCode("C1"); 		
	cb.setCover("ME"); 
	cb.setType(ConstraintType.ParcelArea);
	cb.setMode(ConstraintMode.ALWAYS);
	cb.setDomain("[,5%]");
	cb.build();
	
	cb.setCode("C2"); 
	cb.setCover("ME"); 	
	cb.setType(ConstraintType.TotalArea);
	cb.setMode(ConstraintMode.ONLY);
	cb.setDomain("[20,]");
	cb.build();
	cb.setCode("C3"); 
	cb.setCover("B"); 	
	cb.setType(ConstraintType.TotalArea);
	cb.setMode(ConstraintMode.ONLY);
	cb.setDomain("[20,]");
	cb.build();
	
	cb.setCode("C3"); 
	cb.setCover("ME"); 	
	cb.setType(ConstraintType.DistanceFromFacilities);
	cb.setMode(ConstraintMode.ONLY);
	cb.setDomain("[,2]");
	cb.setParam("head");
	cb.build();
	
	cb.setCode("C4"); 
	cb.setCover("ME"); 	
	cb.setType(ConstraintType.DistanceBetweenCovers);
	cb.setMode(ConstraintMode.NEVER);
	cb.setDomain("[,0.4]");
	cb.setParam("ME");
	cb.build();
	
	cb.setCode("C5"); 
	cb.setCover("ME"); 	
	cb.setType(ConstraintType.OnBooleanCondition);
	cb.setMode(ConstraintMode.ONLY);
	cb.setDomain("[irrigate=TRUE]");
	cb.build();
	
	cb.setCode("C6"); 
	cb.setCover("ME"); 	
	cb.setType(ConstraintType.OnNumericCondition);
	cb.setMode(ConstraintMode.AROUND);
	cb.setDomain("[humidity>0.4]");
	cb.build();
	
	cb.setCode("C7"); 
	cb.setCover("ME"); 	
	cb.setType(ConstraintType.Delay);
	cb.setMode(ConstraintMode.ONLY);
	cb.setDomain("[3,6]");
	cb.setParam("ME");
	cb.build();
	*//*
	cb.setCode("C8");	
	cb.setType(ConstraintType.Duration);
	cb.setDomain("[1,1]");
	cb.setParam("middle");
	cb.build();*/
	/*
	cb.setCode("C9");
	cb.setCover("ME");
	cb.setType(ConstraintType.Repetition);
	cb.setDomain("[1,1]");
	cb.setParam("middle");
	cb.build();*/
	/*
	cb.setCode("C10");
	cb.setCover("ME");
	cb.setType(ConstraintType.NextCover);
	cb.setDomain("[ME,B, C]");
	cb.build();
	*/
	/*
	cb.setCode("C11");
	cb.setCover("O", "C");
	cb.setType(ConstraintType.TemporalPattern);
	cb.setMode(ConstraintMode.ONLY);
	cb.setParam("ME-ME");
	cb.build();
	*//*
	cb.setCode("C12");
	cb.setCover("T", "P");
	cb.setType(ConstraintType.TemporalPattern);
	cb.setMode(ConstraintMode.ONLY);
	cb.setParam("ME-ME");
	cb.build();
	*/
	/*
	cb.setCode("C1"); 
	cb.setCover("ME"); 	
	cb.setType(ConstraintType.TotalArea);
	cb.setMode(ConstraintMode.ONLY);
	cb.setDomain("[8,]");
	cb.build();
	
	cb.setCode("C2"); 
	cb.setCover("B"); 	
	cb.setType(ConstraintType.TotalArea);
	cb.setMode(ConstraintMode.ONLY);
	cb.setDomain("[8,]");
	cb.build();
	
	cb.setCode("C3"); 
	cb.setCover("C"); 	
	cb.setType(ConstraintType.TotalArea);
	cb.setMode(ConstraintMode.ONLY);
	cb.setDomain("[8,]");
	cb.build();
	
	cb.setCode("C11");
	cb.setCover("ME", "ME");
	cb.setType(ConstraintType.SpatialPattern);
	cb.setMode(ConstraintMode.ONLY);
	cb.setDomain("[1350,]");
	cb.build();
	
	cb.setCode("C12");
	cb.setCover("B", "B");
	cb.setType(ConstraintType.SpatialPattern);
	cb.setMode(ConstraintMode.ONLY);
	cb.setDomain("[1350,]");
	cb.build();
	
	cb.setCode("C13");
	cb.setCover("C", "C");
	cb.setType(ConstraintType.SpatialPattern);
	cb.setMode(ConstraintMode.ONLY);
	cb.setDomain("[3000,]");
	cb.build();
	*/
	
}