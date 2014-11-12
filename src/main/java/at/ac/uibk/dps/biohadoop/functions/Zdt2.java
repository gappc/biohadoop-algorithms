package at.ac.uibk.dps.biohadoop.functions;

//Implemented according to http://en.wikipedia.org/wiki/Test_functions_for_optimization and http://www.tik.ee.ethz.ch/sop/download/supplementary/testproblems/zdt2/
public class Zdt2 implements Function {

	@Override
	public double f1(double[] x) {
		return x[0];
	}

	@Override
	public double f2(double[] x) {
		return g(x) * h(f1(x), g(x));
	}

	private double g(double[] x) {
		double result = 0;
		for (int i = 1; i < x.length; i++) {
			result += x[i];
		}
		return 1 + 9 / (x.length - 1) * result;
	}

	private double h(double f1_x, double g_x) {
		return 1 - Math.pow(f1_x / g_x, 2);
	}

}
