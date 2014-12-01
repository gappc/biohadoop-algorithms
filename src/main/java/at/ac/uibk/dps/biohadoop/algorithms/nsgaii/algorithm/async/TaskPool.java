package at.ac.uibk.dps.biohadoop.algorithms.nsgaii.algorithm.async;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.uibk.dps.biohadoop.algorithms.nsgaii.algorithm.CrowdingDistance;
import at.ac.uibk.dps.biohadoop.algorithms.nsgaii.algorithm.Individual;
import at.ac.uibk.dps.biohadoop.algorithms.nsgaii.remote.OffspringConfiguration;
import at.ac.uibk.dps.biohadoop.hadoop.Environment;
import at.ac.uibk.dps.biohadoop.tasksystem.queue.TaskFuture;
import at.ac.uibk.dps.biohadoop.tasksystem.queue.TaskSubmitter;

import com.esotericsoftware.kryo.Kryo;

public class TaskPool implements Callable<Object> {

	private static final Logger LOG = LoggerFactory.getLogger(TaskPool.class);

	private final Map<TaskFuture<Individual>, TaskFuture<Individual>> futures = new ConcurrentHashMap<>();
	private final TaskSubmitter<OffspringConfiguration, double[][], Individual> submitter;
	private final Kryo kryo = new Kryo();
	private final Semaphore semaphore;
	private final Lock lock = new ReentrantLock();

	private double[][] population;
	private double[][] objectiveValues;
	private List<List<Integer>> fronts;

	public TaskPool(
			TaskSubmitter<OffspringConfiguration, double[][], Individual> submitter,
			int targetSize) {
		this.semaphore = new Semaphore(targetSize);
		this.submitter = submitter;
		// kryo.setReferences(false);
		kryo.register(double[][].class);
		kryo.register(ArrayList.class);
		kryo.setRegistrationRequired(true);
	}

	public void start() {
		ExecutorService executor = Executors.newFixedThreadPool(1);
		executor.submit(this);
		executor.shutdown();
	}

	@Override
	public Object call() throws Exception {
		while (!Environment.isShutdown()) {
			try {
				// long start = System.nanoTime();
				if (semaphore.tryAcquire(1000, TimeUnit.MILLISECONDS)) {
					// LOG.info("WAITING FOR AQUIRE {}ns", System.nanoTime() -
					// start);
					// LOG.info("PRODUCE {}", semaphore.availablePermits());
					lock.lock();
					int[] parentIndexes = parentSelectionTournament(fronts,
							objectiveValues);
					lock.unlock();
					double[][] parents = new double[2][];
					parents[0] = population[parentIndexes[0]];
					parents[1] = population[parentIndexes[1]];
					TaskFuture<Individual> future = submitter.add(parents);
					futures.put(future, future);
				}
				// double[] offspring = produceOffspring(population,
				// objectiveValues, fronts);
				// TaskFuture<double[]> future = submitter.add(offspring);
				// Individual individual = new Individual(future, offspring);
				// individuals.put(individual, individual);
				// LOG.info("ALL TOOK {}", System.nanoTime() - start);
			} catch (Exception e) {
				LOG.error("ERROR", e);
			}
		}
		return null;
	}

	// public double[][] getIndividual() {
	// Individual individual = getFinishedIndividual();
	// double[][] data = new double[2][];
	// data[0] = individual.getIndividual();
	// data[1] = individual.getObjectives();
	//
	// // LOG.info("CONSUME");
	// semaphore.release();
	// return data;
	// }

	int count = 0;

	public void update(double[][] population, double[][] objectiveValues,
			List<List<Integer>> fronts) {
		lock.lock();
		this.population = kryo.copyShallow(population);
		this.objectiveValues = kryo.copyShallow(objectiveValues);
		this.fronts = kryo.copy(fronts);
		// this.population = population;
		// this.objectiveValues = objectiveValues;
		// this.fronts = fronts;
		lock.unlock();
	}

	public Individual getFinishedIndividual() {
		while (true) {
			for (TaskFuture<Individual> future : futures.keySet()) {
				if (future.isDone()) {
					semaphore.release();
					return futures.remove(future).get();
				}
			}
			Thread.yield();
		}
	}

	// private double[] produceOffspring(double[][] population,
	// double[][] objectiveValues, List<List<Integer>> fronts) {
	// Random rand = new Random();
	// int genomeSize = population[0].length;
	//
	// double[] newIndividual = new double[genomeSize];
	// int[] parents = parentSelectionTournament(fronts, objectiveValues);
	//
	// double[][] children = SBX.bounded(sbxDistributionFactor,
	// population[parents[0]], population[parents[1]], 0, 1);
	// // Each child has 50% probability to get chosen
	// if (rand.nextDouble() < 0.5) {
	// newIndividual = children[0];
	// } else {
	// newIndividual = children[1];
	// }
	//
	// int mutationGenomeIndex = rand.nextInt(genomeSize);
	// newIndividual[mutationGenomeIndex] = ParamterBasedMutator.mutate(
	// newIndividual[mutationGenomeIndex], mutationFactor, 0, 1);
	// return newIndividual;
	// }
	//
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
