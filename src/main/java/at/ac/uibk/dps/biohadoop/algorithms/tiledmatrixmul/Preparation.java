package at.ac.uibk.dps.biohadoop.algorithms.tiledmatrixmul;

import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.uibk.dps.biohadoop.algorithm.AlgorithmException;
import at.ac.uibk.dps.biohadoop.algorithms.nsgaii.algorithm.NsgaII;
import at.ac.uibk.dps.biohadoop.tasksystem.queue.TaskSubmitter;
import at.ac.uibk.dps.biohadoop.utils.PropertyConverter;

public class Preparation {

	private static final Logger LOG = LoggerFactory
			.getLogger(Preparation.class);

	private final int popSize;
	private final double sbxDistributionFactor;
	private final double mutationFactor;
	private final String matrixLayout;
	private final int matrixSize;
	private final int maxBlockSize;
	private final int iterations;
	private final TaskSubmitter<Matrices, int[], Long> taskSubmitter;

	public Preparation(Map<String, String> properties)
			throws AlgorithmException {
		// Read and parse configuration
		popSize = PropertyConverter.toInt(properties,
				TiledMulAlgorithm.POP_SIZE);
		sbxDistributionFactor = PropertyConverter.toDouble(properties,
				NsgaII.SBX_DISTRIBUTION_FACTOR);
		mutationFactor = PropertyConverter.toDouble(properties,
				TiledMulAlgorithm.MUTATION_FACTOR);
		matrixLayout = PropertyConverter.toString(properties,
				TiledMulAlgorithm.MATRIX_LAYOUT, new String[] { TiledMulAlgorithm.LAYOUT_ROW, TiledMulAlgorithm.LAYOUT_COL });
		matrixSize = PropertyConverter.toInt(properties,
				TiledMulAlgorithm.MATRIX_SIZE);
		maxBlockSize = PropertyConverter.toInt(properties,
				TiledMulAlgorithm.MAX_BLOCK_SIZE);
		iterations = PropertyConverter.toInt(properties,
				TiledMulAlgorithm.ITERATIONS);

		logProperties();

		// Generate random matrices
		final Random rand = new Random();
		double[][] matrixA = generateMatrix(rand, matrixSize);
		double[][] matrixB = generateMatrix(rand, matrixSize);

		if (TiledMulAlgorithm.LAYOUT_ROW.equals(matrixLayout)) {
			// Instanciate TaskSystem
			taskSubmitter = new TaskSubmitter<>(
					AsyncTiledMul.class, new Matrices(matrixA, matrixB));
		} else if (TiledMulAlgorithm.LAYOUT_COL.equals(matrixLayout)) {
			double[][] matrixBColLayout = makeCol(matrixB);
			taskSubmitter = new TaskSubmitter<>(
					AsyncTiledMulWithColLayout.class, new Matrices(matrixA,
							matrixBColLayout));
		}
		else {
			throw new AlgorithmException("Unknown matrix layout: " + matrixLayout);
		}
	}

	private double[][] generateMatrix(Random rand, int size) {
		final double[][] m = new double[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				m[i][j] = rand.nextDouble();
			}
		}
		return m;
	}

	private double[][] makeCol(double[][] matrix) {
		int size = matrix.length;
		double[][] matrixCol = new double[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				matrixCol[i][j] = matrix[j][i];
			}
		}
		return matrixCol;
	}

	public int getPopSize() {
		return popSize;
	}

	public double getSbxDistributionFactor() {
		return sbxDistributionFactor;
	}

	public double getMutationFactor() {
		return mutationFactor;
	}

	public int getMatrixSize() {
		return matrixSize;
	}

	public int getMaxBlockSize() {
		return maxBlockSize;
	}

	public int getIterations() {
		return iterations;
	}

	public TaskSubmitter<Matrices, int[], Long> getTaskSubmitter() {
		return taskSubmitter;
	}

	public void logProperties() {
		LOG.info("----- Parameters: -----");
		LOG.info("POP_SIZE={}", popSize);
		LOG.info("SBX_DISTRIBUTION_FACTOR={}", sbxDistributionFactor);
		LOG.info("MUTATION_FACTOR={}", mutationFactor);
		LOG.info("MATRIX_SIZE={}", matrixSize);
		LOG.info("MAX_BLOCK_SIZE={}", maxBlockSize);
		LOG.info("ITERATIONS={}", iterations);
		LOG.info("-----------------------");
	}

}
