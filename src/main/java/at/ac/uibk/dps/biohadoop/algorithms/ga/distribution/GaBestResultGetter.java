package at.ac.uibk.dps.biohadoop.algorithms.ga.distribution;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import at.ac.uibk.dps.biohadoop.islandmodel.IslandModelException;
import at.ac.uibk.dps.biohadoop.islandmodel.RemoteDataLoader;
import at.ac.uibk.dps.biohadoop.islandmodel.RemoteResultGetter;
import at.ac.uibk.dps.biohadoop.islandmodel.zookeeper.NodeData;

public class GaBestResultGetter implements RemoteResultGetter<GaData> {

	private final RemoteDataLoader<GaData> remoteDataLoader = new RemoteDataLoader<>();
	
	@Override
	public GaData getRemoteData(List<NodeData> nodesData) throws IslandModelException {
		if (nodesData == null || nodesData.isEmpty()) {
			return null;
		}
		List<GaData> datas = remoteDataLoader.getRemoteDatas(nodesData, GaData.class);
		Collections.sort(datas, new Comparator<GaData>() {
			@Override
			public int compare(GaData o1, GaData o2) {
				return (int)Math.signum(o1.getFitness() - o2.getFitness());
			}
		});
		return datas.get(0);
	}

}
