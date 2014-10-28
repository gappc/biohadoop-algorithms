package at.ac.uibk.dps.biohadoop.algorithms.nsgaii.config;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.uibk.dps.biohadoop.algorithms.moead.algorithm.Moead;
import at.ac.uibk.dps.biohadoop.algorithms.nsgaii.algorithm.NsgaII;
import at.ac.uibk.dps.biohadoop.algorithms.nsgaii.distribution.NsgaIIBestResultGetter;
import at.ac.uibk.dps.biohadoop.algorithms.nsgaii.distribution.NsgaIISimpleMerger;
import at.ac.uibk.dps.biohadoop.functions.KryoFunctionObjects;
import at.ac.uibk.dps.biohadoop.functions.Zdt1;
import at.ac.uibk.dps.biohadoop.hadoop.BiohadoopConfiguration;
import at.ac.uibk.dps.biohadoop.hadoop.BiohadoopConfigurationUtil;
import at.ac.uibk.dps.biohadoop.islandmodel.IslandModel;
import at.ac.uibk.dps.biohadoop.persistence.FileLoader;
import at.ac.uibk.dps.biohadoop.persistence.FileSaver;
import at.ac.uibk.dps.biohadoop.solver.SolverConfiguration;
import at.ac.uibk.dps.biohadoop.tasksystem.communication.kryo.KryoRegistrator;
import at.ac.uibk.dps.biohadoop.tasksystem.communication.worker.KryoWorker;
import at.ac.uibk.dps.biohadoop.tasksystem.communication.worker.LocalWorker;
import at.ac.uibk.dps.biohadoop.tasksystem.communication.worker.WebSocketWorker;

public class NsgaIIConfigWriter {

	private static final Logger LOG = LoggerFactory
			.getLogger(NsgaIIConfigWriter.class);

	private static String CONF_OUTPUT_DIR = "/sdb/studium/master-thesis/code/git/biohadoop-algorithms/conf";
	private static String CONF_NAME = "biohadoop-nsgaii";

	private static String LOCAL_CONFIG_NAME = CONF_OUTPUT_DIR + "/" + CONF_NAME
			+ "-local.json";
	private static String LOCAL_PERSISTENCE_SAVE_PATH = "/tmp/biohadoop/nsgaii";
	private static String LOCAL_PERSISTENCE_LOAD_PATH = "/tmp/biohadoop/nsgaii/NSGAII-LOCAL-1/1263479909";

	private static String HADOOP_CONFIG_NAME = CONF_OUTPUT_DIR + "/"
			+ CONF_NAME + ".json";
	private static String HADOOP_PERSISTENCE_SAVE_PATH = "/biohadoop/persistence/nsgaii";
	private static String HADOOP_PERSISTENCE_LOAD_PATH = "/biohadoop/persistence/nsgaii";

	private static String GENOME_SIZE = "100";
	private static String MAX_ITERATIONS = "500";
	private static String POPULATION_SIZE = "300";
	private static String FILE_SAVE_AFTER_ITERATION = "100";
	private static String FILE_LOAD_ON_STARTUP = "false";

	private NsgaIIConfigWriter() {
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
				"NSGA-II", NsgaII.class);

		builder.addProperty(Moead.FUNCTION_CLASS, Zdt1.class.getCanonicalName());
		builder.addProperty(NsgaII.GENOME_SIZE, GENOME_SIZE);
		builder.addProperty(NsgaII.MAX_ITERATIONS, MAX_ITERATIONS);
		builder.addProperty(NsgaII.POPULATION_SIZE, POPULATION_SIZE);
		builder.addProperty(FileSaver.FILE_SAVE_AFTER_ITERATION,
				FILE_SAVE_AFTER_ITERATION);
		builder.addProperty(FileSaver.FILE_SAVE_PATH,
				LOCAL_PERSISTENCE_SAVE_PATH);
		builder.addProperty(FileLoader.FILE_LOAD_ON_STARTUP,
				FILE_LOAD_ON_STARTUP);
		builder.addProperty(FileLoader.FILE_LOAD_PATH,
				LOCAL_PERSISTENCE_LOAD_PATH);
		builder.addProperty(IslandModel.ISLAND_DATA_MERGER,
				NsgaIISimpleMerger.class.getCanonicalName());
		builder.addProperty(IslandModel.ISLAND_DATA_REMOTE_RESULT_GETTER,
				NsgaIIBestResultGetter.class.getCanonicalName());

		SolverConfiguration solverConfiguration = builder.build();

		return new BiohadoopConfiguration.Builder()
				.addLibPath("/biohadoop/lib/")
				.addLibPath("/biohadoop/conf/")
				.addSolver(solverConfiguration)
				.addWorker(KryoWorker.class, 1)
				.addWorker(LocalWorker.class, 0)
				.addWorker(WebSocketWorker.class, 1)
				.addGobalProperty(KryoRegistrator.KRYO_REGISTRATOR,
						KryoFunctionObjects.class.getCanonicalName()).build();
	}

	private static BiohadoopConfiguration makeHadoopConfiguration() {
		SolverConfiguration.Builder builder = new SolverConfiguration.Builder(
				"NSGA-II", NsgaII.class);

		builder.addProperty(Moead.FUNCTION_CLASS, Zdt1.class.getCanonicalName());
		builder.addProperty(NsgaII.GENOME_SIZE, GENOME_SIZE);
		builder.addProperty(NsgaII.MAX_ITERATIONS, MAX_ITERATIONS);
		builder.addProperty(NsgaII.POPULATION_SIZE, POPULATION_SIZE);
		builder.addProperty(FileSaver.FILE_SAVE_AFTER_ITERATION,
				FILE_SAVE_AFTER_ITERATION);
		builder.addProperty(FileLoader.FILE_LOAD_ON_STARTUP,
				FILE_LOAD_ON_STARTUP);
		builder.addProperty(IslandModel.ISLAND_DATA_MERGER,
				NsgaIISimpleMerger.class.getCanonicalName());
		builder.addProperty(IslandModel.ISLAND_DATA_REMOTE_RESULT_GETTER,
				NsgaIIBestResultGetter.class.getCanonicalName());
		builder.addProperty(FileSaver.FILE_SAVE_PATH,
				HADOOP_PERSISTENCE_SAVE_PATH);
		builder.addProperty(FileLoader.FILE_LOAD_PATH,
				HADOOP_PERSISTENCE_LOAD_PATH);

		SolverConfiguration solverConfiguration = builder.build();

		return new BiohadoopConfiguration.Builder()
				.addLibPath("/biohadoop/lib/")
				.addLibPath("/biohadoop/conf/")
				.addSolver(solverConfiguration)
				.addWorker(KryoWorker.class, 1)
				.addWorker(LocalWorker.class, 0)
				.addWorker(WebSocketWorker.class, 1)
				.addGobalProperty(KryoRegistrator.KRYO_REGISTRATOR,
						KryoFunctionObjects.class.getCanonicalName()).build();
	}
}
