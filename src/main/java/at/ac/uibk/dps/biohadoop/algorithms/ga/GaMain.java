package at.ac.uibk.dps.biohadoop.algorithms.ga;

import java.util.List;
import java.util.concurrent.Future;

import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.uibk.dps.biohadoop.algorithm.AlgorithmId;
import at.ac.uibk.dps.biohadoop.hadoop.BiohadoopConfiguration;
import at.ac.uibk.dps.biohadoop.hadoop.BiohadoopConfigurationUtil;
import at.ac.uibk.dps.biohadoop.hadoop.launcher.AlgorithmLauncher;
import at.ac.uibk.dps.biohadoop.hadoop.launcher.EndpointLauncher;

public class GaMain {

	private static final Logger LOG = LoggerFactory.getLogger(GaMain.class);

	private GaMain() {
	}

	public static void main(String[] args) {
		try {
			YarnConfiguration yarnConfiguration = new YarnConfiguration();
			BiohadoopConfiguration biohadoopConfiguration = BiohadoopConfigurationUtil
					.read(yarnConfiguration, args[0]);

			List<Future<AlgorithmId>> algorithms = AlgorithmLauncher
					.launchAlgorithm(biohadoopConfiguration);

			EndpointLauncher masterLauncher = new EndpointLauncher(
					biohadoopConfiguration);
			masterLauncher.startEndpoints();

			for (Future<AlgorithmId> algorithm : algorithms) {
				AlgorithmId solverId = algorithm.get();
				LOG.info("{} finished", solverId);
			}
		} catch (Exception e) {
			LOG.error("Exception while running GaMain", e);
		}
	}
}
