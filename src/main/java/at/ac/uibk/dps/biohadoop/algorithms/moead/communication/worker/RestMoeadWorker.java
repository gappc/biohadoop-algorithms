package at.ac.uibk.dps.biohadoop.algorithms.moead.communication.worker;

import com.fasterxml.jackson.core.type.TypeReference;

import at.ac.uibk.dps.biohadoop.algorithms.moead.algorithm.Functions;
import at.ac.uibk.dps.biohadoop.algorithms.moead.communication.master.MoeadRest;
import at.ac.uibk.dps.biohadoop.communication.Message;
import at.ac.uibk.dps.biohadoop.communication.master.MasterEndpoint;
import at.ac.uibk.dps.biohadoop.communication.worker.RestWorker;

public class RestMoeadWorker extends RestWorker<double[], double[]> {

	@Override
	public Class<? extends MasterEndpoint> getMasterEndpoint() {
		return MoeadRest.class;
	}
	
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

	@Override
	public String getPath() {
		return "/moead";
	}

	@Override
	public TypeReference<Message<double[]>> getInputType() {
		return new TypeReference<Message<double[]>>() {
		};
	}
	
}
