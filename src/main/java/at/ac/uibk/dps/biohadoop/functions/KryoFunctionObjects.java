package at.ac.uibk.dps.biohadoop.functions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import at.ac.uibk.dps.biohadoop.tasksystem.communication.kryo.KryoRegistrator;

import com.esotericsoftware.kryo.Serializer;

public class KryoFunctionObjects implements KryoRegistrator{

	@Override
	public List<Class<? extends Object>> getRegistrationObjects() {
		List<Class<? extends Object>> result = new ArrayList<Class<? extends Object>>();
		result.add(Zdt1.class);
		result.add(Zdt2.class);
		result.add(Zdt3.class);
		result.add(Zdt4.class);
		result.add(Zdt6.class);
		return result;
	}

	@Override
	public Map<Class<? extends Object>, Serializer<?>> getRegistrationObjectsWithSerializer() {
		// TODO Auto-generated method stub
		return null;
	}

}
