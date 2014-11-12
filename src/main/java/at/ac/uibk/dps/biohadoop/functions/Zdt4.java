package at.ac.uibk.dps.biohadoop.functions;

//Implemented according to http://en.wikipedia.org/wiki/Test_functions_for_optimization and http://www.tik.ee.ethz.ch/sop/download/supplementary/testproblems/zdt4/
public class Zdt4 implements Function {

	public static void main(String[] args) {
		Zdt4 zdt4 = new Zdt4();
		double[] ov = new double[]{0.7122448002537111, 0.5044828367428135, 0.02682543305717988, 0.9769573222236531, 0.4689523201288003, 0.5085100979381036, 0.5785258566724296, 0.12645780641836135, 0.8914535498312134, 0.5143437456703687, 0.9356919677599119, 0.12573696301002302, 0.026984575321700732, 0.5815772497867583, 0.5587195798718864, 0.49079705317270617, 0.6682015080400185, 0.5075691173615997, 0.3544445234191067, 0.2875013302254018, 0.6338294536598762, 0.44446535121684083, 0.22872286515995977, 0.4451684063060465, 0.685161476797619, 0.056346013673481576, 0.06930979354426936, 0.8164826374769608, 0.3745805821975724, 0.5177956223268685};
//		double[] ov = new double[]{-2.0810755012362534E-17, 6.521980775975161E-8, 2.2221767017909697E-7, 2.236307449535295E-7, 9.39392046153593E-8, 1.8062185489852394E-7, 1.7283496115247016E-7, 9.063219488415768E-8, 2.8009381933058684E-8, 1.6989155141059177E-8};
		System.out.println(zdt4.f1(ov));
		System.out.println(zdt4.f2(ov));
	}
	
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
		return 1 + 10 * (x.length - 1) + result;
	}

	private double h(double f1_x, double g_x) {
		return 1 - Math.sqrt(f1_x / g_x);
	}

}
