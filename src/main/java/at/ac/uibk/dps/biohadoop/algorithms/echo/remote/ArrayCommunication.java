package at.ac.uibk.dps.biohadoop.algorithms.echo.remote;

import at.ac.uibk.dps.biohadoop.unifiedcommunication.RemoteExecutable;

public class ArrayCommunication implements
		RemoteExecutable<int[], double[], String[]> {

	@Override
	public int[] getInitalData() {
		return new int[] {0, 1, 2, 23};
	}

	@Override
	public String[] compute(double[] data, int[] registrationObject) {
		return new String[] {registrationObject + " - " + System.currentTimeMillis(), "YEAH"};
	}

}
