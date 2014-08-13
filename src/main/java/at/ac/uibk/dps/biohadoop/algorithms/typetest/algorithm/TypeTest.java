package at.ac.uibk.dps.biohadoop.algorithms.typetest.algorithm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.uibk.dps.biohadoop.algorithm.Algorithm;
import at.ac.uibk.dps.biohadoop.algorithm.AlgorithmException;
import at.ac.uibk.dps.biohadoop.algorithms.typetest.config.TypeTestAlgorithmConfig;
import at.ac.uibk.dps.biohadoop.algorithms.typetest.remote.ArrayCommunication;
import at.ac.uibk.dps.biohadoop.algorithms.typetest.remote.ComplexObjectCommunication;
import at.ac.uibk.dps.biohadoop.algorithms.typetest.remote.ListCommunication;
import at.ac.uibk.dps.biohadoop.algorithms.typetest.remote.ObjectCommunication;
import at.ac.uibk.dps.biohadoop.algorithms.typetest.remote.StringCommunication;
import at.ac.uibk.dps.biohadoop.algorithms.typetest.remote.complexobject.ComplexObject;
import at.ac.uibk.dps.biohadoop.queue.DefaultTaskClient;
import at.ac.uibk.dps.biohadoop.queue.TaskClient;
import at.ac.uibk.dps.biohadoop.queue.TaskFuture;
import at.ac.uibk.dps.biohadoop.solver.ProgressService;
import at.ac.uibk.dps.biohadoop.solver.SolverId;

public class TypeTest implements Algorithm<TypeTestAlgorithmConfig> {

	private static final Logger LOG = LoggerFactory.getLogger(TypeTest.class);

	@Override
	public void compute(SolverId solverId, TypeTestAlgorithmConfig config)
			throws AlgorithmException {
		// Get client to distribute work
		try {
			testArrays();
			testComplexObject();
			testList();
			testObject();
			testString();
			ProgressService.setProgress(solverId, 1.0f);
		} catch (InterruptedException e) {
			throw new AlgorithmException("Error while running", e);
		}
	}

	private void testArrays() throws InterruptedException {
		TaskClient<double[], String[]> client = new DefaultTaskClient<>(
				ArrayCommunication.class);
		Random rand = new Random();
		double[] data = new double[] { rand.nextDouble(), rand.nextDouble(),
				rand.nextDouble() };
		TaskFuture<String[]> future = client.add(data);
		LOG.info("Array send: {}", ArrayUtils.toString(data));

		String[] result = future.get();
		LOG.info("Array received: {}", ArrayUtils.toString(result));
	}

	private void testComplexObject() throws InterruptedException {
		TaskClient<ComplexObject, ComplexObject> client = new DefaultTaskClient<>(
				ComplexObjectCommunication.class);
		ComplexObject data = ComplexObject.buildRandom();
		TaskFuture<ComplexObject> future = client.add(data);
		LOG.info("ComplexObject send: {}", data);

		ComplexObject result = future.get();
		LOG.info("ComplexObject received: {}", result);
	}

	private void testList() throws InterruptedException {
		TaskClient<List<Double>, List<String>> client = new DefaultTaskClient<>(
				ListCommunication.class);
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

	private void testObject() throws InterruptedException {
		TaskClient<Date, String> client = new DefaultTaskClient<>(
				ObjectCommunication.class);
		Date data = new Date();
		TaskFuture<String> future = client.add(data);
		LOG.info("Object send: {}", data);

		String result = future.get();
		LOG.info("Object received: {}", result);
	}

	private void testString() throws InterruptedException {
		TaskClient<String, String> client = new DefaultTaskClient<>(
				StringCommunication.class);
		Random rand = new Random();
		String data = "Custom data_" + rand.nextInt();
		TaskFuture<String> future = client.add(data);
		LOG.info("String send: {}", data);

		String result = future.get();
		LOG.info("String received: {}", result);
	}

}
