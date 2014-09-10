package at.ac.uibk.dps.biohadoop.algorithms.sum;

import at.ac.uibk.dps.biohadoop.tasksystem.AsyncComputable;
import at.ac.uibk.dps.biohadoop.tasksystem.ComputeException;

public class AsyncSumComputation implements AsyncComputable<Object, int[], Integer> {

	@Override
	public Integer compute(int[] data, Object initialData)
			throws ComputeException {
		int sum = 0;
		for (int i : data) {
			sum += i;
		}
		return sum;
	}

}
