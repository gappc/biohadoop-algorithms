package at.ac.uibk.dps.biohadoop.algorithms.tiledmatrixmul;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.uibk.dps.biohadoop.hadoop.launcher.WorkerParametersResolver;
import at.ac.uibk.dps.biohadoop.solver.ProgressService;
import at.ac.uibk.dps.biohadoop.solver.SolverId;
import at.ac.uibk.dps.biohadoop.tasksystem.algorithm.Algorithm;
import at.ac.uibk.dps.biohadoop.tasksystem.algorithm.AlgorithmException;
import at.ac.uibk.dps.biohadoop.tasksystem.queue.TaskException;
import at.ac.uibk.dps.biohadoop.tasksystem.queue.TaskFuture;
import at.ac.uibk.dps.biohadoop.tasksystem.queue.TaskSubmitter;

public class TiledMulAlgorithm implements Algorithm {

	public static final String POP_SIZE = "POP_SIZE";
	public static final String MUTATION_FACTOR = "MUTATION_FACTOR";
	public static final String MATRIX_LAYOUT = "MATRIX_LAYOUT";
	public static final String MATRIX_SIZE = "MATRIX_SIZE";
	public static final String MAX_BLOCK_SIZE = "MAX_BLOCK_SIZE";
	public static final String ITERATIONS = "ITERATIONS";
	public static final String LAYOUT_ROW = "LAYOUT_ROW";
	public static final String LAYOUT_COL = "LAYOUT_COL";

	private static final Logger LOG = LoggerFactory
			.getLogger(TiledMulAlgorithm.class);

	@Override
	public void run(SolverId solverId, Map<String, String> properties)
			throws AlgorithmException {
		// Read configuration and prepare needed data and objects
		final Preparation prep = new Preparation(properties);

		// Save current population and new new child population as one array.
		// First the current population, then the new child population
		final int[][] population = generatePopulation(prep);
		final long[] fitness = new long[2 * prep.getPopSize()];

		final List<TaskFuture<Long>> futures = new ArrayList<TaskFuture<Long>>();
		// Submit fitness computation for current population to the task system.
		// New Child population needs no fitness computation, as it is
		// overwritten in the following while loop
		for (int i = 0; i < prep.getPopSize(); i++) {
			futures.add(submitFitnessComputation(prep.getTaskSubmitter(),
					population[i]));
		}
		// Wait for all computations to complete
		for (int i = 0; i < prep.getPopSize(); i++) {
			fitness[i] = futures.get(i).get();
		}

		int count = 0;
		int[] blockMax = new int[2];
		int[] blockMin = new int[] { prep.getMaxBlockSize(),
				prep.getMaxBlockSize() };
		long iterationStart = System.nanoTime();
		while (count < prep.getIterations()) {
			long start = System.nanoTime();

			// Recombination and mutation
			for (int i = prep.getPopSize(); i < 2 * prep.getPopSize(); i++) {
				int[] child = recombinePopulation(population, fitness,
						prep.getMaxBlockSize());
				int[] mutatedChild = mutateChild(child,
						prep.getMutationFactor(), prep.getMaxBlockSize());
				population[i] = mutatedChild;
			}

			// Send data to workers
			futures.clear();
			long remoteStart = System.nanoTime();
			for (int i = prep.getPopSize(); i < 2 * prep.getPopSize(); i++) {
				futures.add(submitFitnessComputation(prep.getTaskSubmitter(),
						population[i]));
			}
			// Wait for worker results
			for (int i = 0; i < prep.getPopSize(); i++) {
				fitness[i + prep.getPopSize()] = futures.get(i).get();
			}
			long remoteEnd = System.nanoTime();

			LOG.info("Before sort");
			logIterationResult(population, fitness);

			// Simple Bubble-Sort to sort best individuals to the front
			for (int i = 0; i < 2 * prep.getPopSize(); i++) {
				for (int j = i + 1; j < 2 * prep.getPopSize(); j++) {
					if (fitness[j] < fitness[i]) {
						long tmpFitness = fitness[i];
						fitness[i] = fitness[j];
						fitness[j] = tmpFitness;

						int[] individual = population[i];
						population[i] = population[j];
						population[j] = individual;
					}
				}
			}

			LOG.info("After sort");
			logIterationResult(population, fitness);

			// Compute global maximum and minimum blocksize, just for interest
			for (int i = 0; i < 2 * prep.getPopSize(); i++) {
				for (int j = 0; j < 2; j++) {
					blockMin[j] = Math.min(population[i][j], blockMin[j]);
					blockMax[j] = Math.max(population[i][j], blockMax[j]);
				}
			}

			long end = System.nanoTime();

			LOG.info(
					"iteration={}, blockMin={}, blockMax={}, iteration-time={}ns, time_seq={}ns, time-par={}ns",
					count++, Arrays.toString(blockMin),
					Arrays.toString(blockMax), (end - start), (end - start)
							- (remoteEnd - remoteStart),
					(remoteEnd - remoteStart));

			ProgressService.setProgress(solverId,
					(float) count / prep.getIterations());
		}
		prep.logProperties();

		int workerSize = WorkerParametersResolver.getWorkerParameters().size();
		long iterationTime = System.nanoTime() - iterationStart;
		LOG.info("Workers={}", WorkerParametersResolver.getWorkerParameters()
				.size());
		LOG.info("Iterations={}", prep.getIterations());
		LOG.info("Iteration time {}ns", iterationTime);
		LOG.info("Iteration time parseable ({} {})", workerSize,
				String.format("%.3f", iterationTime / 1e9));
		LOG.info(
				"Iterations per second ({} {})",
				workerSize,
				String.format("%.3f", prep.getIterations()
						/ (iterationTime / 1e9)));

		LOG.info("-----------------------");
	}

