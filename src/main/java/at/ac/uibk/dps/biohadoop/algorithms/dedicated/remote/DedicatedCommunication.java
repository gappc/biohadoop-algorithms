package at.ac.uibk.dps.biohadoop.algorithms.dedicated.remote;

import at.ac.uibk.dps.biohadoop.communication.RemoteExecutable;

public class DedicatedCommunication implements
		RemoteExecutable<String, String, String> {

	@Override
	public String compute(String data, String initialData) {
		return data + " (passed dedicated worker computation)";
	}

}
