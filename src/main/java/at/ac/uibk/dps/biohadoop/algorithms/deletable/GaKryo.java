package at.ac.uibk.dps.biohadoop.algorithms.deletable;

import at.ac.uibk.dps.biohadoop.algorithms.ga.DistancesGlobal;
import at.ac.uibk.dps.biohadoop.algorithms.ga.algorithm.Ga;
import at.ac.uibk.dps.biohadoop.communication.master.kryo.KryoServer;

public class GaKryo extends KryoServer {

	@Override
	public String getQueueName() {
		return Ga.GA_QUEUE;
	}

	@Override
	public Object getRegistrationObject() {
		return DistancesGlobal.getDistancesAsObject();
	}
}
