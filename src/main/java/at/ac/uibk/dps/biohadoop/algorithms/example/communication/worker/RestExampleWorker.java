package at.ac.uibk.dps.biohadoop.algorithms.example.communication.worker;

import java.util.Date;

import at.ac.uibk.dps.biohadoop.algorithms.example.communication.master.RestExampleMaster;
import at.ac.uibk.dps.biohadoop.communication.Message;
import at.ac.uibk.dps.biohadoop.communication.master.MasterEndpoint;
import at.ac.uibk.dps.biohadoop.communication.worker.RestWorker;

import com.fasterxml.jackson.core.type.TypeReference;

public class RestExampleWorker extends RestWorker<Integer, String> {

	@Override
	public Class<? extends MasterEndpoint> getMasterEndpoint() {
		return RestExampleMaster.class;
	}
	
	@Override
	public void readRegistrationObject(Object data) {
		// Since there is no registration Object, there is nothing to do
	}

	@Override
	public String compute(Integer data) {
		return "Got " + data + " at " + new Date();
	}

	@Override
	public TypeReference<Message<Integer>> getInputType() {
		return new TypeReference<Message<Integer>>() {
		};
	}

}
