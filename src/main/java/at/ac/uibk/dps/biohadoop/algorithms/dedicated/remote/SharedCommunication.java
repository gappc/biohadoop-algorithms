package at.ac.uibk.dps.biohadoop.algorithms.dedicated.remote;

import at.ac.uibk.dps.biohadoop.communication.RemoteExecutable;


public class SharedCommunication implements
		RemoteExecutable<String, String, String> {

//	@Override
//	public String getInitalData() {
//		// No initial object
//		return null;
//	}
	
	@Override
	public String compute(String data, String initialData) {
		return data + " (passed shared worker computation)";
	}

}
