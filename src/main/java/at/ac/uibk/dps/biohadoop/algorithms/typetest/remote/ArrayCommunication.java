package at.ac.uibk.dps.biohadoop.algorithms.typetest.remote;

import org.apache.commons.lang.ArrayUtils;

import at.ac.uibk.dps.biohadoop.tasksystem.ComputeException;
import at.ac.uibk.dps.biohadoop.tasksystem.RemoteExecutable;

public class ArrayCommunication implements
		RemoteExecutable<int[], double[], String[]> {

	@Override
	public String[] compute(double[] data, int[] initialData)
			throws ComputeException {
		String[] result = new String[data.length];
		for (int i = 0; i < result.length; i++) {
			String s = "initialData: " + ArrayUtils.toString(initialData)
					+ " data: " + data[i];
			result[i] = s;
		}
		return result;
	}

}
