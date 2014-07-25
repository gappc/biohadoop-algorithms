package at.ac.uibk.dps.biohadoop.algorithms.deletable;

import at.ac.uibk.dps.biohadoop.algorithms.ga.DistancesGlobal;
import at.ac.uibk.dps.biohadoop.algorithms.ga.algorithm.Ga;
import at.ac.uibk.dps.biohadoop.communication.master.local.LocalEndpoint;
import at.ac.uibk.dps.biohadoop.communication.worker.LocalWorker2;

public class GaLocal extends LocalEndpoint {

	@Override
	public String getQueueName() {
		return Ga.GA_QUEUE;
	}

	@Override
	public Object getRegistrationObject() {
		return DistancesGlobal.getDistances();
	}

	@Override
	public Class<? extends LocalWorker2<?, ?>> getWorkerClass() {
//		return LocalGaWorker.class;
		return null;
	}

}
