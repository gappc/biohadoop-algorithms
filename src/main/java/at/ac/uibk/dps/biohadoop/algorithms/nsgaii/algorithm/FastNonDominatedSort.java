package at.ac.uibk.dps.biohadoop.algorithms.nsgaii.algorithm;

import java.util.ArrayList;
import java.util.List;

public class FastNonDominatedSort {

	// taken from http://repository.ias.ac.in/83498/1/2-a.pdf
	/**
	 * Computes the front, where the main List holds the fronts, e.g.
	 * frontRanking.get(0) means the first front, frontRanking.get(1) the second
	 * front... The entries of each front point to the index of the individual
	 * and its corresponding objective in the population
	 * 
	 * @param objectives
	 * @return
	 */
	public static List<List<Integer>> sort(double[][] objectives) {
		int size = objectives.length;
		
		List<List<Integer>> fronts = new ArrayList<List<Integer>>();
		fronts.add(new ArrayList<Integer>());

		List<List<Integer>> S = new ArrayList<List<Integer>>();
		for (int i = 0; i < size + 1; i++) {
			S.add(new ArrayList<Integer>());
		}

		int[] n = new int[size];

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				double[] p = objectives[i];
				double[] q = objectives[j];
				// if p (= combinedPopulation[i]) dominates q (=
				// combinedPopulation[j]) then include q in Sp (which is S[i])
				if (dominates(p, q)) {
					// solution i dominates solution j
					S.get(i).add(j);
				}
				// if p is dominated by q then increment np (which is n[i])
				else if (dominates(q, p)) {
					// solution i is dominated by an additional other solution
					n[i]++;
				}
			}
			// if no solution dominates p then p is a member of the first front
			if (n[i] == 0) {
				fronts.get(0).add(i);
			}
		}

		int i = 0;
		while (!fronts.get(i).isEmpty()) {
			List<Integer> H = new ArrayList<Integer>();
			for (int memberP : fronts.get(i)) {
				for (int memberQ : S.get(memberP)) {
					n[memberQ]--;
					if (n[memberQ] == 0) {
						H.add(memberQ);
					}
				}
			}
			i++;
			fronts.add(H);
		}
		fronts.remove(fronts.size() - 1);
		return fronts;
	}
	
	private static boolean dominates(double[] ds, double[] ds2) {
		boolean lesser = false;
		for (int i = 0; i < ds.length; i++) {
			if (ds[i] > ds2[i]) {
				return false;
			}
			if (ds[i] < ds2[i]) {
				lesser = true;
			}
		}
		return lesser;
	}
}
