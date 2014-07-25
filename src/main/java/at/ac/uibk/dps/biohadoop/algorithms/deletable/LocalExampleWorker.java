package at.ac.uibk.dps.biohadoop.algorithms.deletable;

import java.util.Date;

import at.ac.uibk.dps.biohadoop.communication.master.MasterEndpoint;
import at.ac.uibk.dps.biohadoop.communication.worker.LocalWorker2;

public class LocalExampleWorker extends LocalWorker2<Integer, String> {

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
