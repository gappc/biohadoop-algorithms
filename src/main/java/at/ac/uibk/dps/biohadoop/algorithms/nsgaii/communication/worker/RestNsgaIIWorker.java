package at.ac.uibk.dps.biohadoop.algorithms.nsgaii.communication.worker;

import at.ac.uibk.dps.biohadoop.algorithms.nsgaii.algorithm.Functions;
import at.ac.uibk.dps.biohadoop.algorithms.nsgaii.communication.master.NsgaIIMaster;
import at.ac.uibk.dps.biohadoop.communication.worker.RestWorkerAnnotation;
import at.ac.uibk.dps.biohadoop.communication.worker.SuperWorker;

@RestWorkerAnnotation(master=NsgaIIMaster.class, receive=double[].class)
public class RestNsgaIIWorker implements SuperWorker<double[], double[]> {

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
