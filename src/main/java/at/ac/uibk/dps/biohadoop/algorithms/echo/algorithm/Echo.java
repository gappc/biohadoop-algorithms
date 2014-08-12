package at.ac.uibk.dps.biohadoop.algorithms.echo.algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.uibk.dps.biohadoop.algorithm.Algorithm;
import at.ac.uibk.dps.biohadoop.algorithm.AlgorithmException;
import at.ac.uibk.dps.biohadoop.algorithms.echo.config.EchoAlgorithmConfig;
import at.ac.uibk.dps.biohadoop.algorithms.echo.remote.ArrayCommunication;
import at.ac.uibk.dps.biohadoop.algorithms.echo.remote.DateCommunication;
import at.ac.uibk.dps.biohadoop.algorithms.echo.remote.EchoCommunication;
import at.ac.uibk.dps.biohadoop.algorithms.echo.remote.StringCommunication;
import at.ac.uibk.dps.biohadoop.queue.DefaultTaskClient;
import at.ac.uibk.dps.biohadoop.queue.TaskClient;
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
		TaskClient<Integer, String> taskClient = new DefaultTaskClient<>(EchoCommunication.class);
		TaskClient<Long, String> dateClient = new DefaultTaskClient<>(DateCommunication.class);
		TaskClient<double[], String[]> arrayClient = new DefaultTaskClient<>(ArrayCommunication.class);
//		TaskClient<String, String> stringClient = new DefaultTaskClient<>(StringCommunication.class);
		TaskClient<String, String> stringClient = new DefaultTaskClient<>(StringCommunication.class, ECHO_QUEUE);

		int size = config.getSize();
		List<TaskFuture<String>> tasks = new ArrayList<>();
		List<TaskFuture<String>> dateTasks = new ArrayList<>();
		List<TaskFuture<String[]>> arrayTasks = new ArrayList<>();
		List<TaskFuture<String>> stringTasks = new ArrayList<>();
		
		ProgressService.setProgress(solverId, 0.1f);

		int submitted = 0;
		
		for (int i = 0; i < size; i++) {
			try {
				// Add task to queue (distribute task to workers)
				TaskFuture<String> task = taskClient.add(i);
				TaskFuture<String> dateTask = dateClient.add(System.currentTimeMillis());
				TaskFuture<String[]> arrayTask = arrayClient.add(new double[] {new Random().nextDouble(), new Random().nextDouble(), new Random().nextDouble()});
				TaskFuture<String> stringTask = stringClient.add(UUID.randomUUID().toString());
				tasks.add(task);
				dateTasks.add(dateTask);
				arrayTasks.add(arrayTask);
				stringTasks.add(stringTask);
				

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

		for (TaskFuture<String> task : dateTasks) {
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
		
		for (TaskFuture<String[]> task : arrayTasks) {
			try {
				// Wait for task to complete
				String[] returnValue = task.get();
				LOG.info("Got result: {} | {}", returnValue[0], returnValue[1]);

				returned++;
			} catch (InterruptedException e) {
				LOG.error("Got interrupted while waiting for TaskFuture {}",
						task, e);
			}
		}
		
		for (TaskFuture<String> task : stringTasks) {
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
