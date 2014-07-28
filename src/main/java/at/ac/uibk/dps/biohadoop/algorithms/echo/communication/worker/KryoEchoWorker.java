package at.ac.uibk.dps.biohadoop.algorithms.echo.communication.worker;

import java.util.Date;

import at.ac.uibk.dps.biohadoop.algorithms.echo.communication.master.EchoMaster;
import at.ac.uibk.dps.biohadoop.communication.worker.KryoWorker;
import at.ac.uibk.dps.biohadoop.communication.worker.Worker;

@KryoWorker(master=EchoMaster.class)
public class KryoEchoWorker implements Worker<Integer, String> {

	@Override
	public void readRegistrationObject(Object data) {
		// Since there is no registration Object, there is nothing to do
	}

	@Override
	public String compute(Integer data) {
		return "Got " + data + " at " + new Date() + " " + Thread.currentThread();
	}

}
