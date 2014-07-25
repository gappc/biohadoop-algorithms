package at.ac.uibk.dps.biohadoop.algorithms.nsgaii.config;

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
import at.ac.uibk.dps.biohadoop.algorithms.nsgaii.algorithm.NsgaII;
import at.ac.uibk.dps.biohadoop.algorithms.nsgaii.communication.master.NsgaIIMaster;
import at.ac.uibk.dps.biohadoop.algorithms.nsgaii.communication.worker.KryoNsgaIIWorker;
import at.ac.uibk.dps.biohadoop.algorithms.nsgaii.communication.worker.RestNsgaIIWorker;
import at.ac.uibk.dps.biohadoop.algorithms.nsgaii.communication.worker.SocketNsgaIIWorker;
import at.ac.uibk.dps.biohadoop.algorithms.nsgaii.communication.worker.WebSocketNsgaIIWorker;
import at.ac.uibk.dps.biohadoop.algorithms.nsgaii.distribution.NsgaIIBestResultGetter;
import at.ac.uibk.dps.biohadoop.algorithms.nsgaii.distribution.NsgaIISimpleMerger;
import at.ac.uibk.dps.biohadoop.communication.CommunicationConfiguration;
import at.ac.uibk.dps.biohadoop.communication.master.Master;
import at.ac.uibk.dps.biohadoop.communication.worker.Worker;
import at.ac.uibk.dps.biohadoop.hadoop.BiohadoopConfiguration;
import at.ac.uibk.dps.biohadoop.handler.HandlerConfiguration;
import at.ac.uibk.dps.biohadoop.handler.distribution.DistributionConfiguration;
import at.ac.uibk.dps.biohadoop.handler.distribution.DistributionHandler;
import at.ac.uibk.dps.biohadoop.handler.distribution.ZooKeeperConfiguration;
import at.ac.uibk.dps.biohadoop.handler.persistence.file.FileLoadConfiguration;
import at.ac.uibk.dps.biohadoop.handler.persistence.file.FileLoadHandler;
import at.ac.uibk.dps.biohadoop.handler.persistence.file.FileSaveConfiguration;
import at.ac.uibk.dps.biohadoop.handler.persistence.file.FileSaveHandler;
import at.ac.uibk.dps.biohadoop.solver.SolverConfiguration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class NsgaIIConfigWriter {

	private static final Logger LOG = LoggerFactory
			.getLogger(NsgaIIConfigWriter.class);

	private static String CONF_OUTPUT_DIR = "/sdb/studium/master-thesis/code/git/biohadoop-algorithms/conf";
	private static String CONF_NAME = "biohadoop-nsgaii";
	private static String LOCAL_OUTPUT_NAME = CONF_OUTPUT_DIR + "/" + CONF_NAME
			+ "-local.json";
	private static String REMOTE_OUTPUT_NAME = CONF_OUTPUT_DIR + "/"
			+ CONF_NAME + ".json";

	private static String LOCAL_PERSISTENCE_SAVE_PATH = "/tmp/biohadoop/nsgaii";
	private static String REMOTE_PERSISTENCE_SAVE_PATH = "/biohadoop/persistence/nsgaii";
	private static String LOCAL_PERSISTENCE_LOAD_PATH = "/tmp/biohadoop/nsgaii/NSGAII-LOCAL-1/1263479909";
	private static String REMOTE_PERSISTENCE_LOAD_PATH = "/biohadoop/persistence/nsgaii";

	private static String LOCAL_DISTRIBUTION_INFO_HOST = "localhost";
	private static int LOCAL_DISTRIBUTION_INFO_PORT = 2181;
	private static String REMOTE_DISTRIBUTION_INFO_HOST = "master";
	private static int REMOTE_DISTRIBUTION_INFO_PORT = 2181;

	private NsgaIIConfigWriter() {
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
		String version = "0.1";
		List<String> includePaths = Arrays.asList("/biohadoop/lib/",
				"/biohadoop/conf/");
		SolverConfiguration solverConfig = buildSolverConfig("MOEAD-LOCAL-1",
				local);
		CommunicationConfiguration communicationConfiguration = buildCommunicationConfiguration();
		ZooKeeperConfiguration globalDistributionConfiguration = buildGlobalDistributionConfig(local);

		return new BiohadoopConfiguration(version, includePaths, Arrays.asList(
				solverConfig, solverConfig, solverConfig, solverConfig),
				communicationConfiguration, globalDistributionConfiguration);
	}

	private static CommunicationConfiguration buildCommunicationConfiguration() {
		List<Class<? extends Master>> masters = new ArrayList<>();
		masters.add(NsgaIIMaster.class);

		Map<Class<? extends Worker<?, ?>>, Integer> workers = new HashMap<>();
		workers.put(KryoNsgaIIWorker.class, 1);
		workers.put(RestNsgaIIWorker.class, 1);
		workers.put(SocketNsgaIIWorker.class, 3);
		workers.put(WebSocketNsgaIIWorker.class, 1);

		return new CommunicationConfiguration(masters, workers);
	}

	private static SolverConfiguration buildSolverConfig(String name,
			boolean local) {
		AlgorithmConfiguration algorithmConfiguration = buildAlgorithmConfig(local);

		FileSaveConfiguration fileSaveConfiguration = buildFileSaveConfig(local);
		FileLoadConfiguration fileLoadConfiguration = buildFileLoadConfig(local);
		DistributionConfiguration distributionConfiguration = buildDistributionConfig();
		List<HandlerConfiguration> handlers = new ArrayList<>();
		handlers.add(fileSaveConfiguration);
		handlers.add(fileLoadConfiguration);
		handlers.add(distributionConfiguration);

		return new SolverConfiguration(name, algorithmConfiguration,
				NsgaII.class, handlers);
	}

	private static AlgorithmConfiguration buildAlgorithmConfig(boolean local) {
		NsgaIIAlgorithmConfig nsgaIIConfig = new NsgaIIAlgorithmConfig();
		if (local) {
			nsgaIIConfig.setOutputFile("/tmp/nsgaii-sol.txt");
		} else {
			nsgaIIConfig.setOutputFile("/biohadoop/data/nsgaii-sol.txt");
		}

		nsgaIIConfig.setMaxIterations(500);
		nsgaIIConfig.setPopulationSize(300);
		nsgaIIConfig.setGenomeSize(100);

		return nsgaIIConfig;
	}

	private static FileSaveConfiguration buildFileSaveConfig(boolean local) {
		String savePath = null;
		if (local) {
			savePath = LOCAL_PERSISTENCE_SAVE_PATH;
		} else {
			savePath = REMOTE_PERSISTENCE_SAVE_PATH;
		}
		return new FileSaveConfiguration(FileSaveHandler.class, savePath, 100);
	}

	private static FileLoadConfiguration buildFileLoadConfig(boolean local) {
		String loadPath = null;
		if (local) {
			loadPath = LOCAL_PERSISTENCE_LOAD_PATH;
		} else {
			loadPath = REMOTE_PERSISTENCE_LOAD_PATH;
		}
		return new FileLoadConfiguration(FileLoadHandler.class, loadPath, false);
	}

	private static DistributionConfiguration buildDistributionConfig() {
		return new DistributionConfiguration(DistributionHandler.class,
				NsgaIISimpleMerger.class, NsgaIIBestResultGetter.class, 2000);
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
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
				false);
		config = mapper.readValue(new File(LOCAL_OUTPUT_NAME),
				BiohadoopConfiguration.class);
		LOG.info(config.toString());

		config = mapper.readValue(new File(REMOTE_OUTPUT_NAME),
				BiohadoopConfiguration.class);
		LOG.info(config.toString());
	}
}
