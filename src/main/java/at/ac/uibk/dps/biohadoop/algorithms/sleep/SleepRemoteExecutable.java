package at.ac.uibk.dps.biohadoop.algorithms.sleep;

import at.ac.uibk.dps.biohadoop.communication.ComputeException;
import at.ac.uibk.dps.biohadoop.communication.RemoteExecutable;

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
