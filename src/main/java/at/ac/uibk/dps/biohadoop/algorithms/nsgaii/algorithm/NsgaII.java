package at.ac.uibk.dps.biohadoop.algorithms.nsgaii.algorithm;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.uibk.dps.biohadoop.algorithm.Algorithm;
import at.ac.uibk.dps.biohadoop.algorithm.AlgorithmException;
import at.ac.uibk.dps.biohadoop.algorithm.AlgorithmId;
import at.ac.uibk.dps.biohadoop.algorithm.AlgorithmService;
import at.ac.uibk.dps.biohadoop.hadoop.launcher.WorkerParametersResolver;
import at.ac.uibk.dps.biohadoop.tasksystem.queue.TaskException;
import at.ac.uibk.dps.biohadoop.tasksystem.queue.TaskFuture;

public class NsgaII implements Algorithm {

	public static final String GENOME_SIZE = "GENOME_SIZE";
	public static final String MAX_ITERATIONS = "MAX_ITERATIONS";
	public static final String POPULATION_SIZE = "POPULATION_SIZE";
	public static final String FUNCTION_CLASS = "FUNCTION_CLASS";
	public static final String SBX_DISTRIBUTION_FACTOR = "SBX_DISTRIBUTION_FACTOR";
	public static final String MUTATION_FACTOR = "MUTATION_FACTOR";

	private static final Logger LOG = LoggerFactory.getLogger(NsgaII.class);
	private static final int LOG_STEPS = 100;

