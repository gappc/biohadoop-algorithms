package at.ac.uibk.dps.biohadoop.algorithms.deletable;

import at.ac.uibk.dps.biohadoop.algorithms.moead.algorithm.Moead;
import at.ac.uibk.dps.biohadoop.communication.master.kryo.KryoServer;

public class MoeadKryo extends KryoServer {

	@Override
	public String getQueueName() {
		return Moead.MOEAD_QUEUE;
	}

	@Override
	public Object getRegistrationObject() {
		return null;
	}

}
