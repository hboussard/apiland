package fr.inra.sad_paysage.apiland.capfarm.model.constraint;

import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

import fr.inra.sad_paysage.apiland.capfarm.model.constraint.ConstraintMode;
import fr.inra.sad_paysage.apiland.capfarm.model.constraint.ConstraintType;
import fr.inra.sad_paysage.apiland.capfarm.model.constraint.CoverAllocationConstraint;

public class DurationConstraintTest extends ConstraintTest {

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testUnitAlwaysDuration() {
		cb.setCode("C1"); 		
		cb.setCover("M"); 
		cb.setType(ConstraintType.Duration);
		cb.setMode(ConstraintMode.ALWAYS);
		cb.setDomain("[1,1]");
		cb.setParams("middle");
		cb.build();
		run();
	}
	
	@Test
	public void testUnitNeverDuration() {
		cb.setCode("C1"); 		
		cb.setCover("M"); 
		cb.setType(ConstraintType.Duration);
		cb.setMode(ConstraintMode.NEVER);
		cb.setDomain("[1,1]");
		cb.setParams("middle");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testUnitOnlyDuration() {
		cb.setCode("C1"); 		
		cb.setCover("M"); 
		cb.setType(ConstraintType.Duration);
		cb.setMode(ConstraintMode.ONLY);
		cb.setDomain("[1,1]");
		cb.setParams("middle");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void test2UnitOnlyDuration() {
		cb.setCode("C1"); 		
		cb.setCover("M"); 
		cb.setType(ConstraintType.Duration);
		cb.setMode(ConstraintMode.ONLY);
		cb.setDomain("[4,6]");
		cb.setParams("middle");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testUnitAroundDuration() {
		cb.setCode("C1"); 		
		cb.setCover("M"); 
		cb.setType(ConstraintType.Duration);
		cb.setMode(ConstraintMode.AROUND);
		cb.setDomain("[1,1]");
		cb.setParams("middle");
		cb.build();
		run();
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testUnitsAlwaysDuration() {
		cb.setCode("C1"); 		
		cb.setCover("B","O","M");
		cb.setType(ConstraintType.Duration);
		cb.setMode(ConstraintMode.ALWAYS);
		cb.setDomain("[1,1]");
		cb.setParams("middle");
		cb.build();
		run();
	}
	
	@Test
	public void testUnitsNeverDuration() {
		cb.setCode("C1"); 		
		cb.setCover("B","O","M");
		cb.setType(ConstraintType.Duration);
		cb.setMode(ConstraintMode.NEVER);
		cb.setDomain("[1,1]");
		cb.setParams("middle");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testUnitsOnlyDuration() {
		cb.setCode("C1"); 		
		cb.setCover("B","O","M");
		cb.setType(ConstraintType.Duration);
		cb.setMode(ConstraintMode.ONLY);
		cb.setDomain("[1,1]");
		cb.setParams("middle");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testUnitsAroundDuration() {
		cb.setCode("C1"); 		
		cb.setCover("B","O","M");
		cb.setType(ConstraintType.Duration);
		cb.setMode(ConstraintMode.AROUND);
		cb.setDomain("[1,1]");
		cb.setParams("middle");
		cb.build();
		run();
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testGroupAlwaysDuration() {
		cb.setCode("C1"); 		
		cb.setCover("CE"); 
		cb.setType(ConstraintType.Duration);
		cb.setMode(ConstraintMode.ALWAYS);
		cb.setDomain("[1,1]");
		cb.setParams("middle");
		cb.build();
		run();
	}
	
	@Test
	public void testGroupNeverDuration() {
		cb.setCode("C1"); 		
		cb.setCover("CE"); 
		cb.setType(ConstraintType.Duration);
		cb.setMode(ConstraintMode.NEVER);
		cb.setDomain("[1,1]");
		cb.setParams("middle");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testGroupOnlyDuration() {
		cb.setCode("C1"); 		
		cb.setCover("CE"); 
		cb.setType(ConstraintType.Duration);
		cb.setMode(ConstraintMode.ONLY);
		cb.setDomain("[1,1]");
		cb.setParams("middle");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testGroupAroundDuration() {
		cb.setCode("C1"); 		
		cb.setCover("CE"); 
		cb.setType(ConstraintType.Duration);
		cb.setMode(ConstraintMode.AROUND);
		cb.setDomain("[1,1]");
		cb.setParams("middle");
		cb.build();
		run();
	}

}
