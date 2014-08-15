package at.ac.uibk.dps.biohadoop.functions;

public class Zdt4 implements Function {

	@Override
	public double f1(double[] x) {
		return x[0];
	}

	public double f2(double[] x) {
		return g(x) * h(f1(x), g(x));
	}

	private double g(double[] x) {
		double result = 0;
		for (int i = 1; i < x.length; i++) {
			result += Math.pow(x[i], 2) - 10 * Math.cos(4 * Math.PI * x[i]);
		}
		return 91 + result;
	}

	private double h(double f1_x, double g_x) {
		return 1 - Math.sqrt(f1_x / g_x);
	}

}
