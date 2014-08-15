package at.ac.uibk.dps.biohadoop.algorithms.nsgaii.remote;

import at.ac.uibk.dps.biohadoop.communication.RemoteExecutable;
import at.ac.uibk.dps.biohadoop.functions.Function;
import at.ac.uibk.dps.biohadoop.functions.FunctionProvider;

public class RemoteFunctionValue implements
		RemoteExecutable<Function, double[], double[]> {

	@Override
	public Function getInitalData() {
		return FunctionProvider.getFunction();
	}
	
	@Override
	public double[] compute(double[] data, Function initialData) {
		double[] result = new double[2];
		result[0] = initialData.f1(data);
		result[1] = initialData.f2(data);
		return result;
	}

}
