package at.ac.uibk.dps.biohadoop.algorithms.deletable;

import at.ac.uibk.dps.biohadoop.algorithms.example.algorithm.Example;
import at.ac.uibk.dps.biohadoop.communication.master.kryo.KryoServer;

public class KryoExampleMaster extends KryoServer {

	@Override
	public String getQueueName() {
		return Example.EXAMPLE_QUEUE;
	}

	@Override
	public Object getRegistrationObject() {
		return null;
	}
}
