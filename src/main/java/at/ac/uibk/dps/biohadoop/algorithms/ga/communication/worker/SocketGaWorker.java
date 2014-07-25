package at.ac.uibk.dps.biohadoop.algorithms.ga.communication.worker;

import at.ac.uibk.dps.biohadoop.algorithms.ga.algorithm.GaFitness;
import at.ac.uibk.dps.biohadoop.algorithms.ga.communication.master.GaMaster;
import at.ac.uibk.dps.biohadoop.communication.worker.SocketWorkerAnnotation;
import at.ac.uibk.dps.biohadoop.communication.worker.SuperWorker;

@SocketWorkerAnnotation(master=GaMaster.class)
public class SocketGaWorker implements SuperWorker<int[], Double> {

	private double[][] distances;

	@Override
	public void readRegistrationObject(Object data) {
		distances = (double[][])data;
	}

	@Override
	public Double compute(int[] data) {
		return GaFitness.computeFitness(distances, data);
	}

}
