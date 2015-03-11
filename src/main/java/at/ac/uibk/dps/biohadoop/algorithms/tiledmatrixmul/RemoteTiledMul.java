package at.ac.uibk.dps.biohadoop.algorithms.tiledmatrixmul;

import java.util.Random;

import at.ac.uibk.dps.biohadoop.operators.ParamterBasedMutator;
import at.ac.uibk.dps.biohadoop.operators.SBX;
import at.ac.uibk.dps.biohadoop.problems.tiledmul.MatrixMul;
import at.ac.uibk.dps.biohadoop.tasksystem.Worker;
import at.ac.uibk.dps.biohadoop.tasksystem.ComputeException;

/**
 * Multiplies two matrices with the tiled loop approach, and returns the time it
 * took. Both matrices are in row layout
 * 
 * @author Christian Gapp
 *
 */
public class RemoteTiledMul implements Worker<RemoteConfiguration, int[][], TiledMulIndividual> {

	private final Random rand = new Random();
	
	@Override
	public TiledMulIndividual compute(int[][] parents, RemoteConfiguration config)
			throws ComputeException {
		int[] parent1 = parents[0];
		int[] parent2 = parents[1];

		// Generate offspring
		int[] offspring = null;
		int[][] children = SBX.bounded(config.getSbxDistributionFactor(),
				parent1, parent2, 1, config.getMaxBlockSize());

		// Each child has 50% probability to get chosen
		if (rand.nextDouble() < 0.5) {
			offspring = children[0];
		} else {
			offspring = children[1];
		}

		// Mutate offspring
		int index = rand.nextInt(offspring.length);
		offspring[index] = ParamterBasedMutator.mutate(offspring[index],
				config.getMutationFactor(), 1, config.getMaxBlockSize());
		
		
		long start = System.nanoTime();
		MatrixMul.tiledMul(config.getMatrices().getMatrixA(), config.getMatrices().getMatrixB(), offspring);
		return new TiledMulIndividual(offspring, System.nanoTime() - start);
	}

}
