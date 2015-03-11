package at.ac.uibk.dps.biohadoop.algorithms.mopso;

import at.ac.uibk.dps.biohadoop.functions.Function;
import at.ac.uibk.dps.biohadoop.tasksystem.Worker;
import at.ac.uibk.dps.biohadoop.tasksystem.ComputeException;

public class RemoteEvaluator implements
		Worker<Class<Function>, double[], double[]> {

	private Function function = null;

	@Override
	public double[] compute(double[] data, Class<Function> functionClass)
			throws ComputeException {
		// Compute objective function values for offspring
		if (this.function == null) {
			this.function = buildFunction(functionClass);
		}
		double[] objectiveValues = new double[2];
		objectiveValues[0] = function.f1(data);
		objectiveValues[1] = function.f2(data);
		return objectiveValues;
	}

	private Function buildFunction(Class<? extends Function> functionClass)
			throws ComputeException {
		try {
			return functionClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new ComputeException("Could not instanciate class "
					+ function);
		}
	}
}
