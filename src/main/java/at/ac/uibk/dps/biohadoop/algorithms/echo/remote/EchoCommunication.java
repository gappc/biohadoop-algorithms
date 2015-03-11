package at.ac.uibk.dps.biohadoop.algorithms.echo.remote;

import at.ac.uibk.dps.biohadoop.tasksystem.ComputeException;
import at.ac.uibk.dps.biohadoop.tasksystem.Worker;

public class EchoCommunication implements
		Worker<String, String, String> {

	@Override
	public String compute(String data, String initialData)
			throws ComputeException {
		return data + " (" + initialData + ")";
	}

}
