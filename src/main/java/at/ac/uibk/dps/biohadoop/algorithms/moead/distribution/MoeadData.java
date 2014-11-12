package at.ac.uibk.dps.biohadoop.algorithms.moead.distribution;

public class MoeadData {

	private final double[][] population;
	private final int iteration;

	public MoeadData(double[][] population, int iteration) {
		this.population = population;
		this.iteration = iteration;
	}

	public double[][] getPopulation() {
		return population;
	}

	public int getIteration() {
		return iteration;
	}
	
}
