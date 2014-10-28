package at.ac.uibk.dps.biohadoop.algorithms.tiledmatrixmul;

public class MatrixMul {
	
	public static double[][] tiledMul(double[][] matrixA,
			double[][] matrixB, int[] blocks) {
		int size = matrixA.length;
		double[][] matrixC = new double[size][size];
		for (int iOuter = 0; iOuter < size; iOuter += blocks[0]) {
			for (int jOuter = 0; jOuter < size; jOuter += blocks[1]) {
				for (int kOuter = 0; kOuter < size; kOuter += blocks[0]) {
					for (int i = iOuter; i < Math.min(iOuter + blocks[0], size); i++) {
						for (int j = jOuter; j < Math.min(jOuter + blocks[1], size); j++) {
							for (int k = kOuter; k < Math.min(kOuter + blocks[0],
									size); k++) {
								matrixC[i][j] += matrixA[i][k] * matrixB[k][j];
							}
						}
					}
				}
			}
		}
		return matrixC;
	}

	public static double[][] tiledMulWithColLayout(double[][] matrixA,
			double[][] matrixB, int[] blocks) {
		int size = matrixA.length;
		double[][] matrixC = new double[size][size];
		for (int iOuter = 0; iOuter < size; iOuter += blocks[0]) {
			for (int jOuter = 0; jOuter < size; jOuter += blocks[1]) {
				for (int kOuter = 0; kOuter < size; kOuter += blocks[0]) {
					for (int i = iOuter; i < Math.min(iOuter + blocks[0], size); i++) {
						for (int j = jOuter; j < Math.min(jOuter + blocks[1], size); j++) {
							for (int k = kOuter; k < Math.min(kOuter + blocks[0],
									size); k++) {
								matrixC[i][j] += matrixA[i][k] * matrixB[j][k];
							}
						}
					}
				}
			}
		}
		return matrixC;
	}
}
