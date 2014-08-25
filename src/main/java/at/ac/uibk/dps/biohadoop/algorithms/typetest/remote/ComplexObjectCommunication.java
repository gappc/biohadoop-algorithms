package at.ac.uibk.dps.biohadoop.algorithms.typetest.remote;

import java.util.ArrayList;
import java.util.List;

import at.ac.uibk.dps.biohadoop.algorithms.typetest.remote.complexobject.Address;
import at.ac.uibk.dps.biohadoop.algorithms.typetest.remote.complexobject.ComplexObject;
import at.ac.uibk.dps.biohadoop.communication.RemoteExecutable;

public class ComplexObjectCommunication implements
		RemoteExecutable<ComplexObject, ComplexObject, ComplexObject> {

//	@Override
//	public ComplexObject getInitalData() {
//		return ComplexObject.buildRandom();
//	}

	@Override
	public ComplexObject compute(ComplexObject data, ComplexObject initalData) {
		int age = (initalData.getAge() + data.getAge()) / 2;
		String name = initalData.getName() + " - " + data.getName();
		List<Address> addresses = new ArrayList<>();
		addresses.addAll(initalData.getAddresses());
		addresses.addAll(data.getAddresses());
		double gpsx = (initalData.getGps()[0] + data.getGps()[0]) / 2;
		double gpsy = (initalData.getGps()[1] + data.getGps()[1]) / 2;
		double[] gps = new double[] { gpsx, gpsy };
		return new ComplexObject(age, name, addresses, gps);
	}

}
