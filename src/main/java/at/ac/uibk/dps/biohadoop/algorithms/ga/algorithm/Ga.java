package at.ac.uibk.dps.biohadoop.algorithms.ga.algorithm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.uibk.dps.biohadoop.algorithm.Algorithm;
import at.ac.uibk.dps.biohadoop.algorithm.AlgorithmException;
import at.ac.uibk.dps.biohadoop.algorithm.AlgorithmId;
import at.ac.uibk.dps.biohadoop.algorithm.AlgorithmService;
import at.ac.uibk.dps.biohadoop.algorithms.ga.DistancesGlobal;
import at.ac.uibk.dps.biohadoop.algorithms.ga.distribution.GaData;
import at.ac.uibk.dps.biohadoop.algorithms.ga.remote.RemoteFitness;
import at.ac.uibk.dps.biohadoop.islandmodel.IslandModel;
import at.ac.uibk.dps.biohadoop.islandmodel.IslandModelException;
import at.ac.uibk.dps.biohadoop.tasksystem.queue.TaskException;
import at.ac.uibk.dps.biohadoop.tasksystem.queue.TaskFuture;
import at.ac.uibk.dps.biohadoop.tasksystem.queue.TaskSubmitter;

public class Ga implements Algorithm {

	public static final String DATA_PATH = "DATA_PATH";
	public static final String MAX_ITERATIONS = "MAX_ITERATIONS";
	public static final String POPULATION_SIZE = "POPULATION_SIZE";

	private static final Logger LOG = LoggerFactory.getLogger(Ga.class);
	private static final int LOG_STEPS = 1000;

	private final Random rand = new Random();

	@Override
	public void run(AlgorithmId algorithmId, Map<String, String> properties)
			throws AlgorithmException {
		// Read algorithm settings from configuration
		String dataPath = properties.get(DATA_PATH);
		Tsp tsp = readTspData(dataPath);
		String populationSizeProperty = properties.get(POPULATION_SIZE);
		int populationSize = Integer.parseInt(populationSizeProperty);
		String maxIterationsProperty = properties.get(MAX_ITERATIONS);
		int maxIterations = Integer.parseInt(maxIterationsProperty);
		DistancesGlobal.setDistances(tsp.getDistances());
		int citySize = tsp.getCities().length;

		// Read island model settings from configuration
		Integer mergeAfterIteration = 1000;

		// Initialize island model
		try {
			IslandModel.initialize(algorithmId);
		} catch (IslandModelException e) {
			throw new AlgorithmException(
					"Could not initialize island model for solver "
							+ algorithmId, e);
		}

		int[][] population = initPopulation(populationSize, citySize);

		// int iterationStart = 0;
		// Load old snapshot from file if possible
		// try {
		// GaData gaData = FileUtils.load(properties, GaData.class);
		// if (gaData != null) {
		// population = gaData.getPopulation();
		// iterationStart = gaData.getIteration();
		// LOG.info("Resuming from iteration {}", iterationStart);
		// } else {
		// population = initPopulation(populationSize, citySize);
		// }
		// } catch (FileLoadException e) {
		// throw new AlgorithmException(e);
		// }

		// Initialize default task setting for remote computation
		TaskSubmitter<double[][], int[], Double> taskClient = new TaskSubmitter<>(
				RemoteFitness.class, DistancesGlobal.getDistances());

		// Start algorithm
		boolean end = false;
		int iteration = 0;
		long startTime = System.currentTimeMillis();

		while (!end) {
			// Recombination
			int[][] offsprings = new int[populationSize][citySize];
			for (int i = 0; i < populationSize; i++) {
				int indexParent1 = rand.nextInt(populationSize);
				int indexParent2 = rand.nextInt(populationSize);

				offsprings[i] = recombination(population[indexParent1],
						population[indexParent2]);
			}

			// Mutation
			int[][] mutated = new int[populationSize][citySize];
			for (int i = 0; i < populationSize; i++) {
				mutated[i] = mutation(offsprings[i]);
			}

			// Evaluation
			double[] values = new double[populationSize * 2];
			try {
				// Submit tasks for remote clients
				List<TaskFuture<Double>> taskFutures = taskClient
						.addAll(population);

				// Submit tasks for remote clients
				for (int i = 0; i < populationSize; i++) {
					taskFutures.add(taskClient.add(mutated[i]));
				}

				// Wait for all tasks to complete
				for (int i = 0; i < taskFutures.size(); i++) {
					values[i] = taskFutures.get(i).get();
					// asyncMetric.tick();
				}
			} catch (TaskException e) {
				throw new AlgorithmException(
						"Error while remote task computation", e);
			}

			// Selection
			for (int i = 0; i < populationSize * 2; i++) {
				for (int j = i + 1; j < populationSize * 2; j++) {
					if (values[j] < values[i]) {
						double tmp = values[j];
						values[j] = values[i];
						values[i] = tmp;

						int[] tmpGenome;
						if (i < populationSize && j < populationSize) {
							// i and j refer to population
							tmpGenome = population[i];
							population[i] = population[j];
							population[j] = tmpGenome;
						} else if (i >= populationSize && j < populationSize) {
							// i refer to mutated, j refer to population
							tmpGenome = mutated[i - populationSize];
							mutated[i - populationSize] = population[j];
							population[j] = tmpGenome;
						} else if (i < populationSize && j >= populationSize) {
							// i refer to population, j refer to mutated
							tmpGenome = population[i];
							population[i] = mutated[j - populationSize];
							mutated[j - populationSize] = tmpGenome;
						} else {
							// i and j refer to mutated
							tmpGenome = mutated[i - populationSize];
							mutated[i - populationSize] = mutated[j
									- populationSize];
							mutated[j - populationSize] = tmpGenome;
						}
					}
				}
			}
			// End algorithm

			iteration++;

			// Set this to enable data sharing with other islands
			// (parallelization using island model)
			IslandModel.publish(algorithmId, new GaData(population, iteration,
					values[0]));

			if (iteration % mergeAfterIteration == 0) {
				try {
					IslandModel.merge(algorithmId, properties, new GaData(
							population, iteration, values[0]));
				} catch (IslandModelException e) {
					LOG.error("Error while merging data from remote island", e);
				}
			}
			// Handle merging of data from other islands (parallelization using
			// island model)
			// if (mergeAfterIteration != null
			// && iteration % mergeAfterIteration == 0) {
			// try {
			// population = (int[][]) IslandModel.merge(solverId,
			// properties, solverData);
			// } catch (IslandModelException e) {
			// throw new AlgorithmException(
			// "Error while trying to merge island data", e);
			// }
			// }

			// Handle saving of results to file
			// if (saveAfterIteration != null
			// && iteration % saveAfterIteration == 0) {
			// try {
			// FileSaver.save(solverId, properties, solverData);
			// } catch (FileSaveException e) {
			// throw new AlgorithmException(
			// "Error while trying to save data to file "
			// + savePath, e);
			// }
			// }

			// By setting the progress here, we inform Biohadoop and Hadoop
			// about the current progress
			AlgorithmService.setProgress(algorithmId, (float) iteration
					/ maxIterations);

			// Check for end condition
			if (iteration == maxIterations) {
				end = true;
			}

			// Some LOG output
			if (iteration % LOG_STEPS == 0 || iteration < 10) {
				long endTime = System.currentTimeMillis();
				LOG.info(
						"Counter: {} ({} worker computations) | last {} GA iterations took {} ms",
						iteration, 2 * iteration * populationSize, LOG_STEPS,
						endTime - startTime);
				startTime = endTime;
				printGenome(tsp.getDistances(), population[0], citySize);
			}
		}
	}

