package at.ac.uibk.dps.biohadoop.algorithms.moead.remote;

import at.ac.uibk.dps.biohadoop.algorithms.moead.algorithm.Functions;
import at.ac.uibk.dps.biohadoop.unifiedcommunication.RemoteExecutable;

public class RemoteFunctionValue implements
		RemoteExecutable<Object, double[], double[]> {

	@Override
	public Object getInitalData() {
		// No initial object for MOEAD
		return null;
	}

	@Override
	public double[] compute(double[] data, Object initialData) {
		double[] result = new double[2];
		result[0] = Functions.f1(data);
		result[1] = Functions.f2(data);
		return result;
	}

}
