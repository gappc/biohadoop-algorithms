package at.ac.uibk.dps.biohadoop.algorithms.ga;

import java.util.List;
import java.util.concurrent.Future;

import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.uibk.dps.biohadoop.hadoop.BiohadoopConfiguration;
import at.ac.uibk.dps.biohadoop.hadoop.BiohadoopConfigurationUtil;
import at.ac.uibk.dps.biohadoop.hadoop.launcher.SolverLauncher;
import at.ac.uibk.dps.biohadoop.hadoop.launcher.MasterLauncher;
import at.ac.uibk.dps.biohadoop.solver.SolverId;

public class GaMain {

	private static final Logger LOG = LoggerFactory.getLogger(GaMain.class);

	private GaMain() {
	}

	public static void main(String[] args) {
		try {
			YarnConfiguration yarnConfiguration = new YarnConfiguration();
			BiohadoopConfiguration biohadoopConfiguration = BiohadoopConfigurationUtil
					.read(yarnConfiguration, args[0]);

			List<Future<SolverId>> algorithms = SolverLauncher
					.launchSolver(biohadoopConfiguration);

			MasterLauncher masterLauncher = new MasterLauncher(
					biohadoopConfiguration);
			masterLauncher.startMasterEndpoints();

			for (Future<SolverId> algorithm : algorithms) {
				SolverId solverId = algorithm.get();
				LOG.info("{} finished", solverId);
			}
		} catch (Exception e) {
			LOG.error("Exception while running GaMain", e);
		}
	}
}
