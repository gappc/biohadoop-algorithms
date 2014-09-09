package at.ac.uibk.dps.biohadoop.algorithms.sleep;

import at.ac.uibk.dps.biohadoop.tasksystem.ComputeException;
import at.ac.uibk.dps.biohadoop.tasksystem.RemoteExecutable;

public class SleepRemoteExecutable implements
		RemoteExecutable<Long, Object, Object> {

	@Override
	public Object compute(Object data, Long initialData)
			throws ComputeException {
		try {
			Thread.sleep(initialData);
			return null;
		} catch (InterruptedException e) {
			throw new ComputeException("Error while sleeping for "
					+ initialData + "ms", e);
		}
	}

}
