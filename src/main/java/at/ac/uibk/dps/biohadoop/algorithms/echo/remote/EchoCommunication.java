package at.ac.uibk.dps.biohadoop.algorithms.echo.remote;

import at.ac.uibk.dps.biohadoop.communication.RemoteExecutable;


public class EchoCommunication implements
		RemoteExecutable<String, Integer, String> {

	@Override
	public String getInitalData() {
		return "FUCK";
	}

	@Override
	public String compute(Integer data, String registrationObject) {
		return registrationObject + " --- " + data;
	}

}
