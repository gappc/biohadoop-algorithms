package at.ac.uibk.dps.biohadoop.algorithms.nsgaii.communication.worker;

import at.ac.uibk.dps.biohadoop.algorithms.nsgaii.algorithm.Functions;
import at.ac.uibk.dps.biohadoop.algorithms.nsgaii.communication.master.NsgaIIKryo;
import at.ac.uibk.dps.biohadoop.communication.master.MasterEndpoint;
import at.ac.uibk.dps.biohadoop.communication.worker.KryoWorker;

public class KryoNsgaIIWorker extends KryoWorker<double[], double[]> {

	@Override
	public Class<? extends MasterEndpoint> getMasterEndpoint() {
		return NsgaIIKryo.class;
	}

	@Override
	public void readRegistrationObject(Object data) {
		// No registration object for NSGA-II
	}

	@Override
	public double[] compute(double[] data) {
		double[] result = new double[2];
		result[0] = Functions.f1(data);
		result[1] = Functions.f2(data);
		return result;
	}

}
