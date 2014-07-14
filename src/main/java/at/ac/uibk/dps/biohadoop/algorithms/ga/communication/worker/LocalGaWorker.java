package at.ac.uibk.dps.biohadoop.algorithms.ga.communication.worker;

import at.ac.uibk.dps.biohadoop.algorithms.ga.algorithm.GaFitness;
import at.ac.uibk.dps.biohadoop.algorithms.ga.communication.master.GaLocal;
import at.ac.uibk.dps.biohadoop.communication.master.MasterEndpoint;
import at.ac.uibk.dps.biohadoop.communication.worker.LocalWorker;

public class LocalGaWorker extends LocalWorker<int[], Double> {

	private double[][] distances;

	@Override
	public void readRegistrationObject(Object data) {
		distances = (double[][]) data;
	}

	@Override
	public Double compute(int[] data) {
		return GaFitness.computeFitness(distances, data);
	}

	@Override
	public Class<? extends MasterEndpoint> getMasterEndpoint() {
		return GaLocal.class;
	}

}
