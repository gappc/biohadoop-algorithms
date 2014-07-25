package at.ac.uibk.dps.biohadoop.algorithms.deletable;

import at.ac.uibk.dps.biohadoop.algorithms.example.algorithm.Example;
import at.ac.uibk.dps.biohadoop.communication.master.local.LocalEndpoint;
import at.ac.uibk.dps.biohadoop.communication.worker.LocalWorker2;

public class LocalExampleMaster extends LocalEndpoint {

	@Override
	public String getQueueName() {
		return Example.EXAMPLE_QUEUE;
	}

	@Override
	public Object getRegistrationObject() {
		return null;
	}

	@Override
	public Class<? extends LocalWorker2<?, ?>> getWorkerClass() {
		return LocalExampleWorker.class;
	}

}
