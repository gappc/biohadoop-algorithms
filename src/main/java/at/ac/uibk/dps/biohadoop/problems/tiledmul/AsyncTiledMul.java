package at.ac.uibk.dps.biohadoop.problems.tiledmul;

import at.ac.uibk.dps.biohadoop.tasksystem.AsyncComputable;
import at.ac.uibk.dps.biohadoop.tasksystem.ComputeException;

/**
 * Multiplies two matrices with the tiled loop approach, and returns the time it
 * took. Both matrices are in row layout
 * 
 * @author Christian Gapp
 *
 */
public class AsyncTiledMul implements AsyncComputable<Matrices, int[], Long> {

	@Override
	public Long compute(int[] blocks, Matrices matrices)
			throws ComputeException {
		long start = System.nanoTime();
		MatrixMul.tiledMul(matrices.getMatrixA(), matrices.getMatrixB(), blocks);
		return System.nanoTime() - start;
	}

}
