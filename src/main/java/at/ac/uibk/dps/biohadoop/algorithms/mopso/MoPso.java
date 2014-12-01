package at.ac.uibk.dps.biohadoop.algorithms.mopso;

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
import at.ac.uibk.dps.biohadoop.functions.Function;
import at.ac.uibk.dps.biohadoop.tasksystem.queue.TaskFuture;
import at.ac.uibk.dps.biohadoop.tasksystem.queue.TaskSubmitter;
import at.ac.uibk.dps.biohadoop.utils.PropertyConverter;

public class MoPso implements Algorithm {

	public static final String PARTICLE_COUNT = "PARTICLE_COUNT";
	public static final String ARCHIVE_SIZE = "ARCHIVE_SIZE";
	public static final String ITERATIONS = "ITERATIONS";
	public static final String INERTIA = "INERTIA";
	public static final String GLOBAL_WEIGHT = "GLOBAL_WEIGHT";
	public static final String PERSONAL_WEIGHT = "PERSONAL_WEIGHT";
	public static final String FUNCTION_CLASS = "FUNCTION_CLASS";

	private static final Logger LOG = LoggerFactory.getLogger(MoPso.class);
	private static final Random RAND = new Random();

	@Override
	public void run(AlgorithmId algorithmId, Map<String, String> properties)
			throws AlgorithmException {
		int particleCount = PropertyConverter.toInt(properties, PARTICLE_COUNT);
		int archiveSize = PropertyConverter.toInt(properties, ARCHIVE_SIZE);
		int iterations = PropertyConverter.toInt(properties, ITERATIONS);
		double inertia = PropertyConverter.toDouble(properties, INERTIA);
		double gWeight = PropertyConverter.toDouble(properties, GLOBAL_WEIGHT);
		double pWeight = PropertyConverter
				.toDouble(properties, PERSONAL_WEIGHT);
		String functionClassName = properties.get(FUNCTION_CLASS);

		Class<Function> functionClass = null;
		try {
			functionClass = (Class<Function>) Class.forName(functionClassName);
		} catch (ClassNotFoundException e) {
			throw new AlgorithmException("Could not build object "
					+ functionClassName, e);
		}

		Particle[] particles = new Particle[particleCount];
		for (int i = 0; i < particleCount; i++) {
			particles[i] = new Particle(2);
		}

		TaskSubmitter<Class<Function>, double[], double[]> taskSubmitter = new TaskSubmitter<>(
				RemoteEvaluator.class, functionClass);
		List<TaskFuture<double[]>> futures = new ArrayList<>();
		for (int i = 0; i < particleCount; i++) {
			futures.add(taskSubmitter.add(particles[i].getCurrentPos()));
		}

		Archive archive = new Archive(archiveSize);

		for (int i = 0; i < particleCount; i++) {
			double[] objectiveValues = futures.get(i).get();
			particles[i].setCurrentValue(objectiveValues);
			particles[i].setPBestValue(objectiveValues);
			archive.offer(particles[i]);
		}

		for (int iteration = 0; iteration < iterations; iteration++) {
			futures.clear();
			for (int i = 0; i < particleCount; i++) {
				Particle archiveMember = archive.getRandomMember();
				move(particles[i], archiveMember, inertia, pWeight, gWeight);
				futures.add(taskSubmitter.add(particles[i].getCurrentPos()));
			}

			for (int i = 0; i < particleCount; i++) {
				double[] objectiveValues = futures.get(i).get();
				particles[i].setCurrentValue(objectiveValues);
				if (archive.offer(particles[i])) {
					particles[i].setPBestValue(objectiveValues);
				}
			}

			if (iteration % 1 == 0) {
				LOG.info("{}", iteration);
			}
		}

		int i = 0;
		StringBuilder sb = new StringBuilder();
		// while (archive.getMember(i) != null) {
		// Particle particle = archive.getMember(i);
		// System.out.println(particle);
		// sb.append(particle.getPBestPos()[0] + " "
		// + particle.getPBestPos()[1] + "\n");
		// i++;
		// }

		for (Particle particle : archive.getForPrint()) {
			System.out.println(particle);
			sb.append(particle.getPBestPos()[0] + " "
					+ particle.getPBestPos()[1] + "\n");
		}
		// for (int n = 0; n < particles.length; n++) {
		// sb.append(particles[n].getPBestPos()[0] + " " +
		// particles[n].getPBestPos()[1] + "\n");
		// i++;
		// }
		try {
			Files.write(Paths.get("/tmp/data.out"), sb.toString().getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void move(Particle particle, Particle gBest, double inertia,
			double pWeight, double gWeight) {
		double r1 = RAND.nextDouble();
		double r2 = RAND.nextDouble();
		for (int i = 0; i < particle.getCurrentPos().length; i++) {
			double oldSpeed = particle.getSpeed()[i];
			double diffToGlobal = gBest.getCurrentPos()[i]
					- particle.getCurrentPos()[i];
			double diffToPersonal = particle.getPBestPos()[i]
					- particle.getCurrentPos()[i];
			double newSpeed = inertia * oldSpeed + pWeight * r1
					* diffToPersonal + gWeight * r2 * diffToGlobal;
			particle.getSpeed()[i] = newSpeed;
			particle.getCurrentPos()[i] = particle.getCurrentPos()[i]
					+ newSpeed;
		}
	}
}
