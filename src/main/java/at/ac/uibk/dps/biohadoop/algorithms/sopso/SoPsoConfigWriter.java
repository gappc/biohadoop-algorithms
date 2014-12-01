package at.ac.uibk.dps.biohadoop.algorithms.sopso;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.uibk.dps.biohadoop.algorithm.AlgorithmConfiguration;
import at.ac.uibk.dps.biohadoop.hadoop.BiohadoopConfiguration;
import at.ac.uibk.dps.biohadoop.hadoop.BiohadoopConfigurationUtil;
import at.ac.uibk.dps.biohadoop.tasksystem.communication.worker.KryoWorker;

public class SoPsoConfigWriter {

	private static final Logger LOG = LoggerFactory
			.getLogger(SoPsoConfigWriter.class);

	// Define where to write the resulting config file
	private static String CONF_OUTPUT_DIR = "/sdb/studium/master-thesis/code/git/biohadoop-algorithms/conf";
	// Define name of the config file
	private static String CONF_NAME = "biohadoop-sopso";
	// Define full path for config file, that is used for running
	// Biohadoop locally (e.g. for development)
	private static String LOCAL_CONFIG_NAME = CONF_OUTPUT_DIR + "/" + CONF_NAME
			+ "-local.json";
	// Define full path for config file, that is used for running Biohadoop in a
	// Hadoop environment
	private static String HADOOP_CONFIG_NAME = CONF_OUTPUT_DIR + "/"
			+ CONF_NAME + ".json";
	// Define full path for config file, that is used for running Biohadoop in
	// UIBK environment
	private static String UIBK_CONFIG_NAME = CONF_OUTPUT_DIR + "/" + CONF_NAME
			+ "-uibk.json";

	private SoPsoConfigWriter() {
	}

	public static void main(String[] args) throws IOException,
			ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		// Build solver configuration
		AlgorithmConfiguration solverConfiguration = new AlgorithmConfiguration.Builder(
				"SOPSO", SoPso.class)
				// Add properties that are private to this algorithm instance,
				// and that are provided as arguments when the algorithm is
				// started
				.addProperty(SoPso.ITERATIONS, "100")
				.addProperty(SoPso.PARTICLE_COUNT, "30")
				.addProperty(SoPso.INERTIA, "0.4")
				.addProperty(SoPso.PERSONAL_WEIGHT, "2.0")
				.addProperty(SoPso.GLOBAL_WEIGHT, "2.0")
				.addProperty(SoPso.MATRIX_SIZE, "128")
				.build();

		// Build Biohadoop configuration
		BiohadoopConfiguration biohadoopConfiguration = new BiohadoopConfiguration.Builder()
				.addLibPath("/biohadoop/lib/")
				.addLibPath("/biohadoop/conf/")
				// .addGobalProperty(KryoConfig.KRYO_BUFFER_SIZE, "1024")
				// .addGobalProperty(KryoConfig.KRYO_MAX_BUFFER_SIZE, "200000")
				.addDefaultEndpoints().addAlgorithm(solverConfiguration)
				.addWorker(KryoWorker.class, 1).build();

		// Save configuration file for Biohadoop running in a local environment
		BiohadoopConfigurationUtil.saveLocal(biohadoopConfiguration,
				LOCAL_CONFIG_NAME);
		// Save configuration file for Biohadoop running in a Hadoop environment
		BiohadoopConfigurationUtil.saveLocal(biohadoopConfiguration,
				HADOOP_CONFIG_NAME);

		// Build UIBK configuration
		biohadoopConfiguration = new BiohadoopConfiguration.Builder()
				.addLibPath("/user/hadoop/biohadoop/lib/")
				.addLibPath("/user/hadoop/biohadoop/conf/")
				// .addGobalProperty(KryoConfig.KRYO_BUFFER_SIZE, "1024")
				// .addGobalProperty(KryoConfig.KRYO_MAX_BUFFER_SIZE,
				// "20000000")
				.addDefaultEndpoints().addAlgorithm(solverConfiguration)
				.addWorker(KryoWorker.class, 1).build();
		// Save configuration file for Biohadoop running in a Hadoop environment
		BiohadoopConfigurationUtil.saveLocal(biohadoopConfiguration,
				UIBK_CONFIG_NAME);

		// Read results to test if everything is ok
		LOG.info(BiohadoopConfigurationUtil.readLocal(LOCAL_CONFIG_NAME)
				.toString());
		LOG.info(BiohadoopConfigurationUtil.readLocal(HADOOP_CONFIG_NAME)
				.toString());
		LOG.info(BiohadoopConfigurationUtil.readLocal(UIBK_CONFIG_NAME)
				.toString());
	}
}
