package at.ac.uibk.dps.biohadoop.algorithms.ga.config;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.uibk.dps.biohadoop.algorithms.ga.algorithm.Ga;
import at.ac.uibk.dps.biohadoop.algorithms.ga.distribution.GaBestResultGetter;
import at.ac.uibk.dps.biohadoop.algorithms.ga.distribution.GaSimpleMerger;
import at.ac.uibk.dps.biohadoop.hadoop.BiohadoopConfiguration;
import at.ac.uibk.dps.biohadoop.hadoop.BiohadoopConfigurationUtil;
import at.ac.uibk.dps.biohadoop.islandmodel.IslandModel;
import at.ac.uibk.dps.biohadoop.islandmodel.zookeeper.ZooKeeperController;
import at.ac.uibk.dps.biohadoop.persistence.FileLoader;
import at.ac.uibk.dps.biohadoop.persistence.FileSaver;
import at.ac.uibk.dps.biohadoop.solver.SolverConfiguration;
import at.ac.uibk.dps.biohadoop.tasksystem.communication.worker.KryoWorker;
import at.ac.uibk.dps.biohadoop.tasksystem.communication.worker.LocalWorker;
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
		SolverConfiguration.Builder builder = new SolverConfiguration.Builder(
				"GA", Ga.class);
		builder.addProperty(Ga.DATA_PATH, LOCAL_DATA_PATH);
		builder.addProperty(Ga.MAX_ITERATIONS, MAX_ITERATIONS);
		builder.addProperty(Ga.POPULATION_SIZE, POPULATION_SIZE);
		builder.addProperty(FileSaver.FILE_SAVE_AFTER_ITERATION,
				FILE_SAVE_AFTER_ITERATION);
		builder.addProperty(FileSaver.FILE_SAVE_PATH,
				LOCAL_PERSISTENCE_SAVE_PATH);
		builder.addProperty(FileLoader.FILE_LOAD_ON_STARTUP,
				FILE_LOAD_ON_STARTUP);
		builder.addProperty(FileLoader.FILE_LOAD_PATH,
				LOCAL_PERSISTENCE_LOAD_PATH);
		builder.addProperty(IslandModel.ISLAND_DATA_MERGER,
				GaSimpleMerger.class.getCanonicalName());
		builder.addProperty(IslandModel.ISLAND_DATA_REMOTE_RESULT_GETTER,
				GaBestResultGetter.class.getCanonicalName());

		SolverConfiguration solverConfiguration = builder.build();

		return new BiohadoopConfiguration.Builder()
				.addLibPath("/biohadoop/lib/")
				.addLibPath("/biohadoop/conf/")
				.addSolver(solverConfiguration)
				.addSolver(solverConfiguration)
				.addSolver(solverConfiguration)
				.addSolver(solverConfiguration)
				.addWorker(KryoWorker.class, 1)
				.addWorker(LocalWorker.class, 0)
				.addWorker(WebSocketWorker.class, 1)
				.addGobalProperty(ZooKeeperController.ZOOKEEPER_HOSTNAME,
						LOCAL_DISTRIBUTION_INFO_HOST)
				.addGobalProperty(ZooKeeperController.ZOOKEEPER_PORT,
						LOCAL_DISTRIBUTION_INFO_PORT).build();
	}

	private static BiohadoopConfiguration makeHadoopConfiguration() {
		SolverConfiguration.Builder builder = new SolverConfiguration.Builder(
				"GA", Ga.class);
		builder.addProperty(Ga.DATA_PATH, HADOOP_DATA_PATH);
		builder.addProperty(Ga.MAX_ITERATIONS, MAX_ITERATIONS);
		builder.addProperty(Ga.POPULATION_SIZE, POPULATION_SIZE);
		builder.addProperty(FileSaver.FILE_SAVE_AFTER_ITERATION,
				FILE_SAVE_AFTER_ITERATION);
		builder.addProperty(FileLoader.FILE_LOAD_ON_STARTUP,
				FILE_LOAD_ON_STARTUP);
		builder.addProperty(IslandModel.ISLAND_DATA_MERGER,
				GaSimpleMerger.class.getCanonicalName());
		builder.addProperty(IslandModel.ISLAND_DATA_REMOTE_RESULT_GETTER,
				GaBestResultGetter.class.getCanonicalName());
		builder.addProperty(FileSaver.FILE_SAVE_PATH,
				HADOOP_PERSISTENCE_SAVE_PATH);
		builder.addProperty(FileLoader.FILE_LOAD_PATH,
				HADOOP_PERSISTENCE_LOAD_PATH);

		SolverConfiguration solverConfiguration = builder.build();

		return new BiohadoopConfiguration.Builder()
				.addLibPath("/biohadoop/lib/")
				.addLibPath("/biohadoop/conf/")
				.addSolver(solverConfiguration)
				.addSolver(solverConfiguration)
				.addSolver(solverConfiguration)
				.addSolver(solverConfiguration)
				.addWorker(KryoWorker.class, 1)
				.addWorker(LocalWorker.class, 0)
				.addWorker(WebSocketWorker.class, 1)
				.addGobalProperty(ZooKeeperController.ZOOKEEPER_HOSTNAME,
						HADOOP_DISTRIBUTION_INFO_HOST)
				.addGobalProperty(ZooKeeperController.ZOOKEEPER_PORT,
						HADOOP_DISTRIBUTION_INFO_PORT).build();
	}
}
