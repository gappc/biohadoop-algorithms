package at.ac.uibk.dps.biohadoop.algorithms.moead.communication.worker;

import at.ac.uibk.dps.biohadoop.algorithms.moead.algorithm.Functions;
import at.ac.uibk.dps.biohadoop.algorithms.moead.communication.master.MoeadMaster;
import at.ac.uibk.dps.biohadoop.communication.worker.RestWorkerAnnotation;
import at.ac.uibk.dps.biohadoop.communication.worker.SuperWorker;

@RestWorkerAnnotation(master=MoeadMaster.class, receive=double[].class)
public class RestMoeadWorker implements SuperWorker<double[], double[]> {
	
	@Override
	public void readRegistrationObject(Object data) {
		// No registration object for MOEAD
	}

	@Override
	public double[] compute(double[] data) {
		double[] result = new double[2];
		result[0] = Functions.f1(data);
		result[1] = Functions.f2(data);
		return result;
	}
	
}
