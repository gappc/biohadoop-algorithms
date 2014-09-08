package at.ac.uibk.dps.biohadoop.algorithms.echo.remote;

import at.ac.uibk.dps.biohadoop.communication.ComputeException;
import at.ac.uibk.dps.biohadoop.communication.RemoteExecutable;

public class EchoCommunication implements
		RemoteExecutable<String, String, String> {

	@Override
	public String compute(String data, String initialData)
			throws ComputeException {
		return data + " (" + initialData + ")";
	}

}
