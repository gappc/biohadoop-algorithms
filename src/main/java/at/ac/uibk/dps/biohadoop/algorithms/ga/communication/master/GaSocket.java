package at.ac.uibk.dps.biohadoop.algorithms.ga.communication.master;

import at.ac.uibk.dps.biohadoop.algorithms.ga.DistancesGlobal;
import at.ac.uibk.dps.biohadoop.algorithms.ga.algorithm.Ga;
import at.ac.uibk.dps.biohadoop.communication.master.socket.SocketServer;

public class GaSocket extends SocketServer {

	@Override
	public String getQueueName() {
		return Ga.GA_QUEUE;
	}

	@Override
	public Object getRegistrationObject() {
		return DistancesGlobal.getDistancesAsObject();
	}
}
