package at.ac.uibk.dps.biohadoop.algorithms.example.communication.worker;

import java.util.Date;

import at.ac.uibk.dps.biohadoop.algorithms.example.communication.master.KryoExampleMaster;
import at.ac.uibk.dps.biohadoop.communication.master.MasterEndpoint;
import at.ac.uibk.dps.biohadoop.communication.worker.KryoWorker;

public class KryoExampleWorker extends KryoWorker<Integer, String> {

	@Override
	public Class<? extends MasterEndpoint> getMasterEndpoint() {
		return KryoExampleMaster.class;
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
