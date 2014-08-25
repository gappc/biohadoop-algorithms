package at.ac.uibk.dps.biohadoop.algorithms.dedicated.algorithm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.uibk.dps.biohadoop.algorithm.Algorithm;
import at.ac.uibk.dps.biohadoop.algorithm.AlgorithmException;
import at.ac.uibk.dps.biohadoop.algorithms.dedicated.remote.DedicatedCommunication;
import at.ac.uibk.dps.biohadoop.algorithms.dedicated.remote.SharedCommunication;
import at.ac.uibk.dps.biohadoop.queue.DefaultTaskClient;
import at.ac.uibk.dps.biohadoop.queue.TaskClient;
import at.ac.uibk.dps.biohadoop.queue.TaskException;
import at.ac.uibk.dps.biohadoop.queue.TaskFuture;
import at.ac.uibk.dps.biohadoop.solver.ProgressService;
import at.ac.uibk.dps.biohadoop.solver.SolverConfiguration;
import at.ac.uibk.dps.biohadoop.solver.SolverId;

public class Dedicated implements Algorithm {

	public static final String DEDICATED_QUEUE = "DEDICATED_QUEUE";

	private static final Logger LOG = LoggerFactory.getLogger(Dedicated.class);

	@Override
	public void compute(SolverId solverId, SolverConfiguration solverConfiguration)
			throws AlgorithmException {
		TaskClient<String, String> sharedClient = new DefaultTaskClient<>(
				SharedCommunication.class);
		TaskClient<String, String> dedicatedClient = new DefaultTaskClient<>(
				DedicatedCommunication.class, DEDICATED_QUEUE, null);

		String sharedString = "This string is send using shared endpoints";
		String dedicatedString = "This string is send using a dedicated endpoint, which uses internally the queue with name "
				+ DEDICATED_QUEUE;

		try {
			TaskFuture<String> sharedFuture = sharedClient.add(sharedString);
			TaskFuture<String> dedicatedFuture = dedicatedClient.add(dedicatedString);

			String sharedResult = sharedFuture.get();
			String dedicatedResult = dedicatedFuture.get();

			LOG.info("Got result from shared endpoint: {}", sharedResult);
			LOG.info("Got result from dedicated endpoint: {}", dedicatedResult);
		} catch (TaskException e) {
			throw new AlgorithmException("Got interrupted while computing task");
		}

		ProgressService.setProgress(solverId, 1.0f);
	}

}
