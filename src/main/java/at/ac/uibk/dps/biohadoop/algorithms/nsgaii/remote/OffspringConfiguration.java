package at.ac.uibk.dps.biohadoop.algorithms.nsgaii.remote;

import at.ac.uibk.dps.biohadoop.functions.Function;

public class OffspringConfiguration {

	private Class<? extends Function> functionClass;
	private double sbxDistributionFactor;
	private double mutationFactor;

	public OffspringConfiguration() {
		// Needed for Jackson
	}
	
	public OffspringConfiguration(Class<? extends Function> functionClass,
			double sbxDistributionFactor, double mutationFactor) {
		this.functionClass = functionClass;
		this.sbxDistributionFactor = sbxDistributionFactor;
		this.mutationFactor = mutationFactor;
	}

	public Class<? extends Function> getFunctionClass() {
		return functionClass;
	}

	public double getSbxDistributionFactor() {
		return sbxDistributionFactor;
	}

	public double getMutationFactor() {
		return mutationFactor;
	}

}
