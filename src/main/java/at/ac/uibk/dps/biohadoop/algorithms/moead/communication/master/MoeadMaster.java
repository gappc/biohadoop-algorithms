package at.ac.uibk.dps.biohadoop.algorithms.moead.communication.master;

import at.ac.uibk.dps.biohadoop.algorithms.ga.DistancesGlobal;
import at.ac.uibk.dps.biohadoop.algorithms.moead.algorithm.Moead;
import at.ac.uibk.dps.biohadoop.algorithms.moead.communication.worker.LocalMoeadWorker;
import at.ac.uibk.dps.biohadoop.communication.master.kryo2.KryoMaster;
import at.ac.uibk.dps.biohadoop.communication.master.local2.LocalMaster;
import at.ac.uibk.dps.biohadoop.communication.master.rest2.RestMaster;
import at.ac.uibk.dps.biohadoop.communication.master.rest2.SuperComputable;
import at.ac.uibk.dps.biohadoop.communication.master.socket2.SocketMaster;
import at.ac.uibk.dps.biohadoop.communication.master.websocket2.WebSocketMaster;

@KryoMaster(queueName = Moead.MOEAD_QUEUE)
@LocalMaster(queueName = Moead.MOEAD_QUEUE, localWorker = LocalMoeadWorker.class)
@RestMaster(path = "moead", queueName = Moead.MOEAD_QUEUE, receive=double[].class)
@SocketMaster(queueName = Moead.MOEAD_QUEUE)
@WebSocketMaster(path = "moead", queueName = Moead.MOEAD_QUEUE)
public class MoeadMaster implements SuperComputable {

	@Override
	public Object getRegistrationObject() {
		return DistancesGlobal.getDistances();
	}

}
