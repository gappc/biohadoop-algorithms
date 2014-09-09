package at.ac.uibk.dps.biohadoop.algorithms.ga.distribution;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.uibk.dps.biohadoop.islandmodel.DataMerger;
import at.ac.uibk.dps.biohadoop.islandmodel.IslandModelException;

public class GaSimpleMerger implements DataMerger<int[][]> {

	private static Logger LOG = LoggerFactory.getLogger(GaSimpleMerger.class);
	
	@Override
	public int[][] merge(int[][] o1, int[][] o2) throws IslandModelException {
		if (o1 == null) {
			throw new IslandModelException(
					"Could not merge data because local data was null: o1="
							+ o1);
		}
		if (o2 == null) {
			LOG.warn("Could not merge local with remote data because rmeote data is null. Returning local data");
			return o1;
		}

		int length1 = o1.length;
		int length2 = length1 > 0 ? o1[0].length : 0;
		int[][] result = new int[length1][length2];

		int halfSize = length1 / 2;
		for (int i = 0; i < halfSize; i++) {
			for (int j = 0; j < length2; j++) {
				result[i][j] = o1[i][j];
			}
		}
		for (int i = halfSize; i < length1; i++) {
			for (int j = 0; j < length2; j++) {
				result[i][j] = o2[i][j];
			}
		}
		return result;
	}

}
