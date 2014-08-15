package at.ac.uibk.dps.biohadoop.algorithms.ga.remote;

import at.ac.uibk.dps.biohadoop.algorithms.ga.DistancesGlobal;
import at.ac.uibk.dps.biohadoop.algorithms.ga.algorithm.GaFitness;
import at.ac.uibk.dps.biohadoop.communication.RemoteExecutable;

public class RemoteFitness implements RemoteExecutable<double[][], int[], Double> {

	@Override
	public double[][] getInitalData() {
		return DistancesGlobal.getDistances();
	}
	
	@Override
	public Double compute(int[] data, double[][] initialData) {
		return GaFitness.computeFitness(initialData, data);
	}

}
