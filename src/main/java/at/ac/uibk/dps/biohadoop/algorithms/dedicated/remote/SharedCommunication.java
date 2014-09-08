package at.ac.uibk.dps.biohadoop.algorithms.dedicated.remote;

import at.ac.uibk.dps.biohadoop.communication.ComputeException;
import at.ac.uibk.dps.biohadoop.communication.RemoteExecutable;

public class SharedCommunication implements
		RemoteExecutable<String, String, String> {

	@Override
	public String compute(String data, String initialData)
			throws ComputeException {
		return data + " (passed shared worker computation)";
	}

}
