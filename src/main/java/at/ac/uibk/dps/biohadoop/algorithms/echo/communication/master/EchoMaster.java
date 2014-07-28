package at.ac.uibk.dps.biohadoop.algorithms.echo.communication.master;

import at.ac.uibk.dps.biohadoop.algorithms.echo.algorithm.Echo;
import at.ac.uibk.dps.biohadoop.algorithms.echo.communication.worker.LocalEchoWorker;
import at.ac.uibk.dps.biohadoop.communication.master.Master;
import at.ac.uibk.dps.biohadoop.communication.master.kryo.KryoMaster;
import at.ac.uibk.dps.biohadoop.communication.master.local.LocalMaster;
import at.ac.uibk.dps.biohadoop.communication.master.rest.RestMaster;
import at.ac.uibk.dps.biohadoop.communication.master.socket.SocketMaster;
import at.ac.uibk.dps.biohadoop.communication.master.websocket.WebSocketMaster;

@KryoMaster(queueName = Echo.ECHO_QUEUE)
@LocalMaster(queueName = Echo.ECHO_QUEUE, localWorker = LocalEchoWorker.class)
@RestMaster(path = "echo", queueName = Echo.ECHO_QUEUE, receive=String.class)
@SocketMaster(queueName = Echo.ECHO_QUEUE)
@WebSocketMaster(path = "echo", queueName = Echo.ECHO_QUEUE)
public class EchoMaster implements Master {

	@Override
	public Object getRegistrationObject() {
		return null;
	}

}
