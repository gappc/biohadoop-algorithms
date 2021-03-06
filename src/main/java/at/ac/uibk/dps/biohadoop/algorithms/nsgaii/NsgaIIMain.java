package at.ac.uibk.dps.biohadoop.algorithms.nsgaii;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Future;

import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.uibk.dps.biohadoop.algorithm.AlgorithmId;
import at.ac.uibk.dps.biohadoop.hadoop.BiohadoopConfiguration;
import at.ac.uibk.dps.biohadoop.hadoop.BiohadoopConfigurationUtil;
import at.ac.uibk.dps.biohadoop.hadoop.launcher.EndpointLauncher;
import at.ac.uibk.dps.biohadoop.hadoop.launcher.AlgorithmLauncher;

public class NsgaIIMain {

	private static final Logger LOG = LoggerFactory.getLogger(NsgaIIMain.class);

	private NsgaIIMain() {
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

//				@SuppressWarnings("unchecked")
//				List<List<Double>> solution = (List<List<Double>>) DataService
//						.getInstance().getData(solverId, DataOptions.DATA);
////				String outputFilename = ((NsgaIIAlgorithmConfig) solverConfiguration
////						.getAlgorithmConfiguration()).getOutputFile();
//				saveToFile("/tmp/nsgaii-sol.txt", solution);
			}
		} catch (Exception e) {
			LOG.error("Exception while running NsgaIIMain", e);
		}
	}

	private static void saveToFile(String filename, List<List<Double>> solution) {
		try {
			BufferedWriter br = new BufferedWriter(new FileWriter(filename));
			for (List<Double> l : solution) {
				br.write(l.get(0) + " " + l.get(1) + "\n");
			}
			br.flush();
			br.close();
		} catch (IOException e) {
			LOG.error("Exception while saving NsgaII data to file {}",
					filename, e);
		}
	}
}
