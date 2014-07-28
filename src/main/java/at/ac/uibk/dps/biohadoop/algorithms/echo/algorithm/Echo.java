package at.ac.uibk.dps.biohadoop.algorithms.echo.algorithm;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.uibk.dps.biohadoop.algorithm.Algorithm;
import at.ac.uibk.dps.biohadoop.algorithm.AlgorithmException;
import at.ac.uibk.dps.biohadoop.algorithms.echo.config.EchoAlgorithmConfig;
import at.ac.uibk.dps.biohadoop.queue.TaskClient;
import at.ac.uibk.dps.biohadoop.queue.TaskClientImpl;
import at.ac.uibk.dps.biohadoop.queue.TaskFuture;
import at.ac.uibk.dps.biohadoop.solver.ProgressService;
import at.ac.uibk.dps.biohadoop.solver.SolverId;

public class Echo implements Algorithm<EchoAlgorithmConfig, Integer> {

	public static final String ECHO_QUEUE = "ECHO_QUEUE";

	private static final Logger LOG = LoggerFactory.getLogger(Echo.class);

	@Override
	public Integer compute(SolverId solverId, EchoAlgorithmConfig config)
			throws AlgorithmException {
		// Get client to distribute work
		TaskClient<Integer, String> taskClient = new TaskClientImpl<>(
				ECHO_QUEUE);

		int size = config.getSize();
		List<TaskFuture<String>> tasks = new ArrayList<>();

		ProgressService.setProgress(solverId, 0.1f);

		int submitted = 0;
		
		for (int i = 0; i < size; i++) {
			try {
				// Add task to queue (distribute task to workers)
				TaskFuture<String> task = taskClient.add(i);
				tasks.add(task);

				submitted++;
			} catch (InterruptedException e) {
				LOG.error("Got interrupted while submitting Task - data: {}",
						i, e);
			}
		}
		
		ProgressService.setProgress(solverId, 0.5f);

		int returned = 0;

		for (TaskFuture<String> task : tasks) {
			try {
				// Wait for task to complete
				String returnValue = task.get();
				LOG.info("Got result: {}", returnValue);

				returned++;
			} catch (InterruptedException e) {
				LOG.error("Got interrupted while waiting for TaskFuture {}",
						task, e);
			}
		}

		LOG.info("Submitted {} tasks, got {} results", submitted, returned);

		ProgressService.setProgress(solverId, 1.0f);

		return returned;
	}

}
