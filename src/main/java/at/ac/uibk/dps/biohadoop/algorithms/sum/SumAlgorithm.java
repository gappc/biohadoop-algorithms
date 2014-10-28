package at.ac.uibk.dps.biohadoop.algorithms.sum;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.uibk.dps.biohadoop.solver.SolverId;
import at.ac.uibk.dps.biohadoop.tasksystem.algorithm.Algorithm;
import at.ac.uibk.dps.biohadoop.tasksystem.algorithm.AlgorithmException;
import at.ac.uibk.dps.biohadoop.tasksystem.queue.SimpleTaskSubmitter;
import at.ac.uibk.dps.biohadoop.tasksystem.queue.TaskException;
import at.ac.uibk.dps.biohadoop.tasksystem.queue.TaskFuture;
import at.ac.uibk.dps.biohadoop.tasksystem.queue.TaskSubmitter;

/**
 * Simple example algorithm, that sums the values of integer array. It uses
 * Biohadoops task system to distribute chunks of the integer array to waiting
 * workers, where they are summed up, after which the result is returned
 * 
 * @author Christian Gapp
 *
 */
public class SumAlgorithm implements Algorithm {

	public static final Logger LOG = LoggerFactory
			.getLogger(SumAlgorithm.class);

	public static final String CHUNKS = "CHUNKS";
	public static final String CHUNK_SIZE = "CHUNK_SIZE";

	@Override
	public void run(SolverId solverId, Map<String, String> properties)
			throws AlgorithmException {
		// Read properties from configuration file
		int chunks = getPropertyAsInt(properties, CHUNKS);
		int chunkSize = getPropertyAsInt(properties, CHUNK_SIZE);

		// Prepare sample data
		int[][] data = buildData(chunks, chunkSize);

		// Get a task submitter for default pipeline. Declare
		// AsyncSumComputation as class that should be run by the workers to
		// compute the results
		TaskSubmitter<int[], Integer> taskSubmitter = new SimpleTaskSubmitter<Object, int[], Integer>(
				AsyncSumComputation.class);

		try {
			List<TaskFuture<Integer>> taskFutures = new ArrayList<>();
			// Submit the sample data to the task system for asynchronous
			// computation. Each submission defines one task
			for (int i = 0; i < chunks; i++) {
				TaskFuture<Integer> future = taskSubmitter.add(data[i]);
				taskFutures.add(future);
			}

			int sum = 0;
			// Wait for all tasks to finish and sum up the result. The get()
			// method blocks until the result for the TaskFuture is available
			for (TaskFuture<Integer> future : taskFutures) {
				sum += future.get();
			}
			LOG.info("The computation result is {}", sum);
		} catch (TaskException e) {
			throw new AlgorithmException("Could not submit data");
		}
	}

	/**
	 * Convenience method to parse the input arguments. Throws an
	 * AlgorithmException if there was an error during parsing
	 * 
	 * @param properties
	 *            contain the configuration for this algorithm, as defined in
	 *            the configuration file
	 * @param key
	 *            for which the value should be retrieved from the map
	 * @return
	 * @throws AlgorithmException
	 *             if there was an exception during the parsing
	 */
	private int getPropertyAsInt(Map<String, String> properties, String key)
			throws AlgorithmException {
		String value = null;
		try {
			value = properties.get(key);
			return Integer.parseInt(value);
		} catch (Exception e) {
			throw new AlgorithmException("Could not convert property " + key
					+ " to long, value was " + value, e);
		}
	}

	/**
	 * Builds <tt>chunks</tt> number of integer arrays, each one of size
	 * <tt>chunkSize</tt>. The arrays are filled with consecutive numbers,
	 * starting from 0. The consecutive numbers continue between the boundaries
	 * of adjacent arrays. For example, array0=[0,1,2], array1=[3,4,5], ...
	 * 
	 * @param chunks
	 *            number of integer arrays
	 * @param chunkSize
	 *            size of each integer array
	 * @return <tt>chunk</tt> number of integer arrays, each one of size
	 *         <tt>chunkSize</tt>. The arrays are filled with consecutive
	 *         numbers, starting from 0. The consecutive numbers continue
	 *         between the boundaries of adjacent arrays
	 */
	private int[][] buildData(int chunks, int chunkSize) {
		int[][] data = new int[chunks][chunkSize];
		for (int i = 0; i < chunks; i++) {
			for (int j = 0; j < chunkSize; j++) {
				data[i][j] = i * chunkSize + j;
			}
		}
		return data;
	}
}
