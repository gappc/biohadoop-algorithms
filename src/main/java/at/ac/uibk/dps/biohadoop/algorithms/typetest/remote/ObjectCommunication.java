package at.ac.uibk.dps.biohadoop.algorithms.typetest.remote;

import java.util.Date;
import java.util.UUID;

import at.ac.uibk.dps.biohadoop.communication.ComputeException;
import at.ac.uibk.dps.biohadoop.communication.RemoteExecutable;

public class ObjectCommunication implements
		RemoteExecutable<UUID, Date, String> {

	@Override
	public String compute(Date data, UUID initialData) throws ComputeException {
		return "initialData: " + initialData + " Data:" + data;
	}

}
