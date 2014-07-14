package at.ac.uibk.dps.biohadoop.algorithms.nsgaii.communication.master;

import javax.websocket.server.ServerEndpoint;

import at.ac.uibk.dps.biohadoop.algorithms.nsgaii.algorithm.NsgaII;
import at.ac.uibk.dps.biohadoop.communication.master.websocket.WebSocketDecoder;
import at.ac.uibk.dps.biohadoop.communication.master.websocket.WebSocketEncoder;
import at.ac.uibk.dps.biohadoop.communication.master.websocket.WebSocketEndpoint;

@ServerEndpoint(value = "/nsgaii", encoders = WebSocketEncoder.class, decoders = WebSocketDecoder.class)
public class NsgaIIWebSocket extends WebSocketEndpoint {

	@Override
	public String getQueueName() {
		return NsgaII.NSGAII_QUEUE;
	}

	@Override
	public Object getRegistrationObject() {
		return null;
	}

}
