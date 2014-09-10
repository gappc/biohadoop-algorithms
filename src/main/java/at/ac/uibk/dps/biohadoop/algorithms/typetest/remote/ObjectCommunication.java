package at.ac.uibk.dps.biohadoop.algorithms.typetest.remote;

import java.util.Date;
import java.util.UUID;

import at.ac.uibk.dps.biohadoop.tasksystem.ComputeException;
import at.ac.uibk.dps.biohadoop.tasksystem.AsyncComputable;

public class ObjectCommunication implements
		AsyncComputable<UUID, Date, String> {

	@Override
	public String compute(Date data, UUID initialData) throws ComputeException {
		return "initialData: " + initialData + " Data:" + data;
	}

}
