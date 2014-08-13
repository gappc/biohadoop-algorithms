package at.ac.uibk.dps.biohadoop.algorithms.typetest.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.uibk.dps.biohadoop.algorithm.AlgorithmConfiguration;
import at.ac.uibk.dps.biohadoop.algorithms.typetest.algorithm.TypeTest;
import at.ac.uibk.dps.biohadoop.algorithms.typetest.remote.kryo.KryoObjects;
import at.ac.uibk.dps.biohadoop.communication.CommunicationConfiguration;
import at.ac.uibk.dps.biohadoop.communication.MasterConfiguration;
import at.ac.uibk.dps.biohadoop.communication.WorkerConfiguration;
import at.ac.uibk.dps.biohadoop.communication.worker.DefaultKryoWorker;
import at.ac.uibk.dps.biohadoop.communication.worker.DefaultRestWorker;
import at.ac.uibk.dps.biohadoop.communication.worker.DefaultSocketWorker;
import at.ac.uibk.dps.biohadoop.communication.worker.DefaultWebSocketWorker;
import at.ac.uibk.dps.biohadoop.hadoop.BiohadoopConfiguration;
import at.ac.uibk.dps.biohadoop.handler.distribution.ZooKeeperConfiguration;
import at.ac.uibk.dps.biohadoop.solver.SolverConfiguration;
import at.ac.uibk.dps.biohadoop.utils.KryoRegistrator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class TypeTestConfigWriter {

	private static final Logger LOG = LoggerFactory
			.getLogger(TypeTestConfigWriter.class);

	private static String CONF_OUTPUT_DIR = "/sdb/studium/master-thesis/code/git/biohadoop-algorithms/conf";
	private static String CONF_NAME = "biohadoop-typetest";
	private static String LOCAL_OUTPUT_NAME = CONF_OUTPUT_DIR + "/" + CONF_NAME
			+ "-local.json";
	private static String REMOTE_OUTPUT_NAME = CONF_OUTPUT_DIR + "/"
			+ CONF_NAME + ".json";

	private static String LOCAL_DISTRIBUTION_INFO_HOST = "localhost";
	private static int LOCAL_DISTRIBUTION_INFO_PORT = 2181;
	private static String REMOTE_DISTRIBUTION_INFO_HOST = "master";
	private static int REMOTE_DISTRIBUTION_INFO_PORT = 2181;

	private TypeTestConfigWriter() {
	}

	public static void main(String[] args) throws IOException,
			ClassNotFoundException, InstantiationException,
			IllegalAccessException {

		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);

		BiohadoopConfiguration biohadoopConfig = buildBiohadoopConfiguration(true);
		mapper.writeValue(new File(LOCAL_OUTPUT_NAME), biohadoopConfig);

		biohadoopConfig = buildBiohadoopConfiguration(false);
		mapper.writeValue(new File(REMOTE_OUTPUT_NAME), biohadoopConfig);

		readAlgorithmConfig();
	}

	private static BiohadoopConfiguration buildBiohadoopConfiguration(
			boolean local) {
		List<String> includePaths = Arrays.asList("/biohadoop/lib/",
				"/biohadoop/conf/");
		SolverConfiguration solverConfig = buildSolverConfig("TYPETEST-LOCAL-1",
				local);
		CommunicationConfiguration communicationConfiguration = buildCommunicationConfiguration();
		ZooKeeperConfiguration globalDistributionConfiguration = buildGlobalDistributionConfig(local);
		Map<String, String> properties = new HashMap<>();
		properties.put(KryoRegistrator.KRYO_REGISTRATOR, KryoObjects.class.getCanonicalName());
		
		return new BiohadoopConfiguration(includePaths,
				Arrays.asList(solverConfig), communicationConfiguration,
				globalDistributionConfiguration, properties);
	}

	private static CommunicationConfiguration buildCommunicationConfiguration() {
		List<MasterConfiguration> masters = new ArrayList<>();

		List<WorkerConfiguration> workers = new ArrayList<>();
		workers.add(new WorkerConfiguration(DefaultKryoWorker.class, null, 1));
		workers.add(new WorkerConfiguration(DefaultRestWorker.class, null, 1));
		workers.add(new WorkerConfiguration(DefaultSocketWorker.class, null, 1));
		workers.add(new WorkerConfiguration(DefaultWebSocketWorker.class, null,
				1));

		return new CommunicationConfiguration(masters, workers);
	}

	private static SolverConfiguration buildSolverConfig(String name,
			boolean local) {
		AlgorithmConfiguration algorithmConfiguration = buildAlgorithmConfig(local);
		return new SolverConfiguration(name, algorithmConfiguration,
				TypeTest.class, null);
	}

	private static AlgorithmConfiguration buildAlgorithmConfig(boolean local) {
		return new TypeTestAlgorithmConfig();
	}

	private static ZooKeeperConfiguration buildGlobalDistributionConfig(
			boolean local) {
		if (local) {
			return new ZooKeeperConfiguration(LOCAL_DISTRIBUTION_INFO_HOST,
					LOCAL_DISTRIBUTION_INFO_PORT);
		} else {
			return new ZooKeeperConfiguration(REMOTE_DISTRIBUTION_INFO_HOST,
					REMOTE_DISTRIBUTION_INFO_PORT);
		}
	}

	private static void readAlgorithmConfig() throws IOException,
			ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		BiohadoopConfiguration config = null;

		ObjectMapper mapper = new ObjectMapper();
		config = mapper.readValue(new File(LOCAL_OUTPUT_NAME),
				BiohadoopConfiguration.class);
		LOG.info(config.toString());

		config = mapper.readValue(new File(REMOTE_OUTPUT_NAME),
				BiohadoopConfiguration.class);
		LOG.info(config.toString());
	}
}
