package at.ac.uibk.dps.biohadoop.algorithms.nsgaii.communication.master;

import at.ac.uibk.dps.biohadoop.algorithms.ga.DistancesGlobal;
import at.ac.uibk.dps.biohadoop.algorithms.nsgaii.algorithm.NsgaII;
import at.ac.uibk.dps.biohadoop.algorithms.nsgaii.communication.worker.LocalNsgaIIWorker;
import at.ac.uibk.dps.biohadoop.communication.master.kryo2.KryoMaster;
import at.ac.uibk.dps.biohadoop.communication.master.local2.LocalMaster;
import at.ac.uibk.dps.biohadoop.communication.master.rest2.RestMaster;
import at.ac.uibk.dps.biohadoop.communication.master.rest2.SuperComputable;
import at.ac.uibk.dps.biohadoop.communication.master.socket2.SocketMaster;
import at.ac.uibk.dps.biohadoop.communication.master.websocket2.WebSocketMaster;

@KryoMaster(queueName = NsgaII.NSGAII_QUEUE)
@LocalMaster(queueName = NsgaII.NSGAII_QUEUE, localWorker = LocalNsgaIIWorker.class)
@RestMaster(path = "nsgaii", queueName = NsgaII.NSGAII_QUEUE, receive=double[].class)
@SocketMaster(queueName = NsgaII.NSGAII_QUEUE)
@WebSocketMaster(path = "nsgaii", queueName = NsgaII.NSGAII_QUEUE)
public class NsgaIIMaster implements SuperComputable {

	@Override
	public Object getRegistrationObject() {
		return DistancesGlobal.getDistances();
	}

}
