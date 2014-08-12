package at.ac.uibk.dps.biohadoop.algorithms.ga.remote;

import at.ac.uibk.dps.biohadoop.algorithms.ga.DistancesGlobal;
import at.ac.uibk.dps.biohadoop.algorithms.ga.algorithm.GaFitness;
import at.ac.uibk.dps.biohadoop.communication.annotation.DedicatedKryo;
import at.ac.uibk.dps.biohadoop.communication.annotation.DedicatedLocal;
import at.ac.uibk.dps.biohadoop.communication.annotation.DedicatedRest;
import at.ac.uibk.dps.biohadoop.communication.annotation.DedicatedSocket;
import at.ac.uibk.dps.biohadoop.communication.annotation.DedicatedWebSocket;
import at.ac.uibk.dps.biohadoop.unifiedcommunication.RemoteExecutable;

@DedicatedKryo(queueName="tada")
@DedicatedLocal(queueName="tada")
@DedicatedRest(queueName="tada")
@DedicatedSocket(queueName="tada")
@DedicatedWebSocket(queueName="tada")
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
