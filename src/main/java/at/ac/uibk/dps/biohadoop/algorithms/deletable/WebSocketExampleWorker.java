package at.ac.uibk.dps.biohadoop.algorithms.deletable;

import java.util.Date;

import javax.websocket.ClientEndpoint;

import at.ac.uibk.dps.biohadoop.communication.master.MasterEndpoint;
import at.ac.uibk.dps.biohadoop.communication.master.websocket.WebSocketDecoder;
import at.ac.uibk.dps.biohadoop.communication.master.websocket.WebSocketEncoder;
import at.ac.uibk.dps.biohadoop.communication.worker.WebSocketWorker2;

@ClientEndpoint(encoders = WebSocketEncoder.class, decoders = WebSocketDecoder.class)
public class WebSocketExampleWorker extends WebSocketWorker2<Integer, String> {

	@Override
	public Class<? extends MasterEndpoint> getMasterEndpoint() {
		return WebSocketExampleMaster.class;
	}
	
	@Override
	public void readRegistrationObject(Object data) {
		// Since there is no registration Object, there is nothing to do
	}

	@Override
	public String compute(Integer data) {
		return "Got " + data + " at " + new Date();
	}
}
