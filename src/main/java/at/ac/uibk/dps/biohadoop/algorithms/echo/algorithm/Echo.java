package at.ac.uibk.dps.biohadoop.algorithms.echo.algorithm;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.uibk.dps.biohadoop.algorithm.Algorithm;
import at.ac.uibk.dps.biohadoop.algorithm.AlgorithmException;
import at.ac.uibk.dps.biohadoop.algorithm.AlgorithmId;
import at.ac.uibk.dps.biohadoop.algorithm.AlgorithmService;
import at.ac.uibk.dps.biohadoop.algorithms.echo.remote.EchoCommunication;
import at.ac.uibk.dps.biohadoop.tasksystem.queue.TaskException;
import at.ac.uibk.dps.biohadoop.tasksystem.queue.TaskFuture;
import at.ac.uibk.dps.biohadoop.tasksystem.queue.TaskSubmitter;

public class Echo implements Algorithm {

	public static final String ECHO_QUEUE = "ECHO_QUEUE";

	private static final Logger LOG = LoggerFactory.getLogger(Echo.class);

	@Override
	public void run(AlgorithmId solverId, Map<String, String> properties)
			throws AlgorithmException {
		// Get client to distribute work
		String initialData = "Worker adds this string to result";
		TaskSubmitter<String, String, String> taskClient = new TaskSubmitter<>(
				EchoCommunication.class, initialData);

		String helloWorld = "Hello World";

		try {
			TaskFuture<String> taskFuture = taskClient.add(helloWorld);

			String result = taskFuture.get();

			LOG.info(result);

		} catch (TaskException e) {
			throw new AlgorithmException("Got interrupted while computing task");
		}

		AlgorithmService.setProgress(solverId, 1.0f);
	}

}
