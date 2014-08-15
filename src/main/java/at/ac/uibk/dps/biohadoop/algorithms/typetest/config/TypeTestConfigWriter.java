package at.ac.uibk.dps.biohadoop.algorithms.typetest.config;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.uibk.dps.biohadoop.algorithms.typetest.algorithm.TypeTest;
import at.ac.uibk.dps.biohadoop.algorithms.typetest.remote.kryo.KryoObjects;
import at.ac.uibk.dps.biohadoop.communication.worker.DefaultKryoWorker;
import at.ac.uibk.dps.biohadoop.communication.worker.DefaultRestWorker;
import at.ac.uibk.dps.biohadoop.communication.worker.DefaultSocketWorker;
import at.ac.uibk.dps.biohadoop.communication.worker.DefaultWebSocketWorker;
import at.ac.uibk.dps.biohadoop.hadoop.BiohadoopConfiguration;
import at.ac.uibk.dps.biohadoop.hadoop.BiohadoopConfigurationUtil;
import at.ac.uibk.dps.biohadoop.solver.SolverConfiguration;
import at.ac.uibk.dps.biohadoop.utils.KryoRegistrator;

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

		SolverConfiguration solverConfiguration = new SolverConfiguration.Builder(
				"TYPETEST", TypeTest.class).build();
		BiohadoopConfiguration biohadoopConfiguration = new BiohadoopConfiguration.Builder()
				.addLibPath("/biohadoop/lib/")
				.addLibPath("/biohadoop/conf/")
				.addSolver(solverConfiguration)
				.addWorker(DefaultKryoWorker.class, 1)
				.addWorker(DefaultRestWorker.class, 1)
				.addWorker(DefaultSocketWorker.class, 1)
				.addWorker(DefaultWebSocketWorker.class, 1)
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
