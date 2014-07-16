package at.ac.uibk.dps.biohadoop.algorithms.ga.communication.worker;

import at.ac.uibk.dps.biohadoop.algorithms.ga.algorithm.GaFitness;
import at.ac.uibk.dps.biohadoop.algorithms.ga.communication.master.GaRest;
import at.ac.uibk.dps.biohadoop.communication.Message;
import at.ac.uibk.dps.biohadoop.communication.master.MasterEndpoint;
import at.ac.uibk.dps.biohadoop.communication.worker.RestWorker;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RestGaWorker extends RestWorker<int[], Double> {

	private double[][] distances;

	@Override
	public Class<? extends MasterEndpoint> getMasterEndpoint() {
		return GaRest.class;
	}
	
	@Override
	public void readRegistrationObject(Object data) {
		ObjectMapper mapper = new ObjectMapper();
		distances = mapper.convertValue(data, double[][].class);
	}

	@Override
	public Double compute(int[] data) {
		return GaFitness.computeFitness(distances, data);
	}

	@Override
	public TypeReference<Message<int[]>> getInputType() {
		return new TypeReference<Message<int[]>>() {
		};
	}

}
