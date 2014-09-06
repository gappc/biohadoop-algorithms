package at.ac.uibk.dps.biohadoop.algorithms.moead.remote;

import at.ac.uibk.dps.biohadoop.communication.RemoteExecutable;
import at.ac.uibk.dps.biohadoop.functions.Function;

public class RemoteFunctionValue implements
		RemoteExecutable<Function, double[], double[]> {

	@Override
	public double[] compute(double[] data, Function initialData) {
		double[] result = new double[2];
		result[0] = initialData.f1(data);
		result[1] = initialData.f2(data);
		return result;
	}

}
