package at.ac.uibk.dps.biohadoop.algorithms.nsgaii.algorithm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import at.ac.uibk.dps.biohadoop.algorithms.nsgaii.algorithm.FastNonDominatedSort;

public class FastNonDominatedSortTest {

	@Test
	public void testTwoNonDominatingInFirstFront() {
		double[][] objectives = new double[][] { { 1.0, 2.0 }, { 2.0, 1.0 } };
		List<List<Integer>> fronts = FastNonDominatedSort.sort(objectives);
		assertEquals(fronts.size(), 1);
		assertEquals(fronts.get(0).size(), 2);
		assertTrue(fronts.get(0).contains(0));
		assertTrue(fronts.get(0).contains(1));
	}
	
	@Test
	public void testThreeAndOneDominatesInFirstFront() {
		double[][] objectives = new double[][] { { 1.0, 1.0 }, { 2.0, 3.0 },
				{ 3.0, 2.0 } };
		List<List<Integer>> fronts = FastNonDominatedSort.sort(objectives);
		assertEquals(fronts.size(), 2);
		assertEquals(fronts.get(0).size(), 1);
		assertEquals(fronts.get(1).size(), 2);
		assertTrue(fronts.get(0).contains(0));
		assertTrue(fronts.get(1).contains(1));
		assertTrue(fronts.get(1).contains(2));
	}
	
	@Test
	public void testThreeAndTwoDominateInFirstFront() {
		double[][] objectives = new double[][] { { 1.0, 2.0 }, { 2.0, 1.0 },
				{ 3.0, 2.0 } };
		List<List<Integer>> fronts = FastNonDominatedSort.sort(objectives);
		assertEquals(fronts.size(), 2);
		assertEquals(fronts.get(0).size(), 2);
		assertEquals(fronts.get(1).size(), 1);
		assertTrue(fronts.get(0).contains(0));
		assertTrue(fronts.get(0).contains(1));
		assertTrue(fronts.get(1).contains(2));
	}
	
	@Test
	public void testThreeAndAllInDifferentFronts() {
		double[][] objectives = new double[][] { { 1.0, 2.0 }, { 2.0, 2.0 },
				{ 3.0, 2.0 } };
		List<List<Integer>> fronts = FastNonDominatedSort.sort(objectives);
		assertEquals(fronts.size(), 3);
		assertEquals(fronts.get(0).size(), 1);
		assertEquals(fronts.get(1).size(), 1);
		assertEquals(fronts.get(2).size(), 1);
		assertTrue(fronts.get(0).contains(0));
		assertTrue(fronts.get(1).contains(1));
		assertTrue(fronts.get(2).contains(2));
	}

}
