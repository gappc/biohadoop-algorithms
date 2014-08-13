package at.ac.uibk.dps.biohadoop.algorithms.echo.remote;

import at.ac.uibk.dps.biohadoop.algorithms.echo.algorithm.Echo;
import at.ac.uibk.dps.biohadoop.communication.RemoteExecutable;
import at.ac.uibk.dps.biohadoop.communication.annotation.DedicatedKryo;
import at.ac.uibk.dps.biohadoop.communication.annotation.DedicatedLocal;
import at.ac.uibk.dps.biohadoop.communication.annotation.DedicatedRest;
import at.ac.uibk.dps.biohadoop.communication.annotation.DedicatedSocket;
import at.ac.uibk.dps.biohadoop.communication.annotation.DedicatedWebSocket;

// set annotation to start local worker for given queue
@DedicatedLocal(queueName=Echo.ECHO_QUEUE)
@DedicatedKryo(queueName=Echo.ECHO_QUEUE)
@DedicatedRest(queueName=Echo.ECHO_QUEUE)
@DedicatedSocket(queueName=Echo.ECHO_QUEUE)
@DedicatedWebSocket(queueName=Echo.ECHO_QUEUE)
public class StringCommunication implements
		RemoteExecutable<String, String, String> {

	@Override
	public String getInitalData() {
		return "FUCK";
	}

	@Override
	public String compute(String data, String registrationObject) {
		return registrationObject + " --- " + data;
	}

}
