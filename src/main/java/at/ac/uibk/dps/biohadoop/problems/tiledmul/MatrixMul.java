package at.ac.uibk.dps.biohadoop.problems.tiledmul;


public class MatrixMul {
	
	public static double[][] tiledMul(double[][] matrixA,
			double[][] matrixB, int[] blocks) {
		int size = matrixA.length;
		double[][] matrixC = new double[size][size];
		for (int iOuter = 0; iOuter < size; iOuter += blocks[0]) {
			int iBorder = Math.min(iOuter + blocks[0], size);
			for (int jOuter = 0; jOuter < size; jOuter += blocks[1]) {
				int jBorder = Math.min(jOuter + blocks[1], size);
				for (int kOuter = 0; kOuter < size; kOuter += blocks[2]) {
					int kBorder = Math.min(kOuter + blocks[2], size);
					for (int i = iOuter; i < iBorder; i++) {
						for (int j = jOuter; j < jBorder; j++) {
							for (int k = kOuter; k < kBorder; k++) {
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
			int iBorder = Math.min(iOuter + blocks[0], size);
			for (int jOuter = 0; jOuter < size; jOuter += blocks[1]) {
				int jBorder = Math.min(jOuter + blocks[1], size);
				for (int kOuter = 0; kOuter < size; kOuter += blocks[1]) {
					int kBorder = Math.min(kOuter + blocks[2], size);
					for (int i = iOuter; i < iBorder; i++) {
						for (int j = jOuter; j < jBorder; j++) {
							for (int k = kOuter; k < kBorder; k++) {
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
