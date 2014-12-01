package at.ac.uibk.dps.biohadoop.algorithms.nsgaii.algorithm;


public class Individual {

	private double[] individual;
	private double[] objectives;
	
	public Individual() {
		// Needed for Jackson
	}

	public Individual(double[] individual, double[] objectives) {
		this.individual = individual;
		this.objectives = objectives;
	}
	
	public double[] getIndividual() {
		return individual;
	}

	public double[] getObjectives() {
		return objectives;
	}

}
