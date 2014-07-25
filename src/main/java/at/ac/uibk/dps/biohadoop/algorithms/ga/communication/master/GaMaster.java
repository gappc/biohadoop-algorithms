package at.ac.uibk.dps.biohadoop.algorithms.ga.communication.master;

import at.ac.uibk.dps.biohadoop.algorithms.ga.DistancesGlobal;
import at.ac.uibk.dps.biohadoop.algorithms.ga.algorithm.Ga;
import at.ac.uibk.dps.biohadoop.algorithms.ga.communication.worker.LocalGaWorker;
import at.ac.uibk.dps.biohadoop.communication.master.kryo2.KryoMaster;
import at.ac.uibk.dps.biohadoop.communication.master.local2.LocalMaster;
import at.ac.uibk.dps.biohadoop.communication.master.rest2.RestMaster;
import at.ac.uibk.dps.biohadoop.communication.master.rest2.SuperComputable;
import at.ac.uibk.dps.biohadoop.communication.master.socket2.SocketMaster;
import at.ac.uibk.dps.biohadoop.communication.master.websocket2.WebSocketMaster;

@KryoMaster(queueName = Ga.GA_QUEUE)
@LocalMaster(queueName = Ga.GA_QUEUE, localWorker = LocalGaWorker.class)
@RestMaster(path = "ga", queueName = Ga.GA_QUEUE, receive=Double.class)
@SocketMaster(queueName = Ga.GA_QUEUE)
@WebSocketMaster(path = "ga", queueName = Ga.GA_QUEUE)
public class GaMaster implements SuperComputable {

	@Override
	public Object getRegistrationObject() {
		return DistancesGlobal.getDistances();
	}

}
