package at.ac.uibk.dps.biohadoop.algorithms.mopso;

import java.util.Arrays;
import java.util.Random;

public class Particle {

	private double[] currentPos;
	private double[] pBestPos;
	private double[] speed;
	private double[] currentValue;
	private double[] pBestValue;

	public Particle(int size) {
		currentPos = new double[size];
		pBestPos = new double[size];
		speed = new double[size];
		currentValue = new double[size];
		pBestValue = new double[size];

		Random rand = new Random();
		for (int i = 0; i < size; i++) {
			double randomValue = rand.nextDouble();
			currentPos[i] = randomValue;
			pBestPos[i] = randomValue;
			speed[i] = 0.0;
			currentValue[i] = Double.MAX_VALUE;
			pBestValue[i] = Double.MAX_VALUE;
		}

	}

	public Particle(Particle parent) {
		int size = parent.currentPos.length;
		this.currentPos = Arrays.copyOf(parent.currentPos, size);
		this.pBestPos = Arrays.copyOf(parent.pBestPos, size);
		this.speed = Arrays.copyOf(parent.speed, size);
		this.currentValue = Arrays.copyOf(parent.currentValue, size);
		this.pBestValue = Arrays.copyOf(parent.pBestValue, size);
	}

	public double[] getCurrentPos() {
		return currentPos;
	}

	public void setCurrentPos(double[] currentPos) {
		this.currentPos = currentPos;
	}

	public double[] getSpeed() {
		return speed;
	}

	public void setSpeed(double[] speed) {
		this.speed = speed;
	}

	public double[] getPBestPos() {
		return pBestPos;
	}

	public void setPBestPos(double[] pBestPos) {
		this.pBestPos = pBestPos;
	}

	public double[] getCurrentValue() {
		return currentValue;
	}

	public void setCurrentValue(double[] currentValue) {
		this.currentValue = currentValue;
	}

	public double[] getPBestValue() {
		return pBestValue;
	}

	public void setPBestValue(double[] pBestValue) {
		this.pBestValue = pBestValue;
	}

	@Override
	public String toString() {
		return "Particle [pBestValue=" + Arrays.toString(pBestValue)
				+ ", currentPos=" + Arrays.toString(currentPos) + ", speed="
				+ Arrays.toString(speed) + ", pBestPos="
				+ Arrays.toString(pBestPos) + "]";
	}
}
