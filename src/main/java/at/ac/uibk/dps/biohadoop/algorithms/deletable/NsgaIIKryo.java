package at.ac.uibk.dps.biohadoop.algorithms.deletable;

import at.ac.uibk.dps.biohadoop.algorithms.nsgaii.algorithm.NsgaII;
import at.ac.uibk.dps.biohadoop.communication.master.kryo.KryoServer;

public class NsgaIIKryo extends KryoServer {

	@Override
	public String getQueueName() {
		return NsgaII.NSGAII_QUEUE;
	}

	@Override
	public Object getRegistrationObject() {
		return null;
	}

}
