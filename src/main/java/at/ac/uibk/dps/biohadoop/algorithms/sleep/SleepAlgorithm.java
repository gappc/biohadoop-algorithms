package at.ac.uibk.dps.biohadoop.algorithms.sleep;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.uibk.dps.biohadoop.solver.ProgressService;
import at.ac.uibk.dps.biohadoop.solver.SolverId;
import at.ac.uibk.dps.biohadoop.tasksystem.algorithm.Algorithm;
import at.ac.uibk.dps.biohadoop.tasksystem.algorithm.AlgorithmException;
import at.ac.uibk.dps.biohadoop.tasksystem.queue.SimpleTaskSubmitter;
import at.ac.uibk.dps.biohadoop.tasksystem.queue.TaskException;
import at.ac.uibk.dps.biohadoop.tasksystem.queue.TaskFuture;
import at.ac.uibk.dps.biohadoop.tasksystem.queue.TaskSubmitter;

public class SleepAlgorithm implements Algorithm {

	public static final String ITERATIONS = "ITERATIONS";
	public static final String SLEEP_MILLISECONDS = "SLEEP_MILLISECONDS";
	public static final String WAIT_BEFORE_SUBMIT_MILLISECONDS = "WAIT_BEFORE_SUBMIT_MILLISECONDS";

	private static final Logger LOG = LoggerFactory
			.getLogger(SleepAlgorithm.class);

	@Override
	public void run(SolverId solverId, Map<String, String> properties)
			throws AlgorithmException {
		// Read configuration data
		long iterations = getPropertyAsLong(properties, ITERATIONS);
		long sleepMilliseconds = getPropertyAsLong(properties,
				SLEEP_MILLISECONDS);
		long waitBeforeSubmitMilliseconds = getPropertyAsLong(properties,
				WAIT_BEFORE_SUBMIT_MILLISECONDS);

		// Create TaskSubmitter to add tasks to the task system. The
		// constructor sets the initial data to the value of "sleepMilliseconds"
		TaskSubmitter<Object, Object> taskSubmitter = new SimpleTaskSubmitter<>(
				SleepRemoteExecutable.class, sleepMilliseconds);

		// Create task objects. As we are not sending anything useful, we just
		// create empty Objects
		Object[] emptyData = new Object[(int) iterations];

		// We want to measure the speedup. Because we don't can be sure when
		// the first worker endpoint is available (may take some time), we
		// wait a short instance, before we submit the first task. Otherwise we
		// would mistakenly measure also the time until the worker endpoints
		// come up.
		try {
			LOG.info("Waiting for {}ms before submitting the first task",
					waitBeforeSubmitMilliseconds);
			Thread.sleep(waitBeforeSubmitMilliseconds);
			LOG.info("Finished waiting, now submitting tasks to task system");
		} catch (InterruptedException e) {
			throw new AlgorithmException(
					"Got interrupted while waiting for first task submission",
					e);
		}

		long start = System.currentTimeMillis();
		try {
			List<TaskFuture<Object>> futures = taskSubmitter.addAll(emptyData);
			for (int i = 0; i < futures.size(); i++) {
				TaskFuture<Object> future = futures.get(i);
				future.get();
				ProgressService.setProgress(solverId, (float) i / iterations);
			}
		} catch (TaskException e) {
			throw new AlgorithmException("Error while running tasks", e);
		}
		long realDuration = System.currentTimeMillis() - start;
		long theoreticSequentialDuration = iterations * sleepMilliseconds;

		LOG.info(
				"Completed all tasks. The optimal sequential version should take {}ms to complete (ITERATIONS={}, SLEEP_MILLISECONDS={}). This version took {}ms. Speedup is {}",
				theoreticSequentialDuration, ITERATIONS, SLEEP_MILLISECONDS,
				realDuration, (double) theoreticSequentialDuration
						/ realDuration);
	}

	private long getPropertyAsLong(Map<String, String> properties, String key)
			throws AlgorithmException {
		String value = null;
		try {
			value = properties.get(key);
			return Long.parseLong(value);
		} catch (Exception e) {
			throw new AlgorithmException("Could not convert property " + key
					+ " to long, value was " + value, e);
		}
	}
}
