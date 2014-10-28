package at.ac.uibk.dps.biohadoop.algorithms.tiledmatrixmul;

public class MatrixMul {
	
	public static double[][] tiledMul(double[][] matrixA,
			double[][] matrixB, int block) {
		int size = matrixA.length;
		double[][] matrixC = new double[size][size];
		for (int iOuter = 0; iOuter < size; iOuter += block) {
			for (int jOuter = 0; jOuter < size; jOuter += block) {
				for (int kOuter = 0; kOuter < size; kOuter += block) {
					for (int i = iOuter; i < Math.min(iOuter + block, size); i++) {
						for (int j = jOuter; j < Math.min(jOuter + block, size); j++) {
							for (int k = kOuter; k < Math.min(kOuter + block,
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
			double[][] matrixB, int block) {
		int size = matrixA.length;
		double[][] matrixC = new double[size][size];
		for (int iOuter = 0; iOuter < size; iOuter += block) {
			for (int jOuter = 0; jOuter < size; jOuter += block) {
				for (int kOuter = 0; kOuter < size; kOuter += block) {
					for (int i = iOuter; i < Math.min(iOuter + block, size); i++) {
						for (int j = jOuter; j < Math.min(jOuter + block, size); j++) {
							for (int k = kOuter; k < Math.min(kOuter + block,
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
