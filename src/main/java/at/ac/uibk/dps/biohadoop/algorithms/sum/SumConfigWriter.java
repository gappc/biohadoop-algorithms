package at.ac.uibk.dps.biohadoop.algorithms.sum;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.uibk.dps.biohadoop.algorithm.AlgorithmConfiguration;
import at.ac.uibk.dps.biohadoop.hadoop.BiohadoopConfiguration;
import at.ac.uibk.dps.biohadoop.hadoop.BiohadoopConfigurationUtil;
import at.ac.uibk.dps.biohadoop.tasksystem.communication.worker.KryoWorker;
import at.ac.uibk.dps.biohadoop.tasksystem.communication.worker.WebSocketWorker;

public class SumConfigWriter {

	private static final Logger LOG = LoggerFactory
			.getLogger(SumConfigWriter.class);

	// Define where to write the resulting config file
	private static String CONF_OUTPUT_DIR = "/sdb/studium/master-thesis/code/git/biohadoop-algorithms/conf";
	// Define name of the config file
	private static String CONF_NAME = "biohadoop-sum";
	// Define full path for config file, that is used for running
	// Biohadoop locally (e.g. for development)
	private static String LOCAL_CONFIG_NAME = CONF_OUTPUT_DIR + "/" + CONF_NAME
			+ "-local.json";
	// Define full path for config file, that is used for running Biohadoop in a
	// Hadoop environment
	private static String HADOOP_CONFIG_NAME = CONF_OUTPUT_DIR + "/"
			+ CONF_NAME + ".json";

	private SumConfigWriter() {
	}

	public static void main(String[] args) throws IOException,
			ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		// Build solver configuration
		AlgorithmConfiguration solverConfiguration = new AlgorithmConfiguration.Builder(
				"SUM", SumAlgorithm.class)
				// Add properties that are private to this algorithm instance,
				// and that are provided as arguments when the algorithm is
				// started
				.addProperty(SumAlgorithm.CHUNKS, "20")
				.addProperty(SumAlgorithm.CHUNK_SIZE, "10").build();

		// Build Biohadoop configuration
		BiohadoopConfiguration biohadoopConfiguration = new BiohadoopConfiguration.Builder()
				.addLibPath("/biohadoop/lib/")
				.addLibPath("/biohadoop/conf/")
				.addAlgorithm(solverConfiguration)
				.addDefaultEndpoints()
				// Add the workers, that should be launched by Biohadoop to
				// compute the tasks. Note that the workers, defined by the
				// addWorker(String, int) method, are part of the default
				// pipeline. Also note, that if a worker has a count of 0, no
				// instance of it is started
				.addWorker(KryoWorker.class, 0)
				.addWorker(WebSocketWorker.class, 0).build();

		// Save configuration file for Biohadoop running in a local environment
		BiohadoopConfigurationUtil.saveLocal(biohadoopConfiguration,
				LOCAL_CONFIG_NAME);
		// Save configuration file for Biohadoop running in a Hadoop environment
		BiohadoopConfigurationUtil.saveLocal(biohadoopConfiguration,
				HADOOP_CONFIG_NAME);

		// Read results to test if everything is ok
		LOG.info(BiohadoopConfigurationUtil.readLocal(LOCAL_CONFIG_NAME)
				.toString());
		LOG.info(BiohadoopConfigurationUtil.readLocal(HADOOP_CONFIG_NAME)
				.toString());
	}
}
