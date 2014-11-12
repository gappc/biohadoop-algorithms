package at.ac.uibk.dps.biohadoop.algorithms.nsgaii.algorithm;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import at.ac.uibk.dps.biohadoop.algorithms.nsgaii.algorithm.CrowdingDistance;

public class CrowdingDistanceTest {

	@Test
	public void test() {
		double[][] objectives = new double[][] { { 1.0 }, { 2.0 }, { 2.2 },
				{ 3.0 }, { 4.0 }, { 5.0 } };

		List<Integer> front = new ArrayList<>();
		for (int i = 0; i < objectives.length; i++) {
			front.add(i);
		}
		CrowdingDistance
				.sortFrontAccordingToCrowdingDistance(front, objectives);
		assertEquals(front.get(0).intValue(), 0);
		assertEquals(front.get(1).intValue(), 5);
		assertEquals(front.get(2).intValue(), 4);
		assertEquals(front.get(3).intValue(), 3);
		assertEquals(front.get(4).intValue(), 1);
		assertEquals(front.get(5).intValue(), 2);
	}

}
