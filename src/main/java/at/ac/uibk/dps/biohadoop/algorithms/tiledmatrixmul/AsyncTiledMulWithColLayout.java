package at.ac.uibk.dps.biohadoop.algorithms.tiledmatrixmul;

import at.ac.uibk.dps.biohadoop.tasksystem.AsyncComputable;
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
		AsyncComputable<Matrices, Integer, Long> {

	@Override
	public Long compute(Integer blockSize, Matrices matrices)
			throws ComputeException {
		long start = System.nanoTime();
		MatrixMul.tiledMulWithColLayout(matrices.getMatrixA(),
				matrices.getMatrixB(), blockSize);
		return System.nanoTime() - start;
	}

}
