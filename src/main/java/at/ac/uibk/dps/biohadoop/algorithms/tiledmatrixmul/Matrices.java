package at.ac.uibk.dps.biohadoop.algorithms.tiledmatrixmul;

public class Matrices {

	private double[][] matrixA;
	private double[][] matrixB;

	public Matrices() {
	}
	
	public Matrices(double[][] matrixA, double[][] matrixB) {
		this.matrixA = matrixA;
		this.matrixB = matrixB;
	}

	public double[][] getMatrixA() {
		return matrixA;
	}

	public double[][] getMatrixB() {
		return matrixB;
	}

}
