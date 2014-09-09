package at.ac.uibk.dps.biohadoop.algorithms.ga.remote;

import at.ac.uibk.dps.biohadoop.algorithms.ga.algorithm.GaFitness;
import at.ac.uibk.dps.biohadoop.tasksystem.ComputeException;
import at.ac.uibk.dps.biohadoop.tasksystem.RemoteExecutable;

public class RemoteFitness implements
		RemoteExecutable<double[][], int[], Double> {

	@Override
	public Double compute(int[] data, double[][] initialData)
			throws ComputeException {
		return GaFitness.computeFitness(initialData, data);
	}

}
