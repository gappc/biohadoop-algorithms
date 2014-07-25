package at.ac.uibk.dps.biohadoop.algorithms.example.communication.master;

import at.ac.uibk.dps.biohadoop.algorithms.example.algorithm.Example;
import at.ac.uibk.dps.biohadoop.algorithms.example.communication.worker.LocalExampleWorker;
import at.ac.uibk.dps.biohadoop.communication.master.Master;
import at.ac.uibk.dps.biohadoop.communication.master.kryo.KryoMaster;
import at.ac.uibk.dps.biohadoop.communication.master.local.LocalMaster;
import at.ac.uibk.dps.biohadoop.communication.master.rest.RestMaster;
import at.ac.uibk.dps.biohadoop.communication.master.socket.SocketMaster;
import at.ac.uibk.dps.biohadoop.communication.master.websocket.WebSocketMaster;

@KryoMaster(queueName = Example.EXAMPLE_QUEUE)
@LocalMaster(queueName = Example.EXAMPLE_QUEUE, localWorker = LocalExampleWorker.class)
@RestMaster(path = "example", queueName = Example.EXAMPLE_QUEUE, receive=String.class)
@SocketMaster(queueName = Example.EXAMPLE_QUEUE)
@WebSocketMaster(path = "example", queueName = Example.EXAMPLE_QUEUE)
public class ExampleMaster implements Master {

	@Override
	public Object getRegistrationObject() {
		return null;
	}

}
