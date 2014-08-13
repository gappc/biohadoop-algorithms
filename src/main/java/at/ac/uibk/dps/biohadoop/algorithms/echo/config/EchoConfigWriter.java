package at.ac.uibk.dps.biohadoop.algorithms.echo.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.uibk.dps.biohadoop.algorithm.AlgorithmConfiguration;
import at.ac.uibk.dps.biohadoop.algorithms.echo.algorithm.Echo;
import at.ac.uibk.dps.biohadoop.algorithms.echo.remote.StringCommunication;
import at.ac.uibk.dps.biohadoop.communication.CommunicationConfiguration;
import at.ac.uibk.dps.biohadoop.communication.MasterConfiguration;
import at.ac.uibk.dps.biohadoop.communication.WorkerConfiguration;
import at.ac.uibk.dps.biohadoop.communication.annotation.DedicatedSocket;
import at.ac.uibk.dps.biohadoop.communication.master.socket.DefaultSocketConnection;
import at.ac.uibk.dps.biohadoop.communication.worker.DefaultKryoWorker;
import at.ac.uibk.dps.biohadoop.communication.worker.DefaultRestWorker;
import at.ac.uibk.dps.biohadoop.communication.worker.DefaultSocketWorker;
import at.ac.uibk.dps.biohadoop.communication.worker.DefaultWebSocketWorker;
import at.ac.uibk.dps.biohadoop.hadoop.BiohadoopConfiguration;
import at.ac.uibk.dps.biohadoop.handler.distribution.ZooKeeperConfiguration;
import at.ac.uibk.dps.biohadoop.solver.SolverConfiguration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class EchoConfigWriter {

	private static final Logger LOG = LoggerFactory
			.getLogger(EchoConfigWriter.class);

	private static String CONF_OUTPUT_DIR = "/sdb/studium/master-thesis/code/git/biohadoop-algorithms/conf";
	private static String CONF_NAME = "biohadoop-echo";
	private static String LOCAL_OUTPUT_NAME = CONF_OUTPUT_DIR + "/" + CONF_NAME
			+ "-local.json";
	private static String REMOTE_OUTPUT_NAME = CONF_OUTPUT_DIR + "/"
			+ CONF_NAME + ".json";

	private static String LOCAL_DISTRIBUTION_INFO_HOST = "localhost";
	private static int LOCAL_DISTRIBUTION_INFO_PORT = 2181;
	private static String REMOTE_DISTRIBUTION_INFO_HOST = "master";
	private static int REMOTE_DISTRIBUTION_INFO_PORT = 2181;

	private EchoConfigWriter() {
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
		SolverConfiguration solverConfig = buildSolverConfig("ECHO-LOCAL-1",
				local);
		CommunicationConfiguration communicationConfiguration = buildCommunicationConfiguration();
		ZooKeeperConfiguration globalDistributionConfiguration = buildGlobalDistributionConfig(local);

		return new BiohadoopConfiguration(includePaths,
				Arrays.asList(solverConfig), communicationConfiguration,
				globalDistributionConfiguration);
	}

	private static CommunicationConfiguration buildCommunicationConfiguration() {
		List<MasterConfiguration> masters = new ArrayList<>();
		masters.add(new MasterConfiguration(DefaultSocketConnection.class,
				StringCommunication.class, DedicatedSocket.class));
//		masters.add(new MasterConfiguration(DefaultKryoServer.class,
//				StringCommunication.class, DedicatedKryo.class));
//		masters.add(new MasterConfiguration(DefaultLocalMasterEndpoint.class,
//				StringCommunication.class, DedicatedLocal.class));

		List<WorkerConfiguration> workers = new ArrayList<>();
//		workers.add(new WorkerConfiguration(DefaultLocalWorker.class, StringCommunication.class, 2));
//		workers.add(new WorkerConfiguration(DefaultLocalWorker.class, null, 2));
		workers.add(new WorkerConfiguration(DefaultKryoWorker.class, null, 1));
		workers.add(new WorkerConfiguration(DefaultRestWorker.class, null, 1));
		workers.add(new WorkerConfiguration(DefaultSocketWorker.class, null, 1));
		workers.add(new WorkerConfiguration(DefaultWebSocketWorker.class, null,
				1));

//		workers.add(new WorkerConfiguration(DefaultKryoWorker.class, StringCommunication.class, 1));
		workers.add(new WorkerConfiguration(DefaultSocketWorker.class, StringCommunication.class, 1));

		return new CommunicationConfiguration(masters, workers);
	}

	private static SolverConfiguration buildSolverConfig(String name,
			boolean local) {
		AlgorithmConfiguration algorithmConfiguration = buildAlgorithmConfig(local);

		// FileSaveConfiguration fileSaveConfiguration =
		// buildFileSaveConfig(local);
		// FileLoadConfiguration fileLoadConfiguration =
		// buildFileLoadConfig(local);
		// DistributionConfiguration distributionConfiguration =
		// buildDistributionConfig();
		// List<HandlerConfiguration> handlers = new ArrayList<>();
		// handlers.add(fileSaveConfiguration);
		// handlers.add(fileLoadConfiguration);
		// handlers.add(distributionConfiguration);

		return new SolverConfiguration(name, algorithmConfiguration,
				Echo.class, null);
	}

	private static AlgorithmConfiguration buildAlgorithmConfig(boolean local) {
		EchoAlgorithmConfig algorithmConfig = new EchoAlgorithmConfig();
		algorithmConfig.setSize(1000);
		return algorithmConfig;
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
