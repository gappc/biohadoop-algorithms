package at.ac.uibk.dps.biohadoop.operators;

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

	public static int[][] bounded(double nc, int[] parent1, int[] parent2,
			int lower, int upper) {
		int[][] result = new int[2][parent1.length];
		for (int i = 0; i < parent1.length; i++) {
			int p1 = parent1[i];
			int p2 = parent2[i];
			
			if (Math.abs(p1 - p2) < EPS) {
				result[0][i] = p1;
				result[1][i] = p2;
				continue;
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

			int y1 = (int) Math.round(0.5 * ((x1 + x2) - betaq
					* Math.abs(x2 - x1)));
			int y2 = (int) Math.round(0.5 * ((x1 + x2) + betaq
					* Math.abs(x2 - x1)));

			result[0][i] = y1;
			result[1][i] = y2;
		}
		return result;
	}

	public static double[][] bounded(double nc, double[] parent1,
			double[] parent2, double lower, double upper) {
		double[][] result = new double[2][parent1.length];
		for (int i = 0; i < parent1.length; i++) {
			double p1 = parent1[i];
			double p2 = parent2[i];

			if (Math.abs(p1 - p2) < EPS) {
				result[0][i] = p1;
				result[1][i] = p2;
				continue;
			}
			double u = rand.nextDouble();

			double x1 = p1;
			double x2 = p2;
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

			double y1 = 0.5 * ((x1 + x2) - betaq * Math.abs(x2 - x1));
			double y2 = 0.5 * ((x1 + x2) + betaq * Math.abs(x2 - x1));

			result[0][i] = y1;
			result[1][i] = y2;
			if (Double.isNaN(y1) || Double.isNaN(y2) || y1 < 0.0 || y2 < 0.0) {
				System.out.println("NONONO");
			}
		}
		return result;
	}

}
