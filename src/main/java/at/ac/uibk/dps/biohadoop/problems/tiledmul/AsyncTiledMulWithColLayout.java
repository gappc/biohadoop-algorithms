package at.ac.uibk.dps.biohadoop.problems.tiledmul;

import at.ac.uibk.dps.biohadoop.tasksystem.Worker;
import at.ac.uibk.dps.biohadoop.tasksystem.ComputeException;

/**
 * Multiplies two matrices with the tiled loop approach, and returns the time it
 * took. Matrix A is in row layout, matrix B is in col layout. This way, a
 * better CPU cache utilization is achieved
 * 
 * @author Christian Gapp
 *
 */

public class AsyncTiledMulWithColLayout implements
		Worker<Matrices, int[], Long> {

	@Override
	public Long compute(int[] blocks, Matrices matrices)
			throws ComputeException {
		long start = System.nanoTime();
		MatrixMul.tiledMulWithColLayout(matrices.getMatrixA(),
				matrices.getMatrixB(), blocks);
		return System.nanoTime() - start;
	}

}
