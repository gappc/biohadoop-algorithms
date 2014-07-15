package at.ac.uibk.dps.biohadoop.algorithms.example.algorithm;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.uibk.dps.biohadoop.algorithm.Algorithm;
import at.ac.uibk.dps.biohadoop.algorithm.AlgorithmException;
import at.ac.uibk.dps.biohadoop.algorithms.example.config.ExampleAlgorithmConfig;
import at.ac.uibk.dps.biohadoop.queue.TaskClient;
import at.ac.uibk.dps.biohadoop.queue.TaskClientImpl;
import at.ac.uibk.dps.biohadoop.queue.TaskFuture;
import at.ac.uibk.dps.biohadoop.solver.ProgressClient;
import at.ac.uibk.dps.biohadoop.solver.ProgressClientImpl;
import at.ac.uibk.dps.biohadoop.solver.SolverId;

public class Example implements Algorithm<ExampleAlgorithmConfig, Integer> {

	public static final String EXAMPLE_QUEUE = "EXAMPLE_QUEUE";

	private static final Logger LOG = LoggerFactory.getLogger(Example.class);

	@Override
	public Integer compute(SolverId solverId, ExampleAlgorithmConfig config)
			throws AlgorithmException {
		// Get client to distribute work
		TaskClient<Integer, String> taskClient = new TaskClientImpl<>(
				EXAMPLE_QUEUE);
		// Get client to submit progress to Hadoop
		ProgressClient progressClient = new ProgressClientImpl(solverId);

		int size = config.getSize();
		List<TaskFuture<String>> tasks = new ArrayList<>();

		int submitted = 0;

		for (int i = 0; i < size; i++) {
			try {
				// Add task to queue (distribute task to workers)
				TaskFuture<String> task = taskClient.add(i);
				tasks.add(task);

				submitted++;
				progressClient.setProgress((float) submitted
						/ (float) (2 * size));
			} catch (InterruptedException e) {
				LOG.error("Got interrupted while submitting Task - data: {}",
						i, e);
			}
		}

		int returned = 0;

		for (TaskFuture<String> task : tasks) {
			try {
				// Wait for task to complete
				String returnValue = task.get();
				LOG.info("Got result: {}", returnValue);

				returned++;
				progressClient.setProgress((float) (submitted + returned)
						/ (float) (2 * size));
			} catch (InterruptedException e) {
				LOG.error("Got interrupted while waiting for TaskFuture {}",
						task, e);
			}
		}

		LOG.info("Submitted {} tasks, got {} results", submitted, returned);

		return returned;
	}

}