	private Tsp readTspData(String dataFile) throws AlgorithmException {
		try {
			return TspFileReader.readFile(dataFile);
		} catch (IOException e) {
			throw new AlgorithmException("Could not read TSP input file "
					+ dataFile);
		}
	}

	private int[][] initPopulation(int genomeSize, int citieSize) {
		int[][] population = new int[genomeSize][citieSize];

		for (int i = 0; i < genomeSize; i++) {
			List<Integer> singlePopulation = new ArrayList<Integer>();
			for (int j = 0; j < citieSize; j++) {
				singlePopulation.add(j);
			}
			Collections.shuffle(singlePopulation);

			for (int j = 0; j < citieSize; j++) {
				population[i][j] = singlePopulation.get(j);
			}
		}
		return population;
	}

	// based on Partially - Mapped Crossover (PMX) and
	// http://www.ceng.metu.edu.tr/~ucoluk/research/publications/tspnew.pdf
	private int[] recombination(int[] ds, int[] ds2) {
		int size = ds.length;

		int[] result = new int[size];
		for (int i = 0; i < size; i++) {
			result[i] = ds[i];
		}

		for (int i = 0; i < size / 2; i++) {
			int swapPos = findElementPos(result, ds2[i]);
			int tmp = result[i];
			result[i] = result[swapPos];
			result[swapPos] = tmp;
		}
		return result;
	}

	private int findElementPos(int[] list, int element) {
		for (int i = 0; i < list.length; i++) {
			if (list[i] == element) {
				return i;
			}
		}
		return -1;
	}

	private int[] mutation(int[] ds) {
		int pos1 = rand.nextInt(ds.length);
		int pos2 = rand.nextInt(ds.length);

		int tmp = ds[pos2];
		ds[pos2] = ds[pos1];
		ds[pos1] = tmp;

		return ds;
	}

	private void printGenome(double[][] distances, int[] solution, int citySize) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < citySize; i++) {
			sb.append(solution[i] + " | ");
		}
		LOG.info("fitness: {} | {}",
				GaFitness.computeFitness(distances, solution), sb.toString());
	}

}
