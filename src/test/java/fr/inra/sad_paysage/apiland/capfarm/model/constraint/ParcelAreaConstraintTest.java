package fr.inra.sad_paysage.apiland.capfarm.model.constraint;

import static org.testng.Assert.assertTrue;
import org.testng.annotations.Test;

import fr.inra.sad_paysage.apiland.capfarm.model.constraint.ConstraintMode;
import fr.inra.sad_paysage.apiland.capfarm.model.constraint.ConstraintType;
import fr.inra.sad_paysage.apiland.capfarm.model.constraint.CoverAllocationConstraint;

public class ParcelAreaConstraintTest extends ConstraintTest {

	@Test
	public void testUnitAlwaysParcelArea() {
		cb.setCode("C1"); 		
		cb.setCover("M"); 
		cb.setType(ConstraintType.ParcelArea);
		cb.setMode(ConstraintMode.ALWAYS);
		cb.setDomain("[0,5]");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testUnitNeverParcelArea() {
		cb.setCode("C1"); 		
		cb.setCover("M"); 
		cb.setType(ConstraintType.ParcelArea);
		cb.setMode(ConstraintMode.NEVER);
		cb.setDomain("[0,5]");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testUnitOnlyParcelArea() {
		cb.setCode("C1"); 		
		cb.setCover("M"); 
		cb.setType(ConstraintType.ParcelArea);
		cb.setMode(ConstraintMode.ONLY);
		cb.setDomain("[0,5]");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testUnitAroundParcelArea() {
		cb.setCode("C1"); 		
		cb.setCover("M"); 
		cb.setType(ConstraintType.ParcelArea);
		cb.setMode(ConstraintMode.AROUND);
		cb.setDomain("[0,5]");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testUnitsAlwaysParcelArea() {
		cb.setCode("C1"); 		
		cb.setCover("B","O","M"); 
		cb.setType(ConstraintType.ParcelArea);
		cb.setMode(ConstraintMode.ALWAYS);
		cb.setDomain("[0,5]");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testUnitsNeverParcelArea() {
		cb.setCode("C1"); 		
		cb.setCover("B","O","M"); 
		cb.setType(ConstraintType.ParcelArea);
		cb.setMode(ConstraintMode.NEVER);
		cb.setDomain("[0,5]");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testUnitsOnlyParcelArea() {
		cb.setCode("C1"); 		
		cb.setCover("B","O","M");
		cb.setType(ConstraintType.ParcelArea);
		cb.setMode(ConstraintMode.ONLY);
		cb.setDomain("[0,5]");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testUnitsAroundParcelArea() {
		cb.setCode("C1"); 		
		cb.setCover("B","O","M");
		cb.setType(ConstraintType.ParcelArea);
		cb.setMode(ConstraintMode.AROUND);
		cb.setDomain("[0,5]");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testGroupAlwaysParcelArea() {
		cb.setCode("C1"); 		
		cb.setCover("CE"); 
		cb.setType(ConstraintType.ParcelArea);
		cb.setMode(ConstraintMode.ALWAYS);
		cb.setDomain("[0,5]");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testGroupNeverParcelArea() {
		cb.setCode("C1"); 		
		cb.setCover("CE"); 
		cb.setType(ConstraintType.ParcelArea);
		cb.setMode(ConstraintMode.NEVER);
		cb.setDomain("[0,5]");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testGroupOnlyParcelArea() {
		cb.setCode("C1"); 		
		cb.setCover("CE"); 
		cb.setType(ConstraintType.ParcelArea);
		cb.setMode(ConstraintMode.ONLY);
		cb.setDomain("[0,5]");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testGroupAroundParcelArea() {
		cb.setCode("C1"); 		
		cb.setCover("CE"); 
		cb.setType(ConstraintType.ParcelArea);
		cb.setMode(ConstraintMode.AROUND);
		cb.setDomain("[0,5]");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}

}
