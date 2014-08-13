package at.ac.uibk.dps.biohadoop.algorithms.echo.remote;

import at.ac.uibk.dps.biohadoop.communication.RemoteExecutable;

public class DateCommunication implements
		RemoteExecutable<Long, Long, String> {

	@Override
	public Long getInitalData() {
		return System.currentTimeMillis();
	}

	@Override
	public String compute(Long data, Long registrationObject) {
		return registrationObject + " - " + System.currentTimeMillis();
	}

}
