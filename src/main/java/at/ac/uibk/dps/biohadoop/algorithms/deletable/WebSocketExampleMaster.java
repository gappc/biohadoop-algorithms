package at.ac.uibk.dps.biohadoop.algorithms.deletable;

import javax.websocket.server.ServerEndpoint;

import at.ac.uibk.dps.biohadoop.algorithms.example.algorithm.Example;
import at.ac.uibk.dps.biohadoop.communication.master.websocket.WebSocketDecoder;
import at.ac.uibk.dps.biohadoop.communication.master.websocket.WebSocketEncoder;
import at.ac.uibk.dps.biohadoop.communication.master.websocket.WebSocketEndpoint;

@ServerEndpoint(value = "/example", encoders = WebSocketEncoder.class, decoders = WebSocketDecoder.class)
public class WebSocketExampleMaster extends WebSocketEndpoint {

	@Override
	public String getQueueName() {
		return Example.EXAMPLE_QUEUE;
	}

	@Override
	public Object getRegistrationObject() {
		return null;
	}

}
