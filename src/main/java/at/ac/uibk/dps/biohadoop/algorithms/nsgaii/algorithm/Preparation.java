package at.ac.uibk.dps.biohadoop.algorithms.nsgaii.algorithm;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.uibk.dps.biohadoop.algorithm.AlgorithmException;
import at.ac.uibk.dps.biohadoop.algorithms.nsgaii.remote.RemoteFunctionValue;
import at.ac.uibk.dps.biohadoop.functions.Function;
import at.ac.uibk.dps.biohadoop.tasksystem.queue.TaskSubmitter;
import at.ac.uibk.dps.biohadoop.utils.PropertyConverter;

public class Preparation {

	private static final Logger LOG = LoggerFactory
			.getLogger(Preparation.class);

	private final int popSize;
	private final int genomeSize;
	private final double sbxDistributionFactor;
	private final double mutationFactor;
	private final int iterations;
	private final String functionClassName;
	private final TaskSubmitter<Class<? extends Function>, double[], double[]> taskSubmitter;

	public Preparation(Map<String, String> properties)
			throws AlgorithmException {
		// Read and parse configuration
		popSize = PropertyConverter.toInt(properties, NsgaII.POPULATION_SIZE);
		genomeSize = PropertyConverter.toInt(properties, NsgaII.GENOME_SIZE);
		sbxDistributionFactor = PropertyConverter.toDouble(properties,
				NsgaII.SBX_DISTRIBUTION_FACTOR);
		mutationFactor = PropertyConverter.toDouble(properties,
				NsgaII.MUTATION_FACTOR);
		iterations = PropertyConverter.toInt(properties, NsgaII.MAX_ITERATIONS);
		functionClassName = PropertyConverter.toString(properties,
				NsgaII.FUNCTION_CLASS, null);

		try {
			LOG.info("Function: {}", functionClassName);
			Class<Function> functionClass = (Class<Function>) Class
					.forName(functionClassName);
			taskSubmitter = new TaskSubmitter<>(
					RemoteFunctionValue.class, functionClass);
		} catch (ClassNotFoundException e) {
			throw new AlgorithmException("Could not build object "
					+ functionClassName, e);
		}

		logProperties();
	}

	public int getPopSize() {
		return popSize;
	}

	public int getGenomeSize() {
		return genomeSize;
	}

	public double getSbxDistributionFactor() {
		return sbxDistributionFactor;
	}

	public double getMutationFactor() {
		return mutationFactor;
	}

	public int getIterations() {
		return iterations;
	}

	public String getFunctionClassName() {
		return functionClassName;
	}

	public TaskSubmitter<Class<? extends Function>, double[], double[]> getTaskSubmitter() {
		return taskSubmitter;
	}

	public void logProperties() {
		LOG.info("----- Parameters: -----");
		LOG.info("POP_SIZE={}", popSize);
		LOG.info("GENOME_SIZE={}", genomeSize);
		LOG.info("SBX_DISTRIBUTION_FACTOR={}", sbxDistributionFactor);
		LOG.info("MUTATION_FACTOR={}", mutationFactor);
		LOG.info("ITERATIONS={}", iterations);
		LOG.info("FUNCTION={}", functionClassName);
		LOG.info("-----------------------");
	}

}