	@Override
	public void run(AlgorithmId solverId, Map<String, String> properties)
			throws AlgorithmException {
		long startTime = System.currentTimeMillis();

		Preparation prep = new Preparation(properties);

		int persitedIteration = 0;

		double[][] population = initializePopulation(prep.getPopSize() * 2,
				prep.getGenomeSize());

		double[][] objectiveValues = new double[prep.getPopSize() * 2][2];
		computeObjectiveValues(prep, population, objectiveValues, 0,
				prep.getPopSize());
		List<List<Integer>> fronts = FastNonDominatedSort.sort(objectiveValues);

		// Initialization finished
		LOG.info("Initialisation took {}ns", System.nanoTime() - startTime);
		startTime = System.currentTimeMillis();

		// Start algorithm
		int iteration = 0;
		boolean end = false;
		long iterationStart = System.nanoTime();
		while (!end) {
			produceOffsprings(population, objectiveValues, fronts,
					prep.getSbxDistributionFactor(), prep.getMutationFactor());

			objectiveValues = computeObjectiveValues(prep, population,
					objectiveValues, prep.getPopSize(), prep.getPopSize());

			fronts = FastNonDominatedSort.sort(objectiveValues);

			double[][] newPopulation = new double[prep.getPopSize() * 2][prep
					.getGenomeSize()];
			double[][] newObjectiveValues = new double[prep.getPopSize() * 2][prep
					.getGenomeSize()];

			int currentRank = 0;
			int newPopSize = 0;
			while (newPopSize < prep.getPopSize()) {
				List<Integer> front = fronts.get(currentRank);
				// if there is place enough for a complete rank, just insert it
				if (newPopSize + front.size() <= prep.getPopSize()) {
					for (int individualIndex : front) {
						newPopulation[newPopSize] = population[individualIndex];
						newObjectiveValues[newPopSize] = objectiveValues[individualIndex];
						newPopSize++;
					}
				}
				// if there is not enough place, use crowding distance to choose
				// best solutions from current front
				else {
					CrowdingDistance.sortFrontAccordingToCrowdingDistance(
							front, objectiveValues);
					int missingElements = prep.getPopSize() - newPopSize;
					for (int i = 0; i < missingElements; i++) {
						newPopulation[newPopSize] = population[front.get(i)];
						newObjectiveValues[newPopSize] = objectiveValues[front.get(i)];
						newPopSize++;
					}
				}
				currentRank++;
			}

			population = newPopulation;
			objectiveValues = newObjectiveValues;

			// TODO ERROR?
//			objectiveValues = computeObjectiveValues(prep, population,
//					objectiveValues, 0, prep.getPopSize());

			iteration++;

			// objectiveValues);

			// solverData = new SolverData<>(result, 0, iteration);
			//
			// // Handle saving of results to file
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
			AlgorithmService.setProgress(solverId,
					(float) iteration / prep.getIterations());

			// Check for end condition
			if (iteration >= prep.getIterations()) {
				end = true;
			}
			if (iteration % 100 == 0) {
				long endTime = System.nanoTime();
				LOG.info("Counter: {} | last {} NSGAII iterations took {} ns",
						iteration + persitedIteration, LOG_STEPS, endTime
								- startTime);
				startTime = endTime;
			}
		}

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < prep.getPopSize(); i++) {
			System.out.println(objectiveValues[i][0] + " "
					+ objectiveValues[i][1]);
			sb.append(objectiveValues[i][0] + " " + objectiveValues[i][1]
					+ "\n");
		}
		try {
			Files.write(Paths.get("/tmp/data.out"), sb.toString().getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int workerSize = WorkerParametersResolver.getWorkerParameters().size();
		long iterationTime = System.nanoTime() - iterationStart;

		prep.logProperties();

		LOG.info("Workers={}", workerSize);
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

	// Initialize population, where first half (of size populationSize) is
	// filled with random numbers, and second half is initialized with
	// zeros. second half contains offsprings
	private double[][] initializePopulation(int populationSize, int genomeSize) {
		Random rand = new Random();
		double[][] population = new double[populationSize][genomeSize];
		for (int i = 0; i < populationSize / 2; i++) {
			for (int j = 0; j < genomeSize; j++) {
				population[i][j] = rand.nextDouble();
			}
		}
		return population;
	}

	private void produceOffsprings(double[][] population,
			double[][] objectiveValues, List<List<Integer>> fronts,
			double sbxDistributionFactor, double mutationFactor) {
		Random rand = new Random();
		int populationSize = population.length / 2;
		int genomeSize = population[0].length;

		for (int i = 0; i < populationSize; i++) {
			int[] parents = parentSelectionTournament(fronts, objectiveValues);

			double[][] children = SBX.bounded(sbxDistributionFactor,
					population[parents[0]], population[parents[1]], 0, 1);
			// Each child has 50% probability to get chosen
			if (rand.nextDouble() < 0.5) {
				population[i + populationSize] = children[0];
			} else {
				population[i + populationSize] = children[1];
			}

			for (int j = 0; j < genomeSize; j++)
				if (Double.isNaN(population[i + populationSize][j])) {
					System.out.println("POPULATION FUCK");
				}

			for (int j = 0; j < genomeSize; j++)
				if (population[i + populationSize][j] < 0.0) {
					System.out.println("POPULATION FUCK");
				}

			int mutationGenomeIndex = rand.nextInt(genomeSize);
			population[i + populationSize][mutationGenomeIndex] = ParamterBasedMutator
					.mutate(population[i + populationSize][mutationGenomeIndex],
							mutationFactor, 0, 1);
			// for (int j = 0; j < genomeSize; j++) {
			// if (rand.nextDouble() < 1.0 / genomeSize) {
			// population[i + populationSize][j] = ParamterBasedMutator
			// .mutate(population[i + populationSize][j],
			// mutationFactor, 0, 1);
			// }
			// }
		}
	}

	private int[] parentSelectionTournament(final List<List<Integer>> fronts,
			double[][] objectives) {
		Random rand = new Random();
		int[] parents = new int[2];

		for (int i = 0; i < 2; i++) {
			int p1Index = rand.nextInt(objectives.length / 2);
			int p2Index = rand.nextInt(objectives.length / 2);

			int p1Front = getFront(fronts, p1Index);
			int p2Front = getFront(fronts, p2Index);
			if (p1Front < p2Front) {
				parents[i] = p1Index;
			} else if (p1Front > p2Front) {
				parents[i] = p2Index;
			} else {
				List<Integer> front = fronts.get(p1Front);
				CrowdingDistance.sortFrontAccordingToCrowdingDistance(front,
						objectives);
				if (front.indexOf(p1Index) < front.indexOf(p2Index)) {
					parents[i] = p1Index;
				} else {
					parents[i] = p2Index;
				}
			}
		}
		return parents;
	}

	private int getFront(List<List<Integer>> fronts, int index) {
		for (int i = 0; i < fronts.size(); i++) {
			if (fronts.get(i).contains(index)) {
				return i;
			}
		}
		throw new RuntimeException("Kacke");
	}

	private double[][] computeObjectiveValues(Preparation prep,
			double[][] population, double[][] objectiveValues, int start,
			int length) throws AlgorithmException {
		try {
			List<TaskFuture<double[]>> taskFutures = new ArrayList<>();
			for (int i = 0; i < length; i++) {
				TaskFuture<double[]> tf = prep.getTaskSubmitter().add(
						population[i + start]);
				taskFutures.add(tf);
			}
			for (int i = 0; i < taskFutures.size(); i++) {
				objectiveValues[i + start] = taskFutures.get(i).get();
			}
			return objectiveValues;
		} catch (TaskException e) {
			LOG.error("Error while remote task computation", e);
			throw new AlgorithmException(e);
		}
	}

}