package at.ac.uibk.dps.biohadoop.algorithms.tiledmatrixmul;

import java.util.Random;

/**
 * Implements Simulated Binary Crossover (SBX). Generates two childs from two
 * parents. Implementation inspired by:
 * <ul>
 * <li>Deb, Kalyanmoy.
 * "An efficient constraint handling method for genetic algorithms." Computer
 * methods in applied mechanics and engineering 186.2 (2000): 311-338.
 * <li>jmetal (http://jmetal.sourceforge.net/)
 * </ul>
 * 
 * @author Christian Gapp
 *
 */
public class SBX {

	private static final Random rand = new Random();
	private static final double EPS = 1e-14;

	public static int[] unbounded(double nc, int x1, int x2) {
		if (Math.abs(x1 - x2) < EPS) {
			return new int[] { x1, x2 };
		}
		double u = rand.nextDouble();
		double beta;
		if (u <= 0.5) {
			beta = Math.pow(2 * u, 1 / (nc + 1));
		} else {
			beta = Math.pow(1 / (2 * (1 - u)), 1 / (nc + 1));
		}

		int y1 = (int) (0.5 * ((x1 + x2) - beta * Math.abs(x2 - x1)));
		int y2 = (int) (0.5 * ((x1 + x2) + beta * Math.abs(x2 - x1)));

		return new int[] { y1, y2 };
	}

	public static int[] bounded(double nc, int p1, int p2, int lower, int upper) {
		if (Math.abs(p1 - p2) < EPS) {
			return new int[] { p1, p2 };
		}
		double u = rand.nextDouble();

		int x1 = p1;
		int x2 = p2;
		if (x2 < x1) {
			x1 = p2;
			x2 = p1;
		}

		double beta = 1.0 + (2.0 * Math.min(x1 - lower, upper - x2))
				/ (x2 - x1);
		double alpha = 2.0 - Math.pow(beta, -(nc + 1.0));

		double betaq;
		if (u <= 0.5) {
			betaq = Math.pow(alpha * u, 1.0 / (nc + 1.0));
		} else {
			betaq = Math.pow(1.0 / (2.0 - alpha * u), 1.0 / (nc + 1.0));
		}

		int y1 = (int) Math
				.round(0.5 * ((x1 + x2) - betaq * Math.abs(x2 - x1)));
		int y2 = (int) Math
				.round(0.5 * ((x1 + x2) + betaq * Math.abs(x2 - x1)));

		return new int[] { y1, y2 };
	}

}
