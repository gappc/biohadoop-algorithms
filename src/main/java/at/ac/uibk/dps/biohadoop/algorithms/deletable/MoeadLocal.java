package at.ac.uibk.dps.biohadoop.algorithms.deletable;

import at.ac.uibk.dps.biohadoop.algorithms.moead.algorithm.Moead;
import at.ac.uibk.dps.biohadoop.communication.master.local.LocalEndpoint;
import at.ac.uibk.dps.biohadoop.communication.worker.LocalWorker2;

public class MoeadLocal extends LocalEndpoint {

	@Override
	public String getQueueName() {
		return Moead.MOEAD_QUEUE;
	}

	@Override
	public Object getRegistrationObject() {
		return null;
	}

	@Override
	public Class<? extends LocalWorker2<?, ?>> getWorkerClass() {
		return null;
//		return LocalMoeadWorker.class;
	}
}
