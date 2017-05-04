package fr.inra.sad_paysage.apiland.capfarm.model.constraint;

import static org.testng.Assert.assertTrue;
import org.testng.annotations.Test;

import fr.inra.sad_paysage.apiland.capfarm.model.constraint.ConstraintMode;
import fr.inra.sad_paysage.apiland.capfarm.model.constraint.ConstraintType;
import fr.inra.sad_paysage.apiland.capfarm.model.constraint.CoverAllocationConstraint;

public class TotalAreaConstraintTest extends ConstraintTest {

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testUnitAlwaysTotalArea(){
		cb.setCode("C2"); 
		cb.setCover("M"); 	
		cb.setType(ConstraintType.TotalArea);
		cb.setMode(ConstraintMode.ALWAYS);
		cb.setDomain("[20,]");
		cb.build();
		run();
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testUnitAroundTotalArea(){
		cb.setCode("C2"); 
		cb.setCover("M"); 	
		cb.setType(ConstraintType.TotalArea);
		cb.setMode(ConstraintMode.AROUND);
		cb.setDomain("[20,]");
		cb.build();
		run();
	}
	
	@Test
	public void testUnitOnlyTotalArea(){
		cb.setCode("C2"); 
		cb.setCover("M"); 	
		cb.setType(ConstraintType.TotalArea);
		cb.setMode(ConstraintMode.ONLY);
		cb.setDomain("[20,]");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
	
		assertTrue(ca.check(start, end, false));
	}

	@Test
	public void testUnitNeverTotalArea(){
		cb.setCode("C2"); 
		cb.setCover("M"); 	
		cb.setType(ConstraintType.TotalArea);
		cb.setMode(ConstraintMode.NEVER);
		cb.setDomain("[20,]");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
	
		assertTrue(ca.check(start, end, false));
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testUnitsAlwaysTotalArea(){
		cb.setCode("C2"); 
		cb.setCover("B","O","M");
		cb.setType(ConstraintType.TotalArea);
		cb.setMode(ConstraintMode.ALWAYS);
		cb.setDomain("[20,]");
		cb.build();
		run();
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testUnitsAroundTotalArea(){
		cb.setCode("C2"); 
		cb.setCover("B","O","M");
		cb.setType(ConstraintType.TotalArea);
		cb.setMode(ConstraintMode.AROUND);
		cb.setDomain("[20,]");
		cb.build();
		run();
	}
	
	@Test
	public void testUnitsOnlyTotalArea(){
		cb.setCode("C2"); 
		cb.setCover("B","O","M");
		cb.setType(ConstraintType.TotalArea);
		cb.setMode(ConstraintMode.ONLY);
		cb.setDomain("[20,]");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, true));
	}

	@Test
	public void testUnitsNeverTotalArea(){
		cb.setCode("C2"); 
		cb.setCover("B","O","M");
		cb.setType(ConstraintType.TotalArea);
		cb.setMode(ConstraintMode.NEVER);
		cb.setDomain("[20,]");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
	
		assertTrue(ca.check(start, end, false));
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testGroupAlwaysTotalArea(){
		cb.setCode("C2"); 
		cb.setCover("CE"); 	
		cb.setType(ConstraintType.TotalArea);
		cb.setMode(ConstraintMode.ALWAYS);
		cb.setDomain("[20,]");
		cb.build();
		run();
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testGroupAroundTotalArea(){
		cb.setCode("C2"); 
		cb.setCover("CE"); 	
		cb.setType(ConstraintType.TotalArea);
		cb.setMode(ConstraintMode.AROUND);
		cb.setDomain("[20,]");
		cb.build();
		run();
	}
	
	@Test
	public void testGroupOnlyTotalArea(){
		cb.setCode("C2"); 
		cb.setCover("CE"); 	
		cb.setType(ConstraintType.TotalArea);
		cb.setMode(ConstraintMode.ONLY);
		cb.setDomain("[20,]");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
	
		assertTrue(ca.check(start, end, false));
	}

	@Test
	public void testGroupNeverTotalArea(){
		cb.setCode("C2"); 
		cb.setCover("CE"); 	
		cb.setType(ConstraintType.TotalArea);
		cb.setMode(ConstraintMode.NEVER);
		cb.setDomain("[20,]");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
	
		assertTrue(ca.check(start, end, false));
	}

}
