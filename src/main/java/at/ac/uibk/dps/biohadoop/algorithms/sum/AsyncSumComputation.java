package at.ac.uibk.dps.biohadoop.algorithms.sum;

import at.ac.uibk.dps.biohadoop.tasksystem.Worker;
import at.ac.uibk.dps.biohadoop.tasksystem.ComputeException;

public class AsyncSumComputation implements Worker<Object, int[], Integer> {

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
