package at.ac.uibk.dps.biohadoop.algorithms.ga.communication.master;

import at.ac.uibk.dps.biohadoop.algorithms.ga.DistancesGlobal;
import at.ac.uibk.dps.biohadoop.algorithms.ga.algorithm.Ga;
import at.ac.uibk.dps.biohadoop.algorithms.ga.communication.worker.LocalGaWorker;
import at.ac.uibk.dps.biohadoop.communication.master.Master;
import at.ac.uibk.dps.biohadoop.communication.master.kryo.KryoMaster;
import at.ac.uibk.dps.biohadoop.communication.master.local.LocalMaster;
import at.ac.uibk.dps.biohadoop.communication.master.rest.RestMaster;
import at.ac.uibk.dps.biohadoop.communication.master.socket.SocketMaster;
import at.ac.uibk.dps.biohadoop.communication.master.websocket.WebSocketMaster;

@KryoMaster(queueName = Ga.GA_QUEUE)
@LocalMaster(queueName = Ga.GA_QUEUE, localWorker = LocalGaWorker.class)
@RestMaster(path = "ga", queueName = Ga.GA_QUEUE, receive=Double.class)
@SocketMaster(queueName = Ga.GA_QUEUE)
@WebSocketMaster(path = "ga", queueName = Ga.GA_QUEUE)
public class GaMaster implements Master {

	@Override
	public Object getRegistrationObject() {
		return DistancesGlobal.getDistances();
	}

}
