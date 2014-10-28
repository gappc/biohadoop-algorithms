package at.ac.uibk.dps.biohadoop.algorithms.tiledmatrixmul;

import at.ac.uibk.dps.biohadoop.tasksystem.AsyncComputable;
import at.ac.uibk.dps.biohadoop.tasksystem.ComputeException;

/**
 * Multiplies two matrices with the tiled loop approach, and returns the time it
 * took. Both matrices are in row layout
 * 
 * @author Christian Gapp
 *
 */
public class AsyncTiledMul implements AsyncComputable<Matrices, Integer, Long> {

	@Override
	public Long compute(Integer blockSize, Matrices matrices)
			throws ComputeException {
		long start = System.nanoTime();
		MatrixMul.tiledMul(matrices.getMatrixA(), matrices.getMatrixB(), blockSize);
		return System.nanoTime() - start;
	}

}
