package at.ac.uibk.dps.biohadoop.algorithms.ga.communication.worker;

import at.ac.uibk.dps.biohadoop.algorithms.ga.algorithm.GaFitness;
import at.ac.uibk.dps.biohadoop.algorithms.ga.communication.master.GaMaster;
import at.ac.uibk.dps.biohadoop.communication.worker.RestWorker;
import at.ac.uibk.dps.biohadoop.communication.worker.Worker;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestWorker(master=GaMaster.class, receive=int[].class)
public class RestGaWorker implements Worker<int[], Double> {

	private double[][] distances;

	@Override
	public void readRegistrationObject(Object data) {
		ObjectMapper mapper = new ObjectMapper();
		distances = mapper.convertValue(data, double[][].class);
	}

	@Override
	public Double compute(int[] data) {
		return GaFitness.computeFitness(distances, data);
	}

}
