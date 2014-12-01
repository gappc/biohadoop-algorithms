package at.ac.uibk.dps.biohadoop.algorithms.nsgaii.remote;

import java.util.Random;

import at.ac.uibk.dps.biohadoop.algorithms.nsgaii.algorithm.Individual;
import at.ac.uibk.dps.biohadoop.functions.Function;
import at.ac.uibk.dps.biohadoop.operators.ParamterBasedMutator;
import at.ac.uibk.dps.biohadoop.operators.SBX;
import at.ac.uibk.dps.biohadoop.tasksystem.AsyncComputable;
import at.ac.uibk.dps.biohadoop.tasksystem.ComputeException;

public class OffspringComputation implements
		AsyncComputable<OffspringConfiguration, double[][], Individual> {

	private final Random rand = new Random();

	private Function function = null;

	@Override
	public Individual compute(double[][] data, OffspringConfiguration config)
			throws ComputeException {
		double[] parent1 = data[0];
		double[] parent2 = data[1];

		// Generate offspring
		double[] offspring = null;
		double[][] children = SBX.bounded(config.getSbxDistributionFactor(),
				parent1, parent2, 0, 1);

		// Each child has 50% probability to get chosen
		if (rand.nextDouble() < 0.5) {
			offspring = children[0];
		} else {
			offspring = children[1];
		}

		// Mutate offspring
		int index = rand.nextInt(offspring.length);
		offspring[index] = ParamterBasedMutator.mutate(offspring[index],
				config.getMutationFactor(), 0, 1);

		// Compute objective function values for offspring
		if (function == null) {
			function = buildFunction(config.getFunctionClass());
		}
		double[] objectiveValues = new double[2];
		objectiveValues[0] = function.f1(offspring);
		objectiveValues[1] = function.f2(offspring);

		return new Individual(offspring, objectiveValues);
	}

	private Function buildFunction(Class<? extends Function> initialData)
			throws ComputeException {
		try {
			return initialData.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new ComputeException("Could not instanciate class "
					+ initialData);
		}
	}
}
