package at.ac.uibk.dps.biohadoop.algorithms.nsgaii.communication.master;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.uibk.dps.biohadoop.algorithms.nsgaii.communication.worker.LocalNsgaIIWorker;
import at.ac.uibk.dps.biohadoop.communication.CommunicationConfiguration;
import at.ac.uibk.dps.biohadoop.communication.master.MasterLifecycle;
import at.ac.uibk.dps.biohadoop.hadoop.BiohadoopConfiguration;
import at.ac.uibk.dps.biohadoop.hadoop.Environment;
import at.ac.uibk.dps.biohadoop.hadoop.launcher.EndpointLaunchException;

public class NsgaIILocal implements MasterLifecycle {

	private static final Logger LOG = LoggerFactory.getLogger(NsgaIILocal.class);
	
	private final String workerClass = LocalNsgaIIWorker.class.getCanonicalName();
	private final ExecutorService executorService = Executors
			.newCachedThreadPool();
	private final List<LocalNsgaIIWorker> localNsgaIIWorkers = new ArrayList<>();

	@Override
	public void configure() {
		BiohadoopConfiguration biohadoopConfiguration = Environment.getBiohadoopConfiguration();
		CommunicationConfiguration communicationConfiguration = biohadoopConfiguration.getCommunicationConfiguration();
		Integer workerCount = communicationConfiguration.getWorkerEndpoints().get(workerClass);
		if (workerCount != null) {
			for (int i = 0; i < workerCount; i++) {
				localNsgaIIWorkers.add(new LocalNsgaIIWorker());
			}
		}
	}

	@Override
	public void start() throws EndpointLaunchException {
		try {
			executorService.invokeAll(localNsgaIIWorkers);
		} catch (InterruptedException e) {
			LOG.error("Error while running {}", workerClass);
			throw new EndpointLaunchException(e);
		}
	}

	@Override
	public void stop() {
		for (LocalNsgaIIWorker localNsgaIIWorker : localNsgaIIWorkers) {
			localNsgaIIWorker.stop();
		}
		executorService.shutdown();
	}

}
