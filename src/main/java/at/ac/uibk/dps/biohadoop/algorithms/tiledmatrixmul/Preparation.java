package at.ac.uibk.dps.biohadoop.algorithms.tiledmatrixmul;

import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.uibk.dps.biohadoop.algorithm.AlgorithmException;
import at.ac.uibk.dps.biohadoop.tasksystem.submitter.SimpleTaskSubmitter;
import at.ac.uibk.dps.biohadoop.tasksystem.submitter.TaskSubmitter;
import at.ac.uibk.dps.biohadoop.utils.PropertyConverter;

public class Preparation {

	private static final Logger LOG = LoggerFactory
			.getLogger(Preparation.class);

	private final int popSize;
	private final int mutationFactor;
	private final int matrixSize;
	private final int maxBlockSize;
	private final int iterations;
	private final TaskSubmitter<Integer, Long> taskSubmitter;

	public Preparation(Map<String, String> properties)
			throws AlgorithmException {
		// Read and parse configuration
		popSize = PropertyConverter.toInt(properties, TiledMulAlgorithm.POP_SIZE);
		mutationFactor = PropertyConverter.toInt(properties,
				TiledMulAlgorithm.MUTATION_FACTOR);
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

		// Instanciate TaskSystem
		taskSubmitter = new SimpleTaskSubmitter<Matrices, Integer, Long>(
				AsyncTiledMul.class, new Matrices(matrixA, matrixB));
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

	public int getPopSize() {
		return popSize;
	}

	public int getMutationFactor() {
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

	public TaskSubmitter<Integer, Long> getTaskSubmitter() {
		return taskSubmitter;
	}

	public void logProperties() {
		LOG.info("----- Parameters: -----");
		LOG.info("POP_SIZE={}", popSize);
		LOG.info("MUTATION_FACTOR={}", mutationFactor);
		LOG.info("MATRIX_SIZE={}", matrixSize);
		LOG.info("MAX_BLOCK_SIZE={}", maxBlockSize);
		LOG.info("ITERATIONS={}", iterations);
		LOG.info("-----------------------");
	}

}
