package at.ac.uibk.dps.biohadoop.algorithms.example.communication.worker;

import java.util.Date;

import at.ac.uibk.dps.biohadoop.algorithms.example.communication.master.LocalExampleMaster;
import at.ac.uibk.dps.biohadoop.communication.master.MasterEndpoint;
import at.ac.uibk.dps.biohadoop.communication.worker.LocalWorker;

public class LocalExampleWorker extends LocalWorker<Integer, String> {

	@Override
	public Class<? extends MasterEndpoint> getMasterEndpoint() {
		return LocalExampleMaster.class;
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
