package at.ac.uibk.dps.biohadoop.algorithms.example.communication.worker;

import java.util.Date;

import at.ac.uibk.dps.biohadoop.algorithms.example.communication.master.ExampleMaster;
import at.ac.uibk.dps.biohadoop.communication.worker.KryoWorker;
import at.ac.uibk.dps.biohadoop.communication.worker.LocalWorker;
import at.ac.uibk.dps.biohadoop.communication.worker.RestWorker;
import at.ac.uibk.dps.biohadoop.communication.worker.SocketWorker;
import at.ac.uibk.dps.biohadoop.communication.worker.Worker;
import at.ac.uibk.dps.biohadoop.communication.worker.WebSocketWorker;

@KryoWorker(master=ExampleMaster.class)
@LocalWorker(master=ExampleMaster.class)
@RestWorker(master=ExampleMaster.class, receive=Integer.class)
@SocketWorker(master=ExampleMaster.class)
@WebSocketWorker(master=ExampleMaster.class)
public class ExampleWorker implements Worker<Integer, String> {

	@Override
	public void readRegistrationObject(Object data) {
		// Since there is no registration Object, there is nothing to do
	}

	@Override
	public String compute(Integer data) {
		return "Got " + data + " at " + new Date() + " " + Thread.currentThread();
	}

}
