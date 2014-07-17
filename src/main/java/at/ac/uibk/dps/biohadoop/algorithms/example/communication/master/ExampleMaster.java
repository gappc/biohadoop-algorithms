package at.ac.uibk.dps.biohadoop.algorithms.example.communication.master;

import at.ac.uibk.dps.biohadoop.algorithms.example.algorithm.Example;
import at.ac.uibk.dps.biohadoop.algorithms.example.communication.worker.ExampleWorker;
import at.ac.uibk.dps.biohadoop.communication.master.kryo2.KryoMaster;
import at.ac.uibk.dps.biohadoop.communication.master.local2.LocalMaster;
import at.ac.uibk.dps.biohadoop.communication.master.rest2.RestMaster;
import at.ac.uibk.dps.biohadoop.communication.master.rest2.SuperComputable;
import at.ac.uibk.dps.biohadoop.communication.master.socket2.SocketMaster;
import at.ac.uibk.dps.biohadoop.communication.master.websocket2.WebSocketMaster;

@KryoMaster(queueName = Example.EXAMPLE_QUEUE)
@LocalMaster(queueName = Example.EXAMPLE_QUEUE, localWorker = ExampleWorker.class)
@RestMaster(path = "testadicazzo", queueName = Example.EXAMPLE_QUEUE, receive=String.class)
@SocketMaster(queueName = Example.EXAMPLE_QUEUE)
@WebSocketMaster(path = "testadio", queueName = Example.EXAMPLE_QUEUE)
public class ExampleMaster implements SuperComputable {

	@Override
	public Object getRegistrationObject() {
		return null;
	}

}
