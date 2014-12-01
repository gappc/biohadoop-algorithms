package at.ac.uibk.dps.biohadoop.algorithms.mopso;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CrowdingDistance {

	public static void sortFrontAccordingToCrowdingDistance(
			List<Integer> front, List<Particle> particles) {

		int objectiveCount = particles.get(0).getCurrentValue().length;
		double[] crowdingDistance = new double[particles.size()];
		// m is objective
		for (int m = 0; m < objectiveCount; m++) {
			// int[] sorted = sortAccordingObjective(objectives, front, m);
			sortFrontAccordingToObjective(front, particles, m);

			crowdingDistance[front.get(0)] = Double.MAX_VALUE;
			crowdingDistance[front.get(front.size() - 1)] = Double.MAX_VALUE;

			// double fmin = objectiveValues[sorted[0]][m];
			// double fmax = objectiveValues[sorted[l - 1]][m];

			for (int i = 1; i < front.size() - 1; i++) {
				Particle particle = particles.get(front.get(i + 1));
				crowdingDistance[front.get(i)] += particle.getCurrentValue()[m]
						- particle.getCurrentValue()[m];
			}
		}

		sortFrontAccordingToCrowdingDistance(front, crowdingDistance);
	}

	private static void sortFrontAccordingToObjective(List<Integer> front,
			final List<Particle> particles, final int m) {
		Collections.sort(front, new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				Particle p1 = particles.get(o1);
				Particle p2 = particles.get(o2);
				if (p1.getCurrentValue()[m] == p2.getCurrentValue()[m]) {
					return 0;
				}
				return p1.getCurrentValue()[m] < p2.getCurrentValue()[m] ? -1 : 1;
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
