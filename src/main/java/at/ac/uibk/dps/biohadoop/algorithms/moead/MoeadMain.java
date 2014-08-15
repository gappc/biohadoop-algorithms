package at.ac.uibk.dps.biohadoop.algorithms.moead;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Future;

import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.uibk.dps.biohadoop.datastore.DataOptions;
import at.ac.uibk.dps.biohadoop.datastore.DataService;
import at.ac.uibk.dps.biohadoop.hadoop.BiohadoopConfiguration;
import at.ac.uibk.dps.biohadoop.hadoop.BiohadoopConfigurationUtil;
import at.ac.uibk.dps.biohadoop.hadoop.launcher.MasterLauncher;
import at.ac.uibk.dps.biohadoop.hadoop.launcher.SolverLauncher;
import at.ac.uibk.dps.biohadoop.solver.SolverId;

public class MoeadMain {

	private static final Logger LOG = LoggerFactory.getLogger(MoeadMain.class);

	private MoeadMain() {
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

				double[][] solution = (double[][]) DataService.getInstance()
						.getData(solverId, DataOptions.DATA);
				saveToFile("/tmp/moead-sol.txt", solution);
			}
		} catch (Exception e) {
			LOG.error("Exception while running MoeadMain", e);
		}
	}

	private static void saveToFile(String filename, double[][] solution) {
		try {
			BufferedWriter br = new BufferedWriter(new FileWriter(filename));
			for (double[] l : solution) {
				br.write(l[0] + " " + l[1] + "\n");
			}
			br.flush();
			br.close();
		} catch (IOException e) {
			LOG.error("Exception while saving Moead data to file {}", filename,
					e);
		}
	}
}
