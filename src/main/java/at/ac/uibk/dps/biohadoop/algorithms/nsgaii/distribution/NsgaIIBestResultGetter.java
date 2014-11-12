package at.ac.uibk.dps.biohadoop.algorithms.nsgaii.distribution;

import java.util.List;
import java.util.Random;

import at.ac.uibk.dps.biohadoop.islandmodel.IslandModelException;
import at.ac.uibk.dps.biohadoop.islandmodel.RemoteDataLoader;
import at.ac.uibk.dps.biohadoop.islandmodel.RemoteResultGetter;
import at.ac.uibk.dps.biohadoop.islandmodel.zookeeper.NodeData;

public class NsgaIIBestResultGetter implements RemoteResultGetter<Object> {

	private final RemoteDataLoader<Object> remoteDataLoader = new RemoteDataLoader<>();
	private final Random random = new Random();
	
	@Override
	public Object getRemoteData(List<NodeData> nodesData) throws IslandModelException {
		if (nodesData == null || nodesData.isEmpty()) {
			return null;
		}
		int index = random.nextInt(nodesData.size());
		return remoteDataLoader.getRemoteData(nodesData.get(index), Object.class);
	}

}
