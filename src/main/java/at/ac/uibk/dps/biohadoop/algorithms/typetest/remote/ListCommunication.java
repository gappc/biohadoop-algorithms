package at.ac.uibk.dps.biohadoop.algorithms.typetest.remote;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;

import at.ac.uibk.dps.biohadoop.communication.RemoteExecutable;

public class ListCommunication implements
		RemoteExecutable<List<Integer>, List<Double>, List<String>> {

//	@Override
//	public List<Integer> getInitalData() {
//		Random rand = new Random();
//		List<Integer> initialData = new ArrayList<>();
//		for (int i = 0; i < 5; i++) {
//			initialData.add(rand.nextInt());
//		}
//		return initialData;
//	}

	@Override
	public List<String> compute(List<Double> data, List<Integer> initialData) {
		List<String> result = new ArrayList<String>();
		for (Double value : data) {
			String s = "initialData: " + ArrayUtils.toString(initialData) + " data: " + value;
			result.add(s);
		}
		return result;
	}
	
}
