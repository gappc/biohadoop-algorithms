package at.ac.uibk.dps.biohadoop.algorithms.nsgaii.algorithm;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.uibk.dps.biohadoop.algorithm.Algorithm;
import at.ac.uibk.dps.biohadoop.algorithm.AlgorithmException;
import at.ac.uibk.dps.biohadoop.algorithm.AlgorithmId;
import at.ac.uibk.dps.biohadoop.algorithm.AlgorithmService;
import at.ac.uibk.dps.biohadoop.algorithms.nsgaii.algorithm.async.TaskPool;
import at.ac.uibk.dps.biohadoop.hadoop.Environment;
import at.ac.uibk.dps.biohadoop.hadoop.launcher.WorkerParametersResolver;
import at.ac.uibk.dps.biohadoop.tasksystem.queue.TaskFuture;

public class NsgaII implements Algorithm {

	public static final String GENOME_SIZE = "GENOME_SIZE";
	public static final String MAX_ITERATIONS = "MAX_ITERATIONS";
	public static final String POPULATION_SIZE = "POPULATION_SIZE";
	public static final String FUNCTION_CLASS = "FUNCTION_CLASS";
	public static final String SBX_DISTRIBUTION_FACTOR = "SBX_DISTRIBUTION_FACTOR";
	public static final String MUTATION_FACTOR = "MUTATION_FACTOR";
	public static final String ASYNC = "ASYNC";

	private static final Logger LOG = LoggerFactory.getLogger(NsgaII.class);
	private static final int LOG_STEPS = 100;

	@Override
	public void run(AlgorithmId solverId, Map<String, String> properties)
			throws AlgorithmException {
		long algorithmStartTime = System.nanoTime();
		long biohadoopStartupTime = Long.parseLong(Environment
				.get(Environment.BIOHADOOP_START_AT_NS));
		LOG.info(
				"Starting NSGA-II at System.nanoTime()={}, Biohadoop startup took {}ns",
				algorithmStartTime, algorithmStartTime - biohadoopStartupTime);

		Preparation prep = new Preparation(properties);

		int persitedIteration = 0;

		double[][] population = initializePopulation(prep.getPopSize() * 2,
				prep.getGenomeSize());
		double[][] objectiveValues = new double[prep.getPopSize() * 2][2];

		// Initialization: compute front for initial population
		List<List<Integer>> fronts = FastNonDominatedSort.sort(Arrays.copyOf(
				objectiveValues, prep.getPopSize()));

		// Initialization: start task pool
		int workerCount = Integer.parseInt(Environment.get(Environment.WORKER_COUNT));
		TaskPool pool = new TaskPool(prep.getTaskSubmitter(), 3 * workerCount);
		pool.update(population, objectiveValues, fronts);
		pool.start();

		// Initialization: create children from initial population and compute
		// objective values for them. The children overwrite the initial
		// population
		List<TaskFuture<Individual>> offsprings = new ArrayList<>();

		if (prep.isAsync()) {
			// Asynchronous generational GA
			for (int i = 0; i < prep.getPopSize(); i++) {
				Individual individual = pool.getFinishedIndividual();
				population[i] = individual.getIndividual();
				objectiveValues[i] = individual.getObjectives();
			}
		} else {
			// Synchronous generational GA
			Random rand = new Random();
			for (int i = 0; i < prep.getPopSize(); i++) {
				double[][] parents = new double[2][];
				parents[0] = population[rand.nextInt(prep.getPopSize())];
				parents[1] = population[rand.nextInt(prep.getPopSize())];
				TaskFuture<Individual> offspring = prep.getTaskSubmitter().add(
						parents);
				offsprings.add(offspring);
			}
			for (int i = 0; i < prep.getPopSize(); i++) {
				population[i] = offsprings.get(i).get().getIndividual();
				objectiveValues[i] = offsprings.get(i).get().getObjectives();
			}
		}
		
		fronts = FastNonDominatedSort.sort(Arrays.copyOf(
				objectiveValues, prep.getPopSize()));

		// Initialization finished
		LOG.info("Algorithm initialisation until main loop took {}ns",
				System.nanoTime() - algorithmStartTime);

		long fullWorkerTime = 0;
		long loopStartTime = System.nanoTime();

		// Start algorithm
		long singleIterationTime = System.nanoTime();
		int iteration = 0;
		boolean end = false;
		while (!end) {
			long workerStartTime = System.nanoTime();
			offsprings.clear();

			if (prep.isAsync()) {
				// Asynchronous generational GA
				pool.update(population, objectiveValues, fronts);
				for (int i = 0; i < prep.getPopSize(); i++) {
					Individual individual = pool.getFinishedIndividual();
					population[i + prep.getPopSize()] = individual
							.getIndividual();
					objectiveValues[i + prep.getPopSize()] = individual
							.getObjectives();
				}
			} else {
				// Synchronous generational GA
				for (int i = 0; i < prep.getPopSize(); i++) {
					int[] parentIndexes = parentSelectionTournament(fronts,
							objectiveValues);
					double[][] parents = new double[2][];
					parents[0] = population[parentIndexes[0]];
					parents[1] = population[parentIndexes[1]];
					TaskFuture<Individual> offspring = prep.getTaskSubmitter()
							.add(parents);
					offsprings.add(offspring);
				}
				for (int i = 0; i < prep.getPopSize(); i++) {
					population[i + prep.getPopSize()] = offsprings.get(i).get()
							.getIndividual();
					objectiveValues[i + prep.getPopSize()] = offsprings.get(i)
							.get().getObjectives();
				}
			}
			fullWorkerTime += System.nanoTime() - workerStartTime;
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
						newObjectiveValues[newPopSize] = objectiveValues[front
								.get(i)];
						newPopSize++;
					}
				}
				currentRank++;
			}

			population = newPopulation;
			objectiveValues = newObjectiveValues;

			iteration++;

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
								- singleIterationTime);
				singleIterationTime = endTime;
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

		long fullAlgorithmTime = System.nanoTime() - algorithmStartTime;
		long fullLoopTime = System.nanoTime() - loopStartTime;
		int workerSize = WorkerParametersResolver.getWorkerParameters().size();

		prep.logProperties();

		LOG.info("Async={}", prep.isAsync());
		LOG.info("Workers={}", workerSize);
		LOG.info("Iterations={}", prep.getIterations());
		LOG.info("Algorithm run time {}ns", fullAlgorithmTime);
		LOG.info("Time spent in main loop {}ns", fullLoopTime);
		LOG.info("Worker time {}ns", fullWorkerTime);
		LOG.info("Iteration time parseable ({} {})", workerSize,
				String.format("%.3f", fullLoopTime / 1e9));
		LOG.info(
				"Iterations per second ({} {})",
				workerSize,
				String.format("%.3f", prep.getIterations()
						/ (fullLoopTime / 1e9)));

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

}