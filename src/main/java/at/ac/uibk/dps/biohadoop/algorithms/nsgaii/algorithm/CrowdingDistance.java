package at.ac.uibk.dps.biohadoop.algorithms.nsgaii.algorithm;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CrowdingDistance {

	public static void sortFrontAccordingToCrowdingDistance(
			List<Integer> front, double[][] objectives) {

		double[] crowdingDistance = new double[objectives.length];
		// m is objective
		for (int m = 0; m < objectives[0].length; m++) {
			// int[] sorted = sortAccordingObjective(objectives, front, m);
			sortFrontAccordingToObjective(front, objectives, m);

			crowdingDistance[front.get(0)] = Double.MAX_VALUE;
			crowdingDistance[front.get(front.size() - 1)] = Double.MAX_VALUE;

			// double fmin = objectiveValues[sorted[0]][m];
			// double fmax = objectiveValues[sorted[l - 1]][m];

			for (int i = 1; i < front.size() - 1; i++) {
				crowdingDistance[front.get(i)] += objectives[front.get(i + 1)][m] - objectives[front
						.get(i - 1)][m];
			}
		}

		sortFrontAccordingToCrowdingDistance(front, crowdingDistance);
	}

	private static void sortFrontAccordingToObjective(List<Integer> front,
			final double[][] objectives, final int m) {
		Collections.sort(front, new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				if (objectives[o1][m] == objectives[o2][m]) {
					return 0;
				}
				return objectives[o1][m] < objectives[o2][m] ? -1 : 1;
			}
		});
	}

	private static void sortFrontAccordingToCrowdingDistance(
			List<Integer> front, final double[] crowdingDistance) {
		Collections.sort(front, new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				if (crowdingDistance[o1] == crowdingDistance[o2]) {
					return 0;
				}
				return crowdingDistance[o1] > crowdingDistance[o2] ? -1 : 1;
			}
		});
	}

}
