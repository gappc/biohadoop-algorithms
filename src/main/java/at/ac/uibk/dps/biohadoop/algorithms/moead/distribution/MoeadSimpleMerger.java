package at.ac.uibk.dps.biohadoop.algorithms.moead.distribution;

import at.ac.uibk.dps.biohadoop.islandmodel.DataMerger;
import at.ac.uibk.dps.biohadoop.islandmodel.IslandModelException;

public class MoeadSimpleMerger implements DataMerger<double[][]> {

	@Override
	public double[][] merge(double[][] o1, double[][] o2)
			throws IslandModelException {
		throw new IslandModelException("No merging defined");
	}

}
