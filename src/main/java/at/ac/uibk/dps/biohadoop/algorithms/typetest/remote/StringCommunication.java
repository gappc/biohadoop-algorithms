package at.ac.uibk.dps.biohadoop.algorithms.typetest.remote;

import at.ac.uibk.dps.biohadoop.tasksystem.ComputeException;
import at.ac.uibk.dps.biohadoop.tasksystem.AsyncComputable;

public class StringCommunication implements
		AsyncComputable<String, String, String> {

	@Override
	public String compute(String data, String initialData)
			throws ComputeException {
		return data + " (" + initialData + ")";
	}

}
