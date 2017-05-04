package fr.inra.sad_paysage.apiland.capfarm.model.constraint;

import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

import fr.inra.sad_paysage.apiland.capfarm.model.constraint.ConstraintMode;
import fr.inra.sad_paysage.apiland.capfarm.model.constraint.ConstraintType;
import fr.inra.sad_paysage.apiland.capfarm.model.constraint.CoverAllocationConstraint;

public class DelayConstraintTest extends ConstraintTest {

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testUnitUnitAlwaysDelay() {
		cb.setCode("C1"); 		
		cb.setCover("M"); 
		cb.setType(ConstraintType.Delay);
		cb.setMode(ConstraintMode.ALWAYS);
		cb.setDomain("[3,6]");
		cb.setParams("M");
		cb.build();
		run();
	}
	
	@Test
	public void testUnitUnitNeverDelay() {
		cb.setCode("C1"); 		
		cb.setCover("M"); 
		cb.setType(ConstraintType.Delay);
		cb.setMode(ConstraintMode.NEVER);
		cb.setDomain("[3,6]");
		cb.setParams("M");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testUnitUnitOnlyDelay() {
		cb.setCode("C1"); 		
		cb.setCover("M");  
		cb.setType(ConstraintType.Delay);
		cb.setMode(ConstraintMode.ONLY);
		cb.setDomain("[3,6]");
		cb.setParams("M");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testUnitUnitAroundDelay() {
		cb.setCode("C1"); 		
		cb.setCover("M"); 
		cb.setType(ConstraintType.Delay);
		cb.setMode(ConstraintMode.AROUND);
		cb.setDomain("[3,6]");
		cb.setParams("M");
		cb.build();
		run();
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testUnitsUnitAlwaysDelay() {
		cb.setCode("C1"); 		
		cb.setCover("B","O","M"); 
		cb.setType(ConstraintType.Delay);
		cb.setMode(ConstraintMode.ALWAYS);
		cb.setDomain("[3,6]");
		cb.setParams("M");
		cb.build();
		run();
	}
	
	@Test
	public void testUnitsUnitNeverDelay() {
		cb.setCode("C1"); 		
		cb.setCover("B","O","M"); 
		cb.setType(ConstraintType.Delay);
		cb.setMode(ConstraintMode.NEVER);
		cb.setDomain("[3,6]");
		cb.setParams("M");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testUnitsUnitOnlyDelay() {
		cb.setCode("C1"); 		
		cb.setCover("B","O","M"); 
		cb.setType(ConstraintType.Delay);
		cb.setMode(ConstraintMode.ONLY);
		cb.setDomain("[3,6]");
		cb.setParams("M");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testUnitsUnitAroundDelay() {
		cb.setCode("C1"); 		
		cb.setCover("B","O","M"); 
		cb.setType(ConstraintType.Delay);
		cb.setMode(ConstraintMode.AROUND);
		cb.setDomain("[3,6]");
		cb.setParams("M");
		cb.build();
		run();
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testGroupUnitAlwaysDelay() {
		cb.setCode("C1"); 		
		cb.setCover("CE"); 
		cb.setType(ConstraintType.Delay);
		cb.setMode(ConstraintMode.ALWAYS);
		cb.setDomain("[3,6]");
		cb.setParams("M");
		cb.build();
		run();
	}
	
	@Test
	public void testGroupUnitNeverDelay() {
		cb.setCode("C1"); 		
		cb.setCover("CE"); 
		cb.setType(ConstraintType.Delay);
		cb.setMode(ConstraintMode.NEVER);
		cb.setDomain("[3,6]");
		cb.setParams("M");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testGroupUnitOnlyDelay() {
		cb.setCode("C1"); 		
		cb.setCover("CE");  
		cb.setType(ConstraintType.Delay);
		cb.setMode(ConstraintMode.ONLY);
		cb.setDomain("[3,6]");
		cb.setParams("M");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testGroupUnitAroundDelay() {
		cb.setCode("C1"); 		
		cb.setCover("CE"); 
		cb.setType(ConstraintType.Delay);
		cb.setMode(ConstraintMode.AROUND);
		cb.setDomain("[3,6]");
		cb.setParams("M");
		cb.build();
		run();
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testUnitUnitsAlwaysDelay() {
		cb.setCode("C1"); 		
		cb.setCover("M"); 
		cb.setType(ConstraintType.Delay);
		cb.setMode(ConstraintMode.ALWAYS);
		cb.setDomain("[3,6]");
		cb.setParams("B","O","M"); 
		cb.build();
		run();
	}
	
	@Test
	public void testUnitUnitsNeverDelay() {
		cb.setCode("C1"); 		
		cb.setCover("M"); 
		cb.setType(ConstraintType.Delay);
		cb.setMode(ConstraintMode.NEVER);
		cb.setDomain("[3,6]");
		cb.setParams("B","O","M"); 
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testUnitUnitsOnlyDelay() {
		cb.setCode("C1"); 		
		cb.setCover("M");  
		cb.setType(ConstraintType.Delay);
		cb.setMode(ConstraintMode.ONLY);
		cb.setDomain("[3,6]");
		cb.setParams("B","O","M"); 
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testUnitUnitsAroundDelay() {
		cb.setCode("C1"); 		
		cb.setCover("M"); 
		cb.setType(ConstraintType.Delay);
		cb.setMode(ConstraintMode.AROUND);
		cb.setDomain("[3,6]");
		cb.setParams("B","O","M"); 
		cb.build();
		run();
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testUnitsUnitsAlwaysDelay() {
		cb.setCode("C1"); 		
		cb.setCover("B","O","M"); 
		cb.setType(ConstraintType.Delay);
		cb.setMode(ConstraintMode.ALWAYS);
		cb.setDomain("[3,6]");
		cb.setParams("B","O","M"); 
		cb.build();
		run();
	}
	
	@Test
	public void testUnitsUnitsNeverDelay() {
		cb.setCode("C1"); 		
		cb.setCover("B","O","M"); 
		cb.setType(ConstraintType.Delay);
		cb.setMode(ConstraintMode.NEVER);
		cb.setDomain("[3,6]");
		cb.setParams("B","O","M"); 
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testUnitsUnitsOnlyDelay() {
		cb.setCode("C1"); 		
		cb.setCover("B","O","M"); 
		cb.setType(ConstraintType.Delay);
		cb.setMode(ConstraintMode.ONLY);
		cb.setDomain("[3,6]");
		cb.setParams("B","O","M"); 
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testUnitsUnitsAroundDelay() {
		cb.setCode("C1"); 		
		cb.setCover("B","O","M"); 
		cb.setType(ConstraintType.Delay);
		cb.setMode(ConstraintMode.AROUND);
		cb.setDomain("[3,6]");
		cb.setParams("B","O","M"); 
		cb.build();
		run();
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testGroupUnitsAlwaysDelay() {
		cb.setCode("C1"); 		
		cb.setCover("CE"); 
		cb.setType(ConstraintType.Delay);
		cb.setMode(ConstraintMode.ALWAYS);
		cb.setDomain("[3,6]");
		cb.setParams("B","O","M"); 
		cb.build();
		run();
	}
	
	@Test
	public void testGroupUnitsNeverDelay() {
		cb.setCode("C1"); 		
		cb.setCover("CE"); 
		cb.setType(ConstraintType.Delay);
		cb.setMode(ConstraintMode.NEVER);
		cb.setDomain("[3,6]");
		cb.setParams("B","O","M"); 
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testGroupUnitsOnlyDelay() {
		cb.setCode("C1"); 		
		cb.setCover("CE");  
		cb.setType(ConstraintType.Delay);
		cb.setMode(ConstraintMode.ONLY);
		cb.setDomain("[3,6]");
		cb.setParams("B","O","M"); 
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testGroupUnitsAroundDelay() {
		cb.setCode("C1"); 		
		cb.setCover("CE"); 
		cb.setType(ConstraintType.Delay);
		cb.setMode(ConstraintMode.AROUND);
		cb.setDomain("[3,6]");
		cb.setParams("B","O","M"); 
		cb.build();
		run();
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testUnitGroupAlwaysDelay() {
		cb.setCode("C1"); 		
		cb.setCover("M"); 
		cb.setType(ConstraintType.Delay);
		cb.setMode(ConstraintMode.ALWAYS);
		cb.setDomain("[3,6]");
		cb.setParams("CE");
		cb.build();
		run();
	}
	
	@Test
	public void testUnitGroupNeverDelay() {
		cb.setCode("C1"); 		
		cb.setCover("M"); 
		cb.setType(ConstraintType.Delay);
		cb.setMode(ConstraintMode.NEVER);
		cb.setDomain("[3,6]");
		cb.setParams("CE");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	//@Test
	public void testUnitGroupOnlyDelay() {
		cb.setCode("C1"); 		
		cb.setCover("M");  
		cb.setType(ConstraintType.Delay);
		cb.setMode(ConstraintMode.ONLY);
		cb.setDomain("[3,6]");
		cb.setParams("CE");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testUnitGroupAroundDelay() {
		cb.setCode("C1"); 		
		cb.setCover("M"); 
		cb.setType(ConstraintType.Delay);
		cb.setMode(ConstraintMode.AROUND);
		cb.setDomain("[3,6]");
		cb.setParams("CE");
		cb.build();
		run();
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testUnitsGroupAlwaysDelay() {
		cb.setCode("C1"); 		
		cb.setCover("B","O","M"); 
		cb.setType(ConstraintType.Delay);
		cb.setMode(ConstraintMode.ALWAYS);
		cb.setDomain("[3,6]");
		cb.setParams("CE");
		cb.build();
		run();
	}
	
	@Test
	public void testUnitsGroupNeverDelay() {
		cb.setCode("C1"); 		
		cb.setCover("B","O","M"); 
		cb.setType(ConstraintType.Delay);
		cb.setMode(ConstraintMode.NEVER);
		cb.setDomain("[3,6]");
		cb.setParams("CE");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testUnitsGroupOnlyDelay() {
		cb.setCode("C1"); 		
		cb.setCover("B","O","M"); 
		cb.setType(ConstraintType.Delay);
		cb.setMode(ConstraintMode.ONLY);
		cb.setDomain("[3,6]");
		cb.setParams("CE");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testUnitsGroupAroundDelay() {
		cb.setCode("C1"); 		
		cb.setCover("B","O","M"); 
		cb.setType(ConstraintType.Delay);
		cb.setMode(ConstraintMode.AROUND);
		cb.setDomain("[3,6]");
		cb.setParams("CE");
		cb.build();
		run();
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testGroupGroupAlwaysDelay() {
		cb.setCode("C1"); 		
		cb.setCover("CE"); 
		cb.setType(ConstraintType.Delay);
		cb.setMode(ConstraintMode.ALWAYS);
		cb.setDomain("[3,6]");
		cb.setParams("CE");
		cb.build();
		run();
	}
	
	@Test
	public void testGroupGroupNeverDelay() {
		cb.setCode("C1"); 		
		cb.setCover("CE"); 
		cb.setType(ConstraintType.Delay);
		cb.setMode(ConstraintMode.NEVER);
		cb.setDomain("[3,6]");
		cb.setParams("CE");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testGroupGroupOnlyDelay() {
		cb.setCode("C1"); 		
		cb.setCover("CE");  
		cb.setType(ConstraintType.Delay);
		cb.setMode(ConstraintMode.ONLY);
		cb.setDomain("[3,6]");
		cb.setParams("CE");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}

}