	private int[][] generatePopulation(Preparation prep) {
		final Random rand = new Random();
		final int[][] population = new int[2 * prep.getPopSize()][2];
		// We only need to initialize current population, set new population to
		// 0
		for (int i = 0; i < prep.getPopSize(); i++) {
			for (int j = 0; j < 2; j++) {
				// Set blockSize. As a blockSize of 0 would make no sense, we
				// add 1
				population[i][j] = rand.nextInt(prep.getMaxBlockSize()) + 1;
			}
		}
		return population;
	}

	private TaskFuture<Long> submitFitnessComputation(
			TaskSubmitter<int[], Long> taskSubmitter, int[] blocks)
			throws TaskException {
		return taskSubmitter.add(blocks);
	}

	// Using k-tournament with k = 2 for parent selection and SBX for crossover
	private int[] recombinePopulation(int[][] population, long[] fitness,
			int maxBlockSize) {
		final Random rand = new Random();
		int parents[] = new int[2];

		for (int i = 0; i < 2; i++) {
			// Take k = 2 parents from current population (remember, current
			// population is stored in first half of population[])
			int i1 = rand.nextInt(population.length / 2);
			int i2 = rand.nextInt(population.length / 2);
			if (fitness[i1] < fitness[i2]) {
				parents[i] = i1;
			} else {
				parents[i] = i2;
			}
		}

		int[] result = new int[2];
		for (int i = 0; i < 2; i++) {
			int[] children = SBX.bounded(1, population[parents[0]][i],
					population[parents[1]][i], 1, maxBlockSize);
			// Each child has 50% probability to get chosen
			if (rand.nextDouble() < 0.5) {
				result[i] = children[0];
			} else {
				result[i] = children[1];
			}
		}
		return result;
	}

	private int[] mutateChild(int[] child, double mutationFactor,
			int maxBlockSize) {
		int[] result = new int[2];
		for (int i = 0; i < 2; i++) {
			result[i] = ParamterBasedMutator.mutate(child[i], mutationFactor,
					1, maxBlockSize);
		}
		return result;
	}

	private void logIterationResult(int[][] population, long[] fitness) {
		StringBuilder sb = new StringBuilder("[");
		for (int i = 0; i < population.length; i++) {
			sb.append(Arrays.toString(population[i])).append(", ");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append("]");
		LOG.info(sb.toString());
		LOG.info(Arrays.toString(fitness));
	}

}
