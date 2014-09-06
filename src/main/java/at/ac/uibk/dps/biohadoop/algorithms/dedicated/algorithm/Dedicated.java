package at.ac.uibk.dps.biohadoop.algorithms.dedicated.algorithm;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.uibk.dps.biohadoop.algorithm.Algorithm;
import at.ac.uibk.dps.biohadoop.algorithm.AlgorithmException;
import at.ac.uibk.dps.biohadoop.algorithms.dedicated.remote.DedicatedCommunication;
import at.ac.uibk.dps.biohadoop.algorithms.dedicated.remote.SharedCommunication;
import at.ac.uibk.dps.biohadoop.queue.SimpleTaskSubmitter;
import at.ac.uibk.dps.biohadoop.queue.TaskSubmitter;
import at.ac.uibk.dps.biohadoop.queue.TaskException;
import at.ac.uibk.dps.biohadoop.queue.TaskFuture;
import at.ac.uibk.dps.biohadoop.solver.ProgressService;
import at.ac.uibk.dps.biohadoop.solver.SolverId;

public class Dedicated implements Algorithm {

	public static final String DEDICATED_SETTING = "EXAMPLE_DEDICATED_SETTING";

	private static final Logger LOG = LoggerFactory.getLogger(Dedicated.class);

	@Override
	public void compute(SolverId solverId, Map<String, String> properties)
			throws AlgorithmException {
		TaskSubmitter<String, String> sharedClient = new SimpleTaskSubmitter<>(
				SharedCommunication.class);
		TaskSubmitter<String, String> dedicatedClient = new SimpleTaskSubmitter<>(
				DedicatedCommunication.class, DEDICATED_SETTING, null);

		String sharedString = "This string is send using the default task setting";
		String dedicatedString = "This string is send using a dedicated task setting, which uses internally the queue with name "
				+ DEDICATED_SETTING;

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
