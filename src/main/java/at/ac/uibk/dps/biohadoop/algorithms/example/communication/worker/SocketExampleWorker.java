package at.ac.uibk.dps.biohadoop.algorithms.example.communication.worker;

import java.util.Date;

import at.ac.uibk.dps.biohadoop.algorithms.example.communication.master.ExampleMaster;
import at.ac.uibk.dps.biohadoop.communication.worker.SocketWorker;
import at.ac.uibk.dps.biohadoop.communication.worker.Worker;

@SocketWorker(master=ExampleMaster.class)
public class SocketExampleWorker implements Worker<Integer, String> {

	@Override
	public void readRegistrationObject(Object data) {
		// Since there is no registration Object, there is nothing to do
	}

	@Override
	public String compute(Integer data) {
		return "Got " + data + " at " + new Date() + " " + Thread.currentThread();
	}

}
