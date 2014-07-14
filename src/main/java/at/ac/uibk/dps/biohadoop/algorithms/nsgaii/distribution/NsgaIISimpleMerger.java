package at.ac.uibk.dps.biohadoop.algorithms.nsgaii.distribution;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.uibk.dps.biohadoop.handler.distribution.DataMergeException;
import at.ac.uibk.dps.biohadoop.handler.distribution.DataMerger;

public class NsgaIISimpleMerger implements DataMerger<double[]> {

	private static final Logger LOG = LoggerFactory.getLogger(NsgaIISimpleMerger.class);
	
	@Override
	public double[] merge(double[] o1, double[] o2) throws DataMergeException {
		if (o1 == null || o2 == null) {
			throw new DataMergeException(
					"Could not merge data because at least one of merging objects was null: o1="
							+ o1 + ", o2=" + o2);
		}

		LOG.error("No merging defined, returning same data");
		return o1;
	}

}
