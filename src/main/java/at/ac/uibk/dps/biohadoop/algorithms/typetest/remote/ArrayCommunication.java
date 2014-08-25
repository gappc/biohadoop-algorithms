package at.ac.uibk.dps.biohadoop.algorithms.typetest.remote;

import org.apache.commons.lang.ArrayUtils;

import at.ac.uibk.dps.biohadoop.communication.RemoteExecutable;

public class ArrayCommunication implements
		RemoteExecutable<int[], double[], String[]> {

//	@Override
//	public int[] getInitalData() {
//		int length = 5;
//		Random rand = new Random();
//		
//		int[] initialData = new int[length];
//		for (int i = 0; i < length; i++) {
//			initialData[i] = rand.nextInt();
//		}
//		return initialData;
//	}

	@Override
	public String[] compute(double[] data, int[] initialData) {
		String[] result = new String[data.length];
		for (int i = 0; i < result.length; i++) {
			String s = "initialData: " + ArrayUtils.toString(initialData) + " data: " + data[i];
			result[i] = s;
		}
		return result;
	}

}
