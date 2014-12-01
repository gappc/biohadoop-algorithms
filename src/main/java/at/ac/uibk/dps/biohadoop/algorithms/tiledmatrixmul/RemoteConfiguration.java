package at.ac.uibk.dps.biohadoop.algorithms.tiledmatrixmul;

import at.ac.uibk.dps.biohadoop.problems.tiledmul.Matrices;

public class RemoteConfiguration {

	private Matrices matrices;
	private int maxBlockSize;
	private double sbxDistributionFactor;
	private double mutationFactor;

	public RemoteConfiguration() {
		// Needed for Jackson
	}

	public RemoteConfiguration(Matrices matrices, int maxBlockSize,
			double sbxDistributionFactor, double mutationFactor) {
		this.matrices = matrices;
		this.maxBlockSize = maxBlockSize;
		this.sbxDistributionFactor = sbxDistributionFactor;
		this.mutationFactor = mutationFactor;
	}

	public Matrices getMatrices() {
		return matrices;
	}

	public int getMaxBlockSize() {
		return maxBlockSize;
	}

	public double getSbxDistributionFactor() {
		return sbxDistributionFactor;
	}

	public double getMutationFactor() {
		return mutationFactor;
	}

}
