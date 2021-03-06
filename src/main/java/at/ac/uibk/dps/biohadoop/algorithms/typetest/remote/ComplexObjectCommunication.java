package at.ac.uibk.dps.biohadoop.algorithms.typetest.remote;

import java.util.ArrayList;
import java.util.List;

import at.ac.uibk.dps.biohadoop.algorithms.typetest.remote.complexobject.Address;
import at.ac.uibk.dps.biohadoop.algorithms.typetest.remote.complexobject.ComplexObject;
import at.ac.uibk.dps.biohadoop.tasksystem.ComputeException;
import at.ac.uibk.dps.biohadoop.tasksystem.Worker;

public class ComplexObjectCommunication implements
		Worker<ComplexObject, ComplexObject, ComplexObject> {

	@Override
	public ComplexObject compute(ComplexObject data, ComplexObject initalData)
			throws ComputeException {
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
