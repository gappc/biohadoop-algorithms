package at.ac.uibk.dps.biohadoop.algorithms.typetest.remote.complexobject;

import java.io.Serializable;

public class Address implements Serializable {
	
	private static final long serialVersionUID = -3747064314792164202L;
	
	private Country country;
	private String city;

	public Address() {
	}
	
	public Address(Country country, String city) {
		this.country = country;
		this.city = city;
	}

	public Country getCountry() {
		return country;
	}

	public String getCity() {
		return city;
	}

	@Override
	public String toString() {
		return "address:[country=" + country + "|city=" + city + "]";
	}

}