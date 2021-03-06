package at.ac.uibk.dps.biohadoop.algorithms.moead.remote;

import at.ac.uibk.dps.biohadoop.functions.Function;
import at.ac.uibk.dps.biohadoop.tasksystem.ComputeException;
import at.ac.uibk.dps.biohadoop.tasksystem.Worker;

public class RemoteFunctionValue implements
		Worker<Class<? extends Function>, double[], double[]> {

	private Function function = null;

	@Override
	public double[] compute(double[] data, Class<? extends Function> initialData)
			throws ComputeException {
		if (function == null) {
			function = buildFunction(initialData);
		}
		double[] result = new double[2];
		result[0] = function.f1(data);
		result[1] = function.f2(data);
		return result;
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
