package fr.inra.sad_paysage.apiland.capfarm.model.constraint;

import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

import fr.inra.sad_paysage.apiland.capfarm.model.constraint.ConstraintMode;
import fr.inra.sad_paysage.apiland.capfarm.model.constraint.ConstraintType;
import fr.inra.sad_paysage.apiland.capfarm.model.constraint.CoverAllocationConstraint;

public class RepetitionConstraintTest extends ConstraintTest {

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testUnitAlwaysRepetition() {
		cb.setCode("C1"); 		
		cb.setCover("M"); 
		cb.setType(ConstraintType.Repetition);
		cb.setMode(ConstraintMode.ALWAYS);
		cb.setDomain("[1,1]");
		cb.setParams("middle");
		cb.build();
		run();
	}
	
	@Test
	public void testUnitNeverRepetition() {
		cb.setCode("C1"); 		
		cb.setCover("M"); 
		cb.setType(ConstraintType.Repetition);
		cb.setMode(ConstraintMode.NEVER);
		cb.setDomain("[1,1]");
		cb.setParams("middle");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void test2UnitNeverRepetition() {
		cb.setCode("C1"); 		
		cb.setCover("M"); 
		cb.setType(ConstraintType.Repetition);
		cb.setMode(ConstraintMode.NEVER);
		cb.setDomain("[2,4]");
		cb.setParams("middle");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testUnitOnlyRepetition() {
		cb.setCode("C1"); 		
		cb.setCover("M"); 
		cb.setType(ConstraintType.Repetition);
		cb.setMode(ConstraintMode.ONLY);
		cb.setDomain("[1,1]");
		cb.setParams("middle");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void test2UnitOnlyRepetition() {
		cb.setCode("C1"); 		
		cb.setCover("M"); 
		cb.setType(ConstraintType.Repetition);
		cb.setMode(ConstraintMode.ONLY);
		cb.setDomain("[2,4]");
		cb.setParams("middle");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testUnitAroundRepetition() {
		cb.setCode("C1"); 		
		cb.setCover("M"); 
		cb.setType(ConstraintType.Repetition);
		cb.setMode(ConstraintMode.AROUND);
		cb.setDomain("[1,1]");
		cb.setParams("middle");
		cb.build();
		run();
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testUnitsAlwaysRepetition() {
		cb.setCode("C1"); 		
		cb.setCover("B","O","M");
		cb.setType(ConstraintType.Repetition);
		cb.setMode(ConstraintMode.ALWAYS);
		cb.setDomain("[1,1]");
		cb.setParams("middle");
		cb.build();
		run();
	}
	
	@Test
	public void testUnitsNeverRepetition() {
		cb.setCode("C1"); 		
		cb.setCover("B","O","M");
		cb.setType(ConstraintType.Repetition);
		cb.setMode(ConstraintMode.NEVER);
		cb.setDomain("[1,1]");
		cb.setParams("middle");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void test2UnitsNeverRepetition() {
		cb.setCode("C1"); 		
		cb.setCover("B","O","M");
		cb.setType(ConstraintType.Repetition);
		cb.setMode(ConstraintMode.NEVER);
		cb.setDomain("[2,4]");
		cb.setParams("middle");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testUnitsOnlyRepetition() {
		cb.setCode("C1"); 		
		cb.setCover("B","O","M");
		cb.setType(ConstraintType.Repetition);
		cb.setMode(ConstraintMode.ONLY);
		cb.setDomain("[1,1]");
		cb.setParams("middle");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void test2UnitsOnlyRepetition() {
		cb.setCode("C1"); 		
		cb.setCover("B","O","M");
		cb.setType(ConstraintType.Repetition);
		cb.setMode(ConstraintMode.ONLY);
		cb.setDomain("[2,4]");
		cb.setParams("middle");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testUnitsAroundRepetition() {
		cb.setCode("C1"); 		
		cb.setCover("B","O","M");
		cb.setType(ConstraintType.Repetition);
		cb.setMode(ConstraintMode.AROUND);
		cb.setDomain("[1,1]");
		cb.setParams("middle");
		cb.build();
		run();
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testGroupAlwaysRepetition() {
		cb.setCode("C1"); 		
		cb.setCover("CE"); 
		cb.setType(ConstraintType.Repetition);
		cb.setMode(ConstraintMode.ALWAYS);
		cb.setDomain("[1,1]");
		cb.setParams("middle");
		cb.build();
		run();
	}
	
	@Test
	public void testGroupNeverRepetition() {
		cb.setCode("C1"); 		
		cb.setCover("CE"); 
		cb.setType(ConstraintType.Repetition);
		cb.setMode(ConstraintMode.NEVER);
		cb.setDomain("[1,1]");
		cb.setParams("middle");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void test2GroupNeverRepetition() {
		cb.setCode("C1"); 		
		cb.setCover("CE"); 
		cb.setType(ConstraintType.Repetition);
		cb.setMode(ConstraintMode.NEVER);
		cb.setDomain("[2,4]");
		cb.setParams("middle");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testGroupOnlyRepetition() {
		cb.setCode("C1"); 		
		cb.setCover("CE"); 
		cb.setType(ConstraintType.Repetition);
		cb.setMode(ConstraintMode.ONLY);
		cb.setDomain("[1,1]");
		cb.setParams("middle");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void test2GroupOnlyRepetition() {
		cb.setCode("C1"); 		
		cb.setCover("CE"); 
		cb.setType(ConstraintType.Repetition);
		cb.setMode(ConstraintMode.ONLY);
		cb.setDomain("[2,4]");
		cb.setParams("middle");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testGroupAroundRepetition() {
		cb.setCode("C1"); 		
		cb.setCover("CE"); 
		cb.setType(ConstraintType.Repetition);
		cb.setMode(ConstraintMode.AROUND);
		cb.setDomain("[1,1]");
		cb.setParams("middle");
		cb.build();
		run();
	}

}
