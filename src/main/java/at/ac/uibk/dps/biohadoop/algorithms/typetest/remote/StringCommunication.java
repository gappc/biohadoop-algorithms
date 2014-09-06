package at.ac.uibk.dps.biohadoop.algorithms.typetest.remote;

import at.ac.uibk.dps.biohadoop.communication.RemoteExecutable;

public class StringCommunication implements
		RemoteExecutable<String, String, String> {

	@Override
	public String compute(String data, String initialData) {
		return data + " (" + initialData + ")";
	}

}
