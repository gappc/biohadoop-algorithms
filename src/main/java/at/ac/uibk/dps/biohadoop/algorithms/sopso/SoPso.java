package at.ac.uibk.dps.biohadoop.algorithms.sopso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.uibk.dps.biohadoop.algorithm.Algorithm;
import at.ac.uibk.dps.biohadoop.algorithm.AlgorithmException;
import at.ac.uibk.dps.biohadoop.algorithm.AlgorithmId;
import at.ac.uibk.dps.biohadoop.problems.tiledmul.AsyncTiledMul;
import at.ac.uibk.dps.biohadoop.problems.tiledmul.Matrices;
import at.ac.uibk.dps.biohadoop.tasksystem.queue.TaskFuture;
import at.ac.uibk.dps.biohadoop.tasksystem.queue.TaskSubmitter;
import at.ac.uibk.dps.biohadoop.utils.PropertyConverter;

public class SoPso implements Algorithm {

	private static final Logger LOG = LoggerFactory.getLogger(SoPso.class);
	
	public static final String PARTICLE_COUNT = "PARTICLE_COUNT";
	public static final String ITERATIONS = "ITERATIONS";
	public static final String INERTIA = "INERTIA";
	public static final String GLOBAL_WEIGHT = "GLOBAL_WEIGHT";
	public static final String PERSONAL_WEIGHT = "PERSONAL_WEIGHT";
	public static final String MATRIX_SIZE = "MATRIX_SIZE";

	private static final int BLOCKS = 2;
	private static final Random RAND = new Random();

	@Override
	public void run(AlgorithmId algorithmId, Map<String, String> properties)
			throws AlgorithmException {
		int particleCount = PropertyConverter.toInt(properties, PARTICLE_COUNT);
		int iterations = PropertyConverter.toInt(properties, ITERATIONS);
		double inertia = PropertyConverter.toDouble(properties, INERTIA);
		double gWeight = PropertyConverter.toDouble(properties, GLOBAL_WEIGHT);
		double pWeight = PropertyConverter
				.toDouble(properties, PERSONAL_WEIGHT);
		int matrixSize = PropertyConverter.toInt(properties, MATRIX_SIZE);

		double[][] matrixA = generateMatrix(matrixSize);
		double[][] matrixB = generateMatrix(matrixSize);
		TaskSubmitter<Matrices, int[], Long> taskSubmitter = new TaskSubmitter<>(
				AsyncTiledMul.class, new Matrices(matrixA, matrixB));

		Particle[] particles = new Particle[particleCount];
		for (int i = 0; i < particleCount; i++) {
			particles[i] = new Particle(BLOCKS, matrixSize);
		}

		List<TaskFuture<Long>> futures = new ArrayList<>();
		for (int i = 0; i < particleCount; i++) {
			futures.add(taskSubmitter.add(particles[i].getCurrentPos()));
		}

		for (int i = 0; i < particleCount; i++) {
			Long particleVal = futures.get(i).get();
			particles[i].setPBestValue(particleVal);
		}

		Particle gBest = getGlobalBest(particles, particles[0]);

		for (int iteration = 0; iteration < iterations; iteration++) {
			futures.clear();
			for (int i = 0; i < particleCount; i++) {
				move(particles[i], gBest, matrixSize, inertia, pWeight, gWeight);
				futures.add(taskSubmitter.add(particles[i].getCurrentPos()));
			}

			for (int i = 0; i < particleCount; i++) {
				Long particleVal = futures.get(i).get();
				if (particleVal < particles[i].getPBestValue()) {
					particles[i].setPBestValue(particleVal);
				}
			}

			gBest = getGlobalBest(particles, gBest);

			if (iteration % 10 == 0) {
				LOG.info("{}: gBest={}", iteration, gBest);
			}
		}
		LOG.info("gBest={}", gBest);
	}

	private double[][] generateMatrix(int size) {
		final double[][] m = new double[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				m[i][j] = RAND.nextDouble();
			}
		}
		return m;
	}

	private Particle getGlobalBest(Particle[] particles, Particle gBest) {
		for (Particle particle : particles) {
			if (particle.getPBestValue() < gBest.getPBestValue()) {
				gBest = particle;
			}
		}
		return new Particle(gBest);
	}

	private void move(Particle particle, Particle gBest, int matrixSize, double inertia,
			double pWeight, double gWeight) {
		double r1 = RAND.nextDouble();
		double r2 = RAND.nextDouble();
		for (int i = 0; i < particle.getCurrentPos().length; i++) {
			double oldSpeed = particle.getSpeed()[i];
			double diffToGlobal = gBest.getCurrentPos()[i]
					- particle.getCurrentPos()[i];
			double diffToPersonal = particle.getPBest()[i]
					- particle.getCurrentPos()[i];
			double newSpeed = inertia * oldSpeed + pWeight * r1
					* diffToPersonal + gWeight * r2 * diffToGlobal;
			particle.getSpeed()[i] = newSpeed;
			particle.getCurrentPos()[i] = (int) Math.round(particle
					.getCurrentPos()[i] + newSpeed);
			if (particle.getCurrentPos()[i] > matrixSize) {
				particle.getCurrentPos()[i] = matrixSize;
			}
			if (particle.getCurrentPos()[i] < 1) {
				particle.getCurrentPos()[i] = 1;
			}
		}
	}
}
