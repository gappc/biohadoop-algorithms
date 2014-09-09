package at.ac.uibk.dps.biohadoop.algorithms.typetest.remote;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;

import at.ac.uibk.dps.biohadoop.tasksystem.ComputeException;
import at.ac.uibk.dps.biohadoop.tasksystem.RemoteExecutable;

public class ListCommunication implements
		RemoteExecutable<List<Integer>, List<Double>, List<String>> {

	@Override
	public List<String> compute(List<Double> data, List<Integer> initialData)
			throws ComputeException {
		List<String> result = new ArrayList<String>();
		for (Double value : data) {
			String s = "initialData: " + ArrayUtils.toString(initialData)
					+ " data: " + value;
			result.add(s);
		}
		return result;
	}

}
