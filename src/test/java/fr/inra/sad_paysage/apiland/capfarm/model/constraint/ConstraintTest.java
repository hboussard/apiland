package fr.inra.sad_paysage.apiland.capfarm.model.constraint;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import fr.inra.sad_paysage.apiland.capfarm.model.Farm;
import fr.inra.sad_paysage.apiland.capfarm.model.constraint.ConstraintBuilder;
import fr.inra.sad_paysage.apiland.capfarm.model.territory.Territory;
import fr.inra.sad_paysage.apiland.capfarm.model.territory.TerritoryFactory;
import fr.inra.sad_paysage.apiland.capfarm.simul.farm.CfmFarmManager;
import fr.inra.sad_paysage.apiland.capfarm.simul.farm.CfmFarmSimulator;
import fr.inra.sad_paysage.apiland.capfarm.simul.output.ConsoleOutput;
import fr.inra.sad_paysage.apiland.core.time.Instant;
import fr.inra.sad_paysage.capfarm.model.CoverFactory;

public abstract class ConstraintTest {

	private static final String path = "C:/Hugues/modelisation/capfarm/api/data/";
	
	protected static Instant start = Instant.get(1, 7, 2010);
	protected static Instant end = Instant.get(1, 7, 2020);
	
	protected Farm farm;
	
	private Territory territory;
	
	protected ConstraintBuilder cb;
	
	private CfmFarmSimulator s;
	
	@BeforeMethod
	public void before() throws Exception {
		// création d'une ferme
		farm = new Farm("151774");
				
		// intégration des couverts
		CoverFactory.init(farm, path+"covers.txt", path+"groups.txt");
		//CoverManager.display();
		
		// intégration du territoire d'une ferme
		territory = TerritoryFactory.init(path+"sig/site_indre2.shp", start);
		TerritoryFactory.init(territory, farm);			
		
		// mise en place des contraintes
		cb = new ConstraintBuilder(farm);
	}
	
	protected void run(){
		// paramétrisation du simulateur
		CfmFarmManager sm = new CfmFarmManager(farm, 1);
		sm.setPath(path);
		sm.setStart(start);
		sm.setEnd(end);
		sm.addOutput(new ConsoleOutput());
				
		// création et lancement du simulateur
		s = new CfmFarmSimulator(sm);
		s.init(1);
		s.run();
		s.close();
	}
	
	@AfterMethod
	public void after() throws Exception {
		farm = null;
		territory = null;
		cb = null;
	}
	 
}
