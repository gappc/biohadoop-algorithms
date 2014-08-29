package at.ac.uibk.dps.biohadoop.algorithms.typetest.algorithm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.uibk.dps.biohadoop.algorithm.Algorithm;
import at.ac.uibk.dps.biohadoop.algorithm.AlgorithmException;
import at.ac.uibk.dps.biohadoop.algorithms.typetest.remote.ArrayCommunication;
import at.ac.uibk.dps.biohadoop.algorithms.typetest.remote.ComplexObjectCommunication;
import at.ac.uibk.dps.biohadoop.algorithms.typetest.remote.ListCommunication;
import at.ac.uibk.dps.biohadoop.algorithms.typetest.remote.ObjectCommunication;
import at.ac.uibk.dps.biohadoop.algorithms.typetest.remote.StringCommunication;
import at.ac.uibk.dps.biohadoop.algorithms.typetest.remote.complexobject.ComplexObject;
import at.ac.uibk.dps.biohadoop.queue.DefaultTaskClient;
import at.ac.uibk.dps.biohadoop.queue.TaskClient;
import at.ac.uibk.dps.biohadoop.queue.TaskException;
import at.ac.uibk.dps.biohadoop.queue.TaskFuture;
import at.ac.uibk.dps.biohadoop.solver.ProgressService;
import at.ac.uibk.dps.biohadoop.solver.SolverId;

public class TypeTest implements Algorithm {

	private static final Logger LOG = LoggerFactory.getLogger(TypeTest.class);

	@Override
	public void compute(SolverId solverId, Map<String, String> properties)
			throws AlgorithmException {
		// Get client to distribute work
		try {
			testArrays();
			testComplexObject();
			testList();
			testObject();
			testString();
			ProgressService.setProgress(solverId, 1.0f);
		} catch (TaskException e) {
			throw new AlgorithmException("Error while running", e);
		}
	}

	private void testArrays() throws TaskException {
		int[] initialData = getArrayInitalData();
		TaskClient<double[], String[]> client = new DefaultTaskClient<>(
				ArrayCommunication.class, initialData);
		Random rand = new Random();
		double[] data = new double[] { rand.nextDouble(), rand.nextDouble(),
				rand.nextDouble() };
		TaskFuture<String[]> future = client.add(data);
		LOG.info("Array send: {}", ArrayUtils.toString(data));

		String[] result = future.get();
		LOG.info("Array received: {}", ArrayUtils.toString(result));
	}

	public int[] getArrayInitalData() {
		int length = 5;
		Random rand = new Random();

		int[] initialData = new int[length];
		for (int i = 0; i < length; i++) {
			initialData[i] = rand.nextInt();
		}
		return initialData;
	}

	private void testComplexObject() throws TaskException {
		ComplexObject initialData = getComplexObjectInitalData();
		TaskClient<ComplexObject, ComplexObject> client = new DefaultTaskClient<>(
				ComplexObjectCommunication.class, initialData);
		ComplexObject data = ComplexObject.buildRandom();
		TaskFuture<ComplexObject> future = client.add(data);
		LOG.info("ComplexObject send: {}", data);

		ComplexObject result = future.get();
		LOG.info("ComplexObject received: {}", result);
	}

	public ComplexObject getComplexObjectInitalData() {
		return ComplexObject.buildRandom();
	}

	private void testList() throws TaskException {
		List<Integer> initialData = getListInitalData();
		TaskClient<List<Double>, List<String>> client = new DefaultTaskClient<>(
				ListCommunication.class, initialData);
		Random rand = new Random();
		List<Double> data = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			data.add(rand.nextDouble());
		}
		TaskFuture<List<String>> future = client.add(data);
		LOG.info("List send: {}", ArrayUtils.toString(data));

		List<String> result = future.get();
		LOG.info("List received: {}", ArrayUtils.toString(result));
	}

	public List<Integer> getListInitalData() {
		Random rand = new Random();
		List<Integer> initialData = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			initialData.add(rand.nextInt());
		}
		return initialData;
	}

	private void testObject() throws TaskException {
		UUID initialData = getObjectInitalData();
		TaskClient<Date, String> client = new DefaultTaskClient<>(
				ObjectCommunication.class, initialData);
		Date data = new Date();
		TaskFuture<String> future = client.add(data);
		LOG.info("Object send: {}", data);

		String result = future.get();
		LOG.info("Object received: {}", result);
	}

	public UUID getObjectInitalData() {
		return UUID.randomUUID();
	}

	private void testString() throws TaskException {
		String initialData = getStringInitalData();
		TaskClient<String, String> client = new DefaultTaskClient<>(
				StringCommunication.class, initialData);
		Random rand = new Random();
		String data = "Custom data_" + rand.nextInt();
		TaskFuture<String> future = client.add(data);
		LOG.info("String send: {}", data);

		String result = future.get();
		LOG.info("String received: {}", result);
	}

	public String getStringInitalData() {
		return "Worker adds this string to result";
	}

}
