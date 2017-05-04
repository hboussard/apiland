package fr.inra.sad_paysage.apiland.capfarm.model.constraint;

import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

import fr.inra.sad_paysage.apiland.capfarm.model.constraint.ConstraintMode;
import fr.inra.sad_paysage.apiland.capfarm.model.constraint.ConstraintType;
import fr.inra.sad_paysage.apiland.capfarm.model.constraint.CoverAllocationConstraint;

public class OnBooleanConditionConstraintTest extends ConstraintTest {

	@Test
	public void testUnitAlwaysOnBooleanCondition() {
		cb.setCode("C1"); 		
		cb.setCover("M"); 
		cb.setType(ConstraintType.OnBooleanCondition);
		cb.setMode(ConstraintMode.ALWAYS);
		cb.setDomain("[irrigate=TRUE]");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testUnitNeverOnBooleanCondition() {
		cb.setCode("C1"); 		
		cb.setCover("M"); 
		cb.setType(ConstraintType.OnBooleanCondition);
		cb.setMode(ConstraintMode.NEVER);
		cb.setDomain("[irrigate=TRUE]");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testUnitOnlyOnBooleanCondition() {
		cb.setCode("C1"); 		
		cb.setCover("M"); 
		cb.setType(ConstraintType.OnBooleanCondition);
		cb.setMode(ConstraintMode.ONLY);
		cb.setDomain("[irrigate=TRUE]");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testUnitAroundOnBooleanCondition() {
		cb.setCode("C1"); 		
		cb.setCover("M"); 
		cb.setType(ConstraintType.OnBooleanCondition);
		cb.setMode(ConstraintMode.AROUND);
		cb.setDomain("[irrigate=TRUE]");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testUnitsAlwaysOnBooleanCondition() {
		cb.setCode("C1"); 		
		cb.setCover("B","O","M"); 
		cb.setType(ConstraintType.OnBooleanCondition);
		cb.setMode(ConstraintMode.ALWAYS);
		cb.setDomain("[irrigate=TRUE]");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testUnitsNeverOnBooleanCondition() {
		cb.setCode("C1"); 		
		cb.setCover("B","O","M"); 
		cb.setType(ConstraintType.OnBooleanCondition);
		cb.setMode(ConstraintMode.NEVER);
		cb.setDomain("[irrigate=TRUE]");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testUnitsOnlyOnBooleanCondition() {
		cb.setCode("C1"); 		
		cb.setCover("B","O","M"); 
		cb.setType(ConstraintType.OnBooleanCondition);
		cb.setMode(ConstraintMode.ONLY);
		cb.setDomain("[irrigate=TRUE]");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testUnitsAroundOnBooleanCondition() {
		cb.setCode("C1"); 		
		cb.setCover("B","O","M"); 
		cb.setType(ConstraintType.OnBooleanCondition);
		cb.setMode(ConstraintMode.AROUND);
		cb.setDomain("[irrigate=TRUE]");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testGroupAlwaysOnBooleanCondition() {
		cb.setCode("C1"); 		
		cb.setCover("CE"); 
		cb.setType(ConstraintType.OnBooleanCondition);
		cb.setMode(ConstraintMode.ALWAYS);
		cb.setDomain("[irrigate=TRUE]");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testGroupNeverOnBooleanCondition() {
		cb.setCode("C1"); 		
		cb.setCover("CE"); 
		cb.setType(ConstraintType.OnBooleanCondition);
		cb.setMode(ConstraintMode.NEVER);
		cb.setDomain("[irrigate=TRUE]");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testGroupOnlyOnBooleanCondition() {
		cb.setCode("C1"); 		
		cb.setCover("CE"); 
		cb.setType(ConstraintType.OnBooleanCondition);
		cb.setMode(ConstraintMode.ONLY);
		cb.setDomain("[irrigate=TRUE]");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testGroupAroundOnBooleanCondition() {
		cb.setCode("C1"); 		
		cb.setCover("CE"); 
		cb.setType(ConstraintType.OnBooleanCondition);
		cb.setMode(ConstraintMode.AROUND);
		cb.setDomain("[irrigate=TRUE]");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}

}
