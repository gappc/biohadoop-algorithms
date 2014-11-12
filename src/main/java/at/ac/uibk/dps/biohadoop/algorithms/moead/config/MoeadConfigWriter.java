package at.ac.uibk.dps.biohadoop.algorithms.moead.config;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.uibk.dps.biohadoop.algorithm.AlgorithmConfiguration;
import at.ac.uibk.dps.biohadoop.algorithms.moead.algorithm.Moead;
import at.ac.uibk.dps.biohadoop.algorithms.moead.distribution.MoeadBestResultGetter;
import at.ac.uibk.dps.biohadoop.algorithms.moead.distribution.MoeadSimpleMerger;
import at.ac.uibk.dps.biohadoop.functions.KryoFunctionObjects;
import at.ac.uibk.dps.biohadoop.functions.Zdt1;
import at.ac.uibk.dps.biohadoop.functions.Zdt3;
import at.ac.uibk.dps.biohadoop.hadoop.BiohadoopConfiguration;
import at.ac.uibk.dps.biohadoop.hadoop.BiohadoopConfigurationUtil;
import at.ac.uibk.dps.biohadoop.islandmodel.IslandModel;
import at.ac.uibk.dps.biohadoop.tasksystem.communication.kryo.KryoRegistrator;
import at.ac.uibk.dps.biohadoop.tasksystem.communication.worker.KryoWorker;
import at.ac.uibk.dps.biohadoop.tasksystem.communication.worker.WebSocketWorker;

public class MoeadConfigWriter {

	private static final Logger LOG = LoggerFactory
			.getLogger(MoeadConfigWriter.class);

	private static String CONF_OUTPUT_DIR = "/sdb/studium/master-thesis/code/git/biohadoop-algorithms/conf";
	private static String CONF_NAME = "biohadoop-moead";

	private static String LOCAL_CONFIG_NAME = CONF_OUTPUT_DIR + "/" + CONF_NAME
			+ "-local.json";
	private static String LOCAL_PERSISTENCE_SAVE_PATH = "/tmp/biohadoop/moead";
	private static String LOCAL_PERSISTENCE_LOAD_PATH = "/tmp/biohadoop/moead/MOEAD-LOCAL-1/2087724778";

	private static String HADOOP_CONFIG_NAME = CONF_OUTPUT_DIR + "/"
			+ CONF_NAME + ".json";
	private static String HADOOP_PERSISTENCE_SAVE_PATH = "/biohadoop/persistence/moead";
	private static String HADOOP_PERSISTENCE_LOAD_PATH = "/biohadoop/persistence/moead";

	private static String GENOME_SIZE = "10";
	private static String MAX_ITERATIONS = "1000";
	private static String NEIGHBOR_SIZE = "290";
	private static String POPULATION_SIZE = "300";
	private static String FILE_SAVE_AFTER_ITERATION = "100";
	private static String FILE_LOAD_ON_STARTUP = "false";

	private MoeadConfigWriter() {
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
				"MOEAD", Moead.class);

		builder.addProperty(Moead.FUNCTION_CLASS, Zdt1.class.getCanonicalName());
		builder.addProperty(Moead.GENOME_SIZE, GENOME_SIZE);
		builder.addProperty(Moead.MAX_ITERATIONS, MAX_ITERATIONS);
		builder.addProperty(Moead.NEIGHBOR_SIZE, NEIGHBOR_SIZE);
		builder.addProperty(Moead.POPULATION_SIZE, POPULATION_SIZE);
		builder.addProperty(IslandModel.ISLAND_DATA_MERGER,
				MoeadSimpleMerger.class.getCanonicalName());
		builder.addProperty(IslandModel.ISLAND_DATA_REMOTE_RESULT_GETTER,
				MoeadBestResultGetter.class.getCanonicalName());

		AlgorithmConfiguration solverConfiguration = builder.build();

		return new BiohadoopConfiguration.Builder()
				.addLibPath("/biohadoop/lib/")
				.addLibPath("/biohadoop/conf/")
				.addAlgorithm(solverConfiguration)
				.addDefaultEndpoints()
				.addWorker(KryoWorker.class, 1)
				.addWorker(WebSocketWorker.class, 1)
				.addGobalProperty(KryoRegistrator.KRYO_REGISTRATOR,
						KryoFunctionObjects.class.getCanonicalName()).build();
	}

	private static BiohadoopConfiguration makeHadoopConfiguration() {
		AlgorithmConfiguration.Builder builder = new AlgorithmConfiguration.Builder(
				"MOEAD", Moead.class);

		builder.addProperty(Moead.FUNCTION_CLASS, Zdt3.class.getCanonicalName());
		builder.addProperty(Moead.GENOME_SIZE, GENOME_SIZE);
		builder.addProperty(Moead.MAX_ITERATIONS, MAX_ITERATIONS);
		builder.addProperty(Moead.NEIGHBOR_SIZE, NEIGHBOR_SIZE);
		builder.addProperty(Moead.POPULATION_SIZE, POPULATION_SIZE);
		builder.addProperty(IslandModel.ISLAND_DATA_MERGER,
				MoeadSimpleMerger.class.getCanonicalName());
		builder.addProperty(IslandModel.ISLAND_DATA_REMOTE_RESULT_GETTER,
				MoeadBestResultGetter.class.getCanonicalName());

		AlgorithmConfiguration solverConfiguration = builder.build();

		return new BiohadoopConfiguration.Builder()
				.addLibPath("/biohadoop/lib/")
				.addLibPath("/biohadoop/conf/")
				.addAlgorithm(solverConfiguration)
				.addDefaultEndpoints()
				.addWorker(KryoWorker.class, 1)
				.addWorker(WebSocketWorker.class, 1)
				.addGobalProperty(KryoRegistrator.KRYO_REGISTRATOR,
						KryoFunctionObjects.class.getCanonicalName()).build();
	}
}
