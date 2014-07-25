package at.ac.uibk.dps.biohadoop.algorithms.deletable;

import at.ac.uibk.dps.biohadoop.algorithms.moead.algorithm.Moead;
import at.ac.uibk.dps.biohadoop.communication.master.socket.SocketServer;

public class MoeadSocket extends SocketServer {

	@Override
	public String getQueueName() {
		return Moead.MOEAD_QUEUE;
	}

	@Override
	public Object getRegistrationObject() {
		return null;
	}
}
