package at.ac.uibk.dps.biohadoop.algorithms.typetest.config;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.uibk.dps.biohadoop.algorithm.AlgorithmConfiguration;
import at.ac.uibk.dps.biohadoop.algorithms.typetest.algorithm.TypeTest;
import at.ac.uibk.dps.biohadoop.algorithms.typetest.remote.kryo.KryoObjects;
import at.ac.uibk.dps.biohadoop.hadoop.BiohadoopConfiguration;
import at.ac.uibk.dps.biohadoop.hadoop.BiohadoopConfigurationUtil;
import at.ac.uibk.dps.biohadoop.tasksystem.communication.kryo.KryoRegistrator;
import at.ac.uibk.dps.biohadoop.tasksystem.communication.worker.KryoWorker;
import at.ac.uibk.dps.biohadoop.tasksystem.communication.worker.WebSocketWorker;

public class TypeTestConfigWriter {

	private static final Logger LOG = LoggerFactory
			.getLogger(TypeTestConfigWriter.class);

	private static String CONF_OUTPUT_DIR = "/sdb/studium/master-thesis/code/git/biohadoop-algorithms/conf";
	private static String CONF_NAME = "biohadoop-typetest";
	private static String LOCAL_CONFIG_NAME = CONF_OUTPUT_DIR + "/" + CONF_NAME
			+ "-local.json";
	private static String HADOOP_CONFIG_NAME = CONF_OUTPUT_DIR + "/"
			+ CONF_NAME + ".json";

	private TypeTestConfigWriter() {
	}

	public static void main(String[] args) throws IOException,
			ClassNotFoundException, InstantiationException,
			IllegalAccessException {

		AlgorithmConfiguration solverConfiguration = new AlgorithmConfiguration.Builder(
				"TYPETEST", TypeTest.class).build();
		BiohadoopConfiguration biohadoopConfiguration = new BiohadoopConfiguration.Builder()
				.addLibPath("/biohadoop/lib/")
				.addLibPath("/biohadoop/conf/")
				.addAlgorithm(solverConfiguration)
				.addDefaultEndpoints()
				.addWorker(KryoWorker.class, 1)
				.addWorker(WebSocketWorker.class, 1)
				.addGobalProperty(KryoRegistrator.KRYO_REGISTRATOR,
						KryoObjects.class.getCanonicalName()).build();

		BiohadoopConfigurationUtil.saveLocal(biohadoopConfiguration,
				LOCAL_CONFIG_NAME);
		BiohadoopConfigurationUtil.saveLocal(biohadoopConfiguration,
				HADOOP_CONFIG_NAME);

		// Read result to test if everything is ok
		LOG.info(BiohadoopConfigurationUtil.readLocal(LOCAL_CONFIG_NAME)
				.toString());
		LOG.info(BiohadoopConfigurationUtil.readLocal(HADOOP_CONFIG_NAME)
				.toString());
	}
}
