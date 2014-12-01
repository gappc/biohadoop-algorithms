package at.ac.uibk.dps.biohadoop.algorithms.sopso;

import java.util.Arrays;
import java.util.Random;

public class Particle {

	private int[] currentPos;
	private int[] pBest;
	private double[] speed;
	private long pBestValue;

	public Particle(int size, int matrixSize) {
		currentPos = new int[size];
		pBest = new int[size];
		speed = new double[size];
		pBestValue = Integer.MAX_VALUE;

		Random rand = new Random();
		for (int i = 0; i < size; i++) {
			int randomValue = rand.nextInt(matrixSize) + 1;
			currentPos[i] = randomValue;
			pBest[i] = randomValue;
			speed[i] = 0.0;
		}

	}

	public Particle(Particle parent) {
		int size = parent.currentPos.length; 
		this.currentPos = Arrays.copyOf(parent.currentPos, size);
		this.speed = Arrays.copyOf(parent.speed, size);
		this.pBest = Arrays.copyOf(parent.pBest, size);
		this.pBestValue = parent.pBestValue;
	}

	public int[] getCurrentPos() {
		return currentPos;
	}

	public void setCurrentPos(int[] currentPos) {
		this.currentPos = currentPos;
	}

	public double[] getSpeed() {
		return speed;
	}

	public void setSpeed(double[] speed) {
		this.speed = speed;
	}

	public int[] getPBest() {
		return pBest;
	}

	public void setPBest(int[] pBest) {
		this.pBest = pBest;
	}

	public long getPBestValue() {
		return pBestValue;
	}

	public void setPBestValue(long pBestValue) {
		this.pBestValue = pBestValue;
	}

	@Override
	public String toString() {
		return "Particle [value=" + pBestValue + ", currentPos="
				+ Arrays.toString(currentPos) + ", speed="
				+ Arrays.toString(speed) + "]";
	}
}
