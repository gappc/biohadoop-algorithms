package at.ac.uibk.dps.biohadoop.algorithms.ga.distribution;

public class GaData {

	private int[][] population;
	private int iteration;
	private double fitness;

	public GaData() {
	}

	public GaData(int[][] population, int iteration, double fitness) {
		this.population = population;
		this.iteration = iteration;
		this.fitness = fitness;
	}

	public int[][] getPopulation() {
		return population;
	}

	public int getIteration() {
		return iteration;
	}

	public double getFitness() {
		return fitness;
	}
	
}
