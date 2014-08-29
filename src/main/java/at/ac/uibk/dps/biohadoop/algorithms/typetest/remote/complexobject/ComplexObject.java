package at.ac.uibk.dps.biohadoop.algorithms.typetest.remote.complexobject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ComplexObject implements Serializable {

	private static final long serialVersionUID = -6185201906103139533L;
	
	private int age;
	private String name;
	private List<Address> addresses;
	private double[] gps;
	
	public ComplexObject() {
	}

	public ComplexObject(int age, String name, List<Address> addresses,
			double[] gps) {
		this.age = age;
		this.name = name;
		this.addresses = addresses;
		this.gps = gps;
	}

	public int getAge() {
		return age;
	}

	public String getName() {
		return name;
	}

	public List<Address> getAddresses() {
		return addresses;
	}

	public double[] getGps() {
		return gps;
	}

	public static ComplexObject buildRandom() {
		Random rand = new Random();
		int age = rand.nextInt();
		String name = "Name_" + rand.nextInt();
		List<Address> addresses = new ArrayList<>();
		Address address1 = new Address(
				Country.values()[Country.values().length - 1], "First City_"
						+ rand.nextInt());
		Address address2 = new Address(
				Country.values()[Country.values().length - 1], "Second City_"
						+ rand.nextInt());
		addresses.add(address1);
		addresses.add(address2);
		double[] gps = new double[] { rand.nextDouble(), rand.nextDouble() };
		return new ComplexObject(age, name, addresses, gps);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("ComplexObject:[age=").append(age).append("|name=")
				.append(name);

		sb.append("|addresses:[");
		for (Address address : addresses) {
			sb.append(address).append("|");
		}
		if (sb.charAt(sb.length() - 1) == '|') {
			sb.deleteCharAt(sb.length() - 1);
		}
		sb.append("]");

		sb.append("|gps:[");
		for (double value : gps) {
			sb.append(value).append("|");
		}
		if (sb.charAt(sb.length() - 1) == '|') {
			sb.deleteCharAt(sb.length() - 1);
		}
		sb.append("]]");
		return sb.toString();
	}

}
