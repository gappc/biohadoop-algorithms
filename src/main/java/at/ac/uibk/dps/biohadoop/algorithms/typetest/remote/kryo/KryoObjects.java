package at.ac.uibk.dps.biohadoop.algorithms.typetest.remote.kryo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import at.ac.uibk.dps.biohadoop.algorithms.typetest.remote.complexobject.Address;
import at.ac.uibk.dps.biohadoop.algorithms.typetest.remote.complexobject.ComplexObject;
import at.ac.uibk.dps.biohadoop.algorithms.typetest.remote.complexobject.Country;
import at.ac.uibk.dps.biohadoop.utils.KryoRegistrator;

import com.esotericsoftware.kryo.Serializer;

public class KryoObjects implements KryoRegistrator {

	@Override
	public List<Class<? extends Object>> getRegistrationObjects() {
		List<Class<? extends Object>> result = new ArrayList<Class<? extends Object>>();
		result.add(ComplexObject.class);
		result.add(Address.class);
		result.add(ArrayList.class);
		result.add(Country.class);
		result.add(Date.class);
		return result;
	}

	@Override
	public Map<Class<? extends Object>, Serializer<?>> getRegistrationObjectsWithSerializer() {
		return null;
//		Map<Class<? extends Object>, Serializer<?>> result = new HashMap<>();
//		result.put(Arrays.asList().getClass(), new ArraysAsListSerializer());
//		return result;
	}

}
