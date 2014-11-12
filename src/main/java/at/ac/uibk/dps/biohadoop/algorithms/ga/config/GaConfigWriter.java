package at.ac.uibk.dps.biohadoop.algorithms.ga.config;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.uibk.dps.biohadoop.algorithm.AlgorithmConfiguration;
import at.ac.uibk.dps.biohadoop.algorithms.ga.algorithm.Ga;
import at.ac.uibk.dps.biohadoop.algorithms.ga.distribution.GaBestResultGetter;
import at.ac.uibk.dps.biohadoop.algorithms.ga.distribution.GaSimpleMerger;
import at.ac.uibk.dps.biohadoop.hadoop.BiohadoopConfiguration;
import at.ac.uibk.dps.biohadoop.hadoop.BiohadoopConfigurationUtil;
import at.ac.uibk.dps.biohadoop.islandmodel.IslandModel;
import at.ac.uibk.dps.biohadoop.islandmodel.zookeeper.ZooKeeperController;
import at.ac.uibk.dps.biohadoop.tasksystem.communication.worker.KryoWorker;
import at.ac.uibk.dps.biohadoop.tasksystem.communication.worker.WebSocketWorker;

public class GaConfigWriter {

	private static final Logger LOG = LoggerFactory
			.getLogger(GaConfigWriter.class);

	private static String CONF_OUTPUT_DIR = "/sdb/studium/master-thesis/code/git/biohadoop-algorithms/conf";
	private static String CONF_NAME = "biohadoop-ga";

	private static String LOCAL_CONFIG_NAME = CONF_OUTPUT_DIR + "/" + CONF_NAME
			+ "-local.json";
	private static String LOCAL_PERSISTENCE_SAVE_PATH = "/tmp/biohadoop/ga";
	private static String LOCAL_PERSISTENCE_LOAD_PATH = "/tmp/biohadoop/ga/GA-LOCAL-1/481038014/";
	private static String LOCAL_DISTRIBUTION_INFO_HOST = "localhost";
	private static String LOCAL_DISTRIBUTION_INFO_PORT = "2181";

	private static String HADOOP_CONFIG_NAME = CONF_OUTPUT_DIR + "/"
			+ CONF_NAME + ".json";
	private static String HADOOP_PERSISTENCE_SAVE_PATH = "/biohadoop/persistence/ga";
	private static String HADOOP_PERSISTENCE_LOAD_PATH = "/biohadoop/persistence/ga";
	private static String HADOOP_DISTRIBUTION_INFO_HOST = "master";
	private static String HADOOP_DISTRIBUTION_INFO_PORT = "2181";

	private static String LOCAL_DATA_PATH = "/sdb/studium/master-thesis/code/git/biohadoop-algorithms/data/att48.tsp";
	private static String HADOOP_DATA_PATH = "/biohadoop/data/att48.tsp";
	private static String MAX_ITERATIONS = "10000";
	private static String POPULATION_SIZE = "10";
	private static String FILE_SAVE_AFTER_ITERATION = "1000";
	private static String FILE_LOAD_ON_STARTUP = "false";

	private GaConfigWriter() {
	}

	public static void main(String[] args) throws IOException,
			ClassNotFoundException, InstantiationException,
			IllegalAccessException {

		BiohadoopConfiguration localConfiguration = makeLocalConfiguration();
		BiohadoopConfiguration hadoopConfiguration = makeHadoopConfiguration();

		BiohadoopConfigurationUtil.saveLocal(localConfiguration,
				LOCAL_CONFIG_NAME);
		BiohadoopConfigurationUtil.saveLocal(hadoopConfiguration,
				HADOOP_CONFIG_NAME);

		// Read result to test if everything is ok
		LOG.info(BiohadoopConfigurationUtil.readLocal(LOCAL_CONFIG_NAME)
				.toString());
		LOG.info(BiohadoopConfigurationUtil.readLocal(HADOOP_CONFIG_NAME)
				.toString());
	}

	private static BiohadoopConfiguration makeLocalConfiguration() {
		AlgorithmConfiguration.Builder builder = new AlgorithmConfiguration.Builder(
				"GA", Ga.class);
		builder.addProperty(Ga.DATA_PATH, LOCAL_DATA_PATH);
		builder.addProperty(Ga.MAX_ITERATIONS, MAX_ITERATIONS);
		builder.addProperty(Ga.POPULATION_SIZE, POPULATION_SIZE);
		builder.addProperty(IslandModel.ISLAND_DATA_MERGER,
				GaSimpleMerger.class.getCanonicalName());
		builder.addProperty(IslandModel.ISLAND_DATA_REMOTE_RESULT_GETTER,
				GaBestResultGetter.class.getCanonicalName());

		AlgorithmConfiguration solverConfiguration = builder.build();

		return new BiohadoopConfiguration.Builder()
				.addLibPath("/biohadoop/lib/")
				.addLibPath("/biohadoop/conf/")
				.addAlgorithm(solverConfiguration)
				.addAlgorithm(solverConfiguration)
				.addAlgorithm(solverConfiguration)
				.addAlgorithm(solverConfiguration)
				.addDefaultEndpoints()
				.addWorker(KryoWorker.class, 1)
				.addWorker(WebSocketWorker.class, 1)
				.addGobalProperty(ZooKeeperController.ZOOKEEPER_HOSTNAME,
						LOCAL_DISTRIBUTION_INFO_HOST)
				.addGobalProperty(ZooKeeperController.ZOOKEEPER_PORT,
						LOCAL_DISTRIBUTION_INFO_PORT).build();
	}

	private static BiohadoopConfiguration makeHadoopConfiguration() {
		AlgorithmConfiguration.Builder builder = new AlgorithmConfiguration.Builder(
				"GA", Ga.class);
		builder.addProperty(Ga.DATA_PATH, HADOOP_DATA_PATH);
		builder.addProperty(Ga.MAX_ITERATIONS, MAX_ITERATIONS);
		builder.addProperty(Ga.POPULATION_SIZE, POPULATION_SIZE);
		builder.addProperty(IslandModel.ISLAND_DATA_MERGER,
				GaSimpleMerger.class.getCanonicalName());
		builder.addProperty(IslandModel.ISLAND_DATA_REMOTE_RESULT_GETTER,
				GaBestResultGetter.class.getCanonicalName());

		AlgorithmConfiguration solverConfiguration = builder.build();

		return new BiohadoopConfiguration.Builder()
				.addLibPath("/biohadoop/lib/")
				.addLibPath("/biohadoop/conf/")
				.addAlgorithm(solverConfiguration)
				.addAlgorithm(solverConfiguration)
				.addAlgorithm(solverConfiguration)
				.addAlgorithm(solverConfiguration)
				.addDefaultEndpoints()
				.addWorker(KryoWorker.class, 1)
				.addWorker(WebSocketWorker.class, 1)
				.addGobalProperty(ZooKeeperController.ZOOKEEPER_HOSTNAME,
						HADOOP_DISTRIBUTION_INFO_HOST)
				.addGobalProperty(ZooKeeperController.ZOOKEEPER_PORT,
						HADOOP_DISTRIBUTION_INFO_PORT).build();
	}
}
