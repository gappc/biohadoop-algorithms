package at.ac.uibk.dps.biohadoop.algorithms.tiledmatrixmul;

import java.util.Random;

/**
 * Implements Parameter based mutation. Implementation taken from:
 * <ul>
 * <li>Deb, Kalyanmoy.
 * "An efficient constraint handling method for genetic algorithms." Computer
 * methods in applied mechanics and engineering 186.2 (2000): 311-338.
 * </ul>
 * 
 * @author Christian Gapp
 *
 */
public class ParamterBasedMutator {

	public static int mutate(int child, double nm, int lower, int upper) {
		Random rand = new Random();
		double u = rand.nextDouble();

		double beta = (double) Math.min(child - lower, upper - child)
				/ (upper - lower);

		double betaq;
		if (u <= 0.5) {
			betaq = Math
					.pow((2.0 * u + (1.0 - 2.0 * u)
							* Math.pow(1.0 - beta, nm + 1.0)), 1.0 / (nm + 1.0)) - 1.0;
		} else {
			betaq = 1.0 - Math
					.pow((2.0 * (1.0 - u) + 2.0 * (u - 0.5)
							* Math.pow(1.0 - beta, nm + 1.0)), 1.0 / (nm + 1.0));
		}

		double mutated = child + betaq * (upper - lower);

		return (int) Math.round(mutated);
	}
}
