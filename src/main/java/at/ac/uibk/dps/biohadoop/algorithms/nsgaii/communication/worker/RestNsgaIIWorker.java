package at.ac.uibk.dps.biohadoop.algorithms.nsgaii.communication.worker;

import at.ac.uibk.dps.biohadoop.algorithms.nsgaii.algorithm.Functions;
import at.ac.uibk.dps.biohadoop.algorithms.nsgaii.communication.master.NsgaIIMaster;
import at.ac.uibk.dps.biohadoop.communication.worker.RestWorker;
import at.ac.uibk.dps.biohadoop.communication.worker.Worker;

@RestWorker(master=NsgaIIMaster.class, receive=double[].class)
public class RestNsgaIIWorker implements Worker<double[], double[]> {

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
