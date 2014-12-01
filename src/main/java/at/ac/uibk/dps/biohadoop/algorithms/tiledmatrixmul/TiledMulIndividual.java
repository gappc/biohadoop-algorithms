package at.ac.uibk.dps.biohadoop.algorithms.tiledmatrixmul;


public class TiledMulIndividual {

	private int[] individual;
	private Long objective;
	
	public TiledMulIndividual() {
		// Needed for Jackson
	}

	public TiledMulIndividual(int[] individual, Long objective) {
		this.individual = individual;
		this.objective = objective;
	}
	
	public int[] getIndividual() {
		return individual;
	}

	public Long getObjective() {
		return objective;
	}

}
