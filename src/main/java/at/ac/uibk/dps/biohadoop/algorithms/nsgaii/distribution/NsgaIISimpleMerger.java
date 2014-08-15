package at.ac.uibk.dps.biohadoop.algorithms.nsgaii.distribution;

import at.ac.uibk.dps.biohadoop.handler.distribution.DataMerger;
import at.ac.uibk.dps.biohadoop.handler.distribution.IslandModelException;

public class NsgaIISimpleMerger implements DataMerger<double[]> {

	@Override
	public double[] merge(double[] o1, double[] o2) throws IslandModelException {
		throw new IslandModelException("No merging defined");
	}

}
