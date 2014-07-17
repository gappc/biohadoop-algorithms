package at.ac.uibk.dps.biohadoop.algorithms.example.communication.worker;

import java.util.Date;

import at.ac.uibk.dps.biohadoop.algorithms.example.communication.master.ExampleMaster;
import at.ac.uibk.dps.biohadoop.communication.worker.KryoWorkerAnnotation;
import at.ac.uibk.dps.biohadoop.communication.worker.LocalWorkerAnnotation;
import at.ac.uibk.dps.biohadoop.communication.worker.RestWorkerAnnotation;
import at.ac.uibk.dps.biohadoop.communication.worker.SocketWorkerAnnotation;
import at.ac.uibk.dps.biohadoop.communication.worker.SuperWorker;
import at.ac.uibk.dps.biohadoop.communication.worker.WebSocketWorkerAnnotation;

@KryoWorkerAnnotation(master=ExampleMaster.class)
@LocalWorkerAnnotation(master=ExampleMaster.class)
@RestWorkerAnnotation(master=ExampleMaster.class, receive=Integer.class)
@SocketWorkerAnnotation(master=ExampleMaster.class)
@WebSocketWorkerAnnotation(master=ExampleMaster.class)
public class ExampleWorker implements SuperWorker<Integer, String> {

	@Override
	public void readRegistrationObject(Object data) {
		// Since there is no registration Object, there is nothing to do
	}

	@Override
	public String compute(Integer data) {
		return "Got " + data + " at " + new Date() + " " + Thread.currentThread();
	}

}
