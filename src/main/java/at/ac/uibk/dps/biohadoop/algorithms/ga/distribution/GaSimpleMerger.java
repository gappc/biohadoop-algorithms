package at.ac.uibk.dps.biohadoop.algorithms.ga.distribution;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.uibk.dps.biohadoop.islandmodel.DataMerger;
import at.ac.uibk.dps.biohadoop.islandmodel.IslandModelException;

public class GaSimpleMerger implements DataMerger<GaData> {

	private static Logger LOG = LoggerFactory.getLogger(GaSimpleMerger.class);
	
	@Override
	public GaData merge(GaData o1, GaData o2) throws IslandModelException {
		if (o1 == null || o1.getPopulation() == null) {
			throw new IslandModelException(
					"Could not merge data because local data was null: o1="
							+ o1);
		}
		if (o2 == null || o2.getPopulation() == null) {
			LOG.warn("Could not merge local with remote data because rmeote data is null. Returning local data");
			return o1;
		}

		int[][] p1 = o1.getPopulation();
		int[][] p2 = o2.getPopulation();
		int length1 = p1.length;
		int length2 = length1 > 0 ? p1[0].length : 0;
		int[][] result = new int[length1][length2];

		int halfSize = length1 / 2;
		for (int i = 0; i < halfSize; i++) {
			for (int j = 0; j < length2; j++) {
				result[i][j] = p1[i][j];
			}
		}
		for (int i = halfSize; i < length1; i++) {
			for (int j = 0; j < length2; j++) {
				result[i][j] = p2[i][j];
			}
		}
		return new GaData(result, -1, Float.NaN);
	}

}
